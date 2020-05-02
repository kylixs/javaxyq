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

import com.javaxyq.data.BaseItemDAO;
import com.javaxyq.data.MedicineItem;
import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.model.Item;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

/**
 * 药品存取实现类
 * @author gongdewei
 * @date 2011-4-30 create
 */
public class MedicineItemDAOImpl implements BaseItemDAO{

	private BeanListHandler<MedicineItem> resultHandler;

	public MedicineItemDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<MedicineItem>(MedicineItem.class, rowProcessor);
	}
	
	@Override
	public void create(Item medicineItem) throws PreexistingEntityException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Long id) throws NonexistentEntityException,SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(Item medicineItem) throws NonexistentEntityException, SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Item findItem(Long id) throws SQLException {
		String sql = "select * from ITEM_MEDICINE where id=? ";
		List<MedicineItem> results;
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
		String sql = "select * from ITEM_MEDICINE where name=? ";
		List<MedicineItem> results;
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


	public List<MedicineItem> findMedicineItemEntities() throws SQLException {
		String sql = "select * from ITEM_MEDICINE ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<MedicineItem> results = runner.query(sql, resultHandler);
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}


	public List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) throws SQLException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from ITEM_MEDICINE OFFSET ? ROWS FETCH NEXT ? ROWS";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<MedicineItem> results = runner.query(sql, resultHandler, firstResult, maxResults);
			return results;
		} catch (SQLException e) {
			throw new SQLException(e);
		}
	}


	public List<MedicineItem> findMedicineItemsByType(int type) throws SQLException {
		String sql = "select * from ITEM_MEDICINE where type=? ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<MedicineItem> results = runner.query(sql, resultHandler, Integer.toHexString(type));
			return results;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	@Override
	public int getItemCount() throws SQLException {
		String sql = "select count(1) from ITEM_MEDICINE ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			Object result = runner.query(sql, new ScalarHandler());
			int count = 0;
			if (result != null) {
				count = ((Number) result).intValue();
			}
			return count;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}
	
	@Override
	public String findTypeByName(String name) throws SQLException {
		String sql = "select * from ITEM_MEDICINE where name=? ";
		List<MedicineItem> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new SQLException(ex);
		}
		if(results.size() == 1) {
			MedicineItem item = results.get(0);
			return item.getType();
		}else if(results.size() >= 1) {
			throw new SQLException("记录不唯一");
		}
		return null;
	}
    /*********************/



}
