
package com.kitty.system.game.market.pool;

import java.util.HashMap;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysDataStall;
import com.kitty.system.init.domain.SysDataStallMenu;
import java.util.Map;

public class MarketDataPool
{
    public static Map<Integer, SysDataStallMenu> t_data_stall_menu;
    public static Map<Integer, SysDataStall> t_data_stall;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        initDataStallMenu(initService);
        initDataStall(initService);
    }
    
    public static void initDataStallMenu(final InitService initService) {
        MarketDataPool.t_data_stall_menu = initService.initDataStallMenu();
    }
    
    public static void initDataStall(final InitService initService) {
        MarketDataPool.t_data_stall = initService.initDataStall();
    }
    
    static {
        MarketDataPool.t_data_stall_menu = new HashMap<Integer, SysDataStallMenu>();
        MarketDataPool.t_data_stall = new HashMap<Integer, SysDataStall>();
    }
}
