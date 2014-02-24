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

import com.javaxyq.data.Items;
import com.javaxyq.data.ItemsDAO;
import com.javaxyq.data.MedicineItemException;
import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.util.DBToolkit;
import com.javaxyq.util.SmartBeanProcessor;

/**
 * 药品存取实现类
 * @author gongdewei
 * @date 2011-4-30 create
 */
public class ItemsDAOImpl implements ItemsDAO {

	private BeanListHandler<Items> resultHandler;

	public ItemsDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<Items>(Items.class, rowProcessor);
	}
	

	public void create(Items item) throws PreexistingEntityException, MedicineItemException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Long id) throws NonexistentEntityException,MedicineItemException {
		// TODO Auto-generated method stub

	}


	public void edit(Items item) throws NonexistentEntityException, MedicineItemException {
		// TODO Auto-generated method stub

	}


	public Items findItem(Long id) throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE where id=? ";
		List<Items> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, id);
		} catch (Exception ex) {
			throw new MedicineItemException(ex);
		}
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new MedicineItemException("记录不唯一");
		}
		return null;
	}


	public Items findItemByName(String name) throws MedicineItemException {
		String sql = "select * from ITEM where name=? ";
		List<Items> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new MedicineItemException(ex);
		}
		if(results.size() == 1) {
			return results.get(0);
		}else if(results.size() >= 1) {
			throw new MedicineItemException("记录不唯一");
		}
		return null;
	}


	public List<Items> findItemEntities() throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<Items> results = runner.query(sql, resultHandler);
			return results;
		} catch (Exception e) {
			throw new MedicineItemException(e);
		}
	}


	public List<Items> findItemEntities(int maxResults, int firstResult) throws MedicineItemException {
		if(firstResult < 1) {
			firstResult = 1;
		}
		if(maxResults < 0) {
			maxResults = 0;
		}
		String sql = "select * from ITEM_MEDICINE OFFSET ? ROWS FETCH NEXT ? ROWS";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<Items> results = runner.query(sql, resultHandler, firstResult, maxResults);
			return results;
		} catch (SQLException e) {
			throw new MedicineItemException(e);
		}
	}

	public List<Items> findItemsByType(int type) throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE where type=? ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<Items> results = runner.query(sql, resultHandler, Integer.toHexString(type));
			return results;
		} catch (Exception e) {
			throw new MedicineItemException(e);
		}
	}

	public int getItemCount() throws MedicineItemException {
		String sql = "select count(1) from ITEM_MEDICINE ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			Object result = runner.query(sql, new ScalarHandler());
			int count = 0;
			if (result != null) {
				count = ((Integer) result).intValue();
			}
			return count;
		} catch (Exception e) {
			throw new MedicineItemException(e);
		}
	}

	@Override
	public String findTypeByName(String name) throws MedicineItemException {
		String sql = "select * from ITEM where name=? ";
		List<Items> results;
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			results = runner.query(sql, resultHandler, name);
		} catch (Exception ex) {
			throw new MedicineItemException(ex);
		}
		if(results.size() == 1) {
			Items item = results.get(0);
			return item.getType();
		}else if(results.size() >= 1) {
			throw new MedicineItemException("记录不唯一");
		}
		return null;
	}

}
