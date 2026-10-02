$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
$dir = Join-Path $env:TEMP ('ai-hub-smoke-' + [guid]::NewGuid().ToString('N'))
New-Item -ItemType Directory -Path $dir | Out-Null
try {
    $webProjects = @('shop','manage','crm','oa','finance','health','wellness','hospital','school','access','schedule','carcare','parenting','labbook','ai','html','crawler','erp','manufacturing','logistics','property','agriculture','construction','hospitality','hrm','service','energy','legal','culture','community','cms','wms','b2b','eldercare','pharmacy','insurance','rental','homeservice','water','sanitation','mining','forestry','fishery','telecom','itops','civic')
    $webPorts = @{ shop=18081; manage=18082; crm=18083; oa=18084; finance=18085; health=18086; wellness=18087; hospital=18088; school=18089; access=18090; schedule=18094; carcare=18095; parenting=18096; labbook=18097; ai=18098; html=18099; crawler=18100; erp=18101; manufacturing=18102; logistics=18103; property=18104; agriculture=18105; construction=18106; hospitality=18107; hrm=18108; service=18109; energy=18110; legal=18111; culture=18112; community=18113 ; cms=18114; wms=18115; b2b=18116; eldercare=18117; pharmacy=18118; insurance=18119; rental=18120; homeservice=18121; water=18122; sanitation=18123; mining=18124; forestry=18125; fishery=18126; telecom=18127; itops=18128; civic=18129 }
    $webEndpoints = @{ shop='products'; manage='users'; crm='customers'; oa='requests'; finance='accounts'; health='members'; wellness='plans'; hospital='patients'; school='students'; access='doors'; schedule='events'; carcare='vehicles'; parenting='children'; labbook='experiments'; ai='skills'; html='directory'; crawler='overview'; erp='products'; manufacturing='materials'; logistics='vehicles'; property='units'; agriculture='plots'; construction='projects'; hospitality='rooms'; hrm='employees'; service='customers'; energy='assets'; legal='clients'; culture='venues'; community='programs' ; cms='sections'; wms='bins'; b2b='suppliers'; eldercare='residents'; pharmacy='medicines'; insurance='policies'; rental='assets'; homeservice='customers'; water='stations'; sanitation='routes'; mining='sites'; forestry='parcels'; fishery='ponds'; telecom='sites'; itops='assets'; civic='services' }
    foreach ($project in $webProjects) {
        $port = $webPorts[$project]; $endpoint = $webEndpoints[$project]
        $jar = (Resolve-Path "$project/backend/target/$project-api-0.1.0.jar").Path
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
    $miniProjects = @('barber','dining','selfshop')
    $miniPorts = @{ barber=18091; dining=18092; selfshop=18093 }
    $miniEndpoints = @{ barber='services'; dining='dishes'; selfshop='products' }
    foreach ($project in $miniProjects) {
        $port = $miniPorts[$project]; $endpoint = $miniEndpoints[$project]
        $jar = (Resolve-Path "$project/backend/target/$project-api-0.1.0.jar").Path
        $proc = Start-Process java -ArgumentList @('-jar', "`"$jar`"", "--server.port=$port") -WorkingDirectory (Resolve-Path $project).Path -WindowStyle Hidden -PassThru -RedirectStandardOutput (Join-Path $dir "$project.out") -RedirectStandardError (Join-Path $dir "$project.err")
        try {
            $url = "http://127.0.0.1:$port"; $ready = $false
            for ($retry = 0; $retry -lt 60; $retry++) { if ($proc.HasExited) { throw "$project exited early; logs: $dir" }; try { $items = Invoke-RestMethod "$url/api/$endpoint" -TimeoutSec 2; $ready = $true; break } catch { Start-Sleep -Milliseconds 500 } }
            if (-not $ready -or $null -eq $items -or $items.Count -lt 1) { throw "$project API invalid; logs: $dir" }
            Write-Host "$project API OK"
        } finally { Stop-Process -Id $proc.Id -Force -ErrorAction SilentlyContinue; $proc.WaitForExit() }
    }
    Write-Host 'All forty-nine real-process checks passed.'
} finally { Pop-Location }
