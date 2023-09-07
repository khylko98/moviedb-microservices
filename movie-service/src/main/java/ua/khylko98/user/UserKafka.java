package ua.khylko98.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserKafka(
        Long id,
        String username,
        String password
) {}
