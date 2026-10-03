#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
for project in shop barber dining selfshop; do
  npm --prefix "$project/miniprogram" ci
  npm --prefix "$project/miniprogram" run build:mp-weixin
done
for project in shop manage crm oa finance health wellness hospital school access schedule carcare parenting labbook ai html crawler erp manufacturing logistics property agriculture construction hospitality hrm service energy legal culture community cms wms b2b eldercare pharmacy insurance rental homeservice water sanitation mining forestry fishery telecom itops civic parking charging parkops fleet scenic clinic dental aesthetics rehab lis kindergarten training elearning exam library petcare petboarding petgrooming veterinary pos loyalty laundry gym photography wedding accounting contracts projectops maintenance qms coldchain freshdelivery crossborder returns realestate testops ticketops bugtrack gridops; do
  npm --prefix "$project/frontend" ci
  npm --prefix "$project/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
  mvn -f "$project/backend/pom.xml" package
  jar tf "$project/backend/target/$project-api-0.1.0.jar" | grep -qx 'BOOT-INF/classes/static/index.html'
done
for project in barber dining selfshop; do
  mvn -f "$project/backend/pom.xml" package
  test -f "$project/backend/target/$project-api-0.1.0.jar"
done
echo 'All eighty-eight projects passed.'
