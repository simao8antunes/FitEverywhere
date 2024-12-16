package pt.fe.up.fiteverywhere.backend.controller.user.children;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.fe.up.fiteverywhere.backend.entity.Gym;
import pt.fe.up.fiteverywhere.backend.entity.user.children.GymManager;
import pt.fe.up.fiteverywhere.backend.repository.GymRepository;
import pt.fe.up.fiteverywhere.backend.repository.user.children.GymManagerRepository;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GymManagerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GymManagerRepository gymManagerRepository;

    @Autowired
    private GymRepository gymRepository;

    @BeforeAll
    public void setUp() {
        // Create gyms
        Gym gym1 = new Gym(5L, "Gym One");
        gym1.setDailyFee(10.0);
        gym1.setWeeklyMembership(50.0);
        gymRepository.save(gym1);

        // Create gym manager and link gyms
        GymManager gymManager = new GymManager("testGymManager", "gym.manager@example.com");
        Set<Gym> linkedGyms = new HashSet<>();
        linkedGyms.add(gym1);
        gymManager.setLinkedGyms(linkedGyms);
        gymManagerRepository.save(gymManager);
    }

    @Test
    @Order(1)
    public void testListGyms_AuthenticatedUserWithGyms_ShouldReturnGymList() throws Exception {
        mockMvc.perform(get("/gym-manager/list-gyms")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "gym.manager@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gyms").isArray())
                .andExpect(jsonPath("$.gyms[0].id").value(5L))
                .andExpect(jsonPath("$.gyms[0].name").value("Gym One"));
    }

    @Test
    @Order(2)
    public void testListGyms_AuthenticatedUserWithoutGyms_ShouldReturnNoContent() throws Exception {
        GymManager gymManagerWithoutGyms = new GymManager("emptyManager", "empty.manager@example.com");
        gymManagerRepository.save(gymManagerWithoutGyms);

        mockMvc.perform(get("/gym-manager/list-gyms")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "empty.manager@example.com"))))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No gyms found"));

        gymManagerRepository.delete(gymManagerWithoutGyms);
    }

    @Test
    @Order(3)
    public void testListGyms_UserNotFound_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/gym-manager/list-gyms")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "nonexistent.user@example.com"))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    @Order(4)
    public void testListGyms_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/gym-manager/list-gyms"))
                .andExpect(status().isUnauthorized());
    }
}