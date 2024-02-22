variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "source_ami" {
  type    = string
  default = "ami-058bd2d568351da34"
}

variable "ssh_username" {
  type    = string
  default = "admin"
}

variable "vpc_id" {
  type    = string
  default = "vpc-0eea03ee09c5b98c8"
}

source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  ami_name        = "unichef_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI Packer"
  ami_regions = [
    "us-east-1",
  ]


  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  vpc_id        = "${var.vpc_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp3"
  }

}

build {
  sources = ["source.amazon-ebs.my-ami"]

#  jar creation part
  provisioner "file" {
    source      = "./unichef.jar"
    destination = "/tmp/unichef.jar"
  }

#  provisioner "file" {
#    source      = "./application.properties"
#    destination = "/tmp/application.properties"
#  }

  provisioner "file" {
    source      = "./unichef.service"
    destination = "/tmp/unichef.service"
  }

  provisioner "shell" {
    script = "./app.sh"
  }

  post-processors {
    post-processor "manifest" {
      output     = "manifest.json"
      strip_path = true
    }
  }
}