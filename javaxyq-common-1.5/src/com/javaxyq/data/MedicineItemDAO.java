package com.javaxyq.data;

import java.util.List;


public interface MedicineItemDAO {

	public abstract void create(MedicineItem medicineItem) throws PreexistingEntityException, MedicineItemException;

	public abstract void edit(MedicineItem medicineItem) throws NonexistentEntityException, MedicineItemException;

	public abstract void destroy(Long id) throws NonexistentEntityException, MedicineItemException;

	public abstract List<MedicineItem> findMedicineItemEntities() throws MedicineItemException;

	public abstract List<MedicineItem> findMedicineItemEntities(int maxResults, int firstResult) throws MedicineItemException;

	public abstract MedicineItem findMedicineItem(Long id) throws MedicineItemException;

	public abstract int getMedicineItemCount() throws MedicineItemException;

	//************* custom *********************//
	public abstract MedicineItem findMedicineItemByName(String name) throws MedicineItemException;

	public abstract List<MedicineItem> findMedicineItemsByType(int type) throws MedicineItemException;

}