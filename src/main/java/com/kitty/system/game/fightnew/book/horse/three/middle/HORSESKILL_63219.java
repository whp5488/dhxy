
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
    @Override
    public NutMap addition(final HorseSkillParam horseSkillParam, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final PetFightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap studyExcute(final Pet pet, final HorseSkillParam horseSkillParam, final Object... obj) {
        double percenta = (20.0 * HorseSkillUtils.getPercentMiddle(horseSkillParam) + 0.1) / 100.0;
        percenta = HorseSkillUtils.getMathRoundPercent(percenta);
        if (percenta != 0.0) {
            final NutMap map = NutMap.NEW();
            final Role player = SpringUtils.getRoleService().getPlayer(pet.getRoleId());
            final List<PetEquip> equips = SpringUtils.getPetService().getPetEffective(player.getId(), pet);
            if (pet.getMaxMana() > 0) {
                final int petMana = CountUtils.getPetMana(pet, equips);
                final int addLife = BigDecimalUtil.decimalScaleInteger(0, petMana * percenta);
                map.setv(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()), (Object)addLife);
            }
            final int petLife = CountUtils.getPetLife(pet, equips);
            final int addMana = BigDecimalUtil.decimalScaleInteger(0, petLife * percenta);
            map.setv(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()), (Object)addMana);
        }
        return null;
    }
}
