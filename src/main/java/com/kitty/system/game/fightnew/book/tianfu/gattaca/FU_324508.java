package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.common.utils.BigDecimalUtil;
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
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import java.util.Optional;
import org.nutz.lang.util.NutMap;

public class FU_324508 extends TianFuProcessor {
  public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public void attackOver(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
    if (effector.getRace() != RACE.RACE_LONG.getValue() || !target.isPlayer())
      return; 
    DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
    TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324508);
    if (tianFuObject == null)
      return; 
    RoleSkill roleSkill = (RoleSkill)args[0];
    if (roleSkill == null)
      return; 
    double percenta = 3.0D * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0D;
    Optional<FightObject> optional = fight.getMySide(target.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && obj.getHostId() == target.getId().intValue() && obj.isPet())).findFirst();
    if (!optional.isPresent())
      return; 
    FightObject targetPet = optional.get();
    boolean flag = SkillAi.canAffectOnPro(percenta);
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 324508);
    if (flag && SKILLATTR.SKILLATTR_ZHENJI.getValue() == roleSkill.getType().intValue()) {
      RoleSkill skill = effector.getSkillBy(Integer.valueOf(30039));
      int dict = 1020;
      if (targetPet.getStatusValue(dict) <= 0) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30039));
        int magic_hit_dist = PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist();
        double[] power = sysSkill.getPower();
        double magic_hit = BigDecimalUtil.decimalScaleDouble(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(1), Double.valueOf(power[1] + power[3] * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 20.0D / 100.0D))) / 100.0D));
        ImbueUtil.addRound(targetPet.getImbueMap(), Integer.valueOf(magic_hit_dist), 1, -magic_hit);
        ImbueParam statusValue = ImbueUtil.getStatusValue(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist());
        if (statusValue != null) {
          ImbueUtil.addOrUpdStatus(target.getImbueMap(), dict, PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), statusValue.getSr(), statusValue.getValue());
          targetPet.setStatus(dict, statusValue.getSr());
          FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetPet.getId().intValue(), true);
          targetParam.getStatusMap().put(Integer.valueOf(dict), Integer.valueOf(statusValue.getSr()));
        } 
        skillRecord.setT(target.getId().intValue());
      } 
    } else if (flag && SKILLATTR.SKILLATTR_POJIA.getValue() == roleSkill.getType().intValue()) {
      RoleSkill skill = effector.getSkillBy(Integer.valueOf(30033));
      int dict = 1021;
      if (targetPet.getStatusValue(dict) <= 0) {
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(Integer.valueOf(30033));
        double[] power = sysSkill.getPower();
        double mark = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(1), Double.valueOf(power[1] + power[2] * (1.0D + Math.pow(skill.getProficient().intValue(), 0.35D) * 2.0D / 100.0D))) / 100.0D));
        ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), 1, mark);
        int shdk = PRO_DICT_TYPE.SH_DK.getDist();
        ImbueParam statusValue = ImbueUtil.getStatusValue(targetPet.getImbueMap(), dict, shdk);
        if (statusValue != null) {
          ImbueUtil.addOrUpdStatus(targetPet.getImbueMap(), dict, shdk, statusValue.getSr(), statusValue.getValue());
          targetPet.setStatus(dict, statusValue.getSr());
          FightObjectParam targetParam = actionDetailParam.gainTargetParam(targetPet.getId().intValue(), true);
          targetParam.getStatusMap().put(Integer.valueOf(dict), Integer.valueOf(statusValue.getSr()));
        } 
        skillRecord.setT(target.getId().intValue());
      } 
    } 
  }
}
