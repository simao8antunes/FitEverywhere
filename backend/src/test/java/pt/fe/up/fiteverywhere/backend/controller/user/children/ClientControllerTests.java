package pt.fe.up.fiteverywhere.backend.controller.user.children;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.WorkoutSuggestionRepository;
import pt.fe.up.fiteverywhere.backend.service.CalendarService;
import pt.fe.up.fiteverywhere.backend.service.PurchaseService;
import pt.fe.up.fiteverywhere.backend.service.WorkoutSuggestionService;
import pt.fe.up.fiteverywhere.backend.service.user.children.ClientService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTests {

    @Mock
    private WorkoutSuggestionService workoutSuggestionService;

    @Mock
    private ClientService clientService;

    @Mock
    private CalendarService calendarService;

    @Mock
    private PurchaseService purchaseService;

    @Mock
    private WorkoutSuggestionRepository workoutSuggestionRepository;

    @InjectMocks
    private ClientController clientController;

    @Mock
    private OAuth2User principal;

    @Mock
    private OAuth2AuthorizedClient authorizedClient;

    @Mock
    private OAuth2AccessToken accessToken;

    // ---- TESTS FOR saveWorkoutPreferences() ----
    @Test
    void saveWorkoutPreferences_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.saveWorkoutPreferences(3, "Morning", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void saveWorkoutPreferences_Success() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));

        ResponseEntity<?> response = clientController.saveWorkoutPreferences(3, "Morning", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Preferences saved successfully"));
        verify(clientService).updatePreferences(client, 3, "Morning");
    }

    // ---- TESTS FOR getWorkoutSuggestions() ----
    @Test
    void getWorkoutSuggestions_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.getWorkoutSuggestions(principal, authorizedClient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void getWorkoutSuggestions_CalendarServiceThrowsException() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(calendarService.fetchCalendarEvents(accessToken)).thenThrow(new RuntimeException("Calendar fetch failed"));

        ResponseEntity<?> response = clientController.getWorkoutSuggestions(principal, authorizedClient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Failed to generate workout suggestions", "message", "Calendar fetch failed"));
    }

    @Test
    void getWorkoutSuggestions_Success() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(authorizedClient.getAccessToken()).thenReturn(accessToken);
        when(calendarService.fetchCalendarEvents(accessToken)).thenReturn(Map.of("event", "mockEvent"));
        when(clientService.generateWorkoutSuggestions(client, Map.of("event", "mockEvent"))).thenReturn(List.of("Suggestion1", "Suggestion2"));

        ResponseEntity<?> response = clientController.getWorkoutSuggestions(principal, authorizedClient);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of("Suggestion1", "Suggestion2"));
    }

    // ---- TESTS FOR purchaseMembership() ----
    @Test
    void purchaseMembership_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.purchaseMembership(1L, "premium", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void purchaseMembership_PurchaseThrowsException() {
        Client client = new Client();
        client.setEmail("test@example.com");
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("Gym not found")).when(purchaseService).purchaseMembership("test@example.com", 1L, "premium");

        ResponseEntity<?> response = clientController.purchaseMembership(1L, "premium", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Gym not found", "message", "Gym not found"));
    }

    @Test
    void purchaseMembership_Success() {
        Client client = new Client();
        client.setEmail("test@example.com");
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));

        ResponseEntity<?> response = clientController.purchaseMembership(1L, "premium", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Membership purchased successfully"));
        verify(purchaseService).purchaseMembership("test@example.com", 1L, "premium");
    }

    // ---- TESTS FOR purchaseService() ----
    @Test
    void purchaseService_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.purchaseService(1L, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void purchaseService_ServiceNotFound() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("Service not found")).when(clientService).buyPTService(client, 1L);

        ResponseEntity<?> response = clientController.purchaseService(1L, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Service not found", "message", "Service not found"));
    }

    @Test
    void purchaseService_Success() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));

        ResponseEntity<?> response = clientController.purchaseService(1L, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Service purchased successfully"));
        verify(clientService).buyPTService(client, 1L);
    }

    // ---- TESTS FOR saveWorkoutSuggestions() ----
    @Test
    void saveWorkoutSuggestions_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.saveWorkoutSuggestions(principal, authorizedClient, List.of());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void saveWorkoutSuggestions_InternalServerError() {
        Client client = new Client();
        List<WorkoutSuggestion> suggestions = List.of(new WorkoutSuggestion());
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("Database error")).when(clientService).saveWorkoutSuggestions(client, suggestions);

        ResponseEntity<?> response = clientController.saveWorkoutSuggestions(principal, authorizedClient, suggestions);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo("Error saving workout suggestions");
    }

    @Test
    void saveWorkoutSuggestions_Success() {
        Client client = new Client();
        List<WorkoutSuggestion> suggestions = List.of(new WorkoutSuggestion());
        List<WorkoutSuggestion> savedSuggestions = List.of(new WorkoutSuggestion());
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(clientService.saveWorkoutSuggestions(client, suggestions)).thenReturn(savedSuggestions);

        ResponseEntity<?> response = clientController.saveWorkoutSuggestions(principal, authorizedClient, suggestions);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(savedSuggestions);
    }

    // ---- TESTS FOR getSavedWorkoutSuggestions() ----
    @Test
    void getSavedWorkoutSuggestions_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.getSavedWorkoutSuggestions(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void getSavedWorkoutSuggestions_InternalServerError() {
        Client client = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(workoutSuggestionRepository.findByClient(client)).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = clientController.getSavedWorkoutSuggestions(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Failed to fetch saved workout suggestions", "message", "Database error"));
    }

    @Test
    void getSavedWorkoutSuggestions_Success() {
        Client client = new Client();
        WorkoutSuggestion suggestion = new WorkoutSuggestion();
        suggestion.setId(1L);
        suggestion.setTime("Morning");
        suggestion.setGym("MockGym");
        List<WorkoutSuggestion> suggestions = List.of(suggestion);
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(clientService.findClientByEmail("test@example.com")).thenReturn(Optional.of(client));
        when(workoutSuggestionRepository.findByClient(client)).thenReturn(suggestions);

        ResponseEntity<?> response = clientController.getSavedWorkoutSuggestions(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(Map.of("id", 1L, "time", "Morning", "gym", "MockGym")));
    }

    // ---- TESTS FOR deleteWorkoutSuggestion() ----
    @Test
    void deleteWorkoutSuggestion_NotFound() {
        when(workoutSuggestionService.getWorkoutSuggestionById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.deleteWorkoutSuggestion(1L, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Workout suggestion not found.");
    }

    @Test
    void deleteWorkoutSuggestion_Success() {
        WorkoutSuggestion suggestion = new WorkoutSuggestion();
        suggestion.setId(1L);
        when(workoutSuggestionService.getWorkoutSuggestionById(1L)).thenReturn(Optional.of(suggestion));

        ResponseEntity<?> response = clientController.deleteWorkoutSuggestion(1L, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Workout suggestion deleted successfully"));
        verify(workoutSuggestionService).deleteWorkoutSuggestion(1L);
    }
}