/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-11-27
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package ui;

import com.javaxyq.battle.BattleCanvas;
import com.javaxyq.core.GameMain;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;

/**
 * @author dewitt
 * @date 2009-11-27 create
 */
public class battle_role_cmd extends PanelHandler {

	private BattleCanvas canvas;
	@Override
	public void initial(PanelEvent evt) {
		super.initial(evt);
		if (super.canvas instanceof BattleCanvas) {
			this.canvas = (BattleCanvas) super.canvas;
		}
	}
	
	public void warmagic(ActionEvent evt) {
		canvas.selectMagic();
	}
	public void waritem(ActionEvent evt) {
		canvas.selectItem();
	}
	public void wardefend(ActionEvent evt) {
		canvas.defendCmd();
	}
	public void warcatch(ActionEvent evt) {
		
	}
	public void warrunaway(ActionEvent evt) {
		canvas.runawayCmd();
	}
}
