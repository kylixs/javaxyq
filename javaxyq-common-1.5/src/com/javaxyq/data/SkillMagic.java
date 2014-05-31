package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Skill;

public class SkillMagic implements Skill,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7706356833045691851L;
	private Long id;
	private String school;
	private String name;
	private String description;
	private String effection;
	private String conditions;
	private String consumption;
	private long type;
	private String magic;
	private long addon;
	private long waddon;
	private String action;
	
	public SkillMagic(){
		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MagicSkill [id=");
		builder.append(id);
		builder.append(", school=");
		builder.append(school);
		builder.append(", name=");
		builder.append(name);
		builder.append(", description=");
		builder.append(description);
		builder.append(", effection=");
		builder.append(effection);	
		builder.append(", conditions=");
		builder.append(conditions);
		builder.append(", consumption=");
		builder.append(consumption);
		builder.append(", type=");
		builder.append(type);
		builder.append(", magic=");
		builder.append(magic);
		builder.append(", addon=");
		builder.append(addon);
		builder.append(", waddon=");
		builder.append(waddon);
		builder.append(", action=");
		builder.append(action);
		builder.append("]");
		return builder.toString();
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

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getMagic() {
		return magic;
	}

	public void setMagic(String magic) {
		this.magic = magic;
	}

	public long getAddon() {
		return addon;
	}

	public void setAddon(long addon) {
		this.addon = addon;
	}

	public long getWaddon() {
		return waddon;
	}

	public void setWaddon(long waddon) {
		this.waddon = waddon;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


}
