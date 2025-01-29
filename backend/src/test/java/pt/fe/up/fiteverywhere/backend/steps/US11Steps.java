package pt.fe.up.fiteverywhere.backend.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

@CucumberContextConfiguration
@SpringBootTest
public class US11Steps {

    private final WebDriver driver;

    private final WebDriverWait wait;

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
        Assertions.assertEquals("http://localhost/", driver.getCurrentUrl(), "The client is not on the search page.");
    }

    @When("the client clicks an event")
    public void theClientClicksAnEvent() {
        // Wait for the event element to be clickable
        WebElement event = wait.until(ExpectedConditions.elementToBeClickable(By.className("event")));
        event.click();
    }

    @Then("a list of gyms near the travel destination should be displayed")
    public void aListOfGymsNearTheTravelDestinationShouldBeDisplayed() {
        // Wait until gym elements are visible on the page
        wait.until(driver -> {
            List<WebElement> gyms = driver.findElements(By.className("gym"));
            System.out.println("Gym List Size: " + gyms.size());
            return !gyms.isEmpty(); // Wait until the list has more than 0 elements
        });
        // Retrieve the gym list again after the condition is satisfied
        List<WebElement> gyms = driver.findElements(By.className("gym"));

        // Assert that gyms are displayed
        Assertions.assertFalse(gyms.isEmpty(), "No gyms are displayed near the travel destination.");
    }

    @Given("the client has searched for gyms near a travel destination")
    public void theClientHasSearchedForGymsNearATravelDestination() {
        WebElement event = wait.until(ExpectedConditions.elementToBeClickable(By.className("event")));
        event.click();

    }

    @When("the gyms are displayed")
    public void theGymsAreDisplayed() {
        aListOfGymsNearTheTravelDestinationShouldBeDisplayed();
    }

    @Then("the gyms should be marked on a map with location markers")
    public void theGymsShouldBeMarkedOnAMapWithLocationMarkers() {
        wait.until(driver -> {
            List<WebElement> poiMarkers = driver.findElements(By.className("marker"));
            System.out.println("Poi Markers: " + poiMarkers.size());
            return !poiMarkers.isEmpty(); // Wait until the list has more than 0 elements
        });
        // Retrieve the gym list again after the condition is satisfied
        List<WebElement> poiMarkers = driver.findElements(By.className("marker"));

        // Assert that gyms are displayed
        Assertions.assertFalse(poiMarkers.isEmpty(), "No gyms are displayed on the map.");
    }
}
