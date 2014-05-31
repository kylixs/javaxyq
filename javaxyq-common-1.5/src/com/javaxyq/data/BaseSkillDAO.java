package com.javaxyq.data;

import java.sql.SQLException;
import java.util.List;

import com.javaxyq.model.Skill;



public interface BaseSkillDAO {

	void create(Skill skill) throws PreexistingEntityException, SQLException;

	void edit(Skill skill) throws NonexistentEntityException, SQLException;

	void destroy(Long id) throws NonexistentEntityException, SQLException;

	//List<Item> findItemEntities() throws SQLException;

	//List<Item> findItemEntities(int maxResults, int firstResult) throws SQLException;

	//List<SkillMain> findSkillBySchool(String school) throws SQLException;


	//************* custom *********************//
	
	Skill findSkillByName(String name) throws SQLException;
	
	//public String findMagicSkillByName(String name) throws SQLException;

}