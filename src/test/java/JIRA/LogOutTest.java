package JIRA;

import Main.FileReader;
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
        System.setProperty("webdriver.chrome.driver", FileReader.getValueByKey("driver.location"));

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
        driver.quit();
    }

    @Test
    public void logout_SuccessfulLogout()
    {
        String expectedToContain = "logged out";
        String profileContain = "log in";
        user.logout();

        String textOutput = driver.findElement(By.cssSelector("#main > div > div > p.title > strong")).getText();
        Assertions.assertTrue(textOutput.contains(expectedToContain));

        driver.navigate().refresh();
        String refreshOutput = driver.findElement(By.cssSelector("#main > div > header > div > div > h1")).getText();
        Assertions.assertTrue(refreshOutput.contains(expectedToContain));

        driver.navigate().to("https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa");
        String profileOutput = driver.findElement(By.cssSelector("#login-form > div.form-body > div.aui-message.aui-message-warning > p")).getText();
        Assertions.assertTrue(profileOutput.contains(profileContain));
    }
}
