import groovy.util.logging.Slf4j;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class AppTest {
    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testNewUserLogin() throws InterruptedException {
        String payload = """
                {
                    "id": "2",
                    "browser": "brave",
                    "platform": "windows",
                    "device": "desktop",
                    "ip": "85.36.0.4"
                }
                """;
        driver.get("http://localhost:3000");
        String url = "http://localhost:7070/users/1";

        // Make PUT request with Rest Assured
        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .put(url)
                .then()
                .statusCode(200);

//        // Navigate to the React app with Selenium
        // Replace with the React app's URL
//
        // Locate the element that displays the data and validate it
        WebElement displayElement = driver.findElement(By.id("phoneEvents")); // Replace with the appropriate CSS selector or ID
        Thread.sleep(5000);
        var isElementPresent = displayElement.findElements(By.xpath("//li"));
        assertEquals(0, isElementPresent.size());
    }

    @Test
    public void testExistingUserLoginTriggeringAllAlerts() throws InterruptedException {
        String payload = """
                {
                    "id": "2",
                    "browser": "chrome",
                    "platform": "mac",
                    "device": "desktop",
                    "ip": "85.36.0.5"
                }
                """;
        driver.get("http://localhost:3000");
        String url = "http://localhost:7070/users/1";

        // Make PUT request with Rest Assured
        given()
                .contentType("application/json")
                .body(payload)
                .when()
                .put(url)
                .then()
                .statusCode(200);

//        // Navigate to the React app with Selenium
        // Replace with the React app's URL
//
        // Locate the element that displays the data and validate it
        WebElement displayElement = driver.findElement(By.id("phoneEvents")); // Replace with the appropriate CSS selector or ID
        Thread.sleep(5000);
        var isElementPresent = displayElement.findElements(By.xpath("//li"));
        assertTrue(isElementPresent.size() > 0);
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
