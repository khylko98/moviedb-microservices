package ua.khylko98.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua.khylko98.jwt.JwtUtil;
import ua.khylko98.user.User;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public Map<String, String> login(AuthRequest authRequest) {
        log.info("Inside login method of AuthService");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()
                )
        );

        User principal = (User) authentication.getPrincipal();

        String token = jwtUtil.issueToken(
                principal.getUsername(),
                principal.getPassword()
        );

        return Map.of("token", token);
    }

}
