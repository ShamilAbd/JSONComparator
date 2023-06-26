package com.shamilabd;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @BeforeAll
    static void startTest() {
        System.out.println("UtilsTest started!");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("-----------beforeEach------------");
    }

    @Test
    void myFirstTest() {
        System.out.println("MyFirstTest!");
        Assertions.assertEquals(4, (2 + 2));
    }

    @AfterEach
    void afterEach() {
        System.out.println("-----------afterEach------------");
    }

    @AfterAll
    static void endTest() {
        System.out.println("UtilsTest ended!");
    }
}
