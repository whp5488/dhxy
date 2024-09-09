package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324506 extends TianFuProcessor
{
    public FU_324506() {
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
        if (fight.isPvp() != 1 || effector.getRace() != RACE.RACE_GUI.getValue() || !target.isPet()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324506);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null || (SKILLATTR.SKILLATTR_XIXUE.getValue() != (int)roleSkill.getType() && SKILLATTR.SKILLATTR_AIHAO.getValue() != (int)roleSkill.getType())) {
            return;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324506);
        double percenta = 0.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        int addLife = (int)((double)effector.getMaxLife() * percenta);
        TianFuUtils.whfmMethod(fight, effector, addLife, actionDetailParam);
        skillRecord.setT((long)(int)target.getId());
        return;
    }
}
