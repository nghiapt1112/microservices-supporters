echo 'Granting permission to gradlew ...'
sudo chmod +x ./gradlew
echo 'Building  ...'
sudo ./gradlew build -x test
sudo fuser -n tcp -k 8761
sudo nohup java -jar ./build/libs/sup-discovery-server-0.0.1.jar > ./sup-eureka.log 2>&1 &
