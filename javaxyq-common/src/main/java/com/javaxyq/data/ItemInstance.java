/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-4-17
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.data;

import com.javaxyq.model.Item;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 物品实例对象
 *
 * @author gongdewei
 * @date 2010-4-17 create
 */
@Getter
@EqualsAndHashCode(of = {"itemId", "name"})
public class ItemInstance {

    transient private Item item;
    @Setter
    private int count;
    private long itemId = -1;
    private String name;
    private String type;

    public ItemInstance(Item item, int count) {
        this.setItem(item);
        this.setCount(count);
    }

    public void setItem(Item item) {
        this.item = item;
        this.itemId = item.getId();
        this.name = item.getName();
        this.type = item.getType();
    }

    /**
     * 改变物品数量
     *
     * @param n 改变量
     */
    public int inc(int n) {
        if (n < 0 && this.count + n < 0) {
            n = -this.count;
        }
        this.count += n;
        return n;
    }

    //---------- delegated methods ---------------//

    public Long getId() {
        return itemId;
    }

    public String getDescription() {
        return item.getDescription();
    }

    public short getLevel() {
        return item.getLevel();
    }

    public long getPrice() {
        return item.getPrice();
    }
}
