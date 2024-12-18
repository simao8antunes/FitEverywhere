package pt.fe.up.fiteverywhere.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.Purchase;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.PurchaseRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PurchaseServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void purchaseMembership_ShouldPurchase_WhenValidDetails() {
        // Arrange
        Client client = new Client();
        Gym gym = new Gym(1L, "Gym1");
        gym.setDailyFee(10.0);

        when(clientRepository.findById("client@test.com")).thenReturn(Optional.of(client));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // Act
        purchaseService.purchaseMembership("client@test.com", 1L, "dailyFee");

        // Assert
        verify(purchaseRepository).save(any(Purchase.class));
    }

    @Test
    void purchaseMembership_ShouldThrowException_WhenClientNotFound() {
        // Arrange
        when(clientRepository.findById("client@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> purchaseService.purchaseMembership("client@test.com", 1L, "dailyFee"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Client not found");
    }

    @Test
    void purchaseMembership_ShouldThrowException_WhenGymNotFound() {
        // Arrange
        Client client = new Client();
        when(clientRepository.findById("client@test.com")).thenReturn(Optional.of(client));
        when(gymRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> purchaseService.purchaseMembership("client@test.com", 1L, "dailyFee"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Gym not found");
    }

    @Test
    void purchaseMembership_ShouldThrowException_WhenInvalidMembershipType() {
        // Arrange
        Client client = new Client();
        Gym gym = new Gym(1L, "Gym1");

        when(clientRepository.findById("client@test.com")).thenReturn(Optional.of(client));
        when(gymRepository.findById(1L)).thenReturn(Optional.of(gym));

        // Act & Assert
        assertThatThrownBy(() -> purchaseService.purchaseMembership("client@test.com", 1L, "invalidType"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid type or gym does not offer this type of membership");
    }
}
