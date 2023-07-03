package com.shamilabd.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class GUIHowCompare extends JFrame {
    public static final int FRAME_WIDTH = 750;
    public static final int FRAME_HEIGHT = 700;
    public static final String HOW_COMPARE_INFO_PAGE_PATH = "/howCompareInfo.html";
    public static final int FRAME_BORDER_THICKNESS = 3;
    private final JButton closeFrame = new JButton("Все понятно");
    private final Color backgroundColor;

    public GUIHowCompare(Color backgroundColor) throws IOException {
        this.backgroundColor = backgroundColor;
        initFrame();
        setListenerForButtons();
        fillFrameContent();
    }

    private void initFrame() {
        setUndecorated(true);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setLocation(dimension.width / 2 - FRAME_WIDTH / 2,
                dimension.height / 2 - FRAME_HEIGHT / 2);
        FrameMove move = new FrameMove(this);
        addMouseListener(move);
        addMouseMotionListener(move);
        setVisible(false);
    }

    private void setListenerForButtons() {
        closeFrame.addActionListener((actionEvent) -> this.setVisible(false));
    }

    private void fillFrameContent() throws IOException {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addDescription(), BorderLayout.CENTER);
        panel.add(addCloseButton(), BorderLayout.SOUTH);
        panel.setBorder(new LineBorder(backgroundColor, FRAME_BORDER_THICKNESS));
        add(panel);
    }

    private JScrollPane addDescription() throws IOException {
        JEditorPane pane = new JEditorPane();
        JScrollPane jScrollPane = new JScrollPane(pane);
        pane.setEnabled(true);
        pane.setContentType("text/html");
        pane.setBackground(backgroundColor);
        try {
            pane.setPage(getClass().getResource(HOW_COMPARE_INFO_PAGE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return jScrollPane;
    }

    private JPanel addCloseButton() {
        JPanel panel = new JPanel();
        panel.add(closeFrame);
        panel.setBackground(backgroundColor);
        return panel;
    }
}
