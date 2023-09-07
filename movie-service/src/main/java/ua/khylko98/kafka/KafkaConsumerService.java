package ua.khylko98.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.khylko98.user.User;
import ua.khylko98.user.UserKafka;
import ua.khylko98.user.UserRepository;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {

    private static final String TOPIC_NAME = "user-topic";

    private UserRepository userRepository;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "user-consumer-group"
    )
    public void listen(String message) {
        UserKafka userKafka = deserializeMessage(message);

        User user = new User();
        user.setId(userKafka.id());
        user.setUsername(userKafka.username());
        user.setPassword(userKafka.password());

        userRepository.save(user);
    }

    private UserKafka deserializeMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, UserKafka.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
