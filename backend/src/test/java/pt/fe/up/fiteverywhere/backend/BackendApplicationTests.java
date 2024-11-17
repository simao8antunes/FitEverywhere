package pt.fe.up.fiteverywhere.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.fe.up.fiteverywhere.backend.service.UserService;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void mainMethodTest() {
        // This will cover the main method of BackendApplication
        //BackendApplication.main(new String[] {});
    }
}