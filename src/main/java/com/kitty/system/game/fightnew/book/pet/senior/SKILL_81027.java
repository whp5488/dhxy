
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81027 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81027);
        FightObject target = (FightObject)args[0];
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 2.0 * (effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int hurt = 2048;
            if (target.getLogic() == LOGIC.LOGICTYPE_PET.getValue() && fight.queryFightObject(target.getHostId()) != null && !fight.queryFightObject(target.getHostId()).isFallDown()) {
                target = fight.queryFightObject(target.getHostId());
            }
            if (target.getCurrLife() - hurt <= 0) {
                hurt = target.getCurrLife();
            }
            ActionParamUtils.changeLife(fight, effector, target, 7, 81027, 2, -hurt, false);
            skillRecord.setT(target.getId());
        }
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
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
