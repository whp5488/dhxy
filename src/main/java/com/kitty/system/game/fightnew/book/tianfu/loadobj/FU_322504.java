package com.kitty.system.game.fightnew.book.tianfu.loadobj;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.StatusConst;
import org.nutz.lang.util.NutMap;

import java.util.Iterator;

public class FU_322504 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }

  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (target.isPlayer()) {
      DayInCeFight dayInCeFight = ((PlayerFightObject)target).getDayInCeFight();
      TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 322504);
      if (tianFuObject != null) {
        boolean flag = false;
        Integer theStatus = -1;
        int attr = -1;
        Iterator var11 = StatusConst.getControlStatus().iterator();

        label47:
        while(var11.hasNext()) {
          Integer status = (Integer)var11.next();
          if (target.getStatusValue(status) > 0) {
            flag = true;
            theStatus = status;
            switch (status) {
              case 1002:
                attr = SKILLATTR.SKILLATTR_CONFUSE.getValue();
                break label47;
              case 1003:
                attr = SKILLATTR.SKILLATTR_ICE.getValue();
                break label47;
              case 1004:
                attr = SKILLATTR.SKILLATTR_SLEEP.getValue();
              case 1005:
              default:
                break label47;
              case 1006:
                attr = SKILLATTR.SKILLATTR_YIWANG.getValue();
                break label47;
            }
          }
        }

        if (flag && attr != -1) {
          RoleSkill roleSkill = (RoleSkill)args[0];
          if (attr == roleSkill.getType()) {
            double percenta = 3.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            boolean flagTrigger = SkillAi.canAffectOnPro(percenta);
            if (flagTrigger) {
              SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)effector.getId(), 322504);
              target.getStatusMap().remove(String.valueOf(theStatus));
              FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId(), false);
              if (targetParam != null) {
                targetParam.addClearStatus(theStatus);
                skillRecord.setT((long)target.getId());
              }
            }

          }
        }
      }
    }
  }
}
