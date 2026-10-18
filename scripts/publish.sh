#!/usr/bin/env bash
# Usage: scripts/publish.sh [--check | --github-only | <jreleaser args, e.g. --dry-run>]
# Stages jfun-core with Gradle, deploys to Maven Central with the JReleaser CLI and
# creates the GitHub release with gh.
#   --check        only verify that tools, credentials and signing keys are available
#   --github-only  only create the GitHub release (recovery after Central succeeded)
#   --dry-run      passed to JReleaser; stages locally, uploads nothing, creates no release,
#                  and prints the release notes that would be used
set -euo pipefail

cd "$(dirname "$0")/.."

mode="${1:-}"
config="$HOME/.jreleaser/config.properties"

has_setting() {
  [[ -n "${!1:-}" ]] && return 0
  [[ -f "$config" ]] && grep -Eq "^$1=.+" "$config"
}

missing=()
command -v gh >/dev/null || missing+=("gh (not installed)")
command -v jreleaser >/dev/null || missing+=("jreleaser (not installed; run via mise)")
if [[ "$mode" != "--github-only" ]]; then
  for var in JRELEASER_GITHUB_TOKEN JRELEASER_MAVENCENTRAL_USERNAME JRELEASER_MAVENCENTRAL_PASSWORD; do
    has_setting "$var" || missing+=("$var")
  done
  for key in public.pgp private.pgp; do
    [[ -f "$HOME/.jreleaser/$key" ]] || missing+=("$HOME/.jreleaser/$key")
  done
fi
if command -v gh >/dev/null && ! gh auth status >/dev/null 2>&1; then
  missing+=("gh authentication (run gh auth login or set GH_TOKEN)")
fi

if ((${#missing[@]})); then
  echo "missing publish settings (credentials go in $config or the environment):" >&2
  printf '  - %s\n' "${missing[@]}" >&2
  exit 1
fi

[[ "$mode" == "--check" ]] && { echo "publish settings ok"; exit 0; }

version="$(<version.txt)"
export JRELEASER_PROJECT_VERSION="$version"
export JRELEASER_GPG_PUBLIC_KEY="${JRELEASER_GPG_PUBLIC_KEY:-$HOME/.jreleaser/public.pgp}"
export JRELEASER_GPG_SECRET_KEY="${JRELEASER_GPG_SECRET_KEY:-$HOME/.jreleaser/private.pgp}"

# Release notes come from JReleaser's conventional-commits changelog (previous tag..this tag);
# gh --generate-notes only lists merged pull requests, which direct commits to main never have.
notes="out/jreleaser/release/CHANGELOG.md"

create_github_release() {
  gh release view "v$version" >/dev/null 2>&1 && return 0
  jreleaser changelog --git-root-search
  gh release create "v$version" --title "v$version" --verify-tag --notes-file "$notes"
}

print_release_notes() {
  jreleaser changelog --git-root-search
  printf '\n===== release notes for v%s =====\n' "$version"
  cat "$notes"
  printf '\n===== end of release notes =====\n'
}

if [[ "$mode" == "--github-only" ]]; then
  create_github_release
  exit 0
fi

./gradlew :jfun-core:publish
jreleaser deploy --git-root-search "$@"

# The native JReleaser CLI cannot create tags (JGit fails under GraalVM), so release.sh
# pushes the tag and the GitHub release is created here.
if [[ " $* " == *" --dry-run "* ]]; then
  print_release_notes
else
  create_github_release
fi
