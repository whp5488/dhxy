package com.kitty.system.game.map.service;

import com.kitty.cache.RedisService;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.common.utils.DateUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.event.MoveEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.bag.message.RespUseIncense;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.enter.message.MapTransmission;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.enter.message.RespClearAllRole;
import com.kitty.system.game.enter.message.RespDisappear;
import com.kitty.system.game.enter.message.RespElseAppear;
import com.kitty.system.game.enter.message.RespEnterMap;
import com.kitty.system.game.enter.message.RespMapTarget;
import com.kitty.system.game.enter.message.RespMapTransmission;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.pk.service.PkService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.system.init.pool.PartDataPool;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapService {
  private Logger logger = LoggerFactory.getLogger(MapService.class);
  
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
  
  @EventHandler({EventType.ENTER_MAP})
  public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
    Role role = enterMapEvent.getRole();
    int mapId = role.getPos().getMapId().intValue();
    SysMapInfo mapInfo = getMap(mapId);
    if (mapInfo == null) {
      role.setPos(new Pos());
      mapId = role.getPos().getMapId().intValue();
    } 
    RespClearAllRole respClearAllRole = new RespClearAllRole();
    respClearAllRole.setRoleId(role.getRoleId().intValue());
    respClearAllRole.setMapId(mapId);
    MessagePusher.pushMessage(role, (Message)respClearAllRole);
    cache(role, mapId);
    sendEnterMap(role);
    sendGameMapTransmission(role, mapId);
    try {
      broadcastNpc(role, mapId);
    } catch (Exception e) {
      this.logger.error("加载NPC错误：", e);
    } 
    Integer incenseExpireTime = (Integer)role.getExpireTimeMap().get("incenseExpireTime");
    if (incenseExpireTime.intValue() > 0) {
      role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(0));
      MessagePusher.pushMessage(role, (Message)new RespUseIncense(Integer.valueOf(90005), Integer.valueOf(0)));
    } 
    if (DataCache.ONLINE_ROLES.containsKey(role.getId()))
      DataCache.ONLINE_ROLES.put(role.getId(), role); 
    role.save();
    this.broadcastService.sendAppear(role);
    SceneManager.INSTANCE.enterMap(mapId, role);
  }
  
  @EventHandler({EventType.MOVE})
  public void handleLoginEvent(MoveEvent moveEvent) {
    Role role = moveEvent.getRole();
    SceneManager.INSTANCE.movePosition(role);
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null && role.getId().equals(Long.valueOf(team.getLeaderUid())))
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (!member.getId().equals(role.getId()))
          if (member.isInTeam()) {
            Role player = this.roleService.getPlayer(member.getId().longValue());
            if (player != null && player.getPos().getMapId().equals(role.getPos().getMapId())) {
              player.setPos(role.getPos().getX().intValue(), role.getPos().getY().intValue(), role.getPos().getDir().intValue(), role.getPos().getMapId().intValue());
              SceneManager.INSTANCE.movePosition(player);
            } 
          }  
      }  
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    int oldMapId = leaveMapEvent.getMapId();
    removeCache(role, oldMapId);
    SceneManager.INSTANCE.leaveMap(oldMapId, role);
    List<Npc> list = getNpcList(new GroupMapParam(role), oldMapId);
    if (list != null)
      for (Npc npc : list) {
        RespDisappear respDisappear = new RespDisappear();
        respDisappear.setId(npc.getId());
        respDisappear.setType(LOGIC.NPC.getValue());
        MessagePusher.pushMessage(role, (Message)respDisappear);
      }  
  }
  
  public void initMapInfo(List<SysMapInfo> maps) {
    Map<Integer, SysMapInfo> id2GameMaps = new HashMap<>(maps.size());
    Map<String, Integer> mapName2Id = new HashMap<>(maps.size());
    for (SysMapInfo mapInfo : maps) {
      id2GameMaps.put(mapInfo.getMapSerial(), mapInfo);
      mapName2Id.put(mapInfo.getName(), mapInfo.getId());
    } 
    MapDataPool.id2GameMaps = id2GameMaps;
    MapDataPool.mapName2Id = mapName2Id;
    SceneManager.INSTANCE.initiate(maps);
  }
  
  public int getMapIdByName(String mapName) {
    return ((Integer)MapDataPool.mapName2Id.get(mapName)).intValue();
  }
  
  public List<Npc> getNpcList(GroupMapParam groupMapParam, int mapId) {
    Set<Integer> set = (Set<Integer>)DataCache.MAP_NPC_ID_S.get(Integer.valueOf(mapId));
    ArrayList<Npc> arrayList = new ArrayList<>();
    if (set != null)
      for (Integer npcId : set) {
        SysNpcInfo npc = (SysNpcInfo)DataCache.ID_NPC.get(npcId);
        if (npc == null)
          continue; 
        arrayList.add(new Npc(npc));
      }  
    Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
    if (mapKeyGenerater != null) {
      Map<Serializable, Set<Integer>> map = (Map<Serializable, Set<Integer>>)DataCache.GROUP_NPC_ID_MAP.get(Integer.valueOf(mapId));
      if (map != null) {
        Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
        set = (key != null) ? map.get(key) : null;
        if (set != null)
          for (Integer npcId : set) {
            Npc npc = (Npc)DataCache.GROUP_NPC.get(npcId);
            if (npc == null)
              continue; 
            arrayList.add(npc);
          }  
      } 
    } 
    set = (Set<Integer>)DataCache.BOOS_OFFICER_MAP_NPC.get(Integer.valueOf(mapId));
    if (set != null)
      for (Integer npcId : set) {
        Npc npc = (Npc)DataCache.BOOS_OFFICER_SET.get(npcId);
        if (npc == null)
          continue; 
        arrayList.add(npc);
      }  
    set = (Set<Integer>)DataCache.STAR_OFFICER_MAP_NPC.get(Integer.valueOf(mapId));
    if (set != null)
      for (Integer npcId : set) {
        Npc npc = (Npc)DataCache.STAR_OFFICER_SET.get(npcId);
        if (npc == null)
          continue; 
        arrayList.add(npc);
      }  
    set = (Set<Integer>)DataCache.SEAL_DEMON_MAP_NPC.get(Integer.valueOf(mapId));
    if (set != null)
      for (Integer npcId : set) {
        Npc npc = (Npc)DataCache.SEAL_DEMON_SET.get(npcId);
        if (npc == null)
          continue; 
        arrayList.add(npc);
      }  
    Set<Long> longs = (Set<Long>)DataCache.MIRROR_MAP_NPC.get(Integer.valueOf(mapId));
    if (longs != null)
      for (Long id : longs) {
        Npc npc = creationMirror(id);
        if (npc == null)
          continue; 
        arrayList.add(npc);
      }  
    List<SysNpcInfo> collect = (List<SysNpcInfo>)DataCache.PLACE_NPC.values().stream().filter(e -> (e.getPeriod() != null && e.getPos().getMapId().equals(Integer.valueOf(mapId)))).collect(Collectors.toList());
    if (collect.size() > 0) {
      int day = Integer.valueOf(DateUtils.dayForWeek(new Date())).intValue();
      for (SysNpcInfo npcInfo : collect) {
        if (npcInfo.getPeriod().contains(Integer.valueOf(day)))
          arrayList.add(new Npc(npcInfo)); 
      } 
    } 
    return arrayList;
  }
  
  public Npc creationMirror(Long id) {
    Role player = this.roleService.getPlayer(id.longValue());
    if (player != null) {
      Mirror mirror = (Mirror)DataCache.MIRROR_MAP.get(player.getId());
      if (mirror != null) {
        Pos pos = (Pos)JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
        SysHero sysHero = (SysHero)PartDataPool.heroMap.get(player.getSerial());
        Npc npc = new Npc(player.getRoleId().intValue(), 0, pos.getMapId().intValue(), pos.getX().intValue(), pos.getY().intValue(), sysHero.getMirror(), player.getName(), mirror.getTitle(), 0, 0, 0, 0);
        npc.setMirror((byte)1);
        return npc;
      } 
    } 
    return null;
  }
  
  public Npc getNpc(int id) {
    SysNpcInfo npc = (SysNpcInfo)DataCache.ID_NPC.get(Integer.valueOf(id));
    if (npc != null)
      return new Npc(npc); 
    npc = (SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(id));
    if (npc != null)
      return new Npc(npc); 
    Npc npc_ = (Npc)DataCache.GROUP_NPC.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    npc_ = (Npc)DataCache.SCENE_TASK_NPC.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    npc_ = (Npc)DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    npc_ = (Npc)DataCache.STAR_OFFICER_SET.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    npc_ = (Npc)DataCache.SEAL_DEMON_SET.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    npc_ = (Npc)DataCache.THREE_SCOPE_SET.get(Integer.valueOf(id));
    if (npc_ != null)
      return npc_; 
    return null;
  }
  
  public List<MapTransmission> getTrasferInfo(int mapId) {
    Set<Integer> set = (Set<Integer>)MapDataPool.mapId2TrasferIdSet.get(Integer.valueOf(mapId));
    if (set == null || set.size() <= 0)
      return new ArrayList<>(); 
    List<MapTransmission> list = new ArrayList<>();
    for (Integer transferId : set) {
      SysMapTeleporter transfer = getTransferById(transferId.intValue());
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
    Set<Integer> set = (Set<Integer>)MapDataPool.mapId2TrasferIdSet.get(Integer.valueOf(fromMapId));
    if (set == null) {
      this.logger.error("根据目标地图名和当前取传送点数据错误，toMapName = {}，fromMapId = {}", toMapName, Integer.valueOf(fromMapId));
      return null;
    } 
    for (Iterator<Integer> iterator = set.iterator(); iterator.hasNext(); ) {
      int transferId = ((Integer)iterator.next()).intValue();
      SysMapTeleporter transfer = getTransferById(transferId);
      if (transfer.getToMapName().equals(toMapName))
        return transfer; 
    } 
    return null;
  }
  
  public SysMapInfo getMap(int mapId) {
    return (SysMapInfo)MapDataPool.id2GameMaps.get(Integer.valueOf(mapId));
  }
  
  public ConcurrentHashSet<Integer> getRoleIds(GroupMapParam groupMapParam, int mapId) {
    if (groupMapParam != null) {
      Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
      if (mapKeyGenerater != null) {
        Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId));
        if (map == null)
          return null; 
        Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
        if (key == null)
          return null; 
        return map.get(key);
      } 
    } 
    return (ConcurrentHashSet<Integer>)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId));
  }
  
  public SysMapTeleporter getTransferById(int transferId) {
    return (SysMapTeleporter)MapDataPool.trasferId2Trasfer.get(Integer.valueOf(transferId));
  }
  
  public void cache(Role role, int mapId) {
    ConcurrentHashSet<Integer> set = null;
    Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
    if (mapKeyGenerater != null) {
      Serializable key = mapKeyGenerater.apply(role);
      if (key == null)
        return; 
      Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId));
      if (map == null) {
        map = SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(Integer.valueOf(mapId), new ConcurrentHashMap<>());
        if (map == null)
          map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(mapId)); 
      } 
      set = map.get(key);
      if (set == null) {
        set = map.putIfAbsent(key, new ConcurrentHashSet());
        if (set == null)
          set = map.get(key); 
      } 
    } else {
      set = (ConcurrentHashSet<Integer>)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId));
      if (set == null) {
        set = SessionCache.MAPID_ROLEIDS.put(Integer.valueOf(mapId), new ConcurrentHashSet());
        if (set == null)
          set = (ConcurrentHashSet<Integer>)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(mapId)); 
      } 
    } 
    set.add(role.getRoleId());
  }
  
  private void removeCache(Role role, int oldMapId) {
    ConcurrentHashSet<Integer> set = null;
    Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(oldMapId));
    if (mapKeyGenerater != null) {
      Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(oldMapId));
      if (map != null) {
        Serializable key = mapKeyGenerater.apply(role);
        set = (key != null) ? map.get(key) : null;
      } 
    } else {
      set = (ConcurrentHashSet<Integer>)SessionCache.MAPID_ROLEIDS.get(Integer.valueOf(oldMapId));
    } 
    if (set != null)
      set.remove(role.getRoleId()); 
  }
  
  public void changeMap(Role role, int mapId, int x, int y, String param) {
    if (role == null)
      return; 
    SysMapInfo mapInfo = getMap(mapId);
    if (mapInfo == null)
      return; 
    if (!checkChangeMap(role, mapId, param))
      return; 
    if (mapId == 3002 || mapId == 3021 || mapId == 3022) {
      Party party = SpringUtils.getPartyService().getParty(role);
      if (party == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("你还没有帮派哦#<E:51>#"));
        return;
      } 
      int scene = PartyDataPool.getScene(party.getLevel());
      if (mapId != scene) {
        mapId = scene;
        switch (scene) {
          case 3002:
            x = 50;
            y = 25;
            break;
          case 3021:
            x = 120;
            y = 71;
            break;
          case 3022:
            x = 120;
            y = 71;
            break;
        } 
      } 
    } 
    int oldMapId = role.getPos().getMapId().intValue();
    role.setPos(x, y, 5, mapId);
    this.teamService.teamTemporary(role, mapId);
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new EnterMapEvent(EventType.ENTER_MAP, role));
  }
  
  public void changeMap(Role role, ReqTelePort reqTelePort, String param) {
    if (role != null)
      changeMap(role, reqTelePort.getMapId(), reqTelePort.getX(), reqTelePort.getY(), param); 
  }
  
  private boolean checkChangeMap(Role role, int map, String param) {
    if (role.popTempCache("not_check_change_map") == null && MapDataPool.NOT_CHANGE_MAPS.contains(role.getPos().getMapId())) {
      SysMapInfo mapInfo = getMap(role.getPos().getMapId().intValue());
      String msg = MessageFormat.format("本场景禁止传送，请点击#<LG1>【{0}】#按钮离开。", new Object[] { mapInfo.getName() });
      if (mapInfo.getHowToOut() != null)
        msg = mapInfo.getHowToOut(); 
      this.teamService.pushMessage(role, (Message)new RespMsg(msg));
      return false;
    } 
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
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null && !team.isLeader(role.getRoleId().intValue()))
        this.teamService.leaveTemp(role, true); 
      MessagePusher.notify2Player(role, 1108, new Object[0]);
      return false;
    } 
    return true;
  }
  
  public void mapTarget(Role role, int map, int x, int y) {
    RespMapTarget mapTarget = new RespMapTarget();
    mapTarget.setMapId(map);
    mapTarget.setPosX(x);
    mapTarget.setPosY(y);
    MessagePusher.pushMessage(role, (Message)mapTarget);
  }
  
  public void sendEnterMap(Role role) {
    sendEnterMap(role, role.getPos(), false);
  }
  
  public void sendEnterMap(Role role, Pos pos, boolean isTaskWalk) {
    SysMapInfo mapInfo = getMap(pos.getMapId().intValue());
    RespEnterMap respEnterMap = new RespEnterMap();
    respEnterMap.setDir(pos.getDir().intValue());
    respEnterMap.setPosX(pos.getX().intValue());
    respEnterMap.setPosY(pos.getY().intValue());
    respEnterMap.setMapId(pos.getMapId().intValue());
    respEnterMap.setSafeZone((byte)mapInfo.getIsPkMap());
    if (isTaskWalk)
      respEnterMap.setIsTaskWalk((byte)1); 
    MessagePusher.pushMessage(role, (Message)respEnterMap);
    if (mapInfo.getIsPkMap() == 1)
      ((ChatService)SpringUtils.getBean(ChatService.class)).sendInfo(role.getId(), "大侠已进入非安全区域，可进行野外决斗，请务必小心！"); 
  }
  
  private void sendGameMapTransmission(Role role, int mapId) {
    List<MapTransmission> list = getTrasferInfo(mapId);
    RespMapTransmission respMapTransmission = new RespMapTransmission();
    respMapTransmission.setList(list);
    MessagePusher.pushMessage(role, (Message)respMapTransmission);
  }
  
  public void broadcastNpc(Role role, int mapId) {
    List<Npc> list = getNpcList(new GroupMapParam(role), mapId);
    for (Npc npc : list) {
      RespElseAppear elseAppear = new RespElseAppear();
      elseAppear.setId(npc.getId());
      elseAppear.setX((short)npc.getX());
      elseAppear.setY((short)npc.getY());
      elseAppear.setS((short)npc.getIcon());
      elseAppear.setN(npc.getName());
      elseAppear.setT(npc.getTitle());
      elseAppear.setC(npc.getMirror());
      MessagePusher.pushMessage(role, (Message)elseAppear);
    } 
  }
  
  public boolean isInPosition(Role role, int mapId, short x, short y, int perimeter) {
    if (role.getPos().getMapId().intValue() != mapId)
      return false; 
    int roleX = role.getPos().getX().intValue();
    int roleY = role.getPos().getY().intValue();
    return (Math.abs(roleX - x) <= perimeter && Math.abs(roleY - y) <= perimeter);
  }
  
  public void addGroupMapNpc(GroupMapParam groupMapParam, Npc npc) {
    int mapId = npc.getMap();
    Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
    if (mapKeyGenerater != null) {
      Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
      Map<Serializable, Set<Integer>> map = DataCache.GROUP_NPC_ID_MAP.computeIfAbsent(Integer.valueOf(mapId), k -> new ConcurrentHashMap<>());
      Set<Integer> set = map.computeIfAbsent(key, k -> new ConcurrentHashSet());
      set.add(Integer.valueOf(npc.getId()));
      DataCache.GROUP_NPC.put(Integer.valueOf(npc.getId()), npc);
    } 
  }
  
  public void removeGroupMapNpc(GroupMapParam groupMapParam, Npc npc) {
    int mapId = npc.getMap();
    Function<Role, Serializable> mapKeyGenerater = (Function<Role, Serializable>)MapDataPool.MAP_KEY_GENERATE.get(Integer.valueOf(mapId));
    if (mapKeyGenerater != null) {
      Serializable key = (groupMapParam.getRole() != null) ? mapKeyGenerater.apply(groupMapParam.getRole()) : groupMapParam.getGroupKey();
      Map<Serializable, Set<Integer>> map = (Map<Serializable, Set<Integer>>)DataCache.GROUP_NPC_ID_MAP.get(Integer.valueOf(mapId));
      if (map != null && key != null) {
        Set<Integer> set = map.get(key);
        if (set != null)
          set.remove(Integer.valueOf(npc.getId())); 
      } 
      DataCache.GROUP_NPC.remove(npc);
    } 
  }
  
  public void leaveMap(Role role, int mapId) {
    int oldMapId = mapId;
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new LeaveMapEvent(EventType.LEAVE_MAP, role, oldMapId));
  }
}
