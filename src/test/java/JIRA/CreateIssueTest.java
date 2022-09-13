package JIRA;

import Main.FileReader;
import dev.failsafe.internal.util.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;
import java.util.stream.Stream;

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
        int randomNumberForId = RandomHelper.generateRandomNumber(100);
        String issueSummary = "New bug issue " + randomNumberForId;
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/MTP-749?filter=-5");
        webDriver.findElement(By.id("create_link")).click();
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("project-field")).click();
        webDriver.findElement(By.id("project-field")).sendKeys(Keys.BACK_SPACE);
        webDriver.findElement(By.id("project-field")).sendKeys("Main Testing Project (MTP)");
        webDriver.findElement(By.id("project-field")).sendKeys(Keys.ENTER);
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("issuetype-field")).click();
        webDriver.findElement(By.id("issuetype-field")).sendKeys(Keys.BACK_SPACE);
        webDriver.findElement(By.id("issuetype-field")).sendKeys("Bug");
        webDriver.findElement(By.id("issuetype-field")).sendKeys(Keys.ENTER);
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("summary")).sendKeys(issueSummary);
        webDriver.findElement(By.id("create-issue-submit")).click();

        var wait = new WebDriverWait(webDriver, Duration.ofMillis(10000));
        wait.until(ExpectedConditions.elementToBeClickable(By.className("issue-created-key")));
        webDriver.findElement(By.className("issue-created-key")).click();

        String searchedIssueSummary = webDriver.findElement(By.id("summary-val")).getText();
        System.out.println(searchedIssueSummary);

        deleteCreatedTestIssue(webDriver);

        Assertions.assertEquals(issueSummary, searchedIssueSummary);
    }

    @Test
    public void createIssueWithoutSummary(){
        String expectedMessage = "You must specify a summary of the issue.";
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/MTP-749?filter=-5");

        webDriver.findElement(By.id("create_link")).click();

        var wait = new WebDriverWait(webDriver, Duration.ofMillis(10000));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("create-issue-submit")));

        webDriver.findElement(By.id("create-issue-submit")).click();

        String actualMessage = "";
        boolean elementFound = true;
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.className("error")));
            actualMessage = webDriver.findElement(By.className("error")).getText();
        } catch (NoSuchElementException e)
        {
            elementFound = false;
        }

        Assertions.assertTrue(elementFound);
        Assertions.assertEquals(expectedMessage,actualMessage);

    }

    @ParameterizedTest
    @MethodSource("projects")
    public void createIssueWithAllOfProjectType(String projectName)
    {
        int randomNumberForId = RandomHelper.generateRandomNumber(100);
        String issueSummary = "New project related issue " + randomNumberForId;
        user.login();

        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/MTP/summary");

        webDriver.findElement(By.id("create_link")).click();

        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("project-field")).click();
        webDriver.findElement(By.id("project-field")).sendKeys(Keys.BACK_SPACE);
        webDriver.findElement(By.id("project-field")).sendKeys(projectName);
        webDriver.findElement(By.id("project-field")).sendKeys(Keys.ENTER);
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.xpath("//*[@id=\"create-issue-dialog\"]/header/h2")).click();
        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("summary")).sendKeys(issueSummary);
        webDriver.findElement(By.id("create-issue-submit")).click();

        var wait = new WebDriverWait(webDriver, Duration.ofMillis(10000));
        wait.until(ExpectedConditions.elementToBeClickable(By.className("issue-created-key")));
        webDriver.findElement(By.className("issue-created-key")).click();
        RandomHelper.Wait(webDriver);

        String actualProjectName = webDriver.findElement(By.id("project-name-val")).getText();

        deleteCreatedTestIssue(webDriver);
        Assertions.assertTrue(projectName.contains(actualProjectName));

    }
    private static Stream<Arguments> projects() {
        return Stream.of(
                Arguments.arguments("TOUCAN project (TOUCAN)"),
                Arguments.arguments("JETI project (JETI)"),
                Arguments.arguments("COALA project (COALA)")
        );
    }

    private void deleteCreatedTestIssue(WebDriver webDriver){
        webDriver.findElement(By.id("opsbar-operations_more")).click();
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;

        WebElement deleteButton = webDriver.findElement(By.id("delete-issue"));
        jse.executeScript("arguments[0].click()", deleteButton);

        RandomHelper.Wait(webDriver);

        webDriver.findElement(By.id("delete-issue-submit")).click();
    }
}
