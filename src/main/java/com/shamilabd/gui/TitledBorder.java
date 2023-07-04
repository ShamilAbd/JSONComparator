package com.shamilabd.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TitledBorder extends javax.swing.border.TitledBorder {
    public TitledBorder(String title) {
        super(title);
    }

    public javax.swing.border.TitledBorder paintBorderAndText(Font font) {
        Border simpleBorder = BorderFactory.createEtchedBorder();
        javax.swing.border.TitledBorder borderWithTitle = new javax.swing.border.TitledBorder(simpleBorder, title);
        borderWithTitle.setTitleColor(GUICommon.TEXT_COLOR);
        borderWithTitle.setTitleFont(font);
        return borderWithTitle;
    }
}
