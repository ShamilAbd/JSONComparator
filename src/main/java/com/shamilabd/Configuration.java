package com.shamilabd;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class Configuration {
    private String firstFilePath;
    private String secondFilePath;
    private String compareObjectListPath;
    private byte jsonComparatorVersion;
    private Set<String> compareKeys;

    public Configuration() {
        // TODO: нужно все проинициализировать
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

    public byte getJsonComparatorVersion() {
        return jsonComparatorVersion;
    }

    public Set<String> getCompareKeys() {
        return Set.copyOf(compareKeys);
    }

    private void createConfigExample() {
        // TODO: если нет конфига, то создать шаблон
    }
}
