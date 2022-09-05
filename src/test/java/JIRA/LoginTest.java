package JIRA;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoginTest {
    private WebDriver webDriver;
    private static FirefoxOptions firefoxOptions;
    private static final String username = "automation28";
    private static final String password = "CCAutoTest19.";

    @BeforeAll
    public static void setProperty(){
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("driver.location"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        System.setProperty("webdriver.gecko.driver", "E:\\Vegyes\\CodeCool\\Projects\\Advanced - Test Autmation\\geckodriver.exe");

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

        user.login(username, password);
        webDriver.get("https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa");

        String fullName = webDriver.findElement(By.id("up-d-fullname")).getText();

        Assertions.assertEquals(expectedUserName, fullName);
    }
}
