[CmdletBinding()]
param([ValidateSet('shop', 'crm')][string]$Project = 'shop')

$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    & "$PSScriptRoot\$Project\run.ps1"
    if ($LASTEXITCODE -ne 0) { throw "$Project demo failed" }
} finally { Pop-Location }
