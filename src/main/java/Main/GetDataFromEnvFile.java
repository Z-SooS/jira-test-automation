package Main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetDataFromEnvFile {
    public static String getByFieldName(String fieldName){
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            return prop.getProperty(fieldName);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
