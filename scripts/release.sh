#!/usr/bin/env bash
# Usage: scripts/release.sh <version> [--dry-run]
# Bumps the version, builds, commits, tags, pushes, then deploys to Maven Central and creates
# the GitHub release (see publish.sh).
# With --dry-run, builds and rehearses the publish, then reverts: no commit, tag or push.
set -euo pipefail

version="${1:?usage: release.sh <version> [--dry-run]}"
dry_run=false
[[ -z "${2:-}" || "$2" == "--dry-run" ]] || { echo "unknown argument: $2" >&2; exit 1; }
[[ "${2:-}" == "--dry-run" ]] && dry_run=true

[[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || { echo "invalid version: $version" >&2; exit 1; }

cd "$(dirname "$0")/.."

[[ "$(git branch --show-current)" == "main" ]] || { echo "must be on main" >&2; exit 1; }
[[ -z "$(git status --porcelain)" ]] || { echo "working tree not clean" >&2; exit 1; }
scripts/publish.sh --check
! git rev-parse "v$version" >/dev/null 2>&1 || { echo "tag v$version already exists" >&2; exit 1; }
! git ls-remote --exit-code --tags origin "v$version" >/dev/null 2>&1 || { echo "tag v$version already exists on origin" >&2; exit 1; }

$dry_run && trap 'git checkout -- version.txt README.md' EXIT

echo "$version" > version.txt
sed -i.bak -E "s/(jfun-core:)[0-9]+\.[0-9]+\.[0-9]+/\1$version/" README.md
rm -f README.md.bak

./gradlew clean build

if $dry_run; then
  scripts/publish.sh --dry-run
  exit 0
fi

if ! git diff --quiet; then
  git commit -am "chore: release $version"
fi
git tag "v$version"
git push origin main "v$version"

scripts/publish.sh
