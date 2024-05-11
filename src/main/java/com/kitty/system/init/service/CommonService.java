
package com.kitty.system.init.service;

import com.kitty.mina.ProtocalSet;
import java.util.Map;
import com.kitty.system.game.fightnew.substitution.service.SubstitutionService;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.mina.ServerConfig;
import com.kitty.system.game.market.service.MarketService;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.instruct.service.InstructService;
import com.kitty.mina.cache.Deploy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService
{
    @Autowired
    private InitService initService;
    
    public void loadProduct() {
        final Map<String, String> map = this.initService.initCommonSet();
        final Map<String, String> version = this.initService.initVersion();
        this.initProtocol();
        Deploy.load(map);
        SpringUtils.getBean(InstructService.class).init();
        SpringUtils.getPartyService().loadParty();
        SpringUtils.getBean(ServerService.class).init(map, version);
        WelfareDataPool.init(map);
        SpringUtils.getBean(MarketService.class).init();
        SpringUtils.getBean(ServerConfig.class).init();
        SpringUtils.getBean(AtWillHandler.class).loadCommonSet(map);
        SpringUtils.getBean(WaterHandler.class).loadCommonSet(map);
        SpringUtils.getBean(GangWarsHandler.class).loadCommonSet(map);
        SpringUtils.getBean(CaHandler.class).loadCommonSet(map);
        SpringUtils.getBean(SubstitutionService.class).init();
    }
    
    private void initProtocol() {
        ProtocalSet.loginSet.add(12290);
        ProtocalSet.loginSet.add(45627);
        ProtocalSet.loginSet.add(4274);
        ProtocalSet.loginSet.add(8284);
        ProtocalSet.loginSet.add(4192);
        ProtocalSet.loginSet.add(45144);
        ProtocalSet.loginSet.add(9040);
        ProtocalSet.loginSet.add(45073);
        ProtocalSet.loginSet.add(45057);
        ProtocalSet.loginSet.add(45696);
        ProtocalSet.loginSet.add(24112);
        ProtocalSet.loginSet.add(63752);
    }
}
