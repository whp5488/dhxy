
package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Optional;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
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

public class FU_322502 extends TianFuProcessor
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
        if (!target.isPlayer()) {
            return;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322502);
        if (tianFuObject == null) {
            return;
        }
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        if ((target.isFallDown() || SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType()) && effector.getStatusValue(1016) <= 0) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 322502);
            final Optional<FightObject> optional = fight.getMySide(effector.getId()).stream().filter(obj -> obj.getSkillCoolMap().getInt(String.valueOf(322502)) > 0).findFirst();
            if (optional.isPresent()) {
                return;
            }
            final boolean flag = SkillAi.canAffectOnPro(0.2);
            if (flag) {
                final double percenta = 2.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
                final int reduceSpeed = (int)(percenta * effector.getSpeed());
                final String dist = String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist());
                final int time = 3;
                ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, -reduceSpeed);
                effector.getSkillCoolMap().setv(String.valueOf(322502), (Object)6);
                final FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId(), true);
                targetParam.getStatusMap().put(1016, time);
                skillRecord.setT(target.getId());
            }
        }
    }
}
