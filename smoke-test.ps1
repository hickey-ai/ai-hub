$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
$dir = Join-Path $env:TEMP ('ai-hub-smoke-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $dir | Out-Null
try {
    $ports = @{ shop=18081; manage=18082; crm=18083; oa=18084; finance=18085; health=18086; wellness=18087; hospital=18088; school=18089; access=18090 }
    $endpoints = @{ shop='products'; manage='users'; crm='customers'; oa='requests'; finance='accounts'; health='members'; wellness='plans'; hospital='patients'; school='students'; access='doors' }
    foreach ($project in $ports.Keys) {
        $port = $ports[$project]; $endpoint = $endpoints[$project]
        $jar = (Resolve-Path "$project/backend/target/$project-api-0.1.0.jar").Path
        $data = Join-Path $dir "$project.json"
        $proc = Start-Process java -ArgumentList @('-jar', "`"$jar`"", "--server.port=$port") -WorkingDirectory (Resolve-Path $project).Path -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $dir "$project.out") -RedirectStandardError (Join-Path $dir "$project.err")
        try {
            $url = "http://127.0.0.1:$port"; $ready = $false
            for ($retry = 0; $retry -lt 60; $retry++) { if ($proc.HasExited) { throw "$project exited early; logs: $dir" }; try { $page = Invoke-WebRequest "$url/" -TimeoutSec 2; $ready = $true; break } catch { Start-Sleep -Milliseconds 500 } }
            if (-not $ready -or $page.Content -notmatch '/assets/index-[^" ]+\.js') { throw "$project page invalid; logs: $dir" }
            if ((Invoke-WebRequest "$url$($Matches[0])").StatusCode -ne 200) { throw "$project JS unavailable" }
            $items = Invoke-RestMethod "$url/api/$endpoint"; if ($null -eq $items -or $items.Count -lt 1) { throw "$project API empty" }
            Write-Host "$project OK"
        } finally { Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue; $proc.WaitForExit() }
    }
    Write-Host 'All ten real-process checks passed.'
} finally { Pop-Location }