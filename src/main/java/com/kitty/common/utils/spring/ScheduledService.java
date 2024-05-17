
package com.kitty.common.utils.spring;

import com.kitty.common.start.ServerService;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.time.PartyBanditHandler;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.activity.handler.time.WorldBoosHandler;
import com.kitty.system.game.activity.handler.time.StarOfficerHandler;
import com.kitty.system.game.fightnew.FightObject;
import java.util.Map;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.logs.LoggerFunction;
import java.util.Iterator;
import java.util.Collection;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.DataCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.LoggerFactory;
import org.nutz.dao.Dao;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.fightnew.service.FightNewService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService
{
    private final Logger logger;
    @Autowired
    RoleService roleService;
    @Autowired
    FightNewService fightNewService;
    @Autowired
    WelfareService welfareService;
    @Autowired
    Dao dao;
    
    public ScheduledService() {
        this.logger = LoggerFactory.getLogger(ScheduledService.class);
    }
    
    @Scheduled(cron = "0 55 4 1 * ?")
    public void resetPartyTask() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringUtils.getPartyService().resetPartyTask();
        stopWatch.stop();
        this.logger.warn("5点重置帮派任务，用时{}毫秒", stopWatch.getTime());
    }
    
    @Scheduled(cron = "0 59 23 * * ?")
    public void updateActivation() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SpringUtils.getPartyService().updateActivation();
        stopWatch.stop();
        this.logger.warn("23点59分更新帮派活跃度，用时{}毫秒", stopWatch.getTime());
    }
    
    @Scheduled(cron = "0 0 5 * * ?")
    public void resetServerLevel() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        this.logger.warn("5点重置服务器等级，用时{}毫秒", stopWatch.getTime());
    }
    
    @Scheduled(cron = "0 0 5 * * ?")
    public void clearMarketDealRecord() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        this.logger.warn("5点清理交易记录，用时{}毫秒", stopWatch.getTime());
    }
    
    @Scheduled(cron = "0 0 5 * * ?")
    public void dailyFiveReset() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final Collection<Role> roles = DataCache.ONLINE_ROLES.values();
        for (final Role role : roles) {
            ThreadLocalUtil.addLocalTask(role, () -> this.roleService.doDailyFiveReset(role, true));
        }
        stopWatch.stop();
        this.logger.warn("5点重置玩家数据，用时{}毫秒", stopWatch.getTime());
    }
    
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkParty() {
        SpringUtils.getPartyService().check();
    }
    
    @Scheduled(cron = "0 0/5 * * * ?")
    public void delFight() {
        final Logger logger = LoggerFunction.FIGHT.getLogger();
        final int before = FightExecutorNewService.FIGHTS.size();
        final ConcurrentHashMap<Integer, Fight> map = new ConcurrentHashMap<>(FightExecutorNewService.FIGHTS);
        final long now = System.currentTimeMillis();
        for (final Map.Entry<Integer, Fight> entry : map.entrySet()) {
            final Fight fight = entry.getValue();
            if (fight.getCreateTime() + 3600000L < now) {
                logger.error("超过一小时删除战场，删除的战场id={}", fight.getId());
                for (final FightObject fightObject : fight.getAllFightObjects()) {
                    this.fightNewService.quiteFight(fight, fightObject, false);
                    logger.error("超过一小时删除战场时，清除的角色id={}", fightObject.getId());
                }
                this.fightNewService.clearFight(fight);
            }
        }
        final int end = FightExecutorNewService.FIGHTS.size();
        if (end != before) {
            logger.error("每五分钟检查一次战场，当前战场数量={}, 删除后数量={}", before, end);
        }
    }
    
    @Scheduled(cron = "0 0/30 * * * ? ")
    public void starOfficer() {
        this.welfareService.stopWatah();
        SpringUtils.getBean(StarOfficerHandler.class).flushStarAfterServerStart();
    }
    
    @Scheduled(cron = "0 0 * * * ?")
    public void boosOfficer() {
        SpringUtils.getBean(WorldBoosHandler.class).flushBoosAfterServerStart();
    }
    
    @Scheduled(cron = "0 0 * * * ?")
    public void banditOfficer() {
        final int hour = TimeUtil.hour(true);
        if (hour >= 10 && hour <= 18) {
            SpringUtils.getBean(PartyBanditHandler.class).flushAfterServerStart();
        }
    }
    
    @Scheduled(cron = "0 20 19 * * ?")
    public void notifyAt() {
        SpringUtils.getBean(WaterHandler.class).notifyAt();
        SpringUtils.getBean(AtWillHandler.class).notifyAt();
        SpringUtils.getBean(GangWarsHandler.class).notifyAt((byte)1);
        SpringUtils.getBean(CaHandler.class).notifyAt();
    }

    /**
     * 测试PK
     */
    @Scheduled(cron = "0 29 5 * * ?")
    public void notifyAtTest() {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        // 长安pk
        SpringUtils.getBean(CaHandler.class).notifyAt();
    }
    
    @Scheduled(cron = "0 50 21 * * ?")
    public void kjQuestion() {
        SpringUtils.getBean(KejuTaskHandler.class).hallCertification();
    }
    
    @Scheduled(cron = "0 0 3 * * ?")
    public void clearFightRecord() {
        SpringUtils.getBean(FightRecordService.class).clearFightRecordScheduled();
    }
}
