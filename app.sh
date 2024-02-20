#!/bin/bash

sudo apt-get update -y
sudo apt-get upgrade -y

echo "Installing Open JDK"
sudo apt-get install openjdk-19-jdk -y
echo "Java installed successfully"
echo "$(java --version) is the version of java"

echo "Creating new group and users"
sudo groupadd unichef
sudo useradd -s /bin/false -g unichef -d /home/unichef -m unichef

sudo mv /tmp/unichef.jar /home/unichef
sudo mv /tmp/unichef.service /etc/systemd/system/unichef.service

cd /home/unichef
sudo chown unichef:unichef ./unichef.jar
sudo chmod +x ./unichef.jar

sudo systemctl daemon-reload
sudo systemctl enable unichef.service
sudo systemctl start unichef.service