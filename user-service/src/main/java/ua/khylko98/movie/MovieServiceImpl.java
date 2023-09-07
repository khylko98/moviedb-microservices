package ua.khylko98.movie;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.khylko98.exception.IdNotFoundException;
import ua.khylko98.user.User;
import ua.khylko98.user.UserRepository;
import ua.khylko98.user.UserService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private UserRepository userRepository;
    private MovieRepository movieRepository;
    private UserService userService;

    @Override
    public List<Movie> getAllWatchedMovies(Long id) {
        log.info("Inside getAllWatchedMovies method of MovieServiceImpl");
        User user = userService.findById(id);
        return user.getWatchedMovies();
    }

    @Transactional
    @Override
    public void addMovieToWatched(Long userId, Long movieId) {
        log.info("Inside addMovieToWatched method of MovieServiceImpl");
        User user = userService.findById(userId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IdNotFoundException(
                        String.format("Movie by id=%d not found", movieId)
                ));
        // add movie to list watched movies in pojo
        user.addMovie(movie);
        // take changes in db
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteMovieFromWatched(Long userId, Long movieId) {
        log.info("Inside deleteMovieFromWatched method of MovieServiceImpl");
        User user = userService.findById(userId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IdNotFoundException(
                        String.format("Movie by id=%d not found", movieId)
                ));
        // delete movie from list watched movies in pojo
        user.deleteMovie(movie);
        // take changes in db
        userRepository.save(user);
    }

}
