package com.javaxyq.data;

import java.util.List;



public interface ItemsDAO {

	public abstract void create(Items Item) throws PreexistingEntityException, MedicineItemException;

	public abstract void edit(Items Item) throws NonexistentEntityException, MedicineItemException;

	public abstract void destroy(Long id) throws NonexistentEntityException, MedicineItemException;

	//public abstract List<Items> findItemEntities() throws MedicineItemException;

	//public abstract List<Items> findItemEntities(int maxResults, int firstResult) throws MedicineItemException;

	public abstract Items findItem(Long id) throws MedicineItemException;

	public abstract int getItemCount() throws MedicineItemException;

	//************* custom *********************//
	public abstract String findTypeByName(String name)throws MedicineItemException;
	
	public abstract Items findItemByName(String name) throws MedicineItemException;

	//public abstract List<Items> findItemsByType(int type) throws MedicineItemException;

}