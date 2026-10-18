default:
    @just --list

# Build and test everything
build:
    ./gradlew build

# Apply code formatting
format:
    ./gradlew spotlessApply

# Release a new version, e.g. `just release 0.0.2`
release version:
    scripts/release.sh {{version}}

# Rehearse a release (build + publish dry run), then revert; no commit, tag or push
release-dry version:
    scripts/release.sh {{version}} --dry-run

# Publish the current version; use `--github-only` if Central succeeded but the GitHub release is missing
publish *args:
    scripts/publish.sh {{args}}
