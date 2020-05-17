package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;


public interface MedicineItemDAO {

	void create(MedicineItem medicineItem) throws PreexistingEntityException, SQLException;

	void edit(MedicineItem medicineItem) throws NonexistentEntityException, SQLException;

	void destroy(Long id) throws NonexistentEntityException, SQLException;

	List<MedicineItem> findMedicineItemEntities() throws SQLException;

	List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) throws SQLException;

	MedicineItem findMedicineItem(Long id) throws SQLException;

	int getMedicineItemCount() throws SQLException;

	//************* custom *********************//
	MedicineItem findMedicineItemByName(String name) throws SQLException;

	List<MedicineItem> findMedicineItemsByType(int type) throws SQLException;

	String findTypeByName(String name)throws SQLException;
}