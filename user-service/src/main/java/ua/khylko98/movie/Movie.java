package ua.khylko98.movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.khylko98.user.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie_topic")
public class Movie {

    @Id
    private Long id;
    private String title;

    @ManyToMany(mappedBy = "watchedMovies")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

}
