$ErrorActionPreference = "Stop"
mvn -B clean verify
if ($LASTEXITCODE -ne 0) { throw "Build/testes falharam." }
