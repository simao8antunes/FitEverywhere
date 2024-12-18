package pt.fe.up.fiteverywhere.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.PersonalTrainerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private GymManagerRepository gymManagerRepository;

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @InjectMocks
    private GymService gymService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getGymById_ShouldReturnGym_WhenExists() {
        // Arrange
        Gym gym = new Gym(1L, "Gym1");
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // Act
        Optional<Gym> result = gymService.getGymById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(gym);
    }

    @Test
    void getGymById_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(gymRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Gym> result = gymService.getGymById(1L);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void saveOrUpdateGym_ShouldSaveGym() {
        // Arrange
        Gym gym = new Gym(1L, "Gym1");

        // Act
        gymService.saveOrUpdateGym(gym);

        // Assert
        verify(gymRepository).save(gym);
    }

    @Test
    void createGymAndLinkToManager_ShouldLinkGymToManager() {
        // Arrange
        Gym gym = new Gym(1L, "Gym1");
        GymManager gymManager = new GymManager("ManagerName", "manager@test.com");

        when(gymRepository.save(gym)).thenReturn(gym);

        // Act
        Gym result = gymService.createGymAndLinkToManager(gymManager, gym);

        // Assert
        assertThat(result).isEqualTo(gym);
        assertThat(gymManager.getLinkedGyms()).contains(gym);
        assertThat(gym.getLinkedGymManagers()).contains(gymManager);
        verify(gymRepository).save(gym);
        verify(gymManagerRepository).save(gymManager);
    }

    @Test
    void linkPersonalTrainer_ShouldLinkTrainerToGym() {
        // Arrange
        Gym gym = new Gym(1L, "Gym1");
        PersonalTrainer personalTrainer = new PersonalTrainer("TrainerName", "trainer@test.com");

        when(gymRepository.save(gym)).thenReturn(gym);

        // Act
        Gym result = gymService.linkPersonalTrainer(gym, personalTrainer);

        // Assert
        assertThat(result).isEqualTo(gym);
        assertThat(personalTrainer.getLinkedGym()).isEqualTo(gym);
        assertThat(gym.getLinkedPersonalTrainers()).contains(personalTrainer);
        verify(gymRepository).save(gym);
        verify(personalTrainerRepository).save(personalTrainer);
    }

    @Test
    void getAllGyms_ShouldReturnAllGyms() {
        // Arrange
        Gym gym1 = new Gym(1L, "Gym1");
        Gym gym2 = new Gym(2L, "Gym2");
        when(gymRepository.findAll()).thenReturn(List.of(gym1, gym2));

        // Act
        Iterable<Gym> result = gymService.getAllGyms();

        // Assert
        assertThat(result).containsExactlyInAnyOrder(gym1, gym2);
    }
}
