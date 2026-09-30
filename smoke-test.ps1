$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
$dir = Join-Path $env:TEMP ('ai-hub-smoke-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $dir | Out-Null
try {
    foreach ($project in @('shop', 'manage', 'crm', 'oa')) {
        $port = @{ shop=18081; manage=18082; crm=18083; oa=18084 }[$project]
        $endpoint = @{ shop='products'; manage='users'; crm='customers'; oa='requests' }[$project]
        $jar = (Resolve-Path "$project/backend/target/$project-api-0.1.0.jar").Path
        $data = Join-Path $dir "$project.json"
        $runs = if ($project -eq 'shop') { 2 } else { 1 }
        for ($run = 1; $run -le $runs; $run++) {
            $proc = Start-Process java -ArgumentList @('-jar', "`"$jar`"", "--server.port=$port", "--aihub.data-file=$data") -WorkingDirectory (Resolve-Path $project).Path -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $dir "$project-$run.out") -RedirectStandardError (Join-Path $dir "$project-$run.err")
            try {
                $url = "http://127.0.0.1:$port"
                $ready = $false
                for ($retry = 0; $retry -lt 60; $retry++) {
                    if ($proc.HasExited) { throw "$project exited early; logs: $dir" }
                    try { $page = Invoke-WebRequest "$url/" -TimeoutSec 2; $ready = $true; break } catch { Start-Sleep -Milliseconds 500 }
                }
                if (-not $ready -or $page.Content -notmatch '/assets/index-[^" ]+\.js') { throw "$project page invalid; logs: $dir" }
                if ((Invoke-WebRequest "$url$($Matches[0])").StatusCode -ne 200) { throw "$project JS unavailable" }
                $items = Invoke-RestMethod "$url/api/$endpoint"
                if ($null -eq $items -or $items.Count -lt 1) { throw "$project API empty" }
                if ($project -eq 'shop') {
                    if ($run -eq 1) {
                        $stock = $items[0].stock
                        $body = @{ customer='Smoke'; email='smoke@example.com'; items=@(@{productId=1;quantity=1}) } | ConvertTo-Json -Depth 5
                        $order = Invoke-RestMethod "$url/api/orders" -Method Post -ContentType 'application/json' -Body $body
                        if ($order.id -ne 1001) { throw 'Order failed' }
                    } elseif ($items[0].stock -ne ($stock - 1)) { throw 'Stock not persisted' }
                }
                Write-Host "$project run $run OK"
            } finally {
                Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue
                $proc.WaitForExit()
            }
        }
    }
    Write-Host 'All real-process checks passed.'
} finally { Pop-Location }
