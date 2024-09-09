package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324408 extends TianFuProcessor
{
    public FU_324408() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (fight.isPvp() != 1 || !effector.isPlayer()) {
            return;
        }
        if (!effector.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324408);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null || SKILLATTR.SKILLATTR_ZHEN.getValue() != (int)roleSkill.getType()) {
            return;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
        if (sysSkill == null || (int)sysSkill.getStep() != 4) {
            return;
        }
        Integer hurt = (Integer)args[1];
        if (hurt == null || (int)hurt <= 0) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324408);
        FightObject unit = SkillAi.effectProBass(fight, effector, Integer.valueOf(1), new ArrayList<>());
        double percenta = 10.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        int addLife = (int)((double)(int)hurt * percenta);
        if (addLife + unit.getCurrLife() > unit.getMaxLife()) {
            addLife = unit.getMaxLife() - unit.getCurrLife();
        }
        if (addLife > 0) {
            FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)unit.getId(), true);
            PassiveSkillUtils.excuteChangeLifeAuto(fight, unit, actionDetailParam, targetParam, addLife);
            skillRecord.setT((long)(int)target.getId());
        }
        return;
    }
}
