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

    public List<String> generateWorkoutSchedule(User user, List<Map<String, Object>> calendarEvents) {
//        int workoutsPerWeek = user.getWorkoutsPerWeek();
//        String preferredTime = user.getPreferredTime();
//        List<String> schedule = new ArrayList<>();
//
//        // Example: Distribute workouts while avoiding calendar events
//        for (int i = 0; i < 7; i++) {
//            if (schedule.size() >= workoutsPerWeek) break;
//
//            String day = LocalDate.now().plusDays(i).getDayOfWeek().name();
//            boolean hasConflict = calendarEvents.stream()
//                    .anyMatch(event -> event.get("day").equals(day) && event.get("time").equals(preferredTime));
//
//            if (!hasConflict) {
//                schedule.add(day + " " + preferredTime);
//            }
//        }
//
//        return schedule;
        return List.of();
    }
}
