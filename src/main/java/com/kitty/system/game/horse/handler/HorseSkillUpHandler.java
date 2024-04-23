package com.kitty.system.game.horse.handler;

import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import java.util.IdentityHashMap;
import org.nutz.lang.util.NutMap;
import org.springframework.stereotype.Component;

@Component("HorseSkillUpHandler")
public class HorseSkillUpHandler extends FeedHorseHandler {
  private static final long EXP_VALUE = 500L;
  
  private static final int SERIAL_DSMQ = 75044;
  
  private static final int SERIAL_LGSM = 75195;
  
  private static final String LACK = "{0}数量不足";
  
  private static final String SUCCESS = "消耗{0}X{1}，#<IR" + Const.RESTYPE_COIN + ">#{2}，熟练度提升{3}";
  
  public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
    int index = Integer.valueOf(String.valueOf(args[0])).intValue() - 1;
    if (index < 0 || index > 2)
      return null; 
    IdentityHashMap<Integer, Long> map = horse.getSkill();
    Integer[] keys = (Integer[])map.keySet().toArray((Object[])new Integer[0]);
    Integer skill = keys[index];
    long exp = getSkillExp(map, skill, index);
    if (exp < 0L)
      return null; 
    long limit = getProficiency(horse.getLevel());
    if (exp >= limit)
      return "技能熟练达到当前等级限制"; 
    int serial = 75044;
    int num = 2;
    String name = "丹书密卷";
    NutMap consume = consume(exp);
    long coin = consume.getLong("coin", 0L);
    if (coin <= 0L)
      return null; 
    num = consume.getInt("ds", 0);
    if (operation == 2) {
      name = "流光碎溟";
      serial = 75195;
      num = consume.getInt("lg", 0);
    } 
    if (num <= 0 || this.bagService.getBagGoods(role, serial, num) != null)
      return MessageFormat.format("{0}数量不足", new Object[] { name }); 
    boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_COIN, coin);
    if (!b)
      return null; 
    Integer integer = this.bagService.consumeBagGoods(role, serial, num, true, true, "坐骑技能升级");
    if (integer != null)
      return MessageFormat.format("{0}数量不足", new Object[] { name }); 
    boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, coin, "HorseSkillUpHandler");
    if (!currency) {
      this.logger.error("坐骑技能升级扣除货币失败，玩家编号 = {}", role.getRoleId());
      return null;
    } 
    role.save();
    int i = 0;
    for (Integer key : map.keySet()) {
      if (i == index && key.equals(skill)) {
        exp += 500L;
        map.put(skill, Long.valueOf((exp > limit) ? limit : exp));
        break;
      } 
      i++;
    } 
    horse.save();
    this.horseService.refresh(role, horse);
    return MessageFormat.format(SUCCESS, new Object[] { name, Integer.valueOf(num), MoneyUtil.colours(Long.valueOf(coin)), Long.valueOf(500L) });
  }
  
  public static long getSkillExp(IdentityHashMap<Integer, Long> map, Integer serial, int index) {
    long exp = -1L;
    int i = 0;
    for (Integer key : map.keySet()) {
      if (key.equals(serial) && i == index) {
        exp = ((Long)map.get(key)).longValue();
        break;
      } 
      i++;
    } 
    return exp;
  }
  
  public static long getProficiency(short level) {
    if (level <= 10)
      return 10000L; 
    if (level > 10 && level <= 20)
      return 20000L; 
    if (level > 20 && level <= 30)
      return 30000L; 
    if (level > 30 && level <= 40)
      return 40000L; 
    if (level > 40 && level <= 50)
      return 50000L; 
    if (level > 50 && level <= 60)
      return 60000L; 
    if (level > 60 && level <= 70)
      return 70000L; 
    if (level > 70 && level <= 80)
      return 80000L; 
    if (level > 80 && level <= 90)
      return 90000L; 
    if (level > 90 && level <= 100)
      return 100000L; 
    if (level > 100 && level <= 110)
      return 110000L; 
    if (level > 110 && level <= 120)
      return 120000L; 
    if (level > 120 && level <= 130)
      return 130000L; 
    return -1L;
  }
  
  public static NutMap consume(long range) {
    NutMap nutMap = new NutMap();
    int count_dsmj = 0;
    int count_lgsm = 0;
    if (range >= 0L && range <= 9999L) {
      count_dsmj = 2;
      count_lgsm = 2;
    } else if (range >= 10000L && range <= 19999L) {
      count_dsmj = 4;
      count_lgsm = 4;
    } else if (range >= 20000L && range <= 29999L) {
      count_dsmj = 4;
      count_lgsm = 4;
    } else if (range >= 30000L && range <= 39999L) {
      count_dsmj = 6;
      count_lgsm = 6;
    } else if (range >= 40000L && range <= 49999L) {
      count_dsmj = 9;
      count_lgsm = 9;
    } else if (range >= 50000L && range <= 59999L) {
      count_dsmj = 12;
      count_lgsm = 12;
    } else if (range >= 60000L && range <= 69999L) {
      count_dsmj = 18;
      count_lgsm = 18;
    } else if (range >= 70000L && range <= 79999L) {
      count_dsmj = 24;
      count_lgsm = 24;
    } else if (range >= 70000L && range <= 79999L) {
      count_dsmj = 24;
      count_lgsm = 24;
    } else if (range >= 80000L && range <= 89999L) {
      count_dsmj = 28;
    } else if (range >= 90000L && range <= 99999L) {
      count_dsmj = 35;
    } else if (range >= 100000L && range <= 109999L) {
      count_dsmj = 42;
    } else if (range >= 110000L && range <= 119999L) {
      count_dsmj = 50;
    } else if (range >= 120000L && range <= 130000L) {
      count_dsmj = 65;
    } 
    int coin = (int)(7510.0D + 19.0D * Math.pow((range / 500L), 2.0D) + (range / 500L * 1392L));
    nutMap.setv("coin", Integer.valueOf(coin));
    nutMap.setv("ds", Integer.valueOf(count_dsmj));
    nutMap.setv("lg", Integer.valueOf(count_lgsm));
    return nutMap;
  }
}
