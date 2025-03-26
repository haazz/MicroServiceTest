package com.example.ms1.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${kafka.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;
    private static final String REPLY_TOPIC = "reply-topic";
    private static final String GROUP = "request-group";

    // ProducerFactory 설정
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // ConsumerFactory 설정
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 30000); // 30초
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 45000); // 45초
        configProps.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 3000); // 3초
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    // Listener Container Factory 설정
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setReplyTemplate(replyingKafkaTemplate());
        return factory;
    }

    // ReplyingKafkaTemplate 설정
    @Bean
    public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate() {
        ContainerProperties containerProperties = new ContainerProperties(REPLY_TOPIC);
        containerProperties.setGroupId(GROUP);
        ConcurrentMessageListenerContainer<String, String> replyContainer =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProperties);

        ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate =
                new ReplyingKafkaTemplate<>(producerFactory(), replyContainer);

        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(30)); // 30초 타임아웃
        return replyingKafkaTemplate;
    }
}
