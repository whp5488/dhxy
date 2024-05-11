
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.Random;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.ArrayList;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81015 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81015);
        final SysPetSkill petSkill = (SysPetSkill)args[0];
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
            return null;
        }
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 14.0 * (effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        List<FightObject> targetObjs = new ArrayList<FightObject>();
        if (!effector.isASide()) {
            targetObjs = fight.getListA();
        }
        else {
            targetObjs = fight.getListB();
        }
        FightObject newTargetPet = null;
        final List<FightObject> targetPets = new ArrayList<FightObject>();
        for (final FightObject targetObj : targetObjs) {
            if (LOGIC.LOGICTYPE_PET.getValue() == targetObj.getLogic() && effector.getIcon() == targetObj.getIcon()) {
                targetPets.add(targetObj);
            }
        }
        if (targetPets.size() > 0) {
            final Random rd = new Random();
            final int i = rd.nextInt(targetPets.size());
            newTargetPet = targetPets.get(i);
        }
        if (newTargetPet == null) {
            return null;
        }
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            final int oldLife = newTargetPet.getCurrLife();
            ActionParamUtils.changeLife(fight, effector, newTargetPet, 7, 81015, 1, -oldLife, false);
            skillRecord.setT(newTargetPet.getId());
        }
        else {
            ActionParamUtils.attackNoHit(fight, effector, newTargetPet, 7, 81015, 0);
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 0;
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
