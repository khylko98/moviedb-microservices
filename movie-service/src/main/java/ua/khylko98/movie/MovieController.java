package ua.khylko98.movie;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/movies")
@Slf4j
@AllArgsConstructor
public class MovieController {

    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        log.info("Inside getAllMovies method of MovieController");
        List<Movie> movies = movieService.getAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        log.info("Inside getMovieById method of MovieController");
        Movie founded = movieService.findById(id);
        return ResponseEntity.ok(founded);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> saveMovie(
            @RequestBody MovieRequest movieRequest
    ) {
        log.info("Inside saveMovie method of MovieController");
        movieService.save(movieRequest);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Movie saved successfully")
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, String>> deleteMovie(@PathVariable Long id) {
        log.info("Inside deleteMovie method of MovieController");
        movieService.delete(id);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Movie deleted successfully")
        );
    }

}
