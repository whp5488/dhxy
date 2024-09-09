package com.kitty.system.game.map.service;

import com.kitty.system.game.enter.message.RespElseAppear;
import com.kitty.system.game.enter.message.RespMapTransmission;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.enter.message.RespEnterMap;
import com.kitty.system.game.enter.message.RespMapTarget;
import java.text.MessageFormat;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.party.model.Party;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.mina.cache.SessionCache;
import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.system.game.enter.message.MapTransmission;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysHero;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.common.utils.DateUtils;
import java.util.Date;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.util.function.Function;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import com.kitty.system.game.map.pool.MapDataPool;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.enter.message.RespDisappear;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.Member;
import com.kitty.listener.event.MoveEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.bag.message.RespUseIncense;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.enter.message.RespClearAllRole;
import com.kitty.common.model.Pos;
import com.kitty.listener.event.EnterMapEvent;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MapService
{
    private Logger logger;
    @Autowired
    Dao dao;
    @Autowired
    RoleService roleService;
    @Autowired
    TeamService teamService;
    @Autowired
    RedisService redisService;
    @Autowired
    PartyService partyService;
    @Autowired
    BroadcastService broadcastService;
    private static final String NOT_CHANGE_MAP = "本场景禁止传送，请点击#<LG1>【{0}】#按钮离开。";
    
    public MapService() {
        this.logger = LoggerFactory.getLogger(MapService.class);
    }
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
        Role role = enterMapEvent.getRole();
        int mapId = (int)role.getPos().getMapId();
        SysMapInfo mapInfo = this.getMap(mapId);
        if (mapInfo == null) {
            role.setPos(new Pos());
            mapId = (int)role.getPos().getMapId();
        }
        RespClearAllRole respClearAllRole = new RespClearAllRole();
        respClearAllRole.setRoleId((int)role.getRoleId());
        respClearAllRole.setMapId(mapId);
        MessagePusher.pushMessage(role, respClearAllRole);
        this.cache(role, mapId);
        this.sendEnterMap(role);
        this.sendGameMapTransmission(role, mapId);
        try {
            this.broadcastNpc(role, mapId);
        }
        catch (Exception e) {
            this.logger.error("加载NPC错误：", e);
        }
        Integer incenseExpireTime = (Integer)role.getExpireTimeMap().get("incenseExpireTime");
        if ((int)incenseExpireTime > 0) {
            role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(0));
            MessagePusher.pushMessage(role, new RespUseIncense(Integer.valueOf(90005), Integer.valueOf(0)));
        }
        if (DataCache.ONLINE_ROLES.containsKey(role.getId())) {
            DataCache.ONLINE_ROLES.put(role.getId(), role);
        }
        role.save();
        this.broadcastService.sendAppear(role);
        SceneManager.INSTANCE.enterMap(mapId, role);
    }
    
    @EventHandler({ EventType.MOVE })
    public void handleLoginEvent(MoveEvent moveEvent) {
        Role role = moveEvent.getRole();
        SceneManager.INSTANCE.movePosition(role);
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null && role.getId().equals(Long.valueOf(team.getLeaderUid()))) {
            for (int i = 0; i < team.getList().size(); ++i) {
                Member member = (Member)team.getList().get(i);
                if (!member.getId().equals(role.getId()) && member.isInTeam()) {
                    Role player = this.roleService.getPlayer((long)member.getId());
                    if (player != null && player.getPos().getMapId().equals(role.getPos().getMapId())) {
                        player.setPos((int)role.getPos().getX(), (int)role.getPos().getY(), (int)role.getPos().getDir(), (int)role.getPos().getMapId());
                        SceneManager.INSTANCE.movePosition(player);
                    }
                }
            }
        }
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
        Role role = leaveMapEvent.getRole();
        int oldMapId = leaveMapEvent.getMapId();
        this.removeCache(role, oldMapId);
        SceneManager.INSTANCE.leaveMap(oldMapId, role);
        List<Npc> list = this.getNpcList(new GroupMapParam(role), oldMapId);
        if (list != null) {
            for (Npc npc : list) {
                RespDisappear respDisappear = new RespDisappear();
                respDisappear.setId(npc.getId());
                respDisappear.setType(LOGIC.NPC.getValue());
                MessagePusher.pushMessage(role, respDisappear);
            }
        }
    }
    
    public void initMapInfo(List<SysMapInfo> maps) {
        Map<Integer, SysMapInfo> id2GameMaps = new HashMap(maps.size());
        Map<String, Integer> mapName2Id = new HashMap(maps.size());
        for (SysMapInfo mapInfo : maps) {
            id2GameMaps.put(mapInfo.getMapSerial(), mapInfo);
            mapName2Id.put(mapInfo.getName(), mapInfo.getId());
        }
        MapDataPool.id2GameMaps = id2GameMaps;
        MapDataPool.mapName2Id = mapName2Id;
        SceneManager.INSTANCE.initiate(maps);
    }
    
    public int getMapIdByName(String mapName) {
        return (int)MapDataPool.mapName2Id.get(mapName);
    }
    
    public List<Npc> getNpcList(GroupMapParam groupMapParam, int mapId) {
        Set<Integer> set = (Set)DataCache.MAP_NPC_ID_S.get(Integer.valueOf(mapId));
        ArrayList<Npc> arrayList = new ArrayList<>();
        if (set != null) {
            for (Integer npcId : set) {
                SysNpcInfo npc = (SysNpcInfo)DataCache.ID_NPC.get(npcId);
                if (npc == null) {
                    continue;
                }
                arrayList.add(new Npc(npc));
            }
        }
        Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
        if (mapKeyGenerater != null) {
            Map<Serializable, Set<Integer>> map = (Map)DataCache.GROUP_NPC_ID_MAP.get(Integer.valueOf(mapId));
            if (map != null) {
                Serializable key = (groupMapParam.getRole() != null) ? ((Serializable)mapKeyGenerater.apply(groupMapParam.getRole())) : groupMapParam.getGroupKey();
                set = ((key != null) ? ((Set)map.get(key)) : null);
                if (set != null) {
                    for (Integer npcId2 : set) {
                        Npc npc2 = (Npc)DataCache.GROUP_NPC.get(npcId2);
                        if (npc2 == null) {
                            continue;
                        }
                        arrayList.add(npc2);
                    }
                }
            }
        }
        set = (Set)DataCache.BOOS_OFFICER_MAP_NPC.get(Integer.valueOf(mapId));
        if (set != null) {
            for (Integer npcId3 : set) {
                Npc npc3 = (Npc)DataCache.BOOS_OFFICER_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        set = (Set)DataCache.STAR_OFFICER_MAP_NPC.get(Integer.valueOf(mapId));
        if (set != null) {
            for (Integer npcId3 : set) {
                Npc npc3 = (Npc)DataCache.STAR_OFFICER_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        set = (Set)DataCache.SEAL_DEMON_MAP_NPC.get(Integer.valueOf(mapId));
        if (set != null) {
            for (Integer npcId3 : set) {
                Npc npc3 = (Npc)DataCache.SEAL_DEMON_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        Set<Long> longs = (Set)DataCache.MIRROR_MAP_NPC.get(Integer.valueOf(mapId));
        if (longs != null) {
            for (Long id : longs) {
                Npc npc4 = this.creationMirror(id);
                if (npc4 == null) {
                    continue;
                }
                arrayList.add(npc4);
            }
        }
        List<SysNpcInfo> collect = (List)DataCache.PLACE_NPC.values().stream().filter(e -> e.getPeriod() != null && e.getPos().getMapId().equals(Integer.valueOf(mapId))).collect(Collectors.toList());
        if (collect.size() > 0) {
            int day = (int)Integer.valueOf(DateUtils.dayForWeek(new Date()));
            for (SysNpcInfo npcInfo : collect) {
                if (npcInfo.getPeriod().contains(Integer.valueOf(day))) {
                    arrayList.add(new Npc(npcInfo));
                }
                else {
                    continue;
                }
            }
            return arrayList;
        }
        return arrayList;
    }
    
    public Npc creationMirror(Long id) {
        Role player = this.roleService.getPlayer((long)id);
        if (player != null) {
            Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(player.getId());
            if (mirror != null) {
                Pos pos = (Pos)JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
                SysHero sysHero = (SysHero)PartDataPool.heroMap.get(player.getSerial());
                Npc npc = new Npc((int)player.getRoleId(), 0, (int)pos.getMapId(), (int)pos.getX(), (int)pos.getY(), sysHero.getMirror(), player.getName(), mirror.getTitle(), 0, 0, 0, 0);
                npc.setMirror((byte)1);
                return npc;
            }
        }
        return null;
    }
    
    public Npc getNpc(int id) {
        SysNpcInfo npc = (SysNpcInfo)DataCache.ID_NPC.get(Integer.valueOf(id));
        if (npc != null) {
            return new Npc(npc);
        }
        npc = (SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(id));
        if (npc != null) {
            return new Npc(npc);
        }
        Npc npc_ = (Npc)DataCache.GROUP_NPC.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        npc_ = (Npc)DataCache.SCENE_TASK_NPC.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        npc_ = (Npc)DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        npc_ = (Npc)DataCache.STAR_OFFICER_SET.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        npc_ = (Npc)DataCache.SEAL_DEMON_SET.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        npc_ = (Npc)DataCache.THREE_SCOPE_SET.get(Integer.valueOf(id));
        if (npc_ != null) {
            return npc_;
        }
        return null;
    }
    
    public List<MapTransmission> getTrasferInfo(int mapId) {
        Set<Integer> set = (Set)MapDataPool.mapId2TrasferIdSet.get(Integer.valueOf(mapId));
        if (set == null || set.size() <= 0) {
            return new ArrayList<>();
        }
        List<MapTransmission> list = new ArrayList<>();
        for (Integer transferId : set) {
            SysMapTeleporter transfer = this.getTransferById((int)transferId);
            MapTransmission MapTransmission = new MapTransmission();
            MapTransmission.setName(transfer.getToMapName());
            MapTransmission.setX(transfer.getFromX());
            MapTransmission.setY(transfer.getFromY());
            MapTransmission.setDir(transfer.getDir());
            list.add(MapTransmission);
        }
        return list;
    }
    
    public SysMapTeleporter getTrasfer(String toMapName, int fromMapId) {
        Set<Integer> set = (Set)MapDataPool.mapId2TrasferIdSet.get(Integer.valueOf(fromMapId));
        if (set == null) {
            this.logger.error("根据目标地图名和当前取传送点数据错误，toMapName = {}，fromMapId = {}", toMapName, Integer.valueOf(fromMapId));
            return null;
        }
        for (int transferId : set) {
            SysMapTeleporter transfer = this.getTransferById(transferId);
            if (transfer.getToMapName().equals(toMapName)) {
                return transfer;
            }
            continue;
        }
        return null;
    }
    
    public SysMapInfo getMap(int mapId) {
        return (SysMapInfo)MapDataPool.id2GameMaps.get(Integer.valueOf(mapId));
    }
    
    public ConcurrentHashSet<Integer> getRoleIds(GroupMapParam groupMapParam, int mapId) {
        if (groupMapParam != null) {
            Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
            if (mapKeyGenerater != null) {
                Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId));
                if (map == null) {
                    return null;
                }
                Serializable key = (groupMapParam.getRole() != null) ? ((Serializable)mapKeyGenerater.apply(groupMapParam.getRole())) : groupMapParam.getGroupKey();
                if (key == null) {
                    return null;
                }
                return (ConcurrentHashSet)map.get(key);
            }
        }
        return (ConcurrentHashSet)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId));
    }
    
    public SysMapTeleporter getTransferById(int transferId) {
        return (SysMapTeleporter)MapDataPool.trasferId2Trasfer.get(Integer.valueOf(transferId));
    }
    
    public void cache(Role role, int mapId) {
        ConcurrentHashSet<Integer> set = null;
        Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
        if (mapKeyGenerater != null) {
            Serializable key = (Serializable)mapKeyGenerater.apply(role);
            if (key == null) {
                return;
            }
            Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId));
            if (map == null) {
                map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(Integer.valueOf(mapId), new ConcurrentHashMap());
                if (map == null) {
                    map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId));
                }
            }
            set = (ConcurrentHashSet)map.get(key);
            if (set == null) {
                set = (ConcurrentHashSet)map.putIfAbsent(key, new ConcurrentHashSet());
                if (set == null) {
                    set = (ConcurrentHashSet)map.get(key);
                }
            }
        }
        else {
            set = (ConcurrentHashSet)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId));
            if (set == null) {
                set = (ConcurrentHashSet)SessionCache.MAPID_ROLEIDS.put(Integer.valueOf(mapId), new ConcurrentHashSet());
                if (set == null) {
                    set = (ConcurrentHashSet)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId));
                }
            }
        }
        set.add(role.getRoleId());
    }
    
    private void removeCache(Role role, int oldMapId) {
        ConcurrentHashSet<Integer> set = null;
        Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(oldMapId));
        if (mapKeyGenerater != null) {
            Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(oldMapId));
            if (map != null) {
                Serializable key = (Serializable)mapKeyGenerater.apply(role);
                set = ((key != null) ? ((ConcurrentHashSet)map.get(key)) : null);
            }
        }
        else {
            set = (ConcurrentHashSet)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(oldMapId));
        }
        if (set != null) {
            set.remove(role.getRoleId());
        }
    }
    
    public void changeMap(Role role, int mapId, int x, int y, String param) {
        if (role == null) {
            return;
        }
        SysMapInfo mapInfo = this.getMap(mapId);
        if (mapInfo == null) {
            return;
        }
        if (!this.checkChangeMap(role, mapId, param)) {
            return;
        }
        if (mapId == 3002 || mapId == 3021 || mapId == 3022) {
            Party party = SpringUtils.getPartyService().getParty(role);
            if (party == null) {
                MessagePusher.pushMessage(role, new RespMsg("你还没有帮派哦#<E:51>#"));
                return;
            }
            int scene = PartyDataPool.getScene(party.getLevel());
            if (mapId != scene) {
                mapId = scene;
                switch (scene) {
                    case 3002: {
                        x = 50;
                        y = 25;
                        break;
                    }
                    case 3021: {
                        x = 120;
                        y = 71;
                        break;
                    }
                    case 3022: {
                        x = 120;
                        y = 71;
                        break;
                    }
                }
            }
        }
        int oldMapId = (int)role.getPos().getMapId();
        role.setPos(x, y, 5, mapId);
        this.teamService.teamTemporary(role, mapId);
        EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
        EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
        return;
    }
    
    public void changeMap(Role role, ReqTelePort reqTelePort, String param) {
        if (role != null) {
            this.changeMap(role, reqTelePort.getMapId(), reqTelePort.getX(), reqTelePort.getY(), param);
        }
    }
    
    private boolean checkChangeMap(Role role, int map, String param) {
        if (role.popTempCache("not_check_change_map") == null && MapDataPool.NOT_CHANGE_MAPS.contains(role.getPos().getMapId())) {
            SysMapInfo mapInfo = this.getMap((int)role.getPos().getMapId());
            String msg = MessageFormat.format("本场景禁止传送，请点击#<LG1>【{0}】#按钮离开。", mapInfo.getName());
            if (mapInfo.getHowToOut() != null) {
                msg = mapInfo.getHowToOut();
            }
            this.teamService.pushMessage(role, new RespMsg(msg));
            return false;
        }
        else {
            if (role.getPos().getMapId().equals(Integer.valueOf(1201))) {
                if (((PkService)SpringUtils.getBean(PkService.class)).isInPrisonTime(role)) {
                    MessagePusher.notify2Player(role, 1113, new Object[0]);
                    return false;
                }
                if (param == null || !"runs_prison".equals(param)) {
                    MessagePusher.notify2Player(role, 1101, new Object[0]);
                    return false;
                }
            }
            if (role.getPkInfoBox().getConceal() > 0 && map != 1201) {
                Team team = this.teamService.getTeam((int)role.getRoleId());
                if (team != null && !team.isLeader((int)role.getRoleId())) {
                    this.teamService.leaveTemp(role, true);
                }
                MessagePusher.notify2Player(role, 1108, new Object[0]);
                return false;
            }
            else {
                return true;
            }
        }
    }
    
    public void mapTarget(Role role, int map, int x, int y) {
        RespMapTarget mapTarget = new RespMapTarget();
        mapTarget.setMapId(map);
        mapTarget.setPosX(x);
        mapTarget.setPosY(y);
        MessagePusher.pushMessage(role, mapTarget);
    }
    
    public void sendEnterMap(Role role) {
        this.sendEnterMap(role, role.getPos(), false);
    }
    
    public void sendEnterMap(Role role, Pos pos, boolean isTaskWalk) {
        SysMapInfo mapInfo = this.getMap((int)pos.getMapId());
        RespEnterMap respEnterMap = new RespEnterMap();
        respEnterMap.setDir((int)pos.getDir());
        respEnterMap.setPosX((int)pos.getX());
        respEnterMap.setPosY((int)pos.getY());
        respEnterMap.setMapId((int)pos.getMapId());
        respEnterMap.setSafeZone((byte)mapInfo.getIsPkMap());
        if (isTaskWalk) {
            respEnterMap.setIsTaskWalk((byte)1);
        }
        MessagePusher.pushMessage(role, respEnterMap);
        if (mapInfo.getIsPkMap() == 1) {
            ((ChatService)SpringUtils.getBean(ChatService.class)).sendInfo(role.getId(), "大侠已进入非安全区域，可进行野外决斗，请务必小心！");
        }
    }
    
    private void sendGameMapTransmission(Role role, int mapId) {
        List<MapTransmission> list = this.getTrasferInfo(mapId);
        RespMapTransmission respMapTransmission = new RespMapTransmission();
        respMapTransmission.setList(list);
        MessagePusher.pushMessage(role, respMapTransmission);
    }
    
    public void broadcastNpc(Role role, int mapId) {
        List<Npc> list = this.getNpcList(new GroupMapParam(role), mapId);
        for (Npc npc : list) {
            RespElseAppear elseAppear = new RespElseAppear();
            elseAppear.setId(npc.getId());
            elseAppear.setX((short)npc.getX());
            elseAppear.setY((short)npc.getY());
            elseAppear.setS((short)npc.getIcon());
            elseAppear.setN(npc.getName());
            elseAppear.setT(npc.getTitle());
            elseAppear.setC(npc.getMirror());
            MessagePusher.pushMessage(role, elseAppear);
        }
    }
    
    public boolean isInPosition(Role role, int mapId, short x, short y, int perimeter) {
        if ((int)role.getPos().getMapId() != mapId) {
            return false;
        }
        int roleX = (int)role.getPos().getX();
        int roleY = (int)role.getPos().getY();
        return Math.abs(roleX - x) <= perimeter && Math.abs(roleY - y) <= perimeter;
    }
    
    public void addGroupMapNpc(GroupMapParam groupMapParam, Npc npc) {
        int mapId = npc.getMap();
        Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
        if (mapKeyGenerater != null) {
            Serializable key = (groupMapParam.getRole() != null) ? ((Serializable)mapKeyGenerater.apply(groupMapParam.getRole())) : groupMapParam.getGroupKey();
            Map<Serializable, Set<Integer>> map = (Map)DataCache.GROUP_NPC_ID_MAP.computeIfAbsent(Integer.valueOf(mapId), k -> new ConcurrentHashMap());
            Set<Integer> set = (Set)map.computeIfAbsent(key, k -> new ConcurrentHashSet());
            set.add(Integer.valueOf(npc.getId()));
            DataCache.GROUP_NPC.put(Integer.valueOf(npc.getId()), npc);
        }
    }
    
    public void removeGroupMapNpc(GroupMapParam groupMapParam, Npc npc) {
        int mapId = npc.getMap();
        Function<Role, Serializable> mapKeyGenerater = (Function)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
        if (mapKeyGenerater != null) {
            Serializable key = (groupMapParam.getRole() != null) ? ((Serializable)mapKeyGenerater.apply(groupMapParam.getRole())) : groupMapParam.getGroupKey();
            Map<Serializable, Set<Integer>> map = (Map)DataCache.GROUP_NPC_ID_MAP.get(Integer.valueOf(mapId));
            if (map != null && key != null) {
                Set<Integer> set = (Set)map.get(key);
                if (set != null) {
                    set.remove(Integer.valueOf(npc.getId()));
                }
            }
            DataCache.GROUP_NPC.remove(npc);
        }
    }
    
    public void leaveMap(Role role, int mapId) {
        int oldMapId = mapId;
        EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
    }
}
