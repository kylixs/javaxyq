/*
 * JavaXYQ Source Code 
 * ItemDetailLabel ItemDetailLabel.groovy
 * by kylixs 2009-10
 * All Rights Reserved.
 * Please see also http://javaxyq.cn or http://javaxyq.googlecode.com.
 * Please email to  javaxyq@qq.com.
 */
package com.javaxyq.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.model.Item;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Animation;



/**
 * 物品详细提示组件
 * @author dewitt
 */
public class ItemDetailLabel extends PromptLabel {

	private static final long serialVersionUID = 789258410998495028L;
	private static final int IMG_WIDTH = 120;
	private static final int IMG_TOP = 10;
	private static final int TITLE_POS = 30;
	private static Font titleFont = new Font("宋体", Font.BOLD, 18); 
	
	private ItemInstance item;
	private Animation anim;
	public ItemDetailLabel(ItemInstance item) {
		super("");
		this.setItem(item);
		setSize(310,170);
	}
	public ItemDetailLabel() {
		super("");
		setSize(310,170);
	}
	
	public void setItem(ItemInstance item) {
		this.item = item;
		this.anim = SpriteFactory.loadAnimation(String.format("item/item120/%04d.tcp",item.getId()));
	}
	
	protected void paintComponent(java.awt.Graphics g) {
		if(this.item == null)return;
		int imgX = (IMG_WIDTH- this.anim.getWidth())/2;
		if(this.anim!=null) this.anim.draw(g, imgX+this.anim.getRefPixelX(), IMG_TOP+this.anim.getRefPixelY());
		
		g.translate(IMG_WIDTH, TITLE_POS);
		g.setColor(Color.YELLOW);
		g.setFont(titleFont);
		g.drawString(item.getName(), 0, 0);//title
		List<String> strs = new ArrayList<String>();
		//说明
		strs.add(item.getDescription());
		strs.add("【等级】"+item.getLevel());
		//功效
		Item _item = item.getItem();
		if (_item instanceof MedicineItem) {
			MedicineItem mitem = (MedicineItem) _item;
			String efficacy = mitem.getEfficacy();
			if(efficacy != null) {
				strs.add("【功效】"+efficacy);
				strs.add("#Y"+efficacy);
			}
			if(mitem.getLevel()==3) {
				strs.add("#Y"+mitem.actualEfficacy()); 
			}
		}
			
		drawStrings(g,strs);
		g.translate(-IMG_WIDTH, -TITLE_POS);
	}
	
	private void drawStrings(Graphics g,List<String> lines) {
		g.setFont(UIUtils.TEXT_FONT);
		FontMetrics fm = g.getFontMetrics();
		int fw = fm.charWidth("中".charAt(0));
		int fh = fm.getHeight();
		int textWidth = getWidth()-IMG_WIDTH;
		int lineLen = textWidth/fw;
		int dy = 0;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if(line.startsWith("#Y")) {
				g.setColor(Color.YELLOW);
				line = line.substring(2);
			}else {
				g.setColor(Color.WHITE);
			}
			int index = 0;
			int count = line.length();
			String str;
			while(index < count) {
				dy += fh;
				str = line.substring(index,index + Math.min(lineLen, count - index));
				index += str.length();
				g.drawString(str, 0, dy);
			}
		}
	}
}
