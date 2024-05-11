
package com.kitty.system.game.fightnew.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.List;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Map;

public class FightDataPool
{
    public static Map<Integer, SysWarRole> data_war_role;
    public static Map<Integer, SysWxOtherWarData> data_wx_other_war_data;
    public static Map<Integer, SysDataMonster> data_monster;
    public static Map<Integer, SysDataMonsterPeripheral> monsterPeripheralMap;
    public static Map<Integer, SysGuajiWar> data_guaji_war;
    public static Map<Integer, List<SysGuajiWar>> data_guaji_war_list;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        initGuajiWar(initService);
        initWarRole(initService);
        initDataMonster(initService);
        initMonsterPeripheral(initService);
        initWxOtherWarData(initService);
    }
    
    public static void initGuajiWar(final InitService initService) {
        FightDataPool.data_guaji_war = initService.initGuajiWar();
        for (final Integer key : FightDataPool.data_guaji_war.keySet()) {
            final SysGuajiWar war = FightDataPool.data_guaji_war.get(key);
            List<SysGuajiWar> list = FightDataPool.data_guaji_war_list.get(war.getWarDataId());
            if (list == null) {
                list = new ArrayList<SysGuajiWar>();
            }
            list.add(war);
            FightDataPool.data_guaji_war_list.put(war.getWarDataId(), list);
        }
    }
    
    public static void initWarRole(final InitService initService) {
        FightDataPool.data_war_role = initService.initWarRole();
    }
    
    public static void initWxOtherWarData(final InitService initService) {
        FightDataPool.data_wx_other_war_data = initService.initWxOtherWarData();
    }
    
    public static void initDataMonster(final InitService initService) {
        FightDataPool.data_monster = initService.initDataMonster();
    }
    
    public static void initMonsterPeripheral(final InitService initService) {
        FightDataPool.monsterPeripheralMap = initService.initMonsterPeripheral();
    }
    
    static {
        FightDataPool.data_war_role = new HashMap<Integer, SysWarRole>();
        FightDataPool.data_wx_other_war_data = new HashMap<Integer, SysWxOtherWarData>();
        FightDataPool.data_monster = new HashMap<Integer, SysDataMonster>();
        FightDataPool.monsterPeripheralMap = new HashMap<Integer, SysDataMonsterPeripheral>();
        FightDataPool.data_guaji_war = new HashMap<Integer, SysGuajiWar>();
        FightDataPool.data_guaji_war_list = new HashMap<Integer, List<SysGuajiWar>>();
    }
}
