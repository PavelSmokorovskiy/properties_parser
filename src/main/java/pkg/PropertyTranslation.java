package pkg;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PropertyTranslation {

    private static final Logger logger = Logger.getLogger(PropertyTranslation.class);

    private static String IN_PATH = "src/main/java/pkg/base_%s.properties";
    private static String OUT_PATH = "src/main/java/pkg/base_%s.impex";
    private static String IMPEX_HEADER = "INSERT_UPDATE PROPERTY_TABLE_NAME;PROPERTY_NAME[unique=true];PROPERTY_TYPE[unique=true];PROPERTY_VALUE[lang=%s]";

    private static List<String> locales = Lists.newArrayList(Arrays.asList("en"));

    public static void main(String[] args) {

        new PropertyTranslation().go();
    }

    public void go() {

        for (String locale : locales) {
            writeData(locale);
        }
    }

    private String getPathWithLocale(String path, String locale) {
        return String.format(path, locale);
    }

    private void writeData(String locale) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(
                new File(getPathWithLocale(OUT_PATH, locale)))) {

            Properties properties = new Properties();
            properties.load(new FileInputStream(getPathWithLocale(IN_PATH, locale)));

            HashMap<Object, Object> map = new HashMap<>(properties);

            PrintWriter printWriter = new PrintWriter(
                    new OutputStreamWriter(fileOutputStream, StandardCharsets.ISO_8859_1), true);

            printWriter.println(getPathWithLocale(IMPEX_HEADER, locale));

            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                printWriter.println(";" + entry.getKey() + ";;" + entry.getValue());
            }

            printWriter.flush();
            printWriter.close();

        } catch (IOException e) {
            logger.info(e);
        }
    }
}
