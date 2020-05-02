/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.io.Serializable;

import com.javaxyq.model.Item;

/**
 *
 * @author Administrator
 */
public class MedicineItem implements Item ,Serializable {
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

    public MedicineItem() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getInjury() {
        return injury;
    }

    public void setInjury(int injury) {
        this.injury = injury;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicineItem)) {
            return false;
        }
        MedicineItem other = (MedicineItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    //******************** custom *********************/
	@Override
	public String toString() {
		return String
			.format(
				"MedicineItem [id=%s, name=%s, type=%s, level=%s, price=%s, hp=%s, description=%s, efficacy=%s, injury=%s, mp=%s]",
				id, name, type, level, price, hp, description, efficacy, injury, mp);
	}

	/**
	 * 功效
	 */
	public String actualEfficacy() {
		boolean first = true;
		StringBuilder buf = new StringBuilder(32);
		if(hp != 0) {
			buf.append("恢复气血");
			buf.append(hp);
			buf.append("点");
			first = false;
		}
		if(mp != 0) {
			if(!first)buf.append("，");
			buf.append("恢复法力");
			buf.append(mp);
			buf.append("点");
			first = false;
		}
//		if(sp != 0) {
//			if(!first)buf.append("，");
//			buf.append("回复愤怒");
//			buf.append(sp);
//			buf.append("点");
//			first = false;
//		}
		if(injury != 0) {//治疗伤势
			if(!first)buf.append("，");
			buf.append("治疗伤势");
			buf.append(injury);
			buf.append("点");
			first = false;
		}
		return buf.toString();
	}
	
}
