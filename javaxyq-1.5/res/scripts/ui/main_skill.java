package ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.AbstractButton;
import javax.swing.JLabel;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import java.util.*;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.config.ImageConfig;
import com.javaxyq.ui.ItemLabel;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;
import com.javaxyq.model.Skill;
import com.javaxyq.core.*;
import com.javaxyq.data.BaseItemDAO;
import com.javaxyq.data.SkillMain;
/**
 * @author wpaul
 * @date 2014-5-15 create
 */
public class main_skill extends PanelHandler implements MouseListener,MouseMotionListener{
	private List<SkillMain> skills;
	private List<Skill> magicskills;
	private List<Label>labels;
	private List<AbstractButton>buttons;
	private int textrow;
	private StringBuffer sb;
	private Expression expression;
	//private int index;

	public void initial(PanelEvent evt) {
		super.initial(evt);
		//this.updateLabels(panel);
		labels = new ArrayList<Label>();
		buttons = new ArrayList<AbstractButton>();
		Player player = context.getPlayer();
		String school = player.getData().school;
		skills = dataManager.findMainSkill(school);
		magicskills = new ArrayList<Skill>();
		sb = new StringBuffer();
		textrow = 0;
		for(int s=0; s<skills.size(); s++){
			SkillMain skill = skills.get(s);
			Label mainskill  = (Label) this.panel.findCompByName("技能"+s);
			Label mskilllevel  = (Label) this.panel.findCompByName("skill"+s);
			mainskill.setAnim(SpriteFactory.loadAnimation("wzife/skillmain/normal/"+
			skill.getId()+".tcp"));
			mainskill.addMouseListener(this);
			mainskill.addMouseMotionListener(this);

			labels.add(mainskill);
			labels.add(mskilllevel);
		}
		this.updateLabels(this.panel);
		AbstractButton button_down = (AbstractButton)this.panel.findCompByName("向下按钮");
		AbstractButton button_up = (AbstractButton)this.panel.findCompByName("向上按钮");
		buttons.add(button_up);
		buttons.add(button_down);
	}
	public void school_skill(ActionEvent evt) {		
		Player player = context.getPlayer();
		String school = player.getData().school;
		skills = dataManager.findMainSkill(school);
		/*for(int s=0; s<skills.size(); s++){
			SkillMain skill = skills.get(s);
			Label mainskill  = (Label) this.panel.findCompByName("技能"+s);
			mainskill.setAnim(SpriteFactory.loadAnimation("wzife/skillmain/normal/"+
			skill.getId()+".tcp"));
			mainskill.addMouseListener(this);
			mainskill.addMouseMotionListener(this);
			labels.add(mainskill);
		}*/
		for(Label label:labels){
            System.out.println("label is:"+label);
			panel.add(label);
		}
		for(AbstractButton button:buttons){
			panel.add(button);
		}
		String background = "/wzife/dialog/mskill.tcp/";
		panel.setBgImage(new ImageConfig(background));
		//updateSkills();
		//println "background is : $background"
		//this.panel.setBgImage(new ImageConfig(background));
	}
	public void plot_skill(ActionEvent evt) {
		for(Label label:labels){
			//label.setAnim(SpriteFactory.loadAnimation(""));
			panel.remove(label);
			//label.removeMouseListener(this);
			//label.removeMouseMotionListener(this);
		}
		String background = "/wzife/dialog/plotskill.tcp/";
		panel.setBgImage(new ImageConfig(background));
		//println "background is : $background"
		//this.panel.setBgImage(new ImageConfig(background));
	}
	public void assist_skill(ActionEvent evt) {
		for(Label label:labels){
			//label.setAnim(SpriteFactory.loadAnimation(""));
			panel.remove(label);
			//label.removeMouseListener(this);
			//label.removeMouseMotionListener(this);
		}
		String background = "/wzife/dialog/assistskill.tcp/";
		panel.setBgImage(new ImageConfig(background));
		//Component[] comps = panel.getComponents();
		
		
		//println "background is : $background"
		//this.panel.setBgImage(new ImageConfig(background));
	}
	public void practice_skill(ActionEvent evt) {
		for(Label label:labels){
			//label.setAnim(SpriteFactory.loadAnimation(""));
			panel.remove(label);
			//label.setText("");
			//label.removeMouseListener(this);
			//label.removeMouseMotionListener(this);
		}
		for(AbstractButton button:buttons){
			panel.remove(button);
		}
		String background = "/wzife/dialog/practiceskill.tcp/";
		panel.setBgImage(new ImageConfig(background));
		//println "background is : $background"
		//this.panel.setBgImage(new ImageConfig(background));
	}
	public void qjbm(ActionEvent evt) {
		/*String fname = "wzife.wd2"
		String background = "界面 "+evt.getActionCommand();*/
	}
	public void text_down(ActionEvent evt){
		if(!sb.toString().equals("")){
			Label skilldes  = (Label) this.panel.findCompByName("技能说明");
			textrow++;
			String tt = sb.toString();
			for(int i=0; i<textrow; i++){
				int index = tt.indexOf("<br>")+4;
	        	tt = tt.substring(index);
			}
	    	String sdes = "<html>" + tt + "</html>";
			skilldes.setText(sdes);
		}	
	}
	public void text_up(ActionEvent event){
		Label skilldes  = (Label) this.panel.findCompByName("技能说明");
		if(textrow>0){
			textrow--;
		}
		String tt = sb.toString();
		for(int i=textrow; i>0; i--){
			int index = tt.indexOf("<br>")+4;
        	tt = tt.substring(index);
		}
    	String sdes = "<html>" + tt + "</html>";
		skilldes.setText(sdes);
	}
	
	
	
	/*private void updateLabels(Panel panel) {
		Component[] comps = panel.getComponents();
		labels = new ArrayList<Label>();
		for (Component c : comps) {
			if (c instanceof Label) {
				System.out.println("lable"+c);
				labels.add((Label) c);
			}
		}
		for(Label label : labels) {
			label.addMouseListener(this);
			label.addMouseMotionListener(this);
		}
		
	}*/
	
	private void processSkill(MouseEvent e){
		Object c = e.getComponent();
		if(c instanceof Label){
			Label label = (Label)c;
			String name = label.getName();
		
			if(name.contains("技能")){
				int index = Integer.parseInt(name.substring(2));
				String[] magics = skills.get(index).getMagicSkill().split("、");
				magicskills.removeAll(magicskills);
                int p = 0;
				for(String magic:magics){
					if(StringUtils.equals(magic, "0")){
						break;
					}
					Skill skill = dataManager.findSkillByName(magic);
					magicskills.add(skill);	
					Label magicskill  = (Label) this.panel.findCompByName("法术"+(p++));
					magicskill.setAnim(SpriteFactory.loadAnimation("wzife/skillmagic/normal/"+
					skill.getId()+".tcp"));	
					magicskill.addMouseListener(this);
					magicskill.addMouseMotionListener(this);
					labels.add(magicskill);
				}	
				for(int i=p;i<13;i++){
					Label magicskill  = (Label) this.panel.findCompByName("法术"+i);
					magicskill.setAnim(SpriteFactory.loadAnimation(""));
				}
				processText(skills.get(index));
			}
			else if(name.contains("法术")){
				int index = Integer.parseInt(name.substring(2));
				processText(magicskills.get(index));
			}
		}
	
	}
	
	private void processText(Skill skill) {
			//技能名称
			String sname = skill.getName();
			Label skillname  = (Label) this.panel.findCompByName("技能名称");
            skillname.setText(sname);
			//技能说明
            textrow = 0;
            sb.setLength(0);
			Label skilldes  = (Label) this.panel.findCompByName("技能说明");
			skilldes.setVerticalAlignment(JLabel.NORTH);
			labels.add(skilldes);
			labels.add(skillname);
			//技能描述
			String des = skill.getDescription();
			sb.append(linefeed(skilldes,des));			
			//效果
			String effect = skill.getEffection();
			if(!effect.equals("0")){
				sb.append(linefeed(skilldes,effect));
			}
			//使用条件
			if(skill.getConditions() != null){
				String conditions = skill.getConditions();
                sb.append(linefeed(skilldes,conditions));
			}			
			//使用消耗
			if(skill.getConsumption() != null){
				String consumption = skill.getConsumption();
                sb.append(linefeed(skilldes,consumption));
			}			
	    	String sdes = "<html>" + sb.toString() + "</html>";
	    	skilldes.setText(sdes);	
	}
	
	/**
	 * 
	 * @param skilldes
	 * @desc 自动换行
	 * @return 
	 */
	private String linefeed(Label skilldes,String des){
		StringBuffer sb = new StringBuffer();
		char[] deschar = des.toCharArray();
    	FontMetrics fm = skilldes.getFontMetrics(skilldes.getFont());
    	int linelen = 0;
    	int offset = 0;
    	for (int i=0;i<deschar.length;i++){
    		if(linelen <= skilldes.getWidth()-fm.charWidth(deschar[0])){
        		linelen += fm.charWidth(deschar[i]);	
    		}else{
    			sb.append(deschar, offset, i-offset);
        		sb.append("<br>");
        		linelen = fm.charWidth(deschar[i]);
    			offset = i;
    		}
    	}
    	sb.append(deschar, offset, deschar.length-offset);
		sb.append("<br>");
    	
    	/*int fw = fm.charWidth(deschar[0]);
    	int fh = fm.getHeight();
    	  	
    	int linelen = deschar.length*fw/skilldes.getWidth()+1;
    	int offset = skilldes.getWidth()/fw;
    	for (int i=0;i<linelen;i++){
    		if(i<linelen-1){
    			sb.append(deschar, i*offset, offset);
        		sb.append("<br>");
    		}else{
    			sb.append(deschar,i*offset,deschar.length-i*offset);
    			sb.append("<br>");
    		}  		
    	}*/
    	return sb.toString();
	}
	
	public void mouseClicked(MouseEvent e) {
		e.consume();
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				//左键点击技能图标
			    processSkill(e);
				break;
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
					String text = label.getTextTpl();
					if(StringUtils.isNotBlank(name) && text!=null) {
						System.out.println("getTextTpl is:"+text);
						vars.add(name+"#="+ label.getTextTpl());
					}
				}
				String tpl = StringUtils.join(vars,"#;");
				JexlEngine jexl = new JexlEngine();
				UnifiedJEXL ujexl = new UnifiedJEXL(jexl);
				expression = ujexl.parse(tpl);
			} catch (Exception e) {
				System.out.println("创建JEXL表达式失败");
				e.printStackTrace();
			}
		}
		if(expression != null) {
			Map<String, Object> properties = dataManager.getProperties(context.getPlayer());
	        System.out.println("proerties is:"+properties);
			JexlContext jexlcontext = new MapContext(properties);
	        String result = expression.evaluate(jexlcontext).toString();
			System.out.println("result is:"+result);
	        String[] items = result.split("#;");
			for (String item : items) {
				String[] values = item.split("#=");
				if(values.length>1){
					System.out.println("item is:"+item);
					Label label = (Label) panel.findCompByName(values[0]);
					label.setText(values[1]);
				}

			}
		}

	}
	
	public void mouseMoved(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {e.consume();}
	
	public void mouseReleased(MouseEvent e) {}
	
	public void mouseEntered(MouseEvent e) {}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mouseDragged(MouseEvent e){}


}