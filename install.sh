#!/usr/bin/env bash


set -e

./gradlew build -x test

cd sup-auth/                 note "Building auth...";          ./gradlew build -x test; cd -

docker-compose build