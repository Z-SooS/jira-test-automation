package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.stream.Collectors;

public class BrowseProjects {
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
    public void browseExistingProject()
    {
        user.login();
        String key = "MTP";


        webDriver.navigate().to("https://jira-auto.codecool.metastage.net/projects/MTP/summary");

        List<String> metaValues = webDriver.findElements(By.className("project-meta-value"))
                .stream().map(WebElement::getText).collect(Collectors.toList());

        Assertions.assertTrue(metaValues.contains(key));
    }
}
