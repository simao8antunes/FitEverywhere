package pt.fe.up.fiteverywhere.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindUserByEmail() {
        User mockUser = new User("John Doe", "johndoe@gmail.com");
        when(userRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(mockUser));

        User result = userService.findUserByEmail("johndoe@gmail.com");

        User result2 = userService.findUserByEmail("janedoe@gmail.com");

        assertNotNull(result);
        assertEquals("John Doe", result.getUsername());
        assertNull(result2);
    }

    @Test
    void testRegisterUser() {

        String username = "testName";
        String email = "johndoe@gmail.com";

        userService.registerUser(username, email);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserCorrectData() {

        String username = "testName";
        String email = "johndoe@gmail.com";

        userService.registerUser(username, email);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
    }

    @Test
    void testLoginUserCorrectCreds() {
        String username = "testname";
        String email = "johndoe@gmail.com";

        User mockUser = new User(username, email);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        assertEquals(userService.loginUser(username), mockUser);
    }

    @Test
    void testLoginUserIncorrectUsername() {
        String wrongUser = "wrongUsername";

        when(userRepository.findByUsername(wrongUser)).thenReturn(null);

        assertNull(userService.loginUser(wrongUser));
    }

    @Test
    void testUserExists() {
        String username = "John Doe";
        String email = "johndoe@gmail.com";

        String fakeUser = "Test";
        String fakeEmail = "testemail@gmail.com";

        User correctUser = new User(username, email);

        when(userRepository.findByUsername(username)).thenReturn(correctUser);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(correctUser));

        when(userRepository.findByUsername(fakeUser)).thenReturn(null);
        when(userRepository.findByEmail(fakeEmail)).thenReturn(null);

        assertTrue(userService.isUserExists(username, email));
        assertTrue(userService.isUserExists(username, fakeEmail));
        assertTrue(userService.isUserExists(fakeUser, email));
        assertFalse(userService.isUserExists(fakeUser, fakeEmail));

    }

    @Test
    public void testSaveUser() {
        User user = new User("John Doe", "johndoe@example.com");

        userService.save(user);

        verify(userRepository).save(user);
    }

    @Test
    public void testfindOrRegisterUser() {

        // User exists

        String username = "John Doe";
        String email = "johndoe@gmail.com";
        String wrongEmail = "wrongemail@gmail.com";

        User user = new User(username, email);
        User newUser = new User("Jane Doe", wrongEmail);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        assertEquals(userService.findOrRegisterOAuthUser(username, email), user);

        // User doesn't exist

        when(userRepository.findByEmail(wrongEmail)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.findOrRegisterOAuthUser(username, wrongEmail);
        
        assertEquals(newUser, result);
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testUpdateUserRole() {
        // Roles: gym, client

        User user = new User("John Doe", "johndoe@gmail.com");
        String role = "client";

        userService.updateUserRole(user, role);

        assertEquals(user.getRole(), role);
        verify(userRepository).save(user);
    }
}