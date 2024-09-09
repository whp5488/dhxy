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
    public SKILL_80006() {
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
        FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
        if (!targetParam.isHit()) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 80006);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 1.5 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag && !target.isFrozen()) {
            int round = 1;
            FightParamPool.addControlStatus(fight, target, 1002);
            target.getStatusMap().setv(String.valueOf(1002), Integer.valueOf(round));
            List<Integer> list = new ArrayList<>();
            if (target.isForgotten()) {
                list.add(Integer.valueOf(1006));
                target.setStatus(1006, 0);
            }
            if (target.isSleep()) {
                list.add(Integer.valueOf(1004));
                target.setStatus(1004, 0);
            }
            if (target.getStatusMap().getInt(String.valueOf(1021)) > 0) {
                target.removeStatus(1021);
                list.add(Integer.valueOf(1021));
            }
            targetParam.addDamageType(1002);
            targetParam.getStatusMap().put(Integer.valueOf(1002), Integer.valueOf(round));
            if (list.size() > 0) {
                targetParam.addClearStatus(list);
            }
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
