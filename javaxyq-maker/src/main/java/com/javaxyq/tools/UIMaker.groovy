
package com.javaxyq.tools;
/*
 * JavaXYQ Source Code 
 * UIMaker UIMaker.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */

import com.javaxyq.util.ClassUtil;
import com.javaxyq.util.ClosureTask;
import com.javaxyq.ui.Panel;


import com.javaxyq.config.ImageConfig;
import javax.swing.table.TableModel;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.Resizable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.util.List;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.ChangeEvent;

import org.codehaus.groovy.runtime.StringBufferWriter;


import groovy.model.ValueHolder;
import groovy.model.ValueModel;
import groovy.swing.SwingBuilder;
import groovy.swing.factory.TableModelFactory;
import com.javaxyq.ui.XmlDialogBuilder;
import com.javaxyq.ui.*;
import com.javaxyq.core.*;
import groovy.util.*;
import java.beans.PropertyChangeListener;
/**
 * @author dewitt
 *
 */
public class UIMaker extends MouseAdapter implements KeyListener,ListSelectionListener,
	DocumentListener,TableModelListener, ChangeListener{
	
	private JFrame frame;
	
	private SwingBuilder builder;
	
	private XmlDialogBuilder xmlDlgBuilder;
	
	private Node rootNode;
	private Node dialogNode;
	private Panel dialog;
	private List dialogs;
	private List nodes;
	private Map node2comp = [:]
	private Map comp2node = [:]
	                         
	private JDialog openfileDialog;
	private JDialog savefileDialog;
	private JDialog editNodeDialog;
	private JDialog createNodeDialog;
	
	private String currentDir = "ui";
	private File selectedFile;
	private File selectedFolder;
	
	private boolean fileDirty;
	private boolean autoPreview;
	
	private Action cutNodeAction;
	private Action copyNodeAction;
	private Action pasteNodeAction;
	private Action editNodeAction;
	private Action deleteNodeAction;
	
		
	public UIMaker() {
		xmlDlgBuilder = new XmlDialogBuilder();
		this.initBuilder();
		this.initGUI();
	}
	
	private void initBuilder() {
		builder = new SwingBuilder();
		builder.registerFactory('tableModel', new MyTableModelFactory());		
	}	
	
	private void initGUI() {
		if(frame!=null) {
			return ;
		}
		
		def about = builder.action(
			name: 'About',
			closure: this.&showAbout,
			mnemonic: 'A',
			accelerator: 'F1'
		)
		// 文件菜单
		def newAction = builder.action(
			name: '新建(N)',
			closure: this.&newFile,
			mnemonic: 'N'
		)
		def openAction = builder.action(
				name: '打开',
				closure: this.&openFile,
				accelerator: 'ctrl O'
		)
		def closeAction = builder.action(
				name: '关闭',
				closure: this.&closeFile,
				accelerator: 'ctrl W'
		)
		def saveAction = builder.action(
				name: '保存',
				closure: this.&saveFile,
				accelerator: 'ctrl S'
		)
		def saveAsAction = builder.action(
				name: '另存为',
				closure: this.&saveAsFile,
				accelerator: 'ctrl alt S'
		)
		def exitAction = builder.action(
				name: '退出',
				closure: this.&exit,
				mnemonic: 'X'
		)
		//编辑菜单
		def newNodeAction = builder.action(
				name: '创建结点(N)..',
				closure: this.&showCreateNodeDialog,
				mnemonic: 'N',
				accelerator: 'ctrl N'
		)
		cutNodeAction = builder.action(
				name: '剪切结点(C)',
				closure: this.&cutNode,
				enabled : false,
				mnemonic: 'X',
				accelerator: 'ctrl X'
		)
		copyNodeAction = builder.action(
				name: '复制结点(C)',
				closure: this.&copyNode,
				enabled : false,
				mnemonic: 'C',
				accelerator: 'ctrl C'
		)
		pasteNodeAction = builder.action(
				name: '粘贴结点(P)',
				enabled : false,
				closure: this.&pasteNode,
				mnemonic: 'P',
				accelerator: 'ctrl V'
		)
		editNodeAction = builder.action(
				name: '修改属性',
				enabled : false,
				closure: this.&editNode,
				accelerator: 'ctrl M',
		)
		deleteNodeAction = builder.action(
				name: '删除',
				enabled : false,
				closure: this.&deleteNode,
				accelerator: 'ctrl D',
		)
		
		//操作菜单
		def previewAction = builder.action(
				name: '生成预览',
				closure: this.&preview,
				mnemonic: 'P',
				accelerator: 'F9'
		)
		def saveChangesAction = builder.action(
				name: '更新到代码',
				closure: this.&recreateXML,
				mnemonic: 'S',
				accelerator: 'F8'
		)
		def rebuildAction = builder.action(
				name: '刷新预览',
				closure: this.&rebuild,
				mnemonic: 'R',
				accelerator: 'F12'
		)
		def lineWrapAction = builder.action(
				name: '自动换行',
				closure: {builder.code.lineWrap = !builder.code.lineWrap},
				accelerator: 'ctrl alt W'
		)
		def fontAction = builder.action(
				name: '字体(F)',
				closure: this.&changeFont,
				mnemonic: 'F',
		)
		

		//主窗口内容
		this.frame = builder.frame(size: [830, 620],title: 'UI Maker for JavaXYQ',
				defaultCloseOperation:WindowConstants.DO_NOTHING_ON_CLOSE) {
			menuBar(){
				menu(mnemonic: 'F',"文件(F)"){
					menuItem(action: newAction)
					menuItem(action: openAction)
					separator();
					menuItem(action: closeAction)
					separator()
					menuItem(action: saveAction)
					menuItem(action: saveAsAction)
					separator()
					menuItem(action: exitAction)
				}
				menu(mnemonic: 'E',"编辑(E)"){
//					menuItem('撤销'){}
//					menuItem('重做'){}
//					separator()
					menuItem(action: newNodeAction)
					menuItem(action: editNodeAction)
					separator()
					menuItem(action: cutNodeAction)
					menuItem(action: copyNodeAction)
					menuItem(action: pasteNodeAction)
					separator()
					menuItem(deleteNodeAction)
//					menuItem('全选'){}
					
				}
				menu(mnemonic: 'A',"对齐(A)"){
					menuItem('左对齐'){}
					menuItem('水平中对齐'){}
					menuItem('右对齐'){}
					separator()
					menuItem('顶对齐'){}
					menuItem('垂直中对齐'){}
					menuItem('底对齐'){}
					separator()
					menuItem('精灵对齐'){}
				}
				menu(mnemonic:'O','操作(O)'){
					menuItem(action: previewAction)		
					menuItem(action: rebuildAction)		
					separator()
					menuItem(action: saveChangesAction)		
				}
				menu(mnemonic: 'T',"其它(T)"){
					menuItem(action: lineWrapAction)		
					menuItem(action: fontAction)		
				}
				glue()
				menu(mnemonic:'H','Help'){
					menuItem(action:about)
				}
			}
			panel(border:BorderFactory.createEmptyBorder(0,6,0,6)){
				borderLayout()
				vbox(constraints: BorderLayout.CENTER){
					tabbedPane(id:'tabbedPane'){
						scrollPane(title:'代码'){
							textArea(id:'code',tabSize:2,font: new Font('DialogInput',Font.PLAIN,14)){
								
							}
						}
						panel(title:'预览'){
							borderLayout()
							vbox(id:'canvas',constraints: BorderLayout.WEST,border:BorderFactory.createTitledBorder('界面预览区域')){							
							}
							panel(constraints: BorderLayout.CENTER,border:BorderFactory.createTitledBorder('面板列表')){
								boxLayout(axis:BoxLayout.Y_AXIS)
								panel(){
									borderLayout()
									scrollPane(constraints: BorderLayout.NORTH,border:new EmptyBorder(0,0,5,0),
											preferredSize:[500,100]){										
										vbox(id:'dialoglist'){
											
										}
									}
									scrollPane(constraints: BorderLayout.CENTER){
										list(id:'layerlist',
												model: new DefaultListModel(),
												//items : ['background','button'],
												dragEnabled : true,
												dropMode : DropMode.INSERT,
												transferHandler : new LayerListTransferHandler()
										)
									}
								}
							}
						}
					}
				}
				panel (constraints: BorderLayout.SOUTH, layout:new FlowLayout(FlowLayout.LEADING),preferredSize:[800,30],
				) {
					label(id:'msglabel', preferredSize:[500,20],text:'按F9键进行预览')
					label '|'
					label(id:'indexlabel', preferredSize:[60,20])
					label '|'
					label(id:'poslabel', preferredSize:[100,20])
					label '|'
					label(id:'sizelabel', preferredSize:[100,20])
				}
			}
		}
		//install real canvas
		def canvas = new SimpleCanvas();
		builder.canvas.add(canvas);
		builder.canvas = canvas;
		
		//图层列表右键菜单
		def popupMenu = builder.popupMenu(){
			menuItem(action: editNodeAction)
			menuItem(action: copyNodeAction)
			menuItem(action: pasteNodeAction)
			separator()
			menuItem(action: newNodeAction)
			separator()
			menuItem(action: deleteNodeAction)
		}
		builder.layerlist.setComponentPopupMenu(popupMenu);
		
		
		//监听事件
		this.frame.windowClosing = {exit()}
		builder.canvas.addMouseListener(this);
		builder.canvas.addKeyListener(this);
		builder.code.document.addDocumentListener(this)
		builder.layerlist.addPropertyChangeListener('LayerOrder',new LayerOrderChangeListener(this));
		builder.layerlist.addListSelectionListener(this)
		builder.layerlist.mouseClicked= {MouseEvent e ->
			//双击编辑结点
			if(e.getButton()==MouseEvent.BUTTON1 && e.clickCount == 2) {
				this.editNode(e);					
			}
		}
		builder.tabbedPane.addChangeListener(this);
		
		this.initDialogs();
	}

	private void initDialogs() {
		def changeDirAction = builder.action(
			name: '改变当前目录',
			closure: this.&changeCurrentDir
		)
		def browserAction = builder.action(
			name: '浏览(B)..',
			mnemonic: 'B',
			closure: this.&openDirBrowser
		)
		def chooseAction = builder.action(
			name: '打开(O)',
			mnemonic: 'O',
			closure: this.&openSelectedFile
		)
		def closeAction = builder.action(
			name: '关闭(C)',
			mnemonic: 'C',
			closure: {this.openfileDialog.dispose()}
		)
		def autoPreviewAction = builder.action(
				name: '打开后自动预览(P)',
				mnemonic: 'P',
				closure: {
					this.autoPreview = builder.btnAutoPreview.selected;
				}
		)
		
		this.openfileDialog = builder.dialog(owner:this.frame,title: '打开文件',size:[450,300],modal:true){
			borderLayout()
			hbox(constraints: BorderLayout.NORTH){
				label '当前目录：'
				textField(id:'dirfield',action: changeDirAction,preferredSize:[300,20]) 
				button(action: browserAction)
			}
			scrollPane(constraints: BorderLayout.CENTER){
				table(id:'filelistTable',showGrid:false,rowSelectionAllowed:true){
					tableModel() {
						propertyColumn(header:'名称', propertyName:'name')
						closureColumn(header:'大小',read:{it.length().intdiv(1024)+' KB'})				
						closureColumn(header:'类型', read:{it.name.afterLast('.')+' 文件'})
						closureColumn(header:'修改日期', read:{new Date(it.lastModified()).format('yyyy-MM-dd HH:mm')})
					}
				}
			}
			panel(constraints: BorderLayout.SOUTH,layout:new FlowLayout(FlowLayout.RIGHT)){
				checkBox(id:'btnAutoPreview', action: autoPreviewAction)
				hglue()
				button(action: chooseAction)
				button(action: closeAction)
			}
		}
		
		this.savefileDialog = builder.dialog(title:'保存文件'){
			
		}
		
		//属性编辑（通用）
		def modifyNodeAction =  builder.action(
			name: '修改(M)',
			mnemonic: 'M',
			enabled : false,
			closure: this.&modifyNode
		)
		this.editNodeDialog = builder.dialog(owner:this.frame ,title:'编辑结点属性',size:[300,300],modal:true){
			borderLayout()
			scrollPane(constraints: BorderLayout.CENTER){
				table(id:'editNodeTable',showGrid:false,rowSelectionAllowed:true){
					tableModel() {
						propertyColumn(header:'名称', propertyName:'key',editable:false)
						propertyColumn(header:'值', propertyName:'value',type:Node.class)
					}
				}
			}
			panel(constraints: BorderLayout.SOUTH){
				button(id:'btnModifyNode',action: modifyNodeAction)
				hstrut()
				button('关闭(C)',mnemonic: 'C',actionPerformed: {this.editNodeDialog.dispose()})
			}
		}
		builder.editNodeTable.model.addTableModelListener(this);
		builder.editNodeTable.setDefaultEditor(Node.class,new PropertyCellEditor());
		this.editNodeDialog.windowClosing = {WindowEvent e ->
				if(builder.editNodeTable.isEditing()) {
					builder.editNodeTable.cellEditor.cancelCellEditing();
				}
			};
		
		//创建结点对话框
		def changeNodeTypeAction =  builder.action(
				name: '修改结点类型',
				closure: this.&changeNodeType
		)
		def createNodeAction =  builder.action(
				name: '创建(C)',
				mnemonic: 'C',
				closure: this.&createNode
		)
		this.createNodeDialog = builder.dialog(owner:this.frame ,title:'创建结点',size:[300,300],modal:true){
			comboBox(id:'nodeType',items:['Dialog','Button','Text','RichText','Sprite','Image','Editor','Action'],
					constraints: BorderLayout.NORTH, action: changeNodeTypeAction, border: new EmptyBorder(4, 4, 4, 4));
			scrollPane(constraints: BorderLayout.CENTER){
				table(id:'newNodeTable',showGrid:false,rowSelectionAllowed:true){
					tableModel() {
						propertyColumn(header:'名称', propertyName:'key',editable:false)
						propertyColumn(header:'值', propertyName:'value',type:Node.class)
					}
				}
			}
			panel(constraints: BorderLayout.SOUTH, border: new EmptyBorder(0, 4, 0, 4)){
				button(id:'btnCreateNode',action: createNodeAction)
				hstrut()
				button('关闭(C)',mnemonic: 'C',actionPerformed: {this.createNodeDialog.dispose()})
			}
		}
		builder.newNodeTable.model.addTableModelListener(this);
		builder.newNodeTable.setDefaultEditor(Node.class,new PropertyCellEditor());
		this.createNodeDialog.windowClosing= {WindowEvent e ->
			if(builder.newNodeTable.isEditing()) {
				builder.newNodeTable.cellEditor.cancelCellEditing();
			}
		};
	}
	
	private void activeTab(int index) {
		builder.tabbedPane.selectedIndex = index;
	}
	
	private void changeCurrentDir(evt) {
		this.currentDir = builder.dirfield.text;
		def files = new File(this.currentDir).listFiles().findAll{it.name.endsWith('.xml')};
		def rowsModel = builder.filelistTable.model.rowsModel;
		rowsModel.value = files;
		builder.filelistTable.validate();
		builder.filelistTable.updateUI();
		//println "files: ${files.size}"
	}
	private FolderChooser folderChooser = new FolderChooser();
	private List<String> recentList = [];
	private void openDirBrowser(evt) {
		folderChooser.setRecentList(recentList.unique());
		recentList = folderChooser.getRecentList();
		int result = folderChooser.showOpenDialog(openfileDialog);
		if (result == FolderChooser.APPROVE_OPTION) {
			this.selectedFolder = folderChooser.getSelectedFile();
			builder.dirfield.text = this.selectedFolder.path
			this.changeCurrentDir();
			recentList << this.selectedFolder.path;
		}		
	}
	private void openSelectedFile(evt) {
		activeTab(0)
		this.openfileDialog.dispose();
		if(closeFile(evt)) {		
			def files = builder.filelistTable.model.rowsModel.value;
			def row = builder.filelistTable.selectedRow;
			this.selectedFile = files[row];
			
			this.frame.title = "UIMaker - ${selectedFile}"
				builder.code.text = "";
			selectedFile.eachLine{
				builder.code.append(it)
				builder.code.append('\n')
			}
			builder.code.setCaretPosition(0)
			this.setDirty(false)
			if(this.autoPreview) {
				preview();
			}
		}
	}
	
	public void show() {
//		NullRepaintManager.install();
		offscreenImage = new BufferedImage(640, 480, BufferedImage.TYPE_USHORT_565_RGB);
		offscreenGraphics = (Graphics2D) offscreenImage.getGraphics();
		this.frame.setLocationRelativeTo(null);
		this.frame.setVisible(true);
		new java.util.Timer().scheduleAtFixedRate(new PaintTask(this), 100, 100);
	}
	//============ actions =============/
	void showAbout(event) {
		JOptionPane.showMessageDialog(frame,
		'''UIMaker for JavaXYQ
			by kylixs 2009.10.31
				 qq:307358033 or mail: kylixs@qq.com
		build with Groovy SwingBuilder''')
	}	
	
	void preview(evt) {
		new java.util.Timer().schedule(new ClosureTask({
			buildUI()
			activeTab(1);
		}), 0);
	}
	
	synchronized void buildUI() {
		builder.msglabel.text = '正在解析界面描述文件...';
		long startTime = System.currentTimeMillis();
		
		//build model
		def xml = builder.code.text
		this.rootNode = new XmlParser().parseText(xml);
		rebuild();
		long endTime = System.currentTimeMillis();
		builder.msglabel.text = "界面预览完成，共花费时间: ${(endTime-startTime)/1000}s";
	}
	
	synchronized void rebuild() {	
		this.node2comp = [:];
		this.comp2node = [:];
		this.dialogs = [];
		this.nodes = [];
		if(rootNode.name() == 'Dialog') {
			this.dialogs << buildDialog(rootNode);
			this.nodes << rootNode
		}else {
			for(def el in rootNode.Dialog) {
				this.dialogs << buildDialog(el);
				this.nodes << el
			}
		}
		if(this.nodes.size()==0) {
			return;
		}
		this.dialog = this.dialogs[0];
		this.dialogNode = this.nodes[0];
		
		//refresh canvas & layerlist
		builder.canvas.removeAll()
		for(dlg in this.dialogs) {		
			builder.canvas.add(dlg)
		}
		builder.dialoglist.removeAll();
		for(def node in this.nodes) {
			builder.dialoglist.add(createLayerItem(node))
		}
		
		def listmodel =builder.layerlist.model; 
		listmodel.removeAllElements();
		listmodel.addElement(new LayerObject(dialogNode))
		for(node in this.dialogNode.children()) {			
			listmodel.addElement(new LayerObject(node))
		}
		builder.layerlist.selectedIndex = 0		
		builder.canvas.requestFocus(true);		
	}

	private Panel buildDialog(node) {
		builder.msglabel.text = "解析${node.@id}..."
		Panel dialog = xmlDlgBuilder.processDialog(node);
		//components
		node2comp[node] = dialog;
		comp2node[dialog] = node;
		for(def el in node.children()) {
			try {
				processNode(dialog,el);
			}catch(e) {
				println "处理控件失败：${el.name()} ${el.attributes()}"
				e.printStackTrace();
			}
		}
		//builder.canvas.add(dialog);
		dialog.mouseListeners.each{
			dialog.removeMouseListener(it)
		}
		dialog.mouseMotionListeners.each{
			dialog.removeMouseMotionListener(it)
		}
		dialog.addMouseListener this
		dialog.addMouseMotionListener this
		dialog.addKeyListener this
		return dialog;
	}
	
	private Component processNode(Panel dialog,Node el) {
		//FIXME 去掉ActionId，如果有Action导致显示有问题？？
		def actionId = el.@actionId;
		el.@actionId = '';
		def comp = xmlDlgBuilder.invokeMethod ('process'+el.name(),[dialog,el])
		el.@actionId = actionId;
		if(comp) {
			node2comp[el] = comp;
			comp2node[comp] = el;
			try {
				comp.actionListeners.each{
					comp.removeActionListener(it);
				}
			}catch(e) {}
			comp.addMouseListener this
			comp.addMouseMotionListener this
			comp.addKeyListener this
		}
		return comp;
	}
	
	private JCheckBox createLayerItem(Node node) {
		JCheckBox checkbox = new JCheckBox(node.@id.afterLast('.'),true);
		checkbox.actionPerformed= {ActionEvent e
			JComponent comp = node2comp[node]; 
			if(checkbox.isSelected()) {
				builder.canvas.add(comp,0)
				markComp(comp);
			}else {
				builder.canvas.remove(comp)
			}
		};
		return checkbox;
	}
	
	void recreateXML(evt) {
		//def xml = builder.code.text
		activeTab(0);
		StringWriter strWriter = new StringWriter();
		strWriter << '<?xml version="1.0" encoding="UTF-8"?>\n'
		XmlNodePrinter printer = new XmlNodePrinter(new PrintWriter(strWriter))
		printer.print rootNode
		builder.code.text = strWriter.toString()
		println 'recreate xml done!'
	}
	
	//=========== Menu: 文件  ==============//
	private static final String NEWFILE_CONTENT = 
"""<Dialogs>
	
</Dialogs>"""
		
	void newFile(event) {
		if(closeFile(event)) {
			builder.code.text = NEWFILE_CONTENT;
			this.selectedFile = null;
			setDirty(false);
		}
	}
	
	void openFile(event) {
		builder.dirfield.text = this.currentDir;
		this.changeCurrentDir();
		this.openfileDialog.setLocationRelativeTo(frame);
		this.openfileDialog.show();
		
	}
	boolean closeFile(event) {
		if(this.fileDirty) {
			def choice = JOptionPane.showConfirmDialog(this.frame,"要将修改保存到${selectedFile}吗？ ", 'UIMaker',
					JOptionPane.YES_NO_CANCEL_OPTION);
			if(choice == JOptionPane.CANCEL_OPTION) {
				return false;
			}else if(choice == JOptionPane.YES_OPTION) {
				this.saveFile(event)				
			}
		}
		this.selectedFile = null;
		builder.code.text = '';
		setDirty(false);
		return true;
	}
	
	void saveFile(event) {
		if(this.selectedFile) {
			storeToFile(this.selectedFile);
			return;
		}
		while(true) {
			//open savefile dialog
			def filename = JOptionPane.showInputDialog(this.frame, "请输入保存的文件名：","ui-${new Date().format('yyyyMMdd-HHmm')}.xml");
			if(!filename) break;
			def newFile = new File(filename);
			if(newFile.exists()) {
				int choice = JOptionPane.showConfirmDialog(this.frame,"文件${filename}已存在，要覆盖吗？",'UI Maker',JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					storeToFile(newFile);
					break;
				}
			}else {
				storeToFile(newFile);
				break;
			}
		}
	}
	
	void saveAsFile(event) {
		if(!this.selectedFile) {
			saveFile(event);
			return;
		}
		while(true) {
			//open savefile dialog
			def filename = JOptionPane.showInputDialog(this.frame, "请输入另存为的文件名：","copy-${selectedFile.name}");
			if(!filename) break;
			def newFile = new File(filename);
			if(newFile.exists()) {
				int choice = JOptionPane.showConfirmDialog(this.frame,"文件${filename}已存在，要覆盖吗？",'UI Maker',JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					storeToFile(newFile);
					break;
				}
			}else {
				storeToFile(newFile);
				break;
			}
		}
	}
	
	void storeToFile(File file) {
		this.selectedFile = file;
		this.selectedFile.withWriter('UTF-8'){writer ->
			writer << builder.code.text
		}
		this.setDirty(false)
		println "[${new Date().format('HH:mm:ss')}]save to file: ${selectedFile.path}"
		builder.msglabel.text = "[${new Date().format('HH:mm:ss')}]: save to ${selectedFile.path}."
	}
	
	void exit(event) {
		//关闭文件
		if( closeFile(event)) {
			//退出
			println '正常退出程序  at '+new Date().format('HH:mm:ss')
			System.exit(0);
		}
	}
	//========== Menu: 插入 ================//
	
	void changeFont(evt) {
		//TODO change font
	}
	
	//============ Mouse Event ==============//
	private JComponent dragComp;
	private Point lastPosition;
	void mousePressed(MouseEvent evt) {
		//println "mousePressed: $evt"
		builder.canvas.requestFocus(true)
		JComponent comp = evt.getComponent();
		//如果点击在canvas空白处
		if(comp == builder.canvas) {
			return;
		}
		if(evt.getButton() != MouseEvent.BUTTON1) {
			return;
		}
		//如果点击在边框上不进行拖曳处理（不影响resize）
		if(overBorder(comp,evt.getPoint())) {
			return;
		}
		//双击编辑结点
		if(evt.getClickCount() == 2) {
			this.editNode();
			return;
		}
		
		this.dragComp = comp; 
		this.lastPosition = evt.point;
		markComp(comp);
		
		this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}
	public void mouseReleased(MouseEvent e) {
		this.dragComp = null;
		this.lastPosition = null;
		this.frame.setCursor(Cursor.getDefaultCursor());
	}
	public void mouseDragged(MouseEvent evt){
		if (lastPosition != null /*&& movable*/) {
			def node = this.comp2node[dragComp];
			if(!node)return;
			Point p = dragComp.getLocation()
			p.translate((evt.x -lastPosition.x).intValue(), (evt.y-lastPosition.y).intValue())
			moveComponent(dragComp,p.x.intValue(),p.y.intValue())
		}
	}
	private Component findTopPanel(Component c) {
		for(;c!=null;) {
			if (c instanceof com.javaxyq.ui.Panel) {
				break;
			}
			c=c.parent
		} 
		return c;
	}
	
	boolean overBorder(JComponent c,Point p) {
		Border border = c.getBorder();
		if(border) {
			Insets insets = border.getBorderInsets(c);
			if(p.x<= insets.left || p.x >= c.width-insets.right || p.y <= insets.top ||p.y >= c.height-insets.bottom) {
				return true;
			}
		}
		return false;
	}
	
	//-------------------- Node Operations -----------------------------//
	void deleteNode(evt) {
		def layerObj = builder.layerlist.selectedValue;
		Node node = layerObj?layerObj.node:null;
		if(node) {
			if(node.name()=='Dialog') {
				JOptionPane.showMessageDialog(this.frame, '不允许删除Dialog结点！如有必要，请手动删除。', 'UI Maker', 
						JOptionPane.CLOSED_OPTION);
				return;
			}

			StringWriter strWriter = new StringWriter();
			node.print(new PrintWriter(strWriter))
			println strWriter
			int choice = JOptionPane.showConfirmDialog(this.frame,"""
					要删除结点吗？
					${strWriter}
					""",
					'UI Maker',JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {
				//node.parent.remove(node); do not work?
				this.dialogNode.remove(node);
				def c = this.node2comp[node];
				if(c) c.parent.remove(c);
				this.node2comp.remove(node);
				this.comp2node.remove(c);
				builder.layerlist.model.removeElement(layerObj);
				println "删除结点：$node"
			}
		}
	}
	
	private Node editingNode;
	//编辑结点属性
	void editNode(evt) {
		def layerObj = builder.layerlist.selectedValue;
		def node = layerObj?layerObj.node: null;
		if(node) {
			builder.editNodeTable.model.rowsModel.value = (node.attributes().entrySet() as List);
			builder.editNodeTable.validate();
			builder.editNodeTable.updateUI();
			builder.btnModifyNode.enabled = false;
			this.editingNode = node;
			this.editNodeDialog.setLocationRelativeTo(this.frame);
			this.editNodeDialog.setVisible(true);
			println "editNode: ${node.attributes()}"
		}
	}
	
	//修改结点属性
	void modifyNode(evt) {
		if(builder.editNodeTable.isEditing()) {
			builder.editNodeTable.cellEditor.stopCellEditing();
		}
		def values = builder.editNodeTable.model.rowsModel.value
		values.each{ entry ->
			this.editingNode."@${entry.key}" = entry.value;
		}
		println "modify: $editingNode"
		//刷新预览！
		//删除原来的控件
		Component oldComp = this.node2comp[this.editingNode];
		if(oldComp) {
			if (oldComp instanceof Panel) {//如果是面板则修改属性
				Panel dlg = oldComp;
				try {
					dlg.setLocation(editingNode.@x.toInteger(),editingNode.@y.toInteger());
				}catch(e) {}
				try {
					dlg.setSize(editingNode.@width.toInteger(),editingNode.@height.toInteger());
				}catch(e) {}
				try {
					dlg.setBgImage(editingNode.@background?new ImageConfig(editingNode.@background):null);
				}catch(e) {}
				//TODO 修改面板列表中显示的名称
				builder.dialoglist.removeAll();
				for(def node in this.nodes) {
					builder.dialoglist.add(createLayerItem(node))
				}
				
			}else {//如果是控件则重新生成
				this.comp2node.remove(oldComp);
				int index = this.dialog.getComponentZOrder(oldComp);
				this.dialog.remove(oldComp);
				Component newComp = this.processNode(this.dialog, editingNode);
				this.dialog.setComponentZOrder(newComp, index);
				selectComp(newComp);
			}
		}
		this.editNodeDialog.dispose();
	}
	
	//显示创建结点对话框
	void showCreateNodeDialog(event) {
		if(!this.rootNode) preview(event);
		changeNodeType(event);		
		this.createNodeDialog.setLocationRelativeTo(this.frame);
		this.createNodeDialog.show();
	}
	//创建结点
	void createNode(event) {
		Map attributes = [:];
		if(builder.newNodeTable.isEditing()) {
			builder.newNodeTable.cellEditor.stopCellEditing();
		}
		def values = builder.newNodeTable.model.rowsModel.value
		values.each{ entry ->
			attributes["$entry.key"] = entry.value;
		}
		String nodeType = builder.nodeType.selectedItem;
		if(nodeType == 'Dialog') {//创建面板
			Node creatingNode = new Node(null,nodeType,attributes);
			Panel newDlg = buildDialog(creatingNode);
			this.rootNode.append(creatingNode);
			this.dialogs << newDlg;
			this.nodes << creatingNode;
			//this.dialog = newDlg;
			//this.dialogNode = creatingNode;
			
			//refresh canvas & layerlist
			builder.dialoglist.add(createLayerItem(creatingNode))
			builder.canvas.add(newDlg,0)
			builder.canvas.requestFocus(true);		
			markComp(newDlg);
		}else {//创建控件
			Node creatingNode = new Node(null,nodeType,attributes);
			Component newComp = this.processNode(this.dialog, creatingNode);
			this.dialogNode.append(creatingNode);
			this.dialog.add(newComp);
			def layerObj = new LayerObject(creatingNode);
			builder.layerlist.model.addElement(layerObj);				
			//builder.layerlist.setSelectedValue(layerObj,true);
			markComp(newComp);
		}
		builder.msglabel.text = "创建结点$nodeType";
		
		this.createNodeDialog.dispose();
	}
	
	//改变创建的结点类型时触发
	private Map nodeAttrNames = [
         Button: ['id','name','text','x','y','width','height','actionId','was','tooltip','toggle'],
         Text: ['id','name','x','y','width','height','tooltip','text','font','size','color','align'],
         RichText: ['id','name','x','y','width','height','tooltip','text','font','size'],
         Sprite: ['id','name','x','y','width','height','tooltip','path'],
         Image: ['id','name','x','y','width','height','path'],
         Editor: ['id','name','x','y','width','height','tooltip','actionId','background'],
         Action: ['id','name','actionId','class'],
         Dialog: ['id','x','y','width','height','tooltip','background','initial','dispose','closable','movable']
         ];
	void changeNodeType(event) {
		String nodeType = builder.nodeType.selectedItem;
		if(nodeType != 'Dialog' && !this.dialogNode) {
			JOptionPane.showMessageDialog(this.frame,'请先创建Dialog结点或者选择一个已经存在的Dialog！','UI Maker',
					JOptionPane.CLOSED_OPTION);
			builder.nodeType.selectedItem = 'Dialog';
			nodeType ='Dialog';
		}
		List attrNames = nodeAttrNames[nodeType];
		Map values = [:];
		for(String key in attrNames) {
			values["$key"] = '';
		}
		builder.newNodeTable.model.rowsModel.value = values.entrySet() as List;
		builder.newNodeTable.validate();
		builder.newNodeTable.updateUI();
		builder.btnCreateNode.enabled = false;
		
	}

	private Node cutNode;
	private Node cloneNode;
	//剪切结点
	void cutNode(event) {
		def layerObj = builder.layerlist.selectedValue;
		def node = layerObj?layerObj.node: null;
		this.cutNode = node;
		pasteNodeAction.enabled = this.cloneNode;
		//TODO
	}
	//复制结点
	void copyNode(event) {
		def layerObj = builder.layerlist.selectedValue;
		def node = layerObj?layerObj.node: null;
		this.cloneNode = node;
		pasteNodeAction.enabled = this.cloneNode;
		
	}
	//粘贴结点
	void pasteNode(event) {
		if(!this.cloneNode || !this.dialogNode) return;
		if(this.cloneNode.name == 'Dialog') {//复制Dialog结点
			println "can't paste dialog node"
		}else {//复制普通结点		
			Node copied = new Node(null,cloneNode.name(),new LinkedHashMap(cloneNode.attributes()));
			Component newComp = this.processNode(this.dialog, copied);
			if(newComp) {
				this.dialogNode.append(copied);
				this.dialog.add(newComp);
				def layerObj = new LayerObject(copied);
				builder.layerlist.model.addElement(layerObj);
				builder.layerlist.setSelectedValue(layerObj,true);
			}
		}
	}
	
	
	//=============== Key Event  ============================//
    public void keyTyped(KeyEvent e) {}
	
    public void keyPressed(KeyEvent e) {
		//println "press key $e"
		def selectedComps = []
		builder.layerlist.selectedValues.each{
			def comp = this.node2comp[it.node]
			if(comp) {
				selectedComps << comp
			}
		}
		if(selectedComps.size == 0) {
			return;
		}
		
		//改变大小
		if(e.isShiftDown()) {
			for(Component comp in selectedComps) {
				Dimension d  = new Dimension();
		    	switch(e.keyCode) {
					case KeyEvent.VK_LEFT:
						d.setSize(e.isControlDown()?-10:-1, 0);
						e.consume();	
						break;
					case KeyEvent.VK_RIGHT:
						d.setSize(e.isControlDown()?10:1, 0);
						e.consume();	
						break;
					case KeyEvent.VK_UP:
						d.setSize(0,e.isControlDown()?-10:-1);
						e.consume();	
						break;
					case KeyEvent.VK_DOWN:
						d.setSize(0,e.isControlDown()?10:1);
						e.consume();	
						break;
				}
				resizeCompBy(comp,d);
			}
			return;
		}
		//移动控件
		for(Component comp in selectedComps) {
			Point p = comp.location
	    	switch(e.keyCode) {
				case KeyEvent.VK_LEFT:
					p.translate(e.isControlDown()?-10:-1, 0);
					e.consume();	
					break;
				case KeyEvent.VK_RIGHT:
					p.translate(e.isControlDown()?10:1, 0);
					e.consume();	
					break;
				case KeyEvent.VK_UP:
					p.translate(0,e.isControlDown()?-10:-1);
					e.consume();	
					break;
				case KeyEvent.VK_DOWN:
					p.translate(0,e.isControlDown()?10:1);
					e.consume();	
					break;
			}
			moveComponent(comp,p.x.intValue(),p.y.intValue());
		}
	}
	
    public void keyReleased(KeyEvent e) {}	
	
	/**
	 * 移动控件
	 * @param c
	 * @param x
	 * @param y
	 */
	private void moveComponent(Component c,int x,int y) {
		c.setLocation(x,y)
		def node = this.comp2node[c];
		node.@x = x;
		node.@y = y;
		//msg
		builder.msglabel.text = c.name?c.name:c.class		
		builder.poslabel.text = x + ' , '+y 
		builder.sizelabel.text = c.width + ','+c.height
		//println "${c.name?c.name:c.class}  move to ($x,$y)"
	}
	
	private JComponent lastSelectedComp = null;
	private Border lastCompBorder = null;
	private Border selectedCompBorder = BorderFactory.createLineBorder(Color.RED, 2);
	private MyResizable lastResizable = null;
	/**
	 * 设置控件为选中状态
	 * （边框、Resizable）
	 * @param c
	 */
	private void selectComp(JComponent c) {
		if(lastSelectedComp) {
			lastSelectedComp.setBorder(lastCompBorder);
		}
		if(lastResizable) {
			lastResizable.uninstallListeners();
			lastResizable = null;
		}
		lastSelectedComp = c;
		lastCompBorder = c?c.getBorder():null;
		if(c) {
			c.setBorder(selectedCompBorder);
			lastResizable = new MyResizable(c,this);
			builder.msglabel.text = c.name?c.name:c.class;		
			builder.poslabel.text = c.x + ' , '+c.y; 
			builder.sizelabel.text = c.width + ','+c.height;
		}else {
			builder.msglabel.text = '';		
			builder.poslabel.text = ''; 
			builder.sizelabel.text = '';
		}
	}
	
	//显示、标记控件
	private void markComp(JComponent comp) {
		def listmodel =builder.layerlist.model; 
		def dlg = this.findTopPanel(comp)
		if(dlg != this.dialog) {//如果点击的不是当前dialog，则刷新图层列表
			this.dialog = dlg;
			this.dialogNode = this.comp2node[this.dialog];
			builder.canvas.setComponentZOrder(dlg,0);
			listmodel.removeAllElements();
			listmodel.addElement(new LayerObject(dialogNode))
			for(def n in this.dialogNode.children()) {			
				listmodel.addElement(new LayerObject(n))
			}
		}
		//更新选中图层列表对应的item
		def node = this.comp2node[comp];
		def layeritem = listmodel.elements().find{it.node == node};
		//def index = listmodel.indexOf(layeritem);
		//builder.layerlist.selectedIndex = index;
		builder.layerlist.setSelectedValue(layeritem,true);
		if(builder.layerlist.selectedIndex == -1) {
			println "没有找到对应的图层item, comp=$comp"
		}
	}
	
	/**
	 * 改变控件大小
	 * @param c
	 * @param d
	 */
	void resizeComp(JComponent c, Dimension d) {
		c.setSize(d);
		Node node = this.comp2node[c];
		if(node) {
			node.@width = c.width;
			node.@height = c.height;
		}
	}
	
	void resizeCompBy(JComponent c, Dimension d) {
		Dimension oldSize = c.getSize();
		d.width += oldSize.width;
		d.height += oldSize.height;
		resizeComp(c,d);
	}
	
	
	//================ Layer List Selection Listener ===========================//
    void valueChanged(ListSelectionEvent e) {
    	def layerObj  = builder.layerlist.selectedValue;
    	copyNodeAction.enabled = layerObj?true:false;
    	editNodeAction.enabled = layerObj?true:false;
    	deleteNodeAction.enabled = layerObj?true:false;
    	pasteNodeAction.enabled = this.dialogNode&&this.cloneNode;
    	
    	if(layerObj) {
			Node node = layerObj.node;
			Component c = this.node2comp[node];
			selectComp(c);
    	}else {
    		selectComp(null);
    	}
    	//builder.canvas.requestFocus(true);
	}
    
    
	//================ Document Listener ===========================//
    public void insertUpdate(DocumentEvent e) {
    	this.setDirty(true)
    	this.frame.title = "UIMaker - ${selectedFile} *"
    };

    public void removeUpdate(DocumentEvent e) {
    	this.setDirty(true)
    	this.frame.title = "UIMaker - ${selectedFile} *"
    };

    public void changedUpdate(DocumentEvent e) {
    	this.setDirty(true)    	
    	this.frame.title = "UIMaker - ${selectedFile} *"
    };
    
    private void setDirty(boolean flag) {
    	this.fileDirty = flag;
    	this.frame.title = "UIMaker - ${selectedFile} ${flag?'*':''}"
    }
    //=========== Table Value Changed Listener ======================/
    public void tableChanged(TableModelEvent e) {
    	//println "tableChanged: $e"
    	if(e.getSource() == builder.editNodeTable.model) {
    		builder.btnModifyNode.enabled = true;
    	}else if(e.getSource() == builder.newNodeTable.model){
    		builder.btnCreateNode.enabled = true;
    	}
    }
    
    //=================== TabbedPane ChangeListener ===========================/
    void stateChanged(ChangeEvent e) {
    	if(e.getSource() == builder.tabbedPane ) {
    		int index = builder.tabbedPane.getSelectedIndex();
    		if(index ==0 ) {//source
    			recreateXML();
    		}else if(index == 1) {//preview
    			buildUI();
    		}
    	}
    }  
    
    //==============================================/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassUtil.init();
		//UIHelper.init();
		
		UIMaker maker = new UIMaker();
		maker.show();
		maker.newFile()
	}
	BufferedImage offscreenImage;

	Graphics2D offscreenGraphics;

}
