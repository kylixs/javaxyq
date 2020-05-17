package com.javaxyq.data;

import java.sql.SQLException;

import com.javaxyq.model.Item;



public interface BaseItemDAO {

	void create(Item Item) throws PreexistingEntityException, SQLException;

	void edit(Item Item) throws NonexistentEntityException, SQLException;

	void destroy(Long id) throws NonexistentEntityException, SQLException;

	Item findItem(Long id) throws SQLException;

	int getItemCount() throws SQLException;

	//************* custom *********************//
	String findTypeByName(String name)throws SQLException;
	
	Item findItemByName(String name) throws SQLException;
}