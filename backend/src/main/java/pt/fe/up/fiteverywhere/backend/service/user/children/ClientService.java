package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.PTServiceRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PTServiceRepository ptServiceRepository;

    public Optional<Client> findClientByEmail(String email) {
        return clientRepository.findById(email); // Return user if found, otherwise null
    }

    public void updatePreferences(Client client, int number, String time) {
        client.setPreferredTime(time);
        client.setWorkoutsPerWeek(number);
        clientRepository.save(client);
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public void buyPTService(Client client, Long serviceId) {
        Optional<PTService> ptService = client.getPtServices().stream().filter(s -> s.getId().equals(serviceId)).findFirst();
        if (ptService.isPresent()) {

            throw new IllegalArgumentException("Client already has this service");
        }
        Optional<PTService> ptServiceOptional = ptServiceRepository.findById(serviceId);

        if (ptServiceOptional.isEmpty()) {
            throw new IllegalArgumentException("Service not found");
        }
        PTService ptService1 = ptServiceOptional.get();
        client.getPtServices().add(ptService1);
        clientRepository.save(client);

    }

}
