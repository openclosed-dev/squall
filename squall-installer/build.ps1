Remove-Item ./target/runtime -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/squall -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/temp -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/*.msi

if ($null -ne $env:JAVA_17_HOME) {
  $toolDir = "$env:JAVA_17_HOME\bin"
} elseif ($null -ne $env:JAVA_HOME) {
  $toolDir = "$env:JAVA_HOME\bin"
} else {
  Write-Host 'Please define enviroment variable "JAVA_HOME"'
  exit 1
}

# Generates slim Java runtime
& "$toolDir\jlink" `
  --add-modules java.base,java.logging,java.management,jdk.unsupported `
  --strip-native-commands --strip-debug --no-man-pages --no-header-files `
  --output target/runtime `
  --verbose

& "$toolDir\jpackage" `
  --type app-image `
  --dest target `
  --runtime-image target/runtime `
  @target/metadata.txt `
  --input target/input `
  --module-path target/mods `
  --module dev.openclosed.squall.cli `
  --java-options '--add-modules jdk.unsupported' `
  --java-options '--add-opens java.base/sun.nio.ch=ALL-UNNAMED' `
  --java-options '--add-opens java.base/java.io=ALL-UNNAMED' `
  --win-console `
  --verbose

& "$toolDir\jpackage" `
  --type msi `
  --dest target `
  --app-image target/squall `
  --resource-dir src/main/jpackage/config/windows `
  @target/metadata.txt `
  --win-per-user-install `
  --win-upgrade-uuid '421c99bd-78dc-425a-b471-f5d077bb15c1' `
  --temp target/temp `
  --verbose
