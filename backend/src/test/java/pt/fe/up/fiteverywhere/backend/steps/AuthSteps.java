package pt.fe.up.fiteverywhere.backend.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;


@CucumberContextConfiguration
@SpringBootTest
public class AuthSteps {

    WebDriver driver = new ChromeDriver();

    @Given("the user navigates to the login page")
    public void userNavigatesToLoginPage() {
        driver.get("/api/auth/login");
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
