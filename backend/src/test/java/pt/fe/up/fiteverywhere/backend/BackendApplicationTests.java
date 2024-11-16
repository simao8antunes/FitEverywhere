package pt.fe.up.fiteverywhere.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.fe.up.fiteverywhere.backend.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void userServiceLoads() {
        // Verifies that the userService bean is loaded correctly
        assertThat(userService).isNotNull();
    }

    @Test
    void mainMethodTest() {
        // This will cover the main method of BackendApplication
        BackendApplication.main(new String[] {});
    }
}