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

import com.javaxyq.data.BaseSkillDAO;
import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SkillMain;
import com.javaxyq.model.Skill;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

/**
 *
 *
 * 
 */
public class SkillMainDAOImpl implements BaseSkillDAO{

	private BeanListHandler<SkillMain> resultHandler;

	public SkillMainDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<SkillMain>(SkillMain.class, rowProcessor);
	}
	
	@Override
	public void create(Skill mainSkill) throws PreexistingEntityException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Long id) throws NonexistentEntityException,SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(Skill mainSkill) throws NonexistentEntityException, SQLException {
		// TODO Auto-generated method stub

	}


	@Override
	public Skill findSkillByName(String name) throws SQLException {
		String sql = "select * from SKILL_MAIN where name=? ";
		List<SkillMain> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new SQLException(ex);
		}
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new SQLException("记录不唯一");
		}
		return null;
	}


	public List<SkillMain> findMedicineItemEntities() throws SQLException {
		String sql = "select * from SKILL_MAIN ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<SkillMain> results = runner.query(sql, resultHandler);
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}


	public List<SkillMain> findMedicineItemEntities(int maxResults, int firstResult) throws SQLException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from SKILL_MAIN OFFSET ? ROWS FETCH NEXT ? ROWS";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<SkillMain> results = runner.query(sql, resultHandler, firstResult, maxResults);
			return results;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}


	public List<SkillMain> findSkillBySchool(String school) throws SQLException {
		String sql = "select * from SKILL_MAIN where school=? ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<SkillMain> results = runner.query(sql, resultHandler, school);
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	
	public String findMagicSkillByName(String name) throws SQLException {
		String sql = "select * from SKILL_MAIN where name=? ";
		List<SkillMain> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new SQLException(ex);
		}
		if(results.size() == 1) {
			SkillMain skill = results.get(0);
			return skill.getMagicSkill();
		}else if(results.size() >= 1) {
			throw new SQLException("记录不唯一");
		}
		return null;
	}
    /*********************/

}
