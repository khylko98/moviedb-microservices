package ua.khylko98.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.khylko98.exception.IdNotFoundException;
import ua.khylko98.exception.UsernameAlreadyExistsException;
import ua.khylko98.kafka.KafkaProducerService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private KafkaProducerService kafkaProducerService;

    @Override
    public List<User> getAll() {
        log.info("Inside getAll method of UserServiceImpl");
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        log.info("Inside findById method of UserServiceImpl");
        try {
            return userRepository.findById(id)
                    .orElseThrow(() -> new IdNotFoundException(
                            String.format("User by id=%d not found", id)
                    ));
        } catch (IdNotFoundException e) {
            log.warn(
                    "Exception occurred while finding user by id: {}",
                    e.getMessage()
            );
            throw e;
        }
    }

    @Transactional
    @Override
    public void save(UserRequest userRequest) {
        log.info("Inside save method of UserServiceImpl");
        checkUsernameAlreadyTaken(userRequest.username());

        User user = new User(
                userRequest.username(),
                userRequest.password()
        );

        // encode password and set it to user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // find saved user by username
        User userToKafkaTopic = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.warn(
                            "Not find user by username={} before send it to kafka",
                            user.getUsername()
                    );
                    return new UsernameNotFoundException(
                            String.format(
                                    "User by username=%s not found",
                                    user.getUsername()
                            )
                    );
                });
        // send it to kafka topic
        kafkaProducerService.sendUserToKafkaTopic(userToKafkaTopic);
    }

    @Transactional
    @Override
    public void update(Long id, UserRequest update) {
        log.info("Inside update method of UserServiceImpl");
        User user = findById(id);

        // param to control if any changes exist
        boolean changes = false;

        // check username
        if (update.username() != null &&
                !update.username().equals(user.getUsername())) {
            user.setUsername(update.username());
            changes = true;
        }

        // check password (with encoded stuff)
        if (update.password() != null &&
                !passwordEncoder.encode(update.password()).equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(update.password()));
            changes = true;
        }

        if (!changes) {
            return;
        }

        userRepository.save(user);

        // find saved user by username
        User userToKafkaTopic = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    log.warn(
                            "Not find user by username={} before send it to kafka",
                            user.getUsername()
                    );
                    return new UsernameNotFoundException(
                            String.format(
                                    "User by username=%s not found",
                                    user.getUsername()
                            )
                    );
                });
        // send it to kafka topic
        kafkaProducerService.sendUserToKafkaTopic(userToKafkaTopic);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.info("Inside delete method of UserServiceImpl");

        // find saved user by username
        User userToKafkaTopic = userRepository.findById(id).orElseThrow(() -> {
            log.warn(
                    "Not find user by id={} before send it to kafka",
                    id
            );
            return new IdNotFoundException(
                    String.format(
                            "User by id=%d not found",
                            id
                    )
            );
        });
        // send it to kafka topic
        kafkaProducerService.deleteUserToKafkaTopic(userToKafkaTopic);

        // delete from repository
        userRepository.deleteById(id);
    }

    private void checkUsernameAlreadyTaken(String username) {
        boolean isUserWithUsernameAlreadyExists =
                userRepository.existsUserByUsername(username);
        if (isUserWithUsernameAlreadyExists) {
            throw new UsernameAlreadyExistsException(
                    String.format("User with username=%s already exists", username)
            );
        }
    }

}
