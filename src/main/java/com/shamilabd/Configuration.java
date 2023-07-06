package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private static Configuration configuration;
    private final String configFileName = "config.json";
    private final String currentJsonComparatorVersion = "1.2";
    private final File config = new File(configFileName);
    private final JSONObject rootJSON;
    private String firstFilePath;
    private String secondFilePath;
    private String compareKeysArrayPath;
    private List<String> compareKeys = new ArrayList<>();
    private String fontName;
    private String charsetName;
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

    private Configuration() throws IOException {
        if (!config.exists()) {
            createConfigExample();
        }
        try {
            rootJSON = new JSONObject(Utils.readFile(config.getAbsolutePath(), StandardCharsets.UTF_8));
        } catch (JSONException e) {
            throw new RuntimeException("Ошибка в заполнении \"" + configFileName + "\": " + e.getMessage());
        }
        checkConfigVersionCompatibility();
        loadParameters();
    }

    public static Configuration getInstance() throws IOException {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    public static void main(String[] args) throws IOException {
        // For simple test
        Configuration configuration = new Configuration();
        System.out.println("ConfigFileName : " + configuration.getConfigFileName());
        System.out.println("FirstFilePath : " + configuration.getFirstFilePath());
        System.out.println("SecondFilePath : " + configuration.getSecondFilePath());
        System.out.println("CompareKeysArrayPath : " + configuration.getCompareKeysArrayPath());
        System.out.println("CompareKeys() : " + configuration.getCompareKeys());
        System.out.println("NullAsNotEqual : " + configuration.getNullAsNotEqual());
        System.out.println("ShowFullyMatched : " + configuration.getShowFullyMatched());
        System.out.println("ShowPartialMatched : " + configuration.getShowPartialMatched());
        System.out.println("ShowNotMatched : " + configuration.getShowNotMatched());
        System.out.println("ShowOnlyCompareKeys : " + configuration.getShowOnlyCompareKeys());
        System.out.println("OpenResultAfterCompare : " + configuration.getOpenResultAfterCompare());
        System.out.println("AddRowNumber : " + configuration.getAddRowNumber());
        System.out.println("AddCommaBetweenObjects : " + configuration.getAddCommaBetweenObjects());
        System.out.println("ConfigFileVersion : " + configuration.getConfigFileVersion());
        System.out.println("FindDuplicatesInFiles : " + configuration.getFindDuplicatesInFiles());
        System.out.println("IgnoreCase : " + configuration.getIgnoreCase());
        System.out.println("TrimText : " + configuration.getTrimText());
        System.out.println("CurrentJsonComparatorVersion : " + configuration.getCurrentJsonComparatorVersion());
        System.out.println("LeftIndentsInObject : " + configuration.getLeftIndentsInObject());
        System.out.println("FontName : " + configuration.getFontName());
        System.out.println("CharsetName : " + configuration.getCharsetName());
    }

    private void createConfigExample() throws IOException {
        String text = "{"
                + "  \"showFullyMatched\": true,"
                + "  \"addCommaBetweenObjects\": true,"
                + "  \"configFileVersion\": \"1.2\","
                + "  \"showOnlyCompareKeys\": false,"
                + "  \"addRowNumber\": false,"
                + "  \"openResultAfterCompare\": true,"
                + "  \"trimText\": false,"
                + "  \"secondFilePath\": \"C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_2.json\","
                + "  \"leftIndentsInObject\": 2,"
                + "  \"showNotMatched\": true,"
                + "  \"fontName\": \"Yu Gothic UI Semibold\","
                + "  \"charsetName\": \"windows-1251\","
                + "  \"compareKeysArrayPath\": \"SomeKey.KeyWithArray\","
                + "  \"ignoreCase\": false,"
                + "  \"nullAsNotEqual\": false,"
                + "  \"firstFilePath\": \"C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_1.json\","
                + "  \"showPartialMatched\": true,"
                + "  \"findDuplicatesInFiles\": true,"
                + "  \"compareKeys\": [\"name\", \"key\"]"
                + "}";
        Utils.saveInFile(config.getAbsolutePath(), text, StandardCharsets.UTF_8);
    }

    private void checkConfigVersionCompatibility() {
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
        firstFilePath = rootJSON.optString("firstFilePath", "");
        secondFilePath = rootJSON.optString("secondFilePath", "");
        fontName = rootJSON.getString("fontName");
        charsetName = rootJSON.optString("charsetName", "windows-1251");
        if (rootJSON.isNull("compareKeysArrayPath")) {
            compareKeysArrayPath = "";
        } else {
            compareKeysArrayPath = rootJSON.getString("compareKeysArrayPath");
        }
        nullAsNotEqual = rootJSON.optBoolean("nullAsNotEqual", false);
        showFullyMatched = rootJSON.optBoolean("showFullyMatched", true);
        showPartialMatched = rootJSON.optBoolean("showPartialMatched", true);
        showNotMatched = rootJSON.optBoolean("showNotMatched", true);
        showOnlyCompareKeys = rootJSON.optBoolean("showOnlyCompareKeys", false);
        addRowNumber = rootJSON.optBoolean("addRowNumber", false);
        addCommaBetweenObjects = rootJSON.optBoolean("addCommaBetweenObjects", true);
        openResultAfterCompare = rootJSON.optBoolean("openResultAfterCompare", true);
        findDuplicatesInFiles = rootJSON.optBoolean("findDuplicatesInFiles", true);
        ignoreCase = rootJSON.optBoolean("ignoreCase", false);
        trimText = rootJSON.optBoolean("trimText", false);
        leftIndentsInObject = rootJSON.optInt("leftIndentsInObject", 2);
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
        return new ArrayList<>(compareKeys);
    }

    public String getCompareKeysForPrint() {
        StringBuilder builder = new StringBuilder();
        int listSize = compareKeys.size();

        for (int i = 0; i < listSize; i++) {
            builder.append("\"");
            builder.append(compareKeys.get(i));
            builder.append("\"");

            if (i < listSize - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
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

    public String getConfigFileName() {
        return configFileName;
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
        rootJSON.put("charsetName", charsetName);
        rootJSON.put("fontName", fontName);
        Utils.saveInFile(configFileName, rootJSON.toString(2), StandardCharsets.UTF_8);
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
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
        this.compareKeys = new ArrayList<>(compareKeys);
    }

    public String getFontName() {
        return fontName;
    }
}
