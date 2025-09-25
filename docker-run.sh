#!/bin/bash

echo "Starting Student Management API with Docker..."

# Check if Docker is running
if ! docker version >/dev/null 2>&1; then
    echo "Error: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if firebase-service-account.json exists
if [ ! -f "src/main/resources/firebase-service-account.json" ]; then
    echo "Error: Firebase service account file not found!"
    echo "Please place your firebase-service-account.json file in src/main/resources/"
    exit 1
fi

# Create logs directory if it doesn't exist
mkdir -p logs

echo "Building and starting the application..."
docker-compose up --build

echo "Application stopped."