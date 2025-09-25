@echo off
echo Starting Student Management API with Docker...

REM Check if Docker is running
docker version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Docker is not running. Please start Docker Desktop and try again.
    pause
    exit /b 1
)

REM Check if firebase-service-account.json exists
if not exist "src\main\resources\firebase-service-account.json" (
    echo Error: Firebase service account file not found!
    echo Please place your firebase-service-account.json file in src\main\resources\
    pause
    exit /b 1
)

REM Create logs directory if it doesn't exist
if not exist "logs" mkdir logs

echo Building and starting the application...
docker-compose up --build

pause