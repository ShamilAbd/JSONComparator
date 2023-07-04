package com.shamilabd.gui;

import com.shamilabd.Configuration;

import javax.swing.*;
import java.awt.*;

public class GUICommon  extends JFrame {
    public static final String APP_NAME = "JSON Comparator";
    public static final String APP_ICON_PATH = "/app_icon.png";
    public static final String JSON_LOGO_PATH = "/json-logo.png";
    public static final String GITHUB_LOGO_PATH = "/github-logo.png";
    public static final String HOW_COMPARE_INFO_PAGE_PATH = "/howCompareInfo.html";
    public static final String STYLES_PATH = "/style.css";
    public static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    public static final Color TEXT_COLOR = new Color(223, 118, 14);
    protected Configuration configuration;
    protected GitHubPageURL gitHubPageURL;
    protected Font textFont;

    public GUICommon() {
        super(APP_NAME);
        initFields();
    }

    private void initFields() {
        try {
            configuration = Configuration.getInstance();
            gitHubPageURL = GitHubPageURL.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            showInitExceptionDialog(e.getMessage());
        }
        textFont = new Font(configuration.getFontName(), Font.PLAIN, 12);
    }

    protected void showInitExceptionDialog(String exceptionMessage) {
        JOptionPane.showMessageDialog(this,
                exceptionMessage,
                "Ошибка при запуске",
                JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
