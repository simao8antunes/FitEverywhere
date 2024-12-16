package pt.fe.up.fiteverywhere.backend.controller;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.fe.up.fiteverywhere.backend.entity.user.children.PersonalTrainer;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public void setUp() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "gym")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "user@test.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: gym"));
    }

    // Test for /auth/login/success - successful login
    @Test
    @Order(1)
    public void testAuthenticatedWithGoogle_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/auth/login/success")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "user@test.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.email").value("user@test.com"));
    }

    // Test for /auth/login/success - user not found login
    @Test
    public void testAuthenticatedWithGoogle_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/auth/login/success")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "nouser@gmail.com");
                            attrs.put("name", "Not Internal User");
                        })))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("User not found"));
    }

    // Test for /auth/login/success - unauthenticated access
    @Test
    public void testLoginSuccess_Unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/auth/login/success"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("User not authenticated"));
    }

    // Test for /auth/login/success - successful login
    @Test
    public void testAuthenticatedWithGoogle_ShouldReturnInternalError() throws Exception {
        mockMvc.perform(get("/auth/login/success")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", 5);
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$").value("Error occurred while fetching user info"));
    }

    // Test for /auth/error - should return authentication error
    @Test
    public void testHandleError_ShouldReturnErrorMessage() throws Exception {
        mockMvc.perform(get("/auth/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Authentication error occurred"));
    }

    // Test for /auth/signup - successful signup
    @Test
    @Order(1)
    public void testCreateClient_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "client")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "client@gmail.com");
                            attrs.put("name", "Client User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: client"));

    }

    // Test for /auth/signup - successful signup
    @Test
    @Order(1)
    public void testCreateGymManager_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/auth/signup")
                .param("role", "gym")
                .with(oauth2Login().attributes(attrs -> {
                    attrs.put("email", "gym@gmail.com");
                    attrs.put("name", "Gym User");
                })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: gym"));
    }

    // Test for /auth/signup - successful signup
    @Test
    @Order(1)
    public void testCreatePersonalTrainer_AuthenticatedUser_ShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/auth/signup")
                .param("role", "pt")
                .with(oauth2Login().attributes(attrs -> {
                    attrs.put("email", "pt@gmail.com");
                    attrs.put("name", "PT User");
                })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Created user with role: pt"));
    }

    // Test for /auth/signup - successful signup
    @Test
    public void testCreateUser_AuthenticatedUser_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "random")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "pt@gmail.com");
                            attrs.put("name", "PT User");
                        })))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid role"));
    }

    @Test
    public void testCreateUser_AuthenticatedUser_ShouldReturn401() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .param("role", "client"))

                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User not authenticated"));
    }

    @Test
    @Order(2)
    public void testUpdateUserDetails_UserNotFound_ShouldReturn401() throws Exception {
        String userJson = """
        {
            "description": "Updated description",
            "linkedGym": null
        }
        """;

        mockMvc.perform(put("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "nouser@test.com"))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User not found for the provided email."));
    }

    @Test
    @Order(3)
    public void testUpdateUserDetails_Success_ShouldReturn200() throws Exception {
        // Assuming a Personal Trainer user with email 'pt@gmail.com' exists in the system
        String userJson = """
        {
            "description": "Updated description",
            "linkedGym": null
        }
        """;
        mockMvc.perform(put("/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .with(oauth2Login().attributes(attrs -> attrs.put("email", "pt@gmail.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User details updated successfully!"));
    }

}