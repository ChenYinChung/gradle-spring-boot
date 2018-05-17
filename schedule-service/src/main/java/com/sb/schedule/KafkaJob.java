package com.sb.schedule;

import com.sb.config.QuartzJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.util.Date;


@QuartzJob(name = "KafkaJob", cronExp = "0/5 * * * * ?")
public class KafkaJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String URL_KAFKA_SEND = "http://localhost:8080/kafka/send?message=";

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        RestTemplate restTemplate = new RestTemplate();
        // Send request with GET method and default Headers.
        String result = restTemplate.getForObject(URL_KAFKA_SEND+new Date(), String.class);

        logger.info("kafka send get result[{}]",result);
    }
}
