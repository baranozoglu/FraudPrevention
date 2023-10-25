package com.demo.ing.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.demo.ing.common.KafkaProperties.RESULT_TOPIC;
import static com.demo.ing.common.KafkaProperties.TRANSACTION_TOPIC;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name(TRANSACTION_TOPIC)
                .build();
    }

    @Bean
    public NewTopic resultTopic() {
        return TopicBuilder.name(RESULT_TOPIC)
                .build();
    }
}
