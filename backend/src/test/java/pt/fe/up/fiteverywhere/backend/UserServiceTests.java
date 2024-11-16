package pt.fe.up.fiteverywhere.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.fe.up.fiteverywhere.backend.entity.User;
import pt.fe.up.fiteverywhere.backend.repository.UserRepository;
import pt.fe.up.fiteverywhere.backend.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindUserByEmail() {
        User mockUser = new User("John Doe", "johndoe@gmail.com", "mockPassword");
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
        String password = "mockPassword";

        userService.registerUser(username, email, password);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserCorrectData() {

        String username = "testName";
        String email = "johndoe@gmail.com";
        String password = "mockPassword";

        userService.registerUser(username, email, password);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals(password, savedUser.getPassword());
    }

    @Test
    void testLoginUserCorrectCreds() {
        String username = "testname";
        String email = "johndoe@gmail.com";
        String password = "mockPassword";

        User mockUser = new User(username, email, password);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        assertEquals(userService.loginUser(username, password), mockUser);
    }

    @Test
    void testLoginUserIncorrectPassword() {
        String username = "testname";
        String email = "johndoe@gmail.com";
        String password = "mockPassword";

        User mockUser = new User(username, email, password);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        assertNull(userService.loginUser(username, "wrongpassword"));
    }

    @Test
    void testLoginUserIncorrectUsername() {
        String wrongUser = "wrongUsername";
        String password = "mockPassword";

        when(userRepository.findByUsername(wrongUser)).thenReturn(null);

        assertNull(userService.loginUser(wrongUser, password));
    }

    @Test
    void testUserExists() {
        String username = "John Doe";
        String email = "johndoe@gmail.com";
        String password = "johndoePassword";

        String fakeUser = "Test";
        String fakeEmail = "testemail@gmail.com";

        User correctUser = new User(username, email, password);

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
        User user = new User("John Doe", "johndoe@example.com", "mockPassword");

        userService.save(user);

        verify(userRepository).save(user);
    }

    @Test
    public void testfindOrRegisterUser() {

        // User exists

        String username = "John Doe";
        String email = "johndoe@gmail.com";
        String password = "mockPassword";
        String wrongEmail = "wrongemail@gmail.com";

        User user = new User(username, email, password);
        User newUser = new User("Jane Doe", wrongEmail, "otherPassword");

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

        User user = new User("John Doe", "johndoe@gmail.com", "mockPassword");
        String role = "client";

        userService.updateUserRole(user, role);

        assertEquals(user.getRole(), role);
        verify(userRepository).save(user);
    }
}