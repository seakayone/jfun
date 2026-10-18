#!/usr/bin/env bash
# Usage: scripts/release.sh [--dry-run]
# Suggests the next version from the conventional commits since the last tag (git-cliff) and asks
# which version to release. Then bumps it, builds, commits, tags, pushes, deploys to Maven Central
# and creates the GitHub release (see publish.sh).
# With --dry-run, uses the suggested version without asking, builds and rehearses the publish,
# then reverts: no commit, tag or push. The version is printed at the end. A dirty working tree
# is allowed (with a warning); a real release never runs on a dirty tree.
set -euo pipefail

dry_run=false
case "${1:-}" in
  "") ;;
  --dry-run) dry_run=true ;;
  *) echo "unknown argument: $1" >&2; exit 1 ;;
esac
[[ -z "${2:-}" ]] || { echo "unexpected argument: $2" >&2; exit 1; }

cd "$(dirname "$0")/.."

semver='^[0-9]+\.[0-9]+\.[0-9]+$'

[[ "$(git branch --show-current)" == "main" ]] || { echo "must be on main" >&2; exit 1; }
if [[ -n "$(git status --porcelain)" ]]; then
  $dry_run || { echo "working tree not clean" >&2; exit 1; }
  echo "warning: working tree not clean; the dry run includes your uncommitted changes" >&2
fi
command -v git-cliff >/dev/null || { echo "git-cliff not installed (run via mise)" >&2; exit 1; }
scripts/publish.sh --check

suggested="$(git-cliff --bumped-version 2>/dev/null || true)"
suggested="${suggested#v}"
[[ "$suggested" =~ $semver ]] || { echo "could not calculate the next version" >&2; exit 1; }

if $dry_run; then
  version="$suggested"
else
  [[ -t 0 ]] || { echo "release asks for the version and needs a terminal" >&2; exit 1; }
  last_tag="$(git describe --tags --abbrev=0)"
  echo "Commits since $last_tag:"
  git --no-pager log --format='  %s' "$last_tag..HEAD"
  read -r -p "Version to release [$suggested]: " version
  version="${version:-$suggested}"
fi

[[ "$version" =~ $semver ]] || { echo "invalid version: $version" >&2; exit 1; }
! git rev-parse "v$version" >/dev/null 2>&1 || { echo "tag v$version already exists" >&2; exit 1; }
! git ls-remote --exit-code --tags origin "v$version" >/dev/null 2>&1 || { echo "tag v$version already exists on origin" >&2; exit 1; }

if $dry_run; then
  # restore from copies, not git, so uncommitted edits to these files survive
  backup="$(mktemp -d)"
  cp version.txt README.md "$backup"/
  trap 'cp "$backup"/version.txt "$backup"/README.md . && rm -rf "$backup"' EXIT
fi

echo "$version" > version.txt
sed -i.bak -E "s/(jfun-core:)[0-9]+\.[0-9]+\.[0-9]+/\1$version/" README.md
rm -f README.md.bak

./gradlew clean build

if $dry_run; then
  scripts/publish.sh --dry-run
  echo
  echo "release-dry ok: the next release would be $version"
  exit 0
fi

if ! git diff --quiet; then
  git commit -am "chore: release $version"
fi
git tag -s "v$version" -m "v$version"
git push origin main "v$version"

scripts/publish.sh
