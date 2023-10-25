package com.demo.ing.transaction.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionCacheRepository {
    private final ValueOperations<Long, TransactionCache> valueOperations;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private RedisTemplate<Long, TransactionCache> redisTemplate;

    public TransactionCacheRepository(@Value("${redis.host}") String redisHost,
                                      @Value("${redis.port}") int redisPort) {
        redisTemplate = redisTemplate(redisHost, redisPort);
        redisTemplate.afterPropertiesSet();
        this.valueOperations = redisTemplate.opsForValue();
    }

    public TransactionCache getCachedTransaction(Long transactionId) {
        return valueOperations.get(transactionId);
    }

    public void insertCachedTransaction(Long transactionId, TransactionCache transactionCache) {
        Boolean inserted = valueOperations.setIfAbsent(transactionId, transactionCache);
        if (inserted != null && inserted) {
            LOGGER.debug("Inserted: {}", transactionId);
        }
    }

    public void deleteCachedTransaction(Long transactionId) {
        Boolean deleted = valueOperations.getOperations().delete(transactionId);
        if (deleted != null && deleted) {
            LOGGER.debug("Removed: {}", transactionId);
        }
    }

    private RedisTemplate<Long, TransactionCache> redisTemplate(String redisHost, int redisPort) {
        RedisTemplate<Long, TransactionCache> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory(redisHost, redisPort));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TransactionCache.class));
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

}
