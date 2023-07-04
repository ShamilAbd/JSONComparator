package com.shamilabd.gui;

import com.shamilabd.HTML;
import com.shamilabd.JSONComparator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class GUIFooter extends GUICommon {
    private final JFrame mainFrame;
    private final JSONComparator comparator;
    private final HTML html;
    private final GUIContent content;
    private final GUIHowCompare howCompareInfo;

    private final Color footerBackgroundColor = Color.BLACK;
    private final Color linkColor = new Color(170, 113, 221);

    private final JLabel versionLabel = new JLabel("Версия " + GUI.APP_NAME
            + ": " + configuration.getCurrentJsonComparatorVersion());
    private final JLabel linkLabel = new JLabel("Сайт проекта:");
    private final JLabel urlLabel = new JLabel("GitHub.com/ShamilAbd/JSONComparator");

    private final JButton howCompare = new JButton("Открыть руководство пользователя");
    private final JButton saveSettings = new JButton("Сохранить настройки");
    private final JButton compare = new JButton("Сравнить файлы");
    private final JButton exit = new JButton("Выход");
    private final Insets buttonInsets = new Insets(3, 11, 3, 11);

    public GUIFooter(JFrame jFrame, GUIContent content) throws IOException {
        mainFrame = jFrame;
        this.content = content;
        comparator = new JSONComparator(configuration);
        html = new HTML(configuration, comparator);
        howCompareInfo = new GUIHowCompare(BACKGROUND_COLOR);
        setListenerForButtons();
        setMarginsForButtons();
    }

    public JPanel getContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getControlButtons(), BorderLayout.NORTH);
        panel.add(getVersionAndLink(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getControlButtons() {
        JPanel wrapper = new JPanel();
        JPanel panel = new JPanel();
        panel.add(howCompare);
        panel.add(saveSettings);
        panel.add(compare);
        panel.add(exit);
        panel.setBorder(new TitledBorder("Что сделать?").paintBorderAndText(textFont));
        panel.setBackground(BACKGROUND_COLOR);
        wrapper.setBackground(BACKGROUND_COLOR);
        wrapper.add(panel);
        return wrapper;
    }

    private JPanel getVersionAndLink() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getVersionLabel(), BorderLayout.WEST);
        panel.add(getLinkPanel(), BorderLayout.EAST);
        panel.setBorder(new EmptyBorder(5, 5, 5, 10));
        panel.setBackground(footerBackgroundColor);
        return panel;
    }

    private JLabel getVersionLabel() {
        versionLabel.setForeground(TEXT_COLOR);
        return versionLabel;
    }

    private JPanel getLinkPanel() {
        JPanel link = new JPanel(new BorderLayout());
        linkLabel.setForeground(TEXT_COLOR);
        link.add(linkLabel, BorderLayout.NORTH);
        urlLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlLabel.addMouseListener(gitHubPageURL);
        urlLabel.setForeground(linkColor);
        link.add(urlLabel, BorderLayout.SOUTH);
        link.setBackground(footerBackgroundColor);
        return link;
    }

    private void setListenerForButtons() {
        howCompare.addActionListener((actionEvent) -> howCompareInfo.setVisible(true));
        exit.addActionListener((actionEvent) -> System.exit(0));
        setListenerForSaveButton();
        setListenerForCompareButton();
    }

    private void setMarginsForButtons() {
        howCompare.setMargin(buttonInsets);
        saveSettings.setMargin(buttonInsets);
        compare.setMargin(buttonInsets);
        exit.setMargin(buttonInsets);
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
