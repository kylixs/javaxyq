package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface SceneDAO {

	public abstract void create(Scene scene) throws PreexistingEntityException, Exception;

	public abstract void edit(Scene scene) throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<Scene> findSceneEntities()throws SQLException;

	public abstract List<Scene> findSceneEntities(int maxResults, int firstResult)throws SQLException;

	public abstract Scene findScene(Integer id) throws SQLException;

	public abstract int getSceneCount()throws SQLException;

}