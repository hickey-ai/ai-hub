#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
npm --prefix frontend ci
npm --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
mvn -f backend/pom.xml package
java -jar backend/target/recycling-api-0.1.0.jar
