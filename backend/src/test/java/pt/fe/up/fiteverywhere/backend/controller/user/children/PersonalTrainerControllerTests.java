package pt.fe.up.fiteverywhere.backend.controller.user.children;


import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.fe.up.fiteverywhere.backend.entity.PTService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonalTrainerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "pt")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "pt@test.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: pt"));
    }

    @Test
    @Order(1)
    public void testAddService_Success_ShouldReturn200() throws Exception {
        // Create a valid PTService DTO
        PTService serviceDTO = new PTService();
        serviceDTO.setId(0L);
        serviceDTO.setName("Yoga Class");
        serviceDTO.setDescription("A relaxing yoga session");
        serviceDTO.setPrice(50.0);
        serviceDTO.setDuration(60);
        serviceDTO.setType("Fitness");

        // Convert DTO to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String serviceJson = objectMapper.writeValueAsString(serviceDTO);

        // Perform the request with valid data
        mockMvc.perform(post("/personal-trainer/add-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceJson)
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "pt@test.com"))))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    public void testAddService_Unauthorized_ShouldReturn401() throws Exception {
        // Create a valid PTService DTO
        PTService serviceDTO = new PTService();
        serviceDTO.setName("Yoga Class");
        serviceDTO.setDescription("A relaxing yoga session");
        serviceDTO.setPrice(50.0);
        serviceDTO.setDuration(60);
        serviceDTO.setType("Fitness");

        ObjectMapper objectMapper = new ObjectMapper();
        String serviceJson = objectMapper.writeValueAsString(serviceDTO);

        // Perform the request without authentication
        mockMvc.perform(post("/personal-trainer/add-service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(serviceJson)
                .with(oauth2Login().attributes(attrs -> attrs.put("email", "invalid@test.com"))))
                .andExpect(status().isUnauthorized());
    }
}
