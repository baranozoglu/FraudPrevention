package com.demo.ing.result;

import com.demo.ing.exception.ResultNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class ResultRepository {
    private static final String RESULT_KEY_PREFIX = "RESULT_";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Result could not found!";
    private final ValueOperations<String, String> valueOperations;
    private RedisTemplate<String, String> redisTemplate;

    public ResultRepository(@Value("${redis.host}") String redisHost,
                            @Value("${redis.port}") int redisPort) {
        redisTemplate = redisTemplate(redisHost, redisPort);
        redisTemplate.afterPropertiesSet();
        this.valueOperations = redisTemplate.opsForValue();
    }


    public String getCachedResult(Long transactionId) {
        String result = valueOperations.get(determineKeyForResult(transactionId));
        if (result == null) {
            throw new ResultNotFoundException(NOT_FOUND_ERROR_MESSAGE);
        }
        return result;
    }

    public void insertCachedResult(Long transactionId, String result) {
        valueOperations.setIfAbsent(determineKeyForResult(transactionId), result);
    }

    private RedisTemplate<String, String> redisTemplate(String redisHost, int redisPort) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory(redisHost, redisPort));
        template.setValueSerializer(StringRedisSerializer.UTF_8);
        return template;
    }

    private JedisConnectionFactory jedisConnectionFactory(String redisHost, int redisPort) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    private String determineKeyForResult(Long transactionId) {
        return RESULT_KEY_PREFIX + transactionId.toString();
    }
}
