package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Skill;

public class SkillMain implements Skill,Serializable{
	private Long id;
	private String school;
	private String name;
	private String description;
	private String effection;
	private String magicSkill;
	private String basicSkill;
	private int level;
	
	private static final long serialVersionUID = -4733685782201972492L;
	
	public SkillMain(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEffection() {
		return effection;
	}
	
	public void setEffection(String effection) {
		this.effection = effection;
	}
	
	public String getMagicSkill() {
		return magicSkill;
	}

	public void setMagicSkill(String magic) {
		this.magicSkill = magic;
	}

	public String getBasicSkill() {
		return basicSkill;
	}

	public void setBasicSkill(String basicSkill) {
		this.basicSkill = basicSkill;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MainSkill [id=");
		builder.append(id);
		builder.append(", school=");
		builder.append(school);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", effection=");
		builder.append(effection);
		builder.append(", magicskill=");
		builder.append(magicSkill);
		builder.append(", basicskill=");
		builder.append(basicSkill);
		builder.append(", level=");
		builder.append(level);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public String getConditions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConsumption() {
		// TODO Auto-generated method stub
		return null;
	}

}
