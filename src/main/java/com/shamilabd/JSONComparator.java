package com.shamilabd;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private boolean isFilesSwapped;
    private final String htmlFilePath;

    public JSONComparator() {
        configuration = new Configuration();
        firstJSON = Path.of(configuration.getFirstFilePath());
        secondJSON = Path.of(configuration.getSecondFilePath());
//        htmlFilePath = "CompareResult_" + System.currentTimeMillis() + ".html";
        htmlFilePath = "CompareResult.html"; // TODO: вернуть миллисекунды после отладки
    }

    public static void main(String[] args) {
        // TODO: 6. Добавить md5 сумм файлов.
        // TODO: 7. потом доделать функционал по поиску дублей в файлах
        // TODO: UI на свинге
        // TODO: поправить вывод результата сравнения
        JSONComparator comparator = new JSONComparator();
        comparator.fillFirstJSONList();
        comparator.fillSecondJSONList();
        comparator.compare();
        comparator.saveInHTML();
        comparator.openHTMLInSystem();
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
                       <p>Файлы:<br>\n""" + firstJSON.getFileName() + "<br>\n"
                        + secondJSON.getFileName() + """
                       </p>
                     </header>
                     <div class="main-content">""" + printResult() + """
                     </div>
                     <div class="date-time-comparing"><p>Дата и время сравнения: """ + getCurrentDateTime() + """
                     </p></div>
                   </body>
                </html>
                """;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFilePath))) {
            writer.write(htmlText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return format.format(new Date());
    }

    private String printResult() {
        String newLineTag = "<br>\r\n";
        String newLine = "\r\n";
        StringBuilder builder = new StringBuilder();
        int rowNumber = 0;
        builder.append("<div class=\"files-statistic\">" + newLine);
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
                builder.append(++rowNumber + ") " + getObjectRepresentation(object) + "<br>\r\n");
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowPartialMatched() && halfMatchedResult.size() > 0) {
            builder.append("<div class=\"partial-matched\">" + newLineTag);
            builder.append("Список частично совпавших элементов:" + newLineTag);
            for (JSONObject object : halfMatchedResult) {
                builder.append(++rowNumber + ") " + getObjectRepresentation(object) + "<br>\r\n");
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && notMatchedResult.size() > 0) {
            builder.append("<div class=\"not-matched\">" + newLineTag);
            builder.append("Остальные элементы бОльшого файла:" + newLineTag);
            for (JSONObject object : notMatchedResult) {
                builder.append(++rowNumber + ") " + getObjectRepresentation(object) + "<br>\r\n");
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && firstListStock.size() > 0) {
            builder.append("<div class=\"not-matched-1list\">" + newLineTag);
            builder.append("Элементы бОльшего списка, для которых не осталось объектов для сравнения:" + newLineTag);
            for (JSONObject object : firstListStock) {
                builder.append(++rowNumber + ") " + getObjectRepresentation(object) + "<br>\r\n");
            }
            builder.append("</div>\r\n");
        }

        if (configuration.getShowNotMatched() && secondListStock.size() > 0) {
            builder.append("<div class=\"not-matched-2list\">" + newLineTag);
            builder.append("Элементы меньшего списка, для которых не нашлось соответствий:" + newLineTag);
            for (JSONObject object : secondListStock) {
                builder.append(++rowNumber + ") " + getObjectRepresentation(object) + "<br>\r\n");
            }
            builder.append("</div>\r\n");
        }
        return builder.toString();
    }

    private String getObjectRepresentation(JSONObject object) {
        if (configuration.getShowOnlyCompareKeys()) {
            return getObjectRepresentationOnlyCompareKeys(object);
        } else {
            return getFullObjectRepresentationView(object);
        }
    }

    private String getObjectRepresentationOnlyCompareKeys(JSONObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int keysCount = configuration.getCompareKeys().size() - 1;
        for (int i = 0; i <= keysCount; i++) {
            String key = configuration.getCompareKeys().get(i);
            boolean isNull = object.isNull(key);
            // TODO: не работает. Доработать.
            if (isNull) {
                builder.append("\"" + key + "\" : " + object.get(key));
            } else if (!isNull && (object.optString(key).equals("true") || object.optString(key).equals("false"))) {
                builder.append("\"" + key + "\" : " + object.get(key));
            } else if (!isNull && !object.get(key).equals("")) {
                builder.append("\"" + key + "\" : \"" + object.get(key) + "\"");
            } else {
                builder.append("\"" + key + "\" : " + object.get(key));
            }

            if (i != (keysCount)) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    private String getFullObjectRepresentationView(JSONObject object) {
        StringBuilder builder = new StringBuilder();
        builder.append(object.toString());
        return builder.toString();
    }

    private void openHTMLInSystem() {
        if (configuration.getOpenResultAfterCompare()) {
            Utils.openFileInSystem(htmlFilePath);
        }
    }
}
