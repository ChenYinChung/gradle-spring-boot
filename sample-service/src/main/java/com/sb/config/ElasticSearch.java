package com.sb.config;


import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @Auther: sammy
 * @Date: 2018/5/18 21:37
 * @Description:
 */
@Component
public class ElasticSearch {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.url}")
    private String url;

    public void send(String _index , String _type , Object object){
        try {
            Gson gson = new Gson();

            Map<String, Object> map = new HashMap<>();

            map.put("timestamp", new Date());
            map.put("source", object);

            String json = gson.toJson(map);

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            String result = restTemplate.postForObject(url(_index, _type), entity, String.class);

            logger.info(result);
        }catch (Exception e){
            logger.error("error occurs",e);
        }

    }

    private String url(String index , String type){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url).append("/").append(index).append("/").append(type).append("/").append(System.currentTimeMillis());

        return stringBuilder.toString();
    }

}
