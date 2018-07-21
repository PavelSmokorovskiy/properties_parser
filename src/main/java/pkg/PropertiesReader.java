package pkg;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader {
    public static void main(String[] args) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File("src/main/java/pkg/impex.impex"))) {

            Properties properties = new Properties();
            properties.load(new FileInputStream("/home/pavel/downloads/base_en.properties"));

            HashMap<Object, Object> map = new HashMap<>(properties);

            PrintWriter printWriter = new PrintWriter(fileOutputStream);

            printWriter.println("INSERT_UPDATE PROPERTY_TABLE_NAME;PROPERTY_NAME[unique=true];PROPERTY_TYPE[unique=true];PROPERTY_VALUE[lang=en]\n" +
                    ";PROPERTY_NAME_VALUE;PROPERTY_TYPE_VALUE;PROPERTY_VALUE_VALUE");

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                printWriter.println(";" + entry.getKey() + ";;" + entry.getValue());
            }

            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}