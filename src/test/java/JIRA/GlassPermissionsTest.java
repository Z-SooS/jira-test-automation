package JIRA;

import Main.FileReader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.regex.Pattern;

public class GlassPermissionsTest {
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
        User user = new User(webDriver);
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

    @ParameterizedTest
    @ValueSource(strings = {"Create Issues", "Edit Issues", "Browse Projects"})
    public void checkAdminPermissionsForTest(String rowToSearch)
    {
        String columnToSearch = "Current User";
        WebElement table = webDriver.findElement(By.xpath("//*[@id=\"glass-permissions-matrix-panel\"]/div/table"));
        WebElement thead = table.findElement(By.tagName("thead"));
        WebElement tbody =  table.findElement(By.tagName("tbody"));
        int columnIndex = -1;
        List<WebElement> headers = thead.findElements(By.tagName("th"));
        for (int i = 0; i<headers.size(); i++)
        {
            boolean isCorrectColumn = Pattern.compile(Pattern.quote(columnToSearch), Pattern.CASE_INSENSITIVE).matcher(headers.get(i).getText()).find();
            if (isCorrectColumn)
            {
                columnIndex = i;
                break;
            }
        }
        Assertions.assertTrue(columnIndex > -1,String.format("%s column has been found",columnToSearch));

        WebElement correctRow = getCorrectRow(rowToSearch,tbody);

        Assertions.assertNotNull(correctRow, String.format("%s row exists in table",rowToSearch));

        int finalColumnIndex = columnIndex;
        Assertions.assertDoesNotThrow(() -> {
            correctRow.findElements(By.tagName("td")).get(finalColumnIndex).findElement(By.className("glass-true-icon"));
        });
    }

    private WebElement getCorrectRow(String rowToSearch, WebElement tbody)
    {
        try {
            return tbody.findElement(By.xpath(String.format("//p[contains(@class,'title')]/b[contains(text(),'%s')]/ancestor::tr", rowToSearch)));
        }
        catch (NoSuchElementException e)
        {
            return null;
        }
    }
}
