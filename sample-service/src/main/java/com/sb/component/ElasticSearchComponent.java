package com.sb.component;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.cluster.Health;
import io.searchbox.core.Bulk;
import io.searchbox.core.Index;
import io.searchbox.indices.ClearCache;
import io.searchbox.indices.CloseIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther: sammy
 * @Date: 2018/5/30 00:52
 * @Description:
 */
@Component
public class ElasticSearchComponent {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JestClient jestClient ;

   
    public Optional<JestResult> deleteIndex(String type) {
        DeleteIndex deleteIndex = new DeleteIndex.Builder(type).build();
        JestResult result = null ;
        try {
            result = jestClient.execute(deleteIndex);
        } catch (IOException e) {
            logger.error("jest delete error:",e);
        }
        return Optional.ofNullable(result);
    }

   
    public Optional<JestResult> clearCache() {
        ClearCache closeIndex = new ClearCache.Builder().build();
        JestResult result = null ;
        try {
            result = jestClient.execute(closeIndex);
        } catch (IOException e) {
            logger.error("jest clean error:",e);
        }
        return Optional.ofNullable(result);
    }

    public Optional<JestResult> closeIndex(String type) {
        CloseIndex closeIndex = new CloseIndex.Builder(type).build();
        JestResult result = null ;
        try {
            result = jestClient.execute(closeIndex);
        } catch (IOException e) {
            logger.error("jest close error:",e);
        }
        return Optional.ofNullable(result);
    }

    public Optional<JestResult> indicesExists(String index) {
        IndicesExists indicesExists = new IndicesExists.Builder(index).build();
        JestResult result = null ;
        try {
            result = jestClient.execute(indicesExists);
        } catch (IOException e) {
            logger.error("jest indicesExists error:",e);
        }
        return Optional.ofNullable(result);
    }

    public Optional<JestResult> health() {
        Health health = new Health.Builder().build();
        JestResult result = null ;
        try {
            result = jestClient.execute(health);
        } catch (IOException e) {
            logger.error("jest health error:",e);
        }
        return Optional.ofNullable(result);
    }

    public <T> void bulkIndex(String index, String type , T o) {

        try {

        Map<String,Object> map = new HashMap<>();
        InetAddress ip= InetAddress.getLocalHost();
        String hostname = ip.getHostName();

        map.put("ip",ip.getHostAddress());
        map.put("hostname",hostname);

        map.put("@timestamp",LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(type).addAction(Arrays.asList(
                    new Index.Builder(map).build()
                )).build();
            jestClient.execute(bulk);
        } catch (Exception e) {
            logger.error("jest insert error:",e);
        }
    }


    public <T> Optional<JestResult> createIndex(T o, String index, String type) {
        Index index1 = new Index.Builder(o).index(index).type(type).build();
        JestResult result = null ;
        try {
            result = jestClient.execute(index1);
        } catch (IOException e) {
            logger.error("jest create index error:",e);
        }
        return Optional.ofNullable(result);
    }
}
