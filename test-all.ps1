# Build and verify every independent project from any working directory.
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    foreach ($project in @('shop', 'barber', 'dining', 'selfshop')) {
        Write-Host "=== $project mini-program ==="
        npm.cmd --prefix "$project/miniprogram" ci
        if ($LASTEXITCODE -ne 0) { throw "$project mini-program npm ci failed" }
        npm.cmd --prefix "$project/miniprogram" run build:mp-weixin
        if ($LASTEXITCODE -ne 0) { throw "$project mini-program build failed" }
    }
    foreach ($project in @('shop', 'manage', 'crm', 'oa', 'finance', 'health', 'wellness', 'hospital', 'school', 'access', 'schedule', 'carcare', 'parenting', 'labbook', 'ai', 'html', 'crawler')) {
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
    foreach ($project in @('barber', 'dining', 'selfshop')) {
        Write-Host "=== $project backend ==="
        mvn.cmd -f "$project/backend/pom.xml" package
        if ($LASTEXITCODE -ne 0) { throw "$project backend tests/package failed" }
        $jar = "$project/backend/target/$project-api-0.1.0.jar"
        if (-not (Test-Path $jar)) { throw "$project jar missing" }
    }
    Write-Host 'All twenty projects passed.'
} finally { Pop-Location }
