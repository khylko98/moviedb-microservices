package ua.khylko98.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.khylko98.user.User;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC_NAME = "user-topic";
    private static final String TOPIC_NAME_FOR_DELETING = "user-delete-topic";

    private KafkaTemplate<String, User> kafkaTemplate;

    public void sendUserToKafkaTopic(User user) {
        log.info("Inside sendUserToKafkaTopic method of KafkaProducerService");
        kafkaTemplate.send(
                TOPIC_NAME,
                user.getId().toString(),
                user
        );
    }

    public void deleteUserToKafkaTopic(User user) {
        log.info("Inside deleteUserToKafkaTopic method of KafkaProducerService");
        kafkaTemplate.send(
                TOPIC_NAME_FOR_DELETING,
                user.getId().toString(),
                user
        );
    }

}
