package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONComparator {
    private List<JSONObject> firstList = new ArrayList<>();
    private List<JSONObject> secondList = new ArrayList<>();
    private List<JSONObject> matchedResult = new ArrayList<>();
    private List<JSONObject> halfMatchedResult = new ArrayList<>();
    private List<JSONObject> notMatchedResult = new ArrayList<>();
    private List<JSONObject> firstListStock = new ArrayList<>();
    private List<JSONObject> secondListStock = new ArrayList<>();
    private int firstListSize;
    private int secondListSize;
    private boolean isFilesSwapped;

    public static void main(String[] args) {
        // TODO: 1. Дополнить config.json файлом.
        // TODO: 2. Получать пути к файлам из config.json
        // TODO: 3. Получать список объектов
        // TODO: 4. Получать список ключей для сравнения
        // TODO: 5. Сохранять результат в файл.
        // TODO: 6. Добавить дату и время сравнения, md5 сумм файлов.

        JSONComparator comparator = new JSONComparator();
        comparator.fillFirstJSONList("jsonForCompare/test2.json");
        comparator.fillSecondJSONList("jsonForCompare/test.json");
        comparator.compare();
        comparator.printResult();
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
     * @param mainList
     * @param compareList
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
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }
    }

    private String getNullableParam(JSONObject object, String name) {
        String value; // TODO: тоже надо переписать на соответствующий тип

        if (object.isNull(name)) {
            value = "";
        } else {
            value = (String) object.get(name);
        }

        return value;
    }

    private void fillFirstJSONList(String path) {
        fillJSONList(firstList, path);
        firstListSize = firstList.size();
    }

    private void fillSecondJSONList(String path) {
        fillJSONList(secondList, path);
        secondListSize = secondList.size();
    }

    private void fillJSONList(List<JSONObject> list, String path) {
        String jsonText = getJSONText(path);
        JSONObject rootJSON = new JSONObject(jsonText);
        JSONArray values = (JSONArray) rootJSON.get("Values");

        for (Object value : values) {
            list.add((JSONObject) value);
        }
    }

    private String getJSONText(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    // TODO: потом доделать функционал по поиску дублей
    private List<JSONObject> copyes = new ArrayList<>();

    private void findCopy(List<JSONObject> mainList) {
        List<JSONObject> compareList = new ArrayList<>(mainList);

        for (JSONObject mainObj : mainList) {
            String sNm = (String) mainObj.get("sNm");
            String sKey = (String) mainObj.get("sKey");
            String dPeriodTo = getNullableParam(mainObj, "dPeriodTo");

            if (compareList.size() > 0) {
                compareList.remove(0); // Убираем себя

                for (JSONObject compareObj : compareList) {
                    boolean nameEquals = compareObj.get("sNm").equals(sNm);
                    boolean keyEquals = compareObj.get("sKey").equals(sKey);
                    boolean periodEquals = getNullableParam(compareObj, "dPeriodTo").equals(dPeriodTo);

                    if (nameEquals && keyEquals && periodEquals) {
                        copyes.add(compareObj);
                        compareList.remove(compareObj); // убрать из списка, т.к. уже нашли его пару
                        break; // выйти из внутреннего цикла
                    }
                }
            }
        }

        if (copyes.size() > 0) {
            int rowNumber = 0;
            System.out.println("\tКопии элеметнов:");
            for (JSONObject object : copyes) {
                System.out.println(++rowNumber + ") sKey:" + object.get("sKey") + ", sNm: \"" + object.get("sNm") + "\"");
            }
        }
    }
}
