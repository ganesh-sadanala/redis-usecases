package com.redispro.leaderboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class LeaderboardService {

    private static final String LEADERBOARD_KEY = "leaderboard";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void incrementUserScore(String userId, int score) {
        redisTemplate.opsForZSet().incrementScore(LEADERBOARD_KEY, userId, score);
    }

    public Set<String> getTopUsers(int count) {
        return redisTemplate.opsForZSet().reverseRange(LEADERBOARD_KEY, 0, count - 1);
    }
}

