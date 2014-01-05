package ui;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;
import org.apache.commons.jexl2.UnifiedJEXL.Expression;

import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.ui.Button;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.util.MP3Player;
import com.javaxyq.util.StringUtils;
import com.javaxyq.widget.Player;

/**
 * 人物状态窗口
 * @author dewitt
 * @date 2009-11-26 create
 */
public class player_status extends PanelHandler{
	
	private Expression expression;
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		System.out.println("initial：player_status ");
		this.updateLabels(panel);
		this.setAutoUpdate(true);
	}
	
	public void dispose(PanelEvent evt) {
		super.dispose(evt);
		System.out.println("dispose: player_status ");
	}
	
	public void assignPoints(ActionEvent evt) {
		Button button = (Button) evt.getSource();
		Panel panel = (Panel) button.getParent();
	
		Player player = context.getPlayer();
		PlayerVO vo = player.getData();
		Set<Entry<String, Integer>> entries = vo.assignPoints.entrySet();
		for (Entry<String, Integer> entry : entries) {
			try {
				String key = entry.getKey();
				Integer value = (Integer) PropertyUtils.getProperty(vo, key);
				value += entry.getValue();
				PropertyUtils.setProperty(vo, key, value);
				entry.setValue(0);
				Button btn = (Button) panel.findCompByName("-"+key);
				btn.setEnabled(false);
				Label label = (Label) panel.findCompByName(key);
				label.setForeground(Color.BLACK);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		dataManager.recalcProperties(player.getData());
	}

	public void level_up(ActionEvent evt) {
    	Player player = context.getPlayer();
    	PlayerVO vo = player.getData();
		long levelExp = dataManager.getLevelExp(vo.level);
		if(vo.exp >= levelExp) {
			helper.prompt( "恭喜你，升级咯~~加油吧！", 2000);
			player.playEffect("level_up",false);
			MP3Player.play("sound/addon/level_up.mp3");
			vo.level += 1;
			vo.physique += 1;
			vo.magic += 1;
			vo.strength += 1;
			vo.agility += 1;
			vo.durability += 1;
			vo.potentiality += 5;
			vo.exp -= levelExp;
			dataManager.recalcProperties(vo);
			
			Button button = (Button) evt.getSource();
			Panel panel = (Panel) button.getParent();
			updateLabels(panel);
		}else {
			//经验不够
			System.out.println("你的经验没达到升级所需的经验");
			helper.prompt( "你的经验没达到升级所需的经验", 2000);
			//MP3Player.play()
		}
	}

	/**
	 * 增加属性点
	 * @param evt
	 */
	public void add_point(ActionEvent evt) {
		System.out.println("add point: "+evt);
		//取参数 , like 'add_point 体质'
		String attr = evt.getCommand().split(" ")[1];
		Label label = (Label) panel.findCompByName(attr);
		PlayerVO vo = context.getPlayer().getData();
		if(vo.potentiality>0) {
			vo.assignPoints.put(attr,vo.assignPoints.get(attr)+1);
			vo.potentiality -=1;
			label.setForeground(Color.RED);
			Button btn = (Button) panel.findCompByName("-"+attr);
			btn.setEnabled(true);
		}
		updateLabels(panel);
	}
	
	/**
	 * 减少属性点
	 * @param evt
	 */
	public void subtract_point(ActionEvent evt) {
		System.out.println("subtract point: " + evt);
		//取参数 , like 'add_point 体质'
		String attr = evt.getCommand().split(" ")[1];
		Label label = (Label) panel.findCompByName(attr);
		PlayerVO vo = context.getPlayer().getData();
		if(vo.assignPoints.get(attr)>0) {
			vo.assignPoints.put(attr,vo.assignPoints.get(attr)-1);
			vo.potentiality += 1;
		}
		if (vo.assignPoints.get(attr) == 0) {
			label.setForeground(Color.BLACK);
			Button btn = (Button) panel.findCompByName("-"+attr);
			btn.setEnabled(false);
		}
		updateLabels(panel);
	}

	public void openSkills(ActionEvent evt) {
		helper.showHideDialog("main_skill");
	}
	
	public void changeTitle(ActionEvent evt) {
		System.out.println("称谓");
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
				System.out.println("创建JEXL表达式失败");
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
	
}
