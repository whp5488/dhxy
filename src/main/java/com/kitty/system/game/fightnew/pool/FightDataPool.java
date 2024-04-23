package com.kitty.system.game.fightnew.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.init.service.InitService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightDataPool {
  public static Map<Integer, SysWarRole> data_war_role = new HashMap<>();
  
  public static Map<Integer, SysWxOtherWarData> data_wx_other_war_data = new HashMap<>();
  
  public static Map<Integer, SysDataMonster> data_monster = new HashMap<>();
  
  public static Map<Integer, SysDataMonsterPeripheral> monsterPeripheralMap = new HashMap<>();
  
  public static Map<Integer, SysGuajiWar> data_guaji_war = new HashMap<>();
  
  public static Map<Integer, List<SysGuajiWar>> data_guaji_war_list = new HashMap<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    initGuajiWar(initService);
    initWarRole(initService);
    initDataMonster(initService);
    initMonsterPeripheral(initService);
    initWxOtherWarData(initService);
  }
  
  public static void initGuajiWar(InitService initService) {
    data_guaji_war = initService.initGuajiWar();
    for (Integer key : data_guaji_war.keySet()) {
      SysGuajiWar war = data_guaji_war.get(key);
      List<SysGuajiWar> list = data_guaji_war_list.get(war.getWarDataId());
      if (list == null)
        list = new ArrayList<>(); 
      list.add(war);
      data_guaji_war_list.put(war.getWarDataId(), list);
    } 
  }
  
  public static void initWarRole(InitService initService) {
    data_war_role = initService.initWarRole();
  }
  
  public static void initWxOtherWarData(InitService initService) {
    data_wx_other_war_data = initService.initWxOtherWarData();
  }
  
  public static void initDataMonster(InitService initService) {
    data_monster = initService.initDataMonster();
  }
  
  public static void initMonsterPeripheral(InitService initService) {
    monsterPeripheralMap = initService.initMonsterPeripheral();
  }
}
