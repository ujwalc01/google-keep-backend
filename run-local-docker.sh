#!/usr/bin/env bash
set -e

# 1. Build multi-module project
mvn clean package -DskipTests

# 2. Bring up full stack (mysql + both services)
docker compose up --build
