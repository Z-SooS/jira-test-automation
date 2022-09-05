package JIRA;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class User {
    private WebDriver driver;

    public User(WebDriver driver){
        this.driver = driver;
    }

    public void login(String username, String password){
        driver.navigate().to("https://jira-auto.codecool.metastage.net/login.jsp?os_destination=%2Fsecure%2FMyJiraHome.jspa");

        driver.findElement(By.id("login-form-username")).sendKeys(username);
        driver.findElement(By.id("login-form-password")).sendKeys(password);

        driver.findElement(By.id("login-form-submit")).click();
    }

    public void logout(){
        driver.findElement(By.className("aui-avatar-inner")).click();
        driver.findElement(By.id("log_out")).click();
    }
}
