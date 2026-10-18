#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
npm --prefix frontend ci
npm --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
mvn -f backend/pom.xml package
printf 'Open http://127.0.0.1:8182 (Ctrl+C to stop)
'
exec java -jar backend/target/authcenter-api-0.1.0.jar
