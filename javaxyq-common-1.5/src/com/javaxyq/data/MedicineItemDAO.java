package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface MedicineItemDAO {

	public abstract void create(MedicineItem medicineItem) throws PreexistingEntityException, SQLException;

	public abstract void edit(MedicineItem medicineItem) throws NonexistentEntityException, SQLException;

	public abstract void destroy(Long id) throws NonexistentEntityException, SQLException;

	public abstract List<MedicineItem> findMedicineItemEntities() throws SQLException;

	public abstract List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) throws SQLException;

	public abstract MedicineItem findMedicineItem(Long id) throws SQLException;

	public abstract int getMedicineItemCount() throws SQLException;

	//************* custom *********************//
	public abstract MedicineItem findMedicineItemByName(String name) throws SQLException;

	public abstract List<MedicineItem> findMedicineItemsByType(int type) throws SQLException;

	public abstract String findTypeByName(String name)throws SQLException;
}