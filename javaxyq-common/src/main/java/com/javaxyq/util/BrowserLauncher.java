package com.javaxyq.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.net.URI;

@Slf4j
public class BrowserLauncher {

    @SneakyThrows
    public static void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            log.error("platform doesn't support launcher browser");
        }
    }
}
