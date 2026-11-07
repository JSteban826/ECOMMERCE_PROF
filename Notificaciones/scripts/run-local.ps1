$ErrorActionPreference = 'Stop'
Set-Location (Join-Path $PSScriptRoot '..')
if (-not (Test-Path '.env')) { throw 'Copia .env.example a .env y completa los valores' }
Get-Content '.env' | ForEach-Object {
    $line = $_
    if ($line -and -not $line.StartsWith('#')) {
        $parts = $line.Split('=', 2)
        if ($parts.Count -ne 2 -or $parts[0] -notmatch '^[A-Z_][A-Z0-9_]*$') { throw 'Variable .env inválida' }
        [Environment]::SetEnvironmentVariable($parts[0], $parts[1], 'Process')
    }
}
& java -jar boot/target/notificaciones-boot-1.0.0-SNAPSHOT.jar
exit $LASTEXITCODE
