package com.javaxyq.data;

import com.javaxyq.model.Item;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "name")
public class WeaponItem implements Item, Serializable {

    private static final long serialVersionUID = 7254665375041414867L;

    private Long id;
    private String resNo;
    private String name;
    private String type;
    private String description;
    private String character;

    private long price;
    private long addAttribute1;
    private long addAttribute2;
    private long accuracy;
    private long damage;
    private long dodge;
    private String addSkill;
    private String efficacy;
    private short level;
}
