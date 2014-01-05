package com.javaxyq.model;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlayerVO  implements Serializable{
	
	private static final long serialVersionUID = 986420253388692309L;
	public static final String STATE_STAND = "stand";
	public static final String STATE_WALK = "walk";
	
	public String id = "Undefined";
	public String name ="player";
	public String character = "0000";
	public int level = 0;
	public int hp = 83;
	public int maxHp = 100;
	public int tmpMaxHp = 100;
	public int mp = 20;
	public int maxMp = 50;
	//愤怒值
	public int sp = 0;
	/** 活力 */
	public int energy = 10;
	public int maxEnergy = 50;
	/** 体力 */
	public int stamina = 10;
	public int maxStamina = 50;
	
	public long exp = 56;
	public int money = 0;
	public int deposit = 0;
	public Date createDate = new Date();
	
	/** 力量 */
	public int strength = 10;
	/** 体质*/
	public int physique = 10;
	/** 魔力 */
	public int magic = 10;
	/** 耐力 */
	public int durability = 10;
	/** 敏捷 */
	public int agility = 10;
	/** 潜力*/
	public int potentiality = 0;
	
	/** 命中 */
	public int hitrate = 62;
	/** 伤害*/
	public int harm = 50;
	/** 防御*/
	public int defense = 16;
	/** 速度*/
	public int speed = 11;
	/** 躲避*/
	public int shun = 21;
	/** 灵力*/
	public int wakan = 19;
	/** tmp命中*/
	public int tmpHitrate = 0;
	/** tmp伤害*/
	public int tmpHarm = 0;
	/** tmp防御*/
	public int tmpDefense = 0;
	/** tmp速度*/
	public int tmpSpeed = 0;
	/** tmp躲避*/
	public int tmpShun = 0;
	/** tmp灵力*/
	public int tmpWakan = 0;

	//称谓$
	public String title = "≮潇洒哥≯";
	//人气
	public int popularity = 800;
	//帮派
	public String faction = "逍遥阁";
	//门派
	public String school = "大唐官府";
	//帮派贡献
	public int factionContribution = 0;  
	//门派贡献
	public int schoolContribution= 0;

	//待分配点数
	public Map<String,Integer> assignPoints = new HashMap<String, Integer>();//[体质:0,魔力:0,力量:0,耐力:0,敏捷:0];

	//成长率
	public float growthRate; 
	
	public String state = STATE_STAND;
	public int direction;
	public int[] colorations;
	public Point sceneLocation = new Point();
	
	public PlayerVO() {
		assignPoints.put("physique", 0);
		assignPoints.put("magic", 0);
		assignPoints.put("strength", 0);
		assignPoints.put("durability", 0);
		assignPoints.put("agility", 0);
	}
	public PlayerVO(String id, String name, String character) {
		this.id = id;
		this.name = name;
		this.character = character;
		assignPoints.put("physique", 0);
		assignPoints.put("magic", 0);
		assignPoints.put("strength", 0);
		assignPoints.put("durability", 0);
		assignPoints.put("agility", 0);
	}

	/**
	 * @param data
	 */
	public PlayerVO(PlayerVO data) {
		this.id = data.id;
		this.name = data.name;
		this.character = data.character;
		this.level = data.level;
		this.hp = data.hp;
		this.maxHp = data.maxHp;
		this.tmpMaxHp = data.tmpMaxHp;
		this.mp = data.mp;
		this.maxMp = data.maxMp;
		this.sp = data.sp;
		this.energy = data.energy;
		this.maxEnergy = data.maxEnergy;
		this.stamina = data.stamina;
		this.maxStamina = data.maxStamina;
		this.exp = data.exp;
		this.money = data.money;
		this.deposit = data.deposit;
		this.createDate = new Date();//create date
		this.strength = data.strength;
		this.physique = data.physique;
		this.magic = data.magic;
		this.durability = data.durability;
		this.agility = data.agility;
		this.potentiality = data.potentiality;
		this.hitrate = data.hitrate;
		this.harm = data.harm;
		this.defense = data.defense;
		this.speed = data.speed;
		this.shun = data.shun;
		this.wakan = data.wakan;
		this.tmpHitrate = data.tmpHitrate;
		this.tmpHarm = data.tmpHarm;
		this.tmpDefense = data.tmpDefense;
		this.tmpSpeed = data.tmpSpeed;
		this.tmpShun = data.tmpShun;
		this.tmpWakan = data.tmpWakan;
		this.title = data.title;
		this.popularity = data.popularity;
		this.faction = data.faction;
		this.school = data.school;
		this.factionContribution = data.factionContribution;
		this.schoolContribution = data.schoolContribution;
		this.assignPoints = new HashMap(data.assignPoints);
		this.growthRate = data.growthRate;
		this.state = data.state;
		this.direction = data.direction;
		this.colorations = data.colorations;
		this.sceneLocation = new Point(data.sceneLocation);
	}
	protected void writeObject(ObjectOutputStream s)
	throws IOException
	{
		s.writeObject(assignPoints);
		s.writeUTF(character);
		s.writeObject(colorations);
		s.writeObject(createDate);
		s.writeInt(deposit);
		s.writeInt(direction);
		s.writeLong(exp);
		s.writeFloat(growthRate);
		s.writeInt(hp);
		s.writeUTF(id);
		s.writeInt(level);
		s.writeInt(maxHp);
		s.writeInt(maxMp);
		s.writeInt(maxEnergy);
		s.writeInt(maxStamina);
		s.writeInt(money);
		s.writeInt(mp);

		s.writeUTF(name);
		s.writeInt(popularity);
		s.writeInt(potentiality);
		s.writeObject(sceneLocation);
		s.writeInt(sp);
		s.writeUTF(state);
		s.writeUTF(title);
		s.writeInt(tmpMaxHp);
		s.writeInt(tmpShun);
		s.writeInt(tmpDefense);
		s.writeInt(tmpWakan);
		s.writeInt(tmpHitrate);
		s.writeInt(tmpHarm);
		s.writeInt(tmpSpeed);

		s.writeUTF(faction);
		s.writeInt(factionContribution);
		s.writeInt(shun);
		s.writeInt(defense);
		s.writeInt(energy);
		s.writeInt(strength);
		s.writeInt(wakan);
		s.writeUTF(school);
		s.writeInt(schoolContribution);
		s.writeInt(agility);
		s.writeInt(hitrate);
		s.writeInt(magic);
		s.writeInt(durability);
		s.writeInt(harm);
		s.writeInt(speed);
		s.writeInt(stamina);
		s.writeInt(physique);
		
	}
	
	/**
	 * Reconstitute this object from a stream (i.e., deserialize it).
	 */
	protected void readObject(ObjectInputStream s)
	throws IOException, ClassNotFoundException
	{
		assignPoints=(Map) s.readObject();
		character=s.readUTF();
		colorations=(int[]) s.readObject();
		createDate=(Date) s.readObject();
		deposit=s.readInt();
		direction=s.readInt();
		exp=s.readLong();
		growthRate=s.readFloat();
		hp=s.readInt();
		id=s.readUTF();
		level=s.readInt();
		maxHp=s.readInt();
		maxMp=s.readInt();
		maxEnergy=s.readInt();
		maxStamina=s.readInt();
		money=s.readInt();
		mp=s.readInt();

		name=s.readUTF();
		popularity=s.readInt();
		potentiality=s.readInt();
		sceneLocation=(Point) s.readObject();
		sp=s.readInt();
		state=s.readUTF();
		title=s.readUTF();
		tmpMaxHp=s.readInt();
		tmpShun=s.readInt();
		tmpDefense=s.readInt();
		tmpWakan=s.readInt();
		tmpHitrate=s.readInt();
		tmpHarm=s.readInt();
		tmpSpeed=s.readInt();

		faction=s.readUTF();
		factionContribution=s.readInt();
		shun=s.readInt();
		defense=s.readInt();
		energy=s.readInt();
		strength=s.readInt();
		wakan=s.readInt();
		school=s.readUTF();
		schoolContribution=s.readInt();
		agility=s.readInt();
		hitrate=s.readInt();
		magic=s.readInt();
		durability=s.readInt();
		harm=s.readInt();
		speed=s.readInt();
		stamina=s.readInt();
		physique=s.readInt();
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlayerVO [assignPoints=");
		builder.append(assignPoints);
		builder.append(", character=");
		builder.append(character);
		builder.append(", colorations=");
		builder.append(Arrays.toString(colorations));
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", deposit=");
		builder.append(deposit);
		builder.append(", direction=");
		builder.append(direction);
		builder.append(", exp=");
		builder.append(exp);
		builder.append(", growthRate=");
		builder.append(growthRate);
		builder.append(", hp=");
		builder.append(hp);
		builder.append(", id=");
		builder.append(id);
		builder.append(", level=");
		builder.append(level);
		builder.append(", maxHp=");
		builder.append(maxHp);
		builder.append(", maxMp=");
		builder.append(maxMp);
		builder.append(", max体力=");
		builder.append(maxStamina);
		builder.append(", max活力=");
		builder.append(maxEnergy);
		builder.append(", money=");
		builder.append(money);
		builder.append(", mp=");
		builder.append(mp);
		builder.append(", name=");
		builder.append(name);
		builder.append(", popularity=");
		builder.append(popularity);
		builder.append(", potentiality=");
		builder.append(potentiality);
		builder.append(", sceneLocation=");
		builder.append(sceneLocation);
		builder.append(", sp=");
		builder.append(sp);
		builder.append(", state=");
		builder.append(state);
		builder.append(", title=");
		builder.append(title);
		builder.append(", tmpMaxHp=");
		builder.append(tmpMaxHp);
		builder.append(", tmp伤害=");
		builder.append(tmpHarm);
		builder.append(", tmp命中=");
		builder.append(tmpHitrate);
		builder.append(", tmp灵力=");
		builder.append(tmpWakan);
		builder.append(", tmp躲避=");
		builder.append(tmpShun);
		builder.append(", tmp速度=");
		builder.append(tmpSpeed);
		builder.append(", tmp防御=");
		builder.append(tmpDefense);
		builder.append(", 伤害=");
		builder.append(harm);
		builder.append(", 体力=");
		builder.append(stamina);
		builder.append(", 体质=");
		builder.append(physique);
		builder.append(", 力量=");
		builder.append(strength);
		builder.append(", 命中=");
		builder.append(hitrate);
		builder.append(", 帮派=");
		builder.append(faction);
		builder.append(", 帮派贡献=");
		builder.append(factionContribution);
		builder.append(", 敏捷=");
		builder.append(agility);
		builder.append(", 活力=");
		builder.append(energy);
		builder.append(", 灵力=");
		builder.append(wakan);
		builder.append(", 耐力=");
		builder.append(durability);
		builder.append(", 躲避=");
		builder.append(shun);
		builder.append(", 速度=");
		builder.append(speed);
		builder.append(", 门派=");
		builder.append(school);
		builder.append(", 门派贡献=");
		builder.append(schoolContribution);
		builder.append(", 防御=");
		builder.append(defense);
		builder.append(", 魔力=");
		builder.append(magic);
		builder.append("]");
		return builder.toString();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCharacter() {
		return character;
	}
	public void setCharacter(String character) {
		this.character = character;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getTmpMaxHp() {
		return tmpMaxHp;
	}
	public void setTmpMaxHp(int tmpMaxHp) {
		this.tmpMaxHp = tmpMaxHp;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public int getMaxMp() {
		return maxMp;
	}
	public void setMaxMp(int maxMp) {
		this.maxMp = maxMp;
	}
	public int getSp() {
		return sp;
	}
	public void setSp(int sp) {
		this.sp = sp;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public int getMaxEnergy() {
		return maxEnergy;
	}
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	public int getStamina() {
		return stamina;
	}
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getMaxStamina() {
		return maxStamina;
	}
	public void setMaxStamina(int max体力) {
		this.maxStamina = max体力;
	}
	public long getExp() {
		return exp;
	}
	public void setExp(long exp) {
		this.exp = exp;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getDeposit() {
		return deposit;
	}
	public void setDeposit(int deposit) {
		this.deposit = deposit;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getStrength() {
		return strength;
	}
	public void setStrength(int 力量) {
		this.strength = 力量;
	}
	public int getPhysique() {
		return physique;
	}
	public void setPhysique(int physique) {
		this.physique = physique;
	}
	public int getMagic() {
		return magic;
	}
	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getDurability() {
		return durability;
	}
	public void setDurability(int durability) {
		this.durability = durability;
	}
	public int getAgility() {
		return agility;
	}
	public void setAgility(int agility) {
		this.agility = agility;
	}
	public int getPotentiality() {
		return potentiality;
	}
	public void setPotentiality(int potentiality) {
		this.potentiality = potentiality;
	}
	public int getHitrate() {
		return hitrate;
	}
	public void setHitrate(int 命中) {
		this.hitrate = 命中;
	}
	public int getHarm() {
		return harm;
	}
	public void setHarm(int 伤害) {
		this.harm = 伤害;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int 防御) {
		this.defense = 防御;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int 速度) {
		this.speed = 速度;
	}
	public int getShun() {
		return shun;
	}
	public void setShun(int 躲避) {
		this.shun = 躲避;
	}
	public int getWakan() {
		return wakan;
	}
	public void setWakan(int 灵力) {
		this.wakan = 灵力;
	}
	public int getTmpHitrate() {
		return tmpHitrate;
	}
	public void setTmpHitrate(int tmp命中) {
		this.tmpHitrate = tmp命中;
	}
	public int getTmpHarm() {
		return tmpHarm;
	}
	public void setTmpHarm(int tmp伤害) {
		this.tmpHarm = tmp伤害;
	}
	public int getTmpDefense() {
		return tmpDefense;
	}
	public void setTmpDefense(int tmp防御) {
		this.tmpDefense = tmp防御;
	}
	public int getTmpSpeed() {
		return tmpSpeed;
	}
	public void setTmpSpeed(int tmp速度) {
		this.tmpSpeed = tmp速度;
	}
	public int getTmpShun() {
		return tmpShun;
	}
	public void setTmpShun(int tmp躲避) {
		this.tmpShun = tmp躲避;
	}
	public int getTmpWakan() {
		return tmpWakan;
	}
	public void setTmpWakan(int tmp灵力) {
		this.tmpWakan = tmp灵力;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public String getFaction() {
		return faction;
	}
	public void setFaction(String 帮派) {
		this.faction = 帮派;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String 门派) {
		this.school = 门派;
	}
	public int getFactionContribution() {
		return factionContribution;
	}
	public void setFactionContribution(int 帮派贡献) {
		this.factionContribution = 帮派贡献;
	}
	public int getSchoolContribution() {
		return schoolContribution;
	}
	public void setSchoolContribution(int 门派贡献) {
		this.schoolContribution = 门派贡献;
	}
	public Map getAssignPoints() {
		return assignPoints;
	}
	public void setAssignPoints(Map assignPoints) {
		this.assignPoints = assignPoints;
	}
	public float getGrowthRate() {
		return growthRate;
	}
	public void setGrowthRate(float growthRate) {
		this.growthRate = growthRate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int[] getColorations() {
		return colorations;
	}
	public void setColorations(int[] colorations) {
		this.colorations = colorations;
	}
	public Point getSceneLocation() {
		return sceneLocation;
	}
	public void setSceneLocation(Point sceneLocation) {
		this.sceneLocation = sceneLocation;
	}

}
