$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
  npm.cmd --prefix miniprogram ci
  if ($LASTEXITCODE -ne 0) { throw 'barber mini-program npm ci failed' }
  npm.cmd --prefix miniprogram run build:mp-weixin
  if ($LASTEXITCODE -ne 0) { throw 'barber mini-program build failed' }
  mvn.cmd -f backend/pom.xml test
  if ($LASTEXITCODE -ne 0) { throw 'barber backend tests failed' }
  mvn.cmd -f backend/pom.xml package
  if ($LASTEXITCODE -ne 0) { throw 'barber backend package failed' }
  java -jar backend/target/barber-api-0.1.0.jar
} finally { Pop-Location }
