package ua.khylko98.auth;

public record AuthRequest(
        String username,
        String password
) {}
