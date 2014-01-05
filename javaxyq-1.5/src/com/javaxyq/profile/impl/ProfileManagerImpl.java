/**
 * 
 */
package com.javaxyq.profile.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.MedicineItemDAO;
import com.javaxyq.data.impl.MedicineItemDAOImpl;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.util.SuffixFilenameFilter;

/**
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class ProfileManagerImpl implements ProfileManager {

	private MedicineItemDAO medicineDAO;
	
	public ProfileManagerImpl() {
		medicineDAO = new MedicineItemDAOImpl();
//		sceneDAO = new SceneDAOImpl();
//		sceneNpcDAO = new SceneNpcDAOImpl();
//		sceneTeleporterDAO = new SceneTeleporterDAOImpl();
	}
	
	@Override
	public List<Profile> listProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		File dir = CacheManager.getInstance().getFile("save");
		if (dir != null && dir.isDirectory()) {
			File[] files = dir.listFiles(new SuffixFilenameFilter(".jxd"));
			for(int i=0; (files!=null && i<files.length); i++) {
				try {
					Profile profile = loadProfile(files[i]);
					profiles.add(profile);
				} catch (ProfileException e) {
					e.printStackTrace();
				}
			}
		}
		return profiles;
	}

	@Override
	public Profile loadProfile(String name) throws ProfileException {
		System.out.println("loading game data: "+new java.util.Date());
		File file = getProfileFile(name, false);
		return loadProfile(file);
	}

	private Profile loadProfile(File file) throws ProfileException {
		if(file==null || !file.exists() || file.length()==0) {
			throw new ProfileException("找不到游戏存档："+file.getAbsolutePath());
		}
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Date createDate = (Date) ois.readObject();
			System.out.printf("读取游戏存档（创建于%s）...\n", createDate);
			//场景ID
			String sceneId = ois.readUTF();
			//人物数据
			PlayerVO playerData = (PlayerVO) ois.readObject();
			//物品数据
			ItemInstance[] items = new ItemInstance[ois.readInt()];
			for(int i=0;i<items.length;i++) {
				ItemInstance _inst = (ItemInstance) ois.readObject();
				items[i] = _inst;
				//load items 
//				if(_inst != null) {
//					_inst.setItem(medicineDAO.findMedicineItem(_inst.getItemId()));
//				}
			}
			//任务数据
			Task[] tasks = null;
			try {
				tasks = new Task[ois.readInt()]; 
				for(int i=0;i<tasks.length;i++) {
					tasks[i] = (Task) ois.readObject();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			ois.close();
			System.out.println("loaded game data: "+new java.util.Date());
			
			Profile profile = new Profile();
			profile.setName(trimSuffix(file.getName()));
			profile.setFilename(file.getAbsolutePath());
			profile.setCreateDate(createDate);
			profile.setPlayerData(playerData);
			profile.setItems(items);
			profile.setSceneId(sceneId);
			profile.setTasks(tasks);
			return profile;
		} catch (Exception e) {
			throw new ProfileException("加载游戏存档时出现错误：name="+file.getName(), e);
		}
	}

	@Override
	public Profile newProfile(String name) {
		Profile profile = new Profile();
		profile.setName(name);
		profile.setCreateDate(new java.util.Date());
		return profile;
	}

	@Override
	public void removeProfile(Profile profile) throws ProfileException {
		if(profile == null) {
			throw new ProfileException("Profile 不能为空");
		}
		if(profile.getFilename() == null) {
			throw new ProfileException("profile.getFilename() 不能为空");
		}
		CacheManager.getInstance().deleteFile(profile.getFilename());
	}

	@Override
	public void saveProfile(Profile profile) {
		try {
			File file = getProfileFile(profile.getName(), true);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			//创建时间
			oos.writeObject(new java.util.Date());
			//场景
			oos.writeUTF(profile.getSceneId());
			//人物数据
			oos.writeObject(profile.getPlayerData());
			//物品数据
			ItemInstance[] items = profile.getItems();
			oos.writeInt(items.length);
			for (int i = 0; i < items.length; i++) {
				oos.writeObject(items[i]);
			}
			//任务数据
			Task[] tasks = profile.getTasks();
			oos.writeInt(tasks.length);
			for (int i = 0; i < tasks.length; i++) {
				oos.writeObject(tasks[i]);
			}
			oos.close();
			profile.setFilename(file.getAbsolutePath());
			System.out.println("游戏存档完毕: "+file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			System.out.println("游戏存档失败,找不到文件！"+profile.getName());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("游戏存档失败，IO错误！"+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("游戏存档失败！"+e.getMessage());
			e.printStackTrace();
		}
	}

	private File getProfileFile(String name, boolean create) {
		String path="save/"+name+".jxd";
		File file = CacheManager.getInstance().getFile(path);
		if((file == null || !file.exists() )&& create) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private String trimSuffix(String filename) {
		filename = filename.replace('\\', '/');
		int p1 = filename.indexOf("/");
		int p2 = filename.lastIndexOf(".");
		if(p1 < 0)p1 = 0;
		if(p2 < 0)p2 = filename.length();
		return filename.substring(p1, p2);
	}
}
