package com.javaxyq.tools;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

//TODO 闪烁效果?恢复正常?自定义颜色?动画?换行?
/**
 * 打印格式文本<br>
 * 
 * 文字格式：聊天时可以为消息内容设置文字格式，指令为“#字母”，区分大小写。 <br>
 * #R 表示后面的字体为红色(red)<br>
 * #G 表示后面的字体为绿色(green)<br>
 * #B 表示后面的字体为蓝色(blue)<br>
 * #K 表示后面的字体为黑色(black)<br>
 * #Y 表示后面的字体为黄色(yellow)<br>
 * #W 表示后面的字体为白色(white)<br>
 * #b 表示后面的字体为闪烁(blink)<br>
 * #c + 六个数字或者A-F字母 自定义颜色，例如：c008000=暗绿色<br>
 * #u + 文字 + #u 文字有下划线。<br>
 * #n 所有文字状态恢复正常。<br>
 * #r 文字换行。<br> ## 输出一个#号。<br>
 * #0-99 动画
 * 
 * @author Langlauf
 * @date
 * @deprecated 不能满足需求 replace by GFormattedLabel.java
 */
public class FormattedTextPrinter {

	private static final int LINE_WIDTH = 14;

	private static final int LINE_HEIGHT = 14;

	private static final TreeMap<String, StyleAttribute> styleMap;

	private static final Font NORML_FONT = new Font("宋体",Font.PLAIN,14);

	static {
		styleMap = new TreeMap<String, StyleAttribute>();
		styleMap.put("#u", new StyleAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON));
		styleMap.put("#R", new StyleAttribute(TextAttribute.FOREGROUND, Color.RED));
		styleMap.put("#G", new StyleAttribute(TextAttribute.FOREGROUND, Color.GREEN));
		styleMap.put("#B", new StyleAttribute(TextAttribute.FOREGROUND, Color.BLUE));
		styleMap.put("#K", new StyleAttribute(TextAttribute.FOREGROUND, Color.BLACK));
		styleMap.put("#Y", new StyleAttribute(TextAttribute.FOREGROUND, Color.YELLOW));
		styleMap.put("#W", new StyleAttribute(TextAttribute.FOREGROUND, Color.WHITE));
		styleMap.put("#n", new StyleAttribute(TextAttribute.FONT, NORML_FONT));
	}

	private static final StyleAttribute Normal_Style=new StyleAttribute(TextAttribute.FOREGROUND,Color.BLACK);
	static class StyleAttribute {
		Attribute attrib;

		Object value;

		int beginIndex;

		public StyleAttribute(Attribute attrib, Object value) {
			this.attrib = attrib;
			this.value = value;
		}

		protected StyleAttribute clone() {
			return new StyleAttribute(attrib,value);
		}
	}

	public static void printText(String text, Graphics2D g) {
		int index = 0, width = 0, x = 20, y = 100;
		String word;
		// if(!text.startsWith("#"))text="#n"+text;
		Pattern pattern =
			Pattern.compile("#([RGBKYWnbur#]|[1-9]\\d|[0-9]|"
				+ "c[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?)|" + "[^#]+");
		Matcher m = pattern.matcher(text);
		StringBuilder builder = new StringBuilder();
		ArrayList<StyleAttribute> attribList = new ArrayList<StyleAttribute>();
		while (m.find()) {
			word = m.group();
			System.out.println(word);
			if (word.startsWith("#")) {
				StyleAttribute attrib = styleMap.get(word);
				if (attrib != null) {
					attrib=attrib.clone();
					attrib.beginIndex = index;
					attribList.add(attrib);
				}else if(word.startsWith("#c")){
					attrib=Normal_Style.clone();
					attrib.value = Color.decode("0x" + word.substring(2));
					attrib.beginIndex = index;
					attribList.add(attrib);
				}else if(word.startsWith("#n")){
					
				}else {
					builder.append(word);
					index += word.length();
				}
			} else {
				builder.append(word);
				index += word.length();
			}
		}
		String printText = builder.toString();
		int len = printText.length();
		AttributedString attribStr = new AttributedString(printText);
		attribStr.addAttribute(TextAttribute.FONT, NORML_FONT);
		attribStr.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
		for (StyleAttribute attrib : attribList) {
			attribStr.addAttribute(attrib.attrib, attrib.value, attrib.beginIndex, len);
		}
		g.drawString(attribStr.getIterator(), x, y);

	}

	public static void main(String[] args) {
		// String text="#ufsdfd\nsf#林#2.2";
		final String text = "这是一个#R《梦幻西游》#Y聊天格式文本:#G大家#c008000新年#B#u好!";
		JFrame frame = new JFrame() {
			private Image img=new ImageIcon("/resources/map/大唐国境.jpg").getImage();

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(img, 0, 30,  null);
				g2d.setColor(Color.WHITE);
				g2d.drawRoundRect(9, 39, getWidth()-19, getHeight()-49, 15, 15);
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g2d.setColor(Color.BLACK);
				g2d.fillRoundRect(10, 40, getWidth()-20, getHeight()-50, 15, 15);
				g2d.setComposite(AlphaComposite.SrcOver);
				FormattedTextPrinter.printText(text, g2d);
			}
		};
		// frame.setBackground(Color.BLACK);
		frame.setTitle("FormattedText");
		frame.setSize(400, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
