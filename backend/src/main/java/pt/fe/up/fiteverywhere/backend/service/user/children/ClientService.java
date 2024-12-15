package pt.fe.up.fiteverywhere.backend.service.user.children;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.PTServiceRepository;
import pt.fe.up.fiteverywhere.backend.entity.user.children.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.entity.WorkoutSuggestion;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import pt.fe.up.fiteverywhere.backend.repository.user.children.WorkoutSuggestionRepository;
import pt.fe.up.fiteverywhere.backend.repository.WorkoutSuggestionRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkoutSuggestionRepository workoutSuggestionRepository;

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

    public List<String> generateWorkoutSuggestions(Client client, Map<String, Object> calendarEvents) {
        // Extract busy times from the events
        List<Map<String, Object>> events = (List<Map<String, Object>>) calendarEvents.get("items");
        List<String> busyTimes = extractBusyTimes(events);
    
        String preferredTime = client.getPreferredTime();
        int workoutsPerWeek = client.getWorkoutsPerWeek();
    
        List<String> suggestions = new ArrayList<>();
    
        // Calculate the interval between workout days (7 days / workouts per week)
        int interval = 7 / workoutsPerWeek;
    
        // Get the current date
        LocalDateTime currentDate = LocalDateTime.now();
    
        for (int i = 0; i < workoutsPerWeek; i++) {
            int daysOffset = i * interval;
    
            // Attempt to find an available slot within a range of ±2 days
            String suggestedDayTime = findAvailableSlotWithRange(busyTimes, preferredTime, daysOffset, 2, currentDate);
            if (!"No available slots".equals(suggestedDayTime)) {
                suggestions.add(suggestedDayTime);
            }
        }
    
        return suggestions;
    }
    
    public List<WorkoutSuggestion> saveWorkoutSuggestions(Client client, List<WorkoutSuggestion> workoutSuggestions) {
        List<WorkoutSuggestion> savedSuggestions = new ArrayList<>();

        for (WorkoutSuggestion workoutSuggestion : workoutSuggestions) {
            // Create a new WorkoutSuggestion object
            WorkoutSuggestion newWorkoutSuggestion = new WorkoutSuggestion();
            newWorkoutSuggestion.setClient(client);
            newWorkoutSuggestion.setTime(workoutSuggestion.getTime());
            newWorkoutSuggestion.setGym(workoutSuggestion.getGym());

            // Save the workout suggestion to the repository
            workoutSuggestionRepository.save(newWorkoutSuggestion);
            savedSuggestions.add(workoutSuggestion);
        }

        return savedSuggestions;
    }



    public String findAvailableSlotWithRange(List<String> busyTimes, String preferredTime, int daysOffset, int range, LocalDateTime currentDate) {
        // Map preferred time to time range (using 24-hour format)
        LocalDateTime preferredDateTimeStart = getPreferredTimeRange(preferredTime).get(0);
        LocalDateTime preferredDateTimeEnd = getPreferredTimeRange(preferredTime).get(1);
    
        for (int offset = -range; offset <= range; offset++) {
            LocalDateTime currentTimeStart = preferredDateTimeStart.plusDays(daysOffset + offset);
            LocalDateTime currentTimeEnd = preferredDateTimeEnd.plusDays(daysOffset + offset);
    
            // Skip past dates
            if (currentTimeStart.isBefore(currentDate)) {
                continue;
            }
    
            String currentSlotStart = currentTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String currentSlotEnd = currentTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    
            boolean isConflict = false;
            for (String busyTime : busyTimes) {
                String[] timeRange = busyTime.split(" ");
                String busyDate = timeRange[0];
                String busyRange = timeRange[1];
                String[] busyStartEnd = busyRange.split("-");
    
                LocalDateTime busyStart = LocalDateTime.parse(busyDate + " " + busyStartEnd[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime busyEnd = LocalDateTime.parse(busyDate + " " + busyStartEnd[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    
                // Check if the current slot conflicts with the busy time
                if (!(currentTimeStart.isAfter(busyEnd) || currentTimeEnd.isBefore(busyStart))) {
                    isConflict = true;
                    break;
                }
            }
    
            if (!isConflict) {
                String newBusyTime = currentSlotStart.split(" ")[0] + " " + currentSlotStart.split(" ")[1] + "-" + currentSlotEnd.split(" ")[1];
                busyTimes.add(newBusyTime); // Add the new slot to the busy times
                return currentSlotStart + "/" + currentSlotEnd; // Return the first available slot
            }
        }
    
        // Fallback: No slot found
        return "No available slots";
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

    private List<String> extractBusyTimes(List<Map<String, Object>> events) {
        List<String> busyTimes = new ArrayList<>();
        if (events == null) {
            return busyTimes; // Return an empty list if no events are present
        }

        for (Map<String, Object> event : events) {
            try {
                String start = (String) ((Map<String, Object>) event.get("start")).getOrDefault("dateTime", (String) ((Map<String, Object>) event.get("start")).get("date"));
                String end = (String) ((Map<String, Object>) event.get("end")).getOrDefault("dateTime", (String) ((Map<String, Object>) event.get("end")).get("date"));

                busyTimes.add(formatBusyTime(start, end));
            } catch (Exception e) {
                // Log and skip malformed events
                System.err.println("Malformed event: " + event);
            }
        }

        return busyTimes;
    }


    private String formatBusyTime(String start, String end) {
        // Extract date and time components (assume ISO 8601 format)
        String date = start.substring(0, 10); // Extract "YYYY-MM-DD"
        String startTime = start.substring(11, 16); // Extract "HH:MM"
        String endTime = end.substring(11, 16); // Extract "HH:MM"

        return date + " " + startTime + "-" + endTime;
    }

    public String findAvailableSlot(List<String> busyTimes, String preferredTime) {
        // Map preferred time to time range (using 24-hour format)
        LocalDateTime preferredDateTimeStart = getPreferredTimeRange(preferredTime).get(0);
        LocalDateTime preferredDateTimeEnd = getPreferredTimeRange(preferredTime).get(1);

        // Loop through next 7 days to find a slot
        for (int i = 0; i < 7; i++) { // Check the next 7 days
            LocalDateTime currentTimeStart = preferredDateTimeStart.plusDays(i);
            LocalDateTime currentTimeEnd = preferredDateTimeEnd.plusDays(i);

            String currentSlotStart = currentTimeStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            String currentSlotEnd = currentTimeEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            boolean isConflict = false;
            for (String busyTime : busyTimes) {
                String[] timeRange = busyTime.split(" ");
                String busyDate = timeRange[0];
                String busyRange = timeRange[1];
                String[] busyStartEnd = busyRange.split("-");

                LocalDateTime busyStart = LocalDateTime.parse(busyDate + " " + busyStartEnd[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                LocalDateTime busyEnd = LocalDateTime.parse(busyDate + " " + busyStartEnd[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                // Check if the current slot conflicts with the busy time
                if (!(currentTimeStart.isAfter(busyEnd) || currentTimeEnd.isBefore(busyStart))) {
                    isConflict = true;
                    break;
                }
            }

            if (!isConflict) {
                String newBusyTime = currentSlotStart.split(" ")[0] + " " + currentSlotStart.split(" ")[1] + "-" + currentSlotEnd.split(" ")[1];
                busyTimes.add(newBusyTime); // Add the new slot to the busy times
                return currentSlotStart + "/" + currentSlotEnd; // Return the first available slot
            }
        }

        // Fallback: No slot found
        return "No available slots";
    }



    private List<LocalDateTime> getPreferredTimeRange(String preferredTime) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay(); // Get today's start of day

        // Map preferred time to time range
        switch (preferredTime.toLowerCase()) {
            case "morning":
                return List.of(startOfDay.plusHours(7), startOfDay.plusHours(11));
            case "afternoon":
                return List.of(startOfDay.plusHours(12), startOfDay.plusHours(19));
            case "evening":
                return List.of(startOfDay.plusHours(19), startOfDay.plusHours(23));
            default:
                throw new IllegalArgumentException("Invalid preferred time");
        }
    }
}
