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
    private String fontName;
    private String configFileVersion;
    private Boolean nullAsNotEqual;
    private Boolean showFullyMatched;
    private Boolean showPartialMatched;
    private Boolean showNotMatched;
    private Boolean showOnlyCompareKeys;
    private Boolean openResultAfterCompare;
    private Boolean addRowNumber;
    private Boolean addCommaBetweenObjects;
    private Boolean findDuplicatesInFiles;
    private Boolean ignoreCase;
    private Boolean trimText;
    private int leftIndentsInObject;
    private final String currentJsonComparatorVersion = "1.0";
    private List<String> compareKeys = new ArrayList<>();
    public final String configFileName = "config.json";

    public Configuration() throws IOException {
        File config = new File(configFileName);
        if (!config.exists()) {
            createConfigExample(config.getAbsolutePath());
        }
        rootJSON = new JSONObject(Utils.readFile(config.getAbsolutePath()));
        checkVersionCompatibility();
        loadParameters();
    }

    private void createConfigExample(String path) throws IOException {
        String text = """
                {
                  "showFullyMatched": true,
                  "addCommaBetweenObjects": true,
                  "configFileVersion": "1.0",
                  "showOnlyCompareKeys": false,
                  "addRowNumber": false,
                  "openResultAfterCompare": true,
                  "trimText": false,
                  "secondFilePath": "C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_2.json",
                  "leftIndentsInObject": 2,
                  "showNotMatched": true,
                  "fontName": "Yu Gothic UI Semibold",
                  "compareKeysArrayPath": "SomeKey.KeyWithArray",
                  "ignoreCase": false,
                  "nullAsNotEqual": false,
                  "firstFilePath": "C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_1.json",
                  "showPartialMatched": true,
                  "findDuplicatesInFiles": true,
                  "compareKeys": ["name", "key", "version"]
                }""";
        Utils.saveInFile(path, text);
    }

    private void checkVersionCompatibility() {
        if (rootJSON.has("configFileVersion")) {
            configFileVersion = (String) rootJSON.get("configFileVersion");
        } else {
            throw new RuntimeException("В файле config.json не заполнен ключ \"configFileVersion\".");
        }

        if (!configFileVersion.equals(currentJsonComparatorVersion)) {
            throw new RuntimeException("Версия config.json не соответствует версии программы:\n"
                    + "Версия программы: " + currentJsonComparatorVersion + "\n"
                    + "Версия файла: " + configFileVersion);
        }
    }

    private void loadParameters() {
        loadCompareKeys();

        firstFilePath = rootJSON.getString("firstFilePath");
        secondFilePath = rootJSON.getString("secondFilePath");
        fontName = rootJSON.getString("fontName");
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
        ignoreCase = rootJSON.getBoolean("ignoreCase");
        trimText = rootJSON.getBoolean("trimText");
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

    public String getConfigFileVersion() {
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

    public String getCurrentJsonComparatorVersion() {
        return currentJsonComparatorVersion;
    }

    public Boolean getIgnoreCase() {
        return ignoreCase;
    }

    public Boolean getTrimText() {
        return trimText;
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
        System.out.println(configuration.getIgnoreCase());
        System.out.println(configuration.getTrimText());
        System.out.println(configuration.getCurrentJsonComparatorVersion());
    }

    public void saveConfig() throws IOException {
        rootJSON.put("firstFilePath", firstFilePath);
        rootJSON.put("secondFilePath", secondFilePath);
        rootJSON.put("compareKeysArrayPath", compareKeysArrayPath);
        rootJSON.put("compareKeys", compareKeys);
        rootJSON.put("nullAsNotEqual", nullAsNotEqual);
        rootJSON.put("showFullyMatched", showFullyMatched);
        rootJSON.put("showPartialMatched", showPartialMatched);
        rootJSON.put("showNotMatched", showNotMatched);
        rootJSON.put("showOnlyCompareKeys", showOnlyCompareKeys);
        rootJSON.put("openResultAfterCompare", openResultAfterCompare);
        rootJSON.put("addRowNumber", addRowNumber);
        rootJSON.put("addCommaBetweenObjects", addCommaBetweenObjects);
        rootJSON.put("findDuplicatesInFiles", findDuplicatesInFiles);
        rootJSON.put("ignoreCase", ignoreCase);
        rootJSON.put("trimText", trimText);
        rootJSON.put("leftIndentsInObject", leftIndentsInObject);
        rootJSON.put("configFileVersion", configFileVersion);
        Utils.saveInFile(configFileName , rootJSON.toString(2));
    }

    public void setFirstFilePath(String firstFilePath) {
        this.firstFilePath = firstFilePath;
    }

    public void setSecondFilePath(String secondFilePath) {
        this.secondFilePath = secondFilePath;
    }

    public void setCompareKeysArrayPath(String compareKeysArrayPath) {
        this.compareKeysArrayPath = compareKeysArrayPath;
    }

    public void setNullAsNotEqual(Boolean nullAsNotEqual) {
        this.nullAsNotEqual = nullAsNotEqual;
    }

    public void setShowFullyMatched(Boolean showFullyMatched) {
        this.showFullyMatched = showFullyMatched;
    }

    public void setShowPartialMatched(Boolean showPartialMatched) {
        this.showPartialMatched = showPartialMatched;
    }

    public void setShowNotMatched(Boolean showNotMatched) {
        this.showNotMatched = showNotMatched;
    }

    public void setShowOnlyCompareKeys(Boolean showOnlyCompareKeys) {
        this.showOnlyCompareKeys = showOnlyCompareKeys;
    }

    public void setOpenResultAfterCompare(Boolean openResultAfterCompare) {
        this.openResultAfterCompare = openResultAfterCompare;
    }

    public void setAddRowNumber(Boolean addRowNumber) {
        this.addRowNumber = addRowNumber;
    }

    public void setAddCommaBetweenObjects(Boolean addCommaBetweenObjects) {
        this.addCommaBetweenObjects = addCommaBetweenObjects;
    }

    public void setFindDuplicatesInFiles(Boolean findDuplicatesInFiles) {
        this.findDuplicatesInFiles = findDuplicatesInFiles;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public void setTrimText(Boolean trimText) {
        this.trimText = trimText;
    }

    public void setLeftIndentsInObject(int leftIndentsInObject) {
        this.leftIndentsInObject = leftIndentsInObject;
    }

    public void setCompareKeys(List<String> compareKeys) {
        this.compareKeys = List.copyOf(compareKeys);
    }

    public String getFontName() {
        return fontName;
    }
}
