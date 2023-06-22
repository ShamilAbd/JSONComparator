package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final JSONObject rootJSON;
    private String firstFilePath;
    private String secondFilePath;
    private String compareKeysArrayPath;
    private Integer jsonComparatorVersion;
    private Boolean nullAsNotEqual;
    private Boolean showFullyMatched;
    private Boolean showPartialMatched;
    private Boolean showNotMatched;
    private Boolean showOnlyCompareKeys;
    private Boolean openResultAfterCompare;
    private Boolean addRowNumber;
    private Boolean addCommaBetweenObjects;
    private int leftIndentsInObject;
    private final int currentJsonComparatorVersion = 1;
    private final List<String> compareKeys = new ArrayList<>();

    public Configuration() {
        File config = new File("config.json");
        if (!config.exists()) {
            createConfigExample(config.getAbsolutePath());
            throw new RuntimeException("В папке был создан config.json файл, заполните его и запустите программу снова.");
        }
        rootJSON = new JSONObject(Utils.readFile(config.getAbsolutePath()));
        checkVersionCompatibility();
        loadParameters();
    }

    private void createConfigExample(String path) { // TODO: обновить перед релизом
        String text = """
                {
                  "firstFilePath" : "C:\\\\Work\\\\test.json or C:/Work/test.json or test.json - for relative path",
                  "secondFilePath" : "test2.json",
                  "compareKeysArrayPath" : "KeyName1.KeyName2.KeyName3",
                  "compareKeys" : ["name", "intValue", "floatValue", "date"],
                  "JSONComparatorVersion" : 1
                }""";
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkVersionCompatibility() {
        if (rootJSON.has("JSONComparatorVersion")) {
            jsonComparatorVersion = (Integer) rootJSON.get("JSONComparatorVersion");
        } else {
            throw new RuntimeException("В файле config.json не заполнен ключ \"JSONComparatorVersion\".");
        }

        if (jsonComparatorVersion != currentJsonComparatorVersion) {
            throw new RuntimeException("Версия config.json не соответствует версии программы:\n"
                    + "Версия программы: " + currentJsonComparatorVersion + "\n"
                    + "Версия файла: " + jsonComparatorVersion);
        }
    }

    private void loadParameters() {
        loadCompareKeys();

        firstFilePath = rootJSON.getString("firstFilePath");
        secondFilePath = rootJSON.getString("secondFilePath");
        compareKeysArrayPath = rootJSON.getString("compareKeysArrayPath");
        nullAsNotEqual = rootJSON.getBoolean("nullAsNotEqual");
        showFullyMatched = rootJSON.getBoolean("showFullyMatched");
        showPartialMatched = rootJSON.getBoolean("showPartialMatched");
        showNotMatched = rootJSON.getBoolean("showNotMatched");
        showOnlyCompareKeys = rootJSON.getBoolean("showOnlyCompareKeys");
        addRowNumber = rootJSON.getBoolean("addRowNumber");
        addCommaBetweenObjects = rootJSON.getBoolean("addCommaBetweenObjects");
        openResultAfterCompare = rootJSON.getBoolean("openResultAfterCompare");
        leftIndentsInObject = rootJSON.getInt("leftIndentsInObject");
    }

    private void loadCompareKeys() {
        JSONArray values = (JSONArray) rootJSON.get("compareKeys");
        for (Object value : values) {
            compareKeys.add((String) value);
        }
    }

    public String getFirstFilePath() {
        return firstFilePath;
    }

    public String getSecondFilePath() {
        return secondFilePath;
    }

    public String getCompareKeysArrayPath() {
        return compareKeysArrayPath;
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

    public Boolean getOpenResultAfterCompare() {
        return openResultAfterCompare;
    }

    public Boolean getAddRowNumber() {
        return addRowNumber;
    }

    public int getLeftIndentsInObject() {
        return leftIndentsInObject;
    }

    public Boolean getAddCommaBetweenObjects() {
        return addCommaBetweenObjects;
    }

    public static void main(String[] args) {
        // For simple test
        Configuration configuration = new Configuration();

        System.out.println(configuration.getFirstFilePath());
        System.out.println(configuration.getSecondFilePath());
        System.out.println(configuration.getCompareKeysArrayPath());
        System.out.println(configuration.getCompareKeys());
        System.out.println(configuration.getNullAsNotEqual());
        System.out.println(configuration.getShowFullyMatched());
        System.out.println(configuration.getShowPartialMatched());
        System.out.println(configuration.getShowNotMatched());
        System.out.println(configuration.getShowOnlyCompareKeys());
        System.out.println(configuration.getOpenResultAfterCompare());
        System.out.println(configuration.getLeftIndentsInObject());
        System.out.println(configuration.getAddRowNumber());
        System.out.println(configuration.getAddCommaBetweenObjects());
        System.out.println(configuration.getJsonComparatorVersion());
    }
}
