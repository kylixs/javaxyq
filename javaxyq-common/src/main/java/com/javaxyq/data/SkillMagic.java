package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillMagic implements Skill, Serializable {

    private static final long serialVersionUID = 7706356833045691851L;

    private Long id;
    private String school;
    private String name;
    private String description;
    private String affection;
    private String conditions;
    private String consumption;
    private long type;
    private String magic;
    private long addon;
    private long waddon;
    private String action;

}
