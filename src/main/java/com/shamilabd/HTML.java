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
                        <header class="header-style">
                            <h2>Сравнение JSON файлов</h2>
                            <p>Файлы:<br>
                                """ + comparator.getFirstJSON().toAbsolutePath() + "<br>\n"
                                    + comparator.getSecondJSON().toAbsolutePath() + """
                            </p>
                        </header>
                        <div class="main-content">""" + printMainStatistics() + """                        
                            <table border=3 class="main-table">
                              <tr>
                                <th scope="col">Список полностью совпавших элементов</th>
                                <th scope="col" colspan="2">Список частично совпавших элементов</th>
                                <th scope="col">Cписок, для которых не осталось объектов для сравнения или не совпавших</th>
                              </tr>
                              <tr>
                                <th scope="row">""" + printMatched() + """
                                </th>
                                <th scope="row">""" + printHalfMatchedFirst() + """
                                </th>
                                <th scope="row">""" + printHalfMatchedSecond() + """
                                </th>
                                <th scope="row">""" + printNotMatched() + """
                                </th>
                              </tr>
                            </table>
                        </div>
                        <div class="date-time-comparing">
                            <p>Дата и время сравнения:\040""" + Utils.getCurrentDateTime() + """
                            </p>
                        </div>
                    </body>
                </html>
                """;
    }

    private String printMatched() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowFullyMatched() && comparator.getMatchedResult().size() > 0) {
            builder.append(getFormattedList(comparator.getMatchedResult(),
                    "fully-matched"));
        }
        return builder.toString();
    }

    private String printHalfMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedFirst().size() > 0) {
            builder.append(getFormattedList(comparator.getHalfMatchedFirst(),
                    "partial-matched1"));
        }
        return builder.toString();
    }

    private String printHalfMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedSecond().size() > 0) {
            builder.append(getFormattedList(comparator.getHalfMatchedSecond(),
                    "partial-matched2"));
        }
        return builder.toString();
    }

    private String printNotMatched() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowNotMatched() && comparator.getNotMatchedResult().size() > 0) {
            builder.append(getFormattedList(comparator.getNotMatchedResult(),
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
                + "Совпало полностью: " + comparator.getMatchedResult().size() + newLineTag
                + "Совпало частично: " + comparator.getHalfMatchedFirst().size() + newLineTag
                + "Не совпало/не с чем сравнивать: " + comparator.getNotMatchedResult().size() + newLineTag
                + "<p></div>\n";
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
        return object.toString();
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
