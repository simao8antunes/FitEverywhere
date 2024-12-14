package pt.fe.up.fiteverywhere.backend.service;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CalendarService {
    private final RestTemplate restTemplate = new RestTemplate();
    String GOOGLE_CALENDAR_EVENTS_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    public Map<String, Object> fetchCalendarEvents(OAuth2AccessToken accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.getTokenValue());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(GOOGLE_CALENDAR_EVENTS_URL, HttpMethod.GET, entity, Map.class);
            // Return the response body (events)
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Handle client and server-side HTTP errors
            throw new RuntimeException("Failed to fetch calendar events: " + e.getStatusCode(), e);
        } catch (Exception e) {
            // Handle any other errors gracefully
            throw new RuntimeException("Unexpected error occurred while fetching calendar events", e);
        }
    }
}
