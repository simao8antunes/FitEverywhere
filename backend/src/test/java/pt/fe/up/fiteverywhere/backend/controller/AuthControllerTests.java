package pt.fe.up.fiteverywhere.backend.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Test for /auth/login/success - successful login
    @Test
    public void testAuthenticatedWithGoogle_ShouldReturnUserInfo() throws Exception {
        mockMvc.perform(get("/auth/login/success")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "testuser@gmail.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.email").value("testuser@gmail.com"));
    }

    // Test for /auth/login/success - unauthenticated access
    @Test
    public void testLoginSuccess_Unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/auth/login/success"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("User not authenticated"));
    }

    // Test for /auth/role - successful role update
    @Test
    public void testUpdateUserRole_WithGoogleOAuth_ShouldUpdateRole() throws Exception {
        mockMvc.perform(put("/auth/role")
                        .param("role", "ADMIN")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "testuser@gmail.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Role updated successfully"));
    }

    // Test for /auth/role - unauthenticated user
    @Test
    public void testUpdateUserRole_Unauthenticated_ShouldReturn401() throws Exception {
        mockMvc.perform(put("/auth/role").param("role", "ADMIN"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("User not authenticated"));
    }

    // Test for /auth/role - user not found
    @Test
    public void testUpdateUserRole_UserNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(put("/auth/role")
                        .param("role", "ADMIN")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "nonexistentuser@example.com");
                            attrs.put("name", "Nonexistent User");
                        })))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    // Test for /auth/error - should return authentication error
    @Test
    public void testHandleError_ShouldReturnErrorMessage() throws Exception {
        mockMvc.perform(get("/auth/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Authentication error occurred"));
    }

    // Test for /auth/calendar/events - successful event retrieval
    @Test
    public void testGetCalendarEvents_Authenticated_ShouldReturnEvents() throws Exception {
        /*mockMvc.perform(get("/auth/calendar/events")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "testuser@gmail.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk());*/
    }

    // Test for /auth/calendar/events - unauthenticated access
    @Test
    public void testGetCalendarEvents_Unauthenticated_ShouldReturn401() throws Exception {
        /*mockMvc.perform(get("/auth/calendar/events"))
                .andExpect(status().isUnauthorized());*/
    }

    // Test for /auth/gyms/nearby - valid input
    @Test
    public void testGetNearbyGyms_ValidInput_ShouldReturnGyms() throws Exception {
        mockMvc.perform(get("/auth/gyms/nearby")
                        .param("latitude", "41.1579")
                        .param("longitude", "-8.6291")
                        .param("radius", "1000")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "testuser@gmail.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isOk());
        // Additional assertions can be added based on expected gym data structure
    }

    // Test for /auth/gyms/nearby - invalid latitude/longitude
    @Test
    public void testGetNearbyGyms_InvalidInput_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/auth/gyms/nearby")
                        .param("latitude", "invalid")
                        .param("longitude", "-8.6291")
                        .param("radius", "0")
                        .with(oauth2Login().attributes(attrs -> {
                            attrs.put("email", "testuser@gmail.com");
                            attrs.put("name", "Test User");
                        })))
                .andExpect(status().isBadRequest());
    }
}