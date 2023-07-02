package com.shamilabd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConfigurationTest {

    private Configuration configuration;

    @BeforeEach
    void setUp() {
        try {
            configuration = new Configuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getFirstFilePath() {
        configuration.setFirstFilePath("C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_1.json");
        Assertions.assertEquals("C:\\\\SomeDirectoryPath\\\\jsonForCompare\\\\test_1.json",
                configuration.getFirstFilePath());
    }
}