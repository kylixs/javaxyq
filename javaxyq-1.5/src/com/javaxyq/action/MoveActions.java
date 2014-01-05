package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

public class MoveActions extends BaseAction {

    public void doAction(ActionEvent e) {
        Player player = ApplicationHelper.getApplication().getContext().getPlayer();// 角色
        String cmd = e.getCommand();
        //stop
        if ("com.javaxyq.action.Stop".equals(cmd)) {
            player.stop(false);
            //player.setChatText("Yes,Stop!#95");
            return;
        }

        //move
        int direction = Sprite.DIR_DOWN_RIGHT;
        if ("com.javaxyq.action.MoveLeft".equals(cmd)) {// direction
            direction = Sprite.DIR_LEFT;
            //player.setChatText("向左移动#92");
        } else if ("com.javaxyq.action.MoveUp".equals(cmd)) {
            direction = Sprite.DIR_UP;
            //player.setChatText("向上移动#92");
        } else if ("com.javaxyq.action.MoveRight".equals(cmd)) {
            direction = Sprite.DIR_RIGHT;
            //player.setChatText("向右移动#92");
        } else if ("com.javaxyq.action.MoveDown".equals(cmd)) {
            direction = Sprite.DIR_DOWN;
            //player.setChatText("向下移动#92");
        } else {
            return;
        }
        player.stepTo(direction);
        player.setDirectionMoving(true);
    }

}
