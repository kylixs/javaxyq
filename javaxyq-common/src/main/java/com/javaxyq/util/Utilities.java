package com.javaxyq.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.URL;


public class Utilities {
    private static final Component sComponent = new Component() {
    };

    private static final MediaTracker sTracker = new MediaTracker(sComponent);

    private static int sID = 0;

    public static boolean waitForImage(Image image) {
        int id;
        synchronized (sComponent) {
            id = sID++;
        }
        sTracker.addImage(image, id);
        try {
            sTracker.waitForID(id);
        } catch (InterruptedException ie) {
            return false;
        }
        if (sTracker.isErrorID(id))
            return false;
        return true;
    }

    public static Image blockingLoad(String path) {
        Image image = Toolkit.getDefaultToolkit().getImage(path);
        if (waitForImage(image) == false)
            return null;
        return image;
    }

    public static Image blockingLoad(URL url) {
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        if (waitForImage(image) == false)
            return null;
        return image;
    }

    public static BufferedImage makeBufferedImage(Image image) {
        return makeBufferedImage(image, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage makeBufferedImage(Image image, int imageType) {
        if (waitForImage(image) == false)
            return null;
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
            image.getHeight(null), imageType);
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
        return bufferedImage;
    }

    public static Frame getNonClearingFrame(String name, Component c) {
        final Frame f = new Frame(name) {
            public void update(Graphics g) {
                paint(g);
            }
        };
        sizeContainerToComponent(f, c);
        centerFrame(f);
        f.setLayout(new BorderLayout());
        f.add(c, BorderLayout.CENTER);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                f.dispose();
            }
        });
        return f;
    }

    public static void sizeContainerToComponent(Container container, Component component) {
        if (container.isDisplayable() == false)
            container.addNotify();
        Insets insets = container.getInsets();
        Dimension size = component.getPreferredSize();
        int width = insets.left + insets.right + size.width;
        int height = insets.top + insets.bottom + size.height;
        container.setSize(width, height);
    }

    public static void centerFrame(Frame f) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension d = f.getSize();
        int x = (screen.width - d.width) / 2;
        int y = (screen.height - d.height) / 2;
        f.setLocation(x, y);
    }
}