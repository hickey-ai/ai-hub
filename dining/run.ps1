$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
  npm.cmd --prefix miniprogram ci
  if ($LASTEXITCODE -ne 0) { throw 'dining mini-program npm ci failed' }
  npm.cmd --prefix miniprogram run build:mp-weixin
  if ($LASTEXITCODE -ne 0) { throw 'dining mini-program build failed' }
  mvn.cmd -f backend/pom.xml test
  if ($LASTEXITCODE -ne 0) { throw 'dining backend tests failed' }
  mvn.cmd -f backend/pom.xml package
  if ($LASTEXITCODE -ne 0) { throw 'dining backend package failed' }
  java -jar backend/target/dining-api-0.1.0.jar
} finally { Pop-Location }
