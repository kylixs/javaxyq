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

    Player createPlayer(PlayerVO playerData);

    void incExp(Player player, int exp);

    void incHp(Player player, int hp);

    void incMoney(Player player, int money);

    void incMp(Player player, int mp);

    /**
     * 增加某属性的值
     */
    void incProp(Player player, String prop, int val);

    /**
     * 获取玩家数据键值表
     */
    Map<String, Object> getProperties(Player player);

    /**
     * 查找NPC闲聊内容
     */
    String findChatText(String npcId);

    /**
     * 查找某类型的物品
     *
     * @param type 物品类型，参考ItemTypes
     */
    ItemInstance[] findItems(Player player, int type);

    List<SceneNpc> findNpcsBySceneId(int sceneId);

    Scene findScene(int sceneId);

    SceneNpc findSceneNpc(Integer id);

    SceneTeleporter findSceneTeleporter(Integer id);

    Skill findSkillByName(String name);

    List<SceneTeleporter> findTeleportersBySceneId(int sceneId);

    List<SkillMain> findMainSkill(String school);

    /**
     * 把物品放到人物背包中
     */
    boolean pickItem(Player player, ItemInstance item);

    /**
     * 叠加物品
     *
     * @param srcItem  源物品
     * @param destItem 目标物品
     * @return 叠加成功返回true
     */
    boolean overlayItems(ItemInstance srcItem, ItemInstance destItem);

    /**
     * 物品可以叠加的最大数量
     */
    int getOverlayAmount(Item item);

    boolean existItem(String name, int count);

    ItemInstance getItemAt(Player player, int index);

    ItemInstance createItem(String name);

    ItemInstance createItem(String string, int count);

    /**
     * 设置人物的道具
     *
     * @param index 道具位置序号，自上而下，自左至右排列 in 5*4 = [0,20)
     */
    void setItem(Player player, int index, ItemInstance item);

    void setItems(Player player, ItemInstance[] items);

    void setItemByName(Player player, int index, String itemName);

    void swapItem(Player player, int srcIndex, int destIndex);

    /**
     * 读取游戏人物道具列表
     */
    ItemInstance[] getItems(Player player);

    boolean removeItemFromPlayer(Player player, int index);

    void removeItemFromPlayer(Player player, ItemInstance item);

    /**
     * 初始化npc属性（怪物）
     */
    void initNPC(Player player);

    void initPlayerData(PlayerVO vo);

    PlayerVO createPlayerData(String character);

    /**
     * 计算怪物的属性值
     */
    void reCalcElfProps(PlayerVO vo);

    /**
     * 计算人物的属性值
     */
    void reCalcProperties(PlayerVO vo);

    Player createNPC(SceneNpc _npc);

    Player createElf(String type, String name, int level);

    String getBasicSkillName(String school);

    long getLevelExp(int level);

    long getMSkillsLevelExp(int level);

    long getMSkillsLevelSpend(int level);
}