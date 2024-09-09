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
    
    public CommonService() {
    }
    
    public void loadProduct() {
        Map<String, String> map = this.initService.initCommonSet();
        Map<String, String> version = this.initService.initVersion();
        this.initProtocol();
        Deploy.load(map);
        ((InstructService)SpringUtils.getBean(InstructService.class)).init();
        SpringUtils.getPartyService().loadParty();
        ((ServerService)SpringUtils.getBean(ServerService.class)).init(map, version);
        WelfareDataPool.init(map);
        ((MarketService)SpringUtils.getBean(MarketService.class)).init();
        ((ServerConfig)SpringUtils.getBean(ServerConfig.class)).init();
        ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).loadCommonSet(map);
        ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).loadCommonSet(map);
        ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).loadCommonSet(map);
        ((CaHandler)SpringUtils.getBean(CaHandler.class)).loadCommonSet(map);
        ((SubstitutionService)SpringUtils.getBean(SubstitutionService.class)).init();
    }
    
    private void initProtocol() {
        ProtocalSet.loginSet.add(Integer.valueOf(1833));
        ProtocalSet.loginSet.add(Integer.valueOf(45627));
        ProtocalSet.loginSet.add(Integer.valueOf(4274));
        ProtocalSet.loginSet.add(Integer.valueOf(9005));
        ProtocalSet.loginSet.add(Integer.valueOf(26890));
        ProtocalSet.loginSet.add(Integer.valueOf(45144));
        ProtocalSet.loginSet.add(Integer.valueOf(9040));
        ProtocalSet.loginSet.add(Integer.valueOf(45073));
        ProtocalSet.loginSet.add(Integer.valueOf(45057));
        ProtocalSet.loginSet.add(Integer.valueOf(45696));
        ProtocalSet.loginSet.add(Integer.valueOf(24112));
        ProtocalSet.loginSet.add(Integer.valueOf(63752));
    }
}
