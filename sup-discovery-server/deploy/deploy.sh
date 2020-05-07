echo 'Stoping old agent...'
sudo fuser -n tcp -k 8761
echo 'Deploying...'
sudo nohup java -jar ./build/libs/sup-discovery-server-0.0.1.jar > ./sup-eureka.log 2>&1 &
