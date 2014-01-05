package com.javaxyq.event;


import java.awt.Point;
import java.util.EventListener;

import com.javaxyq.widget.Player;

public interface PlayerListener extends EventListener {

    void stepOver(Player player);
    
    void move(Player player, Point increment);

    /**
     * 人物行走事件
     * @param evt
     */
    void walk(PlayerEvent evt);

    void click(PlayerEvent evt);
    
    void detect(PlayerEvent evt);
    
    void talk(PlayerEvent evt);
    
    void attack(PlayerEvent evt);
    
    void give(PlayerEvent evt);
    
}
