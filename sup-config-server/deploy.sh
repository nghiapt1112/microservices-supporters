echo 'stopping ...'
sudo fuser -n tcp -k 8888 || true
sleep 2
echo 'building ...'
./gradlew build -x test
echo 'deploying ...'
sudo nohup java -jar ./build/libs/sup-config-server.jar > ./sup-config-server.log 2>&1 &
