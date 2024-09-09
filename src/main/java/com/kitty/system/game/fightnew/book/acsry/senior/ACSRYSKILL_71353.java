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
    public ACSRYSKILL_71353() {
    }
    
    @Override
    public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        RoleSkill skill = null;
        Boolean isHit = Boolean.valueOf(false);
        try {
            skill = (RoleSkill)args[0];
            isHit = (Boolean)args[1];
        }
        catch (Exception e) {
            return;
        }
        if (skill == null) {
            return;
        }
        if (isHit == null || !(boolean)isHit) {
            return;
        }
        if (SKILLATTR.SKILLATTR_ICE.getValue() == (int)skill.getType() && target.getStatusValue(1003) > 0 && effecter.isPlayer()) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effecter.getId(), 71353);
            skillRecord.setT((long)(int)target.getId());
            target.getStatusMap().setv("1003perpetrator", "71353_" + effecter.getId());
        }
        return;
    }
    
    @Override
    public void statusOver(Fight fight, FightObject fightObject, Object... args) {
        Integer status = (Integer)args[0];
        if (status == null || 1003 != (int)status) {
            return;
        }
        String perpetrator = fightObject.getStatusMap().getString("1003perpetrator");
        if (perpetrator != null && perpetrator.contains(String.valueOf(71353))) {
            fightObject.getStatusMap().unset("1003perpetrator");
            boolean flag = SkillAi.canAffectOnPro(0.06);
            if (flag) {
                List<FightObject> objs = this.getFightObject(fight, fightObject);
                if (objs != null && objs.size() > 0) {
                    for (int i = 0; i < objs.size(); ++i) {
                        FightObject obj = (FightObject)objs.get(i);
                        List<Integer> list = new ArrayList<>();
                        for (Integer sta : StatusConst.getControlStatus()) {
                            if (1003 == (int)sta) {
                                continue;
                            }
                            if (obj.getStatusValue((int)sta) > 0) {
                                obj.getStatusMap().setv(String.valueOf(sta), Integer.valueOf(0));
                                list.add(sta);
                            }
                            else {
                                continue;
                            }
                        }
                        obj.setStatus(1003, 1);
                        ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(fightObject, obj, 7, 300999, 2, 1003, 1);
                        if (list.size() > 0) {
                            FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, (int)obj.getId());
                            targetParam.setClearStatus(list);
                        }
                        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
                        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 71353);
                        skillRecord.setT((long)(int)obj.getId());
                    }
                }
            }
        }
        return;
    }
    
    private List<FightObject> getFightObject(Fight fight, FightObject target) {
        int pos = target.getPos();
        int i = pos / 100;
        List<Integer> list = new ArrayList<>();
        if (i == 0) {
            list.add(Integer.valueOf(pos + 100));
        }
        else {
            list.add(Integer.valueOf(pos - 100));
        }
        int g = pos % 10000 % 100;
        if (g == 1) {
            list.add(Integer.valueOf(pos + 1));
        }
        else if (g == 5) {
            list.add(Integer.valueOf(pos - 1));
        }
        else {
            list.add(Integer.valueOf(pos + 1));
            list.add(Integer.valueOf(pos - 1));
        }
        if (list.size() > 0) {
            List<FightObject> returnList = new ArrayList<>();
            for (int j = 0; j < fight.getMySide((int)target.getId()).size(); ++j) {
                FightObject obj = (FightObject)fight.getMySide((int)target.getId()).get(j);
                if (obj != null && !obj.isFallDown() && list.contains(Integer.valueOf(obj.getPos()))) {
                    returnList.add(obj);
                }
            }
            return returnList;
        }
        return null;
    }
}
