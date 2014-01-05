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
@Table(name = "SCENE_TELEPORTER", catalog = "", schema = "APP")
@NamedQueries({
    @NamedQuery(name = "SceneTeleporter.findAll", query = "SELECT s FROM SceneTeleporter s"),
    @NamedQuery(name = "SceneTeleporter.findById", query = "SELECT s FROM SceneTeleporter s WHERE s.id = :id"),
    @NamedQuery(name = "SceneTeleporter.findByStartId", query = "SELECT s FROM SceneTeleporter s WHERE s.startId = :startId"),
    @NamedQuery(name = "SceneTeleporter.findByEndId", query = "SELECT s FROM SceneTeleporter s WHERE s.endId = :endId"),
    @NamedQuery(name = "SceneTeleporter.findByStartPoint", query = "SELECT s FROM SceneTeleporter s WHERE s.startPoint = :startPoint"),
    @NamedQuery(name = "SceneTeleporter.findByEndPoint", query = "SELECT s FROM SceneTeleporter s WHERE s.endPoint = :endPoint"),
    @NamedQuery(name = "SceneTeleporter.findByDescription", query = "SELECT s FROM SceneTeleporter s WHERE s.description = :description")})
public class SceneTeleporter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "START_ID", nullable = false)
    private int startId;
    @Basic(optional = false)
    @Column(name = "END_ID", nullable = false)
    private int endId;
    @Basic(optional = false)
    @Column(name = "START_POINT", nullable = false, length = 8)
    private String startPoint;
    @Basic(optional = false)
    @Column(name = "END_POINT", nullable = false, length = 8)
    private String endPoint;
    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    public SceneTeleporter() {
    }

    public SceneTeleporter(Integer id) {
        this.id = id;
    }

    public SceneTeleporter(Integer id, int startId, int endId, String startPoint, String endPoint) {
        this.id = id;
        this.startId = startId;
        this.endId = endId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public int getEndId() {
        return endId;
    }

    public void setEndId(int endId) {
        this.endId = endId;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
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
        if (!(object instanceof SceneTeleporter)) {
            return false;
        }
        SceneTeleporter other = (SceneTeleporter) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
	public String toString() {
		return String.format(
			"SceneTeleporter [id=%s, startId=%s, startPoint=%s, endId=%s, endPoint=%s, description=%s]", id, startId,
			startPoint, endId, endPoint, description);
	}

}
