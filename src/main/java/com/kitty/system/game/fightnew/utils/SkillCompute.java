package com.kitty.system.game.fightnew.utils;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.Arrays;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class SkillCompute {
  private static final List<Integer> barrier = Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()) });
  
  private static final List<Integer> evade = Arrays.asList(new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()) });
  
  private static final List<Integer> pet_evade = PETSKILLATTR.evade();
  
  public static double computeSkillDamagePoison(double power, double kx, double fk, Integer skillExp, Integer round, Integer level, Integer maxHp, double extra, double weak, double wx) {
    level = Integer.valueOf((level.intValue() < 0) ? 1 : level.intValue());
    double damageBase = power * level.intValue() * (Math.pow(skillExp.intValue(), 0.4D) * 2.8853998118144273D / 100.0D + 1.0D);
    double damage = computeSkillDamage(damageBase, kx, fk, extra, weak, wx).intValue();
    if (round.intValue() > 1)
      damage = Math.min(damage, maxHp.intValue() * 0.5D); 
    damage = Math.max(damage, 1.0D);
    return damage;
  }
  
  public static Integer computeSkillDamagePoisonFollowUp(NutMap map) {
    int id = map.getInt("1005_power");
    double kdx = map.getDouble("1005_kdx");
    double weak = map.getDouble("1005_weak");
    double dqd = map.getDouble("1005_dqd");
    int proficient = map.getInt("1005_proficient");
    int round = map.getInt("1005_round") + 1;
    int level = map.getInt("1005_level");
    int maxLife = map.getInt("1005_maxLife");
    double extra = map.getDouble("1005_extra");
    double wx = map.getDouble("1005_wx");
    double hurt = computeSkillDamagePoison(id, kdx, dqd, Integer.valueOf(proficient), Integer.valueOf(round), Integer.valueOf(level), Integer.valueOf(maxLife), extra, weak, wx);
    return Integer.valueOf(Math.max(1, (int)hurt));
  }
  
  public static Integer computeSkillDamage(double damage, double kx, double fk, double extra, double weak, double wx) {
    damage *= 1.0D + fk * 3.0D - kx * 2.0D + extra;
    damage *= wx * (1.0D + weak);
    damage = Math.max(damage, 1.0D);
    return Integer.valueOf((int)damage);
  }
  
  public static Integer computePetSkillDamage(double damage, double kx, double fk, double extra, double weak, double wx) {
    damage *= 1.0D + fk * 3.0D - kx + extra;
    damage *= wx * (1.0D + weak);
    damage = Math.max(damage, 1.0D);
    return Integer.valueOf((int)damage);
  }
  
  public static Integer computeSkillDamage(int proficient, double power, int max, int curr, double kx, double fk, double extra, double weak, double wx) {
    if (curr <= 0)
      return Integer.valueOf(0); 
    double ratio = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(0.425D * max * 1.1D / curr + power * (Math.pow(proficient, 0.35D) * 2.0D / 100.0D + 1.0D))) / 100.0D;
    double damage = (ratio + fk - kx + extra) * curr;
    damage += damage * wx / 50.0D * (1.0D + weak);
    double hp = curr * 0.5D;
    if (damage > hp)
      damage = hp; 
    damage = Math.max(damage, 1.0D);
    return Integer.valueOf((int)damage);
  }
  
  public static Integer computeSkillLeech(int id, int damage, double extra) {
    int leech = 1;
    SysSkill skill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(id));
    if (skill != null)
      leech = (int)(damage * (skill.getPower()[1] + 2.0D * extra)); 
    return Integer.valueOf(Math.max(leech, 1));
  }
  
  public static Integer computeSkillDamageWildfire(FightObject effector, double[] power, int level, int exp, double kx, double fk, double extra, double weak, int die, double wx) {
    double coeff = 0.0D;
    if (die <= 0) {
      coeff = power[1];
    } else if (die == 1) {
      coeff = power[2];
    } else if (die == 2) {
      coeff = power[3];
    } else if (die == 3) {
      coeff = power[4];
    } else {
      coeff = power[5];
    } 
    double hurtBase = power[0] * level * (Math.pow(exp, 0.4D) * 2.8853998118144273D / 100.0D + 1.0D) * coeff;
    int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
    hurtBase += chageBaseHurt;
    if (effector.isPet())
      return computePetSkillDamage(hurtBase, kx, fk, extra, weak, wx); 
    return computeSkillDamage(hurtBase, kx, fk, extra, weak, wx);
  }
  
  public static Integer computeDragonSkillDamage(Fight fight, FightObject attacker, FightObject target, int damage, double conquer_extent, boolean penetrate, double extra, double weak) {
    double wlkang = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
    if (penetrate)
      wlkang -= conquer_extent; 
    damage = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damage * (1.0D - wlkang + extra)));
    double wx = SkillFunc.getWuXingKeZhiXiuZheng(attacker, target, fight.getType());
    damage = (int)(damage * wx * (1.0D + weak));
    if (target.getType() == 1)
      damage = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damage * Math.pow(0.5D, 0.99D))); 
    return Integer.valueOf(Math.max(damage, 1));
  }
  
  public static int computeAppendTarget(FightObject effector, int proficient, int status) {
    int count = 0;
    double down = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(0.1D + 18.0D * (1.0D + Math.pow(proficient, 0.35D) * 2.0D / 100.0D))) / 100.0D));
    double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.CHASE_RATIO.getDist()), 0.0D);
    if (propertyAdd != 0.0D)
      down += propertyAdd; 
    int num = 1 + (int)(Math.pow(proficient, 0.38D) * 8.0D / 100.0D);
    for (int i = 0; i < num; i++) {
      if (SkillAi.canAffectOnPro(down))
        count++; 
    } 
    if (status > 0)
      count++; 
    return count;
  }
  
  public static boolean computeSkillHit(Fight fight, FightObject attacker, FightObject target, RoleSkill skill) {
    double odds = 1.0D;
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
    double magicHit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
    if ((attacker.getLogic() == LOGIC.LOGICTYPE_HERO.getValue() || attacker.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) && barrier.contains(skill.getType())) {
      if (sysSkill != null) {
        String statusOdds = null;
        if (skill.getType().intValue() == SKILLATTR.SKILLATTR_CONFUSE.getValue()) {
          double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_HL.getDist());
          double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HL.getDist());
          double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HL.getDist());
          odds = mainParameter(skill.getProficient().intValue(), sysSkill.getHitRatio(), kx, hkx, extra);
          statusOdds = "1002odds";
        } else if (skill.getType().intValue() == SKILLATTR.SKILLATTR_SLEEP.getValue()) {
          double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_HS.getDist());
          double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HS.getDist());
          double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HS.getDist());
          odds = mainParameter(skill.getProficient().intValue(), sysSkill.getHitRatio(), kx, hkx, extra);
          statusOdds = "1004odds";
        } else if (skill.getType().intValue() == SKILLATTR.SKILLATTR_ICE.getValue()) {
          double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_FY.getDist());
          double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_FY.getDist());
          double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_FY.getDist());
          odds = mainParameter(skill.getProficient().intValue(), sysSkill.getHitRatio(), kx, hkx, extra);
          statusOdds = "1003odds";
        } else if (skill.getType().intValue() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
          double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_YW.getDist());
          double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_YW.getDist());
          double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_YW.getDist());
          odds = mainParameter(skill.getProficient().intValue(), sysSkill.getHitRatio(), kx, hkx, extra);
          statusOdds = "1006odds";
        } 
        odds += magicHit;
        target.getStatusMap().setv(statusOdds, Double.valueOf(odds));
      } 
    } else if (skill.getType().intValue() == SKILLATTR.SKILLATTR_HENGSAO.getValue() || skill.getType().intValue() == SKILLATTR.SKILLATTR_ZHENJI.getValue() || skill.getType().intValue() == SKILLATTR.SKILLATTR_POJIA.getValue()) {
      if (sysSkill != null) {
        double dodge = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
        double hit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
        odds = hit - dodge;
        odds += magicHit;
      } 
    } else if (!evade.contains(skill.getType()) && !pet_evade.contains(skill.getType())) {
      odds = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
      double hit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
      odds -= hit;
      odds += magicHit;
      if (odds <= 0.0D)
        return false; 
      return SkillAi.canAffectOnPro(odds);
    } 
    return !SkillAi.canAffectOnPro(odds);
  }
  
  public static double mainParameter(int proficient, double factor, double kx, double hkx, double extra) {
    double count = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf(factor + (Math.pow(proficient, 0.4D) * 4.0D / 100.0D + 1.0D) * 300.0D)) / 1000.0D;
    return BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf(count - kx + hkx + extra));
  }
  
  public static void main(String[] args) {
    double kx = 122.5D;
    System.out.println("抗性 = " + kx);
    double hs = 18.9D;
    System.out.println("忽视抗性 = " + hs);
    int factor = 62;
    System.out.println("系数 = " + factor);
    int extra = 0;
    System.out.println("增强 = " + extra);
    double v = mainParameter(800, factor, kx / 100.0D, hs / 100.0D, extra);
    System.out.println("命中率 = " + v);
    System.out.println("是否命中 = " + SkillAi.canAffectOnPro(v));
  }
}
