#!/usr/bin/env bash
# Usage: scripts/publish.sh [--check]
# Stages jfun-core with Gradle, then deploys to Maven Central and creates the GitHub release with the JReleaser CLI.
# With --check, only verifies that credentials and signing keys are available.
set -euo pipefail

cd "$(dirname "$0")/.."

config="$HOME/.jreleaser/config.properties"

has_setting() {
  [[ -n "${!1:-}" ]] && return 0
  [[ -f "$config" ]] && grep -Eq "^$1=.+" "$config"
}

missing=()
for var in JRELEASER_GITHUB_TOKEN JRELEASER_MAVENCENTRAL_USERNAME JRELEASER_MAVENCENTRAL_PASSWORD; do
  has_setting "$var" || missing+=("$var")
done
for key in public.pgp private.pgp; do
  [[ -f "$HOME/.jreleaser/$key" ]] || missing+=("~/.jreleaser/$key")
done

if ((${#missing[@]})); then
  echo "missing publish settings (set in $config or the environment):" >&2
  printf '  - %s\n' "${missing[@]}" >&2
  exit 1
fi

[[ "${1:-}" == "--check" ]] && { echo "publish settings ok"; exit 0; }

export JRELEASER_PROJECT_VERSION="$(<version.txt)"
export JRELEASER_GPG_PUBLIC_KEY="${JRELEASER_GPG_PUBLIC_KEY:-$HOME/.jreleaser/public.pgp}"
export JRELEASER_GPG_SECRET_KEY="${JRELEASER_GPG_SECRET_KEY:-$HOME/.jreleaser/private.pgp}"

./gradlew :jfun-core:publish
jreleaser full-release --git-root-search "$@"
