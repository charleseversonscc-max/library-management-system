Set-Location $PSScriptRoot

$app = "C:\tomcat\webapps\library"
if (Test-Path $app) { Remove-Item $app -Recurse -Force }
Copy-Item webapp $app -Recurse

$env:CATALINA_HOME = "C:\tomcat"
Start-Process "C:\tomcat\bin\startup.bat"
Start-Sleep 15
Start-Process "http://localhost:8080/library/pages/login.html"