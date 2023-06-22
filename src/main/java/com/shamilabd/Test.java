package com.shamilabd;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        Path secondJSON = Path.of(configuration.getSecondFilePath());
        System.out.println(secondJSON.getFileName());
    }
}
