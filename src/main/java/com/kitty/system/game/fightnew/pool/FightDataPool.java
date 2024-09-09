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
    
    public FightDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        initGuajiWar(initService);
        initWarRole(initService);
        initDataMonster(initService);
        initMonsterPeripheral(initService);
        initWxOtherWarData(initService);
    }
    
    public static void initGuajiWar(InitService initService) {
        FightDataPool.data_guaji_war = initService.initGuajiWar();
        for (Integer key : FightDataPool.data_guaji_war.keySet()) {
            SysGuajiWar war = (SysGuajiWar)FightDataPool.data_guaji_war.get(key);
            List<SysGuajiWar> list = (List)FightDataPool.data_guaji_war_list.get(war.getWarDataId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(war);
            FightDataPool.data_guaji_war_list.put(war.getWarDataId(), list);
        }
    }
    
    public static void initWarRole(InitService initService) {
        FightDataPool.data_war_role = initService.initWarRole();
    }
    
    public static void initWxOtherWarData(InitService initService) {
        FightDataPool.data_wx_other_war_data = initService.initWxOtherWarData();
    }
    
    public static void initDataMonster(InitService initService) {
        FightDataPool.data_monster = initService.initDataMonster();
    }
    
    public static void initMonsterPeripheral(InitService initService) {
        FightDataPool.monsterPeripheralMap = initService.initMonsterPeripheral();
    }
    
    static {
        FightDataPool.data_war_role = new HashMap();
        FightDataPool.data_wx_other_war_data = new HashMap();
        FightDataPool.data_monster = new HashMap();
        FightDataPool.monsterPeripheralMap = new HashMap();
        FightDataPool.data_guaji_war = new HashMap();
        FightDataPool.data_guaji_war_list = new HashMap();
    }
}
