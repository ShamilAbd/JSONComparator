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
    private final Boolean nullAsNotEqual;
    private final Boolean showFullyMatched;
    private final Boolean showPartialMatched;
    private final Boolean showNotMatched;
    private final Boolean showOnlyCompareKeys;
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
        firstFilePath = rootJSON.getString("firstFilePath");
        secondFilePath = rootJSON.getString("secondFilePath");
        compareObjectListPath = rootJSON.getString("compareObjectListPath");
        nullAsNotEqual = rootJSON.getBoolean("nullAsNotEqual");
        showFullyMatched = rootJSON.getBoolean("showFullyMatched");
        showPartialMatched = rootJSON.getBoolean("showPartialMatched");
        showNotMatched = rootJSON.getBoolean("showNotMatched");
        showOnlyCompareKeys = rootJSON.getBoolean("showOnlyCompareKeys");

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

    private void createConfigExample(String path) { // TODO: обновить перед релизом
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

    public Boolean getNullAsNotEqual() {
        return nullAsNotEqual;
    }

    public Boolean getShowFullyMatched() {
        return showFullyMatched;
    }

    public Boolean getShowPartialMatched() {
        return showPartialMatched;
    }

    public Boolean getShowNotMatched() {
        return showNotMatched;
    }

    public Boolean getShowOnlyCompareKeys() {
        return showOnlyCompareKeys;
    }

    public int getCurrentJsonComparatorVersion() {
        return currentJsonComparatorVersion;
    }

    public static void main(String[] args) {
        // For simple test
        Configuration configuration = new Configuration();

        System.out.println(configuration.getFirstFilePath());
        System.out.println(configuration.getSecondFilePath());
        System.out.println(configuration.getCompareObjectListPath());
        System.out.println(configuration.getCompareKeys());
        System.out.println(configuration.getNullAsNotEqual());
        System.out.println(configuration.getShowFullyMatched());
        System.out.println(configuration.getShowPartialMatched());
        System.out.println(configuration.getShowNotMatched());
        System.out.println(configuration.getShowOnlyCompareKeys());
        System.out.println(configuration.getJsonComparatorVersion());
    }
}
