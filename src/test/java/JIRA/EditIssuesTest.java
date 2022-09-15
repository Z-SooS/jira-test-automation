package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class EditIssuesTest {
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
    public void successfullyEditAnIssue(){
        String summaryValue;
        String originalSummary = "As an Agile team, I'd like to learn about Scrum >> Click the \"MT-1\" link at the left of this row to see detail in the Description tab on the right";
        User user = new User(webDriver);
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/browse/MT-1");
        clickOnEditClearInputFieldSendValueClickOnSubmit("Kacsa");

        RandomHelper.Wait(webDriver);
        summaryValue = webDriver.findElement(By.id("summary-val")).getText();

        Assertions.assertEquals("Kacsa", summaryValue);

        clickOnEditClearInputFieldSendValueClickOnSubmit(originalSummary);

        RandomHelper.Wait(webDriver);
        summaryValue = webDriver.findElement(By.id("summary-val")).getText();

        Assertions.assertEquals(originalSummary, summaryValue);
    }

    @ParameterizedTest
    @MethodSource("projects")
    public void everyIssueEditable(String url){
        User user = new User(webDriver);
        user.login();

        Assertions.assertTrue(tryToFindElement(url));
    }

    private void clickOnEditClearInputFieldSendValueClickOnSubmit(String valueToInsert){
        webDriver.findElement(By.id("edit-issue")).click();

        RandomHelper.Wait(webDriver);
        webDriver.findElement(By.id("summary")).clear();
        webDriver.findElement(By.id("summary")).sendKeys(valueToInsert);
        webDriver.findElement(By.id("edit-issue-submit")).click();
    }

    private Boolean tryToFindElement(String urlToOpen){
        webDriver.get(urlToOpen);

        RandomHelper.Wait(webDriver);
        try{
            webDriver.findElement(By.id("edit-issue"));
            return true;
        }catch (NoSuchElementException e){
            return false;
        }

    }

    private static Stream<Arguments> projects() {
        return Stream.of(
                Arguments.arguments("https://jira-auto.codecool.metastage.net/browse/TOUCAN-154?jql=project%20%3D%20TOUCAN"),
                Arguments.arguments("https://jira-auto.codecool.metastage.net/browse/JETI-184?jql=project%20%3D%20JETI"),
                Arguments.arguments("https://jira-auto.codecool.metastage.net/issues/?jql=project%20%3D%20COALA")
        );
    }
}
