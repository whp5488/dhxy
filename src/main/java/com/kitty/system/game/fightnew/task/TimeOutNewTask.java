package com.kitty.system.game.fightnew.task;

import java.util.Iterator;
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
    
    public TimeOutNewTask(int fightId) {
        this.logger = LoggerFunction.FIGHT.getLogger();
        this.fightId = fightId;
    }
    
    @Override
    public void run() {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        Fight fight = fightNewService.getFight(this.fightId);
        if (fight == null) {
            return;
        }
        fight.cancelTask(fight.getTimeOutTask());
        FightExecutorNewService fightExecutorNewService = (FightExecutorNewService)SpringUtils.getBean(FightExecutorNewService.class);
        fightExecutorNewService.addFightTask(fight, this::timeOutAction);
        return;
    }
    
    private void timeOutAction() {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        Fight fight = fightNewService.getFight(this.fightId);
        for (FightObject fightObject : fight.getAllFightObjects()) {
            fight.removeHostTask((int)fightObject.getId());
            fight.removePetTask((int)fightObject.getId());
        }
        for (FightObject fightObject : fight.getAllFightObjects()) {
            if (!fightObject.isDoAction()) {
                if (fightObject.isPlayer()) {
                    fightObject.getRole().getExtendBox().setAutoFight((byte)1);
                    RespUpdate respUpdate = new RespUpdate();
                    respUpdate.setList(Collections.singletonList(new FiedValue("autoFight", (byte)1, Integer.valueOf(1))));
                    respUpdate.setId((int)fightObject.getId());
                    MessagePusher.pushMessage(fightObject.getRole(), respUpdate);
                }
                if (fightObject.isPlayer() || fightObject.isPet()) {
                    fightObject.setAutoFight(1);
                    fightNewService.autoFightOne(fight, fightObject, 100L);
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
    }
}
