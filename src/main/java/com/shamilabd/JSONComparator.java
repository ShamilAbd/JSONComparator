package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
        // TODO: 6. Добавить дату и время сравнения, md5 сумм файлов.
        // TODO: 7. потом доделать функционал по поиску дублей в файлах
        // TODO: UI на свинге
        // TODO: поправить вывод результата сравнения
        // TODO: добавить в имя файла таймстемп

        JSONComparator comparator = new JSONComparator();
        comparator.fillFirstJSONList();
        comparator.fillSecondJSONList();
        comparator.compare();
        comparator.saveInHTML();
    }

    private void saveInHTML() {
        String htmlText = """
                <!Doctype html>
                <html lang="ru">
                   <head>
                     <title>JSONComparator</title>
                     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                     <meta name="Author" content="Shamil Abdullin">
                     <link rel="stylesheet" type="text/css" href="style.css">
                   </head>
                   <body>
                     <header class="header-style">
                       <h2>Сравнение JSON файлов</h2>
                       <small>Файлы:<br>\n""" + configuration.getFirstFilePath() + "<br>\n" + configuration.getSecondFilePath() + """
                       </small>
                     </header>
                     <div class="main-content">""" + printResult() + """
                     </div>
                   </body>
                </html>
                """;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("CompareResult.html"))) {
            writer.write(htmlText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String printResult() {
        String newLineTag = "<br>\r\n";
        StringBuilder builder = new StringBuilder();
        int rowNumber = 0;
        builder.append("<div class=\"files-statistic\">" + newLineTag);
        builder.append("Результаты:" + newLineTag);
        builder.append("Элементов в 1 файле: " + firstListSize + "" + newLineTag);
        builder.append("Элементов в 2 файле: " + secondListSize + "" + newLineTag);

        if (isFilesSwapped) {
            builder.append("Во втором файле больше объектов чем в первом, потому будем сравнивать второй файл с первым." + newLineTag);
        }
        builder.append("</div>\r\n");

        if (configuration.getShowFullyMatched() && matchedResult.size() > 0) {
            builder.append("<div class=\"fully-matched\">" + newLineTag);
            builder.append("Список полностью совпавших элементов:" + newLineTag);
            for (JSONObject object : matchedResult) {
//                builder.append(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"" + newLineTag);
                builder.append(getObjectRepresentation());
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowPartialMatched() && halfMatchedResult.size() > 0) {
            builder.append("<div class=\"partial-matched\">" + newLineTag);
            builder.append("Список частично совпавших элементов:" + newLineTag);
            for (JSONObject object : halfMatchedResult) {
                builder.append(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"" + newLineTag);
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && notMatchedResult.size() > 0) {
            builder.append("<div class=\"not-matched\">" + newLineTag);
            builder.append("Остальные элементы бОльшого файла:" + newLineTag);
            for (JSONObject object : notMatchedResult) {
                builder.append(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"" + newLineTag);
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && firstListStock.size() > 0) {
            builder.append("<div class=\"not-matched-1list\">" + newLineTag);
            builder.append("Элементы бОльшого списка, для которых не осталось объектов для сравнения:" + newLineTag);
            for (JSONObject object : firstListStock) {
                builder.append(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"" + newLineTag);
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && secondListStock.size() > 0) {
            builder.append("<div class=\"not-matched-2list\">" + newLineTag);
            builder.append("Элементы меньшего списка, для которых не нашлось соответствий:" + newLineTag);
            for (JSONObject object : secondListStock) {
                builder.append(++rowNumber + ") sKey:" + object.get("sKey") + ", sNumberT1: " + object.get("sNumberT1") + ", sNm: \"" + object.get("sNm") + "\"" + newLineTag);
            }
            builder.append("</div>\r\n");
        }
        return builder.toString();
    }

    private String getObjectRepresentation() {
        if (configuration.getShowOnlyCompareKeys()) {
            return getObjectRepresentationOnlyCompareKeys();
        } else {
            return getFullObjectRepresentationView();
        }
    }

    private String getObjectRepresentationOnlyCompareKeys() {
        StringBuilder builder = new StringBuilder();
        builder.append("Ok"); // TODO: доделать
        return builder.toString();
    }

    private String getFullObjectRepresentationView() {
        StringBuilder builder = new StringBuilder();
        // TODO: доделать
        return builder.toString();
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
