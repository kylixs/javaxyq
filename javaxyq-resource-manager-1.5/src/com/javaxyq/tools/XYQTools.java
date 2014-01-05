/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.util.WASDecoder;
import com.javaxyq.widget.Sprite;
import com.jidesoft.utils.Lm;

/**
 * @author 龚德伟
 * @history 2008-6-26 龚德伟 新建
 */
public class XYQTools {

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("[usage]: XYQTools -option");
            System.out.println("[options]:");
            System.out.println("\t-r run game resource manager");
            System.out.println("\t-s run game script editor");
            return;
        }
        String strCMD = args[0];
        if ("-r".equals(strCMD)) {

        } else if ("-s".equals(strCMD)) {
            // run script editor
        }
    }

    public static void verifyJideLicense() {
        Lm.verifyLicense("Onseven Software AB", "DbVisualizer", ":yLk79NF.NhixitY0obolwn9q:lDRTX1");
    }

    /**
     * create a sprite from this node's data
     * 
     * @param nodeId
     * @return
     */
    public static Sprite createSprite(FileObject file) {
        Sprite sprite = null;
        try {
            InputStream is = file.getDataStream();
            sprite = SpriteFactory.createSprite(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sprite;
    }

    public static Image createImage(FileObject node) {
        try {
            return Toolkit.getDefaultToolkit().createImage(node.getData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageIcon createSpriteIcon(FileObject node, int frameIndex) {
        WASDecoder decoder = new WASDecoder();
        try {
            decoder.load(node.getDataStream());
            return new ImageIcon(decoder.getFrame(frameIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给图片加上水印
     * @param srcImage 原图片
     * @param markImage 水印图片
     * @return
     */
    public static Image markImage(Image srcImage, Image markImage) {
        BufferedImage bi = null;
        Graphics g = null;
        if (srcImage instanceof BufferedImage) {
            bi = (BufferedImage) srcImage;
        } else {
            bi = new BufferedImage(srcImage.getWidth(null), srcImage.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
            g = bi.getGraphics();
            g.drawImage(srcImage, 0, 0, null);
        }
        g = bi.getGraphics();
        g.drawImage(markImage, 0, 0, null);
        return bi;
    }

}
