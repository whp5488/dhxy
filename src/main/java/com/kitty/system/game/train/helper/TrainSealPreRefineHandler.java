package com.kitty.system.game.train.helper;

import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("TrainSealPreRefine")
public class TrainSealPreRefineHandler extends UpgradeHandler
{
    public TrainSealPreRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Long id, Integer type, String param, String cause) {
        TrainSeal seal = this.getSeal(role, id);
        if (seal == null) {
            this.logger.error("信物洗练预览错误：玩家ID = {}，信物ID = {}", role.getRoleId(), id);
            return null;
        }
        this.sealPreRefine(role, seal, type.byteValue());
        return null;
    }
}
