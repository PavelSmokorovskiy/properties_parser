package pkg;

import com.google.common.collect.Lists;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

import static pkg.PropertyTranslation.*;

public class PropertyTranslation {

    static String PATH = "/home/pavel/downloads/base_%s.properties";
    static String OUT_PATH = "/home/pavel/downloads/translation_export.xlsx";
    static String IMPEX_FILE = "/home/pavel/downloads/file.impex";
    static String FOS_FILE = "/home/pavel/downloads/fos.impex";
    static String BASE_PROPERTY = "en";

    public static void main(String[] args) {
        try {
            new PropertiesTranslation().go();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class PropertiesTranslation {

    private static List<String> locales = Lists.newArrayList(Arrays.asList("en", "es_ES"));

    void go() throws Exception {

        try (FileWriter writer = new FileWriter(IMPEX_FILE)) {

            writer.write("kek");

        }

        try (XSSFWorkbook workbook = new XSSFWorkbook(); FileOutputStream outputStream = new FileOutputStream(OUT_PATH)) {
            XSSFSheet sheet = workbook.createSheet();
            writeHeader(sheet);
            writeData(sheet);
            workbook.write(outputStream);
        }
    }

    private void writeData(XSSFSheet sheet) throws IOException {

        OrderedProperties baseProp = getPropertyResource(BASE_PROPERTY);

        for (Object o : baseProp.keySet()) {
            int i = 0;
            String key = (String) o;

            XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(i).setCellValue(key);

            for (String locale : locales) {
                i += 1;
                row.createCell(i).setCellValue(getPropertyResource(locale).getProperty(key));
            }
        }
    }

    private void writeHeader(XSSFSheet sheet) throws IOException {


        XSSFRow row = sheet.createRow(0);
        int i = 1;
        for (String locale : locales) {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(locale);
            i++;
        }
        sheet.setColumnWidth(0, 13560);
    }

    private static Map<String, OrderedProperties> translations = new HashMap<>();

    private OrderedProperties getPropertyResource(String locale) {

        if (!translations.containsKey(locale)) {
            OrderedProperties p = new OrderedProperties();
            try {
                p.load(new BufferedReader(new InputStreamReader(new FileInputStream(getPropPath(locale)), "UTF-8")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            translations.put(locale, p);
        }

        return translations.get(locale);
    }

    private String getPropPath(String locale) {
        return String.format(PATH, locale);
    }

}

class OrderedProperties extends Properties {
    @Override
    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
    }
}

