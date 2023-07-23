$scriptPath = $MyInvocation.MyCommand.Path
$scriptDir = Split-Path $scriptPath -Parent
$homeDir = Split-Path $scriptDir -Parent

$javaCmd = 'java'
if ($null -ne $env:JAVA_HOME) {
    $javaCmd = "$env:JAVA_HOME\bin\java"
}

$javaArgs = @('--module-path', "$homeDir\lib", '--module', 'dev.openclosed.squall.cli') + $args

$encoding = [Console]::OutputEncoding
try {
    [Console]::OutputEncoding = [Text.Encoding]::UTF8
    & $javaCmd $javaArgs
} finally {
    [Console]::OutputEncoding = $encoding
}
