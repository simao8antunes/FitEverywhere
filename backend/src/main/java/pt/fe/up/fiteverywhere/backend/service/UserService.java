package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(String username, String email, String password) {
        User user = new User(username, email, password);
        userRepository.save(user);
    }

    public User loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public boolean isUserExists(String username, String email) {
        return userRepository.findByUsername(username) != null || userRepository.findByEmail(email) != null;
    }

    public User findOrRegisterOAuthUser(String name, String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setUsername(name);
            user.setEmail(email);
            userRepository.save(user);
        }

        return user;
    }

}
