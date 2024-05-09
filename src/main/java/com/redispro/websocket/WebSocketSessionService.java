package com.redispro.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class WebSocketSessionService {

    private static final String SESSION_KEY_PREFIX = "session:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeSession(String sessionId, String data) {
        redisTemplate.opsForValue().set(SESSION_KEY_PREFIX + sessionId, data, Duration.ofHours(1));
    }

    public String getSessionData(String sessionId) {
        return redisTemplate.opsForValue().get(SESSION_KEY_PREFIX + sessionId);
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete(SESSION_KEY_PREFIX + sessionId);
    }
}

