
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import java.util.Arrays;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.common.utils.BigDecimalUtil;
import org.nutz.lang.util.NutMap;
import java.util.List;

public class SkillCompute
{
    private static final List<Integer> barrier;
    private static final List<Integer> evade;
    private static final List<Integer> pet_evade;
    
    public static double computeSkillDamagePoison(final double power, final double kx, final double fk, final Integer skillExp, final Integer round, Integer level, final Integer maxHp, final double extra, final double weak, final double wx) {
        level = ((level < 0) ? 1 : level);
        final double damageBase = power * level * (Math.pow(skillExp, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
        double damage = computeSkillDamage(damageBase, kx, fk, extra, weak, wx);
        if (round > 1) {
            damage = Math.min(damage, maxHp * 0.5);
        }
        damage = Math.max(damage, 1.0);
        return damage;
    }
    
    public static Integer computeSkillDamagePoisonFollowUp(final NutMap map) {
        final int id = map.getInt("1005_power");
        final double kdx = map.getDouble("1005_kdx");
        final double weak = map.getDouble("1005_weak");
        final double dqd = map.getDouble("1005_dqd");
        final int proficient = map.getInt("1005_proficient");
        final int round = map.getInt("1005_round") + 1;
        final int level = map.getInt("1005_level");
        final int maxLife = map.getInt("1005_maxLife");
        final double extra = map.getDouble("1005_extra");
        final double wx = map.getDouble("1005_wx");
        final double hurt = computeSkillDamagePoison(id, kdx, dqd, proficient, round, level, maxLife, extra, weak, wx);
        return Math.max(1, (int)hurt);
    }
    
    public static Integer computeSkillDamage(double damage, final double kx, final double fk, final double extra, final double weak, final double wx) {
        damage *= 1.0 + fk * 3.0 - kx * 2.0 + extra;
        damage *= wx * (1.0 + weak);
        damage = Math.max(damage, 1.0);
        return (int)damage;
    }
    
    public static Integer computePetSkillDamage(double damage, final double kx, final double fk, final double extra, final double weak, final double wx) {
        damage *= 1.0 + fk * 3.0 - kx + extra;
        damage *= wx * (1.0 + weak);
        damage = Math.max(damage, 1.0);
        return (int)damage;
    }
    
    public static Integer computeSkillDamage(final int proficient, final double power, final int max, final int curr, final double kx, final double fk, final double extra, final double weak, final double wx) {
        if (curr <= 0) {
            return 0;
        }
        final double ratio = BigDecimalUtil.decimalScaleObjectDown(2, 0.425 * (max * 1.1 / curr) + power * (Math.pow(proficient, 0.35) * 2.0 / 100.0 + 1.0)) / 100.0;
        double damage = (ratio + fk - kx + extra) * curr;
        damage += damage * (wx / 50.0 * (1.0 + weak));
        final double hp = curr * 0.5;
        if (damage > hp) {
            damage = hp;
        }
        damage = Math.max(damage, 1.0);
        return (int)damage;
    }
    
    public static Integer computeSkillLeech(final int id, final int damage, final double extra) {
        int leech = 1;
        final SysSkill skill = SkillDataPool.skillMap.get(id);
        if (skill != null) {
            leech = (int)(damage * (skill.getPower()[1] + 2.0 * extra));
        }
        return Math.max(leech, 1);
    }
    
    public static Integer computeSkillDamageWildfire(final FightObject effector, final double[] power, final int level, final int exp, final double kx, final double fk, final double extra, final double weak, final int die, final double wx) {
        double coeff = 0.0;
        if (die <= 0) {
            coeff = power[1];
        }
        else if (die == 1) {
            coeff = power[2];
        }
        else if (die == 2) {
            coeff = power[3];
        }
        else if (die == 3) {
            coeff = power[4];
        }
        else {
            coeff = power[5];
        }
        double hurtBase = power[0] * level * (Math.pow(exp, 0.4) * 2.8853998118144273 / 100.0 + 1.0) * coeff;
        final int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
        hurtBase += chageBaseHurt;
        if (effector.isPet()) {
            return computePetSkillDamage(hurtBase, kx, fk, extra, weak, wx);
        }
        return computeSkillDamage(hurtBase, kx, fk, extra, weak, wx);
    }
    
    public static Integer computeDragonSkillDamage(final Fight fight, final FightObject attacker, final FightObject target, int damage, final double conquer_extent, final boolean penetrate, final double extra, final double weak) {
        double wlkang = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
        if (penetrate) {
            wlkang -= conquer_extent;
        }
        damage = BigDecimalUtil.decimalScaleIntegerDown(0, damage * (1.0 - wlkang + extra));
        final double wx = SkillFunc.getWuXingKeZhiXiuZheng(attacker, target, fight.getType());
        damage *= (int)(wx * (1.0 + weak));
        if (target.getType() == 1) {
            damage = BigDecimalUtil.decimalScaleIntegerDown(0, damage * Math.pow(0.5, 0.99));
        }
        return Math.max(damage, 1);
    }
    
    public static int computeAppendTarget(final FightObject effector, final int proficient, final int status) {
        int count = 0;
        double down = BigDecimalUtil.decimalScaleObjectDown(3, BigDecimalUtil.decimalScaleObjectDown(1, 0.1 + 18.0 * (1.0 + Math.pow(proficient, 0.35) * 2.0 / 100.0)) / 100.0);
        final double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.CHASE_RATIO.getDist()), 0.0);
        if (propertyAdd != 0.0) {
            down += propertyAdd;
        }
        for (int num = 1 + (int)(Math.pow(proficient, 0.38) * 8.0 / 100.0), i = 0; i < num; ++i) {
            if (SkillAi.canAffectOnPro(down)) {
                ++count;
            }
        }
        if (status > 0) {
            ++count;
        }
        return count;
    }
    
    public static boolean computeSkillHit(final Fight fight, final FightObject attacker, final FightObject target, final RoleSkill skill) {
        double odds = 1.0;
        final SysSkill sysSkill = SkillDataPool.skillMap.get(skill.getSkillId());
        final double magicHit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
        if ((attacker.getLogic() == LOGIC.LOGICTYPE_HERO.getValue() || attacker.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) && SkillCompute.barrier.contains(skill.getType())) {
            if (sysSkill != null) {
                String statusOdds = null;
                if (skill.getType() == SKILLATTR.SKILLATTR_CONFUSE.getValue()) {
                    final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_HL.getDist());
                    final double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HL.getDist());
                    final double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HL.getDist());
                    odds = mainParameter(skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1002odds";
                }
                else if (skill.getType() == SKILLATTR.SKILLATTR_SLEEP.getValue()) {
                    final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_HS.getDist());
                    final double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HS.getDist());
                    final double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HS.getDist());
                    odds = mainParameter(skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1004odds";
                }
                else if (skill.getType() == SKILLATTR.SKILLATTR_ICE.getValue()) {
                    final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_FY.getDist());
                    final double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_FY.getDist());
                    final double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_FY.getDist());
                    odds = mainParameter(skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1003odds";
                }
                else if (skill.getType() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
                    final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_YW.getDist());
                    final double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_YW.getDist());
                    final double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_YW.getDist());
                    odds = mainParameter(skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1006odds";
                }
                odds += magicHit;
                target.getStatusMap().setv(statusOdds, (Object)odds);
            }
        }
        else if (skill.getType() == SKILLATTR.SKILLATTR_HENGSAO.getValue() || skill.getType() == SKILLATTR.SKILLATTR_ZHENJI.getValue() || skill.getType() == SKILLATTR.SKILLATTR_POJIA.getValue()) {
            if (sysSkill != null) {
                final double dodge = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
                final double hit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
                odds = hit - dodge;
                odds += magicHit;
            }
        }
        else if (!SkillCompute.evade.contains(skill.getType()) && !SkillCompute.pet_evade.contains(skill.getType())) {
            odds = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
            final double hit2 = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
            odds -= hit2;
            odds += magicHit;
            return odds > 0.0 && SkillAi.canAffectOnPro(odds);
        }
        return !SkillAi.canAffectOnPro(odds);
    }
    
    public static double mainParameter(final int proficient, final double factor, final double kx, final double hkx, final double extra) {
        final double count = BigDecimalUtil.decimalScaleDouble(2, factor + (Math.pow(proficient, 0.4) * 4.0 / 100.0 + 1.0) * 300.0) / 1000.0;
        return BigDecimalUtil.decimalScaleDouble(2, count - kx + hkx + extra);
    }
    
    public static void main(final String[] args) {
        final double kx = 122.5;
        System.out.println("抗性 = " + kx);
        final double hs = 18.9;
        System.out.println("忽视抗性 = " + hs);
        final int factor = 62;
        System.out.println("系数 = " + factor);
        final int extra = 0;
        System.out.println("增强 = " + extra);
        final double v = mainParameter(800, factor, kx / 100.0, hs / 100.0, extra);
        System.out.println("命中率 = " + v);
        System.out.println("是否命中 = " + SkillAi.canAffectOnPro(v));
    }
    
    static {
        barrier = Arrays.asList(SKILLATTR.SKILLATTR_CONFUSE.getValue(), SKILLATTR.SKILLATTR_SLEEP.getValue(), SKILLATTR.SKILLATTR_ICE.getValue(), SKILLATTR.SKILLATTR_YIWANG.getValue());
        evade = Arrays.asList(SKILLATTR.SKILLATTR_PAN.getValue(), SKILLATTR.SKILLATTR_ATTACK.getValue(), SKILLATTR.SKILLATTR_SPEED.getValue(), SKILLATTR.SKILLATTR_ZHIYU.getValue());
        pet_evade = PETSKILLATTR.evade();
    }
}
