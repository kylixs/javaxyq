package com.javaxyq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;

//TODO add Link

/**
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
 * #r 文字换行。<br> 
 * ## 输出一个#号。<br>
 * #0-99 动画
 * 
 * @author 龚德伟
 * @history 2008-6-9 龚德伟 新建
 */
public class RichLabel extends JComponent {

	private static final long serialVersionUID = 4898130145332371300L;

	private ArrayList<Object> sectionList;

    private long startTime;

    private long currTime;

    private static HashMap<String, Animation> faceAnimations;
    static {
        faceAnimations = new HashMap<String, Animation>(100);
    }

    public RichLabel() {
        this(null);
    }

    public RichLabel(String text) {
        sectionList = new ArrayList<Object>();
        setBackground(Color.RED);
        setForeground(Color.BLUE);
        setIgnoreRepaint(true);
        setBorder(null);
        setOpaque(false);
        this.text = text;
        setSize(14 * 7, 16);
        parseText(text);
    }

    private void parseText(String text) {
        sectionList.clear();
        if (text == null)
            return;
        String section;
        Pattern pattern = Pattern.compile("#([RGBKYWnbur#]|[1-9]\\d|[0-9]|"
                + "c[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?[0-9A-Fa-f]?)|"
                + "[^#]+");
        Matcher m = pattern.matcher(text);
        Animation anim;
        while (m.find()) {
            section = m.group();
            if (section.startsWith("#")) {
                anim = faceAnimations.get(section);
                if (anim == null && section.charAt(1) >= '0' && section.charAt(1) <= '9') {
                    anim = SpriteFactory.loadAnimation("/resources/emoticons/" + section.substring(1) + ".was");
                    if (anim != null) {
                        faceAnimations.put(section, anim);
                    }
                }
                if (anim != null) {
                    sectionList.add(anim);
                } else if (section.startsWith("#c")) {
                    Color color = Color.decode("0x" + section.substring(2));
                    sectionList.add(color);
                } else if (section.equals("#R")) {
                    sectionList.add(Color.RED);
                } else if (section.equals("#G")) {
                    sectionList.add(Color.GREEN);
                } else if (section.equals("#B")) {
                    sectionList.add(Color.BLUE);
                } else if (section.equals("#W")) {
                    sectionList.add(Color.WHITE);
                } else if (section.equals("#K")) {
                    sectionList.add(Color.BLACK);
                } else if (section.equals("#Y")) {
                    sectionList.add(Color.YELLOW);
                } else if (section.equals("#r")) {
                    // line swap
                    sectionList.add(new Integer(-1));
                } else if (section.equals("#n")) {
                    // FIXME reset
                    sectionList.add(Color.WHITE);
                    // } else {
                    // sectionList.add(section);
                }
            } else {
                sectionList.add(section);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.setFont(UIUtils.TEXT_FONT);
        g.setColor(Color.WHITE);
        if (startTime == 0)
            startTime = System.currentTimeMillis();
        currTime = System.currentTimeMillis();
        int maxwidth = getWidth(), rowWidth = 0, y = 0, rowHeight = 0;
        int count = sectionList.size(), start = 0, x = 0;
        for (int i = 0; i < count; i++) {
            Object obj = sectionList.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                FontMetrics fm = g.getFontMetrics();
                rowHeight = Math.max(rowHeight, fm.getHeight());
                int dx = fm.stringWidth(str);
                if (rowWidth + dx <= maxwidth) {
                    rowWidth += dx;
                } else {
                    Point p = paintRichText(g, x, y, maxwidth, rowHeight, start, i + 1);
                    start = i + 1;
                    rowWidth = p.x;
                    rowHeight = fm.getHeight();
                    x = p.x;
                    y = p.y;
                }
            } else if (obj instanceof Animation) {
                Animation anim = (Animation) obj;
                if (anim.getWidth() + rowWidth > maxwidth) {
                    paintRichText(g, x, y, maxwidth, rowHeight, start, i);
                    start = i;
                    x = 0;
                    y += rowHeight;
                    rowWidth = anim.getWidth();
                    rowHeight = anim.getHeight();
                } else {
                    rowHeight = Math.max(rowHeight, anim.getHeight());
                    rowWidth += anim.getWidth();
                }
            } else if (obj instanceof Integer) {// line swap
                paintRichText(g, x, y, maxwidth, rowHeight, start, i + 1);
                start = i;
                x = 0;
                y += rowHeight;
                rowWidth = 0;
                rowHeight = 0;
            }
        }
        paintRichText(g, x, y, maxwidth, rowHeight, start, count);
    }

    private String lastStr;

    private String text;

    private Point paintRichText(Graphics g, int x, int y, int width, int rowh, int start,
            int end) {
        if (lastStr != null) {
            g.drawString(lastStr, 0, y + rowh);
            lastStr = null;
        }
        for (int i = start; i < end; i++) {
            Object obj = sectionList.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                FontMetrics fm = g.getFontMetrics();
                int len = str.length();
                int begin = 0, index, dx = 0;
                for (index = 0; index < len;) {
                    dx = fm.charWidth(str.charAt(index));
                    while (x + dx <= width) {
                        if (++index >= len)
                            break;
                        dx += fm.charWidth(str.charAt(index));
                    }
                    String s = str.substring(begin, index);
                    if (i == end - 1 && index >= len && x + dx < width && sectionList.size() > end) {
                        lastStr = s;
                        Object nextObj = sectionList.get(end);
                        if (nextObj instanceof Animation) {
                            Animation anim = (Animation) nextObj;
                            if (anim.getWidth() + x + dx > width) {
                                g.drawString(s, x, y + rowh);
                                lastStr = null;
                            }
                        }
                    } else {
                        g.drawString(s, x, y + rowh);
                    }

                    if (index < len || x + dx == width) {
                        begin = index;
                        x = 0;
                        y += rowh;
                        rowh = fm.getHeight();
                    } else {
                        x += fm.stringWidth(s);
                    }
                }
            } else if (obj instanceof Color) {
                g.setColor((Color) obj);
            } else if (obj instanceof Animation) {
                Animation anim = (Animation) obj;
                anim.updateToTime(currTime - startTime);
                g.drawImage(anim.getImage(), x, y + rowh - anim.getHeight(), null);
                x += anim.getWidth();
            }
        }
        return new Point(x, y);
    }

    public static void main(String[] args) {
        String text = "#35这是#52#r一个#R《梦幻西游》#B#52#r聊天#r格式文本:#G大家#c008000新#R年#Y事#G事#B如#R意#B#u好!#35#52#R恭贺新禧！！";
        final JFrame frame = new JFrame("GFormattedLabelText");
        frame.getContentPane().setBackground(Color.GRAY);
        final RichLabel label = new RichLabel(text);
        label.setLocation(10, 10);
        frame.add(label);
        final JTextField textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText(textField.getText());
            }
        });
        frame.add(textField, BorderLayout.SOUTH);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Thread update = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        label.repaint();
                        sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        update.setDaemon(true);
        update.start();
    }

    public void setText(String text) {
        this.text = text;
        parseText(text);
    }

    public String getText() {
        return text;
    }

    public Dimension computeSize(int maxwidth) {
        if (text == null)
            return new Dimension(0, 0);
        int rowWidth = 0, y = 0, rowHeight = 0;
        int count = sectionList.size(), start = 0, x = 0;
        BufferedImage temp = new BufferedImage(maxwidth, 16, BufferedImage.TYPE_USHORT_565_RGB);
        Graphics g = temp.getGraphics();
        g.setFont(UIUtils.TEXT_FONT);
        for (int i = 0; i < count; i++) {
            Object obj = sectionList.get(i);
            if (obj instanceof String) {
                String str = (String) obj;
                FontMetrics fm = g.getFontMetrics();
                rowHeight = Math.max(rowHeight, fm.getHeight());
                int dx = fm.stringWidth(str);
                if (rowWidth + dx <= maxwidth) {
                    rowWidth += dx;
                } else {
                    Point p = paintRichText(g, x, y, maxwidth, rowHeight, start, i + 1);
                    start = i + 1;
                    rowWidth = p.x;
                    rowHeight = fm.getHeight();
                    x = p.x;
                    y = p.y;
                }
            } else if (obj instanceof Animation) {
                Animation anim = (Animation) obj;
                if (anim.getWidth() + rowWidth > maxwidth) {
                    paintRichText(g, x, y, maxwidth, rowHeight, start, i);
                    start = i;
                    x = 0;
                    y += rowHeight;
                    rowWidth = anim.getWidth();
                    rowHeight = anim.getHeight();
                } else {
                    rowHeight = Math.max(rowHeight, anim.getHeight());
                    rowWidth += anim.getWidth();
                }
            } else if (obj instanceof Integer) {// line swap
                paintRichText(g, x, y, maxwidth, rowHeight, start, i + 1);
                start = i;
                x = 0;
                y += rowHeight;
                rowWidth = 0;
                rowHeight = 0;
            }
        }
        Point p = paintRichText(g, x, y, maxwidth, rowHeight, start, count);
        if (y == 0)
            maxwidth = p.x;
        return new Dimension(maxwidth, y + rowHeight + 4);//FIXME height!!1
    }
	@Override
	public void paintImmediately(int x, int y, int w, int h) {
		//super.paintImmediately(x, y, w, h);
	}
}
