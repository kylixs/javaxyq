package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;

import com.javaxyq.model.Skill;



public interface BaseSkillDAO {

	void create(Skill skill) throws PreexistingEntityException, SQLException;

	void edit(Skill skill) throws NonexistentEntityException, SQLException;

	void destroy(Long id) throws NonexistentEntityException, SQLException;

	//************* custom *********************//
	
	Skill findSkillByName(String name) throws SQLException;

}