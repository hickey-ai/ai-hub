#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
for project in shop manage crm oa; do
  printf '=== %s ===\n' "$project"
  npm --prefix "$project/frontend" ci
  npm --prefix "$project/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
  mvn -f "$project/backend/pom.xml" package
  jar tf "$project/backend/target/$project-api-0.1.0.jar" | grep -qx 'BOOT-INF/classes/static/index.html'
done
printf 'All four projects passed.\n'
