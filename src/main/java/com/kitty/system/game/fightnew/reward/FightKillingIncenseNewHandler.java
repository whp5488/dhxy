
package com.kitty.system.game.fightnew.reward;

import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.bean.Fight;
import org.springframework.stereotype.Component;

@Component
public class FightKillingIncenseNewHandler extends FightRewardHandler
{
    @Override
    public void sendReward(final Fight fight, final Role role, final Pet pet) {
    }
    
    @Override
    public void fightFailed(final Fight fight, final Role role) {
        if (role.getExp() <= 0L) {
            return;
        }
        long roleExp = (long)(role.getExp() * 0.5);
        if (roleExp <= 0L) {
            roleExp = role.getExp();
        }
        SpringUtils.getRoleService().addExp(role, -roleExp, null, true);
    }
    
    @Override
    public void flee(final Fight fight, final Role role) {
    }
    
    @Override
    public int getType() {
        return FIGHT_TYPE.FORCE_PK.getValue();
    }
    
    @Override
    public void init() {
        super.putHndlers(this);
    }
}
