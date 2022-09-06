package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LoginTest {
    private WebDriver webDriver;
    private static ChromeOptions browserOptions;

    @BeforeAll
    public static void setProperty(){
        System.setProperty("webdriver.chrome.driver", FileReader.getValueByKey("driver.location"));

        browserOptions = new ChromeOptions();
        browserOptions.addArguments("--incognito");
    }

    @BeforeEach
    public void openNewTab(){
        webDriver = new ChromeDriver(browserOptions);
    }

    @AfterEach
    public void closeTab(){
        webDriver.close();
    }

    @Test
    public void successfulLogin(){
        String expectedUserName = FileReader.getValueByKey("jira.displayname");

        User user = new User(webDriver);

        user.login(FileReader.getValueByKey("jira.username"), FileReader.getValueByKey("jira.password"));
        webDriver.get("https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa");

        String fullName = webDriver.findElement(By.id("up-d-fullname")).getText();

        Assertions.assertEquals(expectedUserName, fullName);
    }

    @Test
    public void loginWithIncorrectPassword(){
        throw new RuntimeException("Not implemented yet.");
    }
}
