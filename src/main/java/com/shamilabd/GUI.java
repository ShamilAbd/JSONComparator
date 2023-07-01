package com.shamilabd;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUI extends JFrame {
    public static final int APP_WIDTH = 660;
    public static final int APP_HEIGHT = 630;
    public static final String APP_NAME = "JSON Comparator";
    private final JFrame howCompareInfo = new JFrame();

    private final Configuration configuration;
    private final JSONComparator comparator;
    private final HTML html;

    private final Color backgroundColor = new Color(43, 43, 43);
    private final Color footerBackgroundColor = Color.BLACK;
    private final Color textColor = new Color(175, 91, 8);
    private final Color textFieldBackgroundColor = new Color(180, 180, 180);
    private final Color linkColor = new Color(170, 113, 221);
    private final Color headerBackground = new Color(60, 63, 65);

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

    private final JButton howCompare = new JButton("Показать как сравнивать файлы");
    private final JButton saveSettings = new JButton("Сохранить настройки");
    private final JButton compare = new JButton("Сравнить файлы");
    private final JButton exit = new JButton("Выход");

    public GUI() throws HeadlessException, IOException {
        super(APP_NAME);
        configuration = new Configuration();
        comparator = new JSONComparator(configuration);
        html = new HTML(configuration, comparator);

        initialization();
        add(getAppHeader(), BorderLayout.NORTH);
        add(getAppCenter(), BorderLayout.CENTER);
        add(getAppFooter(), BorderLayout.SOUTH);
        revalidate();
    }

    private JPanel getAppCenter() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(10,0,0,0));

        addFilesFields(panel);
        addCompareSettingsFields(panel);
        addReportSettingsFields(panel);

        return panel;
    }

    private void initialization() {
        initApp();
        initVariables();
        initHowCompareInfoFrame();
    }

    private void initApp() {
        setUndecorated(true);
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
        setVisible(true);
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
        compareKeys.setText(getListOfCompareKeys());
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

    private void setListenerForButtons() {
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
        howCompare.addActionListener((actionEvent) -> howCompareInfo.setVisible(true));
        saveSettings.addActionListener((actionEvent) -> {
            updateFromTextFields();

            String errorMessage = null;
            try {
                configuration.saveConfig();
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            if (errorMessage == null) {
                JOptionPane.showMessageDialog(this,
                        "Настройки успешно сохранены.",
                        "Сохранение настроек",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        errorMessage,
                        "Сохранение настроек",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        compare.addActionListener((actionEvent) -> {
            updateFromTextFields();

            String errorMessage = null;
            try {
                comparator.compare();
                html.saveContent();
                if (configuration.getOpenResultAfterCompare()) {
                    html.openInSystem();
                }
                comparator.clear();
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = e.getMessage();
            }
            if (errorMessage != null) {
                JOptionPane.showMessageDialog(this,
                        errorMessage,
                        "Ошибка при сравнении",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        exit.addActionListener((actionEvent) -> System.exit(0));
    }

    private void updateFromTextFields() {
        String value = leftIndentsInObject.getText().trim();
        if (value.equals("")) {
            configuration.setLeftIndentsInObject(2);
            leftIndentsInObject.setText("2");
        } else {
            int newValue = 0;
            try {
                newValue = Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
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
            Pattern pattern = Pattern.compile("\"([a-zA-Z0-9\s.,]+)\"");
            Matcher matcher = pattern.matcher(newKeysTextForCompare);
            while (matcher.find()) {
                keys.add(matcher.group(1));
            }
        }
        configuration.setCompareKeys(keys);
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

    private void initHowCompareInfoFrame() {
        howCompareInfo.setUndecorated(true);
        int width = 700;
        int height = 600;
        howCompareInfo.setSize(width, height);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        howCompareInfo.setLocation(dimension.width / 2 - width / 2, dimension.height / 2 - height / 2);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addHowCompareDescription(), BorderLayout.CENTER);
        panel.add(addExitButton(howCompareInfo), BorderLayout.SOUTH);
        howCompareInfo.add(panel);
        howCompareInfo.setVisible(false);
    }

    private JScrollPane addHowCompareDescription() {
        JEditorPane pane = new JEditorPane();
        JScrollPane jScrollPane = new JScrollPane(pane);
        jScrollPane.setPreferredSize(new Dimension(540,400));
        pane.setEnabled(true);
        pane.setContentType("text/html");
        pane.setText("""
                <html>
                <head>
                <style>
                p {font-size: 12px;}
                </style>
                </head>
                <body>
                <div>
                <h2>Руководство пользователя</h2>
                <p>
                После первого запуска программы, в папке, где лежит программа,
                будет создан файл настроек <b>"config.json"</b> со стандартными настройками
                и примерами заполнения полей (т.е. если этого файла нет).<br>
                Эти настройки автоматический подставляются в программу.
                </p>
                <p>
                Для начала работы необходимо:<br>
                1) Указать файлы для сравнения.<br>
                2) Убедиться, что выставленные параметры сравнения и вывода отчета<br>
                удовлетворяют вашим требованиям.<br>
                3) Нажать "Сравнить файлы".<br>
                Если в ходе работы программы возникнет ошибка,<br>
                то будет выведено соответствующее сообщение.<br>
                После успешного сравнения файлов, в папке с программой появится папка <b>"Compare_result"</b> с отчетом, имя файла будет примерно такое: <b>"CompareResult_1688237966862.html"</b>.<br>
                Если был выстален признак "После сравнения открыть файл с результатами", то соответственно отчет откроется в браузере.<br>
                Если нажать на кнопку "Сохранить настройки", то все текущие найстройки сравнения файлов и вывода отчета будут сохранены в файле <b>"config.json"</b>.
                </p>
                <p>
                <h3>Более детальное описание настройки сравнения:</h3>
                Разберем на примере такого JSON объекта:
                <pre>
                {
                  "ObjectName" : "ListOKZ",
                  "ObjectCaption" : "Общероссийский классификатор занятий",
                  "SomeKey" : {
                    "Version" : "1.5",
                    "UserName" : "ShamilAbd",
                    "KeyWithArray" : [
                      {
                        "key" : "123",
                        "name" : "Руководители специализированных подразделений",
                        "periodTo" : "30.06.2015 00:00:00",
                        "version" : 1
                      },
                      {
                        "key" : "777",
                        "name" : "Руководители служб по сбыту, маркетингу и развитию",
                        "periodTo" : null,
                        "version" : 2
                      }
                    ]
                  }
                }
                </pre>
                </p>
                <p>
                1) В строку <b>"Путь до сравниваемых объектов"</b> для этого файла необходимо указать <b>"SomeKey.KeyWithArray"</b>, через точку,
                т.к. в поле "SomeKey" лежит объект "KeyWithArray", а уже в нем лежит массив объектов, которые нужно сравнить.<br>
                Если такой вложенности нет, то там можно указать либо сразу ключ с объектами, либо вообще указать пустую строку,
                если сам файл представляет из себя массив объектов для сравнения.<br>
                2) В строку "Сравниваемые ключи" необходимо в двойных кавычках перечислить список полей, которые необходимо сравнить.<br>
                Для этого примера будет идти сравнение по трем ключам: "name", "key", "version", а "periodTo" в сравнении не будет участвовать.<br>
                3) Если выставлен признак "Сравнение с NULL всегда "не равно"", то поля с отсутствующим значением (с null) всегда будут считаться несовпавшими.
                Если же признак снять, то при сравнении двух null будет получен результат как совпавший.<br>
                4) Если выставлен признак "Сравнивать без учета регистра", то при сравнении строк "Привет", "ПрИвЕт", "привет", "ПРИВЕТ" и любые другие комбинации с регистром будут считаться совпавшими.<br>
                5) Если выставлен признак "При сравнении обрезать пробелы по краям", то при сравнении строк "Привет" и "  Привет " будут считаться совпавшими, т.к. при сравнении пробелы по концам обрежутся.<br>
                6) Если выставлен признак "Найти и вывести дубликаты в файлах" то для каждого файла будет произведен поиск дублей объектов и их вывод в отчет.<br>
                7) Если выставлен признак "Вывести полностью совпавшие элементы" то в отчете будет заполнен столбец "Полностью совпавшие элементы".<br>
                8) Если выставлен признак "Вывести частично совпавшие элементы" то в отчете будет заполнен столбец "Частично совпавшие элементы".<br>
                9) Если выставлен признак "Вывести не совпавшие элементы" то в отчете будет заполнен столбец "Не совпавшие или те, для которых не осталось объектов для сравнения".<br>
                10) Если выставлен признак "Отчет только по сравниваемым ключам", то в отчете при выводе объекта в поля будут использованы лишь сравниваемые ключи, а не все ключи объекта. Тут проще запустить и посмотреть самому.<br>
                11) Если выставлен признак "Добавить номера объектам", то к каждому объекту в своей колонке будет присвоен порядковый номер.<br>
                12) Если выставлен признак "Добавить запятые между объектами", то между объектами будут добавлены запятые. Удобно, если нужно скопировать результат в какой-то JSON файл и сделать из них массив объектов.<br>
                13) Параметр "Пробелов в объекте" указывает, сколько отступов будет добавлено на каждый уровень объекта.<br>
                Как в примере, до "ObjectName" и "ObjectCaption" идет 2 отступа, а "Version" и "UserName" уже следующий уровнь, т.е. там уже будет 4 отступа.<br>
                По умолчанию равно 2.
                </p>
                </div>
                </body>
                </html>
                """);
        return jScrollPane;
    }

    private JPanel addExitButton(JFrame frame) {
        JPanel panel = new JPanel();
        JButton button = new JButton("Все понятно");
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
        panel.add(button);
        panel.setBackground(backgroundColor);
        return panel;
    }

    private JPanel getAppHeader() {
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
        header.setFont(new Font(configuration.getFontName(), Font.BOLD, 24));
        header.setForeground(new Color(255, 141, 0));

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(header);
        buttonPane.add(Box.createHorizontalGlue());

        panel.add(buttonPane, BorderLayout.CENTER);
        panel.add(jsonLogo, BorderLayout.WEST);
        panel.add(gitHubLogo, BorderLayout.EAST);
        panel.setBackground(headerBackground);
        MouseAdapter mouseAdapter = new FrameMove(this);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseListener(mouseAdapter);
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

    private void addCompareSettingsFields(JPanel mainFrame) {
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

    private void addReportSettingsFields(JPanel mainFrame) {
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

    private JPanel getAppFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(getControlButtons(), BorderLayout.NORTH);
        panel.add(getFooterVersionAndLink(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel getControlButtons() {
        JPanel commonPanel = new JPanel(new FlowLayout());
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(howCompare);
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

        JLabel versionLabel = new JLabel("Версия JSON Comparator: " + configuration.getCurrentJsonComparatorVersion());
        versionLabel.setFont(new Font(configuration.getFontName(), Font.BOLD, 14));
        versionLabel.setForeground(textColor);
        version.add(versionLabel);

        JLabel linkLabel = new JLabel("Сайт проекта:");
        linkLabel.setFont(new Font(configuration.getFontName(), Font.BOLD, 14));
        linkLabel.setForeground(textColor);
        link.add(linkLabel, BorderLayout.NORTH);

        JLabel URLLabel = new JLabel("GitHub.com/ShamilAbd/JSONComparator");
        URLLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        URLLabel.addMouseListener(new GitHubPageAction());
        URLLabel.setFont(new Font(configuration.getFontName(), Font.BOLD, 12));
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
            new GUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //printAllFonts();
    }
}
