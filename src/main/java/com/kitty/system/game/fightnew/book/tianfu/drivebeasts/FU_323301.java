
package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import java.util.Map;
import java.util.Optional;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323301 extends TianFuProcessor
{
    @Override
    public NutMap addition(final TianFuAddition tianFuAddition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap otherExcute(final Fight fight, final FightObject fightObject, final Object... args) {
        if (!fightObject.isPet()) {
            return null;
        }
        final Boolean flag = (Boolean)args[0];
        final Role role = fightObject.getRole();
        if (flag) {
            final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
            if (roleTianFus != null && roleTianFus.size() > 0) {
                final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 323301);
                final Optional<RoleTianFu> optional = roleTianFus.stream().filter(obj -> obj.getIsWear() == 1 && obj.getTianFuSerial() == 323301).findFirst();
                if (optional.isPresent()) {
                    final RoleTianFu roleTianFu = optional.get();
                    final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
                    final double percenta = 2.5 * TianFuUtils.getTianFuCount(roleTianFu.getLevel(), roleDayInCe.getLevel()) / 100.0;
                    if (SkillAi.canAffectOnPro(percenta)) {
                        final int cnta = 3;
                        fightObject.setIcon(6102);
                        fightObject.setStatus(200, cnta);
                        skillRecord.setT(fightObject.getId());
                    }
                }
            }
        }
        else {
            final int statusValue = fightObject.getStatusValue(200);
            if (statusValue <= 0) {
                final Map<Integer, Pet> petMap = SpringUtils.getPetService().selectPet(role);
                final Optional<Pet> optionalPet = petMap.values().stream().filter(pet -> pet.getPetId().equals(fightObject.getId())).findFirst();
                if (optionalPet.isPresent()) {
                    Pet pet = optionalPet.get();
                    fightObject.setIcon(pet.getShape());
                    final SkillRecord skillRecord2 = FightParamPool.addSkillRecord(fight, 7, fightObject.getId(), 323301);
                    skillRecord2.setT(fightObject.getId());
                }
            }
        }
        return null;
    }
}
