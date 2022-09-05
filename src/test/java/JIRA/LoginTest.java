package JIRA;

import Main.GetDataFromEnvFile;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class LoginTest {
    private WebDriver webDriver;
    private static FirefoxOptions firefoxOptions;

    @BeforeAll
    public static void setProperty(){
        System.setProperty("webdriver.gecko.driver", GetDataFromEnvFile.getByFieldName("driver.location"));

        firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("-private");
    }

    @BeforeEach
    public void openNewTab(){
        webDriver = new FirefoxDriver(firefoxOptions);
    }

    @AfterEach
    public void closeTab(){
        webDriver.close();
    }

    @Test
    public void successfulLogin(){
        String expectedUserName = "Auto Tester 28";

        User user = new User(webDriver);

        user.login(GetDataFromEnvFile.getByFieldName("jira.username"), GetDataFromEnvFile.getByFieldName("jira.password"));
        webDriver.get("https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa");

        String fullName = webDriver.findElement(By.id("up-d-fullname")).getText();

        Assertions.assertEquals(expectedUserName, fullName);
    }
}
