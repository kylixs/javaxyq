/*
 * JavaXYQ NPC Scripts
 * home page: http://javaxyq.googlecode.com
 */

package npc;

import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.config.*;


/**
 * @author 
 * @date 2010-05-20 create
 */
public class n1009 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	String chat="待我修炼千年，就能变成神仙了#32";
    	ApplicationHelper.getApplication().doTalk(evt.getPlayer(), chat);
    	//add your code here
    }
	
}
