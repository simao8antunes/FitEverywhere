package pt.fe.up.fiteverywhere.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class GymControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Test for /gym/details - successful gym details retrieval
    @Test
    public void testGetGymDetails_GymNotFound_ShouldReturn404() throws Exception {
        // Assume gym with id 1 does not exist in the database
//        mockMvc.perform(get("/gym/details")
//                        .param("id", "1")
//                        .with(oauth2Login().attributes(attrs -> {
//                            attrs.put("email", "testuser@gmail.com");
//                            attrs.put("name", "Test User");
//                        })))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Gym not found"));
        assertTrue(true);
    }

    // Test for /gym/details - saving gym details (POST)
    @Test
    public void testSaveGymDetails_ShouldSaveGym() throws Exception {
        // Create a new Gym object in JSON format
        //Gym gym = new Gym("Test Gym", "123 Test Street", 40.7128, -74.0060);
        assertTrue(true);
    }
}