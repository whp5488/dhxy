package com.kitty.system.game.fightnew.book.awaken.intensify;

import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.warskill.skilldef.PETSKILLATTR;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.AwakenSkillProcessor;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillEffectUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.MagicAttackParam;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.ArrayList;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class AWAKENSKILL_31514 extends AwakenSkillProcessor {
  public NutMap additionSuit(Role role, Pet pet, Object... args) {
    byte suitLevParam = AcsryPetSkillUtil.getSuitLevParam(role, pet);
    if (suitLevParam > 0) {
      double percenta = getPercent((short)suitLevParam);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  public NutMap additionAwaken(Role role, Pet pet, Object... args) {
    short awaken = pet.getAwaken();
    if (awaken > 0) {
      double percenta = getPercent(awaken);
      NutMap map = NutMap.NEW();
      map.put(String.valueOf(PRO_DICT_TYPE.TM_KB_RATIO.getDist()), Double.valueOf(percenta));
      map.put(String.valueOf(PRO_DICT_TYPE.QM_KB_RATIO.getDist()), Double.valueOf(percenta));
      return map;
    } 
    return null;
  }
  
  private double getPercent(short lev) {
    return (25 * lev) / 20.0D / 100.0D;
  }
  
  public void magicAttackEndOther(Fight fight, FightObject effector, Object... args) {
    if (args != null && args.length > 1) {
      RoleSkill skill = (RoleSkill)args[0];
      if (skill != null && (PETSKILLATTR.PETSKILLATTR_FG
        .getValue() == skill.getType().intValue() || PETSKILLATTR.PETSKILLATTR_XL.getValue() == skill.getType().intValue())) {
        List<MagicAttackParam> magicTargets = (List<MagicAttackParam>)args[1];
        if (magicTargets != null && magicTargets.size() > 0) {
          ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 7, 31514);
          NutMap map = AwakenSkillEffectUtils.getPercentDouble((PetFightObject)effector, 30.0D, 20.0D);
          if (map == null || map.getDouble("percent") <= 0.0D)
            return; 
          double percentb = map.getDouble("percent");
          if (percentb > 1.0D)
            percentb = 1.0D; 
          List<FightObjectParam> targetParams = new ArrayList<>();
          int round = 2;
          int i = 0;
          for (MagicAttackParam magicAttackParam : magicTargets) {
            if (magicAttackParam.getTarget() != null) {
              FightObject target = fight.queryFightObject(magicAttackParam.getTarget().getId());
              if (target != null && !target.isFallDown() && !target.isFrozen()) {
                if (i == 0)
                  actionDetailParam.setTargetId(target.getId().intValue()); 
                i++;
                int maxMana = target.getMaxMana();
                target.setStatus(2169, round);
                target.getStatusSubMap().setv("2169_max_mana", Integer.valueOf(maxMana));
                int newMaxMana = (int)(maxMana * (1.0D - percentb));
                int reduceMana = newMaxMana - target.getMaxMana();
                target.setMaxMana(newMaxMana);
                FightObjectParam targetParam = new FightObjectParam();
                targetParam.setChangeMana(reduceMana);
                if (newMaxMana < target.getCurrMana())
                  target.setCurrMana(newMaxMana); 
                targetParam.setMaxMana(target.getMaxMana());
                targetParam.setMissed(2);
                targetParam.getStatusMap().put(Integer.valueOf(2169), Integer.valueOf(round));
                int size = targetParams.size();
                targetParam.setOrder(size);
                targetParam.setObjId(target.getId().intValue());
                targetParams.add(targetParam);
              } 
            } 
          } 
          if (targetParams.size() > 0) {
            actionDetailParam.setTargetParamList(targetParams);
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 31514);
            skillRecord.setT(actionDetailParam.getTargetId());
          } 
        } 
      } 
    } 
  }
  
  public void statusOver(Fight fight, FightObject fightObject, Object... args) {
    if (args != null && args.length > 0) {
      int status = ((Integer)args[0]).intValue();
      if (2169 == status) {
        int oldMaxMana = fightObject.getMaxMana();
        fightObject.setMaxMana(fightObject.getStatusSubMap().getInt("2169_max_mana"));
        ActionDetailParam actionDetailParam = new ActionDetailParam(fightObject, 43, 0, fightObject.getId().intValue());
        FightObjectParam targetParam = new FightObjectParam();
        targetParam.setMaxMana(fightObject.getMaxMana());
        targetParam.setChangeMana(fightObject.getMaxMana() - oldMaxMana);
        targetParam.setMissed(2);
        targetParam.setOrder(1);
        targetParam.setObjId(fightObject.getId().intValue());
        actionDetailParam.getTargetParamList().add(targetParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
      } 
    } 
  }
}
