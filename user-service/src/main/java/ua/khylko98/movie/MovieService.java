package ua.khylko98.movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllWatchedMovies(Long id);

    void addMovieToWatched(Long userId, Long movieId);
    void deleteMovieFromWatched(Long userId, Long movieId);
}
