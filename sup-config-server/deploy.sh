echo 'stopping ...'
sudo fuser -n tcp -k 8888 || true
sleep 2
echo 'building ...'
./gradlew build -x test
echo 'deploying ...'
sudo nohup java -jar -Dspring.output.ansi.enabled=ALWAYS ./build/libs/sup-config-server-1.0.jar > ./sup-config-server.log 2>&1 &
tail -1000f ./sup-config-server.log 
