$ErrorActionPreference = "Stop"
$urls = @("http://localhost:8081/api/health", "http://localhost:8082/api/health")
foreach ($url in $urls) {
    try {
        $response = Invoke-RestMethod -Uri $url -TimeoutSec 5
        Write-Host "$url -> $($response.status)"
    } catch {
        Write-Error "Falha ao verificar $url : $($_.Exception.Message)"
        exit 1
    }
}
