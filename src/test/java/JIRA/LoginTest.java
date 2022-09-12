package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
        webDriver.quit();
    }

    @Test
    public void successfulLogin(){
        String expectedUserName = FileReader.getValueByKey("jira.displayname");

        User user = new User(webDriver);

        user.login();
        webDriver.get("https://jira-auto.codecool.metastage.net/secure/ViewProfile.jspa");

        String fullName = webDriver.findElement(By.id("up-d-fullname")).getText();

        Assertions.assertEquals(expectedUserName, fullName);
    }

    @Test
    public void loginWithIncorrectPassword(){
        String incorrectPassword = "incorrect";
        String expectedMessage = "Sorry, your username and password are incorrect - please try again.";

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/login.jsp");

        webDriver.findElement(By.id("login-form-username")).sendKeys(FileReader.getValueByKey("jira.username"));
        webDriver.findElement(By.id("login-form-password")).sendKeys(incorrectPassword);

        webDriver.findElement(By.id("login-form-submit")).click();

        String actualMessage = "";
        boolean elementFound = true;
        try {
            actualMessage = webDriver.findElement(By.className("aui-message-error")).getText();
        } catch (NoSuchElementException e)
        {
            elementFound = false;
        }


        Assertions.assertTrue(elementFound);
        Assertions.assertEquals(expectedMessage,actualMessage);

        User user = new User(webDriver);
        user.login();
    }

    @Test
    public void login_EmptyCredentials_DisplaysErrorMessage()
    {
        String expectedMessage = "Sorry, your username and password are incorrect - please try again.";

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/login.jsp");

        webDriver.findElement(By.id("login-form-submit")).click();

        boolean elementFound = true;
        String actualMessage = "";
        try {
            actualMessage = webDriver.findElement(By.className("aui-message-error")).getText();
        } catch (NoSuchElementException e)
        {
            elementFound = false;
        }

        Assertions.assertTrue(elementFound);
        Assertions.assertEquals(expectedMessage,actualMessage);
    }

    @Test
    public void login_IncorrectUserName_DisplaysErrorMessage()
    {
        String expectedMessage = "Sorry, your username and password are incorrect - please try again.";
        String uname = "KACSA";
        String pass = "anything";

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/login.jsp");

        webDriver.findElement(By.id("login-form-username")).sendKeys(uname);
        webDriver.findElement(By.id("login-form-password")).sendKeys(pass);

        webDriver.findElement(By.id("login-form-submit")).click();

        boolean elementFound = true;
        String actualMessage = "";
        try {
            actualMessage = webDriver.findElement(By.className("aui-message-error")).getText();
        } catch (NoSuchElementException e)
        {
            elementFound = false;
        }
        Assertions.assertTrue(elementFound);
        Assertions.assertEquals(expectedMessage,actualMessage);
    }
}
