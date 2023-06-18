package com.shamilabd;

import org.apache.commons.codec.digest.DigestUtils;

import java.awt.*;
import java.io.*;

public class Utils {

    public static String readFile(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public static String getMD5(String input) {
        return DigestUtils.md5Hex(input);
    }

    public static void openFileInSystem(String filePath) {
        File file = new File(filePath);
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
