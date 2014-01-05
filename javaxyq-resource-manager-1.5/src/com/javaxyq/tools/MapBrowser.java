package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Formatter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import com.javaxyq.ui.CenterLayout;
import com.javaxyq.util.BrowserLauncher;
import com.javaxyq.util.MapDecoder;
import com.javaxyq.util.Utils;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 梦幻西游地图查看器
 * 
 * @author 龚德伟(kylixs)
 * @history 2007-01-23 新建
 * @history 2008-03-09 增加导出整张地图的功能
 */
public class MapBrowser extends javax.swing.JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;

    private static final String SETTINGS_FILENAME = "Map Browser.ini";

	private JScrollPane mapScrollPanel;

	private JMap map;

	private JPanel centerPanel;

	private JLabel hitsLabel;

	private JPanel statusPanel;

	private JLabel sizeLabel;

	private JLabel pathLabel;

	private JLabel viewRectLabel;

	private JLabel segmentsLabel;

    private String filename;

	private static String appTitle = "Map Browser for 梦幻西游 (v1.2 build20080309)";

	private static String userName = "anybody";

	private static Cursor MOVE_CURSOR = new Cursor(Cursor.MOVE_CURSOR);

	private static Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		Utils.iniGlobalFont();
		MapBrowser inst = new MapBrowser();
		inst.setVisible(true);
	}

	public MapBrowser() {
		super();
        Utils.loadSettings(SETTINGS_FILENAME);
        initGUI();
        showAnnouncement();
        addWindowListener(this);
	}

	private void showAnnouncement() {
        String strText = "请仔细阅读以下使用许可。\n如果您不同意以下任何一点，请立即停止使用此软件。\n\n" + 
        		"\t1、禁止商业用途，仅用于个人研究；\n" +
        		"\t2、若产生任何侵权行为，使用者自负责任；\n" +
        		"\t3、未经许可严禁对本工具进行任何形式的转载、出版及其它的传播行为。\n\n";
        String oldYesText = UIManager.getString("OptionPane.yesButtonText");
        String oldNoText = UIManager.getString("OptionPane.noButtonText");
        UIManager.put("OptionPane.yesButtonText", "我同意");
        UIManager.put("OptionPane.noButtonText", "退出");
        
        int iValue = JOptionPane.showConfirmDialog(this, strText,"许可协议",
                JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
        if(iValue != JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        UIManager.put("OptionPane.yesButtonText",oldYesText);
        UIManager.put("OptionPane.noButtonText", oldNoText);
    }

    private void initGUI() {
		try {
			{
				statusPanel = new JPanel();
				GridLayout jPanel1Layout = new GridLayout(1, 1);
				jPanel1Layout.setHgap(5);
				jPanel1Layout.setVgap(5);
				jPanel1Layout.setColumns(1);
				statusPanel.setLayout(jPanel1Layout);
				getContentPane().add(statusPanel, BorderLayout.SOUTH);
				statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
				statusPanel.setPreferredSize(new java.awt.Dimension(10, 20));
				{
					hitsLabel = new JLabel();
					statusPanel.add(hitsLabel);
					hitsLabel.setText("Hits");
				}
				{
					pathLabel = new JLabel();
					statusPanel.add(pathLabel);
					pathLabel.setText("filename");
				}
				{
					sizeLabel = new JLabel();
					statusPanel.add(sizeLabel);
					sizeLabel.setText("size");
				}
				{
					segmentsLabel = new JLabel();
					statusPanel.add(segmentsLabel);
					segmentsLabel.setText("segments");
				}
				{
					viewRectLabel = new JLabel();
					statusPanel.add(viewRectLabel);
					viewRectLabel.setText("viewsRect");
					viewRectLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
					viewRectLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							Rectangle rect = map.getVisibleRect();
							MapDialog dlg = new MapDialog(MapBrowser.this, rect.width, rect.height);
							dlg.setVisible(true);
							Dimension d = dlg.getVisibleSize();
							if (d.width != rect.width || d.height != rect.height) {
								setViewportSize(d.width, d.height);
							}
						}
					});
				}
			}
			{
				centerPanel = new JPanel();
				BorderLayout jPanel2Layout = new BorderLayout();
				centerPanel.setLayout(jPanel2Layout);
				getContentPane().add(centerPanel, BorderLayout.CENTER);
				centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
				{
					mapScrollPanel = new JScrollPane();
					mapScrollPanel.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
					centerPanel.add(mapScrollPanel, BorderLayout.CENTER);
					{
						map = new JMap();
						//map.setWatermark(watermark);
//						map.setTag(userName);
						JPanel mapPanel = new JPanel(new CenterLayout());
						mapPanel.add("Center", map);
						mapScrollPanel.setViewportView(mapPanel);
						MouseInputAdapter mapMouseHandler = new MouseInputAdapter() {
							Point begPos;

							public void mousePressed(MouseEvent e) {
								setCursor(MOVE_CURSOR);
								begPos = e.getPoint();
							}

							public void mouseReleased(MouseEvent e) {
								setCursor(DEFAULT_CURSOR);
								begPos = null;
							}

							public void mouseDragged(MouseEvent e) {
								// setCursor(MOVE_CURSOR);
								if (begPos == null)
									return;
								JViewport viewport = mapScrollPanel.getViewport();
								Point nowPos = e.getPoint();
								Point viewPos = viewport.getViewPosition();
								viewPos.translate(begPos.x - nowPos.x, begPos.y - nowPos.y);
								if (viewPos.x < 0)
									viewPos.x = 0;
								if (viewPos.y < 0)
									viewPos.y = 0;
								Dimension size = viewport.getViewSize();
								Rectangle viewRect = viewport.getViewRect();
								int maxX = size.width - viewRect.width;
								int maxY = size.height - viewRect.height;
								if (viewPos.x > maxX)
									viewPos.x = maxX;
								if (viewPos.y > maxY)
									viewPos.y = maxY;
								viewport.setViewPosition(viewPos);
							}
						};
						map.addMouseListener(mapMouseHandler);
						map.addMouseMotionListener(mapMouseHandler);
					}
					mapScrollPanel.getViewport().addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							setViewRect(map.getVisibleRect());
						}
					});
				}
			}
			JMenuBar mainMenuBar = new JMenuBar();
			setJMenuBar(mainMenuBar);
			{
				JMenu jMenu3 = new JMenu();
				mainMenuBar.add(jMenu3);
				jMenu3.setText("文件");
				// {
				// JMenuItem newFileMenuItem = new JMenuItem();
				// jMenu3.add(newFileMenuItem);
				// newFileMenuItem.setText("New");
				// }
				{
					JMenuItem openFileMenuItem = new JMenuItem();
					jMenu3.add(openFileMenuItem);
					openFileMenuItem.setText("打开(O)");
					openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_MASK));
					openFileMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							openFile();
						}
					});
				}
				{
					JMenuItem snapMenuItem = new JMenuItem();
					jMenu3.add(snapMenuItem);
					snapMenuItem.setText("截屏(S)");
					snapMenuItem.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_MASK));
					snapMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							exportJPEG();
						}
					});
				}
                {
                    JMenuItem exportMenuItem = new JMenuItem();
                    jMenu3.add(exportMenuItem);
                    exportMenuItem.setText("导出整张地图(E)");
                    exportMenuItem.setAccelerator(KeyStroke.getKeyStroke('E', KeyEvent.CTRL_MASK));
                    exportMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            exportMap();
                        }
                    });
                }
                {
                    JMenuItem exportMenuItem = new JMenuItem();
                    jMenu3.add(exportMenuItem);
                    exportMenuItem.setText("导出地图块(B)");
                    exportMenuItem.setAccelerator(KeyStroke.getKeyStroke('B', KeyEvent.CTRL_MASK));
                    exportMenuItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            exportBlocks();
                        }
                    });
                }
				// {
				// JMenuItem closeFileMenuItem = new JMenuItem();
				// jMenu3.add(closeFileMenuItem);
				// closeFileMenuItem.setText("Close");
				// }
				{
					JSeparator jSeparator2 = new JSeparator();
					jMenu3.add(jSeparator2);
				}
				{
					JMenuItem exitMenuItem = new JMenuItem();
					jMenu3.add(exitMenuItem);
					exitMenuItem.setText("退出");
					exitMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							exit();
						}
					});
				}
			}
			{
				JMenu jMenu5 = new JMenu();
				mainMenuBar.add(jMenu5);
				jMenu5.setText("帮助");
				{
					JMenuItem helpMenuItem = new JMenuItem();
					jMenu5.add(helpMenuItem);
					helpMenuItem.setText("功能介绍");
					helpMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							showIntroduction();
						}
					});
				}
				{
					JMenuItem aboutMenuItem = new JMenuItem();
					jMenu5.add(aboutMenuItem);
					aboutMenuItem.setText("关于");
					aboutMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							showAbout();
						}
					});
				}
			}
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setTitle(appTitle);
			pack();
			setSize(650, 520);
			setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    private void exit() {
        Utils.saveSettings("Map Browser Settings", SETTINGS_FILENAME);
        System.exit(0);        
    }

    /**
	 * 设置Viewport可视区域大小
	 * 
	 * @param width
	 * @param height
	 */
	private void setViewportSize(int width, int height) {
		JViewport viewport = mapScrollPanel.getViewport();
		Rectangle rect = viewport.getViewRect();
		int dx = width - rect.width;
		int dy = height - rect.height;
		int width0 = getWidth() + dx;
		int height0 = getHeight() + dy;
		setSize(width0, height0);
		setLocationRelativeTo(null);
		validate();
	}

	/**
	 * 设置状态栏信息
	 * 
	 */
	private void setStatusText(String hits, String path, Dimension size, Dimension segments) {
		// |操作提示|地图文件名|地图大小|地图块数|当前区域|
		hitsLabel.setText(hits);
		pathLabel.setText(path);
		sizeLabel.setText(size.width + "*" + size.height);
		segmentsLabel.setText(segments.width + "*" + segments.height);
	}

	/**
	 * 设置提示信息
	 * 
	 * @param hits
	 */
	private void setHits(String hits) {
		hitsLabel.setText(hits);
	}

	/**
	 * 设置当前可视区域
	 * 
	 * @param rect
	 */
	private void setViewRect(Rectangle rect) {
		viewRectLabel.setText("[" + rect.x + "," + rect.y + "," + rect.width + "," + rect.height + "]");
	}

    /**
     * 导出当前窗口显示的地图区域
     */
    private void exportJPEG() {
        File saveFile = null;
        BufferedImage bi = null;
        saveFile = Utils.showSaveDialog(this,"保存",Utils.JPEG_FILTER);
        if (saveFile != null) {
        	String filename = saveFile.getAbsolutePath().toLowerCase();
        	if (!filename.endsWith(".jpg") && !filename.endsWith(".jpeg")) {
        		saveFile = new File(filename + ".jpg");
        	}
        	try {
        		Rectangle rect = map.getVisibleRect();// XXX export jpeg
        		bi = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
        		Graphics g = bi.getGraphics();
        		g.clipRect(0, 0, rect.width, rect.height);
        		map.drawRect(g, rect);
        		FileOutputStream out = new FileOutputStream(saveFile);
        		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
        		param.setQuality(1.0f, false);
        		encoder.setJPEGEncodeParam(param);
        		encoder.encode(bi);
        		out.close();
        	} catch (Exception ex) {
        		System.err.println("导出JPEG失败:" + ex.getMessage());
        		ex.printStackTrace();
        		JOptionPane.showMessageDialog(MapBrowser.this, "导出JPEG失败:" + ex.getMessage(), "错误",
        			JOptionPane.ERROR_MESSAGE);
        	}
        }
    }

    private void openFile() {
        File[] files = Utils.showOpenDialog(this,"打开地图", Utils.MAP_FILTER);
        if (files != null) {
        	File file = files[0];
        	boolean b = map.loadMap(file);
        	filename = file.getName();
            if (b) {
        		setTitle(filename + " - " + appTitle);
        		JViewport viewport = mapScrollPanel.getViewport();
        		viewport.setViewPosition(new Point());
        		viewport.setViewSize(map.getSize());
        		setStatusText("地图已打开", file.getAbsolutePath(), map.getSize(), map.getSegments());
        	} else {
        		JOptionPane.showMessageDialog(MapBrowser.this, "打开地图文件失败:" + filename, "错误",
        			JOptionPane.ERROR_MESSAGE);
        		setHits("地图打开失败");
        	}
        }
    }
    
    /**
     * 整张地图导出
     */
    private void exportMap() {
        File saveFile = null;
        saveFile = Utils.showSaveDialog(this,"导出地图",Utils.JPEG_FILTER,filename);
        if (saveFile != null) {
            String filename = saveFile.getAbsolutePath().toLowerCase();
            if (!filename.endsWith(".jpg") && !filename.endsWith(".jpeg")) {
                saveFile = new File(filename + ".jpg");
            }
            try {
                FileOutputStream out = new FileOutputStream(saveFile);
                MapDecoder decoder = this.map.getDecoder();
                Rectangle rect = new Rectangle(decoder.getWidth(),decoder.getHeight());
                BufferedImage bi = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
                Graphics g = bi.getGraphics();
                map.drawRect(g, rect);
                
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
                param.setQuality(1.0f, false);
                encoder.setJPEGEncodeParam(param);
                encoder.encode(bi);
                out.close();
            } catch (Exception ex) {
                System.err.println("导出地图失败:" + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MapBrowser.this, "导出地图失败:" + ex.getMessage(), "错误",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * 分块导出
     */
    private void exportBlocks() {
        File saveFile = null;
        saveFile = Utils.showSaveDialog(this,"导出地图",Utils.JPEG_FILTER,filename);
        if (saveFile != null) {
            try {
                FileOutputStream out = null;
                MapDecoder decoder = this.map.getDecoder();
                int hCount = decoder.getHorSegmentCount();
                int vCount = decoder.getVerSegmentCount();
                byte[] data = null;
                for (int h = 0; h < hCount; h++) {
                    for (int v = 0; v < vCount; v++) {
                        data = decoder.getJpegData(h,v);
                        Formatter formatter = new Formatter();
                        formatter.format("%s_%02d_%02d.jpg",filename,v+1,h+1);
                        out = new FileOutputStream(formatter.toString());
                        out.write(data);
                        out.close();
                    }
                }
            } catch (Exception ex) {
                System.err.println("导出地图失败:" + ex.getMessage());
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MapBrowser.this, "导出地图失败:" + ex.getMessage(), "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
        Utils.saveSettings("Map Browser Settings", SETTINGS_FILENAME);
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    private void showIntroduction() {
        final String url = "http://javaxyq.googlecode.com";
        String msg =
        	"Ctrl+O : 打开地图文件\n" + "Ctrl+S : 导出当前区域的地图图像\n" + "支持鼠标拖动地图\n"
        		+ "点击右下角文字标签可以精确设置可视区域大小\n\n" + "更多信息，请访问以下网址：\n";
        JLabel msgLabel = new JLabel(url);
        msgLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        msgLabel.setForeground(Color.BLUE);
        JTextArea textArea = new JTextArea(msg);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea,BorderLayout.CENTER);
        panel.add(msgLabel,BorderLayout.SOUTH);
        msgLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    BrowserLauncher.openURL(url);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JOptionPane.showMessageDialog(MapBrowser.this, panel, "Help", JOptionPane.QUESTION_MESSAGE);
    }

    private void showAbout() {
        String msg =
        	"Map Browser\n" + "Version: v1.2\n" + "Build  : 2008-3-9\n" + "Author : Kylixs\n"
        		+ "E-mail : kylixs@qq.com\n\n" + "授权用户: " + userName + "\n\n"
        		+ "特别感谢: Foxer、wangdali\n" ;
        JOptionPane.showMessageDialog(MapBrowser.this, msg, "About",
        	JOptionPane.INFORMATION_MESSAGE, Utils.loadIcon("about.png"));
    }

}
