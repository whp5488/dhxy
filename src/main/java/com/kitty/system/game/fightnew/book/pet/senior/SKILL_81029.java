package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
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

public class SKILL_81029 extends SkillProcessor
{
    public SKILL_81029() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81029);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 2.5 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            if (target.isFallDown()) {
                return;
            }
            int recoverMana = (int)((double)target.getMaxMana() * 0.5);
            int oldMana = target.getCurrMana();
            int mana = Math.min(target.getMaxMana(), oldMana + recoverMana);
            if (recoverMana > mana - oldMana) {
                recoverMana = mana - oldMana;
            }
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
            PassiveSkillUtils.excuteChangeManaAuto(fight, target, actionDetailParam, targetParam, recoverMana);
            skillRecord.setT((long)(int)target.getId());
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
