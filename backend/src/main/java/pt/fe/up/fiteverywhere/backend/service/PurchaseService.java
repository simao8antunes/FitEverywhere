package pt.fe.up.fiteverywhere.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.Purchase;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.PurchaseRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

@Service
public class PurchaseService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private GymRepository gymRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Transactional
    public void purchaseMembership(String clientEmail, Long gymId, String type) {
        Client client = clientRepository.findById(clientEmail).orElseThrow(() -> new IllegalArgumentException("Client not found"));
        Gym gym = gymRepository.findById(gymId).orElseThrow(() -> new IllegalArgumentException("Gym not found"));

        Double price = "dailyFee".equals(type) ? gym.getDailyFee() : gym.getWeeklyMembership();
        if (price == null) {
            throw new IllegalArgumentException("Invalid type or gym does not offer this type of membership");
        }

        Purchase purchase = new Purchase(client, gym, type, price);
        purchaseRepository.save(purchase);
    }
}
