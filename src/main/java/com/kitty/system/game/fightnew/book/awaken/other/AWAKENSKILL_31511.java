package com.kitty.system.game.fightnew.book.awaken.other;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.vo.utils.StatusConst;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31511 extends AwakenSkillProcessor {
  public NutMap enteringSide(Fight fight, FightObject effector, Object... args) {
    NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 25.0D, 20.0D);
    if (map != null) {
      double percent = map.getDouble("percent");
      if (percent > 0.0D) {
        ActionDetailParam actionDetailParam = null;
        boolean a = SkillAi.canAffectOnPro(percent);
        if (a) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31511);
          List<FightObject> targets = PassiveSkillUtils.getTargets(fight, effector, (byte)1);
          if (targets != null && targets.size() > 0) {
            int controlStatus = 0;
            FightObject target = null;
            for (FightObject fightObject : targets) {
              controlStatus = AwakenSkillEffectUtils.getStatus(fightObject, StatusConst.getControlStatus());
              if (controlStatus > 0) {
                target = fightObject;
                break;
              } 
            } 
            if (target != null && controlStatus > 0) {
              target.removeStatus(controlStatus);
              List<Integer> clearList = new ArrayList<>();
              clearList.add(Integer.valueOf(controlStatus));
              actionDetailParam = ActionParamUtils.clearStatusReturn(effector, target, 7, 31511, 2, clearList);
              fight.getActionParam().getActionDetailParams().add(actionDetailParam);
              skillRecord.setT(target.getId().intValue());
            } 
          } 
        } 
        boolean b = SkillAi.canAffectOnPro(percent);
        if (b) {
          SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31516);
          List<FightObject> targetSide = fight.getTargetSide(effector.getId().intValue());
          if (targetSide != null && targetSide.size() > 0) {
            List<FightObject> targets = PassiveSkillUtils.getTargets(fight, targetSide.get(0), (byte)1);
            if (targets != null && targets.size() > 0) {
              List<FightObject> list = new ArrayList<>();
              for (FightObject obj : targets) {
                if (AwakenSkillEffectUtils.getStatus(obj, StatusConst.getMiniPositiveStatus()) > 0)
                  list.add(obj); 
              } 
              if (list.size() > 0) {
                int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
                FightObject target = list.get(round);
                if (target != null) {
                  int status = AwakenSkillEffectUtils.getStatus(target, StatusConst.getMiniPositiveStatus());
                  if (status > 0) {
                    target.removeStatus(status);
                    List<Integer> dicts = (List<Integer>)PRO_DICT_TYPE.getGFSDict().get(Integer.valueOf(status));
                    if (dicts != null && dicts.size() > 0)
                      for (Integer dist : dicts)
                        ImbueUtil.removeStatus(target.getImbueMap(), dist.intValue(), status);  
                    List<Integer> clearList = new ArrayList<>();
                    clearList.add(Integer.valueOf(status));
                    if (actionDetailParam != null) {
                      FightObjectParam targetParam = ActionParamUtils.clearStatusForTarget(target.getId().intValue(), 2, clearList, 2);
                      actionDetailParam.getTargetParamList().add(targetParam);
                    } else {
                      ActionParamUtils.clearStatus(fight, effector, target, 7, 31511, 2, clearList);
                    } 
                  } 
                  skillRecord.setT(target.getId().intValue());
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    return null;
  }
}
