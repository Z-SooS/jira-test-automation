package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
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

    @Test
    public void createNewVersionTest() {
        User user = new User(webDriver);

        user.login();

        String expectedName = FileReader.getValueByKey("jira.username") + UUID.randomUUID();
        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/PP?selectedItem=com.atlassian.jira.jira-projects-plugin%3Arelease-page&status=unreleased");

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));

        // Write name to new version
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#releases-add__version > div.releases-add__name > input")));
        webDriver.findElement(By.cssSelector("#releases-add__version > div.releases-add__name > input")).sendKeys(expectedName);

        // Click add button
        wait.until(ExpectedConditions.elementToBeClickable(webDriver.findElement(By.cssSelector("#releases-add__version > div.releases-add__confirm > button"))));
        webDriver.findElement(By.cssSelector("#releases-add__version > div.releases-add__confirm > button")).click();

        // Go to versions page
        webDriver.get("https://jira-auto.codecool.metastage.net/projects/PP?selectedItem=com.codecanvas.glass:glass");
        webDriver.findElement(By.id("aui-uid-2")).click();

        // Read all version names
        List<String> allVersionNames = webDriver.findElements(By.className("versions-table__name")).stream().map(WebElement::getText).collect(Collectors.toList());
        // Check if version with expected name exists
        Assertions.assertTrue(allVersionNames.contains(expectedName));

        // Delete created version
        // Go to releases page
        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/PP?selectedItem=com.atlassian.jira.jira-projects-plugin%3Arelease-page&status=unreleased");
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("item-state-ready")));

        // Get all version tds
        List<WebElement> versionRows = webDriver.findElements(By.className("item-state-ready"));

        // Select row of created version
        WebElement correctRow = versionRows.stream().filter(webElement -> webElement.findElement(By.className("versions-table__name")).getText().contains(expectedName)).findFirst().orElse(null);
        // Check if there
        if (correctRow == null) return;

        // Could replace with jse
        // Click "..."
        correctRow.findElement(By.cssSelector("td.dynamic-table__actions > div > a > span")).click();
        wait.until(ExpectedConditions.elementToBeClickable(correctRow.findElement(By.className("project-config-operations-delete"))));

        // Click delete option
        correctRow.findElement(By.className("project-config-operations-delete")).click();

        // Wait for modal and click confirm
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));
        webDriver.findElement(By.id("submit")).click();
    }
}
