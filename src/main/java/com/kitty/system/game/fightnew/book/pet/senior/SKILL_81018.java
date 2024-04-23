package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.BeforeActionSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_81018 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81018);
    SysPetSkill petSkill = (SysPetSkill)args[1];
    BeforeActionSkill beforeActionSkill = (BeforeActionSkill)fight.getBeforeActionMap().get(Boolean.valueOf(target.isASide()));
    Map<Integer, SysPetSkill> resultMap = (Map<Integer, SysPetSkill>)PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
    SysPetSkill coverPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, (List)effector.getSkillIds().stream().filter(roleSkill -> (roleSkill.getSkillId().intValue() > 1)).collect(Collectors.toList()), resultMap);
    if (coverPetSkill != null && !coverPetSkill.getSerial().equals(petSkill.getSerial()) && 
      beforeActionSkill.getSkills().contains(coverPetSkill.getSerial()))
      return null; 
    if (effector.getSkillCoolMap().getInt(String.valueOf(81018)) > 0)
      return null; 
    effector.getSkillCoolMap().setv(String.valueOf(81018), Integer.valueOf(fight.getMaxRound()));
    if (!beforeActionSkill.getSkills().contains(Integer.valueOf(81018)))
      return null; 
    Integer skillid = beforeActionSkill.getSkills().stream().filter(skill -> (81018 == skill.intValue())).findAny().get();
    if (skillid != null && skillid.intValue() > 0) {
      int hurt = ((Integer)args[0]).intValue();
      int oldCurrMana = target.getCurrMana();
      int surplusMana = oldCurrMana - hurt;
      int changeMana = -hurt;
      if (surplusMana <= 0)
        changeMana = -target.getCurrMana(); 
      ActionParamUtils.changeMana(fight, effector, target, 7, 81018, 2, changeMana);
      PetSkillEffectorUtil.enemyFirstMagicCancelAction(fight, effector, target, beforeActionSkill, skillid);
      skillRecord.setT(target.getId().intValue());
    } 
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
