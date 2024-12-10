package project.coca.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtRedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

    public void setRedisTemplate(String key, String value, long time) {
        if (getToken(key) != null) deleteToken(key);
        Duration expiredDuration = Duration.ofMillis(time);
        redisTemplate.opsForValue().set(key, value, expiredDuration);
    }
}
