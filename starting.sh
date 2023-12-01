#!/bin/sh
sudo apt-get update

# Python
sudo apt install python3

# NodeJS
sudo apt-get install -y ca-certificates curl gnupg
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | sudo gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg
NODE_MAJOR=21
echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | sudo tee /etc/apt/sources.list.d/nodesource.list
sudo apt-get install nodejs -y
sudo apt-get install npm

# Bluetooth
sudo apt-get install -y libbluetooth-dev
sudo apt-get install bluetooth bluez libbluetooth-dev libudev-dev

# Build
npm install
sudo node src/index.js
