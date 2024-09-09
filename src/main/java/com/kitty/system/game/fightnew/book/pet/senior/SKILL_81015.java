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
    public SKILL_81015() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 81015);
        SysPetSkill petSkill = (SysPetSkill)args[0];
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
            return null;
        }
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 14.0 * ((double)effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        List<FightObject> targetObjs = new ArrayList<>();
        if (!effector.isASide()) {
            targetObjs = fight.getListA();
        }
        else {
            targetObjs = fight.getListB();
        }
        FightObject newTargetPet = null;
        List<FightObject> targetPets = new ArrayList<>();
        for (FightObject targetObj : targetObjs) {
            if (LOGIC.LOGICTYPE_PET.getValue() == targetObj.getLogic() && effector.getIcon() == targetObj.getIcon()) {
                targetPets.add(targetObj);
            }
            else {
                continue;
            }
        }
        if (targetPets.size() > 0) {
            Random rd = new Random();
            int i = rd.nextInt(targetPets.size());
            newTargetPet = (FightObject)targetPets.get(i);
        }
        if (newTargetPet == null) {
            return null;
        }
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            int oldLife = newTargetPet.getCurrLife();
            ActionParamUtils.changeLife(fight, effector, newTargetPet, 7, 81015, 1, -oldLife, false);
            skillRecord.setT((long)(int)newTargetPet.getId());
        }
        else {
            ActionParamUtils.attackNoHit(fight, effector, newTargetPet, 7, 81015, 0);
        }
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 0;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
