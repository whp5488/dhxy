package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.RACE;
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
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class FU_324505 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    if (!effector.isPlayer() && effector.getRace() != RACE.RACE_REN.getValue() && effector.getRace() != RACE.RACE_GUI.getValue())
      return null; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324505);
    if (tianFuObject == null)
      return null; 
    Optional<RoleSkill> skillOptional = effector.getSkillIds().stream().filter(obj -> obj.getSkillId().equals(Integer.valueOf(effector.getSkillId()))).findFirst();
    if (skillOptional.isPresent()) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324505);
      RoleSkill roleSkill = skillOptional.get();
      double percenta = 0.8D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
      if (SKILLATTR.SKILLATTR_CONFUSE.getValue() == roleSkill.getType().intValue()) {
        addProperty(fight, effector, 1002, PRO_DICT_TYPE.RISE_HL.getDist(), percenta);
        skillRecord.setT(effector.getId().intValue());
      } else if (SKILLATTR.SKILLATTR_ICE.getValue() == roleSkill.getType().intValue()) {
        addProperty(fight, effector, 1003, PRO_DICT_TYPE.RISE_FY.getDist(), percenta);
        skillRecord.setT(effector.getId().intValue());
      } else if (SKILLATTR.SKILLATTR_SLEEP.getValue() == roleSkill.getType().intValue()) {
        addProperty(fight, effector, 1004, PRO_DICT_TYPE.RISE_HS.getDist(), percenta);
        skillRecord.setT(effector.getId().intValue());
      } else if (SKILLATTR.SKILLATTR_YIWANG.getValue() == roleSkill.getType().intValue()) {
        addProperty(fight, effector, 1006, PRO_DICT_TYPE.RISE_YW.getDist(), percenta);
        skillRecord.setT(effector.getId().intValue());
      } 
    } 
    return null;
  }
  
  public boolean getTriggerPercent(Fight fight, Integer effectorId, int status) {
    double percent = 0.1D;
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effectorId.intValue()).stream().filter(obj -> (obj.getStatusValue(status) > 0)).collect(Collectors.toList());
    if (mySide != null && mySide.size() > 0)
      percent += percent * mySide.size(); 
    return SkillAi.canAffectOnPro(percent);
  }
  
  private void addProperty(Fight fight, FightObject effector, int status, int dist, double percenta) {
    if (getTriggerPercent(fight, effector.getId(), status))
      ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(dist), 1, percenta); 
  }
}
