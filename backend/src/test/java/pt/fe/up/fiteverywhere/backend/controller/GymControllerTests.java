package pt.fe.up.fiteverywhere.backend.controller;

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
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.service.GymService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymControllerTests {

    @Mock
    private GymService gymService;

    @Mock
    private GymManagerService gymManagerService;

    @Mock
    private PersonalTrainerService personalTrainerService;

    @Mock
    private OAuth2User principal;

    @InjectMocks
    private GymController gymController;

    private static final String MOCK_EMAIL = "test@gym.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lenient().when(principal.getAttribute("email")).thenReturn(MOCK_EMAIL);
    }


    @Test
    void createGym_ShouldReturnOkResponse_WhenGymManagerExists() {
        // Arrange
        GymManager gymManager = new GymManager("TestUser", MOCK_EMAIL);
        Gym createdGym = new Gym(1L, "Test Gym");

        when(gymManagerService.findGymManagerByEmail(MOCK_EMAIL)).thenReturn(Optional.of(gymManager));
        when(gymService.createGymAndLinkToManager(any(GymManager.class), any(Gym.class))).thenReturn(createdGym);

        // Act
        ResponseEntity<String> response = gymController.createGym(principal, "Test Gym", 1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Successfully created gym with id: 1");
    }


    @Test
    void createGym_ShouldReturnNotFound_WhenGymManagerDoesNotExist() {
        // Arrange
        when(gymManagerService.findGymManagerByEmail(MOCK_EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = gymController.createGym(principal, "Test Gym", 1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void updateGymDetails_ShouldReturnOkResponse_WhenGymDetailsUpdatedSuccessfully() {
        // Arrange
        GymManager gymManager = new GymManager("TestUser", MOCK_EMAIL);
        Gym gym = new Gym(1L, "Updated Gym");
        gym.setDescription("Updated Description");
        gym.setDailyFee(20.0);
        gym.setWeeklyMembership(100.0);

        Gym existingGym = new Gym(1L, "Original Gym");
        existingGym.setLinkedGymManagers(Set.of(gymManager));

        when(gymManagerService.findGymManagerByEmail(MOCK_EMAIL)).thenReturn(Optional.of(gymManager));
        when(gymService.getGymById(1L)).thenReturn(Optional.of(existingGym));

        // Act
        ResponseEntity<?> response = gymController.updateGymDetails(gym, principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Gym details updated successfully!"));
        verify(gymService).saveOrUpdateGym(existingGym);
    }

    @Test
    void updateGymDetails_ShouldReturnUnauthorized_WhenGymManagerNotFound() {
        // Arrange
        when(gymManagerService.findGymManagerByEmail(MOCK_EMAIL)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = gymController.updateGymDetails(new Gym(1L, "Test Gym"), principal);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "User not found for the provided email."));
    }

    @Test
    void getGymDetails_ShouldReturnGymDetails_WhenGymExists() {
        // Arrange
        GymManager gymManager = new GymManager("TestUser", MOCK_EMAIL);
        Gym gym = new Gym(1L, "Test Gym");
        gymManager.setLinkedGyms(Set.of(gym));

        when(gymManagerService.findGymManagerByEmail(MOCK_EMAIL)).thenReturn(Optional.of(gymManager));

        // Act
        ResponseEntity<?> response = gymController.getGymDetails(principal, 1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.get("name")).isEqualTo("Test Gym");
    }

    @Test
    void linkPT_ShouldReturnOkResponse_WhenLinkedSuccessfully() {
        // Arrange
        Gym gym = new Gym(1L, "Test Gym");
        PersonalTrainer personalTrainer = new PersonalTrainer("PTUser", "pt@gym.com");

        lenient().when(gymService.getGymById(1L)).thenReturn(Optional.of(gym));
        lenient().when(personalTrainerService.findPTByEmail("pt@gym.com")).thenReturn(Optional.of(personalTrainer));

        // Act
        ResponseEntity<?> response = gymController.linkPT(1L, "pt@gym.com");

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Personal trainer linked successfully."));
        verify(gymService).linkPersonalTrainer(gym, personalTrainer);
    }

    @Test
    void getAllGyms_ShouldReturnAllGyms_WhenGymsExist() {
        // Arrange
        Gym gym1 = new Gym(1L, "Gym 1");
        Gym gym2 = new Gym(2L, "Gym 2");

        when(gymService.getAllGyms()).thenReturn(List.of(gym1, gym2));

        // Act
        ResponseEntity<?> response = gymController.getAllGyms();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(List.of(gym1, gym2));
    }
}
