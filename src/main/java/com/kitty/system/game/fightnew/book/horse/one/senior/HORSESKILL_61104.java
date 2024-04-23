package com.kitty.system.game.fightnew.book.horse.one.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_61104 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    if (effector.getType() != 0)
      return null; 
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (obj.getIcon() == effector.getIcon() && obj.isPet() && !obj.getId().equals(effector.getId()))).collect(Collectors.toList());
    if (mySide != null && mySide.size() > 0) {
      Integer hurt = (Integer)args[0];
      if (hurt == null || hurt.intValue() <= 0)
        return null; 
      HorseSkillParam horseSkillParam = (HorseSkillParam)effector.getHorseSkillMap().get(Integer.valueOf(61104));
      if (horseSkillParam == null)
        return null; 
      double percent = HorseSkillUtils.getPercentSenior(horseSkillParam);
      double percenta = (10.0D * percent + 0.2D) / 100.0D;
      percenta = HorseSkillUtils.getMathRoundPercent(percenta);
      int alterHurt = (int)(hurt.intValue() * percenta * mySide.size());
      if (alterHurt != 0) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 61104);
        skillRecord.setT(effector.getId().intValue());
        NutMap map = NutMap.NEW();
        map.setv("alterHurt", Integer.valueOf(alterHurt));
        return map;
      } 
    } 
    return null;
  }
}
