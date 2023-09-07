package ua.khylko98.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // get Authorization from request headers
            final String authHeader = request.getHeader("Authorization");
            log.info("authHeader: {}", authHeader);

            // check if authHeader is empty or not starts with "Bearer ..."
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // skip "Bearer " thing
            final String token = authHeader.substring(7);
            log.info("token: {}",token);
            // get subject (in our case: username)
            final String subject = jwtUtil.getSubject(token);
            log.info("subject: {}", subject);

            // check if username is empty or incorrect context authentication
            if (subject == null ||
                    SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            // load user by username
            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);

            // check if username by loaded user corresponds to token
            boolean isTokenValid = jwtUtil.isTokenValid(
                    token,
                    userDetails.getUsername()
            );
            log.info("isTokenValid: {}", isTokenValid);
            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        } catch (RuntimeException e) {
            log.error(
                    "Exception occurred inside JwtFilter in movie-service: {}",
                    e.getMessage()
            );
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"msg\": \"Invalid JWT signature\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
