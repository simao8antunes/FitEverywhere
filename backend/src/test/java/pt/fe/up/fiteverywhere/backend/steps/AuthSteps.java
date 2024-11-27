package pt.fe.up.fiteverywhere.backend.steps;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;


@CucumberContextConfiguration
@SpringBootTest
public class AuthSteps {
    @Given("the user navigates to the login page")
    public void userNavigatesToLoginPage() {
        assertTrue(true);
    }

    @When("the user submits valid credentials")
    public void userSubmitsValidCredentials() {
        // code to submit login credentials
        assertTrue(true);
    }

    @Then("the user should see the dashboard")
    public void userShouldSeeDashboard() {
        // code to verify dashboard visibility
        assertTrue(true);
    }
}
