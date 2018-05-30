package com.sb.config;


import com.google.gson.GsonBuilder;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: sammy
 * @Date: 2018/5/18 21:37
 * @Description:
 */
@Configuration
public class ElasticSearchConfig {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Value("${elasticsearch.url}")
    String serverUrl;

    @Bean
    public JestClient jestClient() {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(serverUrl)
                .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create())
                .connTimeout(1500)
                .readTimeout(3000)
                .multiThreaded(true)
                .build());
        return factory.getObject();
    }
}
