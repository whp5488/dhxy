package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.wardef.OTHER_SKILL_DICT_TYPE;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.ArrayList;
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
    public FU_321504() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 321504);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 321504);
        if (RACE.RACE_REN.getValue() == effector.getRace()) {
            if (SKILLATTR.SKILLATTR_POISON.getValue() != (int)roleSkill.getType()) {
                return null;
            }
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_POISON.getValue()));
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT((long)(int)effector.getId());
        }
        else if (RACE.RACE_XIAN.getValue() == effector.getRace()) {
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_FIRE.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_WIND.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_THUNDER.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_WATER.getValue()));
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT((long)(int)effector.getId());
        }
        else if (RACE.RACE_GUI.getValue() == effector.getRace()) {
            List<Integer> attrList = new ArrayList<>();
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_XIXUE.getValue()));
            attrList.add(Integer.valueOf(SKILLATTR.SKILLATTR_AIHAO.getValue()));
            this.excutePercent(effector, roleSkill, tianFuObject.getLevel(), dayInCeFight.getLevel(), attrList);
            skillRecord.setT((long)(int)effector.getId());
        }
        return null;
    }
    
    private void excutePercent(FightObject effector, RoleSkill roleSkill, byte tianFuLevel, short dayInCeLevel, List<Integer> attrList) {
        if (attrList.contains(roleSkill.getType())) {
            double percenta = 1.5 * TianFuUtils.getTianFuCount(tianFuLevel, dayInCeLevel) / 100.0;
            boolean flag = SkillAi.canAffectOnPro(percenta);
            int time = 1;
            if (flag) {
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()), Integer.valueOf(time));
                effector.getBeAttackedMap().setv(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()), Double.valueOf(0.3));
            }
            else {
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC.getDist()));
                effector.getBeAttackedMap().unset(String.valueOf(OTHER_SKILL_DICT_TYPE.COMBO_NUM_MAGIC_HURT_PERCENT.getDist()));
            }
        }
    }
}
