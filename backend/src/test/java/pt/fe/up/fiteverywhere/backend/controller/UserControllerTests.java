package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.service.UserService;
import pt.fe.up.fiteverywhere.backend.service.user.children.ClientService;
import pt.fe.up.fiteverywhere.backend.service.user.children.GymManagerService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private ClientService clientService;

    @Mock
    private GymManagerService gymManagerService;

    @Mock
    private PersonalTrainerService personalTrainerService;

    @InjectMocks
    private UserController userController;

    @Mock
    private OAuth2User principal;

    // ---- TESTS FOR handleError() ----
    @Test
    void handleError_ShouldReturnErrorMessage() {
        ResponseEntity<?> response = userController.handleError();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Authentication error occurred"));
    }

    // ---- TESTS FOR getUserInfo() ----
    @Test
    void getUserInfo_UserNotAuthenticated() {
        ResponseEntity<?> response = userController.getUserInfo(null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("User not authenticated");
    }

    @Test
    void getUserInfo_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserInfo(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("User not found");
    }

    @Test
    void getUserInfo_Success() {
        User user = new Client();
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(principal.getAttributes()).thenReturn(Map.of("name", "Test User"));

        ResponseEntity<?> response = userController.getUserInfo(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Login successful", "user", user, "userSpecs", Map.of("name", "Test User")));
    }

    // ---- TESTS FOR createNewUser() ----
    @Test
    void createNewUser_UserNotAuthenticated() {
        ResponseEntity<?> response = userController.createNewUser("client", null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "User not authenticated"));
    }

    @Test
    void createNewUser_InvalidRole() {
        lenient().when(principal.getAttribute("email")).thenReturn("test@example.com");

        ResponseEntity<?> response = userController.createNewUser("invalidRole", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "Invalid role"));
    }

    @Test
    void createNewUser_Success_Client() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test Client");

        ResponseEntity<?> response = userController.createNewUser("client", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Created user with role: client"));
        verify(clientService).save(any(Client.class));
    }

    @Test
    void createNewUser_Success_GymManager() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test GymManager");

        ResponseEntity<?> response = userController.createNewUser("gym", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Created user with role: gym"));
        verify(gymManagerService).save(any(GymManager.class));
    }

    @Test
    void createNewUser_Success_PersonalTrainer() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(principal.getAttribute("name")).thenReturn("Test PT");

        ResponseEntity<?> response = userController.createNewUser("pt", principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "Created user with role: pt"));
        verify(personalTrainerService).save(any(PersonalTrainer.class));
    }

    // ---- TESTS FOR updateUserDetails() ----
    @Test
    void updateUserDetails_UserNotFound() {
        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.updateUserDetails(new PersonalTrainer(), principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo(Map.of("error", "User not found for the provided email."));
    }

    @Test
    void updateUserDetails_Success() {
        PersonalTrainer existingUser = new PersonalTrainer();
        PersonalTrainer updatedUser = new PersonalTrainer();
        updatedUser.setDescription("Updated Description");

        when(principal.getAttribute("email")).thenReturn("test@example.com");
        when(userService.findUserByEmail("test@example.com")).thenReturn(Optional.of(existingUser));

        ResponseEntity<?> response = userController.updateUserDetails(updatedUser, principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(Map.of("message", "User details updated successfully!"));
        verify(personalTrainerService).save(existingUser);
        assertThat(existingUser.getDescription()).isEqualTo("Updated Description");
    }
}