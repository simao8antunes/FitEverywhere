package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GymManagerServiceTest {

    @Mock
    private GymManagerRepository gymManagerRepository;

    @InjectMocks
    private GymManagerService gymManagerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldSaveGymManager() {
        // Arrange
        GymManager gymManager = new GymManager("ManagerName", "manager@test.com");

        // Act
        gymManagerService.save(gymManager);

        // Assert
        verify(gymManagerRepository).save(gymManager);
    }

    @Test
    void findGymManagerByEmail_ShouldReturnGymManager_WhenExists() {
        // Arrange
        GymManager gymManager = new GymManager("ManagerName", "manager@test.com");
        when(gymManagerRepository.findById("manager@test.com")).thenReturn(Optional.of(gymManager));

        // Act
        Optional<GymManager> result = gymManagerService.findGymManagerByEmail("manager@test.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(gymManager);
    }

    @Test
    void findGymManagerByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(gymManagerRepository.findById("manager@test.com")).thenReturn(Optional.empty());

        // Act
        Optional<GymManager> result = gymManagerService.findGymManagerByEmail("manager@test.com");

        // Assert
        assertThat(result).isNotPresent();
    }
}
