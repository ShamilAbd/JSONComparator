package com.shamilabd;

import com.shamilabd.gui.GUICommon;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String readFile(String filePath) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath, Charset.forName("UTF8")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return builder.toString();
    }

    public static void openFileInSystem(String filePath) throws IOException {
        File file = new File(filePath);
        try {
            java.awt.Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void saveInFile(String path, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, Charset.forName("UTF8")))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void saveResources(String folder) throws Exception {
        Utils.exportResource(GUICommon.JSON_LOGO_PATH, folder);
        Utils.exportResource(GUICommon.GITHUB_LOGO_PATH, folder);
        Utils.exportResource(GUICommon.STYLES_PATH, folder);
    }

    public static String getCurrentDateTime() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
    }

    public static void exportResource(String resourceName, String folder) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String totalPath;
        try {
            stream = Utils.class.getResourceAsStream(resourceName);
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            totalPath = folder + resourceName;
            resStreamOut = new FileOutputStream(totalPath);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } finally {
            assert stream != null;
            stream.close();
            assert resStreamOut != null;
            resStreamOut.close();
        }
    }
}
