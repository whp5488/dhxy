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
    public FU_322502() {
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
        if (!target.isPlayer()) {
            return;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322502);
        if (tianFuObject == null) {
            return;
        }
        RoleSkill roleSkill = (RoleSkill)args[0];
        if (roleSkill == null) {
            return;
        }
        if ((target.isFallDown() || SKILLATTR.SKILLATTR_CONFUSE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_ICE.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_SLEEP.getValue() == (int)roleSkill.getType() || SKILLATTR.SKILLATTR_YIWANG.getValue() == (int)roleSkill.getType()) && effector.getStatusValue(1016) <= 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 322502);
            Optional<FightObject> optional = fight.getMySide((int)effector.getId()).stream().filter(obj -> obj.getSkillCoolMap().getInt(String.valueOf(322502)) > 0).findFirst();
            if (optional.isPresent()) {
                return;
            }
            boolean flag = SkillAi.canAffectOnPro(0.2);
            if (flag) {
                double percenta = 2.5 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
                int reduceSpeed = (int)(percenta * (double)effector.getSpeed());
                String dist = String.valueOf(PRO_DICT_TYPE.RISE_SPEED.getDist());
                int time = 3;
                ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, (double)(-reduceSpeed));
                effector.getSkillCoolMap().setv(String.valueOf(322502), Integer.valueOf(6));
                FightObjectParam targetParam = actionDetailParam.gainTargetParam((int)effector.getId(), true);
                targetParam.getStatusMap().put(Integer.valueOf(1016), Integer.valueOf(time));
                skillRecord.setT((long)(int)target.getId());
            }
        }
        return;
    }
}
