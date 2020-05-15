package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import com.javaxyq.util.Section;
import com.javaxyq.util.WASDecoder;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free
 * for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using
 * Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS
 * CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class PaletteViewer extends javax.swing.JFrame {
    private JMenuBar jMenuBar1;

    private JMenu jMenu1;

    private JMenuItem jMenuItem1;

    private JPanel jPanel1;

    private JLabel statusLabel;

    private JTabbedPane tabbedPanel;

    private JMenuItem jMenuItem3;

    private JMenuItem jMenuItem2;

    private JSeparator jSeparator1;

    private JMenu jMenu2;

    protected File dir = new File("resources/");

    protected File dir2 = new File("resources/");

    protected File schemeFile;

    protected JPopupMenu popupMenu;

    protected Map<String, WASDecoder> decoders;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        PaletteViewer inst = new PaletteViewer();
        inst.setVisible(true);
    }

    public PaletteViewer() {
        super();
        decoders = new HashMap<String, WASDecoder>();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                jMenuBar1 = new JMenuBar();
                setJMenuBar(jMenuBar1);
                {
                    jMenu1 = new JMenu();
                    jMenuBar1.add(jMenu1);
                    jMenu1.setText("\u6587\u4ef6");
                    {
                        jMenuItem1 = new JMenuItem();
                        jMenu1.add(jMenuItem1);
                        jMenuItem1.setText("查看精灵调色板");
                        jMenuItem1.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setMultiSelectionEnabled(true);
                                chooser.setCurrentDirectory(dir);
                                chooser.showOpenDialog(PaletteViewer.this);
                                File[] files = chooser.getSelectedFiles();
                                if (files == null || files.length <= 0) {
                                    return;
                                }
                                dir = files[0].getParentFile();
                                browserPalette(files);
                            }
                        });
                    }
                    {
                        jMenu1.addSeparator();
                    }
                    {
                        JMenuItem menuItem2 = new JMenuItem("加载着色方案");
                        jMenu1.add(menuItem2);
                        menuItem2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setCurrentDirectory(dir2);
                                chooser.showOpenDialog(PaletteViewer.this);
                                File file = chooser.getSelectedFile();
                                if (file != null) {
                                    dir2 = file.getParentFile();
                                    //previewScheme(file);
                                    schemeFile = file;
                                }
                            }
                        });
                    }
                    {
                        JMenuItem menuItem2 = new JMenuItem("预览精灵着色效果");
                        jMenu1.add(menuItem2);
                        menuItem2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JFileChooser chooser = new JFileChooser();
                                chooser.setMultiSelectionEnabled(true);
                                chooser.setCurrentDirectory(dir);
                                chooser.showOpenDialog(PaletteViewer.this);
                                File[] files = chooser.getSelectedFiles();
                                if (files == null || files.length <= 0) {
                                    return;
                                }
                                dir = files[0].getParentFile();
                                for (File file : files) {
                                    WASDecoder decoder = new WASDecoder();
                                    try {
                                        decoder.load(file.getAbsolutePath());
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    decoders.put(file.getName(), decoder);
                                    previewScheme(file.getName());
                                }
                            }
                        });
                    }
                    {
                        jSeparator1 = new JSeparator();
                        jMenu1.add(jSeparator1);
                    }
                    {
                        jMenuItem2 = new JMenuItem();
                        jMenu1.add(jMenuItem2);
                        jMenuItem2.setText("退出");
                        jMenuItem2.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                    }
                }
                {
                    jMenu2 = new JMenu();
                    jMenuBar1.add(jMenu2);
                    jMenu2.setText("\u5e2e\u52a9");
                    {
                        jMenuItem3 = new JMenuItem();
                        jMenu2.add(jMenuItem3);
                        jMenuItem3.setText("关于");
                    }
                }
            }
            {
                jPanel1 = new JPanel();
                FlowLayout jPanel1Layout = new FlowLayout();
                jPanel1Layout.setAlignment(FlowLayout.LEFT);
                getContentPane().add(jPanel1, BorderLayout.SOUTH);
                jPanel1.setLayout(jPanel1Layout);
                {
                    statusLabel = new JLabel();
                    jPanel1.add(statusLabel);
                    statusLabel.setText("status");
                }
            }
            {
                tabbedPanel = new JTabbedPane();
                getContentPane().add(tabbedPanel, BorderLayout.CENTER);
            }
            initPopupMenu();
            pack();
            setSize(800, 600);
            setTitle("PaletteViewer");
            setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem menu = new JMenuItem("预览着色效果");
        menu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = tabbedPanel.getSelectedIndex();
                String title = tabbedPanel.getTitleAt(index);
                previewScheme(title);
            }
        });
        popupMenu.add(menu);
    }

    private void previewScheme(String name) {
        WASDecoder decoder = decoders.get(name);
        decoder.loadColorationProfile(schemeFile.getAbsolutePath());
        Section[] sections = decoder.getSections();
        JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < sections.length; i++) {
            int count = sections[i].getSchemeCount();
            JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            for (int s = 0; s < count; s++) {
                decoder.resetPalette();
                decoder.coloration(i, s);
                JLabel label = new JLabel(new ImageIcon(decoder.getFrameImage(0)));
                linePanel.add(label);
            }
            page.add(linePanel);
        }
        tabbedPanel.addTab(name + "着色效果", new JScrollPane(page));
    }

    private void browserPalette(File[] files) {
        Dimension lableSize = new Dimension(30, 18);
        for (File file : files) {
            WASDecoder decoder = new WASDecoder();
            try {
                decoder.load(new FileInputStream(file));
                short[] palette = decoder.getPalette();
                JPanel palettePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                JLabel colorLabel;
                int n = 0;
                for (short c : palette) {
                    colorLabel = new JLabel();
                    colorLabel.setForeground(Color.WHITE);
                    colorLabel.setOpaque(true);
                    colorLabel.setHorizontalAlignment(JLabel.RIGHT);
                    colorLabel.setPreferredSize(lableSize);
                    colorLabel.setToolTipText(n + ":"
                            + String.valueOf(Integer.toHexString(c)));
                    int r, g, b;
                    r = ((c >>> 11) & 0x1F) << 3;
                    g = ((c >>> 5) & 0x3F) << 2;
                    b = (c & 0x1F) << 3;
                    colorLabel.setBackground(new Color(r, g, b));
                    palettePanel.add(colorLabel);
                    n++;
                }
                final JSplitPane page = new JSplitPane(
                    JSplitPane.VERTICAL_SPLIT);
                page.setTopComponent(palettePanel);
                page.setBottomComponent(new JLabel(new ImageIcon(
                    decoder.getFrame(0))));
                page.setDividerLocation(480);
                page.setComponentPopupMenu(popupMenu);
                String title = file.getName();
                tabbedPanel.addTab(title, page);
                decoders.put(title, decoder);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

}
