package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81002 extends SkillProcessor
{
    public SKILL_81002() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (target == null || !target.isFallDown()) {
            return;
        }
        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
        if (!targetParam.isHit()) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81002);
        int num = effector.getSkillCoolMap().getInt("81002value");
        if (num < 2) {
            double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 7.0 * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
            if (num >= 0) {
                percent = 1.0;
            }
            else {
                double pval = AcsryPetSkillUtil.getPval(81002, effector);
                percent += pval;
            }
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                if (effector.getSkillCoolMap().getInt(String.valueOf(81002)) > 0 && effector.getSkillCoolMap().getInt("81002value") >= 2) {
                    return;
                }
                effector.getSkillCoolMap().setv(String.valueOf(81002), Integer.valueOf(1));
                ++num;
                effector.getSkillCoolMap().setv("81002value", Integer.valueOf(num));
                FightObject newTarget = fight.getTargetNoCurrentTarget((int)effector.getId(), (int)target.getId());
                if (newTarget == null) {
                    return;
                }
                RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(81002));
                BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
                battleUnit.countDoubleHit();
                int doubleHit = 1;
                if (SkillAi.checkDoubleHit(effector)) {
                    int numZ = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), effector.getPropertyMap().get(String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist())));
                    doubleHit += numZ;
                }
                boolean flagZ = SkillAi.checkPetIsSkill(fight, effector.isASide(), Integer.valueOf(81057));
                if (flagZ) {
                    int reduceNum = PetPassiveSkillUtil.reduceDoubleHit(fight, effector);
                    doubleHit = Math.max(1, doubleHit - reduceNum);
                }
                battleUnit.setDoubleHit(doubleHit);
                PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, actionDetailParam, 81002);
                skillRecord.setT((long)(int)newTarget.getId());
                if (newTarget.isFallDown()) {
                    this.normalAttack(fight, effector, newTarget, actionDetailParam, new Object[0]);
                }
            }
        }
        return;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
