package pt.fe.up.fiteverywhere.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.fe.up.fiteverywhere.backend.Entity.User;
import pt.fe.up.fiteverywhere.backend.Service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.isUserExists(user.getUsername(), user.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User already exists");
            return ResponseEntity.badRequest().body(response);
        }
        userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User loggedInUser = userService.loginUser(user.getUsername(), user.getPassword());
        if (loggedInUser != null) {
            // Optionally generate a token (JWT) here and return it
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", loggedInUser);  // Or just return a subset like ID, username, roles, etc.
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid credentials");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
