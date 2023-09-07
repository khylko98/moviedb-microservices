package ua.khylko98.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.khylko98.movie.Movie;
import ua.khylko98.movie.MovieKafka;
import ua.khylko98.movie.MovieRepository;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaConsumerService {

    private static final String TOPIC_NAME = "movie-topic";

    private MovieRepository movieRepository;

    @KafkaListener(
            topics = TOPIC_NAME,
            groupId = "movie-consumer-group"
    )
    public void listen(String message) {
        MovieKafka movieKafka = deserializeMessage(message);

        Movie movie = new Movie();
        movie.setId(movieKafka.id());
        movie.setTitle(movieKafka.title());

        movieRepository.save(movie);
    }

    private MovieKafka deserializeMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(message, MovieKafka.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
