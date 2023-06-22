package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.file.Path;
import java.util.*;

public class JSONComparator {
    private final Configuration configuration;
    private final Path firstJSON;
    private final Path secondJSON;
    private final List<JSONObject> firstList = new ArrayList<>();
    private final List<JSONObject> secondList = new ArrayList<>();
    private final List<JSONObject> matchedResult = new ArrayList<>();
    private final List<JSONObject> halfMatchedResult = new ArrayList<>();
    private final List<JSONObject> notMatchedResult = new ArrayList<>();
    private final List<JSONObject> firstListStock = new ArrayList<>();
    private final List<JSONObject> secondListStock = new ArrayList<>();
    private int firstListSize;
    private int secondListSize;

    public JSONComparator() {
        configuration = new Configuration();
        firstJSON = Path.of(configuration.getFirstFilePath());
        secondJSON = Path.of(configuration.getSecondFilePath());
        fillFirstJSONList();
        fillSecondJSONList();
        // TODO: добавить повторное сравнение для поиска полного совпадения среди частично совпавших и перенос их в полностью совпавшие
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
        JSONObject rootJSON = new JSONObject(jsonText);
        JSONArray values = getJSONArrayByPath(pathToJSONArray, rootJSON);
        for (Object value : values) {
            listForJSONObjects.add((JSONObject) value);
        }
    }

    private JSONArray getJSONArrayByPath(String pathToJSONArray, JSONObject json) {
        JSONArray values = null;

        if (pathToJSONArray == null || pathToJSONArray.equals("")) {
            // TODO: (config.json -> compareKeysArrayPath) вынести в константу
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
        compareTwoList(firstList, secondList);
    }

    /**
     * Метод позволяет сравнить 2 коллекции JSONObject на предмет совпадения по указанным полям в настройках.
     *
     * @param mainList    Основной список для сравнения
     * @param compareList Список объектов, которые будут сравниваться с основным списком
     */
    private void compareTwoList(List<JSONObject> mainList, List<JSONObject> compareList) {
        boolean nullAsNotEqual = configuration.getNullAsNotEqual();
        boolean fullEquals = false;
        boolean halfEquals = false;
        for (JSONObject mainObj : mainList) {
            if (compareList.size() == 0) {
                firstListStock.add(mainObj);
                continue;
            }

            for (JSONObject compareObj : compareList) {
                fullEquals = true;
                halfEquals = false;
                for (String key : configuration.getCompareKeys()) {
                    try {
                        if (nullAsNotEqual && (mainObj.isNull(key) || compareObj.isNull(key))) {
                            fullEquals = false;
                            continue;
                        } else if (mainObj.get(key).equals(compareObj.get(key))) {
                            if (!halfEquals) {
                                halfEquals = true;
                            }
                        } else {
                            fullEquals = false;
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException("В сравниваемых файлах не найден ключ \"" + key + "\".");
                    }
                }

                if (fullEquals || halfEquals) {
                    compareList.remove(compareObj);
                    break;
                }
            }

            if (fullEquals) {
                matchedResult.add(mainObj);
            } else if (halfEquals) {
                halfMatchedResult.add(mainObj);
            } else {
                notMatchedResult.add(mainObj);
            }
        }

        if (compareList.size() > 0) {
            secondListStock.addAll(compareList);
        }
    }

    public int getFirstListSize() {
        return firstListSize;
    }

    public int getSecondListSize() {
        return secondListSize;
    }

    public List<JSONObject> getFirstListStock() {
        return new ArrayList<>(firstListStock);
    }

    public List<JSONObject> getSecondListStock() {
        return new ArrayList<>(secondListStock);
    }

    public Path getFirstJSON() {
        return firstJSON;
    }

    public Path getSecondJSON() {
        return secondJSON;
    }

    public List<JSONObject> getFirstList() {
        return firstList;
    }

    public List<JSONObject> getSecondList() {
        return secondList;
    }

    public List<JSONObject> getMatchedResult() {
        return matchedResult;
    }

    public List<JSONObject> getHalfMatchedResult() {
        return halfMatchedResult;
    }

    public List<JSONObject> getNotMatchedResult() {
        return notMatchedResult;
    }
}
