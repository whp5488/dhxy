package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.nutz.lang.util.NutMap;

public class SKILL_83020 extends SkillProcessor {
  public NutMap addition(Addition addition, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, FightObject effector, Object... args) {
    return null;
  }
  
  public int targetCount(Fight fight, FightObject effector, Object... args) {
    List<FightObject> targetSide = (List<FightObject>)fight.getTargetSide(effector.getId().intValue()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
    if (targetSide.size() < 2)
      return targetSide.size(); 
    return 2;
  }
  
  public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
    RoleSkill skill = effector.getSkillBy(Integer.valueOf(83020));
    Effect e = new Effect();
    if (skill != null) {
      SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId().intValue(), 83020);
      int level = effector.getLevel();
      if (level <= 0)
        level = 1; 
      double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((80 * level) + (effector.getMaxMana() / 100 * 6) * (effector.getRelive() * 0.6D + 1.0D) * 
            PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
      int harm = 0;
      if (!target.isFrozen()) {
        SysPetSkill sysSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(83020));
        ArrayList<FightObject> objects = fight.getSideAllFightObjects(effector.isASide());
        int die = 0;
        for (FightObject object : objects) {
          if (object.isFallDown())
            die++; 
        } 
        double[] power = sysSkill.getPower();
        Integer proficient = skill.getProficient();
        if (proficient == null)
          proficient = Integer.valueOf(1); 
        double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_GH.getDist());
        double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_GH.getDist());
        double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_GH.getDist());
        double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
        harm = SkillCompute.computeSkillDamageWildfire(effector, power, effector.getLevel(), proficient.intValue(), kx, hkx, extra, weak, die, wx).intValue();
        harm = (int)(harm + hurt);
      } 
      e.setType(100000);
      e.setHurt(harm);
      skillRecord.setT(target.getId().intValue());
    } 
    return e;
  }
  
  public int costMana(RoleSkill skill, Object... args) {
    if (args != null && args.length >= 2) {
      FightObject effector = (FightObject)args[0];
      if (effector != null)
        return effector.getLevel() * 32; 
    } 
    return 0;
  }
}
