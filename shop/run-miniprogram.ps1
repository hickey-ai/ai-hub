# Build the WeChat mini-program package from any working directory.
$ErrorActionPreference = 'Stop'
Push-Location (Join-Path $PSScriptRoot 'miniprogram')
try {
    npm.cmd ci
    if ($LASTEXITCODE -ne 0) { throw 'mini-program npm ci failed' }
    npm.cmd run build:mp-weixin
    if ($LASTEXITCODE -ne 0) { throw 'mini-program build failed' }
    Write-Host 'Import miniprogram/dist/build/mp-weixin into WeChat DevTools.'
} finally { Pop-Location }
