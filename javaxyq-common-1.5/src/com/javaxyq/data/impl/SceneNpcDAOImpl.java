package com.javaxyq.data.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneNpcDAO;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

public class SceneNpcDAOImpl implements SceneNpcDAO {

	private BeanListHandler<SceneNpc> resultHandler;

	public SceneNpcDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<SceneNpc>(SceneNpc.class, rowProcessor);
	}
	
	@Override
	public void create(SceneNpc sceneNpc) throws PreexistingEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Integer id) throws NonexistentEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(SceneNpc sceneNpc) throws NonexistentEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<SceneNpc> findNpcsBySceneId(int sceneId) throws SQLException {
		String sql = "select * from SCENE_NPC where SCENE_ID=? ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneNpc> results = runner.query(sql, resultHandler, sceneId);
		return results;
	}

	@Override
	public SceneNpc findSceneNpc(Integer id) throws SQLException{
		String sql = "select * from SCENE_NPC where id=? ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneNpc> results = runner.query(sql, resultHandler, id);
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new SQLException("¼ÇÂ¼²»Î¨Ò»");
		}
		return null;
	}

	@Override
	public List<SceneNpc> findSceneNpcEntities() throws SQLException {
		String sql = "select * from SCENE_NPC ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneNpc> results = runner.query(sql, resultHandler);
		return results;
	}

	@Override
	public List<SceneNpc> findSceneNpcEntities(int maxResults, int firstResult) throws SQLException{
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from SCENE_NPC OFFSET ? ROWS FECTH NEXT ? ROWS";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<SceneNpc> results = runner.query(sql, resultHandler);
		return results;
	}

	@Override
	public int getNextSceneNpcId() throws SQLException{
		String sql = "select max(id) from SCENE_NPC ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		Object result = runner.query(sql, new ScalarHandler());
		int maxId = 0;
		if (result != null) {
			maxId = ((Integer) result).intValue();
		}
		return maxId+1;
	}

	@Override
	public int getSceneNpcCount() throws SQLException{
		String sql = "select count(1) from SCENE_NPC ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		Object result = runner.query(sql, new ScalarHandler());
		int count = 0;
		if (result != null) {
			count = ((Integer) result).intValue();
		}
		return count;
	}

}
