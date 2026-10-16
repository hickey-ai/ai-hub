$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    & "$PSScriptRoot\shop\run.ps1"
    if ($LASTEXITCODE -ne 0) { throw 'shop demo failed' }
} finally { Pop-Location }
