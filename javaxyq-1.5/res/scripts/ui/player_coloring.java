/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import javax.swing.ButtonGroup;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.Button; 
import com.javaxyq.ui.Label; 
import com.javaxyq.ui.Panel; 
import com.javaxyq.ui.ToggleButton; 
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

/**
 * @author dewitt
 * @date 2009-11-27 create
 */
public class player_coloring extends PanelHandler {
	
	private Label lblCharacter;
	
	private Label lblWeapon;
	
	private Label lblShadow;
	
	private int selectionPart;
	
	private Panel colorDialog;
	
	private Sprite character;
	
	private ToggleButton btn1;
	
	private ToggleButton btn2;
	
	private ToggleButton btn3;
	
	private Label textCost;
	
	private int[] costs = new int[3];
	
	private Label textMaterial;
	
	private String material;
	
	private int[] originColorations;
	
	private int animIndex;
	
		
	public void initial(PanelEvent evt) {
		super.initial(evt);
		String cmd = evt.getCommand();
		Player player = context.getPlayer();
		Sprite weapon = player.getWeapon();
		//避免重复初始化
		colorDialog = (Panel) evt.getSource();
		textMaterial = (Label) colorDialog.findCompByName("textMaterial");
		textCost = (Label) colorDialog.findCompByName("textCost");
		btn1 = (ToggleButton) colorDialog.findCompByName("btn1");
		btn2 = (ToggleButton) colorDialog.findCompByName("btn2");
		btn3 = (ToggleButton) colorDialog.findCompByName("btn3");
		ButtonGroup bg = new ButtonGroup();
		bg.add(btn1);
		bg.add(btn2);
		bg.add(btn3);
		
		animIndex = 0;
		costs[0] = costs[1] = costs[2] = 0;
		material = "彩果";
		btn1.setSelected(true);
		this.selectionPart = 0;
		character = new Sprite(player.getPerson());
		int[] colorations = character.getColorations();
		originColorations = colorations;
		Animation currCharacter = character.getAnimation(animIndex);
		Animation currWeapon = weapon==null?null: weapon.getAnimation(animIndex);
		Animation shadow = player.getShadow().getAnimation(0);
		preview(currCharacter, currWeapon, shadow);
	}

	public void dispose(PanelEvent evt) {
		super.dispose(evt);
	}
	
	/**
	 * 切换到头部
	 * @param evt
	 */
	public void head(ActionEvent evt) {
		material = "彩果";
  		this.selectionPart = 0;
		textMaterial.setText("所需" + material);
  		textCost.setText("" + costs[selectionPart]);
	}
	/**
	 * 切换到头巾
	 * @param evt
	 */
	public void hood(ActionEvent evt) {
		material = "花豆";
  		this.selectionPart = 1;
		textMaterial.setText("所需" + material);
  		textCost.setText("" + costs[selectionPart]);
	}
	/**
	 * 切换到衣服
	 * @param evt
	 */
	public void clothes(ActionEvent evt) {
		material = "彩果";
  		this.selectionPart = 2;
		textMaterial.setText("所需" + material);
  		textCost.setText("" + costs[selectionPart]);
						
	}
	/**
	 * 改变颜色
	 * @param evt
	 */
	public void coloring(ActionEvent evt) {
		Player player = context.getPlayer();
		Sprite weapon = player.getWeapon();
		int count = player.getColorationCount(this.selectionPart);
		int coloration = character.getColoration(selectionPart);
		coloration = (coloration + 1) % count;
		character.setColoration(selectionPart, coloration);
		//FIXME 染色
  		//ColorationUtil.recreate(character, "shape/"+player.getCharacter()+"/00.pp");
		int[] colorations = character.getColorations();
		character = SpriteFactory.loadSprite("/shape/char/"+player.getCharacter()+"/stand.tcp",colorations);
		
		Animation currCharacter = character.getAnimation(animIndex);
		Animation currWeapon = weapon==null?null:weapon.getAnimation(animIndex);
		Animation shadow = player.getShadow().getAnimation(0);
		preview(currCharacter, currWeapon, shadow);
		
		costs[selectionPart] = (1 + (coloration + 1)/3) * 10;
		textCost.setText("" + costs[selectionPart]);				
	}
	/**
	 * 人物转向
	 * @param evt
	 */
	public void turn(ActionEvent evt) {
		Player player = context.getPlayer();
		Sprite weapon = player.getWeapon();
		int count = character.getAnimationCount();
		if (animIndex == count - 1) {
			animIndex = 0;
		} else if (animIndex < 4) {
			animIndex += 4;
		} else {
			animIndex += -4 + 1;
		}
		animIndex %= count;
		Animation currCharacter = character.getAnimation(animIndex);
		Animation currWeapon = weapon==null?null:weapon.getAnimation(animIndex);
		Animation shadow = player.getShadow().getAnimation(0);
		preview(currCharacter, currWeapon, shadow);
				
		
	}
	/**
	 * 确认染色
	 * @param evt
	 */
	public void confirm(ActionEvent evt) {
		Player player = context.getPlayer();
		Button btnSave = (Button) evt.getSource();
		helper.hideDialog((Panel) btnSave.getParent());
		int[] colorations = character.getColorations();
		int cost = costs[0]*50000 + costs[1]*10000 + costs[2]*100000;
		if(player.getData().money < cost) {
			doTalk(null,"这个染色方案需要"+cost+"两，你身上银两不够呀？！#83");
		}else {
			player.getData().money -= cost;
			doTalk(null,"共花费了"+cost+"两，欢迎下次光临！#32");
			player.setColorations(colorations,true);
			System.out.println("coloring: {" + colorations[0] + "," + colorations[1]+ "," + colorations[2] + "}");
		}		
	}

    private void preview(Animation currCharacter, Animation currWeapon, Animation shadow) {
        if (lblCharacter != null) {
            colorDialog.remove(lblCharacter);
        }
        if (lblWeapon != null) {
            colorDialog.remove(lblWeapon);
        }
        if (lblShadow != null) {
            colorDialog.remove(lblShadow);
        }
        lblCharacter = new Label(currCharacter);
        lblShadow = new Label(shadow);
        if(currWeapon!=null) {       	
        	lblWeapon = new Label(currWeapon);
        	lblWeapon.setLocation(lblCharacter.getX() + currCharacter.getRefPixelX() - currWeapon.getRefPixelX(),
        			lblCharacter.getY() + currCharacter.getRefPixelY() - currWeapon.getRefPixelY());
        	colorDialog.add(lblWeapon);
        }
        lblCharacter.setLocation((colorDialog.getWidth() - lblCharacter.getWidth())/2, 80);
        lblShadow.setLocation(lblCharacter.getX() + currCharacter.getRefPixelX() - shadow.getRefPixelX(),
            lblCharacter.getY() + currCharacter.getRefPixelY() - shadow.getRefPixelY());
        colorDialog.add(lblCharacter);
        colorDialog.add(lblShadow);
    }

	
}
