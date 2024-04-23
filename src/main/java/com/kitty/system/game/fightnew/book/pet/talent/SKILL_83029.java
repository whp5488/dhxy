package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83029 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    double percent = 0.05D;
    double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
    if (k_hl > 0.0D) {
      k_hl = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hl), String.valueOf(1.0D + percent)));
    } else {
      k_hl = k_hl;
    } 
    effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), Double.valueOf(k_hl));
    double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
    if (k_fy > 0.0D) {
      k_fy = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_fy), String.valueOf(1.0D + percent)));
    } else {
      k_fy = k_fy;
    } 
    effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), Double.valueOf(k_fy));
    double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
    if (k_hs > 0.0D) {
      k_hs = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_hs), String.valueOf(1.0D + percent)));
    } else {
      k_hs = k_hs;
    } 
    effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), Double.valueOf(k_hs));
    double k_yw = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()));
    if (k_yw > 0.0D) {
      k_yw = Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(k_yw), String.valueOf(1.0D + percent)));
    } else {
      k_yw = k_yw;
    } 
    effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_YW.getDist()), Double.valueOf(k_yw));
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83029);
    int round = 3;
    if (args.length > 0) {
      if (args[0] instanceof Boolean) {
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1D + 12.0D * (effector.getRelive() * 0.6D + 1.0D) * 
              PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0D));
        double reducePercent = 0.5D;
        target.getStatusMap().setv(String.valueOf(2010), Integer.valueOf(round));
        target.getStatusMap().setv("2010value", effector.getId());
        target.getStatusMap().setv("2010skill", Integer.valueOf(83029));
        target.getStatusMap().setv("2010percent", Double.valueOf(percent));
        target.getStatusMap().setv("2010reducePercent", Double.valueOf(reducePercent));
        Effect effect = new Effect();
        effect.setType(2010);
        effect.setTime(round);
        skillRecord.setT(target.getId().intValue());
        return effect;
      } 
      if (args[0] instanceof String && "die".equals(args[0])) {
        List<FightObject> mySide = fight.getMySide(effector.getId().intValue());
        List<Integer> clearStatusList = new ArrayList<>();
        clearStatusList.add(Integer.valueOf(2010));
        for (FightObject unit : mySide) {
          if (unit.getStatusMap().getInt(String.valueOf(2010)) > 0) {
            unit.getStatusMap().remove(String.valueOf(2010));
            unit.getStatusMap().remove("2010value");
            unit.getStatusMap().remove("2010skill");
            unit.getStatusMap().remove("2010percent");
            unit.getStatusMap().remove("2010reducePercent");
            ActionParamUtils.clearStatus(fight, effector, unit, 7, 83029, 2, clearStatusList);
          } 
        } 
      } 
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    if (args != null && args.length >= 2) {
      FightObject effector = (FightObject)args[0];
      Fight fight = (Fight)args[1];
      if (fight != null && effector != null && AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31510))
        return 0; 
    } 
    int level = PetSkillEffectorUtil.getExcuteLevel(args);
    return level * 21;
  }
  
  public boolean isExcute(Fight fight, FightObject effector, Object... args) {
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && !effector.getId().equals(obj.getId()))).collect(Collectors.toList());
    if (mySide.size() <= 0)
      return false; 
    return true;
  }
  
  public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
    int coolRound = 5;
    effector.getSkillCoolMap().setv(String.valueOf(83029), Integer.valueOf(coolRound));
    return true;
  }
  
  public List<Integer> getTargetIds(Fight fight, FightObject effector) {
    List<FightObject> mySide = (List<FightObject>)fight.getMySide(effector.getId().intValue()).stream().filter(obj -> (!obj.isFallDown() && !obj.getId().equals(effector.getId()))).collect(Collectors.toList());
    if (mySide != null && mySide.size() > 0) {
      List<Integer> targetIds = new ArrayList<>();
      for (FightObject target : mySide)
        targetIds.add(target.getId()); 
      return targetIds;
    } 
    return null;
  }
}
