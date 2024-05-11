
package com.kitty.system.game.fightnew.task;

import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.bean.Fight;

public class AutoRoundFinishTask implements FightingTask
{
    private int objId;
    private Fight fight;
    
    public AutoRoundFinishTask() {
    }
    
    public AutoRoundFinishTask(final Fight fight, final int objId) {
        this.fight = fight;
        this.objId = objId;
    }
    
    @Override
    public void run() {
        SpringUtils.getBean(FightExecutorNewService.class).addFightTask(this.fight, () -> SpringUtils.getBean(FightNewService.class).animationPlayEnd(this.fight, this.objId));
    }
}
