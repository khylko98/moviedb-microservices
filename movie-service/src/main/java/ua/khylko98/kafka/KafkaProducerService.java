package ua.khylko98.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ua.khylko98.movie.Movie;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC_NAME = "movie-topic";

    private KafkaTemplate<String, Movie> kafkaTemplate;

    public void sendMovieToKafkaTopic(Movie movie) {
        log.info("Inside sendMovieToKafkaTopic method of KafkaProducerService");
        kafkaTemplate.send(
                TOPIC_NAME,
                movie.getId().toString(),
                movie
        );
    }

}
