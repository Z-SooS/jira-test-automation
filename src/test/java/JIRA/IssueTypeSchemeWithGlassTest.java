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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IssueTypeSchemeWithGlassTest {
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

    @ParameterizedTest
    @MethodSource("wantedIssueTypes")
    public void verifyIssueTypes(String issueName, String wantedId){
        User user = new User(webDriver);
        user.login();

        webDriver.get("https://jira-auto.codecool.metastage.net/projects/PP?selectedItem=com.codecanvas.glass:glass");
        List<String> issueTypeList = webDriver.findElements(By.cssSelector("td.glass-meta-value>span[title]")).stream().map(WebElement::getAccessibleName).collect(Collectors.toList());

        List<String> idList = new ArrayList<>();

        for (String s : issueTypeList){
            idList.add(s.substring(s.lastIndexOf(" ") + 1));
        }

        Assertions.assertTrue(idList.contains(wantedId));
    }

    private static Stream<Arguments> wantedIssueTypes()
    {
        return Stream.of(
                Arguments.arguments("Epic", "10000"),
                Arguments.arguments("Story", "10001"),
                Arguments.arguments("Task", "10002"),
                Arguments.arguments("Sub-task", "10003"),
                Arguments.arguments("Bug", "10004")
        );
    }
}
