/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-25
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

package npc;

import com.javaxyq.model.Option;
import com.javaxyq.core.*;
import com.javaxyq.event.*;
import com.javaxyq.config.*;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1001 extends PlayerAdapter {
	
    public void talk(PlayerEvent evt) {
    	System.out.println("talk: "+this.getClass().getName());
    	
    	String chat="从这里可以传送到下面的地方#56";
    	Option[] options = new Option[3];
    	options[0] = new Option("傲来国","transport","1092 123 95");
    	options[1] = new Option("东海湾","transport","1506 62 21");
    	options[2] = new Option("哪也不去","close");
    	Option result = ApplicationHelper.getApplication().doTalk(evt.getPlayer(), chat,options);
    	
    	System.out.println("result: "+result);
    }
	
}
