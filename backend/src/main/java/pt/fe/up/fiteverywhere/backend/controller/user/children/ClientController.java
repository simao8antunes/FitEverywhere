package pt.fe.up.fiteverywhere.backend.controller.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.service.user.children.ClientService;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PutMapping("/workout-preferences")
    public ResponseEntity<?> saveWorkoutPreferences(
            @RequestParam int number,
            @RequestParam String time,
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        System.out.println("Updating preferences for user: " + email); // Debug log
        Optional<Client> client = clientService.findClientByEmail(email);
        if (client.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        clientService.updatePreferences(client.get(), number, time);

        return ResponseEntity.ok(Map.of("message", "Preferences saved successfully"));
    }

}
