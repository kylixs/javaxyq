package com.javaxyq.core;

import com.javaxyq.model.PlayerVO;
import com.javaxyq.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class PlayerPropertyCalculator {
    private static String[] 人物门派 = new String[]{"大唐官府", "方寸山", "化生寺", "女儿村", "阴曹地府", "魔王寨", "狮驼岭", "盘丝洞", "天宫", "龙宫", "五庄观", "普陀山"};
    private static double[] 加成系数1 = new double[]{20.0D, 30.0D, 5.0D, 25.5D, 30.0D, 30.0D, 30.5D, 35.0D, 25.0D, 25.0D, 30.0D, 20.0D};
    private static double[] 加成系数2 = new double[]{2.5D, 1.0D, 1.1D, 1.96D, 2.5D, 2.03D, 2.01D, 2.03D, 2.51D, 2.01D, 1.51D, 1.25D};
    private static double[] 加成系数3 = new double[]{0.014D, 0.01D, 0.014D, 0.007D, 0.018D, 0.016D, 0.0063D, 0.016D, 0.0164D, 0.016D, 0.0164D, 0.0136D};
    private static String[] 人族 = new String[]{"0001", "0002", "0003", "0004"};
    private static String[] 魔族 = new String[]{"0005", "0006", "0007", "0008"};
    private static String[] 仙族 = new String[]{"0009", "0010", "0011", "0012"};

    public PlayerPropertyCalculator() {
    }

    private static boolean inArray(String[] array, String value) {
        for(int i = 0; i < array.length; ++i) {
            if (StringUtils.equals(array[i], value)) {
                return true;
            }
        }

        return false;
    }

    private static int reIndex(String[] array, String value) {
        for(int i = 0; i < array.length; ++i) {
            if (StringUtils.equals(array[i], value)) {
                return i;
            }
        }

        return -1;
    }

    private static int formula_harm(PlayerVO attrs, double hurt, int index) {
        for(int level = 0; level < (Integer)attrs.attrsLevel.get("伤害"); ++level) {
            if (level == 0) {
                hurt += 加成系数1[index];
            }

            hurt += 加成系数2[index] + 加成系数3[index] * (double)(Integer)attrs.attrsLevel.get("伤害");
        }

        return (int)hurt;
    }

    private static int formula_defense(PlayerVO attrs, double defense) {
        for(int level = 0; level < (Integer)attrs.attrsLevel.get("防御"); ++level) {
            defense += 1.01D + 0.014D * (double)(Integer)attrs.attrsLevel.get("防御");
        }

        return (int)defense;
    }

    private static int formula_wakan(PlayerVO attrs, double wakan) {
        for(int level = 0; level < (Integer)attrs.attrsLevel.get("灵力"); ++level) {
            wakan += 0.5D + 0.009D * (double)(Integer)attrs.attrsLevel.get("灵力");
        }

        return (int)wakan;
    }

    private static int formula_dodge(PlayerVO attrs, double dodge) {
        for(int level = 0; level < (Integer)attrs.attrsLevel.get("躲避"); ++level) {
            dodge += 2.02D + 0.02D * (double)(Integer)attrs.attrsLevel.get("躲避");
        }

        return (int)dodge;
    }

    public static int calc_speed(PlayerVO attrs) {
        double speed = (double)attrs.physique * 0.1D + (double)attrs.durability * 0.1D + (double)attrs.strength * 0.1D + (double)attrs.agility * 0.7D + (double)(attrs.magic * 0);
        if (StringUtils.equals("女儿村", attrs.school)) {
            for(int level = 0; level < (Integer)attrs.attrsLevel.get("速度"); ++level) {
                speed += 0.68D + 0.005D * (double)(Integer)attrs.attrsLevel.get("速度");
            }
        }

        return (int)speed;
    }

    public static int calc_wakan(PlayerVO attrs) {
        double wakan = (double)attrs.physique * 0.3D + (double)attrs.magic * 0.7D + (double)attrs.durability * 0.2D + (double)attrs.strength * 0.4D + (double)(attrs.agility * 0);
        return formula_wakan(attrs, wakan);
    }

    public static int calc_shun(PlayerVO attrs) {
        double dodge = (double)(attrs.agility * 1 + 10);
        return formula_dodge(attrs, dodge);
    }

    public static int calc_stamina(PlayerVO attrs) {
        return attrs.level * 10 + 50;
    }

    public static int calc_energy(PlayerVO attrs) {
        return attrs.level * 10 + 50;
    }

    public static int calc_hitrate(PlayerVO attrs) {
        double hitrate;
        int level;
        if (inArray(人族, attrs.character)) {
            hitrate = (double)(attrs.strength * 2 + 30);
            if (StringUtils.equals("大唐官府", attrs.school)) {
                for(level = 0; level < (Integer)attrs.attrsLevel.get("命中"); ++level) {
                    hitrate += 1.01D + 0.02D * (double)(Integer)attrs.attrsLevel.get("命中");
                }
            }

            return (int)hitrate;
        } else if (!inArray(魔族, attrs.character)) {
            return inArray(仙族, attrs.character) ? (int)((double)attrs.strength * 1.7D + 30.0D) : -1;
        } else {
            hitrate = (double)((int)((double)attrs.strength * 2.3D + 30.0D));
            if (StringUtils.equals("盘丝洞", attrs.school)) {
                for(level = 0; level < (Integer)attrs.attrsLevel.get("命中"); ++level) {
                    hitrate += 3.0D;
                }
            }

            return (int)hitrate;
        }
    }

    public static int calc_harm(PlayerVO attrs) {
        double hurt;
        int index;
        if (inArray(人族, attrs.character)) {
            hurt = (double)(attrs.hitrate / 3 + 30);
            index = reIndex(人物门派, attrs.school);
            return formula_harm(attrs, hurt, index);
        } else if (inArray(魔族, attrs.character)) {
            hurt = (double)(attrs.hitrate / 3 + 30);
            index = reIndex(人物门派, attrs.school);
            return formula_harm(attrs, hurt, index);
        } else if (inArray(仙族, attrs.character)) {
            hurt = (double)(attrs.hitrate / 3 + 30);
            index = reIndex(人物门派, attrs.school);
            return formula_harm(attrs, hurt, index);
        } else {
            return -1;
        }
    }

    public static int calc_defense(PlayerVO attrs) {
        double defense;
        if (inArray(人族, attrs.character)) {
            defense = (double)attrs.durability * 1.5D;
            return formula_defense(attrs, defense);
        } else if (inArray(魔族, attrs.character)) {
            defense = (double)attrs.durability * 1.4D;
            return formula_defense(attrs, defense);
        } else if (inArray(仙族, attrs.character)) {
            defense = (double)attrs.durability * 1.6D;
            return formula_defense(attrs, defense);
        } else {
            return -1;
        }
    }

    public static int calc_气血(PlayerVO attrs) {
        if (inArray(人族, attrs.character)) {
            return attrs.physique * 5 + 100;
        } else if (inArray(魔族, attrs.character)) {
            return attrs.physique * 6 + 100;
        } else if (!inArray(仙族, attrs.character)) {
            return -1;
        } else {
            double hp = (double)attrs.physique * 4.5D + 100.0D;
            if (StringUtils.equals("天宫", attrs.school)) {
                for(int level = 0; level < (Integer)attrs.attrsLevel.get("HP"); ++level) {
                    hp += hp * 0.003D * (double)(Integer)attrs.attrsLevel.get("HP");
                }
            }

            return (int)hp;
        }
    }

    public static int calc_魔法(PlayerVO attrs) {
        if (inArray(人族, attrs.character)) {
            return attrs.magic * 3 + 80;
        } else if (inArray(魔族, attrs.character)) {
            return (int)((double)attrs.magic * 2.5D + 80.0D);
        } else if (!inArray(仙族, attrs.character)) {
            return -1;
        } else {
            double mp = (double)attrs.magic * 3.5D + 80.0D;
            if (StringUtils.equals("五庄观", attrs.school)) {
                for(int level = 0; level < (Integer)attrs.attrsLevel.get("MP"); ++level) {
                    log.info("mp is:" + mp);
                    log.info("attrsLevel is:" + attrs.attrsLevel.get("MP"));
                    mp += mp * 0.005D * (double)(Integer)attrs.attrsLevel.get("MP");
                }
            }

            return (int)mp;
        }
    }

    public static Object invokeMethod(String mName, Object arg) {
        try {
            Method m = PlayerPropertyCalculator.class.getMethod(mName, arg.getClass());
            return m.invoke(PlayerPropertyCalculator.class, arg);
        } catch (SecurityException var3) {
            var3.printStackTrace();
        } catch (IllegalArgumentException var4) {
            var4.printStackTrace();
        } catch (NoSuchMethodException var5) {
            var5.printStackTrace();
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
        } catch (InvocationTargetException var7) {
            var7.printStackTrace();
        }

        return null;
    }
}
