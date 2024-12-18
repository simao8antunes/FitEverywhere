package pt.fe.up.fiteverywhere.backend.controller.user.children;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.service.UserService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonalTrainerControllerTests {

    @Mock
    private PersonalTrainerService personalTrainerService;

    @Mock
    private UserService userService;

    @Mock
    private OAuth2User principal;

    @InjectMocks
    private PersonalTrainerController personalTrainerController;

    private static final String MOCK_EMAIL = "test@trainer.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lenient().when(principal.getAttribute("email")).thenReturn(MOCK_EMAIL);
    }

    @Test
    void addService_ShouldReturnOkResponse_WhenServiceIsAddedSuccessfully() {
        // Arrange
        PTService serviceDTO = new PTService();
        serviceDTO.setId(0L);
        serviceDTO.setName("Training Service");
        serviceDTO.setDescription("Service Description");
        serviceDTO.setPrice(100.0);
        serviceDTO.setDuration(60);
        serviceDTO.setType("Personal Training");

        PersonalTrainer trainer = new PersonalTrainer("TrainerName", MOCK_EMAIL);
        PTService createdService = new PTService();
        createdService.setId(1L);
        createdService.setName("Training Service");

        when(personalTrainerService.findPersonalByEmail(MOCK_EMAIL)).thenReturn(Optional.of(trainer));
        when(personalTrainerService.addPTService(anyString(), any(PTService.class))).thenReturn(createdService);

        // Act
        ResponseEntity<String> response = personalTrainerController.addService(serviceDTO, principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Service added successfully with id: 1");
    }

    @Test
    void addService_ShouldReturnUnauthorized_WhenTrainerNotFound() {
        // Arrange
        PTService serviceDTO = new PTService();
        when(personalTrainerService.findPersonalByEmail(MOCK_EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = personalTrainerController.addService(serviceDTO, principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void addService_ShouldReturnNotFound_WhenIllegalArgumentExceptionThrown() {
        // Arrange
        PTService serviceDTO = new PTService();
        serviceDTO.setId(0L);
        PersonalTrainer trainer = new PersonalTrainer("TrainerName", MOCK_EMAIL);

        when(personalTrainerService.findPersonalByEmail(MOCK_EMAIL)).thenReturn(Optional.of(trainer));
        when(personalTrainerService.addPTService(anyString(), any(PTService.class)))
                .thenThrow(new IllegalArgumentException("Invalid service data"));

        // Act
        ResponseEntity<String> response = personalTrainerController.addService(serviceDTO, principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Invalid service data");
    }

    @Test
    void getPersonalTrainers_ShouldReturnListOfTrainers_WhenUserExists() {
        // Arrange
        PersonalTrainer trainer1 = new PersonalTrainer("Trainer1", "trainer1@trainer.com");
        PersonalTrainer trainer2 = new PersonalTrainer("Trainer2", "trainer2@trainer.com");

        when(userService.findUserByEmail(MOCK_EMAIL)).thenReturn(Optional.of(trainer1));
        when(personalTrainerService.getAllPersonalTrainers()).thenReturn(List.of(trainer1, trainer2));

        // Act
        ResponseEntity<?> response = personalTrainerController.getPersonalTrainers(principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(trainer1, trainer2));
    }

    @Test
    void getPersonalTrainers_ShouldReturnNotFound_WhenUserDoesNotExist() {
        // Arrange
        when(userService.findUserByEmail(MOCK_EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = personalTrainerController.getPersonalTrainers(principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void getAvailablePTs_ShouldReturnListOfAvailableTrainers_WhenTheyExist() {
        // Arrange
        PersonalTrainer trainer1 = new PersonalTrainer("Trainer1", "trainer1@trainer.com");
        PersonalTrainer trainer2 = new PersonalTrainer("Trainer2", "trainer2@trainer.com");

        when(personalTrainerService.getAvailablePTs()).thenReturn(List.of(trainer1, trainer2));

        // Act
        ResponseEntity<?> response = personalTrainerController.getAvailablePTs();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(trainer1, trainer2));
    }

    @Test
    void getAvailablePTs_ShouldReturnNoContent_WhenNoAvailableTrainersExist() {
        // Arrange
        when(personalTrainerService.getAvailablePTs()).thenReturn(null);

        // Act
        ResponseEntity<?> response = personalTrainerController.getAvailablePTs();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "No available pts found."));
    }
}
