/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import java.io.Serializable;

/**
 *
 * @author Administrator
 */
public class SceneNpc implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private int sceneId;
    private String characterId;
    private String name;
    private int sceneX;
    private int sceneY;
    private String config;
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
