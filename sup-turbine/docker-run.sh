./gradlew clean build -x test
docker build . -t nghia_ms-turbine
docker rm -f n_turbine 
sleep 2
docker run -d --name=n_turbine --add-host="nghia.ms:10.0.0.86" --add-host="nghia.ser:172.26.0.100" --add-host="nghia.tool:10.0.0.25" -p 8989:8989 nghia_ms-turbine