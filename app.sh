#!/bin/bash

USER_HOME_DIR=/home/unichef

sudo apt update -y

sudo apt upgrade -y

#sudo apt --fix-missing install

echo "Installing Open JDK"
#sudo apt-get install openjdk-17-jre -y
#sudo apt install openjdk-17-jdk -y

wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb

sudo apt install ./jdk-21_linux-x64_bin.deb -y

echo "Java installed successfully"
echo "$(java --version) is the version of java"

#sudo update-alternatives --config java

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