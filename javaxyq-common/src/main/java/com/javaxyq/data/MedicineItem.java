/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.javaxyq.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class MedicineItem implements Item, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private long price;
    private int hp;
    private int mp;
    private int injury;
    private String type;
    private String efficacy;
    private short level;

    public MedicineItem(Long id) {
        this.id = id;
    }

    public MedicineItem(Long id, String name, long price, int hp, int mp, int injury, String type, short level) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.hp = hp;
        this.mp = mp;
        this.injury = injury;
        this.type = type;
        this.level = level;
    }

    /**
     * 功效
     */
    public String actualEfficacy() {
        List<String> buf = new ArrayList<>();
        if (hp != 0) buf.add("恢复气血" + hp + "点");
        if (mp != 0) buf.add("恢复发力" + mp + "点");
        // if (sp != 0) buf.add("恢复愤怒" + sp + "点");
        if (injury != 0) buf.add("治疗伤势" + injury + "点");
        return String.join("，", buf);
    }
}
