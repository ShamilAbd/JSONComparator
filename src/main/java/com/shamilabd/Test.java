package com.shamilabd;

import java.io.File;

public class Test {

    public static void main(String[] args) {
       String htmlFilePath = "Compare_result/CompareResult_" + System.currentTimeMillis() + ".html";
        File file = new File(htmlFilePath);
        System.out.println(file.getParent());
        System.out.println(file.getAbsolutePath());
    }
}
