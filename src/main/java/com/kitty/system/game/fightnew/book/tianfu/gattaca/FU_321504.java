
package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import java.util.ArrayList;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_321504 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        final DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        final TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321504);
        if (tianFuObject == null) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 321504);
        if (RACE.RACE_REN.getValue() == effector.getRace()) {
            if (SKILLATTR.SKILLATTR_POISON.getValue() != roleSkill.getType()) {
                return null;
            }
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_POISON.getValue());
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT(effector.getId());
        }
        else if (RACE.RACE_XIAN.getValue() == effector.getRace()) {
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_FIRE.getValue());
            attrList.add(SKILLATTR.SKILLATTR_WIND.getValue());
            attrList.add(SKILLATTR.SKILLATTR_THUNDER.getValue());
            attrList.add(SKILLATTR.SKILLATTR_WATER.getValue());
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT(effector.getId());
        }
        else if (RACE.RACE_GUI.getValue() == effector.getRace()) {
            final List<Integer> attrList = new ArrayList<Integer>();
            attrList.add(SKILLATTR.SKILLATTR_XIXUE.getValue());
            attrList.add(SKILLATTR.SKILLATTR_AIHAO.getValue());
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT(effector.getId());
        }
        return null;
    }
    
    private void excutePercent(final FightObject effector, final RoleSkill roleSkill, final byte tianFuLevel, final short dayInCeLevel, final List<Integer> attrList) {
        if (attrList.contains(roleSkill.getType())) {
            final double percenta = 1.5 * TianFuUtils.getTianFuCount(tianFuLevel, dayInCeLevel) / 100.0;
            final boolean flag = SkillAi.canAffectOnPro(percenta);
            final int time = 1;
            if (flag) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), (Object)time);
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), 0.3);
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()));
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()));
            }
        }
    }
}
