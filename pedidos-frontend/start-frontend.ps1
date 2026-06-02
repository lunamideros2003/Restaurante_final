# Libera el puerto 4200 y arranca el frontend Angular
$connections = Get-NetTCPConnection -LocalPort 4200 -ErrorAction SilentlyContinue
foreach ($conn in $connections) {
    $procId = $conn.OwningProcess
    $name = (Get-Process -Id $procId -ErrorAction SilentlyContinue).ProcessName
    Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
    Write-Host "Proceso detenido en 4200: $name (PID $procId)"
}
Start-Sleep -Seconds 1
Set-Location $PSScriptRoot
npm start
