./gradlew clean build -x test
docker build . -t nghia_ms-config
docker rm -f n_config 
sleep 2
docker run -d --name=n_config --add-host="nghia.ms:10.0.0.86" --add-host="nghia.ser:172.26.0.100" --add-host="nghia.tool:10.0.0.25" nghia_ms-config