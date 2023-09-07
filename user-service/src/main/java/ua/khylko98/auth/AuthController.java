package ua.khylko98.auth;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody AuthRequest authRequest
    ) {
        log.info("Inside login method of AuthController");
        Map<String, String> token = authService.login(authRequest);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        token.get("token")
                ).body(token);
    }

}
