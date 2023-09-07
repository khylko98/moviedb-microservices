package ua.khylko98.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername method of UserDetailsServiceImpl");
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            String.format("User by username=%s not found", username)
                    ));
        } catch (UsernameNotFoundException e) {
            log.warn(
                    "Exception occurred while finding user by username: {}",
                    e.getMessage()
            );
            throw e;
        }
    }

}
