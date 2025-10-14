# Cleanup unwanted files from the project

Write-Host "🧹 Cleaning up unwanted files..." -ForegroundColor Yellow

# Remove duplicate/unused directories
$unwantedPaths = @(
    "api",
    "public", 
    "logs",
    "requirements.txt",
    "render.yml",
    "Dockerfile.local",
    ".dockerignore",
    "src\main\resources\static\templates",
    "src\main\resources\static\css\js"
)

foreach ($path in $unwantedPaths) {
    $fullPath = Join-Path $PWD $path
    if (Test-Path $fullPath) {
        Remove-Item $fullPath -Recurse -Force
        Write-Host "✅ Removed: $path" -ForegroundColor Green
    }
}

# Remove test files (keep only essential ones)
$testFiles = @(
    "test-db.sql",
    "test-webhook.json", 
    "check-trojan.ps1",
    "test-sms.ps1"
)

foreach ($file in $testFiles) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "✅ Removed: $file" -ForegroundColor Green
    }
}

# Remove empty EmailNotificationService.java
$emptyFile = "src\main\java\com\team\incidentresponse\service\EmailNotificationService.java"
if (Test-Path $emptyFile) {
    Remove-Item $emptyFile -Force
    Write-Host "✅ Removed: EmailNotificationService.java" -ForegroundColor Green
}

Write-Host "🎉 Cleanup complete!" -ForegroundColor Cyan