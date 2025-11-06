@echo off
echo ============================================
echo Inserting Maintenance Plans Sample Data
echo ============================================
echo.

echo Server: localhost
echo Database: swp391_fall25
echo Username: sa
echo.

echo Executing SQL script...
echo.

sqlcmd -S localhost -d swp391_fall25 -U sa -P 12345 -i "D:\SWP\SWP391-FALL25\maintenance_plans_sample_data.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo SUCCESS! Maintenance plans inserted.
    echo ============================================
    echo.
    echo Now you can:
    echo 1. Restart your backend server
    echo 2. Test kilometer analysis on the maintenance report screen
    echo.
) else (
    echo.
    echo ============================================
    echo ERROR! Failed to execute SQL script.
    echo ============================================
    echo.
    echo Please check:
    echo 1. SQL Server is running
    echo 2. Database 'swp391_fall25' exists
    echo 3. Username 'sa' and password '12345' are correct
    echo.
)

pause
