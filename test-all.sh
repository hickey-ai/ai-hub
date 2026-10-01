#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
for project in shop barber dining selfshop; do
  npm --prefix "$project/miniprogram" ci
  npm --prefix "$project/miniprogram" run build:mp-weixin
done
for project in shop manage crm oa finance health wellness hospital school access schedule carcare parenting; do
  npm --prefix "$project/frontend" ci
  npm --prefix "$project/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
  mvn -f "$project/backend/pom.xml" package
  jar tf "$project/backend/target/$project-api-0.1.0.jar" | grep -qx 'BOOT-INF/classes/static/index.html'
done
for project in barber dining selfshop; do
  mvn -f "$project/backend/pom.xml" package
  test -f "$project/backend/target/$project-api-0.1.0.jar"
done
echo 'All sixteen projects passed.'
