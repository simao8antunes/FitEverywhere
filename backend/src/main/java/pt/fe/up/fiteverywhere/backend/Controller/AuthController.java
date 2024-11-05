package pt.fe.up.fiteverywhere.backend.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;  

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Adjust if needed
public class AuthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, String>> root() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the FitEverywhere API");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/auth/login/success")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("user", principal.getAttributes());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/error")
    public ResponseEntity<?> handleError() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
