package com.shamilabd.gui;

import com.shamilabd.Configuration;
import com.shamilabd.HTML;
import com.shamilabd.JSONComparator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.IOException;

public class GUIFooter {
    private final JFrame mainFrame;
    private final Configuration configuration;
    private final JSONComparator comparator;
    private final HTML html;
    private final GUIContent content;
    private final GitHubPageURL gitHubPageURL;
    private final GUIHowCompare howCompareInfo;

    private final Color backgroundColor;
    private final Color textColor;
    private final Color footerBackgroundColor = Color.BLACK;
    private final Color linkColor = new Color(170, 113, 221);

    private final JButton howCompare = new JButton("Открыть руководство пользователя");
    private final JButton saveSettings = new JButton("Сохранить настройки");
    private final JButton compare = new JButton("Сравнить файлы");
    private final JButton exit = new JButton("Выход");

    public GUIFooter(JFrame jFrame, Configuration configuration, GUIContent content, GitHubPageURL gitHubPageURL, Color backgroundColor, Color textColor) throws IOException {
        mainFrame = jFrame;
        this.configuration = configuration;
        this.content = content;
        this.gitHubPageURL = gitHubPageURL;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        comparator = new JSONComparator(configuration);
        html = new HTML(configuration, comparator);
        howCompareInfo = new GUIHowCompare(backgroundColor);
        setListenerForButtons();
    }

    public JPanel getContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getControlButtons(), BorderLayout.NORTH);
        panel.add(getVersionAndLink(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getControlButtons() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(howCompare);
        panel.add(saveSettings);
        panel.add(compare);
        panel.add(exit);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder, "Что сделать?");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        return panel;
    }

    private JPanel getVersionAndLink() {
        Font labelFont = new Font(configuration.getFontName(), Font.BOLD, 14);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getVersionLabel(labelFont), BorderLayout.WEST);
        panel.add(getLinkPanel(labelFont), BorderLayout.EAST);
        panel.setBorder(new EmptyBorder(5, 3, 5, 10));
        panel.setBackground(footerBackgroundColor);
        return panel;
    }

    private JLabel getVersionLabel(Font font) {
        JLabel versionLabel = new JLabel("Версия " + GUI.APP_NAME + ": " + configuration.getCurrentJsonComparatorVersion());
        versionLabel.setFont(font);
        versionLabel.setForeground(textColor);
        return  versionLabel;
    }

    private JPanel getLinkPanel(Font font) {
        JPanel link = new JPanel();
        link.setLayout(new BorderLayout());
        link.setBackground(footerBackgroundColor);

        JLabel linkLabel = new JLabel("Сайт проекта:");
        linkLabel.setFont(font);
        linkLabel.setForeground(textColor);
        link.add(linkLabel, BorderLayout.NORTH);

        JLabel urlLabel = new JLabel("GitHub.com/ShamilAbd/JSONComparator");
        urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlLabel.addMouseListener(gitHubPageURL);
        urlLabel.setFont(font.deriveFont(12.0F));
        urlLabel.setForeground(linkColor);
        link.add(urlLabel, BorderLayout.SOUTH);

        return link;
    }

    private void setListenerForButtons() {
        howCompare.addActionListener((actionEvent) -> howCompareInfo.setVisible(true));
        exit.addActionListener((actionEvent) -> System.exit(0));
        setListenerForSaveButton();
        setListenerForCompareButton();
    }

    private void setListenerForSaveButton() {
        saveSettings.addActionListener((actionEvent) -> {
            content.updateFromTextFields();
            try {
                configuration.saveConfig();
                JOptionPane.showMessageDialog(mainFrame,
                        "Настройки успешно сохранены.",
                        "Сохранение настроек",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame,
                        e.getMessage(),
                        "Сохранение настроек",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setListenerForCompareButton() {
        compare.addActionListener((actionEvent) -> {
            content.updateFromTextFields();
            try {
                comparator.compare();
                html.saveContent();
                if (configuration.getOpenResultAfterCompare()) {
                    html.openInSystem();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame,
                        e.getMessage(),
                        "Ошибка при сравнении",
                        JOptionPane.ERROR_MESSAGE);
            } finally {
                comparator.clear();
            }
        });
    }
}
