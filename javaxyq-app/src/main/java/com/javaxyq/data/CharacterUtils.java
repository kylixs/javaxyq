package com.javaxyq.data;

import java.util.HashMap;

public class CharacterUtils {

    private static HashMap<String, String> charNames;

    public static String char_0001 = "0001";
    public static String char_0002 = "0002";
    public static String char_0003 = "0003";
    public static String char_0004 = "0004";
    public static String char_0005 = "0005";
    public static String char_0006 = "0006";
    public static String char_0007 = "0007";
    public static String char_0008 = "0008";
    public static String char_0009 = "0009";
    public static String char_0010 = "0010";
    public static String char_0011 = "0011";
    public static String char_0012 = "0012";

    public static String getCharacterName(String character) {
        if (charNames == null) {
            charNames = new HashMap<String, String>();
            charNames.put("0001", "逍遥生");
            charNames.put("0002", "剑侠客");
            charNames.put("0003", "飞燕女");
            charNames.put("0004", "英女侠");
            charNames.put("0005", "巨魔王");
            charNames.put("0006", "虎头怪");
            charNames.put("0007", "狐美人");
            charNames.put("0008", "骨精灵");
            charNames.put("0009", "神天兵");
            charNames.put("0010", "龙太子");
            charNames.put("0011", "舞天姬");
            charNames.put("0012", "玄彩娥");
        }
        return charNames.get(character);
    }


    /**
     * 判断是否为该角色的首选武器
     *
     * @param character
     * @param type
     * @return
     */
    public static boolean isFirstWeapon(String character, String type) {
        if (char_0001.equals(character)) {
            return "剑".equals(type);
        } else if (char_0002.equals(character)) {
            return "刀".equals(type);
        } else if (char_0003.equals(character)) {
            return "环圈".equals(type);
        } else if (char_0004.equals(character)) {
            return "鞭".equals(type);
        } else if (char_0005.equals(character)) {
            return "刀".equals(type);
        } else if (char_0006.equals(character)) {
            return "锤".equals(type);
        } else if (char_0007.equals(character)) {
            return "鞭".equals(type);
        } else if (char_0008.equals(character)) {
            return "魔棒".equals(type);
        } else if (char_0009.equals(character)) {
            return "锤".equals(type);
        } else if (char_0010.equals(character)) {
            return "枪矛".equals(type);
        } else if (char_0011.equals(character)) {
            return "环圈".equals(type);
        } else if (char_0012.equals(character)) {
            return "魔棒".equals(type);
        }
        return false;
    }


    /**
     * 判断是否为普通状态（站立及行走）
     *
     * @param state
     * @return
     */
    public static boolean isNormalState(String state) {
        return "stand".equals(state) || "walk".equals(state);
    }

}
