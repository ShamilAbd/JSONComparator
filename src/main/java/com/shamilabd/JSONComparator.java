package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONComparator {
    private final Configuration configuration;
    private String firstJSON;
    private String secondJSON;
    private final List<JSONObject> firstList = new ArrayList<>();
    private final List<JSONObject> secondList = new ArrayList<>();
    private final Set<JSONObject> matchedFirst = new HashSet<>();
    private final Set<JSONObject> matchedSecond = new HashSet<>();
    private final Set<JSONObject> halfMatchedFirst = new HashSet<>();
    private final Set<JSONObject> halfMatchedSecond = new HashSet<>();
    private final List<JSONObject> notMatchedFirst = new ArrayList<>();
    private final List<JSONObject> notMatchedSecond = new ArrayList<>();
    private final Set<JSONObject> firstFileDuplicates = new HashSet<>();
    private final Set<JSONObject> secondFileDuplicates = new HashSet<>();
    private int firstListSize;
    private int secondListSize;

    public static void main(String[] args) {
        Configuration configuration;
        try {
            configuration = new Configuration();
            JSONComparator comparator = new JSONComparator(configuration);
            comparator.compare();
            comparator.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONComparator(Configuration configuration) {
        this.configuration = configuration;
    }

    public void compare() throws IOException {
        checkBeforeFillLists();
        firstJSON = new File(configuration.getFirstFilePath()).getAbsolutePath();
        secondJSON = new File(configuration.getSecondFilePath()).getAbsolutePath();
        fillFirstJSONList();
        fillSecondJSONList();
        checkBeforeCompare();
        if (configuration.getFindDuplicatesInFiles()) {
            findDuplicates(firstList, firstFileDuplicates);
            findDuplicates(secondList, secondFileDuplicates);
        }
        findFullMatch();
        findHalfMatch();
        findHalfMatchInOtherList(notMatchedFirst, halfMatchedSecond, halfMatchedFirst);
        findHalfMatchInOtherList(notMatchedSecond, halfMatchedFirst, halfMatchedSecond);
    }

    private void checkBeforeFillLists() {
        if (configuration.getFirstFilePath().trim().equals("")) {
            throw new RuntimeException("Не указан путь к первому сравниваемому файлу.");
        }
        if (configuration.getSecondFilePath().trim().equals("")) {
            throw new RuntimeException("Не указан путь к второму сравниваемому файлу.");
        }
    }

    private void checkBeforeCompare() {
        if (configuration.getCompareKeys().size() == 0) {
            throw new RuntimeException("Не заполнен массив с ключами для сравнения объектов.");
        }
        if (firstList.size() == 0) {
            throw new RuntimeException("В первом файле нет объектов для сравнения.");
        }
        if (secondList.size() == 0) {
            throw new RuntimeException("Во втором файле нет объектов для сравнения.");
        }
        for (String key : configuration.getCompareKeys()) {
            if (!firstList.get(0).has(key)) {
                throw new RuntimeException("В объекте сравнения отсутствует ключ \"" + key + "\"");
            }
        }
    }

    public void clear() {
        firstList.clear();
        secondList.clear();
        matchedFirst.clear();
        matchedSecond.clear();
        halfMatchedFirst.clear();
        halfMatchedSecond.clear();
        notMatchedFirst.clear();
        notMatchedSecond.clear();
        firstFileDuplicates.clear();
        secondFileDuplicates.clear();
        firstJSON = null;
        secondJSON = null;
        firstListSize = 0;
        secondListSize = 0;
    }

    private void fillFirstJSONList() throws IOException {
        fillJSONList(configuration.getFirstFilePath(), configuration.getCompareKeysArrayPath(), firstList);
        firstListSize = firstList.size();
    }

    private void fillSecondJSONList() throws IOException {
        fillJSONList(configuration.getSecondFilePath(), configuration.getCompareKeysArrayPath(), secondList);
        secondListSize = secondList.size();
    }

    public void fillJSONList(String jsonFilePath, String pathToJSONArray, List<JSONObject> listForJSONObjects) throws IOException {
        String jsonText = Utils.readFile(jsonFilePath);
        JSONArray values;
        try {
            if (configuration.getCompareKeysArrayPath().equals("")) {
                values = new JSONArray(jsonText);
            } else {
                JSONObject rootJSON = new JSONObject(jsonText);
                values = getJSONArrayByPath(pathToJSONArray, rootJSON);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("В строке \"Путь до сравниваемых объектов\" указан путь не до массива объектов.");
        }
        for (Object value : values) {
            listForJSONObjects.add((JSONObject) value);
        }
    }

    private JSONArray getJSONArrayByPath(String pathToJSONArray, JSONObject json) {
        JSONArray values = null;

        if (pathToJSONArray == null || pathToJSONArray.equals("")) {
            throw new RuntimeException("Не указан ключ объекта, внутри которого лежит сравниваемый массив.");
        } else if (pathToJSONArray.equals(".")) {
            throw new RuntimeException("Ключ объекта не может равняться точке.");
        } else if (!pathToJSONArray.contains(".")) {
            if (!json.has(pathToJSONArray)) {
                throw new RuntimeException("Ключ \"" + pathToJSONArray + "\" не найден в файле json.");
            }
            try {
                values = (JSONArray) json.get(pathToJSONArray);
            } catch (ClassCastException e) {
                throw new RuntimeException("В качестве пути к массиву объектов указан ключ, не содержащий массив.");
            }
        } else {
            String[] pathsToJSONArray = pathToJSONArray.split("\\.");
            JSONObject subJson = json;

            for (int i = 0; i < pathsToJSONArray.length; i++) {
                if (i != pathsToJSONArray.length - 1) {
                    subJson = (JSONObject) subJson.get(pathsToJSONArray[i]);
                } else {
                    values = (JSONArray) subJson.get(pathsToJSONArray[i]);
                }
            }
        }
        return values;
    }

    private void findDuplicates(List<JSONObject> objects, Set<JSONObject> duplicates) {
        for (int i = 0; i < objects.size(); i++) {
            JSONObject object = objects.get(i);
            for (int j = i + 1; j < objects.size(); j++) {
                JSONObject object2 = objects.get(j);
                if (object.toString().equals(object2.toString())) {
                    duplicates.add(object);
                    duplicates.add(object2);
                }
            }
        }
    }

    private void findFullMatch() {
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
        boolean ignoreCase = configuration.getIgnoreCase();
        boolean trimText = configuration.getTrimText();

        for (JSONObject first : firstList) {
            SECOND_LIST:
            for (int j = 0; j < secondList.size(); j++) {
                JSONObject second = secondList.get(j);
                for (String key : configuration.getCompareKeys()) {
                    if (nullAsNotEqual && (first.isNull(key) || second.isNull(key))) {
                        continue SECOND_LIST;
                    }
                    if (first.get(key) instanceof String && second.get(key) instanceof String) {
                        String firstVal = (String) first.get(key);
                        String secondVal = (String) second.get(key);
                        if (trimText) {
                            firstVal = firstVal.trim();
                            secondVal = secondVal.trim();
                        }
                        if (ignoreCase) {
                            firstVal = firstVal.toUpperCase();
                            secondVal = secondVal.toUpperCase();
                        }
                        if (!firstVal.equals(secondVal)) {
                            continue SECOND_LIST;
                        }
                    } else if (!first.get(key).equals(second.get(key))) {
                        continue SECOND_LIST;
                    }
                }
                matchedFirst.add(first);
                matchedSecond.add(second);
                secondList.remove(second);
            }
        }
        firstList.removeAll(matchedFirst);
    }

    private void findHalfMatch() {
        boolean halfEquals;

        List<JSONObject> firstListCopy = new ArrayList<>(firstList);
        for (JSONObject mainObj : firstListCopy) {
            if (secondList.size() == 0) {
                break;
            }

            List<JSONObject> secondListCopy = new ArrayList<>(secondList);
            for (JSONObject compareObj : secondListCopy) {
                halfEquals = false;
                for (String key : configuration.getCompareKeys()) {
                    try {
                        halfEquals = isHalfEquals(halfEquals, mainObj, compareObj, key);
                    } catch (JSONException e) {
                        throw new RuntimeException("В сравниваемых файлах не найден ключ \"" + key + "\".");
                    }
                }
                if (halfEquals) {
                    halfMatchedFirst.add(mainObj);
                    halfMatchedSecond.add(compareObj);
                    secondList.remove(compareObj);
                    firstList.remove(mainObj);
                }
            }
        }

        if (firstList.size() > 0) {
            notMatchedFirst.addAll(firstList);
        }

        if (secondList.size() > 0) {
            notMatchedSecond.addAll(secondList);
        }
    }

    private boolean isHalfEquals(boolean halfEquals, JSONObject mainObj, JSONObject compareObj, String key) {
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
        boolean ignoreCase = configuration.getIgnoreCase();
        boolean trimText = configuration.getTrimText();

        if (nullAsNotEqual) {
            if (!mainObj.isNull(key) && !compareObj.isNull(key)) {
                if (mainObj.get(key) instanceof String && compareObj.get(key) instanceof String) {
                    String firstVal = (String) mainObj.get(key);
                    String secondVal = (String) compareObj.get(key);
                    if (trimText) {
                        firstVal = firstVal.trim();
                        secondVal = secondVal.trim();
                    }
                    if (ignoreCase) {
                        firstVal = firstVal.toUpperCase();
                        secondVal = secondVal.toUpperCase();
                    }
                    if (firstVal.equals(secondVal)) {
                        if (!halfEquals) {
                            halfEquals = true;
                        }
                    }
                } else if (mainObj.get(key).equals(compareObj.get(key))) {
                    if (!halfEquals) {
                        halfEquals = true;
                    }
                }
            }
        } else if (mainObj.get(key) instanceof String && compareObj.get(key) instanceof String) {
            String firstVal = (String) mainObj.get(key);
            String secondVal = (String) compareObj.get(key);
            if (trimText) {
                firstVal = firstVal.trim();
                secondVal = secondVal.trim();
            }
            if (ignoreCase) {
                firstVal = firstVal.toUpperCase();
                secondVal = secondVal.toUpperCase();
            }
            if (firstVal.equals(secondVal)) {
                if (!halfEquals) {
                    halfEquals = true;
                }
            }
        } else if (mainObj.get(key).equals(compareObj.get(key))) {
            if (!halfEquals) {
                halfEquals = true;
            }
        }
        return halfEquals;
    }

    private void findHalfMatchInOtherList(List<JSONObject> notMatchedMain, Set<JSONObject>halfMatchedSecond, Set<JSONObject> halfMatchedMain) {
        boolean halfEquals;

        List<JSONObject> notMatchedFirstCopy = new ArrayList<>(notMatchedMain);
        for (JSONObject mainObj : notMatchedFirstCopy) {
            if (halfMatchedMain.size() == 0) {
                break;
            }

            List<JSONObject> halfMatchedFirstCopy = new ArrayList<>(halfMatchedSecond);
            for (JSONObject compareObj : halfMatchedFirstCopy) {
                halfEquals = false;
                for (String key : configuration.getCompareKeys()) {
                    halfEquals = isHalfEquals(halfEquals, mainObj, compareObj, key);
                }
                if (halfEquals) {
                    halfMatchedMain.add(mainObj);
                    notMatchedMain.remove(mainObj);
                }
            }
        }
    }

    public String getFirstJSON() {
        return firstJSON;
    }

    public String getSecondJSON() {
        return secondJSON;
    }

    public Set<JSONObject> getMatchedFirst() {
        return matchedFirst;
    }

    public Set<JSONObject> getMatchedSecond() {
        return matchedSecond;
    }

    public Set<JSONObject> getHalfMatchedFirst() {
        return halfMatchedFirst;
    }

    public Set<JSONObject> getHalfMatchedSecond() {
        return halfMatchedSecond;
    }

    public List<JSONObject> getNotMatchedFirst() {
        return notMatchedFirst;
    }

    public List<JSONObject> getNotMatchedSecond() {
        return notMatchedSecond;
    }

    public int getFirstListSize() {
        return firstListSize;
    }

    public int getSecondListSize() {
        return secondListSize;
    }

    public Set<JSONObject> getFirstFileDuplicates() {
        return firstFileDuplicates;
    }

    public Set<JSONObject> getSecondFileDuplicates() {
        return secondFileDuplicates;
    }
}
