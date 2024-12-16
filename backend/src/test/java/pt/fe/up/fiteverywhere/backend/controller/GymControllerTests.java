package pt.fe.up.fiteverywhere.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.fe.up.fiteverywhere.backend.entity.Gym;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GymControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "gym")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "gym.manager@test.com");
                            attrs.put("name", "Gym User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: gym"));
        mockMvc.perform(post("/auth/signup")
                        .param("role", "gym")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "another.user@test.com");
                            attrs.put("name", "Another User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: gym"));
    }

    // Test for creating a gym with a valid user
    @Test
    @Order(1)
    public void testCreateGym_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        String gymName = "Test Gym";
        Long gymId = 1L;

        mockMvc.perform(post("/gym/1")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "gym.manager@test.com")))
                        .param("name", gymName)  // Add `name` as a request parameter
                        .param("id", String.valueOf(gymId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully created gym with id: 1")); // Adjust ID as needed
    }

    // Test for creating a gym with an invalid user
    @Test
    @Order(2)
    public void testCreateGym_InvalidUser_ShouldReturnNotFound() throws Exception {
        String gymName = "Invalid Gym";
        Long gymId = 2L;

        mockMvc.perform(post("/gym/1")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "unknown.user@test.com")))
                        .param("name", gymName)  // Add `name` as a request parameter
                        .param("id", String.valueOf(gymId)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    // Test for updating gym details with a valid user and gym
    @Test
    @Order(3)
    public void testUpdateGym_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        Gym gym = new Gym();
        gym.setId(1L); // Use a valid gym ID
        gym.setName("Updated Gym Name");
        gym.setDailyFee(15.0);
        gym.setWeeklyMembership(100.0);
        String gymPath = String.format("/gym/%s", gym.getId());
        mockMvc.perform(put(gymPath)
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "gym.manager@test.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Gym details updated successfully!"));
    }

    // Test for updating gym details with an unauthorized user
    @Test
    @Order(4)
    public void testUpdateGym_NoGym_ShouldReturnNotFound() throws Exception {
        Gym gym = new Gym();
        gym.setId(1L); // Use a valid gym ID
        gym.setName("Unauthorized Gym Update");
        gym.setDailyFee(25.0);
        gym.setWeeklyMembership(100.0);
        String gymPath = String.format("/gym/%s", gym.getId());
        mockMvc.perform(put(gymPath)
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "another.user@test.com"); // User without permission
                        }))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Gym not found or not authorized to update."));
    }

    @Test
    @Order(4)
    public void testUpdateGym_UnauthorizedUser_ShouldReturnUnauthenticated() throws Exception {
        Gym gym = new Gym();
        gym.setId(1L); // Use a valid gym ID
        gym.setName("Unauthorized Gym Update");
        gym.setDailyFee(25.0);
        gym.setWeeklyMembership(100.0);
        String gymPath = String.format("/gym/%s", gym.getId());
        mockMvc.perform(put(gymPath)
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "invalid.user@test.com"); // User without permission
                        })).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gym)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User not found for the provided email."));
    }

    // Test for getting gym details with a valid user
    @Test
    @Order(5)
    public void testGetGymDetails_AuthenticatedUser_ShouldReturnGymInfo() throws Exception {
        mockMvc.perform(get("/gym/1")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "gym.manager@test.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.dailyFee").exists())
                .andExpect(jsonPath("$.weeklyMembership").exists());
    }

    // Test for getting gym details with a user without gyms
    @Test
    @Order(5)
    public void testGetGymDetails_NoGymForUser_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/gym/1")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "another.user@test.com"); // User without gyms
                        })))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No gym found for the current user."));
    }

    @Test
    @Order(5)
    public void testGetGymDetails_UnauthorizedUser_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/gym/1")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "invalid.user@test.com"); // User without permission
                        })))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User not found for the provided email."));
    }


    @Test
    @Order(6)
    public void testGetAllGyms_AuthenticatedUser_ShouldReturnGymList() throws Exception {
        mockMvc.perform(get("/gym/all")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "gym.manager@test.com")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}