package pt.fe.up.fiteverywhere.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PutMapping("/role")
    public ResponseEntity<Map<String, String>> updateUserRole(
            @RequestParam String role,
            @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
        }

        String email = principal.getAttribute("email");
        System.out.println("Updating role for user: " + email); // Debug log
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found"));
        }
        userService.updateUserRole(user, role);
        return ResponseEntity.ok(Map.of("message", "Role updated successfully"));
    }


    @GetMapping("/error")
    public ResponseEntity<?> handleError() {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @GetMapping("/calendar/events")
    public ResponseEntity<?> getCalendarEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

        // Construct the URL for Google Calendar API
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";
        RestTemplate restTemplate = new RestTemplate();

        // Set Authorization header with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Make the API call
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            // Return the response data (items are the events)
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Handle any errors gracefully
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch calendar events", "message", e.getMessage()));
        }
    }


    @GetMapping("/gyms/nearby")
    public ResponseEntity<?> getNearbyGyms(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int radius) {
    
        // Construct the Overpass API query URL
        String overpassApiUrl = String.format(
                "http://overpass-api.de/api/interpreter?data=[out:json];node[\"leisure\"=\"fitness_centre\"](around:%d,%f,%f);out;",
                radius, latitude, longitude
        );
    
        RestTemplate restTemplate = new RestTemplate();
    
        try {
            // Make the API call to Overpass
            ResponseEntity<Map> response = restTemplate.exchange(overpassApiUrl, HttpMethod.GET, null, Map.class);
    
            // Return the response data (results are the nearby gyms)
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            // Handle any errors gracefully
            return ResponseEntity.status(500).body(Map.of(
                    "error", "Failed to fetch nearby gyms",
                    "message", e.getMessage()
            ));
        }
    }
    
}