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

	public void addExp(Player player, int exp);

	public void addHp(Player player, int hp);

	/**
	 * 把物品放到人物背包中
	 * @return 给予成功返回true
	 */
	public boolean addItemToPlayerBag(Player player, ItemInstance item);

	public void addMoney(Player player, int money);

	public void addMp(Player player, int mp);

	/**
	 * 增加某属性的值
	 * @param player
	 * @param prop
	 * @param val
	 */
	public void addProp(Player player, String prop, int val);

	public ItemInstance createItem(String name);
	public ItemInstance createItem(String string, int amount);

	public boolean existItem(String name, int amount);

	/**
	 * 查找NPC闲聊内容
	 * @param npcId
	 * @return
	 */
	public String findChat(String npcId);

	/**
	 * 查找某类型的物品
	 * @param player
	 * @param type 物品类型，参考ItemTypes
	 * @return
	 */
	public ItemInstance[] findItems(Player player, int type);

	public List<SceneNpc> findNpcsBySceneId(int sceneId);

	/**
	 * @return 
	 * 
	 */
	public Scene findScene(int sceneId);

	public SceneNpc findSceneNpc(Integer id);

	public SceneTeleporter findSceneTeleporter(Integer id);
	
	public Skill findSkillByName(String name);

	public List<SceneTeleporter> findTeleportersBySceneId(int sceneId);

	public ItemInstance getItemAt(Player player, int index);
	
	public List<SkillMain> findMainSkill(String school);
	

	/**
	 * 物品可以叠加的最大数量
	 * @param item
	 * @return
	 */
	public int getOverlayAmount(Item item);

	/**
	 * 读取游戏人物道具列表
	 */
	public ItemInstance[] getItems(Player player);

	/**
	 * 初始化npc属性（怪物）
	 * @param vo
	 */
	public void initNPC(Player player);

	public void initPlayerData(PlayerVO vo);

	public PlayerVO createPlayerData(String character);
	
	/**
	 * 叠加物品
	 * @param srcItem 源物品
	 * @param destItem 目标物品
	 * @return 叠加成功返回true
	 */
	public boolean overlayItems(ItemInstance srcItem, ItemInstance destItem);

	/**
	 * 计算怪物的属性值
	 * 
	 * @param vo
	 */
	public void recalcElfProps(PlayerVO vo);

	/**
	 * 计算人物的属性值
	 * @param vo
	 */
	public void recalcProperties(PlayerVO vo);

	public boolean removeItemFromPlayer(Player player, int index);

	public void removeItemFromPlayer(Player player, ItemInstance item);

	/**
	 * 设置人物的道具
	 * @param index 道具位置序号，自上而下，自左至右排列 in 5*4 = [0,20) 
	 */
	public void setItem(Player player, int index, ItemInstance item);

	public void setItemByName(Player player, int index, String itemName);

	public void swapItem(Player player, int srcIndex, int destIndex);

	public Player createNPC(SceneNpc _npc);
	
	Player createElf(String type,String name,int level);
	
	/**
	 * 获取玩家数据键值表
	 * @param player
	 * @return
	 */
	public Map<String, Object> getProperties(Player player);
	
	public String getBasicSkillName(String school);
	
	public long getLevelExp(int level);
	
	public long getMSkillsLevelExp(int level);
	
	public long getMSkillsLevelSpend(int level);

	public Player createPlayer(PlayerVO playerData);

	public void setItems(Player player, ItemInstance[] items);


}