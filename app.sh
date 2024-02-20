#!/bin/bash

USER_HOME_DIR=/home/unichef

sudo apt-get update -y
sudo apt-get upgrade -y

echo "Installing Open JDK"
sudo apt-get install openjdk-19-jdk -y
echo "Java installed successfully"
echo "$(java --version) is the version of java"

echo "Creating new group and users"
sudo groupadd unichef
sudo useradd -s /bin/false -g unichef -d $USER_HOME_DIR -m unichef

sudo mv /tmp/unichef.jar $USER_HOME_DIR
sudo mv /tmp/unichef.service /etc/systemd/system/unichef.service

#cd /home/unichef
sudo chown unichef:unichef $USER_HOME_DIR/unichef.jar
sudo chmod +x $USER_HOME_DIR/unichef.jar

sudo systemctl daemon-reload
sudo systemctl enable unichef.service
#sudo systemctl start unichef.service
#sudo systemctl status unichef