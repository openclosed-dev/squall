param(
  [string]$version
)

if (!$version) {
  $version = Get-Content -Path ./target/version.txt -ReadCount 1
}

Write-Host 'Deleting previous output...'
Remove-Item ./target/runtime -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/squall -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/temp -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/*.msi
Remove-Item ./target/*.zip

if ($null -ne $env:JAVA_17_HOME) {
  $toolDir = "$env:JAVA_17_HOME\bin"
} elseif ($null -ne $env:JAVA_HOME) {
  $toolDir = "$env:JAVA_HOME\bin"
} else {
  Write-Host 'Please define environment variable "JAVA_HOME"'
  exit 1
}

Write-Host 'Generating slim Java runtime...'
& "$toolDir\jlink" `
  --add-modules java.base,java.logging,java.management,jdk.unsupported `
  --strip-native-commands --strip-debug --no-man-pages --no-header-files `
  --output target/runtime `
  --verbose

Write-Host 'Generating application image...'
& "$toolDir\jpackage" `
  --type app-image `
  --dest target `
  --runtime-image target/runtime `
  --app-version $version `
  @src/main/jpackage/metadata.txt `
  --input target/input `
  --module-path target/mods `
  --module dev.openclosed.squall.cli `
  --java-options '--add-modules jdk.unsupported' `
  --java-options '--add-opens java.base/sun.nio.ch=ALL-UNNAMED' `
  --java-options '--add-opens java.base/java.io=ALL-UNNAMED' `
  --win-console `
  --verbose

Write-Host 'Generating MSI installer...'
& "$toolDir\jpackage" `
  --type msi `
  --dest target `
  --app-image target/squall `
  --resource-dir src/main/jpackage/config/windows `
  --app-version $version `
  @src/main/jpackage/metadata.txt `
  --win-per-user-install `
  --win-upgrade-uuid '421c99bd-78dc-425a-b471-f5d077bb15c1' `
  --temp target/temp `
  --verbose

Write-Host 'Generating zip archive...'
Compress-Archive -Path ./target/squall -DestinationPath "./target/squall-${version}-win-x64.zip"
