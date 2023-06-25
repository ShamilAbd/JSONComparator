package com.shamilabd;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // TODO: покрыть тестами JUnit5
        // TODO: зашить стили и картинку в jar
        // TODO:
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
                        <div class="header-style" id="fileStart">
                            <div class="header-side">
                            </div>
                            <div class="header-center">
                                <h2>Сравнение JSON файлов</h2>
                            </div>
                            <div class="header-side">
                                <a href="https://github.com/ShamilAbd/JSONComparator" target="_blank"><img src="github-logo.png" alt="GitHub.com/ShamilAbd/JSONComparator"></a>
                            </div>
                        </div>
                        <div class="main-content">
                            """ + printStatisticsAndConfigs() + """      
                            <p id="firstFileStart">Перейти на начало объектов <a href="#secondFileStart"><b>второго</b> файла</a>.</p>
                            <p>Объекты <b>первого</b> файла:</p>
                            
                            <table class="main-table">
                              <tr class="header">
                                <td>Полностью совпавшие элементы</td>
                                <td>Частично совпавшие элементы</td>
                                <td>Не совпавшие или те, для которых не осталось объектов для сравнения</td>
                              </tr>
                              <tr class="data">
                                <td scope="row">""" + printMatchedFirst() + """
                                </td>
                                <td scope="row">""" + printHalfMatchedFirst() + """
                                </td>
                                <td scope="row">""" + printNotMatchedFirst() + """
                                </td>
                              </tr>
                            </table><br>
                            
                            <p id="secondFileStart">Перейти на начало объектов <a href="#firstFileStart"><b>первого файла</b></a>.</p>
                            <p>Объекты <b>второго</b> файл:</p>
                        
                            <table class="main-table">
                              <tr class="header">
                                <td>Полностью совпавшие элементы</td>
                                <td>Частично совпавшие элементы</td>
                                <td>Не совпавшие или те, для которых не осталось объектов для сравнения</td>
                              </tr>
                              <tr class="data">
                                <td scope="row">""" + printMatchedSecond() + """
                                </td>
                                <td scope="row">""" + printHalfMatchedSecond() + """
                                </td>
                                <td scope="row">""" + printNotMatchedSecond() + """
                                </td>
                              </tr>
                            </table>
                        </div>
                        <div>
                            <p>Перейти на начало:<br>
                                <a href="#fileStart">отчета</a><br>
                                <a href="#firstFileStart">объектов <b>первого</b> файла</a><br>
                                <a href="#secondFileStart">объектов <b>второго</b> файла</a>
                            </p>
                        </div>
                        <div id="footer-background">
                          <div id="footer">
                            <div id="DateTime">
                              <span class="Head">Дата и время сравнения:</span><br>
                              <span class="Data">""" + Utils.getCurrentDateTime() + """
                              </span>
                            </div>
                            <div id="Version">
                              <span class="Head">Версия JSONComparator:\040""" + configuration.getCurrentJsonComparatorVersion() + """
                              </span>
                            </div>
                            <div id="Author">
                              <span class="Head">Автор: Абдуллин Шамиль</span><br>
                              <span class="Data"><a href="https://github.com/ShamilAbd/JSONComparator" target="_blank">GitHub.com/ShamilAbd/JSONComparator</a></span>
                            </div>
                          </div>
                        </div>
                    </body>
                </html>
                """;
    }

    private String printStatisticsAndConfigs() {
        String newLineTag = "<br>\n";
        return """
                <table class="configAndStatistics">
                  <tr class="file-header">
                    <td colspan="2">Сравниваемые файлы</td>
                  </tr>
                  <tr class="file-list">
                    <td colspan="2">""" + getCompareFilesLinks() + """
                    </td>
                  </tr>
                  <tr class="header">
                    <td>Статистика по итогу сравнения</td>
                    <td>Параметры сравнения файлов</td>
                  </tr>
                  <tr class="data">
                    <td>
                        Объектов в 1 файле:\040""" + comparator.getFirstListSize() + newLineTag + """
                        Объектов в 2 файле:\040""" + comparator.getSecondListSize() + newLineTag + """
                        Совпало полностью:\040""" + comparator.getMatchedFirst().size() + newLineTag + """
                        Совпало частично (первый файл):\040""" + comparator.getHalfMatchedFirst().size() + newLineTag + """
                        Совпало частично (второй файл):\040""" + comparator.getHalfMatchedSecond().size() + newLineTag + """
                        Не совпало/не с чем сравнивать (первый файл):\040""" + comparator.getNotMatchedFirst().size() + newLineTag + """
                        Не совпало/не с чем сравнивать (второй файл):\040""" + comparator.getNotMatchedSecond().size() + newLineTag + """
                    </td>
                    <td>
                        Путь до сравниваемого массива:\040\"""" + configuration.getCompareKeysArrayPath() + "\"" + newLineTag + """
                        Список сравниваемых ключей:\040""" + printCompareKeys() + newLineTag + """
                        Сравнение с null всегда равно ложь:\040""" + (configuration.getNullAsNotEqual() ? "да" : "нет") + newLineTag + """
                        Выводить полностью совпавшие элементы:\040""" + (configuration.getShowFullyMatched() ? "да" : "нет") + newLineTag + """
                        Выводить частично совпавшие элементы:\040""" + (configuration.getShowPartialMatched() ? "да" : "нет") + newLineTag + """
                        Выводить не совпавшие элементы:\040""" + (configuration.getShowNotMatched() ? "да" : "нет") + newLineTag + """
                        Выводить объекты только по сравниваемым ключам:\040""" + (configuration.getShowOnlyCompareKeys() ? "да" : "нет") + newLineTag + """
                        Открыть HTML с результатами после сравнения:\040""" + (configuration.getOpenResultAfterCompare() ? "да" : "нет") + newLineTag + """
                        Добавить порядковые номера к объектам сравнения:\040""" + (configuration.getAddRowNumber() ? "да" : "нет") + newLineTag + """
                        Добавить запятые между объектами сравнения:\040""" + (configuration.getAddCommaBetweenObjects() ? "да" : "нет") + newLineTag + """
                        Количество пробелов в отступе объектов:\040""" + configuration.getLeftIndentsInObject() + newLineTag + """
                    </td>
                  </tr>
                </table>""";
    }

    private String getCompareFilesLinks() {
        return "<a href=\"" + comparator.getFirstJSON().toAbsolutePath() + "\" target=\"_blank\">"
                + comparator.getFirstJSON().toAbsolutePath() + "</a><br>\n"
                + "<a href=\"" + comparator.getSecondJSON().toAbsolutePath() + "\" target=\"_blank\">"
                + comparator.getSecondJSON().toAbsolutePath() + "</a>";
    }

    private String printMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowFullyMatched() && comparator.getMatchedFirst().size() > 0) {
            builder.append(getFormattedList(comparator.getMatchedFirst()));
        }
        return builder.toString();
    }

    private String printHalfMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedFirst().size() > 0) {
            List<JSONObject> objects = new ArrayList<>(comparator.getHalfMatchedFirst());
            builder.append(getFormattedList(objects));
        }
        return builder.toString();
    }

    private String printNotMatchedFirst() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowNotMatched() && comparator.getNotMatchedFirst().size() > 0) {
            builder.append(getFormattedList(comparator.getNotMatchedFirst()));
        }
        return builder.toString();
    }

    private String printMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowFullyMatched() && comparator.getMatchedSecond().size() > 0) {
            builder.append(getFormattedList(comparator.getMatchedSecond()));
        }
        return builder.toString();
    }

    private String printHalfMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowPartialMatched() && comparator.getHalfMatchedSecond().size() > 0) {
            List<JSONObject> objects = new ArrayList<>(comparator.getHalfMatchedSecond());
            builder.append(getFormattedList(objects));
        }
        return builder.toString();
    }

    private String printNotMatchedSecond() {
        StringBuilder builder = new StringBuilder();
        if (configuration.getShowNotMatched() && comparator.getNotMatchedSecond().size() > 0) {
            builder.append(getFormattedList(comparator.getNotMatchedSecond()));
        }
        return builder.toString();
    }

    private StringBuilder getFormattedList(List<JSONObject> list) {
        StringBuilder builder = new StringBuilder();
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
        builder.append("</pre>\n");
        rowNumber = 0;
        return builder;
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
