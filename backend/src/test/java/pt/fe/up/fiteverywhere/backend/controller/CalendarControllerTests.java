package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import pt.fe.up.fiteverywhere.backend.service.CalendarService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalendarControllerTests {

    @Mock
    private CalendarService calendarService;

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2AccessToken accessToken;

    // ---- TEST FOR getCalendarEvents() ----
    @Test
    void getCalendarEvents_FetchError() {
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(calendarService.fetchCalendarEvents(accessToken)).thenThrow(new RuntimeException("Calendar service error"));

        ResponseEntity<?> response = calendarController.getCalendarEvents(authorizedClient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Failed to fetch calendar events", "message", "Calendar service error"));
    }

    @Test
    void getCalendarEvents_Success() {
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(calendarService.fetchCalendarEvents(accessToken)).thenReturn(Map.of("event", "testEvent"));

        ResponseEntity<?> response = calendarController.getCalendarEvents(authorizedClient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("event", "testEvent"));
    }
}