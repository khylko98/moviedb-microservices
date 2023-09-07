package ua.khylko98.user;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User findById(Long id);

    void save(UserRequest userRequest);
    void update(Long id, UserRequest update);
    void delete(Long id);
}
