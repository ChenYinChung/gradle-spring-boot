package com.sb.schedule;

import com.sb.config.QuartzJob;
import com.sb.util.PropertiesUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @Auther: sammy
 * @Date: 2018/5/17 23:54
 * @Description:
 */

@QuartzJob(name = "UserJob", cronExp = "0/10 * * * * ?")
public class UserJob extends QuartzJobBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    static final String URL_USER_LOGIN = "/user/login";
    static final String URL_USER_FIND = "/user/find/sammy";

    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

        RestTemplate restTemplate = new RestTemplate();

        login(restTemplate);

        find(restTemplate);
    }

    private void login(RestTemplate restTemplate) {
        try {
            String host = PropertiesUtils.applicationHost();

            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            String requestJson = "{\"name\":\"sammy\",\"age\":80}";
            HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
            String result = restTemplate.postForObject(host+URL_USER_LOGIN, entity, String.class);


            logger.info("User login post result[{}]", result);

        } catch (IOException e) {
            logger.error("error occurs", e);
        }
    }

    private void find(RestTemplate restTemplate) {
        try {
            String host = PropertiesUtils.applicationHost();

            String result = restTemplate.getForObject(host + URL_USER_FIND, String.class);

            logger.info("User find get result[{}]", result);

        } catch (IOException e) {
            logger.error("error occurs", e);
        }
    }

}
