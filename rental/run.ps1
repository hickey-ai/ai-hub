$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
  npm.cmd --prefix frontend ci
  if ($LASTEXITCODE -ne 0) { throw 'npm ci failed' }
  npm.cmd --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
  if ($LASTEXITCODE -ne 0) { throw 'frontend build failed' }
  mvn.cmd -f backend/pom.xml package
  if ($LASTEXITCODE -ne 0) { throw 'backend test/package failed' }
  java -jar backend/target/rental-api-0.1.0.jar
} finally { Pop-Location }
