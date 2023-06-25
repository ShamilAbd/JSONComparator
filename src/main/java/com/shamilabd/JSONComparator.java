package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSONComparator {
    private final Configuration configuration;
    private final Path firstJSON;
    private final Path secondJSON;
    private final List<JSONObject> firstList = new ArrayList<>();
    private final List<JSONObject> secondList = new ArrayList<>();
    private final List<JSONObject> matchedFirst = new ArrayList<>();
    private final List<JSONObject> matchedSecond = new ArrayList<>();
    private final Set<JSONObject> halfMatchedFirst = new HashSet<>();
    private final Set<JSONObject> halfMatchedSecond = new HashSet<>();
    private final List<JSONObject> notMatchedFirst = new ArrayList<>();
    private final List<JSONObject> notMatchedSecond = new ArrayList<>();
    private int firstListSize;
    private int secondListSize;

    public JSONComparator() {
        configuration = new Configuration();
        firstJSON = Path.of(configuration.getFirstFilePath());
        secondJSON = Path.of(configuration.getSecondFilePath());
        fillFirstJSONList();
        fillSecondJSONList();
        compare();
    }

    private void fillFirstJSONList() {
        fillJSONList(configuration.getFirstFilePath(), configuration.getCompareKeysArrayPath(), firstList);
        firstListSize = firstList.size();
    }

    private void fillSecondJSONList() {
        fillJSONList(configuration.getSecondFilePath(), configuration.getCompareKeysArrayPath(), secondList);
        secondListSize = secondList.size();
    }

    public void fillJSONList(String jsonFilePath, String pathToJSONArray, List<JSONObject> listForJSONObjects) {
        String jsonText = Utils.readFile(jsonFilePath);
        JSONArray values;
        if (configuration.getCompareKeysArrayPath().equals("")) {
            values = new JSONArray(jsonText);
        } else {
            JSONObject rootJSON = new JSONObject(jsonText);
            values = getJSONArrayByPath(pathToJSONArray, rootJSON);
        }
        for (Object value : values) {
            listForJSONObjects.add((JSONObject) value);
        }
    }

    private JSONArray getJSONArrayByPath(String pathToJSONArray, JSONObject json) {
        JSONArray values = null;

        if (pathToJSONArray == null || pathToJSONArray.equals("")) {
            throw new RuntimeException("Не указан ключ объекта (config.json -> compareKeysArrayPath), внутри которого лежит сравниваемый массив.");
        } else if (pathToJSONArray.equals(".")) {
            throw new RuntimeException("Ключ объекта (config.json -> compareKeysArrayPath) не может равняться точке.");
        } else if (!pathToJSONArray.contains(".")) {
            if (!json.has(pathToJSONArray)) {
                throw new RuntimeException("Ключ \"" + pathToJSONArray + "\" не найден в файле json.");
            }
            try {
                values = (JSONArray) json.get(pathToJSONArray);
            } catch (ClassCastException e) {
                throw new RuntimeException("В качестве пути к массиву объектов (config.json -> compareKeysArrayPath) указан ключ, не содержащий массив.");
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

    private void compare() {
        findFullMatch();
        findHalfMatch();
        findHalfMatchInOtherList(notMatchedFirst, halfMatchedSecond, halfMatchedFirst);
        findHalfMatchInOtherList(notMatchedSecond, halfMatchedFirst, halfMatchedSecond);
    }

    private void findFullMatch() {
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
        for (JSONObject first : firstList) {
            SECOND_LIST:
            for (int j = 0; j < secondList.size(); j++) {
                JSONObject second = secondList.get(j);
                for (String key : configuration.getCompareKeys()) {
                    if ((nullAsNotEqual && (first.isNull(key) || second.isNull(key)))
                            || (!first.get(key).equals(second.get(key)))) {
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
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
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
                        if (nullAsNotEqual) {
                            if (!mainObj.isNull(key) && !compareObj.isNull(key) && mainObj.get(key).equals(compareObj.get(key))) {
                                if (!halfEquals) {
                                    halfEquals = true;
                                }
                            }
                        } else if (mainObj.get(key).equals(compareObj.get(key))) {
                            if (!halfEquals) {
                                halfEquals = true;
                            }
                        }
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

    private void findHalfMatchInOtherList(List<JSONObject> notMatchedMain, Set<JSONObject>halfMatchedSecond, Set<JSONObject> halfMatchedMain) {
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
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
                    if (nullAsNotEqual) {
                        if (!mainObj.isNull(key) && !compareObj.isNull(key) && mainObj.get(key).equals(compareObj.get(key))) {
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
                if (halfEquals) {
                    halfMatchedMain.add(mainObj);
                    notMatchedMain.remove(mainObj);
                }
            }
        }
    }

    public Path getFirstJSON() {
        return firstJSON;
    }

    public Path getSecondJSON() {
        return secondJSON;
    }

    public List<JSONObject> getMatchedFirst() {
        return matchedFirst;
    }

    public List<JSONObject> getMatchedSecond() {
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
}
