package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface SceneNpcDAO {

	public abstract void create(SceneNpc sceneNpc) throws PreexistingEntityException, Exception;

	public abstract void edit(SceneNpc sceneNpc) throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<SceneNpc> findSceneNpcEntities() throws SQLException;

	public abstract List<SceneNpc> findSceneNpcEntities(int maxResults, int firstResult) throws SQLException;

	public abstract SceneNpc findSceneNpc(Integer id) throws SQLException;

	public abstract int getSceneNpcCount() throws SQLException;

	//************************* custom *******************************//
	public abstract List<SceneNpc> findNpcsBySceneId(int sceneId) throws SQLException;

	public abstract int getNextSceneNpcId() throws SQLException;

}