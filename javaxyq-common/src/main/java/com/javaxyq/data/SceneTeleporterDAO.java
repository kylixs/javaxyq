package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface SceneTeleporterDAO {

	void create(SceneTeleporter sceneTeleporter) throws Exception;

	void edit(SceneTeleporter sceneTeleporter) throws Exception;

	void destroy(Integer id) throws NonexistentEntityException;

	List<SceneTeleporter> findSceneTeleporterEntities() throws SQLException;

	List<SceneTeleporter> findSceneTeleporterEntities(int maxResults, int firstResult) throws SQLException;

	SceneTeleporter findSceneTeleporter(Integer id) throws SQLException;

	int getSceneTeleporterCount() throws SQLException;

	//************************* custom *******************************//
	List<SceneTeleporter> findTeleportersBySceneId(int sceneId) throws SQLException;

}