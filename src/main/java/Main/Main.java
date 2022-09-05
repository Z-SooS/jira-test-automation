package Main;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
    public static void main(String[] args){
        System.setProperty("webdriver.firefox.driver", "E:\\Vegyes\\CodeCool\\Projects\\Advanced - Test Autmation\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();
    }
}
