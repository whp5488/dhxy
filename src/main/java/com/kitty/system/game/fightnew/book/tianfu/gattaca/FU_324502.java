package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
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

public class FU_324502 extends TianFuProcessor
{
    public FU_324502() {
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
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324502);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        if (target.isFrozen()) {
            return;
        }
        if (SKILLATTR.SKILLATTR_FIRE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_POISON.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_HENGSAO.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == (int)roleSkill.getType()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324502);
            int currLife = target.getCurrLife();
            if (currLife <= 0) {
                return;
            }
            double percenta = 1.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int hurt = (int)((double)currLife * percenta);
            if (hurt > 0) {
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)target.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, target, actionDetailParam, targetParam, -hurt);
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
