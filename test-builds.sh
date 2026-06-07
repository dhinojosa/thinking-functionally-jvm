#!/usr/bin/env bash
#
# Walks every project under projects/ and verifies it compiles and its tests pass,
# using whichever build tool the project is set up with (Maven, sbt, or Gradle).

set -uo pipefail

root_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
projects_dir="$root_dir/projects"

failures=()
skipped=()

for project in "$projects_dir"/*/; do
    name="$(basename "$project")"

    if [[ -f "$project/pom.xml" ]]; then
        cmd="mvn -f \"$project/pom.xml\" test"
    elif [[ -f "$project/build.sbt" ]]; then
        cmd="sbt -batch test"
    elif [[ -f "$project/build.gradle.kts" || -f "$project/build.gradle" ]]; then
        cmd="gradle test"
    else
        echo "==> $name: no recognized build file, skipping"
        skipped+=("$name")
        continue
    fi

    echo "==> $name: running '$cmd'"
    if (cd "$project" && eval "$cmd"); then
        echo "==> $name: OK"
    else
        echo "==> $name: FAILED"
        failures+=("$name")
    fi
    echo
done

echo "-----------------------------------------"
echo "Skipped (no build file): ${skipped[*]:-none}"
echo "Failed:                  ${failures[*]:-none}"
echo "-----------------------------------------"

[[ ${#failures[@]} -eq 0 ]]
