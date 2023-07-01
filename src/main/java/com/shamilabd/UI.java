package com.shamilabd;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UI extends JFrame {
    public static final int APP_WIDTH = 680;
    public static final int APP_HEIGHT = 670;

    private final Configuration configuration;
    private final JSONComparator comparator;
    private final HTML html;

    private final Color backgroundColor = new Color(43, 43, 43);
    private final Color footerBackgroundColor = Color.BLACK;
    private final Color textColor = new Color(175, 91, 8);
    private final Color textFieldBackgroundColor = new Color(180, 180, 180);
    private final Color linkColor = new Color(170, 113, 221);

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

    private final JButton saveSettings = new JButton("Сохранить настройки");
    private final JButton compare = new JButton("Сравнить файлы");
    private final JButton exit = new JButton("Выход");

    public UI() throws HeadlessException, IOException {
        super("JSON Comparator");
        configuration = new Configuration();
        comparator = new JSONComparator(configuration);
        html = new HTML(configuration, comparator);
        initApp();
        initVariables();
        addActionListenerForButtons();
        add(initHeader(), BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new FlowLayout());
        addFilesFields(centerPanel);
        addCompareSettings(centerPanel);
        addReportSettings(centerPanel);
        add(centerPanel, BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(new EmptyBorder(10,0,0,0));
        this.setBackground(backgroundColor);
        setColorForFields();
        revalidate();
        setVisible(true);
    }

    private void initVariables() {
        setValuesFromConfig();
        setListenerForCheckBox();

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
        compareKeys.setText(getListOfCompareKeys());
    }

    private void setListenerForCheckBox() {
        nullAsNotEqual.addActionListener((a) -> {
            System.out.println("nullAsNotEqual");
            configuration.setNullAsNotEqual(((JCheckBox) a.getSource()).isSelected());
        });
        ignoreCase.addActionListener((a) -> {
            System.out.println("ignoreCase");
            configuration.setIgnoreCase(((JCheckBox) a.getSource()).isSelected());
        });
        trimText.addActionListener((a) -> {
            configuration.setTrimText(((JCheckBox) a.getSource()).isSelected());
        });
        showFullyMatched.addActionListener((a) -> {
            configuration.setShowFullyMatched(((JCheckBox) a.getSource()).isSelected());
        });
        showPartialMatched.addActionListener((a) -> {
            configuration.setShowPartialMatched(((JCheckBox) a.getSource()).isSelected());
        });
        showNotMatched.addActionListener((a) -> {
            configuration.setShowNotMatched(((JCheckBox) a.getSource()).isSelected());
        });
        showOnlyCompareKeys.addActionListener((a) -> {
            configuration.setShowOnlyCompareKeys(((JCheckBox) a.getSource()).isSelected());
        });
        openResultAfterCompare.addActionListener((a) -> {
            configuration.setOpenResultAfterCompare(((JCheckBox) a.getSource()).isSelected());
        });
        addRowNumber.addActionListener((a) -> {
            configuration.setAddRowNumber(((JCheckBox) a.getSource()).isSelected());
        });
        addCommaBetweenObjects.addActionListener((a) -> {
            configuration.setAddCommaBetweenObjects(((JCheckBox) a.getSource()).isSelected());
        });
        findDuplicatesInFiles.addActionListener((a) -> {
            configuration.setFindDuplicatesInFiles(((JCheckBox) a.getSource()).isSelected());
        });
    }

    private String getListOfCompareKeys() {
        StringBuilder builder = new StringBuilder();
        java.util.List<String> keys = configuration.getCompareKeys();
        int listSize = keys.size();
        for (int i = 0; i < listSize; i++) {
            builder.append("\"");
            builder.append(keys.get(i));
            builder.append("\"");
            if (i < listSize - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
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
    // TODO: м.б. окно ошибок
    // TODO: как итог - связать оба проекта вместе
    // TODO: покрыть тестами JUnit5

    private void initApp() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setSize(APP_WIDTH, APP_HEIGHT);
        setLocation(dimension.width / 2 - APP_WIDTH / 2,
                dimension.height / 2 - APP_HEIGHT / 2);
        ImageIcon appIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/app_icon.png")));
        setIconImage(appIcon.getImage());
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        //this.setResizable(false); // TODO: потом вернуть на фикс скорее всего.
    }

    private JPanel initHeader() {
        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon jsonLogoImg = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/json-logo.png")));
        Image image = jsonLogoImg.getImage();
        Image newImg = image.getScaledInstance(108, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        jsonLogoImg = new ImageIcon(newImg);
        JLabel jsonLogo = new JLabel(jsonLogoImg);
        jsonLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jsonLogo.addMouseListener(new GitHubPageAction());

        ImageIcon gitHubLogoImg = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/github-logo.png")));
        image = gitHubLogoImg.getImage();
        newImg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        gitHubLogoImg = new ImageIcon(newImg);
        JLabel gitHubLogo = new JLabel(gitHubLogoImg);

        gitHubLogo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gitHubLogo.addMouseListener(new GitHubPageAction());
        JLabel header = new JLabel("JSON Comparator");
        header.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.addMouseListener(new GitHubPageAction());
        header.setFont(new Font("Times New Roman", Font.BOLD, 24));
        header.setForeground(new Color(255, 141, 0));

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(header);
        buttonPane.add(Box.createHorizontalGlue());

        panel.add(buttonPane, BorderLayout.CENTER);
        panel.add(jsonLogo, BorderLayout.WEST);
        panel.add(gitHubLogo, BorderLayout.EAST);
        panel.setBackground(new Color(60, 63, 65));
        buttonPane.setBackground(panel.getBackground());
        panel.setBorder(new EmptyBorder(3,0,3,3));
        return panel;
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

    private void addCompareSettings(JPanel mainFrame) {
        GridLayout layout = new GridLayout(6,1);
        JPanel panel = new JPanel(layout);

        panel.add(getCompareKeysArrayPath());
        panel.add(getCompareKeys());
        panel.add(nullAsNotEqual);
        panel.add(ignoreCase);
        panel.add(trimText);
        panel.add(findDuplicatesInFiles);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder,"Настройки сравнения файлов");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        mainFrame.add(panel);
    }

    private void addReportSettings(JPanel mainFrame) {
        GridLayout layout = new GridLayout(4,2);
        JPanel panel = new JPanel(layout);

        panel.add(showFullyMatched);
        panel.add(showOnlyCompareKeys);
        panel.add(showPartialMatched);
        panel.add(addRowNumber);
        panel.add(showNotMatched);
        panel.add(addCommaBetweenObjects);
        panel.add(getLeftIndentsInObject());
        panel.add(openResultAfterCompare);

        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder,"Настройки вывода результатов");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        panel.setBackground(backgroundColor);
        mainFrame.add(panel);
    }

    private JPanel getLeftIndentsInObject() {
        return getLabelAndTextField("Пробелов в объекте:", leftIndentsInObject);
    }

    private JPanel getCompareKeysArrayPath() {
        return getLabelAndTextField("Путь до сравниваемых объектов:", compareKeysArrayPath);
    }

    private JPanel getCompareKeys() {
        return getLabelAndTextField("Сравниваемые ключи:", compareKeys);
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

    private JPanel footer() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getControlButtons(), BorderLayout.NORTH);
        panel.add(getFooterVersionAndLink(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getControlButtons() {
        JPanel commonPanel = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(saveSettings);
        panel.add(compare);
        panel.add(exit);
        Border simpleBorder = BorderFactory.createEtchedBorder();
        TitledBorder borderWithTitle = new TitledBorder(simpleBorder, "Что делаем, шеф?");
        borderWithTitle.setTitleColor(textColor);
        panel.setBorder(borderWithTitle);
        commonPanel.add(panel);
        commonPanel.setBackground(backgroundColor);
        panel.setBackground(backgroundColor);
        return commonPanel;
    }

    private void addActionListenerForButtons() {
        choice1File.addActionListener((actionEvent) -> {
            JFileChooser fc = getFileFilterJSON();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                file1Path.setText(file.getAbsoluteFile().getAbsolutePath());
                configuration.setFirstFilePath(file.getAbsoluteFile().getAbsolutePath());
            }
        });
        choice2File.addActionListener((actionEvent) -> {
            JFileChooser fc = getFileFilterJSON();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                file2Path.setText(file.getAbsoluteFile().getAbsolutePath());
                configuration.setSecondFilePath(file.getAbsoluteFile().getAbsolutePath());
            }
        });
        saveSettings.addActionListener((actionEvent) -> {
            String value = leftIndentsInObject.getText().trim();
            if (value.equals("")) {
                configuration.setLeftIndentsInObject(0);
                leftIndentsInObject.setText("0");
            } else {
                int newValue = 0;
                try {
                    newValue = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    newValue = 0;
                }
                if (newValue < 0) {
                    configuration.setLeftIndentsInObject(0);
                    leftIndentsInObject.setText("0");
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
                Pattern pattern = Pattern.compile("\"([a-zA-Z0-9\s.,]+)\"");
                Matcher matcher = pattern.matcher(newKeysTextForCompare);
                while (matcher.find()) {
                    keys.add(matcher.group(1));
                }
            }
            configuration.setCompareKeys(keys);
            configuration.saveConfig();
        });
        compare.addActionListener((actionEvent) -> {
            try {
                comparator.compare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                html.saveContent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (configuration.getOpenResultAfterCompare()) {
                html.openInSystem();
            }
        });
        exit.addActionListener((actionEvent) -> System.exit(0));
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

    private JPanel getFooterVersionAndLink() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel version = new JPanel();
        JPanel link = new JPanel();
        link.setLayout(new BorderLayout());

        JLabel versionLabel = new JLabel("Версия JSONComparator: " + configuration.getCurrentJsonComparatorVersion());
        versionLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        versionLabel.addMouseListener(new GitHubPageAction());
        versionLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        versionLabel.setForeground(textColor);
        version.add(versionLabel);

        JLabel linkLabel = new JLabel("Сайт проекта:");
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new GitHubPageAction());
        linkLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        linkLabel.setForeground(textColor);
        link.add(linkLabel, BorderLayout.NORTH);

        JLabel URLLabel = new JLabel("GitHub.com/ShamilAbd/JSONComparator");
        URLLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        URLLabel.addMouseListener(new GitHubPageAction());
        URLLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
        URLLabel.setForeground(linkColor);
        link.add(URLLabel, BorderLayout.SOUTH);

        panel.add(version, BorderLayout.WEST);
        panel.add(link, BorderLayout.EAST);
        link.setBackground(footerBackgroundColor);
        version.setBackground(footerBackgroundColor);
        panel.setBackground(footerBackgroundColor);
        panel.setBorder(new EmptyBorder(5,3,5,10));
        return panel;
    }

    public static void main(String[] args) {
        try {
            new UI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //printAllFonts();
    }

    public static void printAllFonts() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String font : fonts) {
            System.out.println(font);
        }
    }
}
