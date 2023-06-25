package com.shamilabd;

import org.json.JSONObject;
import java.util.*;

public class HTML {
    private final Configuration configuration;
    private final String htmlFilePath;
    private final JSONComparator comparator;
    private int rowNumber = 0;

    public HTML(JSONComparator comparator) {
        configuration = new Configuration();
        this.comparator = comparator;
//        htmlFilePath = "CompareResult_" + System.currentTimeMillis() + ".html";
        htmlFilePath = "CompareResult.html"; // TODO: вернуть миллисекунды после отладки
    }

    public static void main(String[] args) {
        // TODO: 6. Добавить md5 сумм файлов.
        // TODO: 7. потом доделать функционал по поиску дублей в файлах
        // TODO: UI на свинге
        JSONComparator comparator = new JSONComparator();
        HTML resultPage = new HTML(comparator);
        Utils.saveInFile(resultPage.getHtmlFilePath(), resultPage.getHTMLContent());
        resultPage.openInSystem(resultPage.configuration.getOpenResultAfterCompare());
    }

    public String getHTMLContent() {
        return """
                <!Doctype html>
                <html lang="ru">
                    <head>
                        <title>JSONComparator</title>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                        <meta name="Author" content="Shamil Abdullin">
                        <link rel="stylesheet" type="text/css" href="style.css">
                    </head>
                    <body>
                        <header class="header-style" id="fileStart">
                            <h2>Сравнение JSON файлов</h2>
                            <p>Файлы:<br>
                                """ + comparator.getFirstJSON().toAbsolutePath() + "<br>\n"
                                    + comparator.getSecondJSON().toAbsolutePath() + """
                            </p>
                            <div><p>
                                <a href="#firstFileStart">Начало первого файла</a><br>
                                <a href="#secondFileStart">Начало второго файла</a>
                            </p></div>
                        </header>
                        <div class="main-content">""" + printMainStatistics() + """      
                            <p id="firstFileStart">Первый файл:</p>
                            <table border=3 class="main-table">
                              <tr class="header">
                                <th scope="col">Список полностью совпавших элементов</th>
                                <th scope="col">Список частично совпавших элементов</th>
                                <th scope="col">Cписок, для которых не осталось объектов для сравнения или не совпавших</th>
                              </tr>
                              <tr>
                                <th scope="row">""" + printMatchedFirst() + """
                                </th>
                                <th scope="row">""" + printHalfMatchedFirst() + """
                                </th>
                                <th scope="row">""" + printNotMatchedFirst() + """
                                </th>
                              </tr>
                            </table><br>
                            <div id="secondFileStart"> 
                                <a href="#firstFileStart">Перейти на начало первого файла.</a><br>
                                <p>Второй файл:</p> 
                            </div>
                            <table border=3 class="main-table">
                              <tr class="header">
                                <th scope="col">Список полностью совпавших элементов</th>
                                <th scope="col">Список частично совпавших элементов</th>
                                <th scope="col">Cписок, для которых не осталось объектов для сравнения или не совпавших</th>
                              </tr>
                              <tr>
                                <th scope="row">""" + printMatchedSecond() + """
                                </th>
                                <th scope="row">""" + printHalfMatchedSecond() + """
                                </th>
                                <th scope="row">""" + printNotMatchedSecond() + """
                                </th>
                              </tr>
                            </table>
                        </div>
                        <div>
                            <p>
                                <a href="#fileStart">Перейти на начало отчета</a><br>
                                <a href="#firstFileStart">Перейти на начало первого файла</a><br>
                                <a href="#secondFileStart">Перейти на начало второго файла</a>
                            </p>
                        </div>
                        <div class="date-time-comparing">
                            <p>Дата и время сравнения:<br>""" + Utils.getCurrentDateTime() + """
                            </p>
                        </div>
                    </body>
                </html>
                """;
    }

    private String printMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowFullyMatched() && comparator.getMatchedFirst().size() > 0) {
            builder.append(getFormattedList(comparator.getMatchedFirst(),
                    "fully-matched"));
        }
        return builder.toString();
    }

    private String printHalfMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedFirst().size() > 0) {
            List<JSONObject> objects = new ArrayList<>(comparator.getHalfMatchedFirst());
            builder.append(getFormattedList(objects, "partial-matched1"));
        }
        return builder.toString();
    }

    private String printNotMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowNotMatched() && comparator.getNotMatchedFirst().size() > 0) {
            builder.append(getFormattedList(comparator.getNotMatchedFirst(),
                    "not-matched"));
        }
        return builder.toString();
    }

    private String printMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowFullyMatched() && comparator.getMatchedSecond().size() > 0) {
            builder.append(getFormattedList(comparator.getMatchedSecond(),
                    "fully-matched"));
        }
        return builder.toString();
    }

    private String printHalfMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedSecond().size() > 0) {
            List<JSONObject> objects = new ArrayList<>(comparator.getHalfMatchedSecond());
            builder.append(getFormattedList(objects, "partial-matched2"));
        }
        return builder.toString();
    }

    private String printNotMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowNotMatched() && comparator.getNotMatchedSecond().size() > 0) {
            builder.append(getFormattedList(comparator.getNotMatchedSecond(),
                    "not-matched"));
        }
        return builder.toString();
    }

    private StringBuilder getFormattedList(List<JSONObject> list, String className) {
        StringBuilder builder = new StringBuilder();
        builder.append("<div class=\"").append(className).append("\">");
        builder.append("<pre>");

        boolean isNeedAddCommaBetweenObjects = configuration.getAddCommaBetweenObjects();
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            builder.append(getNextRowNumber()).append(getObjectRepresentation(list.get(i)));
            if (isNeedAddCommaBetweenObjects && (i < listSize - 1)) {
                builder.append(",");
            }
            builder.append("\n");
        }
        builder.append("</pre></div>\n");
        return builder;
    }

    private String printMainStatistics() {
        String newLineTag = "<br>\n";
        return "<div class=\"files-statistic\"><p>"
                + "Краткие сведения:" + newLineTag
                + "Элементов в 1 файле: " + comparator.getFirstListSize() + newLineTag
                + "Элементов в 2 файле: " + comparator.getSecondListSize() + newLineTag
                + "Путь до сравниваемого массива: " + configuration.getCompareKeysArrayPath() + newLineTag
                + "Список сравниваемых ключей: " + printCompareKeys() + newLineTag
                + "Сравнение с null всегда равно ложь: " + (configuration.getNullAsNotEqual() ? "да" : "нет") + newLineTag
                + "Выводить полностью совпавшие элементы: " + (configuration.getShowFullyMatched() ? "да" : "нет") + newLineTag
                + "Выводить частично совпавшие элементы: " + (configuration.getShowPartialMatched() ? "да" : "нет") + newLineTag
                + "Выводить не совпавшие элементы: " + (configuration.getShowNotMatched() ? "да" : "нет") + newLineTag
                + "Выводить объект только по сравниваемым ключам: " + (configuration.getShowOnlyCompareKeys() ? "да" : "нет") + newLineTag
                + "Открыть HTML с результатами после сравнения: " + (configuration.getOpenResultAfterCompare() ? "да" : "нет") + newLineTag
                + "Добавить порядковые номера к объетам сравнения: " + (configuration.getAddRowNumber() ? "да" : "нет") + newLineTag
                + "Добавить запятые между объетами сравнения: " + (configuration.getAddCommaBetweenObjects() ? "да" : "нет") + newLineTag
                + "Количество пробелов в отступе объектов: " + configuration.getLeftIndentsInObject() + newLineTag
                + "Совпало полностью: " + comparator.getMatchedFirst().size() + newLineTag
                + "Совпало частично (первый файл): " + comparator.getHalfMatchedFirst().size() + newLineTag
                + "Совпало частично (второй файл): " + comparator.getHalfMatchedSecond().size() + newLineTag
                + "Не совпало/не с чем сравнивать (первый файл): " + comparator.getNotMatchedFirst().size() + newLineTag
                + "Не совпало/не с чем сравнивать (второй файл): " + comparator.getNotMatchedSecond().size() + newLineTag
                + "<p></div>\n";
    }

    private String printCompareKeys() {
        StringBuilder builder = new StringBuilder();
        int keysCount = configuration.getCompareKeys().size();
        for (int i = 0; i < keysCount; i++) {
            builder.append("\"");
            builder.append(configuration.getCompareKeys().get(i));
            builder.append("\"");

            if (i < keysCount - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private String getNextRowNumber() {
        return configuration.getAddRowNumber() ? ++rowNumber + ") " : "";
    }

    private String getObjectRepresentation(JSONObject object) {
        StringBuilder objectText = new StringBuilder();
        if (configuration.getShowOnlyCompareKeys()) {
            objectText.append(getObjectRepresentationOnlyCompareKeys(object));
        } else {
            objectText.append(getFullObjectRepresentationView(object));
        }
        return objectText.toString();
    }

    private String getObjectRepresentationOnlyCompareKeys(JSONObject object) {
        return toStringByCompareKeys(object, configuration.getLeftIndentsInObject());
    }

    private String getFullObjectRepresentationView(JSONObject object) {
        return object.toString(configuration.getLeftIndentsInObject());
    }

    private String toStringByCompareKeys(JSONObject object, int indentFactor) {
        StringBuilder nullValues = new StringBuilder();
        Map<String, Object> originalElements = object.toMap();
        Map<String, Object> newElements = new HashMap<>();
        List<String> keys = configuration.getCompareKeys();

        for (Map.Entry<String, Object> entry : originalElements.entrySet()) {
            for (String key : keys) {
                if (key.equals(entry.getKey())) {
                    Object value = entry.getValue();
                    newElements.put(entry.getKey(), value);

                    if (entry.getValue() == null) {
                        nullValues.append(entry.getKey());
                        nullValues.append(" : null, ");
                    }
                }
            }
        }

        JSONObject jsonObject = new JSONObject(newElements);

        if (nullValues.length() > 2) {
            nullValues.replace(nullValues.length() - 2, nullValues.length(), "");
            StringBuilder jsonWithNullValue = new StringBuilder();
            jsonWithNullValue.append(jsonObject);
            jsonWithNullValue.replace(jsonWithNullValue.length() - 1, jsonWithNullValue.length(), "");
            jsonWithNullValue.append(", ");
            jsonWithNullValue.append(nullValues);
            jsonWithNullValue.append("}\n");
            jsonObject = new JSONObject(jsonWithNullValue.toString());
        }

        return jsonObject.toString(indentFactor);
    }

    public void openInSystem(boolean isNeedOpen) {
        if (isNeedOpen) {
            Utils.openFileInSystem(htmlFilePath);
        }
    }

    public String getHtmlFilePath() {
        return htmlFilePath;
    }
}
