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
import java.util.List;

import com.javaxyq.data.NonexistentEntityException;
import com.javaxyq.data.PreexistingEntityException;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneNpcJpaController;

/**
 * 数据访问门面类
 * 
 * @author gongdewei
 * @date 2010-5-10 create
 */
public class DataFacade {

	private String basePath;

	private SceneNpcJpaController npcController = new SceneNpcJpaController();

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
		int nextId = npcController.getNextSceneNpcId();
		npcVO.setId(nextId);
		npcController.create(npcVO);
		System.out.println("createSceneNpc: "+npcVO);
		return nextId;
	}

	public void deleteSceneNpc(Integer id) throws NonexistentEntityException {
		System.out.println("deleteSceneNpc: "+id);
		npcController.destroy(id);
	}

	public void updateSceneNpc(SceneNpc sceneNpc) throws NonexistentEntityException, Exception {
		System.out.println("updateSceneNpc: "+sceneNpc);
		npcController.edit(sceneNpc);
	}

	public SceneNpc findSceneNpc(Integer id) {
		return npcController.findSceneNpc(id);
	}

	public List<SceneNpc> findNpcsBySceneId(int sceneId) {
		return npcController.findNpcsBySceneId(sceneId);
	}

}
