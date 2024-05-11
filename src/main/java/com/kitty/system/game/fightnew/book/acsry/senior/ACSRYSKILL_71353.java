
package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;

public class ACSRYSKILL_71353 extends AcsrySkillProcessor
{
    @Override
    public void attackOver(final Fight fight, final FightObject effecter, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        RoleSkill skill = null;
        Boolean isHit = false;
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (final Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_ICE.getValue() == skill.getType() && target.getStatusValue(1003) > 0 && effecter.isPlayer()) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId(), 71353);
            skillRecord.setT(target.getId());
            target.getStatusMap().setv("1003perpetrator", (Object)("71353_" + effecter.getId()));
        }
    }
    
    @Override
    public void statusOver(final Fight fight, final FightObject fightObject, final Object... args) {
        final Integer status = (Integer)args[0];
        if (status == null || 1003 != status) {
            return;
        }
        final String perpetrator = fightObject.getStatusMap().getString("1003perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71353))) {
            fightObject.getStatusMap().unset("1003perpetrator");
            final boolean flag = SkillAi.canAffectOnPro(0.06);
            if (flag) {
                final List<FightObject> objs = this.getFightObject(fight, fightObject);
                if (objs != null && objs.size() > 0) {
                    for (int i = 0; i < objs.size(); ++i) {
                        final FightObject obj = objs.get(i);
                        final List<Integer> list = new ArrayList<Integer>();
                        for (final Integer sta : StatusConst.getControlStatus()) {
                            if (1003 == sta) {
                                continue;
                            }
                            if (obj.getStatusValue(sta) <= 0) {
                                continue;
                            }
                            obj.getStatusMap().setv(String.valueOf(sta), 0);
                            list.add(sta);
                        }
                        obj.setStatus(1003, 1);
                        final ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(fightObject, obj, 7, 300999, 2, 1003, 1);
                        if (list.size() > 0) {
                            final FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, obj.getId());
                            targetParam.setClearStatus(list);
                        }
                        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 71353);
                        skillRecord.setT(obj.getId());
                    }
                }
            }
        }
    }
    
    private List<FightObject> getFightObject(final Fight fight, final FightObject target) {
        final int pos = target.getPos();
        final int i = pos / 100;
        final List<Integer> list = new ArrayList<Integer>();
        if (i == 0) {
            list.add(pos + 100);
        }
        else {
            list.add(pos - 100);
        }
        final int g = pos % 10000 % 100;
        if (g == 1) {
            list.add(pos + 1);
        }
        else if (g == 5) {
            list.add(pos - 1);
        }
        else {
            list.add(pos + 1);
            list.add(pos - 1);
        }
        if (list.size() > 0) {
            final List<FightObject> returnList = new ArrayList<FightObject>();
            for (int j = 0; j < fight.getMySide(target.getId()).size(); ++j) {
                final FightObject obj = fight.getMySide(target.getId()).get(j);
                if (obj != null && !obj.isFallDown() && list.contains(obj.getPos())) {
                    returnList.add(obj);
                }
            }
            return returnList;
        }
        return null;
    }
}
