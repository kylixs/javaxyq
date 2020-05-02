/**
 * 
 */
package com.javaxyq.data.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.javaxyq.data.BaseItemDAO;
import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.WeaponItem;
import com.javaxyq.model.Item;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

/**
 * 药品存取实现类
 * @author gongdewei
 * @date 2011-4-30 create
 */
public class WeaponItemDAOImpl implements BaseItemDAO {

	private BeanListHandler<WeaponItem> resultHandler;

	public WeaponItemDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<WeaponItem>(WeaponItem.class, rowProcessor);
	}
	
	@Override
	public void create(Item item) throws PreexistingEntityException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Long id) throws NonexistentEntityException,SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(Item weaponitem) throws NonexistentEntityException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Item findItem(Long id) throws SQLException {
		String sql = "select * from ITEM_WEAPON where id=? ";
		List<WeaponItem> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, id);
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

	@Override
	public Item findItemByName(String name) throws SQLException {
		String sql = "select * from ITEM_WEAPON where name=? ";
		List<WeaponItem> results;
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


	public List<WeaponItem> findItemEntities() throws SQLException {
		String sql = "select * from ITEM_WEAPON ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<WeaponItem> results = runner.query(sql, resultHandler);
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	
	public List<WeaponItem> findItemEntities(int maxResults, int firstResult) throws SQLException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from ITEM_WEAPON OFFSET ? ROWS FETCH NEXT ? ROWS";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<WeaponItem> results = runner.query(sql, resultHandler, firstResult, maxResults);
			return results;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}

	
	public List<WeaponItem> findItemByType(int type) throws SQLException {
		String sql = "select * from ITEM_WEAPON where type=? ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<WeaponItem> results = runner.query(sql, resultHandler, Integer.toHexString(type));
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public String findTypeByName(String name) throws SQLException {
		String sql = "select * from ITEM_WEAPON where name=? ";
		List<WeaponItem> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new SQLException(ex);
		}
		if(results.size() == 1) {
			WeaponItem item = results.get(0);
			return item.getType();
		}else if(results.size() >= 1) {
			throw new SQLException("记录不唯一");
		}
		return null;
	}

	@Override
	public int getItemCount() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
