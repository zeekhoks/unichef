#!/bin/bash

sudo apt update -y
sudo apt upgrade -y

echo "Installing Open JDK"
sudo apt-get install openjdk-19-jdk -y
echo "Java installed successfully"
echo "$(java --version) is the version of java"

sudo mkdir /home/ubuntu/webapp
sudo mv /tmp/unichef.jar /home/ubuntu/webapp
sudo mv /tmp/unichef.service /etc/systemd/system/unichef.service
sudo systemctl enable unichef.service
sudo systemctl start unichef.service