package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.PTServiceRepository;
import pt.fe.up.fiteverywhere.backend.repository.WorkoutSuggestionRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private WorkoutSuggestionRepository workoutSuggestionRepository;

    @Mock
    private PTServiceRepository ptServiceRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findClientByEmail_ShouldReturnClient_WhenExists() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");
        when(clientRepository.findById("test@client.com")).thenReturn(Optional.of(client));

        // Act
        Optional<Client> result = clientService.findClientByEmail("test@client.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(client);
    }

    @Test
    void findClientByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(clientRepository.findById("test@client.com")).thenReturn(Optional.empty());

        // Act
        Optional<Client> result = clientService.findClientByEmail("test@client.com");

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void updatePreferences_ShouldUpdatePreferences() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");

        // Act
        clientService.updatePreferences(client, 3, "morning");

        // Assert
        assertThat(client.getPreferredTime()).isEqualTo("morning");
        assertThat(client.getWorkoutsPerWeek()).isEqualTo(3);
        verify(clientRepository).save(client);
    }

    @Test
    void save_ShouldSaveClient() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");

        // Act
        clientService.save(client);

        // Assert
        verify(clientRepository).save(client);
    }

    @Test
    void generateWorkoutSuggestions_ShouldReturnSuggestions() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");
        client.setPreferredTime("morning");
        client.setWorkoutsPerWeek(3);

        Map<String, Object> calendarEvents = Map.of("items", new ArrayList<>());

        // Act
        List<String> suggestions = clientService.generateWorkoutSuggestions(client, calendarEvents);

        // Assert
        assertThat(suggestions).hasSize(3);
    }

    @Test
    void saveWorkoutSuggestions_ShouldSaveSuggestions() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");
        WorkoutSuggestion suggestion = new WorkoutSuggestion();
        suggestion.setTime("2023-12-18 07:00");
        suggestion.setGym(null);

        List<WorkoutSuggestion> suggestions = List.of(suggestion);

        // Act
        List<WorkoutSuggestion> result = clientService.saveWorkoutSuggestions(client, suggestions);

        // Assert
        assertThat(result).hasSize(1);
        verify(workoutSuggestionRepository, times(1)).save(any(WorkoutSuggestion.class));
    }

    @Test
    void findAvailableSlotWithRange_ShouldReturnSlot_WhenAvailable() {
        // Arrange
        List<String> busyTimes = new ArrayList<>();
        String preferredTime = "morning";
        LocalDateTime currentDate = LocalDateTime.now();

        // Act
        String result = clientService.findAvailableSlotWithRange(busyTimes, preferredTime, 0, 2, currentDate);

        // Assert
        assertThat(result).doesNotContain("No available slots");
    }

    @Test
    void findAvailableSlotWithRange_ShouldReturnNoSlots_WhenBusy() {
        // Arrange
        List<String> busyTimes = new ArrayList<>();
        // Add busy times that cover the entire preferred time range for the ±2-day range
        busyTimes.add(LocalDateTime.now().toLocalDate() + " 07:00-11:00");
        busyTimes.add(LocalDateTime.now().toLocalDate().plusDays(1) + " 07:00-11:00");
        busyTimes.add(LocalDateTime.now().toLocalDate().plusDays(2) + " 07:00-11:00");
        busyTimes.add(LocalDateTime.now().toLocalDate().minusDays(1) + " 07:00-11:00");
        busyTimes.add(LocalDateTime.now().toLocalDate().minusDays(2) + " 07:00-11:00");

        String preferredTime = "morning";
        LocalDateTime currentDate = LocalDateTime.now();

        // Act
        String result = clientService.findAvailableSlotWithRange(busyTimes, preferredTime, 0, 2, currentDate);

        // Assert
        assertThat(result).isEqualTo("No available slots");
    }



    @Test
    void buyPTService_ShouldAddService() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");
        PTService service = new PTService();
        service.setId(1L);

        when(ptServiceRepository.findById(1L)).thenReturn(Optional.of(service));

        // Act
        clientService.buyPTService(client, 1L);

        // Assert
        assertThat(client.getPtServices()).contains(service);
        verify(clientRepository).save(client);
    }

    @Test
    void buyPTService_ShouldThrowException_WhenAlreadyHasService() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");
        PTService service = new PTService();
        service.setId(1L);
        client.getPtServices().add(service);

        // Act & Assert
        assertThatThrownBy(() -> clientService.buyPTService(client, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client already has this service");
    }

    @Test
    void buyPTService_ShouldThrowException_WhenServiceNotFound() {
        // Arrange
        Client client = new Client("ClientName", "test@client.com");

        when(ptServiceRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clientService.buyPTService(client, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Service not found");
    }

    @Test
    void formatBusyTime_ShouldFormatCorrectly() {
        // Act
        String result = clientService.formatBusyTime("2023-12-18T07:00:00", "2023-12-18T09:00:00");

        // Assert
        assertThat(result).isEqualTo("2023-12-18 07:00-09:00");
    }

    @Test
    void extractBusyTimes_ShouldHandleValidEvents() {
        // Arrange
        List<Map<String, Object>> events = List.of(
                Map.of(
                        "start", Map.of("dateTime", "2023-12-18T07:00:00"),
                        "end", Map.of("dateTime", "2023-12-18T09:00:00")
                )
        );

        // Act
        List<String> result = clientService.extractBusyTimes(events);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo("2023-12-18 07:00-09:00");
    }

    @Test
    void getPreferredTimeRange_ShouldReturnCorrectRange() {
        // Act
        List<LocalDateTime> result = clientService.getPreferredTimeRange("morning");

        // Assert
        assertThat(result).hasSize(2);
    }

    @Test
    void getPreferredTimeRange_ShouldThrowException_ForInvalidTime() {
        // Act & Assert
        assertThatThrownBy(() -> clientService.getPreferredTimeRange("invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid preferred time");
    }
}
