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

import com.javaxyq.data.MedicineItem;
import com.javaxyq.data.MedicineItemDAO;
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
public class MedicineItemDAOImpl implements MedicineItemDAO {

	private BeanListHandler<MedicineItem> resultHandler;

	public MedicineItemDAOImpl() {
		BasicRowProcessor rowProcessor = new BasicRowProcessor(new SmartBeanProcessor());
		resultHandler = new BeanListHandler<MedicineItem>(MedicineItem.class, rowProcessor);
	}
	
	@Override
	public void create(MedicineItem medicineItem) throws PreexistingEntityException, MedicineItemException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(Long id) throws NonexistentEntityException,MedicineItemException {
		// TODO Auto-generated method stub

	}

	@Override
	public void edit(MedicineItem medicineItem) throws NonexistentEntityException, MedicineItemException {
		// TODO Auto-generated method stub

	}

	@Override
	public MedicineItem findMedicineItem(Long id) throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE where id=? ";
		List<MedicineItem> results;
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

	@Override
	public MedicineItem findMedicineItemByName(String name) throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE where name=? ";
		List<MedicineItem> results;
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

	@Override
	public List<MedicineItem> findMedicineItemEntities() throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<MedicineItem> results = runner.query(sql, resultHandler);
			return results;
		} catch (Exception e) {
			throw new MedicineItemException(e);
		}
	}

	@Override
	public List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) throws MedicineItemException {
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
			throw new MedicineItemException(e);
		}
	}

	@Override
	public List<MedicineItem> findMedicineItemsByType(int type) throws MedicineItemException {
		String sql = "select * from ITEM_MEDICINE where type=? ";
		try {
			QueryRunner runner = new QueryRunner(DBToolkit.getDataSource());
			List<MedicineItem> results = runner.query(sql, resultHandler, Integer.toHexString(type));
			return results;
		} catch (Exception e) {
			throw new MedicineItemException(e);
		}
	}

	@Override
	public int getMedicineItemCount() throws MedicineItemException {
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

}
