package com.kitty.system.game.map.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.system.init.service.InitService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class MapDataPool {
  public static Map<Integer, SysGuajiMap> guaJiMap = new HashMap<>();
  
  public static Map<Integer, SysMapInfo> mapInfoMap = new HashMap<>();
  
  public static Map<Integer, List<Integer>> levelScopeMap = new HashMap<>();
  
  public static Map<Integer, SysMapInfo> id2GameMaps;
  
  public static Map<String, Integer> mapName2Id;
  
  public static Map<Integer, HashSet<Integer>> mapId2TrasferIdSet;
  
  public static Map<Integer, SysMapTeleporter> trasferId2Trasfer;
  
  public static Set<Integer> polarMaps;
  
  public static Map<Integer, Function<Role, Serializable>> MAP_KEY_GENERATE = new ConcurrentHashMap<>();
  
  public static Set<Integer> NOT_CHANGE_MAPS = new HashSet<>();
  
  static {
    polarMaps = new HashSet<>();
    polarMaps.add(Integer.valueOf(0));
    NOT_CHANGE_MAPS.add(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP));
    NOT_CHANGE_MAPS.add(Integer.valueOf(3003));
    NOT_CHANGE_MAPS.add(Integer.valueOf(3001));
    NOT_CHANGE_MAPS.add(Integer.valueOf(3005));
    MAP_KEY_GENERATE.put(Integer.valueOf(3005), role -> Integer.valueOf(role.getVisible()));
  }
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    mapInfoMap = initService.initMapinfo();
    initMapInfo();
    initTrasfer(initService.initMapteleporter());
    guaJiMap = initService.initGuajiMap();
  }
  
  public static void initMapInfo() {
    for (SysMapInfo value : mapInfoMap.values()) {
      String scope = value.getLevelScope();
      if (scope != null) {
        String[] split = scope.split("-");
        if (split.length >= 2) {
          List<Integer> list = new ArrayList<>();
          for (int i = Integer.valueOf(split[0]).intValue(); i <= Integer.valueOf(split[1]).intValue(); i++)
            list.add(Integer.valueOf(i)); 
          levelScopeMap.put(value.getMapSerial(), list);
        } 
      } 
    } 
  }
  
  public static void initTrasfer(List<SysMapTeleporter> list) {
    Map<Integer, HashSet<Integer>> mapId2Trasfer = new HashMap<>();
    Map<Integer, SysMapTeleporter> trasferId2TrasferMap = new HashMap<>(list.size());
    for (SysMapTeleporter transfer : list) {
      trasferId2TrasferMap.put(transfer.getId(), transfer);
      Set<Integer> idSet = mapId2Trasfer.computeIfAbsent(transfer.getFromMapSerial(), k -> new HashSet());
      idSet.add(transfer.getId());
    } 
    mapId2TrasferIdSet = mapId2Trasfer;
    trasferId2Trasfer = trasferId2TrasferMap;
  }
}
