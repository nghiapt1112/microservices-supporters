echo 'Stoping old agent...'
sudo fuser -n tcp -k 8761 || sleep 2
echo 'Deploying...'
sudo nohup java -jar ./sup-discovery-server-0.0.1.jar > ./sup-eureka.log 2>&1 &
