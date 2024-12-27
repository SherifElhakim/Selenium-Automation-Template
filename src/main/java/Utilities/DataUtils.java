package Utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DataUtils {
    private static final String Test_Data_path = "src/test/resources/TestData/";

    //TODO: reading data from JSON file

    public static String getJsonData(String name, String field) throws FileNotFoundException {
        FileReader reader = new FileReader(Test_Data_path + name + ".json");

        JsonElement jelem = JsonParser.parseReader(reader);
        return jelem.getAsJsonObject().get(field).getAsString();
    }

    //TODO: reading data from properties file
    public static String getPropertiesValue(String fileName , String filed) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(Test_Data_path + fileName + ".properties"));
        return properties.getProperty(filed);
    }
}
