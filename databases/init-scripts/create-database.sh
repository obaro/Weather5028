#!/bin/bash

# Prompt for the PostgreSQL server details
read -p "Enter your PostgreSQL username: " username
read -sp "Enter your PostgreSQL password: " password
echo
read -p "Enter your PostgreSQL server URL: " url
read -p "Enter your PostgreSQL server PORT: " port

# Export the entered credentials to be used by psql
export PGPASSWORD=$password

# Connect to the PostgreSQL server and create the new database
psql -h $url -p $port -U $username -d "postgres" -c "CREATE DATABASE weather_data;"

# Unset the password variable for security
unset PGPASSWORD
