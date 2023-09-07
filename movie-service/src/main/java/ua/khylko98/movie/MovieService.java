package ua.khylko98.movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();
    Movie findById(Long id);

    void save(MovieRequest movieRequest);
    void delete(Long id);
}
