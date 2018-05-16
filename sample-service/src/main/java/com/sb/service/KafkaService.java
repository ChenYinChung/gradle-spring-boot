package com.sb.service;


import com.sb.annotation.ExecutionInterval;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExecutionInterval
    public void listenTopicTest(ConsumerRecord<?, ?> record){
        logger.info("kafka的key: " + record.key());
        logger.info("kafka的value: " + record.value().toString());

    }
}
