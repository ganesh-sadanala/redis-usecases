package com.redispro.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class DataService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String getStringData(String key){
        String cachedData = (String) redisTemplate.opsForValue().get(key);
        if(cachedData != null){
            return cachedData;
        }else{
            String data=fetchDataFromDatabase(key);
            redisTemplate.opsForValue().set(key, data, Duration.ofHours(1));
            return data;
        }
    }

    public List<String> getListData(String key){
        List<String> cachedData = (List<String>) redisTemplate.opsForList().range(key, 0, -1);
        if (cachedData != null && !cachedData.isEmpty()) {
            return cachedData;
        } else {
            List<String> data = fetchDataListFromDatabase(key);
            redisTemplate.opsForList().leftPushAll(key, data);
            redisTemplate.expire(key, Duration.ofHours(1));
            return data;
        }
    }

    public Set<String> getSetData(String key) {
        Set<String> cachedData = (Set<String>) redisTemplate.opsForSet().members(key);
        if (cachedData != null && !cachedData.isEmpty()) {
            return cachedData;
        } else {
            Set<String> data = fetchDataSetFromDatabase(key);
            redisTemplate.opsForSet().add(key, data.toArray(new String[0]));
            redisTemplate.expire(key, Duration.ofHours(1));
            return data;
        }
    }

    public String getHashData(String key, String field) {
        return (String) redisTemplate.opsForHash().get(key, field);
    }

    public Set<String> getSortedSetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    public void cacheHashData(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, Duration.ofHours(1));
    }

    public void cacheSortedSetData(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
        redisTemplate.expire(key, Duration.ofHours(1));
    }
}
