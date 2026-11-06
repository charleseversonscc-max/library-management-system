$ErrorActionPreference = "Stop"
Set-Location $PSScriptRoot

$classes = "webapp\WEB-INF\classes"
New-Item -ItemType Directory -Force -Path $classes, "webapp\WEB-INF\lib" | Out-Null

$files = Get-ChildItem src -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$cp = "C:\tomcat\lib\servlet-api.jar;webapp\WEB-INF\lib\*"

javac -encoding UTF-8 -cp $cp -d $classes $files
if ($LASTEXITCODE -ne 0) { Write-Host "Compile failed"; exit 1 }
Write-Host "Build finished"
