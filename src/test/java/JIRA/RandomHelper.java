package JIRA;

import org.openqa.selenium.WebDriver;

public class RandomHelper {
    public static void Wait(WebDriver webDriver){
        synchronized (webDriver){
            try {
                webDriver.wait(1000);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
