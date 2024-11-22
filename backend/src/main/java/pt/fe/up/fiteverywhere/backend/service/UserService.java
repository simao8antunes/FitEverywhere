package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElse(null);  // Return user if found, otherwise null
    }

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


    public void save(User user) {
        userRepository.save(user);
    }

    public User findOrRegisterOAuthUser(String name, String email) {
        User existingUser = userRepository.findByEmail(email).orElse(null);
        if (existingUser != null) {
            return existingUser;
        }
        // Register the new user
        User newUser = new User();
        newUser.setUsername(name);
        newUser.setEmail(email);
        return userRepository.save(newUser);
    }

    public void updateUserRole(User user, String role) {
        user.setRole(role);
        userRepository.save(user);
    }

    public void updatePreferences(User user, int number, String time) {
        user.setPreferredTime(time);
        user.setWorkoutsPerWeek(number);
        userRepository.save(user);
    }

    public List<String> generateWorkoutSchedule(User user, List<Map<String, Object>> calendarEvents) {
//        int workoutsPerWeek = user.getWorkoutsPerWeek();
//        String preferredTime = user.getPreferredTime();
//        List<String> schedule = new ArrayList<>();
//
//        // Example: Distribute workouts while avoiding calendar events
//        for (int i = 0; i < 7; i++) {
//            if (schedule.size() >= workoutsPerWeek) break;
//
//            String day = LocalDate.now().plusDays(i).getDayOfWeek().name();
//            boolean hasConflict = calendarEvents.stream()
//                    .anyMatch(event -> event.get("day").equals(day) && event.get("time").equals(preferredTime));
//
//            if (!hasConflict) {
//                schedule.add(day + " " + preferredTime);
//            }
//        }
//
//        return schedule;
        return List.of();
    }

}
