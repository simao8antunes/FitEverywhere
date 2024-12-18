package pt.fe.up.fiteverywhere.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.*;

class CalendarServiceTest {

    private CalendarService calendarService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        calendarService = new CalendarService(); // Original implementation instantiates RestTemplate internally
    }

    @Test
    void fetchCalendarEvents_ShouldReturnEvents_WhenSuccessful() {
        // Arrange
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.getTokenValue()).thenReturn("mock-token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("mock-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> mockResponse = Map.of("items", "some events");
        RestTemplate mockRestTemplate = mock(RestTemplate.class);

        // Mock the internal behavior of RestTemplate
        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(Map.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        // Use reflection to replace the RestTemplate instance in CalendarService
        ReflectionTestUtils.setField(calendarService, "restTemplate", mockRestTemplate);

        // Act
        Map<String, Object> result = calendarService.fetchCalendarEvents(accessToken);

        // Assert
        assertThat(result).isEqualTo(mockResponse);
    }

    @Test
    void fetchCalendarEvents_ShouldThrowRuntimeException_OnHttpClientError() {
        // Arrange
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.getTokenValue()).thenReturn("mock-token");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("mock-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate mockRestTemplate = mock(RestTemplate.class);
        when(mockRestTemplate.exchange(anyString(), eq(HttpMethod.GET), eq(entity), eq(Map.class)))
                .thenThrow(new RuntimeException("Client error"));

        // Use reflection to replace the RestTemplate instance in CalendarService
        ReflectionTestUtils.setField(calendarService, "restTemplate", mockRestTemplate);

        // Act & Assert
        Throwable thrown = catchThrowable(() -> calendarService.fetchCalendarEvents(accessToken));

        // Assert
        assertThat(thrown).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected error occurred while fetching calendar events")
                .hasCauseInstanceOf(RuntimeException.class);

        assertThat(thrown.getCause()).hasMessageContaining("Client error");
    }

}
