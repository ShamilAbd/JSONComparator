package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    // TODO: в конфиг добавить флаги какие данные будем выводить в отчет
    private final String firstFilePath;
    private final String secondFilePath;
    private final String compareObjectListPath;
    private final Integer jsonComparatorVersion;
    private final int currentJsonComparatorVersion = 1;
    private final List<String> compareKeys = new ArrayList<>();

    public Configuration() {
        String configFilePath = "config.json";
        File config = new File(configFilePath);
        if (!config.exists()) {
            createConfigExample(config.getAbsolutePath());
        }
        String jsonText = Utils.readFile(config.getAbsolutePath());
        JSONObject rootJSON = new JSONObject(jsonText);
        if (rootJSON.has("JSONComparatorVersion")) {
            jsonComparatorVersion = (Integer) rootJSON.get("JSONComparatorVersion");
        } else {
            jsonComparatorVersion = null;
        }
        checkVersionCompatibility(jsonComparatorVersion);
        firstFilePath = (String) rootJSON.get("firstFilePath");
        secondFilePath = (String) rootJSON.get("secondFilePath");
        compareObjectListPath = (String) rootJSON.get("compareObjectListPath");

        JSONArray values = (JSONArray) rootJSON.get("compareKeys");
        for (Object value : values) {
            compareKeys.add((String) value);
        }
    }

    private void checkVersionCompatibility(Integer versionFromFile) {
        if (versionFromFile == null) {
            throw new RuntimeException("В файле config.json не заполнен ключ \"JSONComparatorVersion\".");
        }

        if (versionFromFile != currentJsonComparatorVersion) {
            throw new RuntimeException("Версия config.json не соответствует версии программы:\n"
                    + "Версия программы: " + currentJsonComparatorVersion + "\n"
                    + "Версия файла: " + versionFromFile);
        }
    }

    private void createConfigExample(String path) {
        String text = """
                {
                  "firstFilePath" : "C:\\\\Work\\\\test.json or C:/Work/test.json or test.json - for relative path",
                  "secondFilePath" : "test2.json",
                  "compareObjectListPath" : "KeyName1.KeyName2.KeyName3",
                  "compareKeys" : ["name", "intValue", "floatValue", "date"],
                  "JSONComparatorVersion" : 1
                }""";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFirstFilePath() {
        return firstFilePath;
    }

    public String getSecondFilePath() {
        return secondFilePath;
    }

    public String getCompareObjectListPath() {
        return compareObjectListPath;
    }

    public int getJsonComparatorVersion() {
        return jsonComparatorVersion;
    }

    public List<String> getCompareKeys() {
        return List.copyOf(compareKeys);
    }

    public static void main(String[] args) {
        // For simple test
        Configuration configuration = new Configuration();

        System.out.println(configuration.getFirstFilePath());
        System.out.println(configuration.getSecondFilePath());
        System.out.println(configuration.getCompareObjectListPath());
        System.out.println(configuration.getCompareKeys());
        System.out.println(configuration.getJsonComparatorVersion());
    }
}
