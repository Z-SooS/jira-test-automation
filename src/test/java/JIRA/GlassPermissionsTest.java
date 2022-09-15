package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class GlassPermissionsTest {
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
        user.login();
        navigateToPermissionsPage();
    }

    @AfterEach
    public void closeTab(){
        webDriver.quit();
    }

    private void navigateToPermissionsPage()
    {
        webDriver.get("https://jira-auto.codecool.metastage.net/projects/PP?selectedItem=com.codecanvas.glass:glass");
        webDriver.findElement(By.id("glass-permissions-nav")).click();
    }
}
