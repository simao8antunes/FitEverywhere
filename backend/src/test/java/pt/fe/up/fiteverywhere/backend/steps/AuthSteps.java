package pt.fe.up.fiteverywhere.backend.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@CucumberContextConfiguration
@SpringBootTest
public class AuthSteps {

    @Given("the user navigates to the login page")
    public void userNavigatesToLoginPage() {
        // code to navigate to the login page
    }

    @When("the user submits valid credentials")
    public void userSubmitsValidCredentials() {
        // code to submit login credentials
    }

    @Then("the user should see the dashboard")
    public void userShouldSeeDashboard() {
        // code to verify dashboard visibility
        assertTrue(true);
    }
}
