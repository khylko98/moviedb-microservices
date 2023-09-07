package ua.khylko98.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.khylko98.jwt.JwtUtil;
import ua.khylko98.movie.Movie;
import ua.khylko98.movie.MovieService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private JwtUtil jwtUtil;
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Inside getAllUsers method of UserController");
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Inside getUserById method of UserController");
        User founded = userService.findById(id);
        return ResponseEntity.ok(founded);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Map<String, String>> saveUser(
            @RequestBody UserRequest userRequest
    ) {
        log.info("Inside saveUser method of UserController");
        userService.save(userRequest);

        String token = jwtUtil.issueToken(
                userRequest.username(),
                "ROLE_USER"
        );

        return ResponseEntity.ok(
                Map.of("token", token)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUserById(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest
    ) {
        log.info("Inside updateUserById method of UserController");
        userService.update(id, userRequest);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Update succeed")
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable Long id) {
        log.info("Inside deleteUserById method of UserController");
        userService.delete(id);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Delete succeed")
        );
    }

    @GetMapping("/{id}/watched-movies")
    public ResponseEntity<List<Movie>> getUserWatchedMovies(@PathVariable Long id) {
        log.info("Inside getUserWatchedMovies method of UserController");
        List<Movie> watchedMovies = movieService.getAllWatchedMovies(id);
        return ResponseEntity.ok(watchedMovies);
    }

    @PostMapping("/{userId}/add-movie/{movieId}")
    public ResponseEntity<Map<String, String>> addMovie(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        log.info("Inside addMovie method of UserController");
        movieService.addMovieToWatched(userId, movieId);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Movie add successfully")
        );
    }

    @DeleteMapping("/{userId}/delete-movie/{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Map<String, String>> deleteMovie(
            @PathVariable Long userId,
            @PathVariable Long movieId
    ) {
        log.info("Inside deleteMovie method of UserController");
        movieService.deleteMovieFromWatched(userId, movieId);
        return ResponseEntity.ok(
                Map.of("resultMsg", "Movie delete successfully")
        );
    }

}
