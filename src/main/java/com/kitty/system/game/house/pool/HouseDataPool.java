package com.kitty.system.game.house.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.enums.house.HouseFurniture;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataFurniture;
import com.kitty.system.init.service.InitService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseDataPool {
  public static Map<Integer, SysDataFurniture> furnitureMap = new HashMap<>();
  
  public static List<Integer> house = new ArrayList<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    furnitureMap = initService.initFurniture();
    for (SysDataFurniture furniture : furnitureMap.values()) {
      if (furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
        house.add(Integer.valueOf(furniture.getSerial()));
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(furniture.getSerial()), role -> role.getRoleId());
        house.add(Integer.valueOf(furniture.getHouse()));
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(furniture.getHouse()), role -> role.getRoleId());
      } 
    } 
  }
}
