package pt.fe.up.fiteverywhere.backend.Service;

import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.Repository.UserRepository;
import pt.fe.up.fiteverywhere.backend.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }
}