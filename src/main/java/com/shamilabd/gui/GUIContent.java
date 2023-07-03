package com.shamilabd.gui;

import com.shamilabd.Configuration;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIContent {
    private final JFrame mainFrame;
    private final Configuration configuration;

    private final Color backgroundColor;
    private final Color textColor;
    private final Color textFieldBackgroundColor = new Color(180, 180, 180);

    private final JTextField file1Path = new JTextField(38);
    private final JTextField file2Path = new JTextField(38);
    private final JButton choice1File = new JButton("Выбрать");
    private final JButton choice2File = new JButton("Выбрать");

    private final JCheckBox nullAsNotEqual = new JCheckBox("Сравнение с NULL всегда \"не равно\"");
    private final JCheckBox ignoreCase = new JCheckBox("Сравнивать без учета регистра");
    private final JCheckBox trimText = new JCheckBox("При сравнении обрезать пробелы по краям");
    private final JCheckBox showFullyMatched = new JCheckBox("Вывести полностью совпавшие элементы");
    private final JCheckBox showPartialMatched = new JCheckBox("Вывести частично совпавшие элементы");
    private final JCheckBox showNotMatched = new JCheckBox("Вывести не совпавшие элементы");
    private final JCheckBox showOnlyCompareKeys = new JCheckBox("Отчет только по сравниваемым ключам");
    private final JCheckBox openResultAfterCompare = new JCheckBox("После сравнения открыть файл с результатами");
    private final JCheckBox addRowNumber = new JCheckBox("Добавить номера объектам");
    private final JCheckBox addCommaBetweenObjects = new JCheckBox("Добавить запятые между объектами");
    private final JCheckBox findDuplicatesInFiles = new JCheckBox("Найти и вывести дубликаты в файлах");
    private final JTextField leftIndentsInObject = new JTextField(12);
    private final JTextField compareKeysArrayPath = new JTextField(36);
    private final JTextField compareKeys = new JTextField(42);

    public GUIContent(JFrame jFrame, Configuration configuration, Color backgroundColor, Color textColor) {
        mainFrame = jFrame;
        this.configuration = configuration;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        initVariables();
    }

    public JPanel getContent() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        addFilesFields(panel);
        addCompareSettingsFields(panel);
        addReportSettingsFields(panel);
        return panel;
    }

    private void initVariables() {
        setValuesFromConfig();
        setListenerForCheckBox();
        setListenerForButtons();
        setColorForFields();
    }

    private void setValuesFromConfig() {
        file1Path.setText(configuration.getFirstFilePath());
        file2Path.setText(configuration.getSecondFilePath());
        nullAsNotEqual.setSelected(configuration.getNullAsNotEqual());
        ignoreCase.setSelected(configuration.getIgnoreCase());
        trimText.setSelected(configuration.getTrimText());
        showFullyMatched.setSelected(configuration.getShowFullyMatched());
        showPartialMatched.setSelected(configuration.getShowPartialMatched());
        showNotMatched.setSelected(configuration.getShowNotMatched());
        showOnlyCompareKeys.setSelected(configuration.getShowOnlyCompareKeys());
        openResultAfterCompare.setSelected(configuration.getOpenResultAfterCompare());
        addRowNumber.setSelected(configuration.getAddRowNumber());
        addCommaBetweenObjects.setSelected(configuration.getAddCommaBetweenObjects());
        findDuplicatesInFiles.setSelected(configuration.getFindDuplicatesInFiles());
        leftIndentsInObject.setText(String.valueOf(configuration.getLeftIndentsInObject()));
        compareKeysArrayPath.setText(configuration.getCompareKeysArrayPath());
        compareKeys.setText(configuration.getCompareKeysForPrint());
    }

    private void setListenerForCheckBox() {
        nullAsNotEqual.addActionListener((a) ->
                configuration.setNullAsNotEqual(((JCheckBox) a.getSource()).isSelected()));
        ignoreCase.addActionListener((a) ->
                configuration.setIgnoreCase(((JCheckBox) a.getSource()).isSelected()));
        trimText.addActionListener((a) ->
                configuration.setTrimText(((JCheckBox) a.getSource()).isSelected()));
        showFullyMatched.addActionListener((a) ->
                configuration.setShowFullyMatched(((JCheckBox) a.getSource()).isSelected()));
        showPartialMatched.addActionListener((a) ->
                configuration.setShowPartialMatched(((JCheckBox) a.getSource()).isSelected()));
        showNotMatched.addActionListener((a) ->
                configuration.setShowNotMatched(((JCheckBox) a.getSource()).isSelected()));
        showOnlyCompareKeys.addActionListener((a) ->
                configuration.setShowOnlyCompareKeys(((JCheckBox) a.getSource()).isSelected()));
        openResultAfterCompare.addActionListener((a) ->
                configuration.setOpenResultAfterCompare(((JCheckBox) a.getSource()).isSelected()));
        addRowNumber.addActionListener((a) ->
                configuration.setAddRowNumber(((JCheckBox) a.getSource()).isSelected()));
        addCommaBetweenObjects.addActionListener((a) ->
                configuration.setAddCommaBetweenObjects(((JCheckBox) a.getSource()).isSelected()));
        findDuplicatesInFiles.addActionListener((a) ->
                configuration.setFindDuplicatesInFiles(((JCheckBox) a.getSource()).isSelected()));
    }

    private void setListenerForButtons() {
        choice1File.addActionListener((actionEvent) -> {
            JFileChooser fc = getFileFilterJSON();
            if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                file1Path.setText(file.getAbsoluteFile().getAbsolutePath());
                configuration.setFirstFilePath(file.getAbsoluteFile().getAbsolutePath());
            }
        });
        choice2File.addActionListener((actionEvent) -> {
            JFileChooser fc = getFileFilterJSON();
            if (fc.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                file2Path.setText(file.getAbsoluteFile().getAbsolutePath());
                configuration.setSecondFilePath(file.getAbsoluteFile().getAbsolutePath());
            }
        });
    }

    private void setColorForFields() {
        setColorAndBackgroundForElement(nullAsNotEqual);
        setColorAndBackgroundForElement(ignoreCase);
        setColorAndBackgroundForElement(trimText);
        setColorAndBackgroundForElement(showFullyMatched);
        setColorAndBackgroundForElement(showPartialMatched);
        setColorAndBackgroundForElement(showNotMatched);
        setColorAndBackgroundForElement(showOnlyCompareKeys);
        setColorAndBackgroundForElement(openResultAfterCompare);
        setColorAndBackgroundForElement(addRowNumber);
        setColorAndBackgroundForElement(addCommaBetweenObjects);
        setColorAndBackgroundForElement(findDuplicatesInFiles);
        setColorAndBackgroundForElement(file1Path);
        setColorAndBackgroundForElement(file2Path);
        setColorAndBackgroundForElement(leftIndentsInObject);
        setColorAndBackgroundForElement(compareKeysArrayPath);
        setColorAndBackgroundForElement(compareKeys);
    }

    private void setColorAndBackgroundForElement(JComponent component) {
        if (component instanceof JTextField) {
            component.setBackground(textFieldBackgroundColor);
        } else {
            component.setBackground(backgroundColor);
            component.setForeground(textColor);
        }
    }

    private JFileChooser getFileFilterJSON() {
        JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toUpperCase().endsWith(".JSON") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "JSON";
            }
        });
        fc.setCurrentDirectory(new File("."));
        return fc;
    }

    private void addFilesFields(JPanel mainFrame) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5;
        constraints.gridy = 0;
        constraints.gridx = 0;

        Insets betweenFilesSpace = new Insets(5, 2, 2, 5);
        constraints.insets = betweenFilesSpace;

        JLabel file1Label = new JLabel("Первый файл: ");
        JLabel file2Label = new JLabel("Второй файл: ");
        file1Label.setForeground(textColor);
        file2Label.setForeground(textColor);
        panel.add(file1Label, constraints);

        constraints.gridx = 1;
        panel.add(file1Path, constraints);
        constraints.gridx = 2;
        constraints.weighty   = 1.0;
        panel.add(choice1File, constraints);

        constraints.insets = betweenFilesSpace;
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(file2Label, constraints);

        constraints.gridx = 1;
        panel.add(file2Path, constraints);
        constraints.gridx = 2;
        constraints.weighty = 1.0;
        panel.add(choice2File, constraints);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder, "Файлы JSON для сравнения");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        mainFrame.add(panel);
    }

    private void addCompareSettingsFields(JPanel mainFrame) {
        GridLayout layout = new GridLayout(6, 1);
        JPanel panel = new JPanel(layout);

        panel.add(getCompareKeysArrayPathWithLabel());
        panel.add(getCompareKeysWithLabel());
        panel.add(nullAsNotEqual);
        panel.add(ignoreCase);
        panel.add(trimText);
        panel.add(findDuplicatesInFiles);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder, "Настройки сравнения файлов");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        mainFrame.add(panel);
    }

    private JPanel getCompareKeysArrayPathWithLabel() {
        return getLabelAndTextField("Путь до сравниваемых объектов:", compareKeysArrayPath);
    }

    private JPanel getCompareKeysWithLabel() {
        return getLabelAndTextField("Сравниваемые ключи:", compareKeys);
    }

    private void addReportSettingsFields(JPanel mainFrame) {
        GridLayout layout = new GridLayout(4, 2);
        JPanel panel = new JPanel(layout);

        panel.add(showFullyMatched);
        panel.add(showOnlyCompareKeys);
        panel.add(showPartialMatched);
        panel.add(addRowNumber);
        panel.add(showNotMatched);
        panel.add(addCommaBetweenObjects);
        panel.add(getLeftIndentsInObjectWithLabel());
        panel.add(openResultAfterCompare);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder, "Настройки вывода результатов");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        mainFrame.add(panel);
    }

    private JPanel getLeftIndentsInObjectWithLabel() {
        return getLabelAndTextField("Пробелов в объекте:", leftIndentsInObject);
    }

    private JPanel getLabelAndTextField(String label, JTextField field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel description = new JLabel(label);
        description.setForeground(textColor);
        panel.add(description);
        panel.add(field);
        panel.setBackground(backgroundColor);
        return panel;
    }

    public void updateFromTextFields() {
        String value = leftIndentsInObject.getText().trim();
        if (value.equals("")) {
            configuration.setLeftIndentsInObject(2);
            leftIndentsInObject.setText("2");
        } else {
            int newValue;
            try {
                newValue = Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
                newValue = 2;
            }
            if (newValue < 0) {
                configuration.setLeftIndentsInObject(2);
                leftIndentsInObject.setText("2");
            } else {
                configuration.setLeftIndentsInObject(newValue);
                leftIndentsInObject.setText(String.valueOf(newValue));
            }
        }

        configuration.setFirstFilePath(file1Path.getText().trim());
        configuration.setSecondFilePath(file2Path.getText().trim());
        configuration.setCompareKeysArrayPath(compareKeysArrayPath.getText().trim());

        String newKeysTextForCompare = compareKeys.getText().trim();
        java.util.List<String> keys = new ArrayList<>();
        if (newKeysTextForCompare.length() > 0) {
            Pattern pattern = Pattern.compile("\"([a-zA-Z0-9 .,]+)\"");
            Matcher matcher = pattern.matcher(newKeysTextForCompare);
            while (matcher.find()) {
                keys.add(matcher.group(1));
            }
        }
        configuration.setCompareKeys(keys);
    }
}
