#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
npm --prefix frontend ci
npm --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
mvn -f backend/pom.xml package
printf 'Open http://127.0.0.1:8082 (Ctrl+C to stop)\n'
exec java -jar backend/target/manage-api-0.1.0.jar
