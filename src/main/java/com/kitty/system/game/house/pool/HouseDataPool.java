package com.kitty.system.game.house.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.init.service.InitService;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.enums.house.HouseFurniture;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.List;
import com.kitty.system.init.domain.SysDataFurniture;
import java.util.Map;

public class HouseDataPool
{
    public static Map<Integer, SysDataFurniture> furnitureMap;
    public static List<Integer> house;
    
    public HouseDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        HouseDataPool.furnitureMap = initService.initFurniture();
        for (SysDataFurniture furniture : HouseDataPool.furnitureMap.values()) {
            if (furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
                HouseDataPool.house.add(Integer.valueOf(furniture.getSerial()));
                MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(furniture.getSerial()), role -> role.getRoleId());
                HouseDataPool.house.add(Integer.valueOf(furniture.getHouse()));
                MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(furniture.getHouse()), role -> role.getRoleId());
            }
            else {
                continue;
            }
        }
    }
    
    static {
        HouseDataPool.furnitureMap = new HashMap();
        HouseDataPool.house = new ArrayList<>();
    }
}
