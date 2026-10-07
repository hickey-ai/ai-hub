#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

projects=()
for project_dir in */; do
  project="${project_dir%/}"
  [[ -f "$project/backend/pom.xml" ]] || continue
  projects+=("$project")
done
if [[ "${#projects[@]}" -ne 102 ]]; then
  echo "Expected 102 runnable projects, found ${#projects[@]}" >&2
  exit 1
fi

for project in "${projects[@]}"; do
  for client in frontend miniprogram; do
    if [[ ( -f "$project/$client/package.json" && ! -f "$project/$client/package-lock.json" ) || ( ! -f "$project/$client/package.json" && -f "$project/$client/package-lock.json" ) ]]; then
      echo "$project/$client needs both package.json and package-lock.json for reproducible builds" >&2
      exit 1
    fi
  done
done

for project in "${projects[@]}"; do
  if [[ -f "$project/miniprogram/package-lock.json" ]]; then
    npm --prefix "$project/miniprogram" ci
    npm --prefix "$project/miniprogram" run build:mp-weixin
  fi
done

for project in "${projects[@]}"; do
  if [[ -f "$project/frontend/package-lock.json" ]]; then
    npm --prefix "$project/frontend" ci
    npm --prefix "$project/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
    mvn -f "$project/backend/pom.xml" package
    jar tf "$project/backend/target/$project-api-0.1.0.jar" | grep -qx 'BOOT-INF/classes/static/index.html'
  fi
done

for project in "${projects[@]}"; do
  if [[ ! -f "$project/frontend/package-lock.json" ]]; then
    mvn -f "$project/backend/pom.xml" package
    test -f "$project/backend/target/$project-api-0.1.0.jar"
  fi
done

echo "All ${#projects[@]} projects passed."
