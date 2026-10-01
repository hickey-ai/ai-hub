$ErrorActionPreference='Stop'
Push-Location $PSScriptRoot
try { npm.cmd --prefix frontend ci; npm.cmd --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir; mvn.cmd -f backend/pom.xml package; java -jar backend/target/access-api-0.1.0.jar } finally { Pop-Location }
