./gradlew clean build -x test
 docker build . -t nghia_ms-monitoring
 docker rm -f n_monitor
 sleep 2
 docker run -d --name=n_monitor --add-host="nghia.ms:10.0.0.86" --add-host="nghia.ser:172.26.0.100" --add-host="nghia.tool:10.0.0.25" -p 7979:7979 nghia_ms-monitoring
