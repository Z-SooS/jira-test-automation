package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CreateIssueTest {
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
        webDriver.quit();
    }

    @Test
    public void createIssue(){
        // TODO not working yet
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/MTP-749?filter=-5");
        webDriver.findElement(By.id("create_link")).click();
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("project-field")).clear();
        webDriver.findElement(By.id("project-field")).sendKeys("Main Testing Project (MTP)");

        webDriver.findElement(By.id("issuetype-field")).clear();
        webDriver.findElement(By.id("issuetype-field")).sendKeys("Bug");

        webDriver.findElement(By.id("summary")).sendKeys("New bug issue");
        webDriver.findElement(By.id("create-issue-submit")).clear();
    }
}
