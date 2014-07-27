package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.SkillMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Skill;
import com.javaxyq.ui.Button;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.MP3Player;
import com.javaxyq.util.StringUtils;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Player;

/**
 * 学习师门技能脚本
 * @author 
 * @date 2014-6-4 create
 */
public class learn_skill extends PanelHandler implements MouseListener,MouseMotionListener {
	private Expression expression;
	private List<SkillMain> skills;
	private List<Skill> magicskills;
	private List<Label>labels;
	private StringBuffer skilldesc;
	Map<String, Object> properties;
	String[] attrs = {"伤害","灵力","防御","灵力","躲避","速度","命中","HP","MP"};
	private Label default_skilllabel;
	private Label default_magiclabel;
	private Label basic_skilllabel;
	
	private static String inArray(String[] array, String value) {
		for (int i = 0; i < array.length; i++) {
			if(value.contains(array[i]))return array[i];
		}
		return null;
	}

	//private static int skillLevel;
	private int textrow;
	private int mlistrow;

	public void initial(PanelEvent evt) {
		super.initial(evt);
		labels = new ArrayList<Label>();
		skilldesc = new StringBuffer();
		magicskills = new ArrayList<Skill>();
    	//PlayerVO vo = context.getPlayer().getData();
		this.properties = new HashMap<String, Object>();
		String school = context.getPlayer().getData().school;
		skills = dataManager.findMainSkill(school);
		textrow = 0;
		mlistrow = 0;
		//基础技能设为默认项	
		String basic_skill = dataManager.getBasicSkillName(school);
		for(int s=0; s<skills.size(); s++){
			SkillMain skill = skills.get(s);
			Label mskillname = (Label) this.panel.findCompByName("skillLevel"+s);
			mskillname.addMouseListener(this);
			mskillname.addMouseMotionListener(this);
			labels.add(mskillname);
			if(StringUtils.equals(basic_skill, skill.getName())){
				//门派基础技能设为红色字体
				this.setBasic_skilllabel(mskillname);
				mskillname.setForeground(UIUtils.getColor("red"));
				mskillname.setBackground(new Color(0x4d,0x33,0xd7));
				//基础技能设为默认项	
				this.setDefault_skilllabel(mskillname);
				String name =mskillname.getName();
			    int index = Integer.parseInt(name.substring(name.length()-1,name.length()));
				int skillLevel = getSkillLevel(index);
			    properties.put("mskillsLevelExp", dataManager.getMSkillsLevelExp(skillLevel));
				properties.put("mskillsLevelSpend", dataManager.getMSkillsLevelSpend(skillLevel));
			}
		}
		this.updateLabelIcon();
		this.updateSchoolSkillLevel();
		this.updateLabels(panel);
        this.setAutoUpdate(true);
		//available_exp.setText(vo.get)
		
	}
	
	public void magics_down(ActionEvent evet){
		Label label = default_skilllabel;
		String skill_name = label.getName();
		int index = Integer.parseInt(skill_name.substring(skill_name.length()-1,skill_name.length()));
		String[] magics = skills.get(index).getMagicSkill().split("、");
		if(magics.length > 3 && mlistrow+3 < magics.length){
			mlistrow++;
			updateMagicsList(3,magics);
		}
	}
	
	public void magics_up(ActionEvent evet){
		if(mlistrow > 0){
			Label label = default_skilllabel;
			String skill_name = label.getName();
			int index = Integer.parseInt(skill_name.substring(skill_name.length()-1,skill_name.length()));
			String[] magics = skills.get(index).getMagicSkill().split("、");
			mlistrow--;
			updateMagicsList(3,magics);
		}
	}
	
	public void text_down(ActionEvent evt){
		if(!skilldesc.toString().equals("")){
			Label skilldes  = (Label) this.panel.findCompByName("技能说明");
			textrow++;
			String tt = skilldesc.toString();
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
		String tt = skilldesc.toString();
		for(int i=textrow; i>0; i--){
			int index = tt.indexOf("<br>")+4;
        	tt = tt.substring(index);
		}
    	String sdes = "<html>" + tt + "</html>";
		skilldes.setText(sdes);
	}
	
	public void learnSkill(ActionEvent evt){
		Player player = context.getPlayer();
    	PlayerVO vo = player.getData();
    	//默认项技能等级
    	Label label = this.getDefault_skilllabel();
        String skill_name = label.getName();
    	int index = Integer.parseInt(skill_name.substring(skill_name.length()-1,skill_name.length()));
    	int skillLevel = this.getSkillLevel(index);
    	//基础技能等级
    	String basic_skillname = this.getBasic_skilllabel().getName();
    	int bindex = Integer.parseInt(basic_skillname.substring(basic_skillname.length()-1,basic_skillname.length()));
    	int basic_skillLevel =this.getSkillLevel(bindex);
    	//升级所需经验和金钱
    	long mskillLevelExp = dataManager.getMSkillsLevelExp(skillLevel);
    	long mskillLevelSpend = dataManager.getMSkillsLevelSpend(skillLevel);
    	System.out.println("i,b is:"+index+","+bindex);
    	System.out.println("level is:"+skillLevel+","+basic_skillLevel);
    	if(skillLevel >= vo.level + 10){
    		System.out.println("所选技能等级不能超过人物等级+10");
    		helper.prompt( "所选技能等级不能超过人物等级+10", 2000);
    	}else if(skillLevel >= basic_skillLevel && index != bindex){
    		System.out.println("所选技能等级不能超过基础技能等级");
    		helper.prompt( "所选技能等级不能超过基础技能等级", 2000);
    	}else if(vo.exp < mskillLevelExp){
    		//经验不够
    		System.out.println("你的经验没达到升级所需的经验");
    		helper.prompt( "你的经验没达到升级所需的经验", 2000);
    		//MP3Player.play()
    	}else if(vo.money <mskillLevelSpend){
    		//金钱不够
    		System.out.println("你的金钱没达到升级所需的金钱");
    		helper.prompt( "你的捡钱没达到升级所需的金钱", 2000);
    	}else{
    		//helper.prompt( "恭喜你，升级咯~~加油吧！", 2000);
			//player.playEffect("level_up",false);
			MP3Player.play("sound/addon/level_up.mp3");
			String skill = "skill"+index;
			int skillLevelUp =vo.mskillsLevel.get(skill).intValue();
		    skillLevelUp += 1;
			vo.mskillsLevel.put(skill, skillLevelUp);
			vo.exp -= mskillLevelExp;
			vo.money -= mskillLevelSpend;
			properties.put("mskillsLevelExp", dataManager.getMSkillsLevelExp(skillLevelUp));
			properties.put("mskillsLevelSpend", dataManager.getMSkillsLevelSpend(skillLevelUp));
			this.updateSchoolSkillLevel();
			dataManager.recalcProperties(vo);
			this.updateLabels(panel);
    	}
	}
	
	private void processSkill(MouseEvent e){
		Object c = e.getComponent();
    	//PlayerVO vo = context.getPlayer().getData();
		if(c instanceof Label){
			Label label = (Label)c;
			String skill_name = label.getName();
			int index = Integer.parseInt(skill_name.substring(skill_name.length()-1,skill_name.length()));
			if(skill_name.contains("skillLevel")){
				mlistrow = 0;			
				int skillLevel = this.getSkillLevel(index);
				//选择技能设为默认项
				this.setDefault_skilllabel(label);
				this.setDefault_magiclabel(null);
				properties.put("mskillsLevelExp", dataManager.getMSkillsLevelExp(skillLevel));
				properties.put("mskillsLevelSpend", dataManager.getMSkillsLevelSpend(skillLevel));
				//设置背景色
				updateLabelBackground();
				Label mskillname = (Label) this.panel.findCompByName(skill_name);
				mskillname.setBackground(new Color(0x4d,0x33,0xd7));			
				mskillname.setOpaque(true);
				String[] magics = skills.get(index).getMagicSkill().split("、");
				magicskills.removeAll(magicskills);
				int length = Math.min(magics.length, 3);
				if(skills.get(index).getMagicSkill().equals("0")){
					length = 0;
				}
				updateMagicsList(length, magics);
				processText(skills.get(index));
			}
			else if(skill_name.contains("法术名")){
				//设置背景色
				Label magicname = (Label) this.panel.findCompByName(skill_name);
				//选择法术为默认项
				setDefault_magiclabel(magicname);
				//System.out.println("color is:"+magicname.getBackground());
				magicname.setBackground(new Color(0x4d,0x32,0xd7));
				updateLabelBackground();
				magicname.setOpaque(true);
				processText(magicskills.get(index));
			}
		}


	}
	
	private void updateMagicsList(int length, String[] magics){
		int index =0;
		for(int i=mlistrow; i<length+mlistrow; i++){
			Skill skill = dataManager.findSkillByName(magics[i]);
			magicskills.add(skill);	
			//System.out.println("法术名 is :"+index);
			Label magicskill  = (Label) this.panel.findCompByName("法术"+index);
			Label magicname  = (Label) this.panel.findCompByName("法术名"+index);
			magicskill.setAnim(SpriteFactory.loadAnimation("wzife/skillmagic/small/"+
			skill.getId()+".tcp"));
			String magic_name = "    "+magics[i];
			magicname.setText(magic_name);
			if(magicname.getMouseListeners().length == 0)magicname.addMouseListener(this);
            if(magicname.getMouseMotionListeners().length == 0)magicname.addMouseMotionListener(this);
			labels.add(magicname);
			index++;
		}
		for(int i=length;i<3;i++){
			Label magicskill  = (Label) this.panel.findCompByName("法术"+i);
			Label magicname  = (Label) this.panel.findCompByName("法术名"+i);
			magicskill.setAnim(SpriteFactory.loadAnimation(""));
			magicname.setText("");
			magicname.setOpaque(false);
			magicname.removeMouseListener(this);
			magicname.removeMouseMotionListener(this);
			labels.remove(magicname);
		}
	}
	
	private void updateLabelIcon(){
		Player player = context.getPlayer();
		String school = player.getData().school;
		skills = dataManager.findMainSkill(school);
		for(int s=0; s<skills.size(); s++){
			SkillMain skill = skills.get(s);
			Label mainskill  = (Label) this.panel.findCompByName("技能"+s);
			mainskill.setAnim(SpriteFactory.loadAnimation("wzife/skillmain/small/"+
			skill.getId()+".tcp"));
		}
	}
	
	private void updateSchoolSkillLevel(){ 
		Player player = context.getPlayer();
    	PlayerVO vo = player.getData();
		String school = player.getData().school;
		skills = dataManager.findMainSkill(school);
		for(int s=0; s<skills.size(); s++){
			SkillMain skill = skills.get(s);
			String name = "     "+skill.getName();
			StringBuffer sb = new StringBuffer ();
			sb.append(name);
			for(int l=name.length();l<10;l++){
				sb.append("  ");
			}
			Integer level =  (Integer) vo.getMskillsLevel().get("skill"+s);	
			sb.append(level.toString()+"/180");
			properties.put("skillLevel"+s, sb.toString());	
			String attr = inArray(attrs, skill.getEffection());
			if(attr != null){
				vo.attrsLevel.put(attr, level.intValue());
				//vo.setAttrsLevel(vo.attrsLevel);
			}
		}
	}

	/**
	 * LABEL背景设为透明
	 */
	private void updateLabelBackground(){
		for(Label label:labels){
		    label.setOpaque(false);
		}
		default_skilllabel.setOpaque(true);
		if(default_magiclabel!=null)default_magiclabel.setOpaque(true);
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
			Map<String, Object> prop = dataManager.getProperties(context.getPlayer());
			prop.putAll(properties);
			JexlContext jexlcontext = new MapContext(prop);
	        String result = expression.evaluate(jexlcontext).toString();
			String[] labelTexts = result.split("#;");
			for (String lText : labelTexts) {
				String[] values = lText.split("#=");
				if(values.length>1){
					//System.out.println("ltext is:"+lText);
					Label label = (Label) panel.findCompByName(values[0]);
					label.setText(values[1]);
				}
				
			}
		}

	}
	
	private void processText(Skill skill) {
		/*//技能名称
		String sname = skill.getName();
		Label skillname  = (Label) this.panel.findCompByName("技能名称");
        skillname.setText(sname);*/
		//技能说明
        textrow = 0;
        skilldesc.setLength(0);
		Label skilldes  = (Label) this.panel.findCompByName("技能说明");
		skilldes.setVerticalAlignment(JLabel.NORTH);
		//labels.add(skilldes);
		//labels.add(skillname);
		//技能描述
		String des = skill.getDescription();
		skilldesc.append(linefeed(skilldes,des));			
		//效果
		String effect = skill.getEffection();
		if(!effect.equals("0")){
			skilldesc.append(linefeed(skilldes,effect));
		}
		//使用条件
		if(skill.getConditions() != null){
			String conditions = skill.getConditions();
			skilldesc.append(linefeed(skilldes,conditions));
		}			
		//使用消耗
		if(skill.getConsumption() != null){
			String consumption = skill.getConsumption();
			skilldesc.append(linefeed(skilldes,consumption));
		}			
    	String sdes = "<html>" + skilldesc.toString() + "</html>";
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
    
    public Integer getSkillLevel(int index){
    	PlayerVO vo = context.getPlayer().getData();
    	return (Integer) vo.getMskillsLevel().get("skill"+index);
    }
    
	public void mouseClicked(MouseEvent e) {
		e.consume();
		switch(e.getButton()){
			case MouseEvent.BUTTON1:
				//左键点击技能图标
			    processSkill(e);
			    updateLabels(panel);
				break;
		}
	}

	public void mouseEntered(MouseEvent e) {
		Object c = e.getComponent();
		if(c instanceof Label){
			Label label = (Label)c;
			String name = label.getName();
			if(name.contains("skillLevel")){
				//设置背景色
				updateLabelBackground();
				Label mskillname = (Label) this.panel.findCompByName(name);
				if(!default_skilllabel.equals(mskillname)){
					mskillname.setBackground(new Color(0xff,0xff,0x80));
					mskillname.setOpaque(true);
				}else{
                    default_skilllabel.setBackground(new Color(0x3d,0x31,0xd1));
				}
				
			}else if(name.contains("法术名")){
				//设置背景色
				updateLabelBackground();
				Label magicname = (Label) this.panel.findCompByName(name);
				if(default_magiclabel != null){
					if(!default_magiclabel.equals(magicname)){
						magicname.setBackground(new Color(0xff,0xfa,0x80));
						magicname.setOpaque(true);
					}else{
						default_magiclabel.setBackground(new Color(0x3f,0x3c,0xd2));
					}
					default_magiclabel.setOpaque(true);
				}else{
					magicname.setBackground(new Color(0xff,0xfa,0x80));
					magicname.setOpaque(true);
				}
			}
			
		}
	}
	
	public void mouseDragged(MouseEvent arg0) {	
	}

	public void mouseMoved(MouseEvent arg0) {	
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public Label getDefault_skilllabel() {
		return default_skilllabel;
	}

	public void setDefault_skilllabel(Label default_skilllabel) {
		this.default_skilllabel = default_skilllabel;
	}

	public Label getDefault_magiclabel() {
		return default_magiclabel;
	}

	public void setDefault_magiclabel(Label default_magiclabel) {
		this.default_magiclabel = default_magiclabel;
	}
	
	public Label getBasic_skilllabel(){
		return basic_skilllabel;
	}
	
	public void setBasic_skilllabel(Label basic_skilllabel){
		this.basic_skilllabel = basic_skilllabel;
	}

}
