package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Item;

public class WeaponItem implements Item, Serializable{
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
	
	
	
	public WeaponItem(){	
	}
	
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getResNo() {
		return resNo;
	}

	public void setResNo(String resNo) {
		this.resNo = resNo;
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getCharacter(){
		return character;
	}
	
	public void setCharacter(String character){
		this.character = character;
	}
	
	public long getPrice(){
		return price;
	}
	
	public void setPrice(long price){
		this.price = price;
	}
	
	public long getAddAttribute1(){
		return addAttribute1;
	}
	
	public void setAddAttribute1(long add_attribute1){
		this.addAttribute1 = add_attribute1;
	}
	
	public long getAddAttribute2(){
		return addAttribute2;
	}
	
	public void setAddAttribute2(long add_attribute2){
		this.addAttribute2 = add_attribute2;
	}
	
	public long getAccuracy(){
		return accuracy;
	}
	
	public void setAccuracy(long accuracy){
		this.accuracy = accuracy;
	}
	
	public long getDamage(){
		return damage;
	}
	
	public void setDamage(long damage){
		this.damage = damage;
	}
	
	public long getDodge(){
		return dodge;
	}
	
	public void setDodge(long dodge){
		this.dodge = dodge;
	}
	
	public String getAddSkill(){
		return addSkill;
	}
	
	public void setAddSkill(String add_skill){
		this.addSkill = add_skill;
	}
	
	public String getEfficacy(){
		return efficacy;
	}
	
	public void setEfficacy(String efficacy){
		this.efficacy = efficacy;
	}
	
	public short getLevel(){
		return level;
	}
	
	public void setLevel(short level){
		this.level = level;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("WeaponItem [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", accuracy=");
		builder.append(accuracy);
		builder.append(", addAttribute1=");
		builder.append(addAttribute1);
		builder.append(", addAttribute2=");
		builder.append(addAttribute2);
		builder.append(", addSkill=");
		builder.append(addSkill);
		builder.append(", character=");
		builder.append(character);
		builder.append(", damage=");
		builder.append(damage);
		builder.append(", description=");
		builder.append(description);
		builder.append(", dodge=");
		builder.append(dodge);
		builder.append(", efficacy=");
		builder.append(efficacy);
		builder.append(", level=");
		builder.append(level);
		builder.append(", price=");
		builder.append(price);
		builder.append(", resNo=");
		builder.append(resNo);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeaponItem other = (WeaponItem) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
