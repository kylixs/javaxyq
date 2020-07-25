/*
 * JavaXYQ Source Code 
 * ItemLabel ItemLabel.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Font;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.widget.Animation;


/**
 * 用于显示物品的label
 * @author dewitt
 */
public class ItemLabel extends Label {
	
	public enum CellType {
		/**背包栏 */
		BAG, 
		/**装备栏 */
		EQUIP
	};
	private static final long serialVersionUID = 3915112361143814573L;
	private Font foregroundFont= new Font("宋体", Font.PLAIN, 14);
	private ItemInstance item;
	private CellType cellType = CellType.BAG;
	
	public ItemLabel() {
		this(null);
	}
	
	public ItemLabel(ItemInstance item) {
		super("");
		this.setItem(item);
	}
	
	public void setItem(ItemInstance item) {
		this.item = item;
		if(item!=null) {
			Animation anim = SpriteFactory.loadAnimation(String.format("item/item50/%04d.tcp",item.getItemId())); 
			setAnim(anim);
		}else {
			setAnim(null);
		}
		setSize(51,51);
	}
	
	public ItemInstance getItem() {
		return item;
	}
	
	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);
		if(item!=null && item.getCount() >1) {
			g.setColor(Color.BLACK);
			g.setFont(foregroundFont);
			String str = String.valueOf(item.getCount());
			g.drawString(str, 5-1, 15);
			g.drawString(str, 5+1, 15);
			g.drawString(str, 5, 15-1);
			g.drawString(str, 5, 15+1);
			g.setColor(Color.WHITE);
			g.setFont(foregroundFont);
			g.drawString(str, 5, 15);
		}
	}
}
