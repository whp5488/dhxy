package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.AstrictSkill;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_81061 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 81061);
    SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(81061));
    if (petSkill != null) {
      List<RoleSkill> skills = (List<RoleSkill>)effector.getSkillIds().stream().filter(roleSkill -> (roleSkill.getSkillId().intValue() > 0)).collect(Collectors.toList());
      Map<Integer, SysPetSkill> sysPetSkillMap = (Map<Integer, SysPetSkill>)PetTriggerPassiveSkill.data_pet_skill_maps.get(petSkill.getTriggerType());
      SysPetSkill sysPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, skills, sysPetSkillMap);
      if (sysPetSkill != null && 81061 != sysPetSkill.getSerial().intValue()) {
        SkillProcessor processor = SkillProcessor.queryProcessor(sysPetSkill.getSerial());
        processor.passive(fight, effector, new Object[0]);
        return null;
      } 
    } 
    boolean isASide = effector.isASide();
    AstrictSkill astrictSkill = (AstrictSkill)fight.getAstrictSkillMap().get(Boolean.valueOf(!isASide));
    boolean isLayIn = true;
    if (astrictSkill != null)
      isLayIn = false; 
    if (isLayIn) {
      astrictSkill = new AstrictSkill();
      astrictSkill.setASide(!isASide);
      astrictSkill.setRound(1);
      astrictSkill.setTime(2);
      fight.getAstrictSkillMap().put(Boolean.valueOf(!isASide), astrictSkill);
    } 
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    return 1;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    return 0;
  }
}
