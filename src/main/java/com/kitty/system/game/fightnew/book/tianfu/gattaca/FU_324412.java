package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324412 extends TianFuProcessor
{
    public FU_324412() {
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
        if (fight.isPvp() != 1 || effector.getRace() != RACE.RACE_REN.getValue()) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        Integer count = (Integer)args[1];
        if (roleSkill == null || SKILLATTR.SKILLATTR_CONFUSE.getValue() != (int)roleSkill.getType() || count == null || (int)count <= 0) {
            return;
        }
        if (!effector.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324412);
        if (tianFuObject == null) {
            return;
        }
        double percenta = 0.8 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324412);
        int addLife = (int)((double)(int)count * percenta * (double)effector.getMaxLife());
        TianFuUtils.whfmMethod(fight, effector, addLife, actionDetailParam);
        skillRecord.setT((long)(int)effector.getId());
        return;
    }
}
