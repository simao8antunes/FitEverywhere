package pt.fe.up.fiteverywhere.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.fe.up.fiteverywhere.backend.Entity.User;
import pt.fe.up.fiteverywhere.backend.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String username, String email, String password) {
        User user = new User(username, email, password);
        return userRepository.save(user);
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
