package ua.khylko98.movie;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.khylko98.exception.IdNotFoundException;
import ua.khylko98.exception.TitleAlreadyExistsException;
import ua.khylko98.exception.TitleNotFoundException;
import ua.khylko98.kafka.KafkaProducerService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private MovieRepository movieRepository;
    private KafkaProducerService kafkaProducerService;

    @Override
    public List<Movie> getAll() {
        log.info("Inside getAll method of MovieServiceImpl");
        return movieRepository.findAll();
    }

    @Override
    public Movie findById(Long id) {
        log.info("Inside findById method of MovieServiceImpl");
        try {
            return movieRepository.findById(id)
                    .orElseThrow(() -> new IdNotFoundException(
                            String.format("Movie by id=%d not found", id)
                    ));
        } catch (IdNotFoundException e) {
            log.warn(
                    "Exception occurred while finding movie by id: {}",
                    e.getMessage()
            );
            throw e;
        }
    }

    @Transactional
    @Override
    public void save(MovieRequest movieRequest) {
        log.info("Inside save method of MovieServiceImpl");
        checkTitleAlreadyTaken(movieRequest.title());

        Movie movie = new Movie(
                movieRequest.title()
        );

        movieRepository.save(movie);

        Movie movieToKafkaTopic = movieRepository.findByTitle(movie.getTitle())
                .orElseThrow(() -> {
                    log.warn(
                            "Not find movie by title={} before send it to kafka",
                            movie.getTitle()
                    );
                    return new TitleNotFoundException(
                            String.format(
                                    "Movie by title=%s not found",
                                    movie.getTitle()
                            )
                    );
                });
        kafkaProducerService.sendMovieToKafkaTopic(movieToKafkaTopic);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Inside delete method of MovieServiceImpl");
        movieRepository.deleteById(id);
    }

    private void checkTitleAlreadyTaken(String title) {
        boolean isMovieWithTitleAlreadyExists =
                movieRepository.existsMovieByTitle(title);
        if (isMovieWithTitleAlreadyExists) {
            throw new TitleAlreadyExistsException(
                    String.format("Movie with title=%s already exists", title)
            );
        }
    }

}
