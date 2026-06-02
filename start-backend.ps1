# Libera el puerto 8080 y arranca el backend
$connections = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
foreach ($conn in $connections) {
    $procId = $conn.OwningProcess
    $name = (Get-Process -Id $procId -ErrorAction SilentlyContinue).ProcessName
    Stop-Process -Id $procId -Force -ErrorAction SilentlyContinue
    Write-Host "Proceso detenido en 8080: $name (PID $procId)"
}
Start-Sleep -Seconds 1
Set-Location $PSScriptRoot
.\mvnw.cmd spring-boot:run
