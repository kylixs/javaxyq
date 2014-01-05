package com.javaxyq.tools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;

import com.javaxyq.ui.CenterLayout;
import com.javaxyq.ui.Label;
import com.javaxyq.widget.Sprite;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideComboBox;

/**
 * 精灵预览
 * 
 * @author 龚德伟
 * @history 2008-7-2 龚德伟 新建
 */
public class SpriteDialog extends JDialog {
	private static final long serialVersionUID = 8029400278451880408L;

	// TODO  拖动精灵的方向(类似旋转)
    private Sprite sprite;

    private Label animLabel;

    private FileObject fileObject;

    private JCheckBox autoPlayCheckbox;

    private JideComboBox dirctionComboBox;

    private ApplicationActionMap actionMap;

	private Timer timer;

	private JPanel animPanel;

	private JideComboBox frameComboBox;
	
	private static JFileChooser fileChooser;

    public SpriteDialog(JFrame owner,FileObject fileObject) {
    	super(owner);
        this.fileObject = fileObject;
        sprite = XYQTools.createSprite(fileObject);
        actionMap = Application.getInstance().getContext().getActionMap(this);
        initGUI(fileObject);
        this.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowActivated(WindowEvent e) {
        		if(timer == null) {
        		timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						repaint();				
					}
				}, 100, 100);
        		}
			}
			public void windowClosing(WindowEvent e) {
				timer.cancel();		
				sprite = null;
				remove(animPanel);
				animPanel = null;
				animLabel.setAnim(null);
			}
		});
    }

    private void initGUI(final FileObject fileObject) {
        animLabel = new Label(sprite.getCurrAnimation());
        animLabel.setDebug(true);
        animLabel.setBorder(new LineBorder(Color.LIGHT_GRAY));
        animPanel = new JPanel(new CenterLayout());
        animPanel.add("Center", animLabel);
        //animPanel.setBackground(Color.BLACK);
        
        String[] items = new String[sprite.getAnimationCount()];
        for (int i = 0; i < items.length; i++) {
            items[i] = "方向" + i;
        }
        dirctionComboBox = new JideComboBox(items);
        dirctionComboBox.setPreferredSize(new Dimension(80, 22));
        dirctionComboBox.setEditable(false);
        dirctionComboBox.setFocusable(false);
        dirctionComboBox.setAction(actionMap.get("changeDirection"));
        JPanel controlPanel = new JPanel();
        controlPanel.add(dirctionComboBox);
        autoPlayCheckbox = new JCheckBox(actionMap.get("autoPlay"));
        autoPlayCheckbox.setSelected(true);
        autoPlayCheckbox.setFocusable(false);
        controlPanel.add(autoPlayCheckbox);
        
        JButton btnPlay = new JButton(actionMap.get("play"));
        btnPlay.setMargin(new Insets(2, 2, 2, 2));
        controlPanel.add(btnPlay);
        JButton btnFirst = new JButton(actionMap.get("first"));
        btnFirst.setMargin(new Insets(2, 2, 2, 2));
        controlPanel.add(btnFirst);
        JButton btnPrev = new JButton(actionMap.get("prev"));
        btnPrev.setMargin(new Insets(2, 2, 2, 2));
        controlPanel.add(btnPrev);
        String[] frameitems = new String[sprite.getCurrAnimation().getFrameCount()];
        for (int i = 0; i < frameitems.length; i++) {
        	frameitems[i] = "frame " + i;
        }
        frameComboBox = new JideComboBox(frameitems);
        frameComboBox.setPreferredSize(new Dimension(80, 22));
        frameComboBox.setEditable(false);
        frameComboBox.setFocusable(false);
        frameComboBox.setAction(actionMap.get("changeFrame"));
        controlPanel.add(frameComboBox);
        JButton btnNext = new JButton(actionMap.get("next"));
        btnNext.setMargin(new Insets(2, 2, 2, 2));
        controlPanel.add(btnNext);
        JButton btnLast = new JButton(actionMap.get("last"));
        btnLast.setMargin(new Insets(2, 2, 2, 2));
        controlPanel.add(btnLast);
        
        
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(" 预览 "), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        previewPanel.add(animPanel, BorderLayout.CENTER);
        previewPanel.add(controlPanel, BorderLayout.SOUTH);

        JPanel infoPanel = new JPanel(new GridLayout(12, 1, 6, 6));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(" 信息 "), BorderFactory.createEmptyBorder(4, 4, 4, 4)));
        infoPanel.add(new JLabel("name: \t" + fileObject.getName()));
        if (fileObject instanceof WdfFileNode) {
			WdfFileNode node = (WdfFileNode) fileObject;
			infoPanel.add(new JLabel("id: \t" + Long.toHexString(node.getId()).toUpperCase()));
			infoPanel.add(new JLabel("offset: \t" + Long.toHexString(node.getOffset()).toUpperCase()));
		}
        infoPanel.add(new JLabel("size: \t" + (fileObject.getSize()*1.0/1024)+" KB"));
        //infoPanel.add(new JLabel("space: \t" + fileObject.getSpace()));
        infoPanel.add(new JLabel("path: \t" + fileObject.getPath()));
        infoPanel.add(new JSeparator());
        infoPanel.add(new JLabel("FrameCount: \t" + sprite.getAnimationCount()));
        infoPanel.add(new JLabel("width: \t" + sprite.getWidth()));
        infoPanel.add(new JLabel("height: \t" + sprite.getHeight()));
        infoPanel.add(new JLabel("RefPixelX: \t" + sprite.getRefPixelX()));
        infoPanel.add(new JLabel("RefPixelY: \t" + sprite.getRefPixelY()));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(previewPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.EAST);
        
        //FIXME buttons
        ButtonPanel buttonPanel = new ButtonPanel();
        JideButton closeButton = new JideButton("关闭");
        closeButton.setName("closeButton");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SpriteDialog.this.dispose();
            }
        });
        
        JideButton exportButton = new JideButton("导出");
        exportButton.setName("exportButton");
        exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fileChooser == null) {
					fileChooser = new  JFileChooser();
				}
				fileChooser.setSelectedFile(new File(fileObject.getName()));
				int opt = fileChooser.showSaveDialog(SpriteDialog.this);
				if(opt == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						byte[] buf = fileObject.getData();
						FileOutputStream fos = new FileOutputStream(file);
						fos.write(buf);
						fos.close();
					} catch (IOException e1) {
						System.out.println("导出精灵失败！"+fileObject.getName());
						e1.printStackTrace();
					}
					
				}
			}
		});
        
//        JideButton prevButton = new JideButton("prev");
//        prevButton.setName("prevButton");
//        JideButton nextButton = new JideButton("next");
//        nextButton.setName("nextButton");
//        
        buttonPanel.addButton(exportButton);
//        buttonPanel.add(prevButton);
//        buttonPanel.add(nextButton);
        buttonPanel.add(Box.createHorizontalBox());
        buttonPanel.addButton(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(contentPanel, BorderLayout.CENTER);
//        setModal(true);
        setAlwaysOnTop(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    dispose();
                    SpriteDialog.this.getParent().remove(SpriteDialog.this);
                }
            }
        });
    }

    @Action
    public void autoPlay() {
        this.sprite.setAutoPlay(autoPlayCheckbox.isSelected());
    }
    @Action
    public void first() {
    	this.sprite.setRepeat(0);
    	this.sprite.getCurrAnimation().setIndex(0);
    	frameComboBox.setSelectedIndex(this.sprite.getCurrAnimation().getIndex());
    }
    @Action
    public void prev() {
    	this.sprite.setRepeat(0);
    	int index = this.sprite.getCurrAnimation().getIndex()-1;
    	this.sprite.getCurrAnimation().setIndex(index>0?index:0);
    	frameComboBox.setSelectedIndex(this.sprite.getCurrAnimation().getIndex());
    }
    @Action
    public void next() {
    	this.sprite.setRepeat(0);
    	int index = this.sprite.getCurrAnimation().getIndex()+1;
    	int count = this.sprite.getCurrAnimation().getFrameCount();
    	index = index<count-1?index:count-1;
		this.sprite.getCurrAnimation().setIndex(index);
    	frameComboBox.setSelectedIndex(this.sprite.getCurrAnimation().getIndex());
    }
    @Action
    public void last() {
    	this.sprite.setRepeat(0);
    	int count = this.sprite.getCurrAnimation().getFrameCount();
    	this.sprite.getCurrAnimation().setIndex(count-1);
    	frameComboBox.setSelectedIndex(count-1);
    }
    @Action
    public void changeFrame() {
    	this.sprite.setRepeat(0);
        int index = frameComboBox.getSelectedIndex();
    	this.sprite.getCurrAnimation().setIndex(index);
    }

    @Action
    public void play() {
    	this.sprite.resetFrames();
    	this.sprite.setRepeat(1);
    }

    @Action
    public void changeDirection() {
        int index = dirctionComboBox.getSelectedIndex();
        this.sprite.setDirection(index);
        this.animLabel.setAnim(this.sprite.getCurrAnimation());
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public void setFileObject(FileObject fileObject) {
        this.fileObject = fileObject;
        //FIXME 
    }
}
