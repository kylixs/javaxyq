package com.javaxyq.tools;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MapDialog extends javax.swing.JDialog {
	private JButton cancelButton;
	private JButton okButton;
	private JTextField heightTextField;
	private JTextField widthTextField;

	/**
	* Auto-generated main method to display this JDialog
	*/
	public static void main(String[] args) {
		MapDialog inst = new MapDialog(null,640,480);
		inst.setVisible(true);
	}
	
	public MapDialog(JFrame frame,int width,int height) {
		super(frame);
		initGUI();
		this.mapWidth=width;
		this.mapHeight=height;
		widthTextField.setText(String.valueOf(width));
		heightTextField.setText(String.valueOf(height));
		setLocationRelativeTo(frame);
	}
	private int mapWidth;
	private int mapHeight;
	private void initGUI() {
		try {
			getContentPane().setLayout(null);
			{
				JLabel jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("\u5bbd\u5ea6");
				jLabel1.setBounds(21, 42, 35, 21);
			}
			{
				JLabel jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setText("\u9ad8\u5ea6");
				jLabel2.setBounds(161, 42, 35, 21);
			}
			{
				widthTextField = new JTextField();
				getContentPane().add(widthTextField);
				widthTextField.setBounds(56, 42, 63, 21);
			}
			{
				heightTextField = new JTextField();
				getContentPane().add(heightTextField);
				heightTextField.setBounds(196, 42, 63, 21);
			}
			{
				okButton = new JButton();
				getContentPane().add(okButton);
				okButton.setText("\u786e\u5b9a");
				okButton.setBounds(49, 119, 63, 28);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							int width=Integer.parseInt(widthTextField.getText());
							int height=Integer.parseInt(heightTextField.getText());
							if(width>1024||height>768||width<=0||height<=0) {
								JOptionPane.showMessageDialog(null, "地图大小范围:长-(0,1024],宽-(0,768]!!");
							}else {
								MapDialog.this.mapWidth=width;
								MapDialog.this.mapHeight=height;
								dispose();
							}
						}catch(Exception ex) {
							JOptionPane.showMessageDialog(null, "设置地图大小时出现错误!请不要输入非数字字符!");
						}
					}
				});
			}
			{
				cancelButton = new JButton();
				getContentPane().add(cancelButton);
				cancelButton.setText("\u53d6\u6d88");
				cancelButton.setBounds(182, 119, 63, 28);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
			}
			{
				JLabel jLabel3 = new JLabel();
				getContentPane().add(jLabel3);
				jLabel3.setText("\u8bf7\u8bbe\u5b9a\u5730\u56fe\u53ef\u89c6\u533a\u57df\u5927\u5c0f");
				jLabel3.setBounds(21, 14, 245, 21);
			}
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setModal(true);
			setTitle("地图大小");
			this.setSize(300, 200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Dimension getVisibleSize() {
		return new Dimension(mapWidth,mapHeight);
	}
}
