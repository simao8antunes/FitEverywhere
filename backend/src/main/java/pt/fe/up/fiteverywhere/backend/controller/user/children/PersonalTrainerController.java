package pt.fe.up.fiteverywhere.backend.controller.user.children;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.fe.up.fiteverywhere.backend.entity.PTService;
import pt.fe.up.fiteverywhere.backend.service.user.children.PersonalTrainerService;

@RestController
@RequestMapping("/personal-trainer")
public class PersonalTrainerController {

    @Autowired
    private PersonalTrainerService personalTrainerService;

    @PostMapping("/add-service")
    public ResponseEntity<String> addService(@RequestBody PTService serviceDTO,
                                             @AuthenticationPrincipal OAuth2User principal) {
        // Get the trainer's email from the logged-in user
        String trainerEmail = principal.getAttribute("email");

        // Map DTO to PTService entity
        PTService newService = new PTService();
        newService.setId(serviceDTO.getId() == 0 ? null : serviceDTO.getId());
        newService.setName(serviceDTO.getName());
        newService.setDescription(serviceDTO.getDescription());
        newService.setPrice(serviceDTO.getPrice());
        newService.setDuration(serviceDTO.getDuration());
        newService.setType(serviceDTO.getType());

        try {
            PTService createdService = personalTrainerService.addPTService(trainerEmail, newService);
            return ResponseEntity.ok("Service added successfully with id: " + createdService.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
