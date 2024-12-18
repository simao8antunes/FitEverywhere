package pt.fe.up.fiteverywhere.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.fe.up.fiteverywhere.backend.service.CalendarService;

import java.util.Map;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/events")
    public ResponseEntity<?> getCalendarEvents(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient) {
        try {
            // Retrieve access token from authorized client
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
            // Fetch events using the service
            Map<String, Object> events = calendarService.fetchCalendarEvents(accessToken);

            // Return the events data
            return ResponseEntity.ok(events);
        } catch (RuntimeException e) {
            // Handle any errors gracefully
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch calendar events", "message", e.getMessage()));
        }
    }
}
