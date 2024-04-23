package com.kitty.system.game.house.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.house.HouseFurniture;
import com.kitty.system.game.house.message.RespFurnitureStore;
import com.kitty.system.game.house.message.RespHouse;
import com.kitty.system.game.house.message.vo.HouseEasy;
import com.kitty.system.game.house.model.House;
import com.kitty.system.game.house.pool.HouseDataPool;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.init.domain.SysDataFurniture;
import com.kitty.system.init.domain.SysMapInfo;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseService {
  @Autowired
  Dao dao;
  
  @Autowired
  IdWorker worker;
  
  @Autowired
  RoleService roleService;
  
  @Autowired
  MapService mapService;
  
  @Autowired
  TeamService teamService;
  
  public List<House> getHouse(long id) {
    List<House> houses = (List<House>)DataCache.HOUSE_MAP.get(Long.valueOf(id));
    if (houses == null) {
      houses = Collections.synchronizedList(this.dao.query(House.class, (Condition)Cnd.where("r_id", "=", Long.valueOf(id))));
      DataCache.HOUSE_MAP.put(Long.valueOf(id), houses);
      return houses;
    } 
    return houses;
  }
  
  public House getUseHouse(long id) {
    List<House> house = getHouse(id);
    return house.stream().filter(e -> (e.getState() == 1)).findFirst().orElse(null);
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
      if (member != null && !member.isInTeam()) {
        Role player = this.roleService.getPlayer(member.getId().longValue());
        if (HouseDataPool.house.contains(player.getPos().getMapId()))
          this.mapService.changeMap(role, 1011, 124, 67, null); 
      } 
    } else if (HouseDataPool.house.contains(role.getPos().getMapId())) {
      this.mapService.changeMap(role, 1011, 124, 67, null);
    } 
  }
  
  @EventHandler({EventType.LEAVE_MAP})
  public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
    Role role = leaveMapEvent.getRole();
    if (!HouseDataPool.house.contains(Integer.valueOf(leaveMapEvent.getMapId())))
      removeGroupMap(role, leaveMapEvent.getMapId()); 
  }
  
  @EventHandler({EventType.ENTER_MAP})
  public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
    Role role = enterMapEvent.getRole();
    if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0) {
      Role player = this.roleService.getPlayer(role.getHouse());
      addGroupMap(role, player.getRoleId().intValue(), role.getPos().getMapId().intValue());
    } 
  }
  
  @EventHandler({EventType.TEAM_JOIN})
  public void handleTeamJoinEvent(TeamJoinEvent joinEvent) {
    Role role = joinEvent.getRole();
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      Role player = this.roleService.getPlayer(team.getLeaderRoleId());
      if (player.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(player.getHouse()))) {
        Integer mapId = player.getPos().getMapId();
        Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
        if (map == null) {
          SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(mapId, new ConcurrentHashMap<>());
          map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
        } 
        int house = player.getHouse();
        ConcurrentHashSet<Integer> set = map.get(Integer.valueOf(house));
        if (set != null) {
          set.add(role.getRoleId());
          role.setHouse(house);
        } 
      } 
    } 
  }
  
  public void reqHouseGoHome(Role role, byte type) {
    if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse())))
      removeGroupMap(role, role.getPos().getMapId().intValue()); 
    House useHouse = getUseHouse(role.getId().longValue());
    if (useHouse == null)
      return; 
    int map = useHouse.getMotif();
    if (type == 0) {
      SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(map));
      map = furniture.getHouse();
    } 
    addGroupMap(role, role.getRoleId().intValue(), map);
    String[] split = ((SysMapInfo)MapDataPool.mapInfoMap.get(Integer.valueOf(map))).getBornPoint().split(":");
    this.mapService.changeMap(role, map, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), null);
  }
  
  public void reqHouseFriedVisit(Role role, int host) {
    if (role.getRoleId().equals(Integer.valueOf(host)))
      return; 
    Role player = this.roleService.getPlayer(host);
    if (player == null)
      return; 
    if (HouseDataPool.house.contains(role.getPos().getMapId()))
      removeGroupMap(role, role.getPos().getMapId().intValue()); 
    House useHouse = getUseHouse(player.getId().longValue());
    String[] split = ((SysMapInfo)MapDataPool.mapInfoMap.get(Integer.valueOf(useHouse.getMotif()))).getBornPoint().split(":");
    this.mapService.changeMap(role, useHouse.getMotif(), Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue(), null);
    addGroupMap(role, host, useHouse.getMotif());
  }
  
  public void removeGroupMap(Role role, int host) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      for (int i = 0; i < team.getList().size(); i++) {
        Role memberRole = this.roleService.getPlayer(((Member)team.getList().get(i)).getId().longValue());
        if (memberRole != null)
          executeRemoveGroupMap(memberRole, host); 
      } 
    } else {
      executeRemoveGroupMap(role, host);
    } 
  }
  
  public void executeRemoveGroupMap(Role role, int host) {
    if (role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
      Role player = this.roleService.getPlayer(role.getHouse());
      Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(host));
      if (map != null) {
        ConcurrentHashSet<Integer> set = map.get(player.getRoleId());
        if (set != null) {
          set.remove(role.getRoleId());
          role.setHouse(0);
          if (set.size() <= 0)
            SessionCache.GROUP_MAPID_ROLEIDS.remove(Integer.valueOf(host)); 
        } 
      } 
    } 
  }
  
  public void addGroupMap(Role role, int host, int serial) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    Map<Serializable, ConcurrentHashSet<Integer>> map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(serial));
    if (map == null) {
      SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(Integer.valueOf(serial), new ConcurrentHashMap<>());
      map = (Map<Serializable, ConcurrentHashSet<Integer>>)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(serial));
    } 
    ConcurrentHashSet<Integer> set = map.get(Integer.valueOf(host));
    if (set == null) {
      set = new ConcurrentHashSet();
      map.put(Integer.valueOf(host), set);
    } 
    if (team != null && team.isLeader(role.getRoleId().intValue())) {
      for (int i = 0; i < team.getList().size(); i++) {
        Member member = team.getList().get(i);
        if (member.isInTeam()) {
          Role memberRole = this.roleService.getPlayer(member.getId().longValue());
          if (memberRole != null) {
            set.add(memberRole.getRoleId());
            memberRole.setHouse(host);
          } 
        } 
      } 
    } else {
      set.add(role.getRoleId());
      role.setHouse(host);
    } 
  }
  
  public void house(Role role) {
    RespHouse respHouse = new RespHouse();
    List<House> list = getHouse(role.getId().longValue());
    for (House house : list)
      respHouse.getList().add(new HouseEasy(house)); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)respHouse);
  }
  
  public void switchoverMotif(Role role, long id) {
    List<House> list = getHouse(role.getId().longValue());
    House house = list.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
    if (house == null || house.getState() == 1)
      return; 
    list.forEach(e -> {
          if (e.getState() == 1) {
            e.setState((byte)0);
            e.save();
            return;
          } 
        });
    house.setState((byte)1);
    house.save();
    house(role);
  }
  
  public void reqBuyFurniture(Role role, int serial) {
    SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(serial));
    if (furniture == null)
      return; 
    if (!this.roleService.deductCurrency(role, Integer.valueOf(furniture.getType()), furniture.getPrice(), "购买家具"))
      return; 
    if (furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
      House house = addHouse(role.getId().longValue(), furniture.getSerial());
      if (house != null) {
        RespHouse respHouse = new RespHouse();
        respHouse.getList().add(new HouseEasy(house));
        MessagePusher.pushMessage(role.getId().longValue(), (Message)respHouse);
      } 
    } 
  }
  
  public House addHouse(long id, int serial) {
    SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(serial));
    if (furniture != null && furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
      List<House> list = getHouse(id);
      byte state = 0;
      if (list.size() <= 0)
        state = 1; 
      House house = new House(Long.valueOf(this.worker.nextId()), id, furniture.getName(), furniture.getSerial(), state);
      list.add(house);
      house.save();
      return house;
    } 
    return null;
  }
  
  public void openFurnitureStore(Role role) {
    if (role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespFurnitureStore((byte)0));
      return;
    } 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespFurnitureStore((byte)1));
  }
}
