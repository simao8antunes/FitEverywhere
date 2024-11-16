package pt.fe.up.fiteverywhere.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import pt.fe.up.fiteverywhere.backend.controller.AuthController;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.service.UserService;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_UserAlreadyExists() throws Exception {
        User user = new User("John Doe", "johndoe@gmail.com", "mockPassword");
        when(userService.isUserExists(user.getUsername(), user.getEmail())).thenReturn(true);

        mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("User already exists"));

        verify(userService, never()).registerUser(anyString(), anyString(), anyString());

    }

    @Test
    public void testRegisterUser_LoginSuccessful() throws Exception {
        User user = new User("John Doe", "johndoe@gmail.com", "mockPassword");
        when(userService.isUserExists(user.getUsername(), user.getEmail())).thenReturn(false);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(userService).registerUser(user.getUsername(), user.getEmail(), user.getPassword());
    }

    @Test
    public void testLoginUser_Successful() throws Exception {
        User user = new User("John Doe", "johndoe@gmail.com", "mockPassword");
        when(userService.loginUser("John Doe", "mockPassword")).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        User user = new User("John Doe", "johndoe@gmail.com", "mockPassword");
        when(userService.loginUser("John Doe", "mockPassword")).thenReturn(null);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid credentials"));
    }

    @Test
    public void testLogoutUser() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        mockMvc.perform(get("/auth/logout")
                .requestAttr("mockRequest", request)
                .requestAttr("mockResponse", response))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));

    }

    @Test
    public void testRoot() throws Exception {
        mockMvc.perform(get("/auth/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Welcome to the FitEverywhere API"));
    }

    /*@Test
    public void testGetUserInfo() throws Exception {
        OAuth2User principal = mock(OAuth2User.class);
        when(principal.getAttribute("email")).thenReturn("johndoe@example.com");
        when(principal.getAttribute("name")).thenReturn("John Doe");

        User user = new User("John Doe", "johndoe@example.com", "mockPassword");
        when(userService.findOrRegisterOAuthUser("John Doe", "johndoe@example.com")).thenReturn(user);

        Authentication authentication = new TestingAuthenticationToken(principal, null);

        mockMvc.perform(get("/auth/login/success")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.user.username").value("John Doe"))
                .andExpect(jsonPath("$.user.email").value("johndoe@example.com"));

        // Verify service interaction
        verify(userService, times(1)).findOrRegisterOAuthUser("John Doe", "johndoe@example.com");

        ERROR: jakarta.servlet.ServletException: Request processing failed: java.lang.IllegalStateException: No primary or single unique constructor found for interface org.springframework.security.oauth2.core.user.OAuth2User
    }*/
}
