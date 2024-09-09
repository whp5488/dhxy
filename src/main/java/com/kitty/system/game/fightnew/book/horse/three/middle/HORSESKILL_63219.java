package com.kitty.system.game.fightnew.book.horse.three.middle;

import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.horse.HorseSkillProcessor;

public class HORSESKILL_63219 extends HorseSkillProcessor
{
    public HORSESKILL_63219() {
    }
    
    @Override
    public NutMap addition(HorseSkillParam horseSkillParam, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, PetFightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(Pet pet, HorseSkillParam horseSkillParam, Object... obj) {
        double percenta = (20.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            NutMap map = NutMap.NEW();
            Role player = SpringUtils.getRoleService().getPlayer((int)pet.getRoleId());
            List<PetEquip> equips = SpringUtils.getPetService().getPetEffective((long)player.getId(), pet);
            if ((int)pet.getMaxMana() > 0) {
                int petMana = CountUtils.getPetMana(pet, equips);
                int addLife = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf((double)petMana * percenta));
                map.setv(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()), Integer.valueOf(addLife));
            }
            int petLife = CountUtils.getPetLife(pet, equips);
            int addMana = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf((double)petLife * percenta));
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()), Integer.valueOf(addMana));
        }
        return null;
    }
}
