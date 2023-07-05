package com.shamilabd.gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GUI extends GUICommon {
    public static final int APP_WIDTH = 660;
    public static final int APP_HEIGHT = 585;

    public GUI() throws HeadlessException {
        GUIFooter footer = null;
        GUIContent content = null;
        GUIHeader header = null;
        try {
            initApp();
            header = new GUIHeader(this);
            content = new GUIContent(this);
            footer = new GUIFooter(this, content);
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
        setBackground(BACKGROUND_COLOR);
        setVisible(true);
    }
}
