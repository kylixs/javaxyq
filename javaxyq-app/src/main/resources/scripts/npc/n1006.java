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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author gongdewei
 * @date 2010-4-25 create
 */
public class n1006 extends PlayerAdapter {
	Logger log = LoggerFactory.getLogger(this.getClass());

    public void talk(PlayerEvent evt) {
    	log.info("talk: "+this.getClass().getName());
    	Option[] options = new Option[] {
	    	new Option("购买","open","buy"),
	    	new Option("告辞","close")};
    	ApplicationHelper.getApplication().chat(evt.getPlayer(), "小店有各种草药，价廉物美，请客官随便挑选。",options);
    }
	
}
