#!/usr/bin/env bash
# Usage: scripts/release.sh <version> [--no-publish]
# Bumps the version, builds, commits, tags, pushes and publishes via JReleaser.
set -euo pipefail

version="${1:?usage: release.sh <version> [--no-publish]}"
publish=true
[[ "${2:-}" == "--no-publish" ]] && publish=false

[[ "$version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || { echo "invalid version: $version" >&2; exit 1; }

cd "$(dirname "$0")/.."

[[ "$(git branch --show-current)" == "main" ]] || { echo "must be on main" >&2; exit 1; }
[[ -z "$(git status --porcelain)" ]] || { echo "working tree not clean" >&2; exit 1; }
! git rev-parse "v$version" >/dev/null 2>&1 || { echo "tag v$version already exists" >&2; exit 1; }

echo "$version" > version.txt
sed -i.bak -E "s/(jfun-core:)[0-9]+\.[0-9]+\.[0-9]+/\1$version/" README.md
rm -f README.md.bak

./gradlew clean build

if ! git diff --quiet; then
  git commit -am "chore: release $version"
fi
git tag "v$version"
git push origin main "v$version"

if $publish; then
  ./gradlew :jfun-core:publish :jfun-core:jreleaserFullRelease
fi
