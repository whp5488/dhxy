package com.kitty.system.game.map.pool;

import com.kitty.system.game.task.service.handler.dungeon.DungeonCmHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import java.io.Serializable;
import com.kitty.system.game.role.domain.Role;
import java.util.function.Function;
import java.util.Set;
import com.kitty.system.init.domain.SysMapTeleporter;
import java.util.HashSet;
import java.util.List;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysGuajiMap;
import java.util.Map;

public class MapDataPool
{
    public static Map<Integer, SysGuajiMap> guaJiMap;
    public static Map<Integer, SysMapInfo> mapInfoMap;
    public static Map<Integer, List<Integer>> levelScopeMap;
    public static Map<Integer, SysMapInfo> id2GameMaps;
    public static Map<String, Integer> mapName2Id;
    public static Map<Integer, HashSet<Integer>> mapId2TrasferIdSet;
    public static Map<Integer, SysMapTeleporter> trasferId2Trasfer;
    public static Set<Integer> polarMaps;
    public static Map<Integer, Function<Role, Serializable>> MAP_KEY_GENERATE;
    public static Set<Integer> NOT_CHANGE_MAPS;
    
    public MapDataPool() {
    }
    
    public static void load() {
        InitService initService = SpringUtils.getInitService();
        MapDataPool.mapInfoMap = initService.initMapinfo();
        initMapInfo();
        initTrasfer(initService.initMapteleporter());
        MapDataPool.guaJiMap = initService.initGuajiMap();
    }
    
    public static void initMapInfo() {
        for (SysMapInfo value : MapDataPool.mapInfoMap.values()) {
            String scope = value.getLevelScope();
            if (scope != null) {
                String[] split = scope.split("-");
                if (split.length < 2) {
                    continue;
                }
                List<Integer> list = new ArrayList<>();
                for (int i = Integer.valueOf(split[0]); i <= Integer.valueOf(split[1]); ++i) {
                    list.add(Integer.valueOf(i));
                }
                MapDataPool.levelScopeMap.put(value.getMapSerial(), list);
            }
        }
    }
    
    public static void initTrasfer(List<SysMapTeleporter> list) {
        Map<Integer, HashSet<Integer>> mapId2Trasfer = new HashMap();
        Map<Integer, SysMapTeleporter> trasferId2TrasferMap = new HashMap(list.size());
        for (SysMapTeleporter transfer : list) {
            trasferId2TrasferMap.put(transfer.getId(), transfer);
            Set<Integer> idSet = (Set)mapId2Trasfer.computeIfAbsent(transfer.getFromMapSerial(), k -> new HashSet());
            idSet.add(transfer.getId());
        }
        MapDataPool.mapId2TrasferIdSet = mapId2Trasfer;
        MapDataPool.trasferId2Trasfer = trasferId2TrasferMap;
    }
    
    static {
        MapDataPool.guaJiMap = new HashMap();
        MapDataPool.mapInfoMap = new HashMap();
        MapDataPool.levelScopeMap = new HashMap();
        MapDataPool.MAP_KEY_GENERATE = new ConcurrentHashMap();
        MapDataPool.NOT_CHANGE_MAPS = new HashSet();
        (MapDataPool.polarMaps = new HashSet()).add(Integer.valueOf(0));
        MapDataPool.NOT_CHANGE_MAPS.add(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP));
        MapDataPool.NOT_CHANGE_MAPS.add(Integer.valueOf(DungeonCmHandler.DUNGEON_ENTER_MAP));
        MapDataPool.NOT_CHANGE_MAPS.add(Integer.valueOf(3003));
        MapDataPool.NOT_CHANGE_MAPS.add(Integer.valueOf(3001));
        MapDataPool.NOT_CHANGE_MAPS.add(Integer.valueOf(3005));
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(3005), Role::getVisible);
    }
}
