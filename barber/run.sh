#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
npm --prefix miniprogram ci
npm --prefix miniprogram run build:mp-weixin
mvn -f backend/pom.xml test
mvn -f backend/pom.xml package
exec java -jar backend/target/barber-api-0.1.0.jar
