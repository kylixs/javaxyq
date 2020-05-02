package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface SceneTeleporterDAO {

	public abstract void create(SceneTeleporter sceneTeleporter) throws PreexistingEntityException, Exception;

	public abstract void edit(SceneTeleporter sceneTeleporter) throws NonexistentEntityException, Exception;

	public abstract void destroy(Integer id) throws NonexistentEntityException;

	public abstract List<SceneTeleporter> findSceneTeleporterEntities() throws SQLException;

	public abstract List<SceneTeleporter> findSceneTeleporterEntities(int maxResults, int firstResult) throws SQLException;

	public abstract SceneTeleporter findSceneTeleporter(Integer id) throws SQLException;

	public abstract int getSceneTeleporterCount() throws SQLException;

	//************************* custom *******************************//
	public abstract List<SceneTeleporter> findTeleportersBySceneId(int sceneId) throws SQLException;

}