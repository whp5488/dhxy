
package com.kitty.system.game.map.pool;

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
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        MapDataPool.mapInfoMap = initService.initMapinfo();
        initMapInfo();
        initTrasfer(initService.initMapteleporter());
        MapDataPool.guaJiMap = initService.initGuajiMap();
    }
    
    public static void initMapInfo() {
        for (final SysMapInfo value : MapDataPool.mapInfoMap.values()) {
            final String scope = value.getLevelScope();
            if (scope != null) {
                final String[] split = scope.split("-");
                if (split.length < 2) {
                    continue;
                }
                final List<Integer> list = new ArrayList<Integer>();
                for (int i = Integer.valueOf(split[0]); i <= Integer.valueOf(split[1]); ++i) {
                    list.add(i);
                }
                MapDataPool.levelScopeMap.put(value.getMapSerial(), list);
            }
        }
    }
    
    public static void initTrasfer(final List<SysMapTeleporter> list) {
        final Map<Integer, HashSet<Integer>> mapId2Trasfer = new HashMap<Integer, HashSet<Integer>>();
        final Map<Integer, SysMapTeleporter> trasferId2TrasferMap = new HashMap<Integer, SysMapTeleporter>(list.size());
        for (final SysMapTeleporter transfer : list) {
            trasferId2TrasferMap.put(transfer.getId(), transfer);
            final Set<Integer> idSet = mapId2Trasfer.computeIfAbsent(transfer.getFromMapSerial(), k -> new HashSet());
            idSet.add(transfer.getId());
        }
        MapDataPool.mapId2TrasferIdSet = mapId2Trasfer;
        MapDataPool.trasferId2Trasfer = trasferId2TrasferMap;
    }
    
    static {
        MapDataPool.guaJiMap = new HashMap<Integer, SysGuajiMap>();
        MapDataPool.mapInfoMap = new HashMap<Integer, SysMapInfo>();
        MapDataPool.levelScopeMap = new HashMap<Integer, List<Integer>>();
        MapDataPool.MAP_KEY_GENERATE = new ConcurrentHashMap<Integer, Function<Role, Serializable>>();
        MapDataPool.NOT_CHANGE_MAPS = new HashSet<Integer>();
        (MapDataPool.polarMaps = new HashSet<Integer>()).add(0);
        MapDataPool.NOT_CHANGE_MAPS.add(DungeonMwkFront.DUNGEON_ENTER_MAP);
        MapDataPool.NOT_CHANGE_MAPS.add(3003);
        MapDataPool.NOT_CHANGE_MAPS.add(3001);
        MapDataPool.NOT_CHANGE_MAPS.add(3005);
        MapDataPool.MAP_KEY_GENERATE.put(3005, role -> role.getVisible());
    }
}
