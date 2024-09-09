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
    public FightKillingIncenseNewHandler() {
    }
    
    @Override
    public void sendReward(Fight fight, Role role, Pet pet) {
    }
    
    @Override
    public void fightFailed(Fight fight, Role role) {
        if (role.getExp() <= 0L) {
            return;
        }
        long roleExp = (long)((double)role.getExp() * 0.5);
        if (roleExp <= 0L) {
            roleExp = role.getExp();
        }
        SpringUtils.getRoleService().addExp(role, -roleExp, null, true);
        return;
    }
    
    @Override
    public void flee(Fight fight, Role role) {
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
