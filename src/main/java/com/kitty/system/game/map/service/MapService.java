
package com.kitty.system.game.map.service;

import com.kitty.system.game.enter.message.RespElseAppear;
import com.kitty.system.game.enter.message.RespMapTransmission;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.enter.message.RespEnterMap;
import com.kitty.system.game.enter.message.RespMapTarget;
import com.kitty.system.game.pk.service.PkService;
import java.text.MessageFormat;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.party.model.Party;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.common.message.RespMsg;
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
import java.util.stream.Collector;
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
import com.kitty.mina.message.Message;
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
    public void handleEnterMapEvent(final EnterMapEvent enterMapEvent) {
        final Role role = enterMapEvent.getRole();
        int mapId = role.getPos().getMapId();
        final SysMapInfo mapInfo = this.getMap(mapId);
        if (mapInfo == null) {
            role.setPos(new Pos());
            mapId = role.getPos().getMapId();
        }
        final RespClearAllRole respClearAllRole = new RespClearAllRole();
        respClearAllRole.setRoleId(role.getRoleId());
        respClearAllRole.setMapId(mapId);
        MessagePusher.pushMessage(role, respClearAllRole);
        this.cache(role, mapId);
        this.sendEnterMap(role);
        this.sendGameMapTransmission(role, mapId);
        try {
            this.broadcastNpc(role, mapId);
        }
        catch (final Exception e) {
            this.logger.error("加载NPC错误：", e);
        }
        final Integer incenseExpireTime = role.getExpireTimeMap().get("incenseExpireTime");
        if (incenseExpireTime > 0) {
            role.getExpireTimeMap().put("incenseExpireTime", 0);
            MessagePusher.pushMessage(role, new RespUseIncense(90005, 0));
        }
        if (DataCache.ONLINE_ROLES.containsKey(role.getId())) {
            DataCache.ONLINE_ROLES.put(role.getId(), role);
        }
        role.save();
        this.broadcastService.sendAppear(role);
        SceneManager.INSTANCE.enterMap(mapId, role);
    }
    
    @EventHandler({ EventType.MOVE })
    public void handleLoginEvent(final MoveEvent moveEvent) {
        final Role role = moveEvent.getRole();
        SceneManager.INSTANCE.movePosition(role);
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && role.getId().equals(team.getLeaderUid())) {
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (!member.getId().equals(role.getId())) {
                    if (member.isInTeam()) {
                        final Role player = this.roleService.getPlayer(member.getId());
                        if (player != null && player.getPos().getMapId().equals(role.getPos().getMapId())) {
                            player.setPos(role.getPos().getX(), role.getPos().getY(), role.getPos().getDir(), role.getPos().getMapId());
                            SceneManager.INSTANCE.movePosition(player);
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(final LeaveMapEvent leaveMapEvent) {
        final Role role = leaveMapEvent.getRole();
        final int oldMapId = leaveMapEvent.getMapId();
        this.removeCache(role, oldMapId);
        SceneManager.INSTANCE.leaveMap(oldMapId, role);
        final List<Npc> list = this.getNpcList(new GroupMapParam(role), oldMapId);
        if (list != null) {
            for (final Npc npc : list) {
                final RespDisappear respDisappear = new RespDisappear();
                respDisappear.setId(npc.getId());
                respDisappear.setType(LOGIC.NPC.getValue());
                MessagePusher.pushMessage(role, respDisappear);
            }
        }
    }
    
    public void initMapInfo(final List<SysMapInfo> maps) {
        final Map<Integer, SysMapInfo> id2GameMaps = new HashMap<>(maps.size());
        final Map<String, Integer> mapName2Id = new HashMap<>(maps.size());
        for (final SysMapInfo mapInfo : maps) {
            id2GameMaps.put(mapInfo.getMapSerial(), mapInfo);
            mapName2Id.put(mapInfo.getName(), mapInfo.getId());
        }
        MapDataPool.id2GameMaps = id2GameMaps;
        MapDataPool.mapName2Id = mapName2Id;
        SceneManager.INSTANCE.initiate(maps);
    }
    
    public int getMapIdByName(final String mapName) {
        return MapDataPool.mapName2Id.get(mapName);
    }
    
    public List<Npc> getNpcList(final GroupMapParam groupMapParam, final int mapId) {
        Set<Integer> set = DataCache.MAP_NPC_ID_S.get(mapId);
        final ArrayList<Npc> arrayList = new ArrayList<Npc>();
        if (set != null) {
            for (final Integer npcId : set) {
                final SysNpcInfo npc = DataCache.ID_NPC.get(npcId);
                if (npc == null) {
                    continue;
                }
                arrayList.add(new Npc(npc));
            }
        }
        final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(mapId);
        if (mapKeyGenerater != null) {
            final Map<Serializable, Set<Integer>> map = DataCache.GROUP_NPC_ID_MAP.get(mapId);
            if (map != null) {
                final Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
                set = ((key != null) ? map.get(key) : null);
                if (set != null) {
                    for (final Integer npcId2 : set) {
                        final Npc npc2 = DataCache.GROUP_NPC.get(npcId2);
                        if (npc2 == null) {
                            continue;
                        }
                        arrayList.add(npc2);
                    }
                }
            }
        }
        set = DataCache.BOOS_OFFICER_MAP_NPC.get(mapId);
        if (set != null) {
            for (final Integer npcId3 : set) {
                final Npc npc3 = DataCache.BOOS_OFFICER_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        set = DataCache.STAR_OFFICER_MAP_NPC.get(mapId);
        if (set != null) {
            for (final Integer npcId3 : set) {
                final Npc npc3 = DataCache.STAR_OFFICER_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        set = DataCache.SEAL_DEMON_MAP_NPC.get(mapId);
        if (set != null) {
            for (final Integer npcId3 : set) {
                final Npc npc3 = DataCache.SEAL_DEMON_SET.get(npcId3);
                if (npc3 == null) {
                    continue;
                }
                arrayList.add(npc3);
            }
        }
        final Set<Long> longs = DataCache.MIRROR_MAP_NPC.get(mapId);
        if (longs != null) {
            for (final Long id : longs) {
                final Npc npc4 = this.creationMirror(id);
                if (npc4 == null) {
                    continue;
                }
                arrayList.add(npc4);
            }
        }
        final List<SysNpcInfo> collect = DataCache.PLACE_NPC.values().stream().filter(e -> e.getPeriod() != null && e.getPos().getMapId().equals(mapId)).collect(Collectors.toList());
        if (collect.size() > 0) {
            final int day = Integer.valueOf(DateUtils.dayForWeek(new Date()));
            for (final SysNpcInfo npcInfo : collect) {
                if (npcInfo.getPeriod().contains(day)) {
                    arrayList.add(new Npc(npcInfo));
                }
            }
        }
        return arrayList;
    }
    
    public Npc creationMirror(final Long id) {
        final Role player = this.roleService.getPlayer(id);
        if (player != null) {
            final Mirror mirror = DataCache.MIRROR_MAP.get(player.getId());
            if (mirror != null) {
                final Pos pos = JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
                final SysHero sysHero = PartDataPool.heroMap.get(player.getSerial());
                final Npc npc = new Npc(player.getRoleId(), 0, pos.getMapId(), pos.getX(), pos.getY(), sysHero.getMirror(), player.getName(), mirror.getTitle(), 0, 0, 0, 0);
                npc.setMirror((byte)1);
                return npc;
            }
        }
        return null;
    }
    
    public Npc getNpc(final int id) {
        SysNpcInfo npc = DataCache.ID_NPC.get(id);
        if (npc != null) {
            return new Npc(npc);
        }
        npc = DataCache.PLACE_NPC.get(id);
        if (npc != null) {
            return new Npc(npc);
        }
        Npc npc_ = DataCache.GROUP_NPC.get(id);
        if (npc_ != null) {
            return npc_;
        }
        npc_ = DataCache.SCENE_TASK_NPC.get(id);
        if (npc_ != null) {
            return npc_;
        }
        npc_ = DataCache.BOOS_OFFICER_SET.get(id);
        if (npc_ != null) {
            return npc_;
        }
        npc_ = DataCache.STAR_OFFICER_SET.get(id);
        if (npc_ != null) {
            return npc_;
        }
        npc_ = DataCache.SEAL_DEMON_SET.get(id);
        if (npc_ != null) {
            return npc_;
        }
        npc_ = DataCache.THREE_SCOPE_SET.get(id);
        if (npc_ != null) {
            return npc_;
        }
        return null;
    }
    
    public List<MapTransmission> getTrasferInfo(final int mapId) {
        final Set<Integer> set = MapDataPool.mapId2TrasferIdSet.get(mapId);
        if (set == null || set.size() <= 0) {
            return new ArrayList<MapTransmission>();
        }
        final List<MapTransmission> list = new ArrayList<MapTransmission>();
        for (final Integer transferId : set) {
            final SysMapTeleporter transfer = this.getTransferById(transferId);
            final MapTransmission MapTransmission = new MapTransmission();
            MapTransmission.setName(transfer.getToMapName());
            MapTransmission.setX(transfer.getFromX());
            MapTransmission.setY(transfer.getFromY());
            MapTransmission.setDir(transfer.getDir());
            list.add(MapTransmission);
        }
        return list;
    }
    
    public SysMapTeleporter getTrasfer(final String toMapName, final int fromMapId) {
        final Set<Integer> set = MapDataPool.mapId2TrasferIdSet.get(fromMapId);
        if (set == null) {
            this.logger.error("根据目标地图名和当前取传送点数据错误，toMapName = {}，fromMapId = {}", (Object)toMapName, (Object)fromMapId);
            return null;
        }
        for (final int transferId : set) {
            final SysMapTeleporter transfer = this.getTransferById(transferId);
            if (transfer.getToMapName().equals(toMapName)) {
                return transfer;
            }
        }
        return null;
    }
    
    public SysMapInfo getMap(final int mapId) {
        return MapDataPool.id2GameMaps.get(mapId);
    }
    
    public ConcurrentHashSet<Integer> getRoleIds(final GroupMapParam groupMapParam, final int mapId) {
        if (groupMapParam != null) {
            final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(mapId);
            if (mapKeyGenerater != null) {
                final Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                if (map == null) {
                    return null;
                }
                final Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
                if (key == null) {
                    return null;
                }
                return map.get(key);
            }
        }
        return SessionCache.MAPID_ROLEIDS.get(mapId);
    }
    
    public SysMapTeleporter getTransferById(final int transferId) {
        return MapDataPool.trasferId2Trasfer.get(transferId);
    }
    
    public void cache(final Role role, final int mapId) {
        ConcurrentHashSet<Integer> set = null;
        final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(mapId);
        if (mapKeyGenerater != null) {
            final Serializable key = mapKeyGenerater.apply(role);
            if (key == null) {
                return;
            }
            Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
            if (map == null) {
                map = SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(mapId, new ConcurrentHashMap<Serializable, ConcurrentHashSet<Integer>>());
                if (map == null) {
                    map = SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                }
            }
            set = map.get(key);
            if (set == null) {
                set = map.putIfAbsent(key, new ConcurrentHashSet<Integer>());
                if (set == null) {
                    set = map.get(key);
                }
            }
        }
        else {
            set = SessionCache.MAPID_ROLEIDS.get(mapId);
            if (set == null) {
                set = SessionCache.MAPID_ROLEIDS.put(mapId, new ConcurrentHashSet<Integer>());
                if (set == null) {
                    set = SessionCache.MAPID_ROLEIDS.get(mapId);
                }
            }
        }
        set.add(role.getRoleId());
    }
    
    private void removeCache(final Role role, final int oldMapId) {
        ConcurrentHashSet<Integer> set = null;
        final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(oldMapId);
        if (mapKeyGenerater != null) {
            final Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(oldMapId);
            if (map != null) {
                final Serializable key = mapKeyGenerater.apply(role);
                set = ((key != null) ? map.get(key) : null);
            }
        }
        else {
            set = SessionCache.MAPID_ROLEIDS.get(oldMapId);
        }
        if (set != null) {
            set.remove(role.getRoleId());
        }
    }
    
    public void changeMap(final Role role, int mapId, int x, int y, final String param) {
        if (role == null) {
            return;
        }
        final SysMapInfo mapInfo = this.getMap(mapId);
        if (mapInfo == null) {
            return;
        }
        if (!this.checkChangeMap(role, mapId, param)) {
            return;
        }
        if (mapId == 3002 || mapId == 3021 || mapId == 3022) {
            final Party party = SpringUtils.getPartyService().getParty(role);
            if (party == null) {
                MessagePusher.pushMessage(role, new RespMsg("你还没有帮派哦#<E:51>#"));
                return;
            }
            final int scene = PartyDataPool.getScene(party.getLevel());
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
        final int oldMapId = role.getPos().getMapId();
        role.setPos(x, y, 5, mapId);
        this.teamService.teamTemporary(role, mapId);
        EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
        EventDispatcher.getInstance().fireEvent(new EnterMapEvent(EventType.ENTER_MAP, role));
    }
    
    public void changeMap(final Role role, final ReqTelePort reqTelePort, final String param) {
        if (role != null) {
            this.changeMap(role, reqTelePort.getMapId(), reqTelePort.getX(), reqTelePort.getY(), param);
        }
    }
    
    private boolean checkChangeMap(final Role role, final int map, final String param) {
        if (role.popTempCache("not_check_change_map") == null && MapDataPool.NOT_CHANGE_MAPS.contains(role.getPos().getMapId())) {
            final SysMapInfo mapInfo = this.getMap(role.getPos().getMapId());
            String msg = MessageFormat.format("本场景禁止传送，请点击#<LG1>【{0}】#按钮离开。", mapInfo.getName());
            if (mapInfo.getHowToOut() != null) {
                msg = mapInfo.getHowToOut();
            }
            this.teamService.pushMessage(role, new RespMsg(msg));
            return false;
        }
        if (role.getPos().getMapId().equals(1201)) {
            if (SpringUtils.getBean(PkService.class).isInPrisonTime(role)) {
                MessagePusher.notify2Player(role, 1113);
                return false;
            }
            if (param == null || !"runs_prison".equals(param)) {
                MessagePusher.notify2Player(role, 1101);
                return false;
            }
        }
        if (role.getPkInfoBox().getConceal() > 0 && map != 1201) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null && !team.isLeader(role.getRoleId())) {
                this.teamService.leaveTemp(role, true);
            }
            MessagePusher.notify2Player(role, 1108);
            return false;
        }
        return true;
    }
    
    public void mapTarget(final Role role, final int map, final int x, final int y) {
        final RespMapTarget mapTarget = new RespMapTarget();
        mapTarget.setMapId(map);
        mapTarget.setPosX(x);
        mapTarget.setPosY(y);
        MessagePusher.pushMessage(role, mapTarget);
    }
    
    public void sendEnterMap(final Role role) {
        this.sendEnterMap(role, role.getPos(), false);
    }
    
    public void sendEnterMap(final Role role, final Pos pos, final boolean isTaskWalk) {
        final SysMapInfo mapInfo = this.getMap(pos.getMapId());
        final RespEnterMap respEnterMap = new RespEnterMap();
        respEnterMap.setDir(pos.getDir());
        respEnterMap.setPosX(pos.getX());
        respEnterMap.setPosY(pos.getY());
        respEnterMap.setMapId(pos.getMapId());
        respEnterMap.setSafeZone((byte)mapInfo.getIsPkMap());
        if (isTaskWalk) {
            respEnterMap.setIsTaskWalk((byte)1);
        }
        MessagePusher.pushMessage(role, respEnterMap);
        if (mapInfo.getIsPkMap() == 1) {
            SpringUtils.getBean(ChatService.class).sendInfo(role.getId(), "大侠已进入非安全区域，可进行野外决斗，请务必小心！");
        }
    }
    
    private void sendGameMapTransmission(final Role role, final int mapId) {
        final List<MapTransmission> list = this.getTrasferInfo(mapId);
        final RespMapTransmission respMapTransmission = new RespMapTransmission();
        respMapTransmission.setList(list);
        MessagePusher.pushMessage(role, respMapTransmission);
    }
    
    public void broadcastNpc(final Role role, final int mapId) {
        final List<Npc> list = this.getNpcList(new GroupMapParam(role), mapId);
        for (final Npc npc : list) {
            final RespElseAppear elseAppear = new RespElseAppear();
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
    
    public boolean isInPosition(final Role role, final int mapId, final short x, final short y, final int perimeter) {
        if (role.getPos().getMapId() != mapId) {
            return false;
        }
        final int roleX = role.getPos().getX();
        final int roleY = role.getPos().getY();
        return Math.abs(roleX - x) <= perimeter && Math.abs(roleY - y) <= perimeter;
    }
    
    public void addGroupMapNpc(final GroupMapParam groupMapParam, final Npc npc) {
        final int mapId = npc.getMap();
        final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(mapId);
        if (mapKeyGenerater != null) {
            final Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
            final Map<Serializable, Set<Integer>> map = DataCache.GROUP_NPC_ID_MAP.computeIfAbsent(Integer.valueOf(mapId), k -> new ConcurrentHashMap());
            final Set<Integer> set = map.computeIfAbsent(key, k -> new ConcurrentHashSet());
            set.add(npc.getId());
            DataCache.GROUP_NPC.put(npc.getId(), npc);
        }
    }
    
    public void removeGroupMapNpc(final GroupMapParam groupMapParam, final Npc npc) {
        final int mapId = npc.getMap();
        final Function<Role, Serializable> mapKeyGenerater = MapDataPool.MAP_KEY_GENERATE.get(mapId);
        if (mapKeyGenerater != null) {
            final Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
            final Map<Serializable, Set<Integer>> map = DataCache.GROUP_NPC_ID_MAP.get(mapId);
            if (map != null && key != null) {
                final Set<Integer> set = map.get(key);
                if (set != null) {
                    set.remove(npc.getId());
                }
            }
            DataCache.GROUP_NPC.remove(npc);
        }
    }
    
    public void leaveMap(final Role role, final int mapId) {
        final int oldMapId = mapId;
        EventDispatcher.getInstance().fireEvent(new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
    }
}
