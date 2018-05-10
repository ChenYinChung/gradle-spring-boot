##Purpose
 測試spring boot framework ,miro service 以RESTful為基礎, kafka傳送及接收
 
 * config - 環境先前設定，kafka producer/consumer
 * controller - micro service 
 * model - DB ORM
 * repository - DB ORM jpa
 * service - transaction usual use 
 

## Requirements
* jdk 10
* mysql 8
* kafka
* flyway
* hibernate

## Spring boot framework integrate 3rd tools

* flyway - ./gradlew -Penv=sit flywayInfo

* run WebApplication for startup application