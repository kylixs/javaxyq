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
@Table(name = "SCENE_NPC", catalog = "", schema = "APP")
@NamedQueries({
    @NamedQuery(name = "SceneNpc.findAll", query = "SELECT s FROM SceneNpc s"),
    @NamedQuery(name = "SceneNpc.findById", query = "SELECT s FROM SceneNpc s WHERE s.id = :id"),
    @NamedQuery(name = "SceneNpc.findBySceneId", query = "SELECT s FROM SceneNpc s WHERE s.sceneId = :sceneId"),
    @NamedQuery(name = "SceneNpc.findByCharacterId", query = "SELECT s FROM SceneNpc s WHERE s.characterId = :characterId"),
    @NamedQuery(name = "SceneNpc.findByName", query = "SELECT s FROM SceneNpc s WHERE s.name = :name"),
    @NamedQuery(name = "SceneNpc.findBySceneX", query = "SELECT s FROM SceneNpc s WHERE s.sceneX = :sceneX"),
    @NamedQuery(name = "SceneNpc.findBySceneY", query = "SELECT s FROM SceneNpc s WHERE s.sceneY = :sceneY"),
    @NamedQuery(name = "SceneNpc.findByConfig", query = "SELECT s FROM SceneNpc s WHERE s.config = :config"),
    @NamedQuery(name = "SceneNpc.findByDescription", query = "SELECT s FROM SceneNpc s WHERE s.description = :description")})
public class SceneNpc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "SCENE_ID", nullable = false)
    private int sceneId;
    @Basic(optional = false)
    @Column(name = "CHARACTER_ID", nullable = false, length = 20)
    private String characterId;
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;
    @Basic(optional = false)
    @Column(name = "SCENE_X", nullable = false)
    private int sceneX;
    @Basic(optional = false)
    @Column(name = "SCENE_Y", nullable = false)
    private int sceneY;
    @Basic(optional = false)
    @Column(name = "CONFIG", nullable = false, length = 2000)
    private String config;
    @Column(name = "DESCRIPTION", length = 400)
    private String description;

    public SceneNpc() {
    }

    public SceneNpc(Integer id) {
        this.id = id;
    }

    public SceneNpc(Integer id, int sceneId, String characterId, String name, int sceneX, int sceneY, String config) {
        this.id = id;
        this.sceneId = sceneId;
        this.characterId = characterId;
        this.name = name;
        this.sceneX = sceneX;
        this.sceneY = sceneY;
        this.config = config;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
    }

    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSceneX() {
        return sceneX;
    }

    public void setSceneX(int sceneX) {
        this.sceneX = sceneX;
    }

    public int getSceneY() {
        return sceneY;
    }

    public void setSceneY(int sceneY) {
        this.sceneY = sceneY;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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
        if (!(object instanceof SceneNpc)) {
            return false;
        }
        SceneNpc other = (SceneNpc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return String.format(
			"SceneNpc [id=%s, name=%s, characterId=%s, sceneId=%s, sceneX=%s, sceneY=%s, config=%s, description=%s]",
			id, name, characterId, sceneId, sceneX, sceneY, config, description);
	}

}
