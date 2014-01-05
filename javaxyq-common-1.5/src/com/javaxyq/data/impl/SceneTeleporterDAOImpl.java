package com.javaxyq.data.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.data.SceneTeleporterDAO;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

public class SceneTeleporterDAOImpl implements SceneTeleporterDAO {

	private BeanListHandler<SceneTeleporter> resultHandler;
	public SceneTeleporterDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<SceneTeleporter>(SceneTeleporter.class, rowProcessor);
	}
	@Override
	public void create(SceneTeleporter sceneTeleporter) throws PreexistingEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Integer id) throws NonexistentEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(SceneTeleporter sceneTeleporter) throws NonexistentEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneTeleporter findSceneTeleporter(Integer id) throws SQLException {
		String sql = "select * from SCENE_TELEPORTER where id=? ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneTeleporter> results = runner.query(sql, resultHandler, id);
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new SQLException("¼ÇÂ¼²»Î¨Ò»");
		}
		return null;
	}

	@Override
	public List<SceneTeleporter> findSceneTeleporterEntities() throws SQLException {
		String sql = "select * from SCENE_TELEPORTER ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneTeleporter> results = runner.query(sql, resultHandler);
		return results;
	}

	@Override
	public List<SceneTeleporter> findSceneTeleporterEntities(int maxResults, int firstResult) throws SQLException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from SCENE_TELEPORTER OFFSET ? ROWS FECTH NEXT ? ROWS";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneTeleporter> results = runner.query(sql, resultHandler);
		return results;
	}

	@Override
	public List<SceneTeleporter> findTeleportersBySceneId(int sceneId) throws SQLException {
		String sql = "select * from SCENE_TELEPORTER where START_ID=? ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneTeleporter> results = runner.query(sql, resultHandler, sceneId);
		return results;
	}

	@Override
	public int getSceneTeleporterCount() throws SQLException {
		String sql = "select count(1) from SCENE_TELEPORTER ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		Object result = runner.query(sql, new ScalarHandler());
		int count = 0;
		if (result != null) {
			count = ((Integer) result).intValue();
		}
		return count;
	}

}
