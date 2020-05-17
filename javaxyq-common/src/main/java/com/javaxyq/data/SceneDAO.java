package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface SceneDAO {

	void create(Scene scene) throws Exception;

	void edit(Scene scene) throws Exception;

	void destroy(Integer id) throws NonexistentEntityException;

	List<Scene> findSceneEntities()throws SQLException;

	List<Scene> findSceneEntities(int maxResults, int firstResult)throws SQLException;

	Scene findScene(Integer id) throws SQLException;

	int getSceneCount()throws SQLException;

}