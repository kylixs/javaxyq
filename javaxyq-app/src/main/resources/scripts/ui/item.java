/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.ItemTypes;
import com.javaxyq.ui.ItemDetailLabel;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.ItemLabel.CellType;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 道具行囊对话框脚本
 * @author dewitt
 * @date 2009-11-27 create
 */
public class item extends PanelHandler implements MouseListener,MouseMotionListener {
	Logger log = LoggerFactory.getLogger(this.getClass());
	int x0=28,y0 = 198;
	int rows = 4,cols = 5;
	int cellWidth = 51,cellHeight = 51;
	
	int x1 =197, y1 =26;
	int erows = 3,ecols = 2;
	int ecellWidth = 54,ecellHeight = 54;
	
	private ItemDetailLabel detailLabel;
	private ItemLabel[] itemlabels ;
	private int selectedIndex = -1;
	private Label selectedBorder = null;
	private Label selectingBorder = null;
	private ItemLabel selItemLabel = null;
	private Expression expression;
	
	public item() {
		this.detailLabel = new ItemDetailLabel();
		itemlabels = new ItemLabel[rows*cols+erows*ecols];
		selectedBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselected.tcp"));
		selectingBorder = new Label(SpriteFactory.loadAnimation("wzife/button/itemselecting.tcp"));
	}
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		UIHelper.removeAllMouseListeners(panel);
		this.panel.addMouseListener(this);
		this.panel.addMouseMotionListener(this);
		
		Player player = context.getPlayer();
		Label face  = (Label) this.panel.findCompByName("face");
		face.setAnim(SpriteFactory.loadAnimation("wzife/photo/facebig/"+player.getCharacter()+".tcp"));
		
		this.updateItems();
		this.updateLabels(this.panel);
		this.setAutoUpdate(true);
	}
	
	public void dispose(PanelEvent evt) {
		super.dispose(evt);
		this.panel.removeMouseListener(this);
		this.panel.removeMouseMotionListener(this);
	}

	public void update(PanelEvent evt) {
		this.updateLabels(this.panel);
	}
	
	/**
	 * 装上装备栏
	 */
	private void takeupEquipment(ItemInstance item){
		//----|-----
		// 0  | 1
		//----|-----
		// 2  | 3
		//----|-----
		// 4  | 5
		Player player = context.getPlayer();
		int targetIndex = 0;
		if(ItemTypes.isWeapon(item.getItem())){
			targetIndex = 2;
			player.takeupWeapon((WeaponItem) item.getItem());
			log.info("takeup weapon: "+item.getItem());
//		}else if(ItemTypes.isType(item.getItem(), ItemTypes.TYPE_EQUIPMENT)){
//			targetIndex = 3;
			//takeup equipment
		}
		
		ItemInstance oldItem = dataManager.getItemAt(player, targetIndex);
		if(oldItem != item) {
			dataManager.removeItemFromPlayer(player, item);
			dataManager.setItem(player, targetIndex, item);
			if(oldItem != null) {
				dataManager.addItemToPlayerBag(player, oldItem);
			}
		}
		
		
	}
	
	/**
	 * 从装备栏卸下
	 * @param item
	 */
	private void takeoffEquipment(ItemInstance item){
		Player player = context.getPlayer();
		if(ItemTypes.isWeapon(item.getItem())){
			player.takeoffWeapon();
//		}else if(ItemTypes.isType(item.getItem(), ItemTypes.TYPE_EQUIPMENT)){
			//player.takeoffEquipment();
		}

		dataManager.removeItemFromPlayer(player, item);
		dataManager.addItemToPlayerBag(player, item);
	}
	
	 /* 销毁物品
	 * @param evt
	 */
	public void destory_item(ActionEvent evt){
		if(selItemLabel==null) {//未选择物品
			context.getWindow().getHelper().prompt("请先选择要销毁的物品，然后再点击【销毁】按钮。", 3000);
			return;
		}
		dataManager.removeItemFromPlayer(context.getPlayer(), selectedIndex);
		stopMoving();
		updateItems();

	}
	
	/**
	 * 更新道具栏
	 */
	synchronized private void updateItems() {
		ItemInstance[] items = dataManager.getItems(context.getPlayer());
		
		//----|-----
		// 0  | 1
		//----|-----
		// 2  | 3
		//----|-----
		// 4  | 5
		//装备栏
		for(int r=0;r<erows;r++) {
			for(int c=0;c<ecols;c++) {
				//create label
				int index = r*ecols + c;
				ItemInstance item = items[index];
				ItemLabel label = itemlabels[index];
				if(item!=null) {//数据列表有item
					if(label!=null) {//格子有物品
						if(label.getItem() != item) {//如果不是同一个物品
							label.setItem(item);
						}
					}else {//格子空着
						try {
							label = new ItemLabel(item);
							label.setLocation(x1 + c*ecellWidth , y1+r*ecellHeight+1);
							label.addMouseListener(this);
							label.addMouseMotionListener(this);
							panel.add(label,0);
							itemlabels[index] = label;
						}catch(Exception e1) {
							log.info("添加item失败！"+item);
							e1.printStackTrace();
						}
					}
					label.setCellType(CellType.EQUIP);
				}else {//清除格子
					if(label!=null) {
						panel.remove(label);
						label.removeMouseListener(this);
						label.removeMouseMotionListener(this);
						itemlabels[index] = null;
					}
				}
			}
		}
		
		//背包栏
		for(int r=0;r<rows;r++) {
			for(int c=0;c<cols;c++) {
				//create label
				int index = r*cols + c + (erows*ecols);
				ItemInstance item = items[index];
				ItemLabel label = itemlabels[index];
				if(item!=null) {//数据列表有item
					if(label!=null) {//格子有物品
						if(label.getItem() != item) {//如果不是同一个物品
							label.setItem(item);
						}
					}else {//格子空着
						try {
							label = new ItemLabel(item);
							label.setLocation(x0 + c*cellWidth , y0+r*cellHeight+1);
							UIHelper.removeAllMouseListeners(label);
							label.addMouseListener(this);
							label.addMouseMotionListener(this);
							panel.add(label);
							itemlabels[index] = label;
						}catch(Exception e) {
							log.info("添加item失败！"+item);
							e.printStackTrace();
						}
					}
					label.setCellType(CellType.BAG);
				}else {//清除格子
					if(label!=null) {
						panel.remove(label);
						label.removeMouseListener(this);
						label.removeMouseMotionListener(this);
						itemlabels[index] = null;
					}
				}
			}
		}
	}
	
	private void updateLabels(Panel panel) {
		Component[] comps = panel.getComponents();
		List<Label>labels = new ArrayList<Label>();
		for (Component c : comps) {
			if (c instanceof Label) {
				labels.add((Label) c);
			}
		}
		if(expression == null) {
			try {
				List<String> vars = new ArrayList<String>(); 
				for(Label label : labels) {
					String name = label.getName();
					if(StringUtils.isNotBlank(name)) {
						vars.add(name+"#="+ label.getTextTpl());
					}
				}
				String tpl = StringUtils.join(vars,"#;");
				JexlEngine jexl = new JexlEngine();
				UnifiedJEXL ujexl = new UnifiedJEXL(jexl);
				expression = ujexl.parse(tpl);
			} catch (Exception e) {
				log.info("创建JEXL表达式失败");
				e.printStackTrace();
			}
		}
		if(expression != null) {
			Map<String, Object> properties = dataManager.getProperties(context.getPlayer());
	        JexlContext jexlcontext = new MapContext(properties);
	        String result = expression.evaluate(jexlcontext).toString();
			String[] items = result.split("#;");
			for (String item : items) {
				String[] values = item.split("#=");
				Label label = (Label) panel.findCompByName(values[0]);
				label.setText(values[1]);
			}
		}
	}		
	
    public void mouseClicked(MouseEvent e) {
    	e.consume();
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				//左键点击移动物品
				moveItems(e);
				break;
			case MouseEvent.BUTTON3:
				//右键点击使用物品
				useItem(e);
				break;
		}

    }
	
	/**
	 * 移动物品
	 * @param e
	 */
	synchronized private void moveItems(MouseEvent e){
    	Object src = e.getComponent();
    	Point cell = getCell(e);
    	if(cell!=null) {
    		int newIndex = cell.x + cols*cell.y + 6;
    		if(selItemLabel!=null) {//已经选择了物品
				if(selectedIndex == newIndex) {
					stopMoving();
					return;
				}
				Player player = context.getPlayer();
				//如果不能叠加物品则进行移动
				if(!overlayItems(selectedIndex, newIndex)) {
					//交换模型中的数据
					dataManager.swapItem(player, selectedIndex, newIndex);
				}
    			//完成移动
    			updateItems();
				stopMoving();
    		}else if(src instanceof ItemLabel){//选择物品
    			ItemLabel label = (ItemLabel) src;
    			selectedBorder.setLocation(x0+cell.x*cellWidth, y0+cell.y*cellHeight);
    			panel.add(selectedBorder,0);
    			label.setVisible(false);
    			helper.setMovingObject(label.getAnim(), new Point(-e.getX(),-e.getY()));
    			selItemLabel = label;
    			selectedIndex = newIndex;
    		}
    		
    	}else {//没有点击在单元格上，撤销移动物品
    		stopMoving();
    	}
    			
	}
	
    /**
     * 停止移动物品（移动完成或者取消移动）
     */
    private void stopMoving() {
		if(selItemLabel!=null) {
			selItemLabel.setVisible(true);
		}
		selectedIndex = -1;
		selItemLabel = null;
		helper.removeMovingObject();
		panel.remove(selectedBorder);
    	
    }
	
	/**
	 * 判断两个物品是否可以叠加，如果可以则进行叠加
	 * @param srcIndex
	 * @param destIndex
	 * @return
	 */
	private boolean overlayItems(int srcIndex,int destIndex) {
		ItemInstance[] items = dataManager.getItems(context.getPlayer());
		if(items[srcIndex]!=null && items[destIndex]!=null) {
			//如果叠加成功
			if(dataManager.overlayItems(items[srcIndex],items[destIndex])) {
				if(items[srcIndex].getAmount()==0) {//如果物品数量为0，则销毁之
					items[srcIndex] = null;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 使用物品
	 * @param e
	 * @return
	 */
	public boolean useItem(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof ItemLabel) {
			ItemLabel label = (ItemLabel) c;
			ItemInstance item = label.getItem();
			
			//点击在装备栏上
			if(CellType.EQUIP.equals(label.getCellType())) {
				takeoffEquipment(label.getItem());
			}else {
				//点击在背包栏，如果是武器类则装备上，否则使用物品
				if(ItemTypes.isWeapon(item.getItem())){
					takeupEquipment(label.getItem());
				}else {
					application.getItemManager().useItem(context.getPlayer(),label.getItem());
				}
			}
			
			updateItems();
			return true;
		}
		return false;
	}

    public void mouseMoved(MouseEvent e){
    	Object src = e.getComponent();
    	Point bagCell = getCell(e);
    	Point equipCell = getEquipCell1(e);
    	if(bagCell!=null) {
	    	selectingBorder.setLocation(x0+bagCell.x*cellWidth-1, y0+bagCell.y*cellHeight-1);
	    	panel.add(selectingBorder,0);
    	}else if(equipCell!=null){
		    selectingBorder.setLocation(x1+equipCell.x*ecellWidth-1, y1+equipCell.y*ecellHeight-1);
		    panel.add(selectingBorder,0);
    	}else {
    		panel.remove(selectingBorder);
    	}
    	
    	
    	if (src instanceof ItemLabel) {
    		ItemLabel label = (ItemLabel) src;
    		if(label.isVisible()) {
    		this.detailLabel.setItem(label.getItem());
    		helper.showToolTip(this.detailLabel,label,e);
    		}
    	}else {
    		//panel.remove(this.detailLabel);
    		helper.hideToolTip(this.detailLabel);
		}
    	
    }
    
    private Point getCell(MouseEvent e) {
		JComponent src = (JComponent) e.getComponent();
    	Point p = e.getPoint();
    	if(src != panel) {
    		p = SwingUtilities.convertPoint(src, p, panel);
    	}
    	if(p.x>x0 && p.x <x0+cellWidth*cols && p.y>y0 && p.y<y0+cellHeight*rows) {
	    	int r = (p.y-y0)/cellHeight;
	    	int c = (p.x-x0)/cellWidth;
	    	return new Point(c,r);
    	}    	
    	return null;
    }
    
    private Point getEquipCell1(MouseEvent e){
		JComponent src = (JComponent) e.getComponent();
		Point p = e.getPoint();
		if(src != panel) {
			p = SwingUtilities.convertPoint(src, p, panel);
		}
		if(p.x>x1 && p.x <x1+ecellWidth*ecols && p.y>y1 && p.y<y1+ecellHeight*erows) {
			int r = (p.y-y1)/ecellHeight;
			int c = (p.x-x1)/ecellWidth;
			return new Point(c,r);
		}
		return null;
	}
    
    public void mousePressed(MouseEvent e) {e.consume();}
    
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseEntered(MouseEvent e) {}
    
    public void mouseExited(MouseEvent e) {}
    
    public void mouseDragged(MouseEvent e){}
	
}
