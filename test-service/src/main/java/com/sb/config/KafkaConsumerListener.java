package com.sb.config;

import com.sb.service.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

public class KafkaConsumerListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    KafkaService kafkaService;

    @KafkaListener(topics = {"test"})
    public void listen(ConsumerRecord<?, ?> record) {
        kafkaService.listenTopicTest(record);
    }
}
