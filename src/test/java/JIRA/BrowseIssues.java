package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.regex.Pattern;

public class BrowseIssues {
    private WebDriver webDriver;
    private User user;
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
        user = new User(webDriver);
    }

    @AfterEach
    public void closeTab(){
        webDriver.close();
    }

    @Test
    public void ExistingIssueDetails()
    {
        String expectedSummary = "Browse Issue Testing";
        user.login();

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/browse/MTP-2253");

        String actualSummary = webDriver.findElement(By.id("summary-val")).getText();

        Assertions.assertEquals(expectedSummary,actualSummary);
    }

    @Test
    public void nonExistingIssueDetails()
    {
        String expectedMessage = "You can't view this issue";
        user.login();

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/browse/MTP-99999999999");

        String message = webDriver.findElement(By.className("issue-body-content")).getText();

        boolean contains = Pattern.compile(Pattern.quote(expectedMessage), Pattern.CASE_INSENSITIVE).matcher(message).find();
        Assertions.assertTrue(contains);
    }



}
