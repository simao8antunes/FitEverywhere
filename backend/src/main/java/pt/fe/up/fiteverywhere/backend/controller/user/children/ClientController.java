package pt.fe.up.fiteverywhere.backend.controller.user.children;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.service.PurchaseService;
import pt.fe.up.fiteverywhere.backend.service.CalendarService;
import pt.fe.up.fiteverywhere.backend.service.user.children.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private PurchaseService purchaseService;

    @PutMapping("/workout-preferences")
    public ResponseEntity<?> saveWorkoutPreferences(
            @RequestParam int number,
            @RequestParam String time,
            @AuthenticationPrincipal OAuth2User principal) {

        System.out.println("Principal: " + principal); // Debug log
        String email = principal.getAttribute("email");
        System.out.println("Updating preferences for user: " + email); // Debug log
        Optional<Client> client = clientService.findClientByEmail(email);
        if (client.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        clientService.updatePreferences(client.get(), number, time);

        return ResponseEntity.ok(Map.of("message", "Preferences saved successfully"));
    }

    @PostMapping("/{gymId}/purchase")
    public ResponseEntity<?> purchaseMembership(
            @PathVariable Long gymId,
            @RequestParam String type,
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        System.out.println("Purchasing membership for user: " + email); // Debug log
        Optional<Client> client = clientService.findClientByEmail(email);
        if (client.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        try {
            purchaseService.purchaseMembership(client.get().getEmail(), gymId, type);
            return ResponseEntity.ok(Map.of("message", "Membership purchased successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Gym not found", "message", e.getMessage()));
        }
    }

    @PostMapping("/{serviceId}/purchase-service")
    public ResponseEntity<?> purchaseService(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal OAuth2User principal) {

        String email = principal.getAttribute("email");
        System.out.println("Purchasing service for user: " + email); // Debug log
        Optional<Client> client = clientService.findClientByEmail(email);
        if (client.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        try {
            clientService.buyPTService(client.get(), serviceId);
            return ResponseEntity.ok(Map.of("message", "Service purchased successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Service not found", "message", e.getMessage()));
        }
    }


    @GetMapping("/workout-suggestions")
    public ResponseEntity<?> getWorkoutSuggestions(
            @AuthenticationPrincipal OAuth2User principal,
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {

        String email = principal.getAttribute("email");

        Optional<Client> clientOptional = clientService.findClientByEmail(email);
        if (clientOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        Client client = clientOptional.get();

        try {
            // Fetch calendar events using the CalendarService
            Map<String, Object> calendarEvents = calendarService.fetchCalendarEvents(accessToken);

            // Generate workout suggestions using the ClientService
            List<String> suggestions = clientService.generateWorkoutSuggestions(client, calendarEvents);

            // Return the suggestions to the user
            return ResponseEntity.ok(suggestions);
        } catch (RuntimeException e) {
            // Handle any errors gracefully
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate workout suggestions", "message", e.getMessage()));
        }
    }

}
