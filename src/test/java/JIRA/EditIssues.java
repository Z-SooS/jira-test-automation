package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class EditIssues {
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
    public void successfullyEditAnIssue(){
        String summaryValue;
        String originalSummary = "As an Agile team, I'd like to learn about Scrum >> Click the \"MT-1\" link at the left of this row to see detail in the Description tab on the right";
        User user = new User(webDriver);
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/MT-1");
        webDriver.findElement(By.id("edit-issue")).click();

        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("summary")).clear();
        webDriver.findElement(By.id("summary")).sendKeys("Kacsa");
        webDriver.findElement(By.id("edit-issue-submit")).click();

        RandomHelper.Wait(webDriver);
        summaryValue = webDriver.findElement(By.id("summary-val")).getText();

        Assertions.assertEquals("Kacsa", summaryValue);

        webDriver.findElement(By.id("edit-issue")).click();

        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("summary")).clear();
        webDriver.findElement(By.id("summary")).sendKeys(originalSummary);
        webDriver.findElement(By.id("edit-issue-submit")).click();

        RandomHelper.Wait(webDriver);
        summaryValue = webDriver.findElement(By.id("summary-val")).getText();

        Assertions.assertEquals(originalSummary, summaryValue);
    }
}
