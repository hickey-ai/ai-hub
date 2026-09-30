# Build and verify every independent project from any working directory.
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    foreach ($project in @('shop', 'manage', 'crm', 'oa')) {
        Write-Host "=== $project ==="
        npm.cmd --prefix "$project/frontend" ci
        if ($LASTEXITCODE -ne 0) { throw "$project npm ci failed" }
        npm.cmd --prefix "$project/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
        if ($LASTEXITCODE -ne 0) { throw "$project frontend build failed" }
        mvn.cmd -f "$project/backend/pom.xml" package
        if ($LASTEXITCODE -ne 0) { throw "$project backend tests/package failed" }
        $jar = "$project/backend/target/$project-api-0.1.0.jar"
        $entries = & jar tf $jar
        if ($LASTEXITCODE -ne 0 -or $entries -notcontains 'BOOT-INF/classes/static/index.html') {
            throw "$project jar does not contain the frontend"
        }
    }
    Write-Host 'All four projects passed.'
} finally { Pop-Location }
