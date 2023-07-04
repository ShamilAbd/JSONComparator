package com.shamilabd.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GitHubPageURL extends MouseAdapter {
    private static GitHubPageURL gitHubPageURL;

    private GitHubPageURL() {
    }

    public static GitHubPageURL getInstance() {
        if (gitHubPageURL == null) {
            gitHubPageURL = new GitHubPageURL();
        }
        return gitHubPageURL;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/ShamilAbd/JSONComparator"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
