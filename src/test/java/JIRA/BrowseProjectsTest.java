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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BrowseProjectsTest {
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
    public void browseExistingProject()
    {
        user.login();
        String key = "MTP";


        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/MTP/summary");

        List<String> metaValues = webDriver.findElements(By.className("project-meta-value"))
                .stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertTrue(metaValues.contains(key));
    }

    @Test
    public void browseNonExistingProject()
    {
        String expectedError = "You can't view this project";

        user.login();
        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/b/summary");

        String main = webDriver.findElement(By.id("main")).getText();

        Assertions.assertTrue(main.contains(expectedError));
    }

    @ParameterizedTest
    @MethodSource("projects")
    public void browseConfiguredProjects(String url, String key)
    {
        user.login();

        webDriver.navigate().to(url);

        String projectMeta = webDriver.findElement(By.className("project-meta")).getText();

        Assertions.assertTrue(projectMeta.contains(key));
    }
    private static Stream<Arguments> projects() {
        return Stream.of(
                Arguments.arguments("https://jira-auto.codecool.metastage.net/projects/TOUCAN/summary","TOUCAN"),
                Arguments.arguments("https://jira-auto.codecool.metastage.net/projects/JETI/summary", "JETI"),
                Arguments.arguments("https://jira-auto.codecool.metastage.net/projects/COALA/summary", "COALA")
        );
    }
}
