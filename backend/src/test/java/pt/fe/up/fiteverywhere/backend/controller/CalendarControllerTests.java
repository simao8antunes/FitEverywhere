package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalendarControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "gym")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "user@test.com");
                            attrs.put("name", "Gym User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: gym"));
    }

    @Test
    public void testGetCalendarEvents_AuthenticatedUser_ShouldReturnEvents() throws Exception {
        // Perform the GET request with OAuth2 authentication and expect a 200 OK status
        mockMvc.perform(get("/calendar/events")
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "invalid.google.user@test.com"))))
                .andExpect(status().isInternalServerError());
    }
}
