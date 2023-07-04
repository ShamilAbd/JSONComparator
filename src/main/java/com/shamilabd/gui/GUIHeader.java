package com.shamilabd.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class GUIHeader extends GUICommon {
    private final JFrame mainFrame;
    private final Color headerBackground = new Color(60, 63, 65);
    private final Color headerColor = new Color(255, 141, 0);
    private static final float HEADER_FONT_SIZE = 24.0F;
    private final static int JSON_LOGO_WIDTH = 108;
    private final static int JSON_LOGO_HEIGHT = 40;
    private final static int GITHUB_LOGO_WIDTH = 40;
    private final static int GITHUB_LOGO_HEIGHT = 40;
    private final JLabel header = new JLabel(GUI.APP_NAME);
    public static final EmptyBorder PADDINGS_BETWEEN_FRAME_AND_LOGOS = new EmptyBorder(3, 0, 3, 3);

    public GUIHeader(JFrame jFrame) {
        mainFrame = jFrame;
    }

    public JPanel getContent() {
        JPanel panel = new JPanel(new BorderLayout());
        FrameMove frameMove = new FrameMove(mainFrame);
        panel.addMouseMotionListener(frameMove);
        panel.addMouseListener(frameMove);
        panel.setBackground(headerBackground);
        panel.setBorder(PADDINGS_BETWEEN_FRAME_AND_LOGOS);
        panel.add(getJsonLogoLabel(), BorderLayout.WEST);
        panel.add(getCentralLabel(), BorderLayout.CENTER);
        panel.add(getGitHubLogo(), BorderLayout.EAST);
        return panel;
    }

    private JLabel getJsonLogoLabel() {
        return getResizedImageIconInLabel(JSON_LOGO_PATH, JSON_LOGO_WIDTH, JSON_LOGO_HEIGHT);
    }

    private JPanel getCentralLabel() {
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.addMouseListener(gitHubPageURL);
        header.setForeground(headerColor);
        header.setFont(textFont.deriveFont(HEADER_FONT_SIZE));
        header.setBackground(Color.BLUE);

        JPanel textPanel = new JPanel();
        textPanel.add(header);
        textPanel.setBackground(headerBackground);

        return textPanel;
    }

    private JLabel getGitHubLogo() {
        return getResizedImageIconInLabel(GITHUB_LOGO_PATH, GITHUB_LOGO_WIDTH, GITHUB_LOGO_HEIGHT);
    }

    private JLabel getResizedImageIconInLabel(String jsonLogoPath, int jsonLogoWidth, int jsonLogoHeight) {
        // Если надо будет еще какой-то image перевести в label, то вынести этот метод в отдельный класс
        ImageIcon jsonLogoImg = new ImageIcon(Objects.requireNonNull(this.getClass().getResource(jsonLogoPath)));
        Image image = jsonLogoImg.getImage();
        Image newImg = image.getScaledInstance(jsonLogoWidth, jsonLogoHeight,  Image.SCALE_SMOOTH);
        jsonLogoImg = new ImageIcon(newImg);

        JLabel jsonLogo = new JLabel(jsonLogoImg);
        jsonLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jsonLogo.addMouseListener(gitHubPageURL);

        return jsonLogo;
    }
}
