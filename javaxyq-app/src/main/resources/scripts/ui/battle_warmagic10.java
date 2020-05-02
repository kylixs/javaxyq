/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.UIHelper;
import com.javaxyq.widget.Animation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dewitt
 * @date 2009-11-27 create
 */
public class battle_warmagic10 extends PanelHandler {
	Logger log = LoggerFactory.getLogger(this.getClass());
	private boolean initialized = false;
	private BattleCanvas battleCanvas;
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		if(initialized) return;
		initialized = true;
		if (super.canvas instanceof BattleCanvas) {
			this.battleCanvas = (BattleCanvas) super.canvas;
		}
		MouseListener mouseHandler = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				Label label = (Label) evt.getSource();
				String magicId = label.getText();
				battleCanvas.setSelectedMagic(magicId);
			}
		};
		
		String[] magicIds = {"0326","0327","0328","0329"};
		String[] magicNames = {"雷击","落岩","水攻","烈火"};
		for (int i = 0; i < magicIds.length; i++) {
			Animation anim = SpriteFactory.loadAnimation("/wzife/magic/normal/"+magicIds[i]+".tcp");
			Label label = (Label) panel.findCompByName("magic"+(i+1));
			label.setAnim(anim);
			label.setText(magicIds[i]);
			label.setToolTipText(magicNames[i]);
			UIHelper.removeAllMouseListeners(label);
			label.addMouseListener(mouseHandler);
		}
	}

}
