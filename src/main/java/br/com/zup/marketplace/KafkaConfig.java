package br.com.zup.marketplace;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaConfig {

    @Bean
    public NewTopic topic_teste() {
        return TopicBuilder.name("teste")
                .partitions(1)
                .replicas(1)
                .build();
    }

}
