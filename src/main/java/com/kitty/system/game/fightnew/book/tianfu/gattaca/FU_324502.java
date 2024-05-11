
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
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public void attackOver(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (fight.isPvp() != 1 || !effector.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324502);
        if (tianFuObject == null) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        if (target.isFrozen()) {
            return;
        }
        if (SKILLATTR.SKILLATTR_FIRE.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_WIND.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_THUNDER.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_WATER.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_POISON.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_AIHAO.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_POJIA.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_HENGSAO.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_ZHENJI.getValue() == roleSkill.getType()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 324502);
            final int currLife = target.getCurrLife();
            if (currLife <= 0) {
                return;
            }
            final double percenta = 1.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            final int hurt = (int)(currLife * percenta);
            if (hurt > 0) {
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), true);
                PassiveSkillUtils.excuteChangeLifeAuto(fight, target, actionDetailParam, targetParam, -hurt);
                skillRecord.setT(target.getId());
            }
        }
    }
}
