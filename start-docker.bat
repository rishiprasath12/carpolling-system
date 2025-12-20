@echo off
REM Carpooling Application - Docker Startup Script (Windows)

echo ======================================
echo   Carpooling Application - Docker
echo ======================================
echo.

REM Check if Docker is installed
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not installed!
    echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop
    exit /b 1
)

REM Check if Docker is running
docker info >nul 2>&1
if errorlevel 1 (
    echo Error: Docker is not running!
    echo Please start Docker Desktop and try again.
    exit /b 1
)

echo Docker is installed and running
echo.

echo Building and starting containers...
echo.

REM Build and start containers
docker-compose up --build -d

REM Wait for services to be healthy
echo.
echo Waiting for services to be ready...
timeout /t 10 /nobreak >nul

echo.
echo Application started successfully!
echo.
echo ======================================
echo   Access Points
echo ======================================
echo Application API:    http://localhost:8010
echo Swagger UI:         http://localhost:8010/swagger-ui.html
echo API Docs:           http://localhost:8010/api-docs
echo PostgreSQL:         localhost:5432
echo.
echo ======================================
echo   Database Credentials
echo ======================================
echo Database: carpolling_db
echo Username: admin
echo Password: admin
echo.
echo ======================================
echo   Useful Commands
echo ======================================
echo View logs:        docker-compose logs -f
echo Stop services:    docker-compose stop
echo Restart:          docker-compose restart
echo Shut down:        docker-compose down
echo.

pause


