default:
    @just --list

# Build and test everything
build:
    ./gradlew build

# Apply code formatting
format:
    ./gradlew spotlessApply

# Release: suggests the next version from the conventional commits and asks for the version
release:
    scripts/release.sh

# Rehearse a release with the suggested version (build + publish dry run), then revert; no commit, tag or push
release-dry:
    scripts/release.sh --dry-run

# Publish the current version; use `--github-only` if Central succeeded but the GitHub release is missing
publish *args:
    scripts/publish.sh {{args}}
