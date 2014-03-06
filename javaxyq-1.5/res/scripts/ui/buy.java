
/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.TextField;
import com.javaxyq.ui.UIHelper;
/**
 * 购买对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class buy extends PanelHandler implements MouseListener,MouseMotionListener,DocumentListener{
	private int amount = 0;
	private long price = 0;
	private long totalCost = 0;
	private Timer timer;
	private Label selectedBorder;
	private Label selectingBorder;
	private ItemDetailLabel detailLabel = new ItemDetailLabel();
	private TextField fieldAmount;
	private ItemInstance selectedItem; 
	
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		selectedBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselected.tcp"));
		selectingBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselecting.tcp"));
		//初始化物品
		String[] items = {"四叶花","七叶莲","天青地白","草果","九香虫","水黄莲","紫丹罗","佛手","旋复花","百色花",
		             "香叶","龙须草","灵脂","白玉骨头","鬼切草","曼佗罗花","山药","八角莲叶","人参","月见草"};
		int x0 = 8, y0 = 36;
		int rows = 4, cols = 5;
		for(int y =0;y<rows;y++) {
			for(int x=0;x<cols;x++) {
				ItemLabel label = new ItemLabel(dataManager.createItem(items[y*cols+x]));
				label.setName("item-"+items[y*cols+x]);
				label.setSize(50,50);
				label.setLocation(x0+x*51,y0+y*51);
				panel.add(label);
				UIHelper.removeAllMouseListeners(label);
				label.addMouseListener(this);
				label.addMouseMotionListener(this);
			}
		}
		fieldAmount = (TextField) panel.findCompByName("field_amount");
		fieldAmount.getDocument().addDocumentListener(this);
		setAutoUpdate(true);
	}

	public void update(PanelEvent evt) {
		this.totalCost = amount * price;
		Label lblPrice = (Label) panel.findCompByName("lbl_price");
		lblPrice.setText(String.valueOf(price));
		Label lblCost = (Label) panel.findCompByName("lbl_cost");
		lblCost.setText(String.valueOf(totalCost));
		Label lblCash = (Label) panel.findCompByName("lbl_cash");
		lblCash.setText(String.valueOf(context.getPlayer().getData().getMoney()));
		
	}
	public void confirm_buy(ActionEvent evt) {
		update(null);
		int money = context.getPlayer().getData().getMoney();
		if(money < totalCost) {
			doTalk(context.getTalker(),"总共需要#R"+totalCost+"#n两，你的现金不够呀？！");
		}else {
			money -= totalCost;
			context.getPlayer().getData().money = money;
			ItemInstance item = new ItemInstance(selectedItem.getItem(),amount);
			dataManager.addItemToPlayerBag(context.getPlayer(),item);
			doTalk(context.getTalker(),"你购买了"+amount+"个"+selectedItem.getName()+"，总共花费了#R"+totalCost+"#n两。#32");
			System.out.println( "buy "+selectedItem.getName()+"*"+amount+", cost "+totalCost);
		}	}
	private void setSelectedItem(ItemInstance item) {
		this.selectedItem = item;		
		ItemLabel label = (ItemLabel) panel.findCompByName("item-"+item.getName());
		selectedBorder.setLocation(label.getX()-1, label.getY()-1);
		panel.add(selectedBorder,0);
		this.price = item.getPrice();
		this.amount = 1;
		update(null);
		fieldAmount.setText(String.valueOf(amount));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		ItemLabel label = (ItemLabel) e.getSource();
		ItemInstance item = label.getItem();
		if(this.selectedItem == item) {
			int n = e.isShiftDown()?10:1;
			this.amount += e.getButton()==MouseEvent.BUTTON1? n:-n;
			if(this.amount > 99) {
				this.amount = 99;
			}
			if(this.amount < 1) {
				this.amount = 1;
			}
			update(null);
			fieldAmount.setText(String.valueOf(amount));
		}else {
			setSelectedItem(item);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}


	@Override
	public void mouseExited(MouseEvent e) {
		panel.remove(selectingBorder);
		helper.hideToolTip(detailLabel);		
	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		ItemLabel label = (ItemLabel) e.getSource();
		selectingBorder.setLocation(label.getX()-1, label.getY()-1);
		panel.add(selectingBorder,0);
		detailLabel.setItem(label.getItem());
		helper.showToolTip(detailLabel, label, e);
				
	}
	
	private void syncAmount() {
		try {
			this.amount = Integer.parseInt(fieldAmount.getText());
		}catch(Exception ex) {
			//fieldAmount.setText(this.amount);
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		syncAmount();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		syncAmount();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		syncAmount();
		
	}

	
}
