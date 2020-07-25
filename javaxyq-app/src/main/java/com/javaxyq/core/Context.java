/**
 *
 */
package com.javaxyq.core;

import com.javaxyq.widget.Player;
import lombok.Data;

/**
 * @author Administrator
 *
 */
@Data
public class Context {

    private GameWindow window;
    private Player player;
    private String scene;
    private Player talker;
}
