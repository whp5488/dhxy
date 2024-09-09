package com.kitty.system.game.fightnew.book.tianfu.drivebeasts;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Optional;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.cache.DataCache;
import java.util.List;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_323301 extends TianFuProcessor
{
    public FU_323301() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public NutMap otherExcute(Fight fight, FightObject fightObject, Object... args) {
        if (!fightObject.isPet()) {
            return null;
        }
        Boolean flag = (Boolean)args[0];
        Role role = fightObject.getRole();
        if ((boolean)flag) {
            List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
            if (roleTianFus != null && roleTianFus.size() > 0) {
                SkillRecord skillRecord2 = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 323301);
                Optional<RoleTianFu> optional = roleTianFus.stream().filter(obj -> obj.getIsWear() == 1 && obj.getTianFuSerial() == 323301).findFirst();
                if (optional.isPresent()) {
                    RoleTianFu roleTianFu = (RoleTianFu)optional.get();
                    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
                    double percenta = 2.5 * TianFuUtils.getTianFuCount(roleTianFu.getLevel(), roleDayInCe.getLevel()) / 100.0;
                    if (SkillAi.canAffectOnPro(percenta)) {
                        int cnta = 3;
                        fightObject.setIcon(6102);
                        fightObject.setStatus(200, cnta);
                        skillRecord2.setT((long)(int)fightObject.getId());
                    }
                }
            }
        }
        else {
            int statusValue = fightObject.getStatusValue(200);
            if (statusValue <= 0) {
                Map<Integer, Pet> petMap = SpringUtils.getPetService().selectPet(role);
                Optional<Pet> optionalPet = petMap.values().stream().filter(pet -> pet.getPetId().equals(fightObject.getId())).findFirst();
                if (optionalPet.isPresent()) {
                	Pet pet = (Pet)optionalPet.get();
                    fightObject.setIcon((int)pet.getShape());
                    SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)fightObject.getId(), 323301);
                    skillRecord.setT((long)(int)fightObject.getId());
                }
            }
        }
        return null;
    }
}
