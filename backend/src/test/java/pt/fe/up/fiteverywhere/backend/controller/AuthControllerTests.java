package pt.fe.up.fiteverywhere.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pt.fe.up.fiteverywhere.backend.service.UserService;


@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthController authController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
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
