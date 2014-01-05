/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.resources;

import com.javaxyq.core.Pluggable;
import com.javaxyq.widget.Sprite;

/**
 * @author 龚德伟
 * @history 2008-5-22 龚德伟 新建
 */
public interface SpriteProvider extends ResourceProvider<Sprite>,Pluggable{
    Sprite getSprite(String id);
}
