##Purpose
 測試spring boot framework ,miro service 以RESTful為基礎, kafka傳送及接收,elasticsearch & kibana
 
 * config - 環境先前設定，kafka producer/consumer
 * controller - micro service 
 * model - DB ORM
 * repository - DB ORM jpa
 * service - transaction usual use 
 

## Requirements
* docker
* docker-compose
* jdk 10
* 在docker-compose目錄,elasticsearch.yml 
     
     修改 network.publish_host: 192.168.1.104 改為電腦對應ip
     
     discovery.zen.ping.unicast.hosts: ["192.168.1.104"]

* create mysql schema test with utf8mb4 and collate utf8mb4_unicode_ci

以下是所需啟動的Server
* mysql 8
* zookeeper inside <-docker
* kafka 2.12 本地安裝
* elasticsearch 6 <-docker latest
* kibana  <- docker latest
* redis <- docker latest

## Spring boot framework integrate 3rd tools

run in command mode , needs install gradle 4.7 version above 

* flyway 
  
    -> ./gradlew -Penv=sit flywayInfo

* run WebApplication for startup application
  * start docker-compose up -d
    -> startup elstaicsearch , kibana , zookeeper , redis 
  
  *  start up kafka with cli
  
     <KAFKA_HOME>/bin> ./kafka-server-startup.sh ../config/server.properties
     
  * java -jar test-service-1.0.jar -Xmx3550m -Xms3550m -Xmn2g -Xss128k -XX:+UseParallelGC  -XX:MaxGCPauseMillis=100 -XX:+UseAdaptiveSizePolicy