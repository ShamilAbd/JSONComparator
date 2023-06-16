package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JSONComparator {
    private final Configuration configuration;
    private final List<JSONObject> firstList = new ArrayList<>();
    private final List<JSONObject> secondList = new ArrayList<>();
    private final List<JSONObject> matchedResult = new ArrayList<>();
    private final List<JSONObject> halfMatchedResult = new ArrayList<>();
    private final List<JSONObject> notMatchedResult = new ArrayList<>();
    private final List<JSONObject> firstListStock = new ArrayList<>();
    private final List<JSONObject> secondListStock = new ArrayList<>();
    private int firstListSize;
    private int secondListSize;
    private boolean isFilesSwapped;

    public static void main(String[] args) {
        // TODO: 4. Получать список ключей для сравнения
        // TODO: 5. Сохранять результат в файл.
        // TODO: 6. Добавить дату и время сравнения, md5 сумм файлов.
        // TODO: 7. потом доделать функционал по поиску дублей в файлах

        JSONComparator comparator = new JSONComparator();
        comparator.fillFirstJSONList();
        comparator.fillSecondJSONList();
        comparator.compare();
        comparator.printResult();
    }

    public JSONComparator() {
        configuration = new Configuration();
    }

    private void compare() {
        if (firstList.size() >= secondList.size()) {
            compareTwoList(firstList, secondList);
        } else {
            isFilesSwapped = true;
            compareTwoList(secondList, firstList);
        }
    }

    /**
     * Метод позволяет сравнить 2 коллекции JSONObject на предмет совпадения по указанным полям в настройках.
     * @param mainList Основной список для сравнения
     * @param compareList Список объектов, которые будут сравниваться с основным списком
     */
    private void compareTwoList(List<JSONObject> mainList, List<JSONObject> compareList) {
        for (JSONObject mainObj : mainList) {
            String sNm = (String) mainObj.get("sNm");
            String sKey = (String) mainObj.get("sKey");
            String sNumberT1 = getNullableParam(mainObj, "sNumberT1");
//            String dPeriodTo = getNullableParam(mainObj, "dPeriodTo");

            if (compareList.size() == 0) {
                firstListStock.add(mainObj);
                continue;
            }

            boolean notMatched = true;
            for (JSONObject compareObj : compareList) {
                boolean nameEquals = compareObj.get("sNm").equals(sNm);
                boolean keyEquals = compareObj.get("sKey").equals(sKey);
//                boolean periodEquals = getNullableParam(compareObj, "dPeriodTo").equals(dPeriodTo);
                String sNumberT1Compare = getNullableParam(compareObj, "sNumberT1");

                if (nameEquals && keyEquals) { //  && periodEquals
                    if (sNumberT1.equals(sNumberT1Compare)) {
                        matchedResult.add(compareObj);
                    } else {
                        halfMatchedResult.add(compareObj);
                    }
                    notMatched = false;
                    compareList.remove(compareObj); // убрать из списка, т.к. уже нашли его пару
                    break; // выйти из внутреннего цикла
                }
            }
            if (notMatched) {
                notMatchedResult.add(mainObj);
            }
        }

        if (compareList.size() > 0) {
            secondListStock.addAll(compareList);
        }
    }

    private void printResult() {
        int rowNumber = 0;
        System.out.println("Элеметнов в 1 файле: " + firstListSize);
        System.out.println("Элеметнов в 2 файле: " + secondListSize);
        if (isFilesSwapped) {
            System.out.println("Во втором файле больше объектов чем в первом, потому будем сравнивать второй файл с первым.");
        }

        System.out.println();

        if (matchedResult.size() > 0) {
            System.out.println("\tСписок полностью совпавших элементов:");
            for (JSONObject object : matchedResult) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }

        if (halfMatchedResult.size() > 0) {
            System.out.println("\t------------------------------");
            System.out.println("\tСписок частично совпавших элементов:");
            for (JSONObject object : halfMatchedResult) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }

        if (notMatchedResult.size() > 0) {
            System.out.println("\t------------------------------");
            System.out.println("\tОстальные элементы бОльшого файла:");
            for (JSONObject object : notMatchedResult) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }

        if (firstListStock.size() > 0) {
            System.out.println("\t------------------------------");
            System.out.println("\tЭлементы бОльшого списка, для которых не осталось объектов для сравнения:");
            for (JSONObject object : firstListStock) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }

        if (secondListStock.size() > 0) {
            System.out.println("\t------------------------------");
            System.out.println("\tЭлементы меньшего списка, для которых не нашлось соответствий:");
            for (JSONObject object : secondListStock) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", period: " + object.get("dPeriodTo") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }
    }

    private String getNullableParam(JSONObject json, String key) {
        String value; // TODO: тоже надо переписать на соответствующий тип

        if (json.isNull(key)) {
            value = "";
        } else {
            value = (String) json.get(key);
        }

        return value;
    }

    private void fillFirstJSONList() {
        fillJSONList(configuration.getFirstFilePath(), configuration.getCompareObjectListPath(), firstList);
        firstListSize = firstList.size();
    }

    private void fillSecondJSONList() {
        fillJSONList(configuration.getSecondFilePath(), configuration.getCompareObjectListPath(), secondList);
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
            throw new RuntimeException("Не указан ключ объекта (config.json -> compareObjectListPath), внутри которого лежит сравниваемый массив.");
        } else if (pathToJSONArray.equals(".")) {
            throw new RuntimeException("Ключ объекта (config.json -> compareObjectListPath) не может равняться точке.");
        } else if (!pathToJSONArray.contains(".")) {
            if (!json.has(pathToJSONArray)) {
                throw new RuntimeException("Ключ \"" + pathToJSONArray + "\" не найден в файле json.");
            }
            try {
                values = (JSONArray) json.get(pathToJSONArray);
            } catch (ClassCastException e) { // TODO: compareObjectListPath переименовать на ...Array...
                throw new RuntimeException("В качестве пути к массиву объектов (config.json -> compareObjectListPath) указан ключ, не содержащий массив."); // TODO: (config.json -> compareObjectListPath) вынести в константу
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
}
