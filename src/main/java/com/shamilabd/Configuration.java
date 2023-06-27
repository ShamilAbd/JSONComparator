package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private final JSONObject rootJSON;
    private String firstFilePath;
    private String secondFilePath;
    private String compareKeysArrayPath;
    private Integer configFileVersion;
    private Boolean nullAsNotEqual;
    private Boolean showFullyMatched;
    private Boolean showPartialMatched;
    private Boolean showNotMatched;
    private Boolean showOnlyCompareKeys;
    private Boolean openResultAfterCompare;
    private Boolean addRowNumber;
    private Boolean addCommaBetweenObjects;
    private Boolean findDuplicatesInFiles;
    private int leftIndentsInObject;
    private final int currentJsonComparatorVersion = 1;
    private final List<String> compareKeys = new ArrayList<>();

    public Configuration() throws IOException {
        File config = new File("config.json");
        if (!config.exists()) {
            createConfigExample(config.getAbsolutePath());
            throw new RuntimeException("В папке был создан config.json файл, заполните его и запустите программу снова.");
        }
        rootJSON = new JSONObject(Utils.readFile(config.getAbsolutePath()));
        checkVersionCompatibility();
        loadParameters();
    }

    private void createConfigExample(String path) { // TODO: обновить перед релизом и добавить файл readme с описанием по заполнению
        String text = """
              {
                "firstFilePath" : "C:\\\\Work\\\\test.json or C:/Work/test.json or test.json - for relative path",
                "secondFilePath" : "test2.json",
                "compareKeysArrayPath" : "KeyName1.KeyName2.KeyName3 or KeyName or just empty string",
                "compareKeys" : ["name", "intValue", "floatValue", "date"],
                "nullAsNotEqual" : false,
                "showFullyMatched" : true,
                "showPartialMatched" : true,
                "showNotMatched" : true,
                "showOnlyCompareKeys" : false,
                "openResultAfterCompare" : true,
                "addRowNumber" : false,
                "addCommaBetweenObjects" : true,
                "findDuplicatesInFiles" : true,
                "leftIndentsInObject" : 2,
                "configFileVersion" : 1
              }""";
        Utils.saveInFile(path, text);
    }

    private void checkVersionCompatibility() {
        if (rootJSON.has("configFileVersion")) {
            configFileVersion = (Integer) rootJSON.get("configFileVersion");
        } else {
            throw new RuntimeException("В файле config.json не заполнен ключ \"configFileVersion\".");
        }

        if (configFileVersion != currentJsonComparatorVersion) {
            throw new RuntimeException("Версия config.json не соответствует версии программы:\n"
                    + "Версия программы: " + currentJsonComparatorVersion + "\n"
                    + "Версия файла: " + configFileVersion);
        }
    }

    private void loadParameters() {
        loadCompareKeys();

        firstFilePath = rootJSON.getString("firstFilePath");
        secondFilePath = rootJSON.getString("secondFilePath");
        if (rootJSON.isNull("compareKeysArrayPath")){
            compareKeysArrayPath = "";
        } else {
            compareKeysArrayPath = rootJSON.getString("compareKeysArrayPath");
        }
        nullAsNotEqual = rootJSON.getBoolean("nullAsNotEqual");
        showFullyMatched = rootJSON.getBoolean("showFullyMatched");
        showPartialMatched = rootJSON.getBoolean("showPartialMatched");
        showNotMatched = rootJSON.getBoolean("showNotMatched");
        showOnlyCompareKeys = rootJSON.getBoolean("showOnlyCompareKeys");
        addRowNumber = rootJSON.getBoolean("addRowNumber");
        addCommaBetweenObjects = rootJSON.getBoolean("addCommaBetweenObjects");
        openResultAfterCompare = rootJSON.getBoolean("openResultAfterCompare");
        findDuplicatesInFiles = rootJSON.getBoolean("findDuplicatesInFiles");
        leftIndentsInObject = rootJSON.getInt("leftIndentsInObject");
    }

    private void loadCompareKeys() {
        RuntimeException notFillCompareKeys = new RuntimeException("Не заполнен массив с ключами для сранения объектов (config.json -> \"compareKeys\").");
        if (rootJSON.isNull("compareKeys")) {
            throw notFillCompareKeys;
        }
        JSONArray values = (JSONArray) rootJSON.get("compareKeys");
        if (values.isEmpty()) {
            throw notFillCompareKeys;
        }
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

    public int getConfigFileVersion() {
        return configFileVersion;
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

    public Boolean getFindDuplicatesInFiles() {
        return findDuplicatesInFiles;
    }

    public int getCurrentJsonComparatorVersion() {
        return currentJsonComparatorVersion;
    }

    public static void main(String[] args) throws IOException {
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
        System.out.println(configuration.getConfigFileVersion());
        System.out.println(configuration.getFindDuplicatesInFiles());
        System.out.println(configuration.getCurrentJsonComparatorVersion());
    }
}
