package com.kitty.system.game.fightnew.book.acsry.senior;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.acsry.AcsrySkillProcessor;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;

public class ACSRYSKILL_71353 extends AcsrySkillProcessor {
  public void attackOver(Fight fight, FightObject effecter, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    RoleSkill skill = null;
    Boolean isHit = Boolean.valueOf(false);
    try {
      skill = (RoleSkill)args[0];
      isHit = (Boolean)args[1];
    } catch (Exception e) {
      return;
    } 
    if (skill == null)
      return; 
    if (isHit == null || !isHit.booleanValue())
      return; 
    if (SKILLATTR.SKILLATTR_ICE.getValue() == skill.getType().intValue() && target.getStatusValue(1003) > 0 && 
      effecter.isPlayer()) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effecter.getId().intValue(), 71353);
      skillRecord.setT(target.getId().intValue());
      target.getStatusMap().setv("1003perpetrator", "71353_" + effecter.getId());
    } 
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    Integer status = (Integer)args[0];
    if (status == null || 1003 != status.intValue())
      return; 
    String perpetrator = fightObject.getStatusMap().getString("1003perpetrator");
    if (perpetrator != null && perpetrator.contains(String.valueOf(71353))) {
      fightObject.getStatusMap().unset("1003perpetrator");
      boolean flag = SkillAi.canAffectOnPro(0.06D);
      if (flag) {
        List<FightObject> objs = getFightObject(fight, fightObject);
        if (objs != null && objs.size() > 0)
          for (int i = 0; i < objs.size(); i++) {
            FightObject obj = objs.get(i);
            List<Integer> list = new ArrayList<>();
            for (Integer sta : StatusConst.getControlStatus()) {
              if (1003 == sta.intValue())
                continue; 
              if (obj.getStatusValue(sta.intValue()) > 0) {
                obj.getStatusMap().setv(String.valueOf(sta), Integer.valueOf(0));
                list.add(sta);
              } 
            } 
            obj.setStatus(1003, 1);
            ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(fightObject, obj, 7, 300999, 2, 1003, 1);
            if (list.size() > 0) {
              FightObjectParam targetParam = ActionParamUtils.getTargetParam(actionDetailParam, obj.getId().intValue());
              targetParam.setClearStatus(list);
            } 
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId().intValue(), 71353);
            skillRecord.setT(obj.getId().intValue());
          }  
      } 
    } 
  }
  
  private List<FightObject> getFightObject(Fight fight, FightObject target) {
    int pos = target.getPos();
    int i = pos / 100;
    List<Integer> list = new ArrayList<>();
    if (i == 0) {
      list.add(Integer.valueOf(pos + 100));
    } else {
      list.add(Integer.valueOf(pos - 100));
    } 
    int g = pos % 10000 % 100;
    if (g == 1) {
      list.add(Integer.valueOf(pos + 1));
    } else if (g == 5) {
      list.add(Integer.valueOf(pos - 1));
    } else {
      list.add(Integer.valueOf(pos + 1));
      list.add(Integer.valueOf(pos - 1));
    } 
    if (list.size() > 0) {
      List<FightObject> returnList = new ArrayList<>();
      for (int j = 0; j < fight.getMySide(target.getId().intValue()).size(); j++) {
        FightObject obj = fight.getMySide(target.getId().intValue()).get(j);
        if (obj != null && !obj.isFallDown() && list.contains(Integer.valueOf(obj.getPos())))
          returnList.add(obj); 
      } 
      return returnList;
    } 
    return null;
  }
}
