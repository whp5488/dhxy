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
    
    public SkillCompute() {
    }
    
    public static double computeSkillDamagePoison(double power, double kx, double fk, Integer skillExp, Integer round, Integer level, Integer maxHp, double extra, double weak, double wx) {
        level = Integer.valueOf(((int)level < 0) ? 1 : ((int)level));
        double damageBase = power * (double)(int)level * (Math.pow((double)(int)skillExp, 0.4) * 2.8853998118144273 / 100.0 + 1.0);
        double damage = (double)(int)computeSkillDamage(damageBase, kx, fk, extra, weak, wx);
        if ((int)round > 1) {
            damage = Math.min(damage, (double)(int)maxHp * 0.5);
        }
        damage = Math.max(damage, 1.0);
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
        double hurt = computeSkillDamagePoison((double)id, kdx, dqd, Integer.valueOf(proficient), Integer.valueOf(round), Integer.valueOf(level), Integer.valueOf(maxLife), extra, weak, wx);
        return Integer.valueOf(Math.max(1, (int)hurt));
    }
    
    public static Integer computeSkillDamage(double damage, double kx, double fk, double extra, double weak, double wx) {
        damage *= 1.0 + fk * 3.0 - kx * 2.0 + extra;
        damage *= wx * (1.0 + weak);
        damage = Math.max(damage, 1.0);
        return Integer.valueOf((int)damage);
    }
    
    public static Integer computePetSkillDamage(double damage, double kx, double fk, double extra, double weak, double wx) {
        damage *= 1.0 + fk * 3.0 - kx + extra;
        damage *= wx * (1.0 + weak);
        damage = Math.max(damage, 1.0);
        return Integer.valueOf((int)damage);
    }
    
    public static Integer computeSkillDamage(int proficient, double power, int max, int curr, double kx, double fk, double extra, double weak, double wx) {
        if (curr <= 0) {
            return Integer.valueOf(0);
        }
        double ratio = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(0.425 * ((double)max * 1.1 / (double)curr) + power * (Math.pow((double)proficient, 0.35) * 2.0 / 100.0 + 1.0))) / 100.0;
        double damage = (ratio + fk - kx + extra) * (double)curr;
        damage += damage * (wx / 50.0 * (1.0 + weak));
        double hp = (double)curr * 0.5;
        if (damage > hp) {
            damage = hp;
        }
        damage = Math.max(damage, 1.0);
        return Integer.valueOf((int)damage);
    }
    
    public static Integer computeSkillLeech(int id, int damage, double extra) {
        int leech = 1;
        SysSkill skill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(id));
        if (skill != null) {
            leech = (int)((double)damage * (skill.getPower()[1] + 2.0 * extra));
        }
        return Integer.valueOf(Math.max(leech, 1));
    }
    
    public static Integer computeSkillDamageWildfire(FightObject effector, double[] power, int level, int exp, double kx, double fk, double extra, double weak, int die, double wx) {
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
        double hurtBase = power[0] * (double)level * (Math.pow((double)exp, 0.4) * 2.8853998118144273 / 100.0 + 1.0) * coeff;
        int chageBaseHurt = AcsrySkillUtil.getChageBaseHurt(effector);
        hurtBase += (double)chageBaseHurt;
        if (effector.isPet()) {
            return computePetSkillDamage(hurtBase, kx, fk, extra, weak, wx);
        }
        return computeSkillDamage(hurtBase, kx, fk, extra, weak, wx);
    }
    
    public static Integer computeDragonSkillDamage(Fight fight, FightObject attacker, FightObject target, int damage, double conquer_extent, boolean penetrate, double extra, double weak) {
        double wlkang = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
        if (penetrate) {
            wlkang -= conquer_extent;
        }
        damage = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damage * (1.0 - wlkang + extra)));
        double wx = SkillFunc.getWuXingKeZhiXiuZheng(attacker, target, fight.getType());
        damage = (int)((double)damage * (wx * (1.0 + weak)));
        if (target.getType() == 1) {
            damage = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damage * Math.pow(0.5, 0.99)));
        }
        return Integer.valueOf(Math.max(damage, 1));
    }
    
    public static int computeAppendTarget(FightObject effector, int proficient, int status) {
        int count = 0;
        double down = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(0.1 + 18.0 * (1.0 + Math.pow((double)proficient, 0.35) * 2.0 / 100.0))) / 100.0));
        double propertyAdd = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.CHASE_RATIO.getDist()), 0.0);
        if (propertyAdd != 0.0) {
            down += propertyAdd;
        }
        for (int num = 1 + (int)(Math.pow((double)proficient, 0.38) * 8.0 / 100.0), i = 0; i < num; ++i) {
            if (SkillAi.canAffectOnPro(down)) {
                ++count;
            }
        }
        if (status > 0) {
            ++count;
        }
        return count;
    }
    
    public static boolean computeSkillHit(Fight fight, FightObject attacker, FightObject target, RoleSkill skill) {
        double odds = 1.0;
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
        double magicHit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
        if ((attacker.getLogic() == LOGIC.LOGICTYPE_HERO.getValue() || attacker.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) && SkillCompute.barrier.contains(skill.getType())) {
            if (sysSkill != null) {
                String statusOdds = null;
                if ((int)skill.getType() == SKILLATTR.SKILLATTR_CONFUSE.getValue()) {
                    double kx = target.getSelfAllTempProOther(attacker, PRO_DICT_TYPE.K_HL.getDist());
                    double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HL.getDist());
                    double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HL.getDist());
                    odds = mainParameter((int)skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1002odds";
                }
                else if ((int)skill.getType() == SKILLATTR.SKILLATTR_SLEEP.getValue()) {
                    double kx = target.getSelfAllTempProOther(attacker, PRO_DICT_TYPE.K_HS.getDist());
                    double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_HS.getDist());
                    double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_HS.getDist());
                    odds = mainParameter((int)skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1004odds";
                }
                else if ((int)skill.getType() == SKILLATTR.SKILLATTR_ICE.getValue()) {
                    double kx = target.getSelfAllTempProOther(attacker, PRO_DICT_TYPE.K_FY.getDist());
                    double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_FY.getDist());
                    double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_FY.getDist());
                    odds = mainParameter((int)skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1003odds";
                }
                else if ((int)skill.getType() == SKILLATTR.SKILLATTR_YIWANG.getValue()) {
                    double kx = target.getSelfAllTempProOther(attacker, PRO_DICT_TYPE.K_YW.getDist());
                    double hkx = attacker.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_YW.getDist());
                    double extra = attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_YW.getDist());
                    odds = mainParameter((int)skill.getProficient(), sysSkill.getHitRatio(), kx, hkx, extra);
                    statusOdds = "1006odds";
                }
                odds += magicHit;
                target.getStatusMap().setv(statusOdds, Double.valueOf(odds));
            }
        }
        else if ((int)skill.getType() == SKILLATTR.SKILLATTR_HENGSAO.getValue() || (int)skill.getType() == SKILLATTR.SKILLATTR_ZHENJI.getValue() || (int)skill.getType() == SKILLATTR.SKILLATTR_POJIA.getValue()) {
            if (sysSkill != null) {
                double dodge = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
                double hit2 = attacker.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
                odds = hit2 - dodge;
                odds += magicHit;
            }
        }
        else if (!SkillCompute.evade.contains(skill.getType()) && !SkillCompute.pet_evade.contains(skill.getType())) {
            odds = target.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
            double hit = attacker.getSelfAllTempPro(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
            odds -= hit;
            odds += magicHit;
            return odds > 0.0 && SkillAi.canAffectOnPro(odds);
        }
        return !SkillAi.canAffectOnPro(odds);
    }
    
    public static double mainParameter(int proficient, double factor, double kx, double hkx, double extra) {
        double count = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf(factor + (Math.pow((double)proficient, 0.4) * 4.0 / 100.0 + 1.0) * 300.0)) / 1000.0;
        return BigDecimalUtil.decimalScaleDouble(Integer.valueOf(2), Double.valueOf(count - kx + hkx + extra));
    }
    
    public static void main(String[] args) {
        double kx = 122.5;
        System.out.println("抗性 = " + kx);
        double hs = 18.9;
        System.out.println("忽视抗性 = " + hs);
        int factor = 62;
        System.out.println("系数 = " + factor);
        int extra = 0;
        System.out.println("增强 = " + extra);
        double v = mainParameter(800, (double)factor, kx / 100.0, hs / 100.0, (double)extra);
        System.out.println("命中率 = " + v);
        System.out.println("是否命中 = " + SkillAi.canAffectOnPro(v));
    }
    
    static {
        barrier = Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()));
        evade = Arrays.asList(Integer.valueOf(SKILLATTR.SKILLATTR_PAN.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ATTACK.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SPEED.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_ZHIYU.getValue()));
        pet_evade = PETSKILLATTR.evade();
    }
}
