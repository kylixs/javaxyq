/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-29
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.model;

import lombok.Data;

/**
 * 对话的选项
 *
 * @author gongdewei
 * @date 2010-4-29 create
 */
@Data
public class Option {

    private String text;
    private String action;
    private Object value;
    private boolean selected;

    public Option(String text, String action) {
        this(text, action, null);
    }

    public Option(String text, String action, Object value) {
        super();
        this.text = text;
        this.action = action;
        this.value = value;
    }
}
