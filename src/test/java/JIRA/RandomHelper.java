package JIRA;

import org.openqa.selenium.WebDriver;

import java.util.Random;

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

    public static int generateRandomNumber(int upperBound){
        Random random = new Random();

        return random.nextInt(upperBound);
    }
}
