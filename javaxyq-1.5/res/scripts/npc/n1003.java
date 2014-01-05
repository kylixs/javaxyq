/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package npc;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.PlayerAdapter;
import com.javaxyq.event.PlayerEvent;
import com.javaxyq.model.Option;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1003 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	Option[] options = new Option[3];
    	options[0] = new Option("五庄观","transport","1146 12 11");
    	options[1] = new Option("傲来国","transport","1092 163 8");
    	options[2] = new Option("我还想周围看看","close");
    	ApplicationHelper.getApplication().doTalk(evt.getPlayer(), "客官,你要到哪里去呢?",options);
    }
	
}
