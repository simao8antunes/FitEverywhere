package pt.fe.up.fiteverywhere.backend.service.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

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

}
