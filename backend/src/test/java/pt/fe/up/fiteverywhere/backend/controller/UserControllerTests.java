package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.*;
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


    // Test for /auth/calendar/events - successful event retrieval
//    @Test
//    public void testGetCalendarEvents_Authenticated_ShouldReturnEvents() throws Exception {
//        /*mockMvc.perform(get("/auth/calendar/events")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isOk());*/
//    }
//
//    // Test for /auth/calendar/events - unauthenticated access
//    @Test
//    public void testGetCalendarEvents_Unauthenticated_ShouldReturn401() throws Exception {
//        /*mockMvc.perform(get("/auth/calendar/events"))
//                .andExpect(status().isUnauthorized());*/
//    }
//
//    @Test
//    public void testSaveWorkoutPreferences_AuthenticatedUser_ShouldSavePreferences() throws Exception {
//        // Assume the user with email "testuser@gmail.com" exists in the database
//        // and has "workoutsPerWeek" and "preferredTime" set.
//
//        mockMvc.perform(post("/auth/workout-preferences")
//                        .param("number", "3")
//                        .param("time", "Morning")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Preferences saved successfully"));
//    }
//
//    // Test for /workout-preferences - user not found in database
//    @Test
//    public void testSaveWorkoutPreferences_UserNotFound_ShouldReturn404() throws Exception {
//        mockMvc.perform(post("/auth/workout-preferences")
//                        .param("number", "3")
//                        .param("time", "Morning")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "nonexistentuser@example.com");
//                            attrs.put("name", "Nonexistent User");
//                        })))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("User not found"));
//    }
//
//    // Test for /workout-preferences - authenticated user with valid data
//    @Test
//    public void testGetWorkoutPreferences_AuthenticatedUser_ShouldReturnPreferences() throws Exception {
//        mockMvc.perform(get("/auth/workout-preferences")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.workoutsPerWeek").exists())
//                .andExpect(jsonPath("$.preferredTime").exists());
//    }
//
//    // Test for /workout-preferences - authenticated user not found in database
//    @Test
//    public void testGetWorkoutPreferences_UserNotFound_ShouldReturn404() throws Exception {
//        mockMvc.perform(get("/auth/workout-preferences")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "nonexistentuser@example.com");
//                            attrs.put("name", "Nonexistent User");
//                        })))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$").value("User not found"));
//    }

}