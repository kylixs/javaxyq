/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;

import com.javaxyq.config.LabelConfig;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.Option;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.OptionLabel;
import com.javaxyq.ui.RichLabel;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public class Toolkit {
    private static Toolkit instance = new Toolkit();

    private Map<String, Color> colorMap = new HashMap<String, Color>();

    private Toolkit() {
        colorMap.put("white", Color.WHITE);
        colorMap.put("black", Color.BLACK);
        colorMap.put("red", Color.RED);
        colorMap.put("pink", Color.PINK);
        colorMap.put("blue", Color.BLUE);
        colorMap.put("yellow", Color.YELLOW);
        colorMap.put("green", Color.GREEN);
    }

    public static Toolkit getInstance() {
        return instance;
    }

    public OptionLabel createOptionLabel(int x, int y, int width, int height, Option option) {
    	OptionLabel label = new OptionLabel(option);
    	label.setLocation(x, y);
    	label.setSize(width, height);
    	return label;
    }
//    public LinkLabel createLinkLabel(int x, int y, int width, int height, String text,
//            String action, String arguments) {
//        LinkLabel label = new LinkLabel(text, action, arguments);
//        label.setLocation(x, y);
//        label.setSize(width, height);
//        return label;
//    }

    public RichLabel createRichLabel(int x, int y, int width, int height, String text) {
        LabelConfig cfg = new LabelConfig(x, y, width, height, text);
        return this.createRichLabel(cfg);
    }

    public Label createLabel(LabelConfig cfg) {
        Label label = new Label(cfg.getText());
        label.setName(cfg.getName());
        label.setLocation(cfg.getX(), cfg.getY());
        label.setSize(cfg.getWidth(), cfg.getHeight());
        String color = cfg.getColor();
        String align = cfg.getAlign();
        if (color != null) {
            label.setForeground(parseColor(color));
        }
        if (align != null) {
            if (align.equals("center")) {
                label.setHorizontalAlignment(JLabel.CENTER);
            } else if (align.equals("right")) {
                label.setHorizontalAlignment(JLabel.RIGHT);
            }
        }
        return label;
    }

    private Color parseColor(String color) {
        return colorMap.get(color);
    }

    public RichLabel createRichLabel(LabelConfig cfg) {
        RichLabel label = new RichLabel(cfg.getText());
        label.setName(cfg.getName());
        label.setLocation(cfg.getX(), cfg.getY());
        //XXX get prefered size
        Dimension size = label.computeSize(cfg.getWidth());
        label.setSize(cfg.getWidth(), size.height);
        return label;
    }

    public static InputStream getInputStream(String filename) {
        InputStream is = Toolkit.class.getResourceAsStream(filename);
        if (is == null) {
            try {
                if (filename.charAt(0) == '/') {
                    filename = filename.substring(1);
                }
                File file = new File(filename); 
                if(file.exists()) {
                	is = new FileInputStream(filename);
                }else {
                	is = CacheManager.getInstance().getResourceAsStream(filename);
                }
            } catch (FileNotFoundException e) {
            	System.out.println("找不到文件: "+filename);
                //e.printStackTrace();
            } catch (IOException e) {
            	System.out.println("找不到文件: "+filename);
				e.printStackTrace();
			}
        }
        return is;
    }

    public static byte[] getResourceData(String filename) throws IOException {
        InputStream is = getInputStream(filename);
        if (is == null) {
            return null;
        }
        byte[] buf = new byte[is.available()];
        int count = 0;
        while (is.available() > 0) {
            count += is.read(buf, count, is.available());
        }
        return buf;
    }

    public static Image createImageFromResource(String filename) {
        byte[] data = null;
        try {
            data = getResourceData(filename);
        } catch (IOException e) {
            System.err.println("create image error!");
            e.printStackTrace();
        }
        if (data == null) {
            return null;
        }
        return java.awt.Toolkit.getDefaultToolkit().createImage(data);
    }

    public static InputStream getInputStream(Class clazz, String filename) {
        // TODO Toolkit: getInputStream
        return null;
    }
}
