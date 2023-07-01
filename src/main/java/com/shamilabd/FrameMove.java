package com.shamilabd;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrameMove extends MouseAdapter {
    private JFrame jFrame;
    private int x;
    private int y;

    public FrameMove(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        jFrame.setLocation(e.getXOnScreen() - x, e.getYOnScreen() - y);
    }
}
