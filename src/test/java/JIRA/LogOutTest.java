package JIRA;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LogOutTest {
    private WebDriver driver;
    private static ChromeOptions options;
    private User user;

    @BeforeAll
    public static void setDriverLocation()
    {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\WebDrivers\\chromedriver.exe");

        options = new ChromeOptions();
        options.addArguments("--incognito");
    }

    @BeforeEach
    public void openNewBrowser()
    {
        driver = new ChromeDriver(options);
        user = new User(driver);
        user.login();
    }

    @AfterEach
    public void closeBrowser()
    {
        driver.close();
    }

    @Test
    public void logout_SuccessfulLogout()
    {
        user.logout();

        String textOutput = driver.findElement(By.cssSelector("#main > div > div > p.title > strong")).getText();
        String expectedToContain = "logged out";

        driver.navigate().refresh();

        String refreshOutput = driver.findElement(By.cssSelector("#main > div > header > div > div > h1")).getText();



        Assertions.assertTrue(textOutput.contains(expectedToContain));
    }
}
