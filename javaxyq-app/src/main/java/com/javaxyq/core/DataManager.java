package com.javaxyq.core;

import java.util.List;
import java.util.Map;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.Scene;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.data.SkillMain;
import com.javaxyq.model.Item;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.model.Skill;
import com.javaxyq.widget.Player;

public interface DataManager {

	void addExp(Player player, int exp);

	void addHp(Player player, int hp);

	/**
	 * 把物品放到人物背包中
	 * @return 给予成功返回true
	 */
	boolean addItemToPlayerBag(Player player, ItemInstance item);

	void addMoney(Player player, int money);

	void addMp(Player player, int mp);

	/**
	 * 增加某属性的值
	 */
	void addProp(Player player, String prop, int val);

	ItemInstance createItem(String name);
	ItemInstance createItem(String string, int amount);

	boolean existItem(String name, int amount);

	/**
	 * 查找NPC闲聊内容
	 */
	String findChat(String npcId);

	/**
	 * 查找某类型的物品
	 * @param player
	 * @param type 物品类型，参考ItemTypes
	 */
	ItemInstance[] findItems(Player player, int type);

	List<SceneNpc> findNpcsBySceneId(int sceneId);

	Scene findScene(int sceneId);

	SceneNpc findSceneNpc(Integer id);

	SceneTeleporter findSceneTeleporter(Integer id);
	
	Skill findSkillByName(String name);

	List<SceneTeleporter> findTeleportersBySceneId(int sceneId);

	ItemInstance getItemAt(Player player, int index);
	
	List<SkillMain> findMainSkill(String school);
	

	/**
	 * 物品可以叠加的最大数量
	 */
	int getOverlayAmount(Item item);

	/**
	 * 读取游戏人物道具列表
	 */
	ItemInstance[] getItems(Player player);

	/**
	 * 初始化npc属性（怪物）
	 */
	void initNPC(Player player);

	void initPlayerData(PlayerVO vo);

	PlayerVO createPlayerData(String character);
	
	/**
	 * 叠加物品
	 * @param srcItem 源物品
	 * @param destItem 目标物品
	 * @return 叠加成功返回true
	 */
	boolean overlayItems(ItemInstance srcItem, ItemInstance destItem);

	/**
	 * 计算怪物的属性值
	 */
	void recalcElfProps(PlayerVO vo);

	/**
	 * 计算人物的属性值
	 */
	void recalcProperties(PlayerVO vo);

	boolean removeItemFromPlayer(Player player, int index);

	void removeItemFromPlayer(Player player, ItemInstance item);

	/**
	 * 设置人物的道具
	 * @param index 道具位置序号，自上而下，自左至右排列 in 5*4 = [0,20) 
	 */
	void setItem(Player player, int index, ItemInstance item);

	void setItemByName(Player player, int index, String itemName);

	void swapItem(Player player, int srcIndex, int destIndex);

	Player createNPC(SceneNpc _npc);
	
	Player createElf(String type,String name,int level);
	
	/**
	 * 获取玩家数据键值表
	 */
	Map<String, Object> getProperties(Player player);
	
	String getBasicSkillName(String school);
	
	long getLevelExp(int level);
	
	long getMSkillsLevelExp(int level);
	
	long getMSkillsLevelSpend(int level);

	Player createPlayer(PlayerVO playerData);

	void setItems(Player player, ItemInstance[] items);
}