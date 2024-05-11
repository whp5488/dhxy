
package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.ArrayList;
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

public class SKILL_80006 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
        if (!targetParam.isHit()) {
            return;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 80006);
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 1.5 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag && !target.isFrozen()) {
            final int round = 1;
            FightParamPool.addControlStatus(fight, target, 1002);
            target.getStatusMap().setv(String.valueOf(1002), (Object)round);
            final List<Integer> list = new ArrayList<Integer>();
            if (target.isForgotten()) {
                list.add(1006);
                target.setStatus(1006, 0);
            }
            if (target.isSleep()) {
                list.add(1004);
                target.setStatus(1004, 0);
            }
            if (target.getStatusMap().getInt(String.valueOf(1021)) > 0) {
                target.removeStatus(1021);
                list.add(1021);
            }
            targetParam.addDamageType(1002);
            targetParam.getStatusMap().put(1002, round);
            if (list.size() > 0) {
                targetParam.addClearStatus(list);
            }
            skillRecord.setT(target.getId());
        }
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
