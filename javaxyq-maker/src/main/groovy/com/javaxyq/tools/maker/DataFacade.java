/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-10
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package com.javaxyq.tools.maker;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.List;

import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneNpcDAO;
import com.javaxyq.data.impl.SceneNpcDAOImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据访问门面类
 * 
 * @author gongdewei
 * @date 2010-5-10 create
 */
@Slf4j
public class DataFacade {

	private String basePath;

	private SceneNpcDAO npcDao = new SceneNpcDAOImpl();

	public DataFacade(String basePath) {
		super();
		this.basePath = basePath;
	}

	private FilenameFilter sceneFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return dir.isFile() && (name.endsWith(".map") || name.endsWith(".MAP"));
		}
	};
	private FilenameFilter dirFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			File file = new File(dir, name);
			return file.isDirectory() && !file.isHidden();
		}
	};

	public String[] getAllCharacters() {
		File chardir = new File(this.basePath + "shape/char/");
		return chardir.list(dirFilter);
	}

	public String[] getAllScenes() {
		File chardir = new File(this.basePath + "scene/");
		return chardir.list(sceneFilter);
	}

	/**
	 * @param npcVO
	 * @return
	 * @throws Exception 
	 * @throws PreexistingEntityException 
	 * @throws Exception
	 * @throws PreexistingEntityException
	 */
	public int createSceneNpc(SceneNpc npcVO) throws PreexistingEntityException, Exception {
		int nextId = npcDao.getNextSceneNpcId();
		npcVO.setId(nextId);
		npcDao.create(npcVO);
		log.info("createSceneNpc: "+npcVO);
		return nextId;
	}

	public void deleteSceneNpc(Integer id) throws NonexistentEntityException {
		log.info("deleteSceneNpc: "+id);
		try {
			npcDao.destroy(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateSceneNpc(SceneNpc sceneNpc) throws NonexistentEntityException, Exception {
		log.info("updateSceneNpc: "+sceneNpc);
		npcDao.edit(sceneNpc);
	}

	public SceneNpc findSceneNpc(Integer id) throws SQLException {
		return npcDao.findSceneNpc(id);
	}

	public List<SceneNpc> findNpcsBySceneId(int sceneId) throws SQLException {
		return npcDao.findNpcsBySceneId(sceneId);
	}

}
