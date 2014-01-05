/**
 * 
 */
package com.javaxyq.profile;

import java.util.Arrays;
import java.util.Date;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Task;

/**
 * ”Œœ∑¥Êµµ
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
			items = new ItemInstance[20];
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
		StringBuilder builder = new StringBuilder();
		builder.append("Profile [name=");
		builder.append(name);
		builder.append(", filename=");
		builder.append(filename);
		builder.append(", sceneId=");
		builder.append(sceneId);
		builder.append(", playerData=");
		builder.append(playerData);
		builder.append(", items=");
		builder.append(Arrays.toString(items));
		builder.append(", tasks=");
		builder.append(Arrays.toString(tasks));
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append("]");
		return builder.toString();
	}
}
