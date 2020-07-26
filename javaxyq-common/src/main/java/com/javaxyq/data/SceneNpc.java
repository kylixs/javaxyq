/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.javaxyq.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
}
