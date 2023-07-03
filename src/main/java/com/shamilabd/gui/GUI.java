package com.shamilabd.gui;

import com.shamilabd.*;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI extends JFrame {
    public static final int APP_WIDTH = 660;
    public static final int APP_HEIGHT = 630;
    public static final String APP_NAME = "JSON Comparator";
    public static final String APP_ICON_PATH = "/app_icon.png";

    private final Color backgroundColor = new Color(43, 43, 43);
    private final Color textColor = new Color(223, 118, 14);

    public GUI() throws HeadlessException {
        super(APP_NAME);

        GUIFooter footer = null;
        GUIContent content = null;
        GUIHeader header = null;
        try {
            initApp();
            Configuration configuration = new Configuration();
            GitHubPageURL gitHubPageURL = new GitHubPageURL();
            header = new GUIHeader(this, configuration, gitHubPageURL);
            content = new GUIContent(this, configuration, backgroundColor, textColor);
            footer = new GUIFooter(this, configuration, content, gitHubPageURL, backgroundColor, textColor);
        } catch (Exception e) {
            e.printStackTrace();
            showInitExceptionDialog(e.getMessage());
        }
        add(header.getContent(), BorderLayout.NORTH);
        add(content.getContent(), BorderLayout.CENTER);
        add(footer.getContent(), BorderLayout.SOUTH);
        revalidate();
    }

    public static void main(String[] args) {
        try {
            new GUI();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: добавить логирование
        }
    }

    private void initApp() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setSize(APP_WIDTH, APP_HEIGHT);
        setLocation(dimension.width / 2 - APP_WIDTH / 2,
                dimension.height / 2 - APP_HEIGHT / 2);
        ImageIcon appIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource(APP_ICON_PATH)));
        setIconImage(appIcon.getImage());
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        setVisible(true);
    }

    private void showInitExceptionDialog(String exceptionMessage) {
        JOptionPane.showMessageDialog(this,
                exceptionMessage,
                "Ошибка при запуске",
                JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }
}
