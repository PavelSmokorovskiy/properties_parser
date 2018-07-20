package pkg;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader {
    public static void main(String[] args) {

        Properties prop = new Properties();
        InputStream input = null;
        OutputStream output = null;
        Map<String, String> map = new HashMap();

        try {

            input = new FileInputStream("/home/pavel/downloads/base_en.properties");


            prop.load(input);

            for (final String name: prop.stringPropertyNames())
                map.put(name, prop.getProperty(name));

            for (Map.Entry entry : map.entrySet()) {
                System.out.println("key: " + entry.getKey() + ", value: " + entry.getValue());
            }

        try {
            output = new FileOutputStream("/home/pavel/downloads/impex.impex");
            prop.store(output, null);
        } finally {
                output.close();
        }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
