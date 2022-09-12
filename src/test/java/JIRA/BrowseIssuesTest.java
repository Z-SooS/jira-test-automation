package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class BrowseIssuesTest {
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

    @ParameterizedTest
    @MethodSource("issuesListUrls")
    public void issuesAtLeastThree(String url)
    {
        int minimum = 3;
        user.login();

        webDriver.get(url);

        List<WebElement> issues = webDriver.findElements(By.cssSelector("ol.issue-list > li"));

        Assertions.assertTrue(issues.size() >= minimum);
    }
    private static Stream<String> issuesListUrls()
    {
        return Stream.of(
                "https://jira-auto.codecool.metastage.net/projects/COALA/issues",
                "https://jira-auto.codecool.metastage.net/projects/JETI/issues",
                "https://jira-auto.codecool.metastage.net/projects/TOUCAN/issues"
        );
    }

    @ParameterizedTest
    @MethodSource("specificIssueProjectUrls")
    public void checkIssuesWIthSpecificId(String projectKey)
    {
        String urlBase = "https://jira-auto.codecool.metastage.net/browse/";
        int[] ids = new int[]{1,2,3};
        String[] expectedIds = new String[ids.length];
        String[] actualIds = new String[ids.length];
        boolean[] actualNotFound = new boolean[ids.length];
        boolean[] expectedNotFound = new boolean[ids.length];

        user.login();

        for (int index = 0; index<ids.length; index++) {
            expectedIds[index] = projectKey + '-' + ids[index];

            webDriver.get(urlBase + expectedIds[index]);

            try {
                actualIds[index] = webDriver.findElement(By.id("key-val")).getText();
            }catch (NoSuchElementException e){
                actualNotFound[index] = true;
            }
        }

        Assertions.assertArrayEquals(expectedNotFound,actualNotFound);
        Assertions.assertArrayEquals(expectedIds,actualIds);
    }
    private static Stream<String> specificIssueProjectUrls()
    {
        return Stream.of(
                "COALA",
                "TOUCAN",
                "JETI"
        );
    }

}
