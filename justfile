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

# Release without publishing to Maven Central / GitHub
release-dry version:
    scripts/release.sh {{version}} --no-publish

# Publish the current version (use when a release was tagged but publishing failed)
publish *args:
    scripts/publish.sh {{args}}
