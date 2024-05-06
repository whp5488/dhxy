package com.kitty.common.utils.spring;

import com.kitty.common.utils.TimeUtil;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.activity.handler.time.PartyBanditHandler;
import com.kitty.system.game.activity.handler.time.StarOfficerHandler;
import com.kitty.system.game.activity.handler.time.WorldBoosHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.welfare.service.WelfareService;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {
  private final Logger logger = LoggerFactory.getLogger(ScheduledService.class);
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  FightNewService fightNewService;
  
  @Autowired
  WelfareService welfareService;
  
  @Autowired
  Dao dao;
  
  @Scheduled(cron = "0 55 4 1 * ?")
  public void resetPartyTask() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    SpringUtils.getPartyService().resetPartyTask();
    stopWatch.stop();
    this.logger.warn("5点重置帮派任务，用时{}毫秒", stopWatch.getTime());
  }
  
  @Scheduled(cron = "0 59 23 * * ?")
  public void updateActivation() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    SpringUtils.getPartyService().updateActivation();
    stopWatch.stop();
    this.logger.warn("23点59分更新帮派活跃度，用时{}毫秒", stopWatch.getTime());
  }
  
  @Scheduled(cron = "0 0 5 * * ?")
  public void resetServerLevel() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.stop();
    this.logger.warn("5点重置服务器等级，用时{}毫秒", stopWatch.getTime());
  }
  
  @Scheduled(cron = "0 0 5 * * ?")
  public void clearMarketDealRecord() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    stopWatch.stop();
    this.logger.warn("5点清理交易记录，用时{}毫秒", stopWatch.getTime());
  }
  
  @Scheduled(cron = "0 0 5 * * ?")
  public void dailyFiveReset() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Collection<Role> roles = DataCache.ONLINE_ROLES.values();
    for (Role role : roles)
      ThreadLocalUtil.addLocalTask(role, () -> this.roleService.doDailyFiveReset(role, true)); 
    stopWatch.stop();
    this.logger.warn("5点重置玩家数据，用时{}毫秒", stopWatch.getTime());
  }
  
  @Scheduled(cron = "0 0/5 * * * ?")
  public void checkParty() {
    SpringUtils.getPartyService().check();
  }
  
  @Scheduled(cron = "0 0/5 * * * ?")
  public void delFight() {
      Logger logger = LoggerFunction.FIGHT.getLogger();
      int before = FightExecutorNewService.FIGHTS.size();
      ConcurrentHashMap<Integer, Fight> map = new ConcurrentHashMap<>(FightExecutorNewService.FIGHTS);
      long now = System.currentTimeMillis();
      for (Map.Entry<Integer, Fight> entry : map.entrySet()) {
          Fight fight = entry.getValue();
          if (fight.getCreateTime() + 3600000L < now) {
              logger.error("超过一小时删除战场，删除的战场id={}", fight.getId());
              for (FightObject fightObject : fight.getAllFightObjects()) {
                  this.fightNewService.quiteFight(fight, fightObject, false);
                  logger.error("超过一小时删除战场时，清除的角色id={}", fightObject.getId());
              }
              this.fightNewService.clearFight(fight);
          }
      }
      int end = FightExecutorNewService.FIGHTS.size();
      if (end != before)
          logger.error("每五分钟检查一次战场，当前战场数量={}, 删除后数量={}", before, end);
  }
  
  @Scheduled(cron = "0 0/30 * * * ? ")
  public void starOfficer() {
    this.welfareService.stopWatah();
    ((StarOfficerHandler)SpringUtils.<StarOfficerHandler>getBean(StarOfficerHandler.class)).flushStarAfterServerStart();
  }
  
  @Scheduled(cron = "0 0 * * * ?")
  public void boosOfficer() {
    ((WorldBoosHandler)SpringUtils.<WorldBoosHandler>getBean(WorldBoosHandler.class)).flushBoosAfterServerStart();
  }
  
  @Scheduled(cron = "0 0 * * * ?")
  public void banditOfficer() {
    int hour = TimeUtil.hour(true);
    if (hour >= 10 && hour <= 18)
      ((PartyBanditHandler)SpringUtils.<PartyBanditHandler>getBean(PartyBanditHandler.class)).flushAfterServerStart(); 
  }
  
  @Scheduled(cron = "0 20 19 * * ?")
  public void notifyAt() {
    ((WaterHandler)SpringUtils.<WaterHandler>getBean(WaterHandler.class)).notifyAt();
    ((AtWillHandler)SpringUtils.<AtWillHandler>getBean(AtWillHandler.class)).notifyAt();
    ((GangWarsHandler)SpringUtils.<GangWarsHandler>getBean(GangWarsHandler.class)).notifyAt((byte)1);
    ((CaHandler)SpringUtils.<CaHandler>getBean(CaHandler.class)).notifyAt();
  }
  
  @Scheduled(cron = "0 50 21 * * ?")
  public void kjQuestion() {
    ((KejuTaskHandler)SpringUtils.<KejuTaskHandler>getBean(KejuTaskHandler.class)).hallCertification();
  }
  
  @Scheduled(cron = "0 0 3 * * ?")
  public void clearFightRecord() {
    ((FightRecordService)SpringUtils.<FightRecordService>getBean(FightRecordService.class)).clearFightRecordScheduled();
  }
}
