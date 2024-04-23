package com.kitty.system.game.fightnew.book.horse.three.normal;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_63014 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (target.isPet()) {
      PetFightObject petTarget = (PetFightObject)target;
      Integer hurt = (Integer)args[0];
      if (hurt == null)
        return null; 
      HorseSkillParam horseSkillParam = (HorseSkillParam)petTarget.getHorseSkillMap().get(Integer.valueOf(63014));
      if (horseSkillParam == null)
        return null; 
      double percenta = (20.0D * getPercent(horseSkillParam) + 0.1D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      boolean flag = SkillAi.canAffectOnPro(percenta);
      if (flag) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 63014);
        skillRecord.setT(target.getId().intValue());
        NutMap map = NutMap.NEW();
        int alterHurt = hurt.intValue() - 1;
        map.setv("alterHurt", Integer.valueOf(-alterHurt));
        return map;
      } 
    } 
    return null;
  }
  
  private double getPercent(HorseSkillParam horseSkillParam) {
    double percent = (Math.floor((horseSkillParam.getGrowUp() + 10000.0D) / 11265.0D) * (-0.7D * horseSkillParam.getGrowUp() + 885.5D) + horseSkillParam.getGrowUp()) / 1000.0D / 1.325D * (0.6D * (-0.0067D * Math.pow(Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.7D * horseSkillParam.getLevel() + 56.0D) + horseSkillParam.getLevel(), 2.0D) + 2.06D * (Math.floor((horseSkillParam.getLevel() + 1000.0D) / 1080.0D) * (-0.7D * horseSkillParam.getLevel() + 56.0D) + horseSkillParam.getLevel())) / 130.0D + (-0.0067D * Math.pow(Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.7D * horseSkillParam.getProficient() + 56000.0D) + horseSkillParam.getProficient(), 2.0D) / 1000.0D + 2.06D * (Math.floor((horseSkillParam.getProficient() + 1000000.0D) / 1080000.0D) * (-0.7D * horseSkillParam.getProficient() + 56000.0D) + horseSkillParam.getProficient())) / 325000.0D + 0.1D) * 0.6D;
    return percent;
  }
}
