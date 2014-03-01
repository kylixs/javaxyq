package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Item;

public class WeaponItem implements Item, Serializable{
	private static final long serialVersionUID = 7254665375041414867L;

	private Long id;
	private String name;
	private String type;
	private String description;
	
	private long price;
	private long add_attribute1;
	private long add_attribute2;
	private long accuracy;
	private long damage;
	private long dodge;
	private String add_skill;
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
	
	public long getPrice(){
		return price;
	}
	
	public void setPrice(long price){
		this.price = price;
	}
	
	public long getAdd_attribute1(){
		return add_attribute1;
	}
	
	public void setAdd_attribute1(long add_attribute1){
		this.add_attribute1 = add_attribute1;
	}
	
	public long getAdd_attribute2(){
		return add_attribute2;
	}
	
	public void setAdd_attribute2(long add_attribute2){
		this.add_attribute2 = add_attribute2;
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
	
	public String getAdd_skill(){
		return add_skill;
	}
	
	public void setAdd_skill(String add_skill){
		this.add_skill = add_skill;
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
	
	public String toString() {
		return String
			.format(
				"WeaponItem [id=%s, name=%s, description=%s, price=%s, add_attribute1=%s, add_attribute2=%s, accuracy=%s, damage=%s, dodge=%s, type=%s, add_skill=%s, efficacy=%s, level=%s]",
				id, name, description, price, add_attribute1, add_attribute2, accuracy, damage, dodge, type, add_skill, efficacy, level);
	}
	
	@Override
	public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

}
