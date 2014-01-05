/**
 * 
 */
package com.javaxyq.data.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.Scene;
import com.javaxyq.data.SceneDAO;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

/**
 * @author gongdewei
 * @date 2011-5-1 create
 */
public class SceneDAOImpl implements SceneDAO {

	private BeanListHandler<Scene> resultHandler;

	public SceneDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<Scene>(Scene.class, rowProcessor);
	}
	
	@Override
	public void create(Scene scene) throws PreexistingEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Integer id) throws NonexistentEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(Scene scene) throws NonexistentEntityException, Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Scene findScene(Integer id) throws SQLException {
		String sql = "select * from SCENE where id=? ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<Scene> results = runner.query(sql, resultHandler, id);
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new SQLException("¼ÇÂ¼²»Î¨Ò»");
		}
		return null;
	}

	@Override
	public List<Scene> findSceneEntities() throws SQLException {
		String sql = "select * from SCENE ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<Scene> results = runner.query(sql, resultHandler);
		return results;
	}

	@Override
	public List<Scene> findSceneEntities(int maxResults, int firstResult) throws SQLException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from SCENE OFFSET ? ROWS FECTH NEXT ? ROWS";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		List<Scene> results = runner.query(sql, resultHandler, firstResult, maxResults);
		return results;
	}

	@Override
	public int getSceneCount() throws SQLException {
		String sql = "select count(1) from SCENE ";
		QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
		Object result = runner.query(sql, new ScalarHandler());
		int count = 0;
		if (result != null) {
			count = ((Integer) result).intValue();
		}
		return count;
	}

}
