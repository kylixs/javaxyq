package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Skill;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SkillMain implements Skill, Serializable {

    private static final long serialVersionUID = -4733685782201972492L;

    private Long id;
    private String school;
    private String name;
    private String description;
    private String affection;
    private String magicSkill;
    private String basicSkill;
    private int level;

    @Override
    public String getConditions() {
        return null;
    }

    @Override
    public String getConsumption() {
        return null;
    }
}
