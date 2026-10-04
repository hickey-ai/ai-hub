# Build and verify every independent project from any working directory.
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    $projects = @(Get-ChildItem -Directory | Where-Object { Test-Path (Join-Path $_.FullName 'backend/pom.xml') } | Sort-Object Name)
    if ($projects.Count -ne 98) { throw "Expected 98 runnable projects, found $($projects.Count)" }
    foreach ($project in $projects) {
        foreach ($client in @('frontend', 'miniprogram')) {
            $manifest = Join-Path $project.FullName "$client/package.json"
            $lock = Join-Path $project.FullName "$client/package-lock.json"
            if ((Test-Path $manifest) -ne (Test-Path $lock)) {
                throw "$($project.Name)/$client needs both package.json and package-lock.json for reproducible builds"
            }
        }
    }

    foreach ($project in ($projects | Where-Object { Test-Path (Join-Path $_.FullName 'miniprogram/package-lock.json') })) {
        Write-Host "=== $($project.Name) mini-program ==="
        npm.cmd --prefix "$($project.Name)/miniprogram" ci
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) mini-program npm ci failed" }
        npm.cmd --prefix "$($project.Name)/miniprogram" run build:mp-weixin
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) mini-program build failed" }
    }

    foreach ($project in ($projects | Where-Object { Test-Path (Join-Path $_.FullName 'frontend/package-lock.json') })) {
        Write-Host "=== $($project.Name) ==="
        npm.cmd --prefix "$($project.Name)/frontend" ci
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) npm ci failed" }
        npm.cmd --prefix "$($project.Name)/frontend" run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) frontend build failed" }
        mvn.cmd -f "$($project.Name)/backend/pom.xml" package
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) backend tests/package failed" }
        $jar = "$($project.Name)/backend/target/$($project.Name)-api-0.1.0.jar"
        $entries = & jar tf $jar
        if ($LASTEXITCODE -ne 0 -or $entries -notcontains 'BOOT-INF/classes/static/index.html') {
            throw "$($project.Name) jar does not contain the frontend"
        }
    }

    foreach ($project in ($projects | Where-Object { -not (Test-Path (Join-Path $_.FullName 'frontend/package-lock.json')) })) {
        Write-Host "=== $($project.Name) backend ==="
        mvn.cmd -f "$($project.Name)/backend/pom.xml" package
        if ($LASTEXITCODE -ne 0) { throw "$($project.Name) backend tests/package failed" }
        $jar = "$($project.Name)/backend/target/$($project.Name)-api-0.1.0.jar"
        if (-not (Test-Path $jar)) { throw "$($project.Name) jar missing" }
    }
    Write-Host "All $($projects.Count) projects passed."
} finally { Pop-Location }
