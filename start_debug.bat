@echo off
cls
echo.
echo ╔═══════════════════════════════════════════════════════════════╗
echo ║                                                               ║
echo ║   STARTING BACKEND SERVER WITH DEBUG MODE                     ║
echo ║                                                               ║
echo ╚═══════════════════════════════════════════════════════════════╝
echo.
echo 📍 Working Directory: D:\SWP\SWP391-FALL25
echo 🔧 Port: 8081
echo 🐛 Debug Logging: ENABLED
echo 📊 SQL Logging: ENABLED
echo.
echo ⏳ Starting server... (This may take 30-60 seconds)
echo.

cd /d "D:\SWP\SWP391-FALL25"
mvn spring-boot:run

pause
