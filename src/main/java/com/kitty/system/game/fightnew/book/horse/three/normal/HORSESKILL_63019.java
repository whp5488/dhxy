package com.kitty.system.game.fightnew.book.horse.three.normal;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class HORSESKILL_63019 extends HorseSkillProcessor {
  public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
    return null;
  }
  
  public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
    return null;
  }
  
  public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
    double percenta = (20.0D * HorseSkillUtils.getPercent(horseSkillParam) + 0.1D) / 100.0D;
    percenta = HorseSkillUtils.getMathRoundPercent(percenta);
    if (percenta != 0.0D) {
      NutMap map = NutMap.NEW();
      Role player = SpringUtils.getRoleService().getPlayer(pet.getRoleId().intValue());
      List<PetEquip> equips = SpringUtils.getPetService().getPetEffective(player.getId().longValue(), pet);
      if (pet.getMaxMana().intValue() > 0) {
        int petMana = CountUtils.getPetMana(pet, equips);
        int addLife = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(petMana * percenta));
        map.setv(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()), Integer.valueOf(addLife));
      } 
      int petLife = CountUtils.getPetLife(pet, equips);
      int addMana = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf(petLife * percenta));
      map.setv(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()), Integer.valueOf(addMana));
      return map;
    } 
    return null;
  }
}
