package pt.fe.up.fiteverywhere.backend.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

public class US11Steps {

    private WebDriver driver;

    private WebDriverWait wait;

    public US11Steps() {
        // Initialize WebDriver
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        driver.manage().window().maximize();
        driver.get("http://localhost/login");

    }

    @Given("the client is on the search page")
    public void theClientIsOnTheDashboardPage() {
        wait.until(driver -> {
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);
            return currentUrl.equals("http://localhost/");
        });
    }

    @When("the client clicks an event")
    public void theClientClicksAnEvent() {

    }

    @Then("a list of gyms near the travel destination should be displayed")
    public void aListOfGymsNearTheTravelDestinationShouldBeDisplayed() {
    }

    @Given("the client has searched for gyms near a travel destination")
    public void theClientHasSearchedForGymsNearATravelDestination() {
    }

    @When("the gyms are displayed")
    public void theGymsAreDisplayed() {
    }

    @Then("the gyms should be marked on a map with location markers")
    public void theGymsShouldBeMarkedOnAMapWithLocationMarkers() {
    }
}
