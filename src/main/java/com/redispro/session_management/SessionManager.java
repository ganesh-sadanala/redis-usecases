package com.redispro.session_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setSessionAttribute(String sessionId, String attributeName, Object attributeValue) {
        redisTemplate.opsForHash().put(sessionId, attributeName, attributeValue);
    }

    public Object getSessionAttribute(String sessionId, String attributeName) {
        return redisTemplate.opsForHash().get(sessionId, attributeName);
    }
}

