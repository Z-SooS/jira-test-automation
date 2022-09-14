package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v85.indexeddb.model.Key;

import java.util.List;
import java.util.stream.Collectors;

public class GlassVersionsTest {
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
    public void addIssueToVersion(){
        User user = new User(webDriver);

        user.login();
        webDriver.get("https://jira-auto.codecool.metastage.net/browse/PP-401");
        webDriver.findElement(By.id("edit-issue")).click();
        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("fixVersions-textarea")).click();
        webDriver.findElement(By.id("fixVersions-textarea")).sendKeys("xixo");
        webDriver.findElement(By.id("fixVersions-textarea")).sendKeys(Keys.ENTER);
        webDriver.findElement(By.id("edit-issue-submit")).click();
        webDriver.get("https://jira-auto.codecool.metastage.net/projects/PP/versions/10593");
        List<String> summaryList = webDriver.findElements(By.cssSelector("td.summary")).stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertTrue(summaryList.contains("Test issue 8000"));

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/PP-401");
        webDriver.findElement(By.id("edit-issue")).click();
        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("fixVersions-textarea")).click();
        webDriver.findElement(By.id("fixVersions-textarea")).sendKeys(Keys.BACK_SPACE);
        webDriver.findElement(By.id("fixVersions-textarea")).sendKeys(Keys.BACK_SPACE);
        webDriver.findElement(By.id("edit-issue-submit")).click();
    }
}
