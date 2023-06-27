package com.shamilabd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConfigurationTest {

    private final Configuration configuration = new Configuration();

    public ConfigurationTest() throws IOException {
    }

    @Test
    void getFirstFilePath() {
        Assertions.assertEquals("D:/Java/Projects/JSONComparator/jsonForCompare/test.json",
                configuration.getFirstFilePath());
    }

//    @Test
//    void getSecondFilePath() {
//    }

//    @Test
//    void getCompareKeysArrayPath() {
//        Assertions.assertArrayEquals(List.of("sNm", "sKey"), configuration.getCompareKeys());
//    }

//    @Test
//    void getJsonComparatorVersion() {
//    }
//
//    @Test
//    void getCompareKeys() {
//    }
//
//    @Test
//    void getNullAsNotEqual() {
//    }
//
//    @Test
//    void getShowFullyMatched() {
//    }
//
//    @Test
//    void getShowPartialMatched() {
//    }
//
//    @Test
//    void getShowNotMatched() {
//    }
//
//    @Test
//    void getShowOnlyCompareKeys() {
//    }
//
//    @Test
//    void getOpenResultAfterCompare() {
//    }
//
//    @Test
//    void getAddRowNumber() {
//    }
//
//    @Test
//    void getLeftIndentsInObject() {
//    }
//
//    @Test
//    void getAddCommaBetweenObjects() {
//    }
//
//    @Test
//    void getFindDuplicatesInFiles() {
//    }
//
//    @Test
//    void getCurrentJsonComparatorVersion() {
//    }
}