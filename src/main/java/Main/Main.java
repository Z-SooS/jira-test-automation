package Main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
    public static void main(String[] args){
        System.setProperty("webdriver.chrome.driver", GetDataFromEnvFile.getByFieldName("driver.location"));
        WebDriver driver = new FirefoxDriver();
    }
}
