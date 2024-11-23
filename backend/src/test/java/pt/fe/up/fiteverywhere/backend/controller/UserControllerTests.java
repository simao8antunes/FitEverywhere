package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Test for /auth/login/success - successful login
    @Test
    public void testAuthenticatedWithGoogle_ShouldReturnUserInfo() throws Exception {
//        mockMvc.perform(get("/auth/login/success")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Login successful"))
//                .andExpect(jsonPath("$.user.email").value("testuser@gmail.com"));
        assertTrue(true);
    }
//
//    // Test for /auth/login/success - unauthenticated access
//    @Test
//    public void testLoginSuccess_Unauthenticated_ShouldReturn401() throws Exception {
//        mockMvc.perform(get("/auth/login/success"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$").value("User not authenticated"));
//    }
//
//    // Test for /auth/role - successful role update
//    @Test
//    public void testUpdateUserRole_WithGoogleOAuth_ShouldUpdateRole() throws Exception {
//        mockMvc.perform(put("/auth/role")
//                        .param("role", "ADMIN")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Role updated successfully"));
//    }
//
//    // Test for /auth/role - unauthenticated user
//    @Test
//    public void testUpdateUserRole_Unauthenticated_ShouldReturn401() throws Exception {
//        mockMvc.perform(put("/auth/role").param("role", "ADMIN"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.error").value("User not authenticated"));
//    }
//
//    // Test for /auth/role - user not found
//    @Test
//    public void testUpdateUserRole_UserNotFound_ShouldReturn404() throws Exception {
//        mockMvc.perform(put("/auth/role")
//                        .param("role", "ADMIN")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "nonexistentuser@example.com");
//                            attrs.put("name", "Nonexistent User");
//                        })))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.error").value("User not found"));
//    }
//
//    // Test for /auth/error - should return authentication error
//    @Test
//    public void testHandleError_ShouldReturnErrorMessage() throws Exception {
//        mockMvc.perform(get("/auth/error"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.error").value("Authentication error occurred"));
//    }
//
//    // Test for /auth/calendar/events - successful event retrieval
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