package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Skill;

public class SkillMain implements Skill,Serializable{
	private Long id;
	private String school;
	private String name;
	private String description;
	private String effection;
	private String magic_skill;
	
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

	public String getMagic_skill() {
		return magic_skill;
	}

	public void setMagic_skill(String magic_skill) {
		this.magic_skill = magic_skill;
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
		builder.append(", magic_skill=");
		builder.append(magic_skill);
		builder.append("]");
		return builder.toString();
	}

}
