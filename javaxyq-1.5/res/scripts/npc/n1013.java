/*
 * JavaXYQ NPC Scripts
 * home page: http://javaxyq.googlecode.com
 */

package npc;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;


/**
 * @author yourname
 * @date 2010-10-11 create
 */
public class n1013 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	String chat="小本生意，客官请细细挑选。#35";
    	ApplicationHelper.getApplication().doTalk(evt.getPlayer(), chat);
    	//add your code here
    }
	
}
