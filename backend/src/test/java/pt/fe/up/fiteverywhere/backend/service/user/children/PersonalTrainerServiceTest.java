package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;
import pt.fe.up.fiteverywhere.backend.repository.PTServiceRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.PersonalTrainerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PersonalTrainerServiceTest {

    @Mock
    private PersonalTrainerRepository personalTrainerRepository;

    @Mock
    private PTServiceRepository ptServiceRepository;

    @InjectMocks
    private PersonalTrainerService personalTrainerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ShouldSavePersonalTrainer() {
        // Arrange
        PersonalTrainer trainer = new PersonalTrainer("TrainerName", "test@trainer.com");

        // Act
        personalTrainerService.save(trainer);

        // Assert
        verify(personalTrainerRepository).save(trainer);
    }

    @Test
    void findPersonalByEmail_ShouldReturnTrainer_WhenExists() {
        // Arrange
        PersonalTrainer trainer = new PersonalTrainer("TrainerName", "test@trainer.com");
        when(personalTrainerRepository.findById("test@trainer.com")).thenReturn(Optional.of(trainer));

        // Act
        Optional<PersonalTrainer> result = personalTrainerService.findPersonalByEmail("test@trainer.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(trainer);
    }

    @Test
    void findPersonalByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(personalTrainerRepository.findById("test@trainer.com")).thenReturn(Optional.empty());

        // Act
        Optional<PersonalTrainer> result = personalTrainerService.findPersonalByEmail("test@trainer.com");

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void addPTService_ShouldAddServiceToTrainer_WhenTrainerExists() {
        // Arrange
        PersonalTrainer trainer = new PersonalTrainer("TrainerName", "test@trainer.com");
        PTService service = new PTService();
        service.setName("ServiceName");

        when(personalTrainerRepository.findById("test@trainer.com")).thenReturn(Optional.of(trainer));
        when(ptServiceRepository.save(service)).thenReturn(service);

        // Act
        PTService result = personalTrainerService.addPTService("test@trainer.com", service);

        // Assert
        assertThat(result).isEqualTo(service);
        assertThat(service.getPersonalTrainer()).isEqualTo(trainer);
        verify(ptServiceRepository).save(service);
    }

    @Test
    void addPTService_ShouldThrowException_WhenTrainerNotFound() {
        // Arrange
        PTService service = new PTService();
        when(personalTrainerRepository.findById("nonexistent@trainer.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> personalTrainerService.addPTService("nonexistent@trainer.com", service))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Personal trainer not found");
    }

    @Test
    void findPTByEmail_ShouldReturnTrainer_WhenExists() {
        // Arrange
        PersonalTrainer trainer = new PersonalTrainer("TrainerName", "test@trainer.com");
        when(personalTrainerRepository.findById("test@trainer.com")).thenReturn(Optional.of(trainer));

        // Act
        Optional<PersonalTrainer> result = personalTrainerService.findPTByEmail("test@trainer.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(trainer);
    }

    @Test
    void findPTByEmail_ShouldReturnEmpty_WhenNotExists() {
        // Arrange
        when(personalTrainerRepository.findById("test@trainer.com")).thenReturn(Optional.empty());

        // Act
        Optional<PersonalTrainer> result = personalTrainerService.findPTByEmail("test@trainer.com");

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void getAvailablePTs_ShouldReturnTrainersWithoutGym() {
        // Arrange
        PersonalTrainer trainer1 = new PersonalTrainer("Trainer1", "trainer1@trainer.com");
        PersonalTrainer trainer2 = new PersonalTrainer("Trainer2", "trainer2@trainer.com");
        trainer1.setLinkedGym(null);
        trainer2.setLinkedGym(null);

        when(personalTrainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));

        // Act
        Iterable<PersonalTrainer> result = personalTrainerService.getAvailablePTs();

        // Assert
        assertThat(result).containsExactlyInAnyOrder(trainer1, trainer2);
    }

    @Test
    void getAvailablePTs_ShouldReturnEmpty_WhenAllTrainersLinkedToGyms() {
        // Arrange
        Gym gym = new Gym(1L, "Test Gym"); // Create a Gym object
        PersonalTrainer trainer1 = new PersonalTrainer("Trainer1", "trainer1@trainer.com");
        trainer1.setLinkedGym(gym); // Assign the gym to the trainer

        when(personalTrainerRepository.findAll()).thenReturn(List.of(trainer1));

        // Act
        Iterable<PersonalTrainer> result = personalTrainerService.getAvailablePTs();

        // Assert
        assertThat(result).isEmpty(); // Expecting no available trainers
    }


    @Test
    void getAllPersonalTrainers_ShouldReturnAllTrainers() {
        // Arrange
        PersonalTrainer trainer1 = new PersonalTrainer("Trainer1", "trainer1@trainer.com");
        PersonalTrainer trainer2 = new PersonalTrainer("Trainer2", "trainer2@trainer.com");

        when(personalTrainerRepository.findAll()).thenReturn(List.of(trainer1, trainer2));

        // Act
        Iterable<PersonalTrainer> result = personalTrainerService.getAllPersonalTrainers();

        // Assert
        assertThat(result).containsExactlyInAnyOrder(trainer1, trainer2);
    }
}
