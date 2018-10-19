#!/usr/bin/env bash


set -e

cd sup-auth/ 			note "Building auth...";  	./gradlew clean build -x test; cd -
cd sup-config-server/ 		note "Building config...";  	./gradlew clean build -x test; cd -
cd sup-discovery-server/ 	note "Building discovery...";  	./gradlew clean build -x test; cd -
cd sup-edge-server/ 		note "Building edge...";  	./gradlew clean build -x test; cd -
cd sup-monitor-dashboard/ 	note "Building monitor...";  	./gradlew clean build -x test; cd -
cd sup-turbine/ 		note "Building turbine..."; 	./gradlew clean build -x test; cd -
#cd sup-zipkin-server/ 		note "Building zipkin...";  	./gradlew clean build -x test; cd -

docker compose up
