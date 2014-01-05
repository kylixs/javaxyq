/*
 * JavaXYQ NPC Scripts
 * home page: http://javaxyq.googlecode.com
 */

package npc;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;


/**
 * @author 
 * @date 2010-05-20 create
 */
public class n1011 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	String chat="人家说我的花篮很好看#92";
    	ApplicationHelper.getApplication().doTalk(evt.getPlayer(), chat);
    	//add your code here
    }
	
}
