/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-15
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.util;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.utils.Lm;

/**
 * @author Administrator
 * @date 2010-5-15 create
 */
public class UIUtils {

	static {
		Lm.verifyLicense("Onseven Software AB", "DbVisualizer", ":yLk79NF.NhixitY0obolwn9q:lDRTX1");
	}
	/**
	 * 提示错误信息
	 * @param msg
	 * @param e
	 */
	public static void showError(String msg, Exception e) {
		ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
		e.printStackTrace(new PrintStream(output));
        JideOptionPane optionPane = new JideOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
        optionPane.setTitle(msg);
        optionPane.setDetails(output.toString());
        JDialog dialog = optionPane.createDialog("Warning");
        dialog.setResizable(true);
        dialog.pack();
        dialog.setVisible(true);
	}
	/**
	 * 提示普通信息
	 * @param msg
	 * @param e
	 */
	public static void showMessgge(String msg,String details) {
		JideOptionPane optionPane = new JideOptionPane("", JOptionPane.INFORMATION_MESSAGE, JideOptionPane.CLOSE_OPTION);
		optionPane.setTitle(msg);
		optionPane.setDetails(details);
		JDialog dialog = optionPane.createDialog("Note");
		dialog.setResizable(true);
		dialog.pack();
		dialog.setVisible(true);
	}
	public static final Font TEXT_FONT = new Font("宋体", Font.PLAIN, 14);
	public static final Color COLOR_NAME_BACKGROUND = new Color(27, 26, 18);
	public static final Color COLOR_NAME = new Color(118, 229, 128);
	public static final Font TEXT_NAME_FONT = new Font("宋体", Font.PLAIN, 16);
	public static final Color TEXT_NAME_NPC_COLOR = new Color(219, 197, 63);
	public static final Color COLOR_NAME_HIGHLIGHT = Color.RED;
	private static Map<String, Color> colors = new HashMap<String, Color>();
	static {
		colors.put("black", Color.black);
		colors.put("blue", Color.blue);
		colors.put("cyan", Color.cyan);
		colors.put("darkGray", Color.darkGray);
		colors.put("gray", Color.gray);
		colors.put("green", Color.green);
		colors.put("lightGray", Color.lightGray);
		colors.put("magenta", Color.magenta);
		colors.put("orange", Color.orange);
		colors.put("pink", Color.pink);
		colors.put("red", Color.red);
		colors.put("white", Color.white);
		colors.put("yellow", Color.yellow);
	}
	/**
	 * @param color
	 * @return
	 */
	public static Color getColor(String color) {
		Color c = colors.get(color);
		if(c == null) {
			c = Color.getColor(color, Color.white);
		}
		return c;
	}
}
