package com.shamilabd;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GitHubPageAction extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/ShamilAbd/JSONComparator"));
        } catch (IOException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
}
