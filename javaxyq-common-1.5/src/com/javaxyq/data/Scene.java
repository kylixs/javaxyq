/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "SCENE", catalog = "", schema = "APP")
@NamedQueries({
    @NamedQuery(name = "Scene.findAll", query = "SELECT s FROM Scene s"),
    @NamedQuery(name = "Scene.findById", query = "SELECT s FROM Scene s WHERE s.id = :id"),
    @NamedQuery(name = "Scene.findByName", query = "SELECT s FROM Scene s WHERE s.name = :name"),
    @NamedQuery(name = "Scene.findByDescription", query = "SELECT s FROM Scene s WHERE s.description = :description")})
public class Scene implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    public Scene() {
    }

    public Scene(Integer id) {
        this.id = id;
    }

    public Scene(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Scene)) {
            return false;
        }
        Scene other = (Scene) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return String.format("Scene [id=%s, name=%s, description=%s]", id, name, description);
	}

}
