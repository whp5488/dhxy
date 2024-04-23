package com.kitty.system.common.data;

import com.kitty.cache.RedisService;
import com.kitty.common.model.Pos;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.enums.common.Mall;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.wardef.HERO;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.init.Monster;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysLevelExp;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.init.domain.vo.MonsterPower;
import com.kitty.system.init.domain.vo.Move;
import com.kitty.system.init.pool.ExpDataPool;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataGlobalFunc {
  public static boolean visual(int rolex, int roley, int npcx, int npcy, double range) {
    if (npcx <= 0 && npcy <= 0)
      return true; 
    double d2 = Math.hypot((npcx - rolex), (npcy - roley));
    if (d2 > range)
      return false; 
    if (d2 < range)
      return true; 
    return false;
  }
  
  public static long storageExp(int grade, int type) {
    long exp = 1500000000L;
    if (type == LOGIC.LOGICTYPE_HERO.getValue()) {
      if (grade >= 1120 && grade < 4000) {
        exp = 15000000000L;
      } else if (grade >= 4000) {
        exp = 30000000000L;
      } 
    } else if (type == LOGIC.LOGICTYPE_PET.getValue()) {
      if (grade >= 1180 && grade < 3000) {
        exp = 15000000000L;
      } else if (grade >= 3000) {
        exp = 30000000000L;
      } 
    } 
    return exp;
  }
  
  public static String[] getGuess(String string) {
    if (string.length() == 2) {
      String outer = string.substring(0, 1);
      String inner = string.substring(1, string.length());
      return new String[] { outer, inner };
    } 
    if (string.length() == 3) {
      String outer = string.substring(0, 2);
      String inner = string.substring(2, string.length());
      return new String[] { outer, inner };
    } 
    return null;
  }
  
  public static Integer GetObjType(Integer lTypeId) {
    if (lTypeId != null && lTypeId.intValue() > 0) {
      Integer rNum = Integer.valueOf(lTypeId.intValue() / 10000);
      return rNum;
    } 
    return Integer.valueOf(0);
  }
  
  public static Integer GetProType(int dis) {
    if (dis <= 0)
      return Integer.valueOf(PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()); 
    return Integer.valueOf(PRO_DICT_TYPE.getType(Integer.valueOf(dis % 10000)));
  }
  
  public static Integer GetProDictType(int dis) {
    if (dis <= 0)
      return Integer.valueOf(dis); 
    return Integer.valueOf(dis % 10000);
  }
  
  public static Integer getRoleReborn(int rei, int race, int sex) {
    if (rei >= 1 && rei <= 4) {
      Map<Integer, SysReiEn> map = PartDataPool.reiEnMap;
      for (Integer key : map.keySet()) {
        SysReiEn reiEn = map.get(key);
        if (reiEn.getRei() == rei && reiEn.getRace() == race && reiEn.getSex() == sex)
          return Integer.valueOf(reiEn.getSerial()); 
      } 
    } 
    return Integer.valueOf(-1);
  }
  
  public static boolean checkPetFly(Pet pet) {
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null)
      return false; 
    if (!sysPet.getLevelType().equals(Integer.valueOf(PETLEVELTYPE.Pet_LevelType_GJSH.getValue())) && !sysPet.getLevelType().equals(Integer.valueOf(PETLEVELTYPE.Pet_LevelType_TSSS.getValue())))
      return false; 
    return true;
  }
  
  public static int getPetFlyCount(int rei, int level) {
    int count = 0;
    int grade = rei * 1000 + level;
    if (grade >= 2120) {
      count = 3;
    } else if (grade >= 1100) {
      count = 2;
    } else if (grade >= 50) {
      count = 1;
    } 
    return count;
  }
  
  public static Integer getRace(Integer serial) {
    Integer value = Integer.valueOf(1);
    SysHero hero = (SysHero)PartDataPool.heroMap.get(serial);
    if (hero != null)
      value = Integer.valueOf(hero.getRace()); 
    return value;
  }
  
  public static List<Integer> getRoleCareerSerial(List<Integer> cable) {
    List<Integer> list = new ArrayList<>();
    if (cable != null) {
      Map<Integer, SysHero> data_hero = PartDataPool.heroMap;
      for (Integer key : data_hero.keySet()) {
        SysHero hero = data_hero.get(key);
        Integer race = Integer.valueOf(hero.getRace());
        Integer gender = Integer.valueOf(hero.getGender());
        if (gender.intValue() == HERO.HERO_MALE.getValue()) {
          if (race.intValue() == RACE.RACE_REN.getValue() && cable.contains(Integer.valueOf(1))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_XIAN.getValue() && cable.contains(Integer.valueOf(3))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_MO.getValue() && cable.contains(Integer.valueOf(5))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_GUI.getValue() && cable.contains(Integer.valueOf(7))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_LONG.getValue() && cable.contains(Integer.valueOf(9)))
            list.add(hero.getSerial()); 
          continue;
        } 
        if (gender.intValue() == HERO.HERO_FEMALE.getValue()) {
          if (race.intValue() == RACE.RACE_REN.getValue() && cable.contains(Integer.valueOf(2))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_XIAN.getValue() && cable.contains(Integer.valueOf(4))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_MO.getValue() && cable.contains(Integer.valueOf(6))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_GUI.getValue() && cable.contains(Integer.valueOf(8))) {
            list.add(hero.getSerial());
            continue;
          } 
          if (race.intValue() == RACE.RACE_LONG.getValue() && cable.contains(Integer.valueOf(10)))
            list.add(hero.getSerial()); 
        } 
      } 
      return list;
    } 
    return list;
  }
  
  public static Integer GetItemTypeByItemTypeId(Integer iTypeId) {
    if (iTypeId != null) {
      Integer num = Integer.valueOf(iTypeId.intValue() / 10000);
      if (num.intValue() > 100)
        return Integer.valueOf(num.intValue() / 100 * 100); 
      if (num.intValue() > 10)
        return Integer.valueOf(num.intValue() / 10 * 10); 
      return num;
    } 
    return null;
  }
  
  public static Integer getItemSubTypeByItemTypeId(Integer iTypeId) {
    if (iTypeId != null) {
      iTypeId = Integer.valueOf(iTypeId.intValue() % 10000);
      Integer num = Integer.valueOf(iTypeId.intValue() / 1000);
      return num;
    } 
    return null;
  }
  
  public static Integer getPetSkillBookTypeByItemTypeId(Integer iTypeId) {
    if (iTypeId != null) {
      iTypeId = Integer.valueOf(iTypeId.intValue() % 1000);
      Integer bookType = Integer.valueOf(iTypeId.intValue() / 100);
      return bookType;
    } 
    return null;
  }
  
  public static Map GetItemDataByItemTypeId(Integer iTypeId) {
    Integer type = GetItemTypeByItemTypeId(iTypeId);
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue())
      return ItemDataPool.data_drug; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue())
      return PetDataPool.petMap; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue())
      return ItemDataPool.data_gift_item; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue())
      return ItemDataPool.data_stuff; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue())
      return ItemDataPool.data_other_item; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_TASK.getValue())
      return ItemDataPool.data_task_item; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue())
      return EquipDataPool.newBieEq; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue())
      return EquipDataPool.seniorBieEq; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())
      return EquipDataPool.magicalBieEq; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue())
      return EquipDataPool.sinBieEq; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue())
      return ItemDataPool.acsryEggMap; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())
      return EquipDataPool.acsryMap; 
    type.intValue();
    ITEMLARGETYPE.ITEM_LARGE_TYPE_HUOBANEQPT.getValue();
    return null;
  }
  
  public static Map<Integer, SysSkill> getSkillListByAttr(Integer attr) {
    Map<Integer, SysSkill> map = new HashMap<>();
    Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
    for (Integer key : skillMap.keySet()) {
      SysSkill skill = skillMap.get(key);
      if (skill.getAttr().equals(attr))
        map.put(skill.getStep(), skill); 
    } 
    return map;
  }
  
  public static Message operationalConstraint(Role role, int value, String msg) {
    ServerService serverService = (ServerService)SpringUtils.getBean(ServerService.class);
    if (serverService.getType() <= 0)
      return null; 
    if (value > 0) {
      long gold = (value * serverService.getRatio());
      long total_gold = role.getTotalGold();
      if (total_gold < gold)
        return (Message)new RespMsg(String.format(msg, new Object[] { Integer.valueOf(value) })); 
    } 
    return null;
  }
  
  public static Map<String, Double> data_getMonsterKeZhiXiuZhengCoeff(boolean flag) {
    Map<String, Double> map = new HashMap<>();
    map.put("kzMonsterCoeff", Double.valueOf(1.0D));
    map.put("qkzMonsterCoeff", Double.valueOf(1.0D));
    if (flag) {
      map.put("kzMonsterCoeff", ((SysWxOtherWarData)FightDataPool.data_wx_other_war_data.get(Integer.valueOf(1))).getValue());
      map.put("qkzMonsterCoeff", ((SysWxOtherWarData)FightDataPool.data_wx_other_war_data.get(Integer.valueOf(2))).getValue());
    } 
    return map;
  }
  
  public static List<Integer> GetPlayerSkillId(Integer race, Integer sex) {
    List<Integer> list = new ArrayList<>();
    if (race.intValue() == RACE.RACE_REN.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()));
      } else {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_POISON.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()));
      } 
    } else if (race.intValue() == RACE.RACE_MO.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHEN.getValue()));
      } else {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHEN.getValue()));
      } 
    } else if (race.intValue() == RACE.RACE_XIAN.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_WIND.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_THUNDER.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_WATER.getValue()));
      } else {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_FIRE.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_THUNDER.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_WATER.getValue()));
      } 
    } else if (race.intValue() == RACE.RACE_GUI.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_XIXUE.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_AIHAO.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()));
      } else {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_SHUAIRUO.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_AIHAO.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()));
      } 
    } else if (race.intValue() == RACE.RACE_LONG.getValue()) {
      if (sex.intValue() == HERO.HERO_MALE.getValue()) {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_HENGSAO.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_POJIA.getValue()));
      } else {
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_HENGSAO.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()));
        list.add(Integer.valueOf(SKILLATTR.SKILLATTR_ZHENJI.getValue()));
      } 
      list.add(Integer.valueOf(SKILLATTR.SKILLATTR_NILIN.getValue()));
    } 
    return list;
  }
  
  public static Integer convertToGold(Integer coin, Integer silver, boolean flag) {
    Integer gold = Integer.valueOf(0);
    gold = Integer.valueOf(gold.intValue() + coin.intValue() / 10000);
    gold = Integer.valueOf(gold.intValue() + silver.intValue() / 100);
    if (flag && gold.intValue() != Math.floor(gold.intValue()))
      gold = Integer.valueOf(BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(Math.floor(gold.intValue()) + 1.0D))); 
    return gold;
  }
  
  public static long getMallItemGold(List<Integer> list) {
    if (list == null || list.size() <= 0)
      return -1L; 
    long gold = 0L;
    for (Integer serial : list) {
      long mallPrice = getMallPrice(serial.intValue(), 1);
      if (mallPrice <= 0L)
        return -1L; 
      gold += mallPrice;
    } 
    return gold;
  }
  
  public static long getMallPrice(int serial, int num) {
    Integer discount = Integer.valueOf(-1);
    double price = 0.0D;
    Map<Integer, SysShopOther> shopOtherMap = MallDataPool.data_shopother;
    SysShopOther shop = shopOtherMap.get(Integer.valueOf(serial));
    if (shop != null) {
      price = shop.getPrice();
      discount = shop.getDiscount();
    } 
    if (discount.intValue() <= 0) {
      Map<Integer, SysShopStrengthenMaterial> shopStrengthenMaterialMap = MallDataPool.data_shopStrengthenMaterial;
      SysShopStrengthenMaterial shopStrengthenMaterial = shopStrengthenMaterialMap.get(Integer.valueOf(serial));
      if (shopStrengthenMaterial != null) {
        price = shopStrengthenMaterial.getPrice();
        discount = shopStrengthenMaterial.getDiscount();
      } 
    } 
    if (discount.intValue() <= 0)
      discount = Integer.valueOf(100); 
    String priceMoney = String.valueOf((int)(price * num));
    Integer sum_price = Integer.valueOf(BigDecimalUtil.decimalScaleString(Integer.valueOf(0), BigDecimalUtil.multiply(priceMoney, BigDecimalUtil.divide(discount.toString(), "100"))));
    return sum_price.longValue();
  }
  
  public static int getBagGoodsNum(BagGoods bagGoods) {
    int num = 0;
    NutMap rests = bagGoods.getRests();
    for (String key : rests.keySet()) {
      if (rests.getInt(key) > 0)
        num += rests.getInt(key, 0); 
    } 
    return num;
  }
  
  public static int getCardChangeNum(CardChange cardChange) {
    int num = 0;
    NutMap rests = cardChange.getRests();
    for (String s : rests.keySet())
      num += rests.getInt(s); 
    return num;
  }
  
  public static int unrestricted(NutMap rests) {
    int num = 0;
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    for (String key : rests.keySet()) {
      if (String.valueOf(0).equals(key)) {
        num += rests.getInt(key, 0);
        continue;
      } 
      if (!String.valueOf(2).equals(key) && NumberUtil.isNumber(key)) {
        Integer time = Integer.valueOf(key);
        if (currTime > time.intValue())
          num += rests.getInt(key, 0); 
      } 
    } 
    return num;
  }
  
  public static int convertLevel(int lv) {
    int level = lv;
    if (lv > 100 && lv < 120) {
      level = 1000 + lv;
    } else if (lv > 120 && lv < 140) {
      level = 2000 + lv;
    } else if (lv > 140 && lv < 200) {
      level = 3000 + lv;
    } 
    return level;
  }
  
  public static String getColours(int level, boolean flag) {
    String colours = "W";
    if (flag && level == 1) {
      colours = "LG1";
    } else if (level == 2) {
      colours = "LG1";
    } else if (level == 3) {
      colours = "LB1";
    } else if (level == 4) {
      colours = "P";
    } else if (level == 5) {
      colours = "O";
    } 
    return colours;
  }
  
  public static boolean isTheSameParty(int factionA, int factionB) {
    if (factionA <= 0 || factionB <= 0)
      return false; 
    if (factionA != factionB)
      return false; 
    return true;
  }
  
  public static String needGradeJudge(int needRelive, int needLevel, int roleRelive, int roleLevel, String msg) {
    int need = needRelive * 1000 + needLevel;
    int curve = roleRelive * 1000 + roleLevel;
    if (curve < need) {
      if (needRelive > 0) {
        msg = msg + "#<R>" + needRelive + "转" + needLevel + "级#";
        return msg;
      } 
      msg = msg + "#<R>" + needLevel + "级#";
      return msg;
    } 
    return null;
  }
  
  public static long getNeedLimitLevelExp(int type, int rebirth, int level, int limit) {
    long exp = 0L;
    if (type == LOGIC.HORSE.getValue())
      exp = ExpDataPool.horseLevelExpMap.entrySet().stream().filter(e -> {
            boolean b;
            if (((Integer)e.getKey()).intValue() >= level && ((Integer)e.getKey()).intValue() <= limit) {
              b = true;
            } else {
              b = false;
            } 
            return b;
          }).mapToLong(value -> getNeedExp(rebirth, (SysLevelExp)value.getValue())).sum(); 
    return exp;
  }
  
  public static long getExpForRebirthLevel(int type, int rebirth, int level) {
    SysLevelExp levelExp = getLevelExp(type, level);
    if (levelExp == null)
      return 0L; 
    return getNeedExp(rebirth, levelExp);
  }
  
  public static long getNeedExp(int rebirth, SysLevelExp levelExp) {
    long needExp = 0L;
    switch (rebirth) {
      case 0:
        needExp = levelExp.getNeedExpRei0();
        return needExp;
      case 1:
        needExp = levelExp.getNeedExpRei1();
        return needExp;
      case 2:
        needExp = levelExp.getNeedExpRei2();
        return needExp;
      case 3:
        needExp = levelExp.getNeedExpRei3();
        return needExp;
      case 4:
        needExp = levelExp.getNeedExpRei4();
        return needExp;
    } 
    needExp = 0L;
    return needExp;
  }
  
  public static SysLevelExp getLevelExp(int type, int level) {
    SysLevelExp levelExp = null;
    switch (type) {
      case 1:
        levelExp = (SysLevelExp)ExpDataPool.heroLevelExpMap.get(Integer.valueOf(level));
        break;
      case 2:
        levelExp = (SysLevelExp)ExpDataPool.petLevelExpMap.get(Integer.valueOf(level));
        break;
      case 3:
        levelExp = (SysLevelExp)ExpDataPool.partnerLevelExpMap.get(Integer.valueOf(level));
        break;
      case 4:
        levelExp = (SysLevelExp)ExpDataPool.horseLevelExpMap.get(Integer.valueOf(level));
        break;
    } 
    return levelExp;
  }
  
  public static int getPj(int pj) {
    String value = String.valueOf(pj);
    if (value.length() >= 2)
      return Integer.valueOf(value.substring(1, 2)).intValue(); 
    return 0;
  }
  
  public static int getEquipScore(List<WearEquip> wearEquips) {
    int score = 0;
    for (WearEquip wearEquip : wearEquips) {
      FiedEquip fiedEquip = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
      if (fiedEquip != null)
        score += fiedEquip.getValue().intValue(); 
    } 
    return score;
  }
  
  public static int getEquipScore(WearEquip equip) {
    int score = 0;
    if (equip != null) {
      FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
      if (fiedEquip != null)
        score += fiedEquip.getValue().intValue(); 
    } 
    return score;
  }
  
  public static Integer getEquipInlayGemSerial(int serial, int pos, int curGemNum) {
    Integer type = GetItemTypeByItemTypeId(Integer.valueOf(serial));
    int sub = 7;
    if (type.intValue() >= ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue() && type.intValue() <= ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
      if (curGemNum < 3) {
        sub = 1;
      } else if (curGemNum < 6) {
        sub = 2;
      } else if (curGemNum < 9) {
        sub = 3;
      } else if (curGemNum < 12) {
        sub = 4;
      } else if (curGemNum < 15) {
        sub = 5;
      } else if (curGemNum < 18) {
        sub = 6;
      } else {
        sub = 7;
      } 
      return Integer.valueOf(70000 + pos * 100 + sub);
    } 
    return Integer.valueOf(70107);
  }
  
  public static Map<Integer, Integer> getCalculateGem(int pos, int num) {
    Map<Integer, Integer> map = new LinkedHashMap<>();
    int sub = 1;
    for (int i = 0; i < num; i++) {
      int serial = 70000 + pos * 100 + sub;
      Integer integer = map.get(Integer.valueOf(serial));
      if (integer == null) {
        integer = Integer.valueOf(0);
      } else if (integer.intValue() >= 3) {
        sub++;
        serial = 70000 + pos * 100 + sub;
        integer = Integer.valueOf(0);
      } 
      integer = Integer.valueOf(integer.intValue() + 1);
      map.put(Integer.valueOf(serial), integer);
    } 
    return map;
  }
  
  public static int gem2lowGemNum(int serial) {
    int level = serial % 100;
    int[] data = { 1, 6, 30, 120, 360, 720, 1440 };
    return data[level - 1];
  }
  
  public static int getGemNum(BagEquip equip) {
    int bn = 0;
    FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst().orElse(null);
    if (fiedEquip != null)
      bn = fiedEquip.getValue().intValue(); 
    return bn;
  }
  
  public static int getWeaponGemNum(List<WearEquip> wearEquips) {
    try {
      if (wearEquips.size() > 0) {
        WearEquip wearEquip = wearEquips.stream().filter(equip -> {
              boolean b;
              if (equip.getPosition() == ADORN.WEAPON.getValue()) {
                b = true;
              } else {
                b = false;
              } 
              return b;
            }).findFirst().orElse(null);
        if (wearEquip != null) {
          int maxGemNum = 20;
          int gemNum = getGemNum(wearEquip);
          if (gemNum >= maxGemNum) {
            List<WearEquip> collect = (List<WearEquip>)wearEquips.stream().filter(equip -> {
                  boolean b2;
                  if (equip.getPosition() > ADORN.WEAPON.getValue() && equip.getPosition() <= ADORN.SHOES.getValue()) {
                    b2 = true;
                  } else {
                    b2 = false;
                  } 
                  return b2;
                }).collect(Collectors.toList());
            int sumGemNum = gemNum;
            for (int i = 0; i < collect.size(); i++)
              sumGemNum += getGemNum(collect.get(i)); 
            if (sumGemNum >= 5 * maxGemNum)
              return 5 * maxGemNum; 
            return sumGemNum;
          } 
          return gemNum;
        } 
        return 0;
      } 
      return 0;
    } catch (Exception e) {
      return 0;
    } 
  }
  
  public static int getGemNum(WearEquip equip) {
    int bn = 0;
    Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue().intValue();
    } 
    return bn;
  }
  
  public static int getDur(List<FiedEquip> list) {
    int dur = 0;
    Optional<FiedEquip> first = list.stream().filter(e -> e.getType().equals(Integer.valueOf(6672))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      dur = fiedEquip.getValue().intValue();
    } 
    return dur;
  }
  
  public static EquipAlike getEquipData(Integer itemId) {
    EquipAlike alike = new EquipAlike();
    SysNewBieEq newBieEq = (SysNewBieEq)EquipDataPool.newBieEq.get(itemId);
    if (newBieEq != null) {
      BeanUtils.copyBeanProp(alike, newBieEq);
    } else if (EquipDataPool.seniorBieEq.get(itemId) != null) {
      SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(itemId);
      BeanUtils.copyBeanProp(alike, bieEq);
    } else if (EquipDataPool.magicalBieEq.get(itemId) != null) {
      SysMagicalBieEq bieEq2 = (SysMagicalBieEq)EquipDataPool.magicalBieEq.get(itemId);
      BeanUtils.copyBeanProp(alike, bieEq2);
    } else if (EquipDataPool.sinBieEq.get(itemId) != null) {
      SysSinBieEq bieEq3 = (SysSinBieEq)EquipDataPool.sinBieEq.get(itemId);
      BeanUtils.copyBeanProp(alike, bieEq3);
    } else if (EquipDataPool.acsryMap.get(itemId) != null) {
      SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(itemId);
      BeanUtils.copyBeanProp(alike, acsry);
    } 
    if (alike.getName() == null)
      return null; 
    return alike;
  }
  
  public static String getEquipDesc(int serial, int pj) {
    Integer type = GetItemTypeByItemTypeId(Integer.valueOf(serial));
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue())
      return ((SysNewBieEq)EquipDataPool.newBieEq.get(Integer.valueOf(serial))).getDesc(); 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue())
      return ((SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(serial))).getDesc(); 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())
      return pj + "级神兵"; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue())
      return pj + "阶仙器"; 
    if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())
      return ((SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(serial))).getName(); 
    return null;
  }
  
  public static int randomEquip(int type, int pj, int race) {
    if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
      List<SysSinBieEq> collect = (List<SysSinBieEq>)EquipDataPool.sinBieEq.values().stream().filter(e -> {
            boolean b;
            if (getPj(e.getSerial()) == pj) {
              b = true;
            } else {
              b = false;
            } 
            return b;
          }).collect(Collectors.toList());
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
      return ((SysSinBieEq)collect.get(index)).getSerial();
    } 
    if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
      List<SysMagicalBieEq> collect2 = (List<SysMagicalBieEq>)EquipDataPool.magicalBieEq.values().stream().filter(e -> {
            boolean b2;
            if (getPj(e.getSerial()) == pj) {
              b2 = true;
            } else {
              b2 = false;
            } 
            return b2;
          }).collect(Collectors.toList());
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect2.size() - 1));
      return ((SysMagicalBieEq)collect2.get(index)).getSerial();
    } 
    if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue())
      return randomAcsryEgg(race); 
    return -1;
  }
  
  public static int randomAcsryEgg(int race) {
    List<SysDataAcsry> list = (List<SysDataAcsry>)EquipDataPool.acsryMap.values().stream().filter(e -> {
          boolean b;
          if (e.getOwnerRole().contains(Integer.valueOf(race)) || e.getOwnerRole().contains(Integer.valueOf(0))) {
            b = true;
          } else {
            b = false;
          } 
          return b;
        }).collect(Collectors.toList());
    if (list.size() > 0) {
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
      SysDataAcsry acsry = list.get(index);
      SysDataAcsryEgg acsryEgg = ItemDataPool.acsryEggMap.values().stream().filter(e -> {
            boolean b2;
            if (e.getHatchType() == acsry.getSerial()) {
              b2 = true;
            } else {
              b2 = false;
            } 
            return b2;
          }).findFirst().orElse(null);
      if (acsryEgg != null)
        return acsryEgg.getSerial(); 
    } 
    return -1;
  }
  
  public static boolean isInMap(Role role, int mapId) {
    if (role == null)
      return false; 
    return role.getPos().getMapId().equals(Integer.valueOf(mapId));
  }
  
  public static ItemEasyInfo getItemEasyInfo(Integer serial) {
    ItemEasyInfo easy = null;
    try {
      Integer type = GetItemTypeByItemTypeId(serial);
      if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue()) {
        SysOtherItem item = (SysOtherItem)ItemDataPool.data_other_item.get(serial);
        easy = new ItemEasyInfo();
        BeanUtils.copyBeanProp(easy, item);
        easy.setCategory(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue()));
      } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue()) {
        SysDataStuff item2 = (SysDataStuff)ItemDataPool.data_stuff.get(serial);
        easy = new ItemEasyInfo();
        BeanUtils.copyBeanProp(easy, item2);
        easy.setCategory(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue()));
      } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
        SysGiftItem item3 = (SysGiftItem)ItemDataPool.data_gift_item.get(serial);
        easy = new ItemEasyInfo();
        BeanUtils.copyBeanProp(easy, item3);
        easy.setCategory(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()));
      } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue()) {
        SysDrug item4 = (SysDrug)ItemDataPool.data_drug.get(serial);
        easy = new ItemEasyInfo();
        BeanUtils.copyBeanProp(easy, item4);
        easy.setCategory(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_DRUG.getValue()));
      } else if (type.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()) {
        SysDataAcsryEgg acsryEgg = (SysDataAcsryEgg)ItemDataPool.acsryEggMap.get(serial);
        easy = new ItemEasyInfo();
        BeanUtils.copyBeanProp(easy, acsryEgg);
        easy.setCategory(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()));
      } 
    } catch (Exception e) {
      System.out.println("获取道具详情错误编号为： = " + serial);
      return null;
    } 
    return easy;
  }
  
  public static List<NutMap> setMsg() {
    String[] arr = { "嘻嘻，看我的~~", "嘻嘻，看我的~~", "欺负我个头小是吗？", "", "" };
    List<NutMap> list = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      NutMap map = new NutMap();
      map.put("type", Integer.valueOf(i));
      map.put("msg", arr[i]);
      list.add(map);
    } 
    return list;
  }
  
  public static Map<Integer, Map<Integer, Integer>> setQuota() {
    Map<Integer, Integer> value = new HashMap<>();
    Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
    List<Integer> list = Mall.toList();
    for (Integer key : list)
      map.put(key, value); 
    return map;
  }
  
  public static Map<Integer, Integer> faction() {
    Map<Integer, Integer> map = new HashMap<>();
    List<Integer> list = FACTION_PRO_DICT.toList();
    for (Integer key : list)
      map.put(key, Integer.valueOf(0)); 
    return map;
  }
  
  public static Map<String, Integer> setting() {
    Map<String, Integer> map = new HashMap<>();
    map.put("else_auto_fight", Integer.valueOf(1));
    map.put("else_db_speed_show", Integer.valueOf(0));
    return map;
  }
  
  public static Map<String, Integer> closeLimit() {
    Map<String, Integer> map = new HashMap<>();
    map.put("close_dan", Integer.valueOf(0));
    map.put("close_fight", Integer.valueOf(0));
    return map;
  }
  
  public static NutMap newDistrict() {
    NutMap nutMap = new NutMap();
    for (Byte key : WelfareDataPool.newDistrictMap.keySet())
      nutMap.setv(String.valueOf(key), Integer.valueOf(0)); 
    return nutMap;
  }
  
  public static boolean seniorIsGem(SysSeniorBieEq bieEq, BagEquip equip) {
    boolean flag = true;
    if (bieEq.getNeedGrade() >= 120) {
      Map<String, SysAboveAttrLib> aboveMap = EquipDataPool.above_attr_lib;
      String baseAttr = bieEq.getBaseAttr();
      Map<Integer, Double> fieldMap = new HashMap<>();
      String[] split;
      for (String dist : split = baseAttr.split(",")) {
        SysAboveAttrLib lib = aboveMap.get(dist);
        if (lib != null) {
          String base = lib.getBaseAttr();
          if (base != null && 
            !base.isEmpty()) {
            String[] bases = base.split(";");
            if (bases.length > 0) {
              Random random = new Random();
              String curBase = bases[random.nextInt(bases.length)];
              String[] curBaseArr = curBase.split(",");
              if (curBaseArr.length == 3) {
                String curKey = curBaseArr[0];
                int curMaxValue = Integer.valueOf(curBaseArr[2]).intValue();
                int pro = CONVERT_DICT.getDist(curKey);
                if (pro > 0)
                  fieldMap.put(Integer.valueOf(pro + PRO_DICT_SERIAL.EQUIP_PRO.getSerial()), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(curMaxValue * 0.92D)))); 
              } 
            } 
          } 
        } 
      } 
      List<FiedEquip> fields = equip.getField();
      for (FiedEquip field : fields) {
        if (field.getType().intValue() > PRO_DICT_SERIAL.EQUIP_PRO.getSerial() && field != null) {
          Double val = fieldMap.get(field.getType());
          if (field.getValue() == null) {
            log.error("装备编号：{}, 属性编号：{}", equip.getId(), field.getType());
            continue;
          } 
          if (val == null) {
            flag = false;
            break;
          } 
          if (field.getValue().doubleValue() < val.doubleValue()) {
            flag = false;
            break;
          } 
        } 
      } 
    } 
    return flag;
  }
  
  public static Integer revolution(Integer str) {
    String value = String.valueOf(str);
    if (value.length() < 2)
      return Integer.valueOf(0); 
    value = value.substring(0, value.length() - 1);
    return Integer.valueOf(value + "0");
  }
  
  public static int proficiency(int level) {
    int lowLv = Math.min(level, 100);
    int highLv = Math.min(Math.max(level - 100, 0), 60);
    int limit = (int)(Math.ceil((lowLv / 10)) * 1000.0D + Math.ceil((highLv / 10)) * 2500.0D);
    return limit;
  }
  
  public static SysDataMonster getMainMonster(Integer serial) {
    SysWarRole sysWarRole = (SysWarRole)FightDataPool.data_war_role.get(serial);
    if (sysWarRole != null) {
      Monster monster = sysWarRole.getMonster().stream().filter(e -> e.getPos().equals(Integer.valueOf(10103))).findFirst().orElse(null);
      if (monster == null) {
        monster = sysWarRole.getMonster().stream().filter(e -> e.getPos().equals(Integer.valueOf(10003))).findFirst().orElse(null);
        if (monster == null)
          monster = sysWarRole.getMonster().get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(sysWarRole.getMonster().size() - 1))); 
      } 
      if (monster != null)
        return (SysDataMonster)FightDataPool.data_monster.get(monster.getSerial()); 
    } 
    return null;
  }
  
  public static SysDataMonster createMonster(int grade, SysDataMonster obj) {
    int b, rei = grade / 1000;
    int level = grade % 1000;
    int a = 1;
    if (level < 100) {
      b = level * 100;
    } else {
      b = 10000 + (level - 100) * 250;
    } 
    Integer ext = Integer.valueOf(Math.max(a, b));
    double rdk = RandomUtil.getRandomDouble(Double.valueOf(0.0D), Double.valueOf(0.1D));
    obj.setGrade(Integer.valueOf(level));
    obj.setJnSld(ext);
    obj.setHp(Integer.valueOf(obj.getHp().intValue() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((level + (2000 - level * 20) / 100) * obj.getPowerHp().getA() * (Math.pow(level, obj.getPowerHp().getB()) + rei * obj.getPowerHp().getC())))));
    obj.setAp(Integer.valueOf(obj.getAp().intValue() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(((level + (2000 - level * 20) / 100) / 5) * obj.getPowerAp().getA() * (Math.pow(level, obj.getPowerAp().getB()) + rei * obj.getPowerAp().getC())))));
    obj.setSp(Integer.valueOf(obj.getSp().intValue() + level * 5 * BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(obj.getPowerSp().getA() * (Math.pow(level, obj.getPowerSp().getB()) + rei * obj.getPowerSp().getC())))));
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KJ.getDist())) != null)
      obj.setConGold(createMonsterAttr(obj.getConGold(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KJ.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KM.getDist())) != null)
      obj.setConWood(createMonsterAttr(obj.getConWood(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KM.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KS.getDist())) != null)
      obj.setConWater(createMonsterAttr(obj.getConWater(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KH.getDist())) != null)
      obj.setConFire(createMonsterAttr(obj.getConFire(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KH.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KT.getDist())) != null)
      obj.setConSoil(createMonsterAttr(obj.getConSoil(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.Q_KT.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist())) != null)
      obj.setPdeFend(createMonsterAttr(obj.getPdeFend(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.WL_XS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist())) != null)
      obj.setPPofang(createMonsterAttr(obj.getPPofang(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist())) != null)
      obj.setPPofangPro(createMonsterAttr(obj.getPPofangPro(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.CONQUER_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist())) != null)
      obj.setPFanZhen(createMonsterAttr(obj.getPFanZhen(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist())) != null)
      obj.setPFanZhenPro(createMonsterAttr(obj.getPFanZhenPro(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SHOCK_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist())) != null)
      obj.setPKuangBao(createMonsterAttr(obj.getPKuangBao(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RAGE_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())) != null)
      obj.setPLjTimes(Integer.valueOf((int)createMonsterAttr(obj.getPLjTimes().intValue(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist()))))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist())) != null)
      obj.setPLjPro(createMonsterAttr(obj.getPLjPro(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist())) != null)
      obj.setPAcc(createMonsterAttr(obj.getPAcc(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist())) != null)
      obj.setPSbl(createMonsterAttr(obj.getPSbl(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist())) != null)
      obj.setShuiOdd(createMonsterAttr(obj.getShuiOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist())) != null)
      obj.setHuoOdd(createMonsterAttr(obj.getHuoOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.H_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist())) != null)
      obj.setFengOdd(createMonsterAttr(obj.getFengOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.F_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist())) != null)
      obj.setLeiOdd(createMonsterAttr(obj.getLeiOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.L_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist())) != null)
      obj.setGuiHuoOdd(createMonsterAttr(obj.getGuiHuoOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.GH_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist())) != null)
      obj.setSanShiOdd(createMonsterAttr(obj.getSanShiOdd(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.SS_KB_RATIO.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_S.getDist())) != null)
      obj.setRainEhan(createMonsterAttr(obj.getRainEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_S.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_H.getDist())) != null)
      obj.setFireEhan(createMonsterAttr(obj.getFireEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_H.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_F.getDist())) != null)
      obj.setWindEhan(createMonsterAttr(obj.getWindEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_F.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_L.getDist())) != null)
      obj.setThunderEhan(createMonsterAttr(obj.getThunderEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_L.getDist())))); 
    obj.setRecoverEhan(obj.getRecoverEhan());
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_SS.getDist())) != null)
      obj.setSanshiEhan(createMonsterAttr(obj.getSanshiEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_SS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist())) != null)
      obj.setBreakEhan(createMonsterAttr(obj.getBreakEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_PJ.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist())) != null)
      obj.setAdAtkEhan(createMonsterAttr(obj.getAdAtkEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ATTACK.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist())) != null)
      obj.setAdDefEhan(createMonsterAttr(obj.getAdDefEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_DEF.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist())) != null)
      obj.setAdSpdEhan(createMonsterAttr(obj.getAdSpdEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist())) != null)
      obj.setSweepEhan(createMonsterAttr(obj.getSweepEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_QHS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist())) != null)
      obj.setThudEhan(createMonsterAttr(obj.getThudEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZJ.getDist())))); 
    obj.setDisorderEhan(obj.getDisorderEhan());
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_HL.getDist())) != null)
      obj.setDisorderEhan(createMonsterAttr(obj.getDisorderEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_HL.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_FY.getDist())) != null)
      obj.setSealEhan(createMonsterAttr(obj.getSealEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_FY.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_D.getDist())) != null)
      obj.setPoisonEhan(createMonsterAttr(obj.getPoisonEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_D.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_HS.getDist())) != null)
      obj.setSleepEhan(createMonsterAttr(obj.getSleepEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_HS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_YW.getDist())) != null)
      obj.setForgetEhan(createMonsterAttr(obj.getForgetEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_YW.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_MH.getDist())) != null)
      obj.setCharmEhan(createMonsterAttr(obj.getCharmEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_MH.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist())) != null)
      obj.setCureEhan(createMonsterAttr(obj.getCureEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZY.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist())) != null)
      obj.setShockEhan(createMonsterAttr(obj.getShockEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_ZS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_GH.getDist())) != null)
      obj.setGfireEhan(createMonsterAttr(obj.getGfireEhan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.RISE_GH.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_S.getDist())) != null)
      obj.setKShui(createMonsterAttr(obj.getKShui(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_S.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_H.getDist())) != null)
      obj.setKHuo(createMonsterAttr(obj.getKHuo(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_H.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_F.getDist())) != null)
      obj.setKFeng(createMonsterAttr(obj.getKFeng(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_F.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_L.getDist())) != null)
      obj.setKLei(createMonsterAttr(obj.getKLei(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_L.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_HL.getDist())) != null)
      obj.setKHunLuan(createMonsterAttr(obj.getKHunLuan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_HL.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_FY.getDist())) != null)
      obj.setKFengYin(createMonsterAttr(obj.getKFengYin(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_FY.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_HS.getDist())) != null)
      obj.setKHunShui(createMonsterAttr(obj.getKHunShui(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_HS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_YW.getDist())) != null)
      obj.setKYiWang(createMonsterAttr(obj.getKYiWang(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_YW.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_DU.getDist())) != null)
      obj.setKZhongDu(createMonsterAttr(obj.getKZhongDu(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_DU.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_ZS.getDist())) != null)
      obj.setKZhenShe(createMonsterAttr(obj.getKZhenShe(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_ZS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_GH.getDist())) != null)
      obj.setKGuiHuo(createMonsterAttr(obj.getKGuiHuo(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_GH.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_SS.getDist())) != null)
      obj.setKSanShi(createMonsterAttr(obj.getKSanShi(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.K_SS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist())) != null)
      obj.setFkShui(createMonsterAttr(obj.getFkShui(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_S.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist())) != null)
      obj.setFkHuo(createMonsterAttr(obj.getFkHuo(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_H.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist())) != null)
      obj.setFkFeng(createMonsterAttr(obj.getFkFeng(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_F.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist())) != null)
      obj.setFkLei(createMonsterAttr(obj.getFkLei(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_L.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist())) != null)
      obj.setFkHunLuan(createMonsterAttr(obj.getFkHunLuan(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_HL.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist())) != null)
      obj.setFkZhenShe(createMonsterAttr(obj.getFkZhenShe(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_ZS.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist())) != null)
      obj.setFkGuiHuo(createMonsterAttr(obj.getFkGuiHuo(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_GH.getDist())))); 
    if (obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist())) != null)
      obj.setFkSanShi(createMonsterAttr(obj.getFkSanShi(), rei, level, (MonsterPower)obj.getPowerAttr().get(Integer.valueOf(PRO_DICT_TYPE.OMIT_K_SS.getDist())))); 
    obj.setFsGjGl(1.0D);
    return obj;
  }
  
  public static double createMonsterAttr(double base, int rei, int level, MonsterPower obj) {
    return base + Math.pow(level, obj.getA()) * obj.getB() + rei * obj.getC();
  }
  
  public static Integer getFormula(Role role, Integer key) {
    int formula = key.intValue();
    SysDataTaskFormula taskFormula = (SysDataTaskFormula)TaskDataPool.taskFormulaMap.get(key);
    if (taskFormula != null) {
      FiedValue fiedValue = taskFormula.getContent().stream().filter(e -> e.getType().equals(String.valueOf(role.getRace()))).findFirst().orElse(null);
      if (fiedValue != null)
        formula = Integer.valueOf(String.valueOf(fiedValue.getValue())).intValue(); 
    } 
    return Integer.valueOf(formula);
  }
  
  public static Pos getTreasureMap() {
    Pos pos = new Pos(Integer.valueOf(136), Integer.valueOf(50), Integer.valueOf(0), Integer.valueOf(1010));
    try {
      Random random = new Random();
      List<Integer> can_move_map = PartDataPool.can_move_map;
      int index = random.nextInt(can_move_map.size());
      Integer serial = can_move_map.get(index);
      Move move = (Move)((RedisService)SpringUtils.getBean(RedisService.class)).get("move_" + serial, Move.class);
      if (move != null) {
        String str = move.getList().get(random.nextInt(move.getList().size()));
        String[] split = str.split(",");
        if (split.length > 1) {
          pos.setX(Integer.valueOf(split[0]));
          pos.setY(Integer.valueOf(split[1]));
          pos.setMapId(serial);
        } 
        return pos;
      } 
    } catch (Exception e) {
      log.error("随机获取一个藏宝图位置错误");
    } 
    return pos;
  }
  
  public static Npc createNpc(Role role, int serial, int type) {
    try {
      SysDataTaskNpc taskNpc = (SysDataTaskNpc)TaskDataPool.taskNpcMap.get(Integer.valueOf(serial));
      if (taskNpc != null) {
        int shape = taskNpc.getShape();
        String name = dynamicName(taskNpc.getName());
        if (taskNpc.getShapeType() <= 1)
          shape = getFormula(role, Integer.valueOf(taskNpc.getShape())).intValue(); 
        Pos pos = null;
        if (taskNpc.getScene() <= 0) {
          pos = getTreasureMap();
        } else {
          pos = getTreasureMap(taskNpc.getScene());
          if (taskNpc.getX() >= 0 && taskNpc.getY() >= 0) {
            pos = new Pos();
            pos.setX(Integer.valueOf(taskNpc.getX()));
            pos.setY(Integer.valueOf(taskNpc.getY()));
            pos.setMapId(Integer.valueOf(taskNpc.getScene()));
          } 
        } 
        if (pos == null)
          return null; 
        int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
        Npc npc = new Npc(id, role.getRoleId().intValue(), pos.getMapId().intValue(), pos.getX().intValue(), pos.getY().intValue(), shape, name, "", serial, type);
        return npc;
      } 
      return null;
    } catch (Exception e) {
      System.out.println("任务创建NPC = " + serial + ", type = " + type);
      e.printStackTrace();
      return null;
    } 
  }
  
  public static String dynamicName(String name) {
    if (name.contains("星名"))
      name = "RandomStarOfficerName"; 
    if (name.contains("Rand")) {
      SysDataTaskNpcNameType nameType = (SysDataTaskNpcNameType)TaskDataPool.taskNpcNameTypeMap.get(name);
      if (nameType != null) {
        name = "";
        Random random = new Random();
        List<SysDataTaskNpcName> list = TaskDataPool.taskNpcNameList;
        for (String str : nameType.getValue()) {
          List<SysDataTaskNpcName> collect = (List<SysDataTaskNpcName>)list.stream().filter(e -> e.getKey().equals(str)).collect(Collectors.toList());
          int index = random.nextInt(collect.size());
          name = name + ((SysDataTaskNpcName)collect.get(index)).getName();
        } 
      } else {
        name = "Random";
      } 
    } else if (StringUtils.isNumeric(name)) {
      name = "数字";
    } 
    return name;
  }
  
  public static Pos getTreasureMap(int scene) {
    try {
      List<Integer> can_move_map = PartDataPool.can_move_map;
      Integer serial = can_move_map.stream().filter(e -> e.equals(Integer.valueOf(scene))).findFirst().orElse(null);
      if (serial != null) {
        Move move = (Move)((RedisService)SpringUtils.getBean(RedisService.class)).get("move_" + serial, Move.class);
        if (move != null) {
          Random random = new Random();
          String str = move.getList().get(random.nextInt(move.getList().size()));
          String[] split = str.split(",");
          if (split.length > 1) {
            Pos pos = new Pos();
            pos.setX(Integer.valueOf(split[0]));
            pos.setY(Integer.valueOf(split[1]));
            pos.setMapId(serial);
            return pos;
          } 
        } 
      } 
      return null;
    } catch (Exception e) {
      log.error("根据地图编号随机获取一个位置错误：{}", Integer.valueOf(scene));
      return null;
    } 
  }
  
  private static final Logger log = LoggerFactory.getLogger(DataGlobalFunc.class);
}
