package com.kitty.system.game.fightnew.book.horse.four.senior;

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

public class HORSESKILL_64102 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = getPercenta(horseSkillParam) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      map.setv(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  private double getPercenta(HorseSkillParam horseSkillParam) {
    double percent = 1.0E-4D * Math.pow(horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.05D * horseSkillParam.getGrowUp() / 1000.0D / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D) + 0.1D;
    return percent;
  }
  
  private double getPercentc(HorseSkillParam horseSkillParam) {
    double percent = 2.0E-4D * Math.pow((horseSkillParam.getGrowUp() / 1000) / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D), 2.0D) + 0.1D * (horseSkillParam.getGrowUp() / 1000) / (1.175D + 0.0013D * horseSkillParam.getLevel()) * (0.6D * horseSkillParam.getLevel() + 0.4D * horseSkillParam.getProficient() / 1000.0D) + 0.1D;
    return percent;
  }
  
  public void attackOver(Fight fight, PetFightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (effector.isPet() && target.getStatusValue(1016) <= 0) {
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(64102));
      if (horseSkillParam == null)
        return; 
      double percentb = (2.0D * HorseSkillUtils.getPercentSenior(horseSkillParam) + 0.2D) / 100.0D;
      percentb = HorseSkillUtils.getMathRoundPercent(percentb);
      boolean flag = SkillAi.canAffectOnPro(percentb);
      if (!flag)
        return; 
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 64102);
      int cnt = (int)Math.floor((2 + horseSkillParam.getLevel() / 80));
      double percentc = getPercentc(horseSkillParam) / 100.0D;
      percentc = HorseSkillUtils.getMathRoundPercent(percentc);
      ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), cnt, -percentc);
      target.getStatusMap().setv(String.valueOf(1016), Integer.valueOf(cnt));
      FightObjectParam targetParam = actionDetailParam.gainTargetParam(target.getId().intValue(), false);
      if (targetParam != null) {
        targetParam.getStatusMap().put(Integer.valueOf(1016), Integer.valueOf(cnt));
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
