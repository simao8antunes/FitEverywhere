package pt.fe.up.fiteverywhere.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenExists() {
        // Arrange
        User user = mock(User.class);
        when(userRepository.findById("user@test.com")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findUserByEmail("user@test.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(user);
    }

    @Test
    void findUserByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(userRepository.findById("user@test.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserByEmail("user@test.com");

        // Assert
        assertThat(result).isNotPresent();
    }
}
