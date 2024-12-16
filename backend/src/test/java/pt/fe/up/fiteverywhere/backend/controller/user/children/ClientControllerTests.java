package pt.fe.up.fiteverywhere.backend.controller.user.children;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.fe.up.fiteverywhere.backend.entity.user.children.Client;
import pt.fe.up.fiteverywhere.backend.repository.user.children.ClientRepository;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeAll
    public void setUp() {
        // Create a sample client for testing
        Client client = new Client("testUser", "test.user@example.com");
        client.setWorkoutsPerWeek(3);
        client.setPreferredTime("Morning");
        clientRepository.save(client);
    }

    @AfterAll
    public void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testSaveWorkoutPreferences_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(put("/client/workout-preferences")
                        .param("number", "4")
                        .param("time", "Evening")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "test.user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Preferences saved successfully"));
    }

    @Test
    @Order(2)
    public void testSaveWorkoutPreferences_UserNotFound_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/client/workout-preferences")
                        .param("number", "3")
                        .param("time", "Afternoon")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "unknown.user@example.com"))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    @Order(3)
    public void testSaveWorkoutPreferences_InvalidInput_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(put("/client/workout-preferences")
                        .param("number", "notANumber")
                        .param("time", "")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "test.user@example.com"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    public void testSaveWorkoutPreferences_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(put("/client/workout-preferences")
                        .param("number", "3")
                        .param("time", "Morning"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(5)
    public void testSaveWorkoutPreferences_UpdatesClientPreferences() throws Exception {
        mockMvc.perform(put("/client/workout-preferences")
                        .param("number", "5")
                        .param("time", "Night")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "test.user@example.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Preferences saved successfully"));

        Optional<Client> updatedClient = clientRepository.findById("test.user@example.com");
        Assertions.assertTrue(updatedClient.isPresent());
        Assertions.assertEquals(5, updatedClient.get().getWorkoutsPerWeek());
        Assertions.assertEquals("Night", updatedClient.get().getPreferredTime());
    }
}