/**
 * 
 */
package com.javaxyq.profile;

import java.util.List;

/**
 * 存档管理类
 * @author gongdewei
 * @date 2011-5-2 create
 */
public interface ProfileManager {
	
	Profile loadProfile(String name) throws ProfileException;
	
	Profile newProfile(String name) throws ProfileException;
	
	void saveProfile(Profile profile) throws ProfileException;
	
	void removeProfile(Profile profile) throws ProfileException;
	
	List<Profile> listProfiles() throws ProfileException;
}
