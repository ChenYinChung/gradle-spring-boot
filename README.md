##Purpose
 測試spring boot framework ,miro service 以RESTful為基礎, kafka傳送及接收
 
 * config - 環境先前設定，kafka producer/consumer
 * controller - micro service 
 * model - DB ORM
 * repository - DB ORM jpa
 * service - transaction usual use 
 

## Requirements
* jdk 10

以下是所需啟動的Server
* mysql 8
* kafka 2.12(include zookeeper inside) <- docker
* elasticsearch 6 <-docker latest
* kibana  <- docker latest
* redis <- docker latest

## Spring boot framework integrate 3rd tools

run in command mode , needs install gradle 4.7 version above 

* flyway 
  
    -> ./gradlew -Penv=sit flywayInfo

* run WebApplication for startup application

  java -jar test-service-1.0.jar -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:+UseParallelGC  -XX:MaxGCPauseMillis=100 -XX:+UseAdaptiveSizePolicy