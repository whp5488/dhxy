package com.kitty.system.game.fightnew.book.horse.four.middle;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_64301 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = getPercent(horseSkillParam) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  private double getPercent(HorseSkillParam horseSkillParam) {
    double percent = (1.0E-4D * Math.pow(horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.05D * horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D)) * 0.8D + 0.05D;
    return percent;
  }
  
  public void attackOver(Fight fight, PetFightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (effector.isPet() && effector.getStatusValue(1010) <= 0) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(64301));
      if (horseSkillParam == null)
        return; 
      double percentb = (1.0D * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1D) / 100.0D;
      percentb = HorseSkillUtils.getMathRoundPercent(percentb);
      boolean flag = SkillAi.canAffectOnPro(percentb);
      if (!flag)
        return; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 64301);
      int cnt = (int)Math.floor((2 + horseSkillParam.getLevel() / 80));
      double percentc = getPercent(horseSkillParam) / 100.0D;
      percentc = HorseSkillUtils.getMathRoundPercent(percentc);
      ImbueUtil.addOrUpdStatus(effector.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), cnt, percentc);
      effector.getStatusMap().setv(String.valueOf(1010), Integer.valueOf(cnt));
      FightObjectParam targetParam = actionDetailParam.gainTargetParam(effector.getId().intValue(), false);
      if (targetParam != null) {
        targetParam.getStatusMap().put(Integer.valueOf(1010), Integer.valueOf(cnt));
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
  }
}
