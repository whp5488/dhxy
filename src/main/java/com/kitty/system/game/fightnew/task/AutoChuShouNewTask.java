package com.kitty.system.game.fightnew.task;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.role.domain.Role;

public class AutoChuShouNewTask implements FightingTask {
  private ReqFightHandle reqFightHandle;
  
  private Fight fight;
  
  public AutoChuShouNewTask(ReqFightHandle reqFightHandle, Fight fight) {
    this.reqFightHandle = reqFightHandle;
    this.fight = fight;
  }
  
  public void run() {
    ((FightExecutorNewService)SpringUtils.getBean(FightExecutorNewService.class)).addFightTask(this.fight, () -> {
          FightObject fightObject = this.fight.queryFightObject(Integer.valueOf(this.reqFightHandle.getAttackerId()));
          if (fightObject != null) {
            if (fightObject.getRace() == RACE.RACE_LONG.getValue() && fightObject.getBorn() == 1 && fightObject.getAutoFight() == 1 && fightObject.getStatusMap().getInt(String.valueOf(1023)) <= 0) {
              int round = fightObject.getSkillCoolMap().getInt(String.valueOf(30041));
              if (round <= 0) {
                fightObject.setMorph(30041);
              } else {
                Role role = fightObject.getRole();
                RespNotifyMiscEx respNotifyMiscEx = new RespNotifyMiscEx("变身技能冷却中。");
                MessagePusher.pushMessage(role, (Message)respNotifyMiscEx);
              } 
            } 
            ((FightNewService)SpringUtils.getBean(FightNewService.class)).doActionNew(this.reqFightHandle);
            if (fightObject.isPet()) {
              this.fight.removePetTask(this.reqFightHandle.getAttackerId());
            } else if (fightObject.isPlayer()) {
              this.fight.removeHostTask(this.reqFightHandle.getAttackerId());
            } 
          } 
        });
  }
}
