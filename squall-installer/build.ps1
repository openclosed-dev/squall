Remove-Item ./target/squall -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/temp -Force -Recurse -ErrorAction SilentlyContinue
Remove-Item ./target/*.msi

jpackage --type app-image --dest target `
  @target/metadata.txt @src/main/jpackage/modules.txt --win-console --verbose

Copy-Item -Path 'src/main/legal' -Destination 'target/squall' -Recurse

jpackage --type msi --dest target `
  --app-image target/squall --resource-dir src/main/jpackage/config/windows @target/metadata.txt `
  --win-per-user-install --win-upgrade-uuid '421c99bd-78dc-425a-b471-f5d077bb15c1' `
  --temp target/temp --verbose
