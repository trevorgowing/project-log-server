#!/usr/bin/env bash

set -e

sudo docker-compose up -d mysql
sudo docker-compose up --build project-log-server