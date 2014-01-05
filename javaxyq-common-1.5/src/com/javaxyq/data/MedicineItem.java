/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.javaxyq.model.Item;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ITEM_MEDICINE", catalog = "", schema = "APP")
@NamedQueries({
    @NamedQuery(name = "MedicineItem.findAll", query = "SELECT m FROM MedicineItem m"),
    @NamedQuery(name = "MedicineItem.findById", query = "SELECT m FROM MedicineItem m WHERE m.id = :id"),
    @NamedQuery(name = "MedicineItem.findByName", query = "SELECT m FROM MedicineItem m WHERE m.name = :name"),
    @NamedQuery(name = "MedicineItem.findByDescription", query = "SELECT m FROM MedicineItem m WHERE m.description = :description"),
    @NamedQuery(name = "MedicineItem.findByPrice", query = "SELECT m FROM MedicineItem m WHERE m.price = :price"),
    @NamedQuery(name = "MedicineItem.findByHp", query = "SELECT m FROM MedicineItem m WHERE m.hp = :hp"),
    @NamedQuery(name = "MedicineItem.findByMp", query = "SELECT m FROM MedicineItem m WHERE m.mp = :mp"),
    @NamedQuery(name = "MedicineItem.findByInjury", query = "SELECT m FROM MedicineItem m WHERE m.injury = :injury"),
    @NamedQuery(name = "MedicineItem.findByType", query = "SELECT m FROM MedicineItem m WHERE m.type = :type"),
    @NamedQuery(name = "MedicineItem.findByEfficacy", query = "SELECT m FROM MedicineItem m WHERE m.efficacy = :efficacy"),
    @NamedQuery(name = "MedicineItem.findByLevel", query = "SELECT m FROM MedicineItem m WHERE m.level = :level")})
public class MedicineItem implements Item, Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 20)
    private String name;
    @Column(name = "DESCRIPTION", length = 400)
    private String description;
    @Basic(optional = false)
    @Column(name = "PRICE", nullable = false)
    private long price;
    @Basic(optional = false)
    @Column(name = "HP", nullable = false)
    private int hp;
    @Basic(optional = false)
    @Column(name = "MP", nullable = false)
    private int mp;
    @Basic(optional = false)
    @Column(name = "INJURY", nullable = false)
    private int injury;
    @Basic(optional = false)
    @Column(name = "TYPE", nullable = false, length = 10)
    private String type;
    @Column(name = "EFFICACY", length = 400)
    private String efficacy;
    @Basic(optional = false)
    @Column(name = "LEVEL", nullable = false)
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
