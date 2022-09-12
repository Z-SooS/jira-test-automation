package JIRA;

import org.openqa.selenium.WebDriver;

import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String getDefaultBrowser() {
        // TODO not working, do not use it yet
        try
        {
            // Get registry where we find the default browser
            Process process = Runtime.getRuntime().exec("REG QUERY HKEY_CLASSES_ROOT\\http\\shell\\open\\command");
            Scanner kb = new Scanner(process.getInputStream());
            while (kb.hasNextLine())
            {
                // Get output from the terminal, and replace all '\' with '/' (makes regex a bit more manageable)
                String registry = (kb.nextLine()).replaceAll("\\\\", "/").trim();

                // Extract the default browser
                Matcher matcher = Pattern.compile("/(?=[^/]*$)(.+?)[.]").matcher(registry);
                if (matcher.find())
                {
                    // Scanner is no longer needed if match is found, so close it
                    kb.close();
                    String defaultBrowser = matcher.group(1);

                    // Capitalize first letter and return String
                    defaultBrowser = defaultBrowser.substring(0, 1).toUpperCase() + defaultBrowser.substring(1, defaultBrowser.length());
                    return defaultBrowser;
                }
            }
            // Match wasn't found, still need to close Scanner
            kb.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        // Have to return something if everything fails
        return "Error: Unable to get default browser";
    }

    public static String getOsName(){
        return System.getProperty("os.name").toLowerCase();
    }
}
