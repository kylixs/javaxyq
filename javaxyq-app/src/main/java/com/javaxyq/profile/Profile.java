/**
 * 
 */
package com.javaxyq.profile;

import java.util.Arrays;
import java.util.Date;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.SkillMain;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;

/**
 * 游戏存档
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class Profile {
	private String name;
	private String filename;
	private String sceneId;
	private Task[] tasks;
	private PlayerVO playerData;
	private ItemInstance[] items;
	private Date createDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public Task[] getTasks() {
		if(tasks == null) {
			tasks = new Task[0];
		}
		return tasks;
	}

	public void setTasks(Task[] tasks) {
		this.tasks = tasks;
	}

	public PlayerVO getPlayerData() {
		return playerData;
	}

	public void setPlayerData(PlayerVO playerData) {
		this.playerData = playerData;
	}

	public ItemInstance[] getItems() {
		if(items == null) {
			items = new ItemInstance[26];
		}
		return items;
	}

	public void setItems(ItemInstance[] items) {
		this.items = items;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	@Override
	public String toString() {
		return "Profile [name=" + name +
				", filename=" + filename +
				", sceneId=" + sceneId +
				", playerData=" + playerData +
				", items=" + Arrays.toString(items) +
				", tasks=" + Arrays.toString(tasks) +
				", createDate=" + createDate +
				"]";
	}
}
