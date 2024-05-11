
package com.kitty.system.game.fightnew.task;

import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import java.util.Collections;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.logs.LoggerFunction;
import org.slf4j.Logger;

public class TimeOutNewTask implements FightingTask
{
    private final Logger logger;
    private int fightId;
    
    public TimeOutNewTask(final int fightId) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.fightId = fightId;
    }
    
    @Override
    public void run() {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final Fight fight = fightNewService.getFight(this.fightId);
        if (fight == null) {
            return;
        }
        fight.cancelTask(fight.getTimeOutTask());
        final FightExecutorNewService fightExecutorNewService = SpringUtils.getBean(FightExecutorNewService.class);
        fightExecutorNewService.addFightTask(fight, this::timeOutAction);
    }
    
    private void timeOutAction() {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final Fight fight = fightNewService.getFight(this.fightId);
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            fight.removeHostTask(fightObject.getId());
            fight.removePetTask(fightObject.getId());
        }
        for (final FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isDoAction()) {
                if (fightObject.isPlayer()) {
                    fightObject.getRole().getExtendBox().setAutoFight((byte)1);
                    final RespUpdate respUpdate = new RespUpdate();
                    respUpdate.setList(Collections.singletonList(new FiedValue("autoFight", (byte)1, 1)));
                    respUpdate.setId(fightObject.getId());
                    MessagePusher.pushMessage(fightObject.getRole(), respUpdate);
                }
                if (!fightObject.isPlayer() && !fightObject.isPet()) {
                    continue;
                }
                fightObject.setAutoFight(1);
                fightNewService.autoFightOne(fight, fightObject, 100L);
            }
        }
    }
}
