package com.javaxyq.core;

import com.javaxyq.config.PlayerConfig;
import com.javaxyq.data.*;
import com.javaxyq.data.impl.*;
import com.javaxyq.io.CacheManager;
import com.javaxyq.model.*;
import com.javaxyq.task.TaskManager;
import com.javaxyq.util.StringUtils;
import com.javaxyq.util.UIUtils;
import com.javaxyq.widget.Player;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据服务类
 *
 * @author 龚德伟
 * @history 2008-6-8 龚德伟 新建
 */
@Slf4j
public class DataStore implements DataManager {

    private static int elfId;
    /**
     * 人物升级经验表
     */
    private static final int[] levelExpTable = {
            40, 110, 237, 450, 779, 1252, 1898, 2745, 3822, 5159, 6784, 8726, 11013, 13674, 16739, 20236, 24194, 28641, 330606, 39119,
            45208, 51902, 59229, 67218, 75899, 85300, 95450, 106377, 118110, 130679, 144112, 158438, 173685, 189882, 207059,
            225244, 244466, 264753, 286134, 308639, 332296, 357134, 383181, 410466, 439019, 468868, 500042, 532569, 566478,
            601799, 638560, 676790, 716517, 757770, 800579, 844972, 890978, 938625, 987942, 1038959, 1091704, 1146206, 1202493,
            1260594, 1320539, 1382356, 1446074, 1511721, 1579326, 1648919, 1720528, 1794182, 1869909, 1947738, 2027699,
            2109820, 2194130, 2280657, 2369430, 2460479, 2553832, 2649518, 2747565, 2848002, 2950859, 3056164, 3163946,
            3274233, 3387054, 3502439, 3620416, 3741014, 3864261, 3990186, 4118819, 4250188, 4384322, 4521249, 4660998, 4803599
    };
    private static final int[] mSkillsLevelExpTable = {
            16, 32, 52, 75, 103, 136, 179, 231, 295, 372, 466, 578, 711, 867, 1049, 1260, 1503, 1780, 2096, 2452, 2854, 3304, 3807, 4364, 4983, 5664,
            6415, 7238, 8138, 9120, 10188, 11347, 12602, 13959, 15423, 16998, 18692, 20508, 22452, 24532, 26753, 29121, 31642, 34323, 37169, 40188,
            43388, 46773, 50352, 54132, 58120, 62324, 66750, 71407, 76303, 81444, 86840, 92500, 98430, 104640, 111136, 117931, 125031, 132444,
            140183, 148253, 156666, 165430, 174556, 184052, 193930, 204198, 214868, 225948, 237449, 249383, 261760, 274589, 287884, 301652,
            315908, 330662, 345924, 361708, 378023, 394882, 412297, 430280, 448844, 468000, 487760, 508137, 529145, 550796, 573103, 596078,
            619735, 644088, 669149, 694932, 721452, 748722, 776755, 805566, 835169, 865579, 896809, 928876, 961792, 995572, 1030234, 1065190,
            1102256, 1139649, 1177983, 1217273, 1256104, 1298787, 1341043, 1384320, 1428632, 1473999, 1520435, 1567957, 1616583, 1666328,
            1717211, 1769248, 1822456, 1876852, 1932456, 1989284, 2047353, 2106682, 2167289, 2229192, 2292410, 2356960, 2422861, 2490132,
            2558792, 2628860, 2700356, 2773296, 2847703, 2923593, 3000989, 3079908, 3160372, 3242400, 6652022, 6822452, 6996132, 7173104,
            7353406, 11305620, 11586254, 11872072, 12163140, 12459518, 15033471, 15315219, 15600468, 15889236, 16181550, 16477425, 16776885,
            17079954, 17386650, 17697000, 24014692, 24438308, 24866880, 25300432, 25739000, 32728255, 33289095, 33856310, 34492930, 40842000
    };
    private static final int[] mSkillsLevelSpendTable = {
            6, 12, 19, 28, 38, 51, 67, 86, 110, 139, 174, 216, 266, 325, 393, 472, 563, 667, 786, 919, 1070, 1238, 1426, 1636, 1868, 2124, 2404, 2714, 3050,
            3420, 3820, 4255, 4725, 5234, 5783, 6374, 7009, 7690, 8419, 9199, 10032, 10920, 11865, 12871, 13938, 15070, 16270, 17540, 18882, 20299,
            21795, 23371, 25031, 26777, 28613, 30541, 32565, 34687, 36911, 39240, 41676, 44224, 46886, 49666, 52568, 55595, 58749, 62036, 65458,
            69019, 72723, 76574, 80575, 84730, 89043, 93518, 98160, 102971, 107956, 113119, 118465, 123998, 129721, 135640, 141758, 148080, 154611,
            161355, 168316, 175500, 182910, 190551, 198429, 206548, 214913, 223529, 232400, 241533, 250931, 260599, 270544, 280770, 291283, 302087,
            313188, 324592, 336303, 348328, 360672, 373339, 386337, 399671, 413346, 427368, 441743, 456477, 471576, 487045, 502891, 519120, 535737,
            552749, 570163, 587984, 606218, 624873, 643954, 663468, 683421, 703819, 724671, 745981, 767757, 790005, 812733, 835947, 859653, 883860,
            908573, 933799, 959547, 985822, 1012633, 1039986, 1067888, 1096347, 1125371, 1154965, 1185139, 1215900, 2494508, 2558419, 2623549,
            2689914, 2757527, 4239607, 4344845, 4452027, 4561177, 4672319, 4510041, 4594563, 4680138, 4766769, 4854465, 4943226, 5033064, 5123985,
            5215995, 5309100, 7204407, 7331490, 7460064, 7590129, 7721700, 9818475, 9986727, 10156893, 10328979, 12252600
    };

    public static String[] 门派列表 = {
            "大唐官府", "方寸山", "化生寺", "女儿村",
            "阴曹地府", "魔王寨", "狮驼岭", "盘丝洞",
            "天宫", "龙宫", "五庄观", "普陀山"
    };
    public static String[] basic_skill = {
            "为官之道", "黄庭经", "小乘佛法", "毒经",
            "灵通术", "牛逼神功", "魔兽神功", "蛛丝阵法",
            "天罡气", "九龙诀", "周易学", "灵性"
    };

    public static final String[] 人族门派 = {"大唐官府", "方寸山", "化生寺", "女儿村"};

    public static final String[] 魔族门派 = {"阴曹地府", "魔王寨", "狮驼岭", "盘丝洞 "};

    public static final String[] 仙族门派 = {"天宫", "龙宫", "五庄观", "普陀山"};

    /**
     * 判断数组array是否包含值value
     */
    private static boolean inArray(String[] array, String value) {
        for (String s : array) {
            if (s.equals(value)) return true;
        }
        return false;
    }

    private static Properties parseConfig(String str) {
        Properties configs = new Properties();
        try {
            if (str != null) {
                String[] entries = str.split(";");
                for (String entry : entries) {
                    String[] kv = entry.split("=");
                    configs.setProperty(kv[0], kv[1]);
                }
            }
        } catch (Exception e) {
            log.error("解析Config失败！{}", str, e);

        }
        return configs;
    }

    private Context context;

    private Map<String, Integer> devilData = new HashMap<String, Integer>();

    /**
     * 成长率表
     */
    private Map<String, Double> growthRateTable = new HashMap<String, Double>();

    private Map<String, Integer> humanData = new HashMap<String, Integer>();

    private Map<String, Integer> immortalData = new HashMap<String, Integer>();

    private Map<Player, ItemInstance[]> itemsMap = new HashMap<Player, ItemInstance[]>();

    private List<BaseItemDAO> itemDAOs = new ArrayList<BaseItemDAO>();
    private String lastChat = "";
    private MedicineItemDAOImpl medicineDAO;
    private Random rand = new Random();
    private SceneDAO sceneDAO;
    private SceneNpcDAO sceneNpcDAO;
    private SceneTeleporterDAO sceneTeleporterDAO;
    private SkillMainDAOImpl skillMain;
    private SkillMagicDAOImpl skillMagic;
    private String[] 魔族 = {"0005", "0006", "0007", "0008"};
    private WeaponItemDAOImpl weaponDAO;

    /**
     * (一)各种族初始资料
     * 1)魔族
     * 体12-魔力11-力量11-耐力8-敏8
     * 血172-魔法值107-命中55-伤害43-防御11-速度8-躲闪18-灵力17
     * 2)人族
     * 体10-魔力10-力量10-耐力10-敏10
     * 血150-魔法值110-命中50-伤害41-防御15-速度10-躲闪30-灵力16
     * 3)仙族
     * 体12-魔力5-力量11-耐力12-敏10
     * 血154-魔法值97-命中48-伤害46-防御19-速度10-躲闪20-灵力13
     */
    private String[] 人族 = {"0001", "0002", "0003", "0004"};
    private String[] 仙族 = {"0009", "0010", "0011", "0012"};

    public DataStore(Context context) {
        this.context = context;
        init();
        //人族初始属性
        humanData.put("体质", 10);
        humanData.put("魔力", 10);
        humanData.put("力量", 10);
        humanData.put("durability", 10);
        humanData.put("敏捷", 10);

        humanData.put("命中", 50);
        humanData.put("伤害", 41);
        humanData.put("防御", 15);
        humanData.put("速度", 10);
        humanData.put("躲避", 20);
        humanData.put("灵力", 16);

        humanData.put("hp", 150);
        humanData.put("mp", 110);

        //魔族初始属性
        devilData.put("体质", 12);
        devilData.put("魔力", 11);
        devilData.put("力量", 11);
        devilData.put("durability", 8);
        devilData.put("敏捷", 8);

        devilData.put("命中", 55);
        devilData.put("伤害", 43);
        devilData.put("防御", 11);
        devilData.put("速度", 8);
        devilData.put("躲避", 18);
        devilData.put("灵力", 17);

        devilData.put("hp", 172);
        devilData.put("mp", 107);

        //仙族初始属性
        immortalData.put("体质", 12);
        immortalData.put("魔力", 5);
        immortalData.put("力量", 11);
        immortalData.put("durability", 12);
        immortalData.put("敏捷", 10);

        immortalData.put("命中", 48);
        immortalData.put("伤害", 46);
        immortalData.put("防御", 19);
        immortalData.put("速度", 10);
        immortalData.put("躲避", 20);
        immortalData.put("灵力", 13);

        immortalData.put("hp", 154);
        immortalData.put("mp", 97);

        //成长率
        growthRateTable.put("2009", 1.5);
        growthRateTable.put("2010", 0.9);
        growthRateTable.put("2011", 1.0);
        growthRateTable.put("2012", 1.2);
        growthRateTable.put("2036", 0.8);
        growthRateTable.put("2037", 0.8);
    }

    public void incExp(Player player, int exp) {
        PlayerVO vo = player.getData();
        vo.exp += exp;
    }

    public void incHp(Player player, int hp) {
        PlayerVO vo = player.getData();
        vo.hp = Math.min(vo.hp + hp, vo.maxHp);
        if (vo.hp < 0) {
            vo.hp = 0;
        }
    }


    /**
     * 给以人物某物品
     *
     * @return 给予成功返回true
     */
    public boolean pickItem(Player player, ItemInstance item) {
        //TODO 获得物品
        synchronized (player) {
            ItemInstance[] items = getItems(player);
            int index = 6;//背包从6开始，前面是装备栏
            for (; index < items.length; index++) {
                if (items[index] == null) {
                    items[index] = item;
                    return true;
                } else if (overlayItems(item, items[index])) {//可以叠加
                    if (item.getCount() == 0) return true;//叠加完毕
                }
            }
            return false;
        }
    }

    public void incMoney(Player player, int money) {
        PlayerVO vo = player.getData();
        vo.money += money;
    }

    public void incMp(Player player, int mp) {
        PlayerVO vo = player.getData();
        vo.mp = Math.min(vo.mp + mp, vo.maxMp);
        if (vo.mp < 0) {
            vo.mp = 0;
        }
    }

    /**
     * 增加某属性的值
     */
    public void incProp(Player player, String prop, int val) {
        PlayerVO vo = player.getData();
        //可能改属性有上限值
        String maxProp = "max" + prop;
        //if()
    }

    /**
     * 创建普通的小怪（场景练级）
     */
    public Player createElf(String type, String name, int level) {
        Player player = createPlayer(type, (int[]) null);
        player.setName(name);
        //初始化怪物属性
        PlayerVO data = new PlayerVO("elf-" + (++elfId), name, type);
        data.level = level;
        //初始化场景怪物的属性
        initElf(data);
        player.setData(data);
        //player.setNameForeground(GameMain.TEXT_NAME_NPC_COLOR);
        log.info("create elf: " + data);
        return player;
    }

    public ItemInstance createItem(String name) {
        return this.createItem(name, 1);
    }

    @Override
    public ItemInstance createItem(String name, int count) {
        if (name == null || name.isEmpty()) return null;
        name = name.trim();
        Item item = this.findItemByName(name);
        if (item != null) {
            return new ItemInstance(item, count);
        }
        log.error("createItem failed: " + name);
        return null;
    }

    public Item findItemByName(String name) {
        for (BaseItemDAO itemDao : itemDAOs) {
            try {
                Item item = itemDao.findItemByName(name);
                if (item != null) {
                    return item;
                }
            } catch (SQLException e) {
                log.error("", e);
            }
        }
        return null;
    }

    /**
     * 创建NPC实例
     */
    public Player createNPC(PlayerConfig cfg) {
        Player p = this.createPlayer(cfg);
        p.setNameForeground(UIUtils.TEXT_NAME_NPC_COLOR);
        return p;
    }

    /**
     * 创建NPC实例
     */
    public Player createNPC(SceneNpc _npc) {
        Player player = new Player(String.valueOf(_npc.getId()), _npc.getName(), _npc.getCharacterId());
        player.setSceneLocation(_npc.getSceneX(), _npc.getSceneY());
        //String strColorations = cfg.getColorations();
        //		if (strColorations != null) {// 染色
        //			String[] colors = strColorations.split(",");
        //			int[] colorations = new int[colors.length];
        //			for (int i = 0; i < colors.length; i++) {
        //				colorations[i] = colors[i].charAt(0) - '0';
        //			}
        //			player.setColorations(colorations, false);//在setState之前
        //		}
        Properties configs = parseConfig(_npc.getConfig());
        player.setState(configs.getProperty("state", "stand"));
        player.setDirection(Integer.parseInt(configs.getProperty("direction", "0")));
        //String mAction = cfg.getMovement();
        //		if (mAction != null) {
        //			MovementManager.put(player, mAction, cfg.getPeriod());
        //		}

        player.setNameForeground(UIUtils.TEXT_NAME_NPC_COLOR);
        return player;
    }

    //FIXME 重复创建同一个角色时，动画更新有问题（共享了同一个Sprite！）
    public Player createPlayer(PlayerConfig cfg) {
        Player player = new Player(cfg.getId(), cfg.getName(), cfg.getCharacter());
        player.setSceneLocation(cfg.getX(), cfg.getY());
        String strColorations = cfg.getColorations();
        if (strColorations != null) {// 染色
            String[] colors = strColorations.split(",");
            int[] colorations = new int[colors.length];
            for (int i = 0; i < colors.length; i++) {
                colorations[i] = colors[i].charAt(0) - '0';
            }
            player.setColorations(colorations, false);//在setState之前
        }
        player.setState(cfg.getState());
        player.setDirection(cfg.getDirection());
        String mAction = cfg.getMovement();
//		if (mAction != null) {
//			MovementManager.put(player, mAction, cfg.getPeriod());
//		}
        return player;
    }

    public Player createPlayer(PlayerVO data) {
        Player player = new Player(data.id, data.name, data.character);
        player.setData(data);
        return player;
    }

    public Player createPlayer(String character, int[] colorations) {
        Player player = new Player(null, "未命名", character);
        player.setColorations(colorations, true);
        player.setState(Player.STATE_STAND);
        player.setDirection(0);
        return player;
    }

    public Player createPlayer(String character, Map<String, Object> cfg) {
        Player player = createPlayer(character, (int[]) cfg.get("colorations"));
        PlayerVO data = new PlayerVO((String) cfg.get("id"), (String) cfg.get("name"), character);
        try {
            BeanUtils.populate(player, cfg);
            BeanUtils.populate(data, cfg);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("", e);
        }
        //FIXME 初始化属性
        //data.level = cfg.level;
        this.initPlayerData(data);
        this.reCalcProperties(data);
        log.info("create player: " + data);
        player.setData(data);
        return player;
    }

    public boolean existItem(String name, int count) {
        return false;
    }

    /**
     * 查找NPC闲聊内容
     */
    public String findChatText(String npcId) {
        File file = CacheManager.getInstance().getFile("chat/" + npcId + ".txt");
        if (file != null && file.exists()) {
            List<String> chats = new ArrayList<String>();
            try {
                String str = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while ((str = br.readLine()) != null) {
                    if (StringUtils.isNotBlank(str) && !"P N".equals(str.trim())) {
                        chats.add(str);
                    }
                }
            } catch (IOException e) {
                log.error("", e);
            }
            int index = rand.nextInt(chats.size());
            if (lastChat != null && lastChat.startsWith(npcId)) {
                int lastindex = Integer.parseInt(lastChat.substring(npcId.length() + 1));
                index = (lastindex + 1) % chats.size();
            }
            if (chats.size() > index) {
                String chat = chats.get(index);
                lastChat = npcId + "_" + index;
                return chat;
            }
        }
        return null;
    }

    /**
     * 查找某类型的物品
     */
    public ItemInstance[] findItems(Player player, int type) {
        ItemInstance[] allItems = getItems(player);
        List<ItemInstance> results = new ArrayList<>();
        for (ItemInstance item : allItems) {
            if (item != null && ItemTypes.isType(item.getItem(), type)) {
                results.add(item);
            }
        }
        return results.toArray(new ItemInstance[0]);
    }

    public List<SceneNpc> findNpcsBySceneId(int sceneId) {
        try {
            return sceneNpcDAO.findNpcsBySceneId(sceneId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Scene findScene(int sceneId) {
        try {
            return sceneDAO.findScene(sceneId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SceneNpc findSceneNpc(Integer id) {
        try {
            return sceneNpcDAO.findSceneNpc(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SceneTeleporter findSceneTeleporter(Integer id) {
        try {
            return sceneTeleporterDAO.findSceneTeleporter(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<SkillMain> findMainSkill(String school) {
        try {
            return skillMain.findSkillBySchool(school);
        } catch (SQLException e) {
            log.error("", e);
        }
        return null;
    }

    public Skill findSkillByName(String name) {
        try {
            return skillMagic.findSkillByName(name);
        } catch (SQLException e) {
            log.error("", e);
        }
        return null;
    }

    public List<SceneTeleporter> findTeleportersBySceneId(int sceneId) {
        try {
            return sceneTeleporterDAO.findTeleportersBySceneId(sceneId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemInstance getItemAt(Player player, int index) {
        ItemInstance[] list = itemsMap.get(player);
        return list != null ? list[index] : null;
    }

    /**
     * 读取游戏人物道具列表
     */
    public ItemInstance[] getItems(Player player) {
        return itemsMap.computeIfAbsent(player, k -> new ItemInstance[26]);
    }

    public String getBasicSkillName(String school) {
        for (int i = 0; i < 门派列表.length; i++) {
            if (StringUtils.equals(门派列表[i], school)) {
                return basic_skill[i];
            }
        }
        return null;
    }

    public long getLevelExp(int level) {

        return levelExpTable[level];
    }

    public long getMSkillsLevelExp(int level) {

        return mSkillsLevelExpTable[level];

    }

    public long getMSkillsLevelSpend(int level) {

        return mSkillsLevelSpendTable[level];

    }

    /**
     * 物品可以叠加的最大数量
     */
    public int getOverlayAmount(Item item) {
        int amount = 1;
        if (item instanceof MedicineItem) {
            MedicineItem mitem = (MedicineItem) item;
            switch (mitem.getLevel()) {
                case 1:
                    amount = 99;
                    break;
                case 2:
                    amount = 30;
                    break;
                case 3:
                    amount = 10;
                    break;

                default:
                    break;
            }
        }
        return amount;
    }


    /**
     * 获取玩家数据键值表
     */
    public Map<String, Object> getProperties(Player player) {
        try {
            Map<String, Object> props = ((Map<String, Object>) PropertyUtils.describe(player.getData()));
            props.put("levelExp", getLevelExp(player.getData().level));
            return props;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化数据中心
     */
    private void init() {
        String cacheBase = CacheManager.getInstance().getCacheBase();
        System.setProperty("derby.system.home", cacheBase);
        log.info("initializing datastore, dbDir: " + cacheBase);
        weaponDAO = new WeaponItemDAOImpl();
        medicineDAO = new MedicineItemDAOImpl();
        sceneDAO = new SceneDAOImpl();
        sceneNpcDAO = new SceneNpcDAOImpl();
        sceneTeleporterDAO = new SceneTeleporterDAOImpl();
        skillMain = new SkillMainDAOImpl();
        skillMagic = new SkillMagicDAOImpl();


        itemDAOs.add(weaponDAO);
        itemDAOs.add(medicineDAO);
		
		/*try {
			Skill skill = skillMain.findSkillByName("五行学说");Action:
			System.out.println("skill's description is:"+skill.getDescription());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        log.info("initialized datastore");
    }

    /**
     * 初始化数据
     */
    private void initData() {
        log.info("creating game data");
        int[] colorations = new int[3];
        colorations[0] = 2;
        colorations[1] = 4;
        colorations[2] = 3;
        Map<String, Object> data = new HashMap<>();
        data.put("name", "逍遥葫芦");
        data.put("level", 5);
        data.put("school", "五庄观");
        data.put("direction", 0);
        data.put("state", "stand");
        data.put("colorations", colorations);
        Player p = this.createPlayer("0010", data);
        context.setPlayer(p);
        p.setSceneLocation(52, 32);
        context.setScene("1146");//五庄观

        ItemInstance item = createItem("四叶花");
        item.setCount(99);
        pickItem(p, item);
        item = createItem("佛手");
        item.setCount(99);
        pickItem(p, item);
        item = createItem("血色茶花");
        item.setCount(30);
        item = createItem("九香虫");
        pickItem(p, item);
        int money = 50000;
        incMoney(p, money);
        log.info("create game data");
    }

    private void initElf(PlayerVO elf) {
        //TODO 完善属性点数分配
        Random random = new Random();
        //初始至少有5点，每等级至少1点
        int base = elf.level * 1 + 5;
        //野生怪物待分配点数：25（初始50点的一半）+ 等级*3（每等级3点）
        int toAssign = 25 + elf.level * 3;
        int[] assigns = new int[5];
        //随机分配点数
        for (int i = 0; i < toAssign; i++) {
            assigns[random.nextInt(5)]++;
        }
        elf.physique = base + assigns[0];
        elf.magic = base + assigns[1];
        elf.strength = base + assigns[2];
        elf.defense = base + assigns[3];
        elf.agility = base + assigns[4];
        //随机门派
        elf.school = 门派列表[random.nextInt(12)];
        //重新计算属性
        this.reCalcElfProps(elf);
    }

    /**
     * 初始化npc属性（怪物）
     */
    public void initNPC(Player player) {
        //TODO
    }

    public void initPlayerData(PlayerVO vo) {
        try {
            if (inArray(人族, vo.character)) {
                BeanUtils.populate(vo, humanData);
            } else if (inArray(魔族, vo.character)) {
                BeanUtils.populate(vo, devilData);
            } else if (inArray(仙族, vo.character)) {
                BeanUtils.populate(vo, immortalData);
            }
            vo.maxHp = vo.hp;
            vo.maxMp = vo.mp;
            vo.tmpMaxHp = vo.maxHp;
            vo.potentiality = 5 + 5 * vo.level;
            vo.physique += vo.level;
            vo.magic += vo.level;
            vo.strength += vo.level;
            vo.durability += vo.level;
            vo.agility += vo.level;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public PlayerVO createPlayerData(String character) {
        try {
            PlayerVO vo = new PlayerVO();
            if (inArray(人族, character)) {
                BeanUtils.populate(vo, humanData);
            } else if (inArray(魔族, character)) {
                BeanUtils.populate(vo, devilData);
            } else if (inArray(仙族, character)) {
                BeanUtils.populate(vo, immortalData);
            }
            vo.character = character;
            vo.createDate = new java.util.Date();
            vo.maxHp = vo.hp;
            vo.maxMp = vo.mp;
            vo.tmpMaxHp = vo.maxHp;
            vo.potentiality = 5 + 5 * vo.level;
            vo.physique += vo.level;
            vo.magic += vo.level;
            vo.strength += vo.level;
            vo.durability += vo.level;
            vo.agility += vo.level;
            return vo;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载默认存档
     */
    public void loadData() {
        log.info("loading game data: " + new java.util.Date());
        File file = CacheManager.getInstance().getFile("save/0.jxd");
        if (file == null || !file.exists() || file.length() == 0) {
            initData();
            return;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            log.info("读取游戏存档（创建于{}）...", ois.readObject());
            //场景ID
            String sceneId = ois.readUTF();
            //人物数据
            PlayerVO playerData = (PlayerVO) ois.readObject();
            //物品数据
            ItemInstance[] items = new ItemInstance[ois.readInt()];
            for (int i = 0; i < items.length; i++) {
                ItemInstance _inst = (ItemInstance) ois.readObject();
                items[i] = _inst;
                //read item
                if (_inst != null) {
                    _inst.setItem((MedicineItem) medicineDAO.findItem(_inst.getItemId()));
                }
            }
            //任务数据
            Task[] tasks = new Task[ois.readInt()];
            for (int i = 0; i < tasks.length; i++) {
                tasks[i] = (Task) ois.readObject();
            }
            ois.close();

            //初始化
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("name", playerData.name);
            data.put("level", playerData.level);
            data.put("school", playerData.school);
            data.put("direction", playerData.direction);
            data.put("state", playerData.state);
            data.put("colorations", playerData.colorations);
            data.put("sceneLocation", playerData.sceneLocation);
            Player player = this.createPlayer(playerData.character, data);
            player.setData(playerData);
            context.setPlayer(player);
            context.setScene(sceneId);
            for (int i = 0; i < items.length; i++) {
                setItem(player, i, items[i]);
            }
            TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
            for (Task task : tasks) {
                taskManager.add(task);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        log.info("loaded game data");
    }

    /**
     * 叠加物品
     *
     * @param srcItem  源物品
     * @param destItem 目标物品
     * @return 叠加成功返回true
     */
    public boolean overlayItems(ItemInstance srcItem, ItemInstance destItem) {
        if (srcItem.equals(destItem)) {
            int maxAmount = getOverlayAmount(srcItem.getItem());
            if (maxAmount > destItem.getCount()) {
                int total = srcItem.getCount() + destItem.getCount();
                destItem.setCount(Math.min(total, maxAmount));
                srcItem.setCount(total - destItem.getCount());
                return true;
            }
        }
        return false;
    }

    /**
     * 计算怪物的属性值
     *
     * @param vo
     */
    public void reCalcElfProps(PlayerVO vo) {
        //TODO 完善成长率
        Double rate = growthRateTable.get(vo.character);
        if (rate == null) {
            rate = 1.0;
        }
        int maxhp0 = vo.maxHp;
        int maxmp0 = vo.maxMp;
        vo.maxHp = vo.physique * 5 + 100;
        vo.maxMp = vo.magic * 3 + 80;
        vo.hitrate = (int) (rate * (vo.strength * 2 + 30));
        vo.harm = (int) (rate * (vo.strength * 0.7 + 34));
        vo.defense = (int) (rate * (vo.durability * 1.5));
        vo.speed = (int) (0.8 * rate * (vo.physique * 0.1 + vo.durability * 0.1 + vo.strength * 0.1 + vo.agility * 0.7 + vo.magic * 0));
        vo.wakan = (int) (rate * (vo.physique * 0.3 + vo.magic * 0.7 + vo.durability * 0.2 + vo.strength * 0.4 + vo.agility * 0));
        vo.shun = (int) (rate * (vo.agility * 1 + 10));

    }

    /**
     * 计算人物的属性值
     *
     * @param vo
     */
    public void reCalcProperties(PlayerVO vo) {
        String[] attrs = {"hitrate", "speed", "wakan", "shun", "harm", "defense", "stamina", "energy"};
        try {
            for (String attr : attrs) {
                Object value = PlayerPropertyCalculator.invokeMethod("calc_" + attr, vo);
                BeanUtils.copyProperty(vo, attr, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //,"气血","魔法"
        int maxHp0 = vo.maxHp;
        int maxMp0 = vo.maxMp;
        vo.maxHp = vo.tmpMaxHp = PlayerPropertyCalculator.calc_气血(vo);
        vo.maxMp = PlayerPropertyCalculator.calc_魔法(vo);
        vo.hp += vo.maxHp - maxHp0;
        vo.mp += vo.maxMp - maxMp0;
    }

    public boolean removeItemFromPlayer(Player player, int index) {
        ItemInstance[] items = getItems(player);
        if (items[index] != null) {
            log.info("remove item: " + items[index]);
            items[index] = null;
            return true;
        }
        return false;
    }

    public void removeItemFromPlayer(Player player, ItemInstance item) {
        if (item == null) return;
        ItemInstance[] items = getItems(player);
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                items[i] = null;
                break;
            }
        }
        log.info("remove item: " + item);
    }

    /**
     * 保存游戏数据到存档
     */
    public void saveData() {
        try {
            File file = CacheManager.getInstance().createFile("save/tmp.jxd");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            //创建时间
            oos.writeObject(new java.util.Date());
            //场景
            oos.writeUTF(context.getScene());

            //人物数据
            Player player = context.getPlayer();
            PlayerVO playerData = player.getData();
            playerData.state = "stand";
            playerData.direction = player.getDirection();
            playerData.colorations = player.getColorations();
            playerData.sceneLocation = player.getSceneLocation();
            oos.writeObject(playerData);

            //物品数据
            ItemInstance[] items = getItems(player);
            oos.writeInt(items.length);
            for (ItemInstance item : items) {
                oos.writeObject(item);
            }

            //任务数据
            TaskManager taskManager = ApplicationHelper.getApplication().getTaskManager();
            List<Task> tasks = taskManager.getTaskList();
            oos.writeInt(tasks.size());
            for (Task task : tasks) {
                oos.writeObject(task);
            }
            oos.close();

            //替换默认存档
            File defaultfile = CacheManager.getInstance().createFile("save/0.jxd");
//			if(defaultfile!=null && defaultfile.exists()) {
//				GameMain.deleteFile("save/1.jxd");
//				defaultfile.renameTo(new File(defaultfile.getAbsolutePath().replaceFirst("0\\.jxd", "1.jxd")));
//			}
            if (defaultfile != null && defaultfile.exists()) {
                defaultfile.delete();
            }
            file.renameTo(defaultfile);
            log.info("游戏存档完毕");
        } catch (FileNotFoundException e) {
            log.error("游戏存档失败,找不到文件！", e);
        } catch (IOException e) {
            log.error("游戏存档失败，IO错误！", e);
        } catch (Exception e) {
            log.error("游戏存档失败！", e);
        }
    }

    /**
     * 设置人物的道具
     *
     * @param index 道具位置序号，自上而下，自左至右排列 in 5*4 = [0,20)
     */
    public void setItem(Player player, int index, ItemInstance item_inst) {
        ItemInstance[] items_inst = getItems(player);
        items_inst[index] = item_inst;

        if (item_inst != null && item_inst.getItem() == null) {
            if (item_inst.getName() != null) {
                Item item = this.findItemByName(item_inst.getName());
                if (item != null) {
                    item_inst.setItem(item);
                } else {
                    items_inst[index] = null;
                }
            }
        }
    }

    public void setItemByName(Player player, int index, String itemName) {
        setItem(player, index, createItem(itemName));
    }


    public void setItems(Player player, ItemInstance[] items_inst) {
        //TODO 改进初始化物品的算法
        if (items_inst != null) {
            ItemInstance[] _items = getItems(player);
            for (int i = 0; i < _items.length; i++) {
                ItemInstance _inst = items_inst[i];
                _items[i] = _inst;
                try {
                    if (_inst != null && _inst.getItem() == null) {
                        if (_inst.getName() != null) {
                            Item item = this.findItemByName(_inst.getName());
                            if (item != null) {
                                _inst.setItem(item);
                            } else {
                                _items[i] = null;
                            }
                        }
                    }
                } catch (Exception e) {
                    _items[i] = null;
                    log.error("", e);
                }
            }
        }
    }

    public void swapItem(Player player, int srcIndex, int destIndex) {
        synchronized (player) {
            ItemInstance[] items = getItems(player);
            ItemInstance temp = items[srcIndex];
            items[srcIndex] = items[destIndex];
            items[destIndex] = temp;
        }
    }


}
