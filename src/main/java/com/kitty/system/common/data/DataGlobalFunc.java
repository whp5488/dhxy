
package com.kitty.system.common.data;

import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.game.vo.Npc;
import java.io.Serializable;
import com.kitty.cache.RedisService;
import com.kitty.system.init.domain.vo.Move;
import com.kitty.common.model.Pos;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.vo.MonsterPower;
import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import java.util.Random;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.LinkedHashMap;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.init.domain.SysLevelExp;
import com.kitty.system.init.pool.ExpDataPool;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.card.domain.CardChange;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.pool.SkillDataPool;
import java.util.HashMap;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.enums.war.wardef.HERO;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.pet.domain.Pet;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import org.slf4j.Logger;

public class DataGlobalFunc
{
    private static final Logger log;
    
    public static boolean visual(final int rolex, final int roley, final int npcx, final int npcy, final double range) {
        if (npcx <= 0 && npcy <= 0) {
            return true;
        }
        final double d2 = Math.hypot(npcx - rolex, npcy - roley);
        return d2 <= range && d2 < range;
    }
    
    public static long storageExp(final int grade, final int type) {
        long exp = 1500000000L;
        if (type == LOGIC.LOGICTYPE_HERO.getValue()) {
            if (grade >= 1120 && grade < 4000) {
                exp = 15000000000L;
            }
            else if (grade >= 4000) {
                exp = 30000000000L;
            }
        }
        else if (type == LOGIC.LOGICTYPE_PET.getValue()) {
            if (grade >= 1180 && grade < 3000) {
                exp = 15000000000L;
            }
            else if (grade >= 3000) {
                exp = 30000000000L;
            }
        }
        return exp;
    }
    
    public static String[] getGuess(final String string) {
        if (string.length() == 2) {
            final String outer = string.substring(0, 1);
            final String inner = string.substring(1, string.length());
            return new String[] { outer, inner };
        }
        if (string.length() == 3) {
            final String outer = string.substring(0, 2);
            final String inner = string.substring(2, string.length());
            return new String[] { outer, inner };
        }
        return null;
    }
    
    public static Integer GetObjType(final Integer lTypeId) {
        if (lTypeId != null && lTypeId > 0) {
            final Integer rNum = lTypeId / 10000;
            return rNum;
        }
        return 0;
    }
    
    public static Integer GetProType(final int dis) {
        if (dis <= 0) {
            return PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue();
        }
        return PRO_DICT_TYPE.getType(dis % 10000);
    }
    
    public static Integer GetProDictType(final int dis) {
        if (dis <= 0) {
            return dis;
        }
        return dis % 10000;
    }
    
    public static Integer getRoleReborn(final int rei, final int race, final int sex) {
        if (rei >= 1 && rei <= 4) {
            final Map<Integer, SysReiEn> map = PartDataPool.reiEnMap;
            for (final Integer key : map.keySet()) {
                final SysReiEn reiEn = map.get(key);
                if (reiEn.getRei() == rei && reiEn.getRace() == race && reiEn.getSex() == sex) {
                    return reiEn.getSerial();
                }
            }
        }
        return -1;
    }
    
    public static boolean checkPetFly(final Pet pet) {
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        return sysPet != null && (sysPet.getLevelType().equals(PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) || sysPet.getLevelType().equals(PETLEVELTYPE.Pet_LevelType_TSSS.getValue()));
    }
    
    public static int getPetFlyCount(final int rei, final int level) {
        int count = 0;
        final int grade = rei * 1000 + level;
        if (grade >= 2120) {
            count = 3;
        }
        else if (grade >= 1100) {
            count = 2;
        }
        else if (grade >= 50) {
            count = 1;
        }
        return count;
    }
    
    public static Integer getRace(final Integer serial) {
        Integer value = 1;
        final SysHero hero = PartDataPool.heroMap.get(serial);
        if (hero != null) {
            value = hero.getRace();
        }
        return value;
    }
    
    public static List<Integer> getRoleCareerSerial(final List<Integer> cable) {
        final List<Integer> list = new ArrayList<Integer>();
        if (cable != null) {
            final Map<Integer, SysHero> data_hero = PartDataPool.heroMap;
            for (final Integer key : data_hero.keySet()) {
                final SysHero hero = data_hero.get(key);
                final Integer race = hero.getRace();
                final Integer gender = hero.getGender();
                if (gender == HERO.HERO_MALE.getValue()) {
                    if (race == RACE.RACE_REN.getValue() && cable.contains(1)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_XIAN.getValue() && cable.contains(3)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_MO.getValue() && cable.contains(5)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_GUI.getValue() && cable.contains(7)) {
                        list.add(hero.getSerial());
                    }
                    else {
                        if (race != RACE.RACE_LONG.getValue() || !cable.contains(9)) {
                            continue;
                        }
                        list.add(hero.getSerial());
                    }
                }
                else {
                    if (gender != HERO.HERO_FEMALE.getValue()) {
                        continue;
                    }
                    if (race == RACE.RACE_REN.getValue() && cable.contains(2)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_XIAN.getValue() && cable.contains(4)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_MO.getValue() && cable.contains(6)) {
                        list.add(hero.getSerial());
                    }
                    else if (race == RACE.RACE_GUI.getValue() && cable.contains(8)) {
                        list.add(hero.getSerial());
                    }
                    else {
                        if (race != RACE.RACE_LONG.getValue() || !cable.contains(10)) {
                            continue;
                        }
                        list.add(hero.getSerial());
                    }
                }
            }
            return list;
        }
        return list;
    }
    
    public static Integer GetItemTypeByItemTypeId(final Integer iTypeId) {
        if (iTypeId == null) {
            return null;
        }
        final Integer num = iTypeId / 10000;
        if (num > 100) {
            return num / 100 * 100;
        }
        if (num > 10) {
            return num / 10 * 10;
        }
        return num;
    }
    
    public static Integer getItemSubTypeByItemTypeId(Integer iTypeId) {
        if (iTypeId != null) {
            iTypeId %= 10000;
            final Integer num = iTypeId / 1000;
            return num;
        }
        return null;
    }
    
    public static Integer getPetSkillBookTypeByItemTypeId(Integer iTypeId) {
        if (iTypeId != null) {
            iTypeId %= 1000;
            final Integer bookType = iTypeId / 100;
            return bookType;
        }
        return null;
    }
    
    public static Map GetItemDataByItemTypeId(final Integer iTypeId) {
        final Integer type = GetItemTypeByItemTypeId(iTypeId);
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue()) {
            return ItemDataPool.data_drug;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()) {
            return PetDataPool.petMap;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
            return ItemDataPool.data_gift_item;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue()) {
            return ItemDataPool.data_stuff;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue()) {
            return ItemDataPool.data_other_item;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_TASK.getValue()) {
            return ItemDataPool.data_task_item;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
            return EquipDataPool.newBieEq;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
            return EquipDataPool.seniorBieEq;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
            return EquipDataPool.magicalBieEq;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            return EquipDataPool.sinBieEq;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()) {
            return ItemDataPool.acsryEggMap;
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            return EquipDataPool.acsryMap;
        }
        type.intValue();
        ITEMLARGETYPE.ITEM_LARGE_TYPE_HUOBANEQPT.getValue();
        return null;
    }
    
    public static Map<Integer, SysSkill> getSkillListByAttr(final Integer attr) {
        final Map<Integer, SysSkill> map = new HashMap<Integer, SysSkill>();
        final Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
        for (final Integer key : skillMap.keySet()) {
            final SysSkill skill = skillMap.get(key);
            if (skill.getAttr().equals(attr)) {
                map.put(skill.getStep(), skill);
            }
        }
        return map;
    }
    
    public static Message operationalConstraint(final Role role, final int value, final String msg) {
        final ServerService serverService = SpringUtils.getBean(ServerService.class);
        if (serverService.getType() <= 0) {
            return null;
        }
        if (value > 0) {
            final long gold = value * serverService.getRatio();
            final long total_gold = role.getTotalGold();
            if (total_gold < gold) {
                return new RespMsg(String.format(msg, value));
            }
        }
        return null;
    }
    
    public static Map<String, Double> data_getMonsterKeZhiXiuZhengCoeff(final boolean flag) {
        final Map<String, Double> map = new HashMap<String, Double>();
        map.put("kzMonsterCoeff", 1.0);
        map.put("qkzMonsterCoeff", 1.0);
        if (flag) {
            map.put("kzMonsterCoeff", FightDataPool.data_wx_other_war_data.get(1).getValue());
            map.put("qkzMonsterCoeff", FightDataPool.data_wx_other_war_data.get(2).getValue());
        }
        return map;
    }
    
    public static List<Integer> GetPlayerSkillId(final Integer race, final Integer sex) {
        final List<Integer> list = new ArrayList<Integer>();
        if (race == RACE.RACE_REN.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                list.add(SKILLATTR.SKILLATTR_CONFUSE.getValue());
                list.add(SKILLATTR.SKILLATTR_SLEEP.getValue());
                list.add(SKILLATTR.SKILLATTR_ICE.getValue());
            }
            else {
                list.add(SKILLATTR.SKILLATTR_POISON.getValue());
                list.add(SKILLATTR.SKILLATTR_SLEEP.getValue());
                list.add(SKILLATTR.SKILLATTR_ICE.getValue());
            }
        }
        else if (race == RACE.RACE_MO.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                list.add(SKILLATTR.SKILLATTR_SPEED.getValue());
                list.add(SKILLATTR.SKILLATTR_ATTACK.getValue());
                list.add(SKILLATTR.SKILLATTR_ZHEN.getValue());
            }
            else {
                list.add(SKILLATTR.SKILLATTR_PAN.getValue());
                list.add(SKILLATTR.SKILLATTR_ATTACK.getValue());
                list.add(SKILLATTR.SKILLATTR_ZHEN.getValue());
            }
        }
        else if (race == RACE.RACE_XIAN.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                list.add(SKILLATTR.SKILLATTR_WIND.getValue());
                list.add(SKILLATTR.SKILLATTR_THUNDER.getValue());
                list.add(SKILLATTR.SKILLATTR_WATER.getValue());
            }
            else {
                list.add(SKILLATTR.SKILLATTR_FIRE.getValue());
                list.add(SKILLATTR.SKILLATTR_THUNDER.getValue());
                list.add(SKILLATTR.SKILLATTR_WATER.getValue());
            }
        }
        else if (race == RACE.RACE_GUI.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                list.add(SKILLATTR.SKILLATTR_XIXUE.getValue());
                list.add(SKILLATTR.SKILLATTR_AIHAO.getValue());
                list.add(SKILLATTR.SKILLATTR_YIWANG.getValue());
            }
            else {
                list.add(SKILLATTR.SKILLATTR_SHUAIRUO.getValue());
                list.add(SKILLATTR.SKILLATTR_AIHAO.getValue());
                list.add(SKILLATTR.SKILLATTR_YIWANG.getValue());
            }
        }
        else if (race == RACE.RACE_LONG.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                list.add(SKILLATTR.SKILLATTR_HENGSAO.getValue());
                list.add(SKILLATTR.SKILLATTR_ZHIYU.getValue());
                list.add(SKILLATTR.SKILLATTR_POJIA.getValue());
            }
            else {
                list.add(SKILLATTR.SKILLATTR_HENGSAO.getValue());
                list.add(SKILLATTR.SKILLATTR_ZHIYU.getValue());
                list.add(SKILLATTR.SKILLATTR_ZHENJI.getValue());
            }
            list.add(SKILLATTR.SKILLATTR_NILIN.getValue());
        }
        return list;
    }
    
    public static Integer convertToGold(final Integer coin, final Integer silver, final boolean flag) {
        Integer gold = 0;
        gold += coin / 10000;
        gold += silver / 100;
        if (flag && gold != Math.floor(gold)) {
            gold = BigDecimalUtil.decimalScaleInteger(0, Math.floor(gold) + 1.0);
        }
        return gold;
    }
    
    public static long getMallItemGold(final List<Integer> list) {
        if (list == null || list.size() <= 0) {
            return -1L;
        }
        long gold = 0L;
        for (final Integer serial : list) {
            final long mallPrice = getMallPrice(serial, 1);
            if (mallPrice <= 0L) {
                return -1L;
            }
            gold += mallPrice;
        }
        return gold;
    }
    
    public static long getMallPrice(final int serial, final int num) {
        Integer discount = -1;
        double price = 0.0;
        final Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
        final SysShopOther shop = shopOtherMap.get(serial);
        if (shop != null) {
            price = shop.getPrice();
            discount = shop.getDiscount();
        }
        if (discount <= 0) {
            final Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
            final SysShopStrengthenMaterial shopStrengthenMaterial = shopStrengthenMaterialMap.get(serial);
            if (shopStrengthenMaterial != null) {
                price = shopStrengthenMaterial.getPrice();
                discount = shopStrengthenMaterial.getDiscount();
            }
        }
        if (discount <= 0) {
            discount = 100;
        }
        final String priceMoney = String.valueOf((int)(price * num));
        final Integer sum_price = BigDecimalUtil.decimalScaleString(0, BigDecimalUtil.multiply(priceMoney, BigDecimalUtil.divide(discount.toString(), "100")));
        return sum_price;
    }
    
    public static int getBagGoodsNum(final BagGoods bagGoods) {
        int num = 0;
        final NutMap rests = bagGoods.getRests();
        for (final String key : rests.keySet()) {
            if (rests.getInt(key) > 0) {
                num += rests.getInt(key, 0);
            }
        }
        return num;
    }
    
    public static int getCardChangeNum(final CardChange cardChange) {
        int num = 0;
        final NutMap rests = cardChange.getRests();
        for (final String s : rests.keySet()) {
            num += rests.getInt(s);
        }
        return num;
    }
    
    public static int unrestricted(final NutMap rests) {
        int num = 0;
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        for (final String key : rests.keySet()) {
            if (String.valueOf(0).equals(key)) {
                num += rests.getInt(key, 0);
            }
            else {
                if (String.valueOf(2).equals(key) || !NumberUtil.isNumber(key)) {
                    continue;
                }
                final Integer time = Integer.valueOf(key);
                if (currTime <= time) {
                    continue;
                }
                num += rests.getInt(key, 0);
            }
        }
        return num;
    }
    
    public static int convertLevel(final int lv) {
        int level = lv;
        if (lv > 100 && lv < 120) {
            level = 1000 + lv;
        }
        else if (lv > 120 && lv < 140) {
            level = 2000 + lv;
        }
        else if (lv > 140 && lv < 200) {
            level = 3000 + lv;
        }
        return level;
    }
    
    public static String getColours(final int level, final boolean flag) {
        String colours = "W";
        if (flag && level == 1) {
            colours = "LG1";
        }
        else if (level == 2) {
            colours = "LG1";
        }
        else if (level == 3) {
            colours = "LB1";
        }
        else if (level == 4) {
            colours = "P";
        }
        else if (level == 5) {
            colours = "O";
        }
        return colours;
    }
    
    public static boolean isTheSameParty(final int factionA, final int factionB) {
        return factionA > 0 && factionB > 0 && factionA == factionB;
    }
    
    public static String needGradeJudge(final int needRelive, final int needLevel, final int roleRelive, final int roleLevel, String msg) {
        final int need = needRelive * 1000 + needLevel;
        final int curve = roleRelive * 1000 + roleLevel;
        if (curve >= need) {
            return null;
        }
        if (needRelive > 0) {
            msg = msg + "#<R>" + needRelive + "转" + needLevel + "级#";
            return msg;
        }
        msg = msg + "#<R>" + needLevel + "级#";
        return msg;
    }
    
    public static long getNeedLimitLevelExp(final int type, final int rebirth, final int level, final int limit) {
        long exp = 0L;
        if (type == LOGIC.HORSE.getValue()) {
            exp = ExpDataPool.horseLevelExpMap.entrySet().stream().filter(e -> {
                final boolean b = e.getKey() >= level && e.getKey() <= limit;
                return b;
            }).mapToLong(value -> getNeedExp(rebirth, value.getValue())).sum();
        }
        return exp;
    }
    
    public static long getExpForRebirthLevel(final int type, final int rebirth, final int level) {
        final SysLevelExp levelExp = getLevelExp(type, level);
        if (levelExp == null) {
            return 0L;
        }
        return getNeedExp(rebirth, levelExp);
    }
    
    public static long getNeedExp(final int rebirth, final SysLevelExp levelExp) {
        long needExp = 0L;
        switch (rebirth) {
            case 0: {
                needExp = levelExp.getNeedExpRei0();
                break;
            }
            case 1: {
                needExp = levelExp.getNeedExpRei1();
                break;
            }
            case 2: {
                needExp = levelExp.getNeedExpRei2();
                break;
            }
            case 3: {
                needExp = levelExp.getNeedExpRei3();
                break;
            }
            case 4: {
                needExp = levelExp.getNeedExpRei4();
                break;
            }
            default: {
                needExp = 0L;
                break;
            }
        }
        return needExp;
    }
    
    public static SysLevelExp getLevelExp(final int type, final int level) {
        SysLevelExp levelExp = null;
        switch (type) {
            case 1: {
                levelExp = ExpDataPool.heroLevelExpMap.get(level);
                break;
            }
            case 2: {
                levelExp = ExpDataPool.petLevelExpMap.get(level);
                break;
            }
            case 3: {
                levelExp = ExpDataPool.partnerLevelExpMap.get(level);
                break;
            }
            case 4: {
                levelExp = ExpDataPool.horseLevelExpMap.get(level);
                break;
            }
        }
        return levelExp;
    }
    
    public static int getPj(final int pj) {
        final String value = String.valueOf(pj);
        if (value.length() >= 2) {
            return Integer.valueOf(value.substring(1, 2));
        }
        return 0;
    }
    
    public static int getEquipScore(final List<WearEquip> wearEquips) {
        int score = 0;
        for (final WearEquip wearEquip : wearEquips) {
            final FiedEquip fiedEquip = wearEquip.getField().stream().filter(e -> e.getType().equals(6679)).findFirst().orElse(null);
            if (fiedEquip != null) {
                score += fiedEquip.getValue();
            }
        }
        return score;
    }
    
    public static int getEquipScore(final WearEquip equip) {
        int score = 0;
        if (equip != null) {
            final FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(6679)).findFirst().orElse(null);
            if (fiedEquip != null) {
                score += fiedEquip.getValue();
            }
        }
        return score;
    }
    
    public static Integer getEquipInlayGemSerial(final int serial, final int pos, final int curGemNum) {
        final Integer type = GetItemTypeByItemTypeId(serial);
        int sub = 7;
        if (type >= ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue() && type <= ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            if (curGemNum < 3) {
                sub = 1;
            }
            else if (curGemNum < 6) {
                sub = 2;
            }
            else if (curGemNum < 9) {
                sub = 3;
            }
            else if (curGemNum < 12) {
                sub = 4;
            }
            else if (curGemNum < 15) {
                sub = 5;
            }
            else if (curGemNum < 18) {
                sub = 6;
            }
            else {
                sub = 7;
            }
            return 70000 + pos * 100 + sub;
        }
        return 70107;
    }
    
    public static Map<Integer, Integer> getCalculateGem(final int pos, final int num) {
        final Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        int sub = 1;
        for (int i = 0; i < num; ++i) {
            int serial = 70000 + pos * 100 + sub;
            Integer integer = map.get(serial);
            if (integer == null) {
                integer = 0;
            }
            else if (integer >= 3) {
                ++sub;
                serial = 70000 + pos * 100 + sub;
                integer = 0;
            }
            ++integer;
            map.put(serial, integer);
        }
        return map;
    }
    
    public static int gem2lowGemNum(final int serial) {
        final int level = serial % 100;
        final int[] data = { 1, 6, 30, 120, 360, 720, 1440 };
        return data[level - 1];
    }
    
    public static int getGemNum(final BagEquip equip) {
        int bn = 0;
        final FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst().orElse(null);
        if (fiedEquip != null) {
            bn = fiedEquip.getValue();
        }
        return bn;
    }
    
    public static int getWeaponGemNum(final List<WearEquip> wearEquips) {
        try {
            if (wearEquips.size() <= 0) {
                return 0;
            }
            final WearEquip wearEquip = wearEquips.stream().filter(equip -> {
                final boolean b = equip.getPosition() == ADORN.WEAPON.getValue();
                return b;
            }).findFirst().orElse(null);
            if (wearEquip == null) {
                return 0;
            }
            final int maxGemNum = 20;
            final int gemNum = getGemNum(wearEquip);
            if (gemNum < maxGemNum) {
                return gemNum;
            }
            final List<WearEquip> collect = wearEquips.stream().filter(equip -> {
                final boolean b2 = equip.getPosition() > ADORN.WEAPON.getValue() && equip.getPosition() <= ADORN.SHOES.getValue();
                return b2;
            }).collect(Collectors.toList());
            int sumGemNum = gemNum;
            for (int i = 0; i < collect.size(); ++i) {
                sumGemNum += getGemNum(collect.get(i));
            }
            if (sumGemNum >= 5 * maxGemNum) {
                return 5 * maxGemNum;
            }
            return sumGemNum;
        }
        catch (final Exception e) {
            return 0;
        }
    }
    
    public static int getGemNum(final WearEquip equip) {
        int bn = 0;
        final Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            final FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        return bn;
    }
    
    public static int getDur(final List<FiedEquip> list) {
        int dur = 0;
        final Optional<FiedEquip> first = list.stream().filter(e -> e.getType().equals(6672)).findFirst();
        if (first.isPresent()) {
            final FiedEquip fiedEquip = first.get();
            dur = fiedEquip.getValue();
        }
        return dur;
    }
    
    public static EquipAlike getEquipData(final Integer itemId) {
        final EquipAlike alike = new EquipAlike();
        final SysNewBieEq newBieEq = EquipDataPool.newBieEq.get(itemId);
        if (newBieEq != null) {
            BeanUtils.copyBeanProp(alike, newBieEq);
        }
        else if (EquipDataPool.seniorBieEq.get(itemId) != null) {
            final SysSeniorBieEq bieEq = EquipDataPool.seniorBieEq.get(itemId);
            BeanUtils.copyBeanProp(alike, bieEq);
        }
        else if (EquipDataPool.magicalBieEq.get(itemId) != null) {
            final SysMagicalBieEq bieEq2 = EquipDataPool.magicalBieEq.get(itemId);
            BeanUtils.copyBeanProp(alike, bieEq2);
        }
        else if (EquipDataPool.sinBieEq.get(itemId) != null) {
            final SysSinBieEq bieEq3 = EquipDataPool.sinBieEq.get(itemId);
            BeanUtils.copyBeanProp(alike, bieEq3);
        }
        else if (EquipDataPool.acsryMap.get(itemId) != null) {
            final SysDataAcsry acsry = EquipDataPool.acsryMap.get(itemId);
            BeanUtils.copyBeanProp(alike, acsry);
        }
        if (alike.getName() == null) {
            return null;
        }
        return alike;
    }
    
    public static String getEquipDesc(final int serial, final int pj) {
        final Integer type = GetItemTypeByItemTypeId(serial);
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
            return EquipDataPool.newBieEq.get(serial).getDesc();
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
            return EquipDataPool.seniorBieEq.get(serial).getDesc();
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
            return pj + "级神兵";
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            return pj + "阶仙器";
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            return EquipDataPool.acsryMap.get(serial).getName();
        }
        return null;
    }
    
    public static int randomEquip(final int type, final int pj, final int race) {
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            final List<SysSinBieEq> collect = EquipDataPool.sinBieEq.values().stream().filter(e -> {
                final boolean b = getPj(e.getSerial()) == pj;
                return b;
            }).collect(Collectors.toList());
            final int index = RandomUtil.round(0, collect.size() - 1);
            return collect.get(index).getSerial();
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
            final List<SysMagicalBieEq> collect2 = EquipDataPool.magicalBieEq.values().stream().filter(e -> {
                final boolean b2 = getPj(e.getSerial()) == pj;
                return b2;
            }).collect(Collectors.toList());
            final int index = RandomUtil.round(0, collect2.size() - 1);
            return collect2.get(index).getSerial();
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()) {
            return randomAcsryEgg(race);
        }
        return -1;
    }
    
    public static int randomAcsryEgg(final int race) {
        final List<SysDataAcsry> list = EquipDataPool.acsryMap.values().stream().filter(e -> {
            final boolean b = e.getOwnerRole().contains(race) || e.getOwnerRole().contains(0);
            return b;
        }).collect(Collectors.toList());
        if (list.size() > 0) {
            final int index = RandomUtil.round(0, list.size() - 1);
            final SysDataAcsry acsry = list.get(index);
            final SysDataAcsryEgg acsryEgg = ItemDataPool.acsryEggMap.values().stream().filter(e -> {
                final boolean b2 = e.getHatchType() == acsry.getSerial();
                return b2;
            }).findFirst().orElse(null);
            if (acsryEgg != null) {
                return acsryEgg.getSerial();
            }
        }
        return -1;
    }
    
    public static boolean isInMap(final Role role, final int mapId) {
        return role != null && role.getPos().getMapId().equals(mapId);
    }
    
    public static ItemEasyInfo getItemEasyInfo(final Integer serial) {
        ItemEasyInfo easy = null;
        try {
            final Integer type = GetItemTypeByItemTypeId(serial);
            if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue()) {
                final SysOtherItem item = ItemDataPool.data_other_item.get(serial);
                easy = new ItemEasyInfo();
                BeanUtils.copyBeanProp(easy, item);
                easy.setCategory(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue());
            }
            else if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue()) {
                final SysDataStuff item2 = ItemDataPool.data_stuff.get(serial);
                easy = new ItemEasyInfo();
                BeanUtils.copyBeanProp(easy, item2);
                easy.setCategory(ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue());
            }
            else if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
                final SysGiftItem item3 = ItemDataPool.data_gift_item.get(serial);
                easy = new ItemEasyInfo();
                BeanUtils.copyBeanProp(easy, item3);
                easy.setCategory(ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue());
            }
            else if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue()) {
                final SysDrug item4 = ItemDataPool.data_drug.get(serial);
                easy = new ItemEasyInfo();
                BeanUtils.copyBeanProp(easy, item4);
                easy.setCategory(ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue());
            }
            else if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()) {
                final SysDataAcsryEgg acsryEgg = ItemDataPool.acsryEggMap.get(serial);
                easy = new ItemEasyInfo();
                BeanUtils.copyBeanProp(easy, acsryEgg);
                easy.setCategory(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue());
            }
        }
        catch (final Exception e) {
            System.out.println("获取道具详情错误编号为： = " + serial);
            return null;
        }
        return easy;
    }
    
    public static List<NutMap> setMsg() {
        final String[] arr = { "嘻嘻，看我的~~", "嘻嘻，看我的~~", "欺负我个头小是吗？", "", "" };
        final List<NutMap> list = new ArrayList<NutMap>();
        for (int i = 0; i < 5; ++i) {
            final NutMap map = new NutMap();
            map.put("type", i);
            map.put("msg", arr[i]);
            list.add(map);
        }
        return list;
    }
    
    public static Map<Integer, Map<Integer, Integer>> setQuota() {
        final Map<Integer, Integer> value = new HashMap<Integer, Integer>();
        final Map<Integer, Map<Integer, Integer>> map = new HashMap<Integer, Map<Integer, Integer>>();
        final List<Integer> list = Mall.toList();
        for (final Integer key : list) {
            map.put(key, value);
        }
        return map;
    }
    
    public static Map<Integer, Integer> faction() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        final List<Integer> list = FACTION_PRO_DICT.toList();
        for (final Integer key : list) {
            map.put(key, 0);
        }
        return map;
    }
    
    public static Map<String, Integer> setting() {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("else_auto_fight", 1);
        map.put("else_db_speed_show", 0);
        return map;
    }
    
    public static Map<String, Integer> closeLimit() {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("close_dan", 0);
        map.put("close_fight", 0);
        return map;
    }
    
    public static NutMap newDistrict() {
        final NutMap nutMap = new NutMap();
        for (final Byte key : WelfareDataPool.newDistrictMap.keySet()) {
            nutMap.setv(String.valueOf(key), 0);
        }
        return nutMap;
    }
    
    public static boolean seniorIsGem(final SysSeniorBieEq bieEq, final BagEquip equip) {
        boolean flag = true;
        if (bieEq.getNeedGrade() >= 120) {
            final Map<String, SysAboveAttrLib> aboveMap = EquipDataPool.above_attr_lib;
            final String baseAttr = bieEq.getBaseAttr();
            final Map<Integer, Double> fieldMap = new HashMap<Integer, Double>();
            final String[] split2;
            final String[] split = split2 = baseAttr.split(",");
            for (final String dist : split2) {
                final SysAboveAttrLib lib = aboveMap.get(dist);
                if (lib != null) {
                    final String base = lib.getBaseAttr();
                    if (base != null && !base.isEmpty()) {
                        final String[] bases = base.split(";");
                        if (bases.length > 0) {
                            final Random random = new Random();
                            final String curBase = bases[random.nextInt(bases.length)];
                            final String[] curBaseArr = curBase.split(",");
                            if (curBaseArr.length == 3) {
                                final String curKey = curBaseArr[0];
                                final int curMaxValue = Integer.valueOf(curBaseArr[2]);
                                final int pro = CONVERT_DICT.getDist(curKey);
                                if (pro > 0) {
                                    fieldMap.put(pro + PRO_DICT_SERIAL.EQUIP_PRO.getSerial(), BigDecimalUtil.decimalScaleObjectDown(3, curMaxValue * 0.92));
                                }
                            }
                        }
                    }
                }
            }
            final List<FiedEquip> fields = equip.getField();
            for (final FiedEquip field : fields) {
                if (field.getType() > PRO_DICT_SERIAL.EQUIP_PRO.getSerial() && field != null) {
                    final Double val = fieldMap.get(field.getType());
                    if (field.getValue() == null) {
                        DataGlobalFunc.log.error("装备编号：{}, 属性编号：{}", equip.getId(), field.getType());
                    }
                    else {
                        if (val == null) {
                            flag = false;
                            break;
                        }
                        if (field.getValue() < val) {
                            flag = false;
                            break;
                        }
                        continue;
                    }
                }
            }
        }
        return flag;
    }
    
    public static Integer revolution(final Integer str) {
        String value = String.valueOf(str);
        if (value.length() < 2) {
            return 0;
        }
        value = value.substring(0, value.length() - 1);
        return Integer.valueOf(value + "0");
    }
    
    public static int proficiency(final int level) {
        final int lowLv = Math.min(level, 100);
        final int highLv = Math.min(Math.max(level - 100, 0), 60);
        final int limit = (int)(Math.ceil(lowLv / 10) * 1000.0 + Math.ceil(highLv / 10) * 2500.0);
        return limit;
    }
    
    public static SysDataMonster getMainMonster(final Integer serial) {
        final SysWarRole sysWarRole = FightDataPool.data_war_role.get(serial);
        if (sysWarRole != null) {
            Monster monster = sysWarRole.getMonster().stream().filter(e -> e.getPos().equals(10103)).findFirst().orElse(null);
            if (monster == null) {
                monster = sysWarRole.getMonster().stream().filter(e -> e.getPos().equals(10003)).findFirst().orElse(null);
                if (monster == null) {
                    monster = sysWarRole.getMonster().get(RandomUtil.round(0, sysWarRole.getMonster().size() - 1));
                }
            }
            if (monster != null) {
                return FightDataPool.data_monster.get(monster.getSerial());
            }
        }
        return null;
    }
    
    public static SysDataMonster createMonster(final int grade, final SysDataMonster obj) {
        final int rei = grade / 1000;
        final int level = grade % 1000;
        final int a = 1;
        int b;
        if (level < 100) {
            b = level * 100;
        }
        else {
            b = 10000 + (level - 100) * 250;
        }
        final Integer ext = Math.max(a, b);
        final double rdk = RandomUtil.getRandomDouble(0.0, 0.1);
        obj.setGrade(level);
        obj.setJnSld(ext);
        obj.setHp(obj.getHp() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(0, (level + (2000 - level * 20) / 100) * obj.getPowerHp().getA() * (Math.pow(level, obj.getPowerHp().getB()) + rei * obj.getPowerHp().getC())));
        obj.setAp(obj.getAp() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(0, (level + (2000 - level * 20) / 100) / 5 * obj.getPowerAp().getA() * (Math.pow(level, obj.getPowerAp().getB()) + rei * obj.getPowerAp().getC())));
        obj.setSp(obj.getSp() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(0, obj.getPowerSp().getA() * (Math.pow(level, obj.getPowerSp().getB()) + rei * obj.getPowerSp().getC())));
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KJ.getDist()) != null) {
            obj.setConGold(createMonsterAttr(obj.getConGold(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KJ.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KM.getDist()) != null) {
            obj.setConWood(createMonsterAttr(obj.getConWood(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KM.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KS.getDist()) != null) {
            obj.setConWater(createMonsterAttr(obj.getConWater(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KH.getDist()) != null) {
            obj.setConFire(createMonsterAttr(obj.getConFire(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KH.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KT.getDist()) != null) {
            obj.setConSoil(createMonsterAttr(obj.getConSoil(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.Q_KT.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.WL_XS.getDist()) != null) {
            obj.setPdeFend(createMonsterAttr(obj.getPdeFend(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.WL_XS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()) != null) {
            obj.setPPofang(createMonsterAttr(obj.getPPofang(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.CONQUER_EXTENT.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.CONQUER_RATIO.getDist()) != null) {
            obj.setPPofangPro(createMonsterAttr(obj.getPPofangPro(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.CONQUER_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.SHOCK_EXTENT.getDist()) != null) {
            obj.setPFanZhen(createMonsterAttr(obj.getPFanZhen(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.SHOCK_EXTENT.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.SHOCK_RATIO.getDist()) != null) {
            obj.setPFanZhenPro(createMonsterAttr(obj.getPFanZhenPro(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.SHOCK_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RAGE_RATIO.getDist()) != null) {
            obj.setPKuangBao(createMonsterAttr(obj.getPKuangBao(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RAGE_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.COMBO_NUM.getDist()) != null) {
            obj.setPLjTimes((int)createMonsterAttr(obj.getPLjTimes(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.COMBO_NUM.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.COMBO_RATIO.getDist()) != null) {
            obj.setPLjPro(createMonsterAttr(obj.getPLjPro(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.COMBO_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.HIT_RATIO.getDist()) != null) {
            obj.setPAcc(createMonsterAttr(obj.getPAcc(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.HIT_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.MISS_RATIO.getDist()) != null) {
            obj.setPSbl(createMonsterAttr(obj.getPSbl(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.MISS_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.S_KB_RATIO.getDist()) != null) {
            obj.setShuiOdd(createMonsterAttr(obj.getShuiOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.S_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.H_KB_RATIO.getDist()) != null) {
            obj.setHuoOdd(createMonsterAttr(obj.getHuoOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.H_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.F_KB_RATIO.getDist()) != null) {
            obj.setFengOdd(createMonsterAttr(obj.getFengOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.F_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.L_KB_RATIO.getDist()) != null) {
            obj.setLeiOdd(createMonsterAttr(obj.getLeiOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.L_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.GH_KB_RATIO.getDist()) != null) {
            obj.setGuiHuoOdd(createMonsterAttr(obj.getGuiHuoOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.GH_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.SS_KB_RATIO.getDist()) != null) {
            obj.setSanShiOdd(createMonsterAttr(obj.getSanShiOdd(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.SS_KB_RATIO.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_S.getDist()) != null) {
            obj.setRainEhan(createMonsterAttr(obj.getRainEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_S.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_H.getDist()) != null) {
            obj.setFireEhan(createMonsterAttr(obj.getFireEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_H.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_F.getDist()) != null) {
            obj.setWindEhan(createMonsterAttr(obj.getWindEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_F.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_L.getDist()) != null) {
            obj.setThunderEhan(createMonsterAttr(obj.getThunderEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_L.getDist())));
        }
        obj.setRecoverEhan(obj.getRecoverEhan());
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_SS.getDist()) != null) {
            obj.setSanshiEhan(createMonsterAttr(obj.getSanshiEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_SS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_PJ.getDist()) != null) {
            obj.setBreakEhan(createMonsterAttr(obj.getBreakEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_PJ.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ATTACK.getDist()) != null) {
            obj.setAdAtkEhan(createMonsterAttr(obj.getAdAtkEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ATTACK.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_DEF.getDist()) != null) {
            obj.setAdDefEhan(createMonsterAttr(obj.getAdDefEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_DEF.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_SPEED.getDist()) != null) {
            obj.setAdSpdEhan(createMonsterAttr(obj.getAdSpdEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_SPEED.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_QHS.getDist()) != null) {
            obj.setSweepEhan(createMonsterAttr(obj.getSweepEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_QHS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZJ.getDist()) != null) {
            obj.setThudEhan(createMonsterAttr(obj.getThudEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZJ.getDist())));
        }
        obj.setDisorderEhan(obj.getDisorderEhan());
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_HL.getDist()) != null) {
            obj.setDisorderEhan(createMonsterAttr(obj.getDisorderEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_HL.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_FY.getDist()) != null) {
            obj.setSealEhan(createMonsterAttr(obj.getSealEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_FY.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_D.getDist()) != null) {
            obj.setPoisonEhan(createMonsterAttr(obj.getPoisonEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_D.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_HS.getDist()) != null) {
            obj.setSleepEhan(createMonsterAttr(obj.getSleepEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_HS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_YW.getDist()) != null) {
            obj.setForgetEhan(createMonsterAttr(obj.getForgetEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_YW.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_MH.getDist()) != null) {
            obj.setCharmEhan(createMonsterAttr(obj.getCharmEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_MH.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZY.getDist()) != null) {
            obj.setCureEhan(createMonsterAttr(obj.getCureEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZY.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZS.getDist()) != null) {
            obj.setShockEhan(createMonsterAttr(obj.getShockEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_ZS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_GH.getDist()) != null) {
            obj.setGfireEhan(createMonsterAttr(obj.getGfireEhan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.RISE_GH.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_S.getDist()) != null) {
            obj.setKShui(createMonsterAttr(obj.getKShui(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_S.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_H.getDist()) != null) {
            obj.setKHuo(createMonsterAttr(obj.getKHuo(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_H.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_F.getDist()) != null) {
            obj.setKFeng(createMonsterAttr(obj.getKFeng(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_F.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_L.getDist()) != null) {
            obj.setKLei(createMonsterAttr(obj.getKLei(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_L.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_HL.getDist()) != null) {
            obj.setKHunLuan(createMonsterAttr(obj.getKHunLuan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_HL.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_FY.getDist()) != null) {
            obj.setKFengYin(createMonsterAttr(obj.getKFengYin(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_FY.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_HS.getDist()) != null) {
            obj.setKHunShui(createMonsterAttr(obj.getKHunShui(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_HS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_YW.getDist()) != null) {
            obj.setKYiWang(createMonsterAttr(obj.getKYiWang(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_YW.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_DU.getDist()) != null) {
            obj.setKZhongDu(createMonsterAttr(obj.getKZhongDu(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_DU.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_ZS.getDist()) != null) {
            obj.setKZhenShe(createMonsterAttr(obj.getKZhenShe(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_ZS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_GH.getDist()) != null) {
            obj.setKGuiHuo(createMonsterAttr(obj.getKGuiHuo(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_GH.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.K_SS.getDist()) != null) {
            obj.setKSanShi(createMonsterAttr(obj.getKSanShi(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.K_SS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_S.getDist()) != null) {
            obj.setFkShui(createMonsterAttr(obj.getFkShui(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_S.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_H.getDist()) != null) {
            obj.setFkHuo(createMonsterAttr(obj.getFkHuo(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_H.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_F.getDist()) != null) {
            obj.setFkFeng(createMonsterAttr(obj.getFkFeng(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_F.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_L.getDist()) != null) {
            obj.setFkLei(createMonsterAttr(obj.getFkLei(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_L.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_HL.getDist()) != null) {
            obj.setFkHunLuan(createMonsterAttr(obj.getFkHunLuan(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_HL.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_ZS.getDist()) != null) {
            obj.setFkZhenShe(createMonsterAttr(obj.getFkZhenShe(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_ZS.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_GH.getDist()) != null) {
            obj.setFkGuiHuo(createMonsterAttr(obj.getFkGuiHuo(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_GH.getDist())));
        }
        if (obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_SS.getDist()) != null) {
            obj.setFkSanShi(createMonsterAttr(obj.getFkSanShi(), rei, level, obj.getPowerAttr().get(PRO_DICT_TYPE.OMIT_K_SS.getDist())));
        }
        obj.setFsGjGl(1.0);
        return obj;
    }
    
    public static double createMonsterAttr(final double base, final int rei, final int level, final MonsterPower obj) {
        return base + Math.pow(level, obj.getA()) * obj.getB() + rei * obj.getC();
    }
    
    public static Integer getFormula(final Role role, final Integer key) {
        int formula = key;
        final SysDataTaskFormula taskFormula = TaskDataPool.taskFormulaMap.get(key);
        if (taskFormula != null) {
            final FiedValue fiedValue = taskFormula.getContent().stream().filter(e -> e.getType().equals(String.valueOf(role.getRace()))).findFirst().orElse(null);
            if (fiedValue != null) {
                formula = Integer.valueOf(String.valueOf(fiedValue.getValue()));
            }
        }
        return formula;
    }
    
    public static Pos getTreasureMap() {
        final Pos pos = new Pos(136, 50, 0, 1010);
        try {
            final Random random = new Random();
            final List<Integer> can_move_map = PartDataPool.can_move_map;
            final int index = random.nextInt(can_move_map.size());
            final Integer serial = can_move_map.get(index);
            final Move move = SpringUtils.getBean(RedisService.class).get("move_" + serial, Move.class);
            if (move != null) {
                final String str = move.getList().get(random.nextInt(move.getList().size()));
                final String[] split = str.split(",");
                if (split.length > 1) {
                    pos.setX(Integer.valueOf(split[0]));
                    pos.setY(Integer.valueOf(split[1]));
                    pos.setMapId(serial);
                }
                return pos;
            }
        }
        catch (final Exception e) {
            DataGlobalFunc.log.error("随机获取一个藏宝图位置错误");
        }
        return pos;
    }
    
    public static Npc createNpc(final Role role, final int serial, final int type) {
        try {
            final SysDataTaskNpc taskNpc = TaskDataPool.taskNpcMap.get(serial);
            if (taskNpc == null) {
                return null;
            }
            int shape = taskNpc.getShape();
            final String name = dynamicName(taskNpc.getName());
            if (taskNpc.getShapeType() <= 1) {
                shape = getFormula(role, taskNpc.getShape());
            }
            Pos pos = null;
            if (taskNpc.getScene() <= 0) {
                pos = getTreasureMap();
            }
            else {
                pos = getTreasureMap(taskNpc.getScene());
                if (taskNpc.getX() >= 0 && taskNpc.getY() >= 0) {
                    pos = new Pos();
                    pos.setX(taskNpc.getX());
                    pos.setY(taskNpc.getY());
                    pos.setMapId(taskNpc.getScene());
                }
            }
            if (pos == null) {
                return null;
            }
            final int id = SpringUtils.getBean(BossService.class).getTempNpcId();
            final Npc npc = new Npc(id, role.getRoleId(), pos.getMapId(), pos.getX(), pos.getY(), shape, name, "", serial, type);
            return npc;
        }
        catch (final Exception e) {
            System.out.println("任务创建NPC = " + serial + ", type = " + type);
            e.printStackTrace();
            return null;
        }
    }
    
    public static String dynamicName(String name) {
        if (name.contains("星名")) {
            name = "RandomStarOfficerName";
        }
        if (name.contains("Rand")) {
            final SysDataTaskNpcNameType nameType = TaskDataPool.taskNpcNameTypeMap.get(name);
            if (nameType != null) {
                name = "";
                final Random random = new Random();
                final List<SysDataTaskNpcName> list = TaskDataPool.taskNpcNameList;
                for (final String str : nameType.getValue()) {
                    final List<SysDataTaskNpcName> collect = list.stream().filter(e -> e.getKey().equals(str)).collect(Collectors.toList());
                    final int index = random.nextInt(collect.size());
                    name += collect.get(index).getName();
                }
            }
            else {
                name = "Random";
            }
        }
        else if (StringUtils.isNumeric(name)) {
            name = "数字";
        }
        return name;
    }
    
    public static Pos getTreasureMap(final int scene) {
        try {
            final List<Integer> can_move_map = PartDataPool.can_move_map;
            final Integer serial = can_move_map.stream().filter(e -> e.equals(scene)).findFirst().orElse(null);
            if (serial != null) {
                final Move move = SpringUtils.getBean(RedisService.class).get("move_" + serial, Move.class);
                if (move != null) {
                    final Random random = new Random();
                    final String str = move.getList().get(random.nextInt(move.getList().size()));
                    final String[] split = str.split(",");
                    if (split.length > 1) {
                        final Pos pos = new Pos();
                        pos.setX(Integer.valueOf(split[0]));
                        pos.setY(Integer.valueOf(split[1]));
                        pos.setMapId(serial);
                        return pos;
                    }
                }
            }
            return null;
        }
        catch (final Exception e) {
            DataGlobalFunc.log.error("根据地图编号随机获取一个位置错误：{}", scene);
            return null;
        }
    }
    
    static {
        log = LoggerFactory.getLogger(DataGlobalFunc.class);
    }
}
