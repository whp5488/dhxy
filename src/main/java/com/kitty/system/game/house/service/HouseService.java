package com.kitty.system.game.house.service;

import com.kitty.system.game.house.message.RespFurnitureStore;
import com.kitty.system.enums.house.HouseFurniture;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.house.message.vo.HouseEasy;
import com.kitty.system.game.house.message.RespHouse;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysDataFurniture;
import java.io.Serializable;
import com.kitty.common.utils.ConcurrentHashSet;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.mina.cache.SessionCache;
import java.util.Map;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.house.pool.HouseDataPool;
import com.kitty.listener.event.LoginEvent;
import java.util.Collections;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.house.model.House;
import java.util.List;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class HouseService
{
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
    
    public HouseService() {
    }
    
    public List<House> getHouse(long id) {
        List<House> houses = (List)DataCache.HOUSE_MAP.get(Long.valueOf(id));
        if (houses == null) {
            houses = Collections.synchronizedList(this.dao.query(House.class, Cnd.where("r_id", "=", Long.valueOf(id))));
            DataCache.HOUSE_MAP.put(Long.valueOf(id), houses);
            return houses;
        }
        return houses;
    }
    
    public House getUseHouse(long id) {
        List<House> house = this.getHouse(id);
        return (House)house.stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            Member member = (Member)team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
            if (member != null && !member.isInTeam()) {
                Role player = this.roleService.getPlayer((long)member.getId());
                if (HouseDataPool.house.contains(player.getPos().getMapId())) {
                    this.mapService.changeMap(role, 1011, 124, 67, null);
                }
            }
        }
        else if (HouseDataPool.house.contains(role.getPos().getMapId())) {
            this.mapService.changeMap(role, 1011, 124, 67, null);
        }
    }
    
    @EventHandler({ EventType.LEAVE_MAP })
    public void handleLeaveMapEvent(LeaveMapEvent leaveMapEvent) {
        Role role = leaveMapEvent.getRole();
        if (!HouseDataPool.house.contains(Integer.valueOf(leaveMapEvent.getMapId()))) {
            this.removeGroupMap(role, leaveMapEvent.getMapId());
        }
    }
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(EnterMapEvent enterMapEvent) {
        Role role = enterMapEvent.getRole();
        if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0) {
            Role player = this.roleService.getPlayer(role.getHouse());
            this.addGroupMap(role, (int)player.getRoleId(), (int)role.getPos().getMapId());
        }
    }
    
    @EventHandler({ EventType.TEAM_JOIN })
    public void handleTeamJoinEvent(TeamJoinEvent joinEvent) {
        Role role = joinEvent.getRole();
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            Role player = this.roleService.getPlayer(team.getLeaderRoleId());
            if (player.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(player.getHouse()))) {
                Integer mapId = player.getPos().getMapId();
                Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                if (map == null) {
                    SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(mapId, new ConcurrentHashMap());
                    map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                }
                int house = player.getHouse();
                ConcurrentHashSet<Integer> set = (ConcurrentHashSet)map.get(Integer.valueOf(house));
                if (set != null) {
                    set.add(role.getRoleId());
                    role.setHouse(house);
                }
            }
        }
    }
    
    public void reqHouseGoHome(Role role, byte type) {
        if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
            this.removeGroupMap(role, (int)role.getPos().getMapId());
        }
        House useHouse = this.getUseHouse((long)role.getId());
        if (useHouse == null) {
            return;
        }
        int map = useHouse.getMotif();
        if (type == 0) {
            SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(map));
            map = furniture.getHouse();
        }
        this.addGroupMap(role, (int)role.getRoleId(), map);
        String[] split = ((SysMapInfo)MapDataPool.mapInfoMap.get(Integer.valueOf(map))).getBornPoint().split(":");
        this.mapService.changeMap(role, map, (int)Integer.valueOf(split[0]), (int)Integer.valueOf(split[1]), null);
        return;
    }
    
    public void reqHouseFriedVisit(Role role, int host) {
        if (role.getRoleId().equals(Integer.valueOf(host))) {
            return;
        }
        Role player = this.roleService.getPlayer(host);
        if (player == null) {
            return;
        }
        if (HouseDataPool.house.contains(role.getPos().getMapId())) {
            this.removeGroupMap(role, (int)role.getPos().getMapId());
        }
        House useHouse = this.getUseHouse((long)player.getId());
        String[] split = ((SysMapInfo)MapDataPool.mapInfoMap.get(Integer.valueOf(useHouse.getMotif()))).getBornPoint().split(":");
        this.mapService.changeMap(role, useHouse.getMotif(), (int)Integer.valueOf(split[0]), (int)Integer.valueOf(split[1]), null);
        this.addGroupMap(role, host, useHouse.getMotif());
        return;
    }
    
    public void removeGroupMap(Role role, int host) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            for (int i = 0; i < team.getList().size(); ++i) {
                Role memberRole = this.roleService.getPlayer((long)((Member)team.getList().get(i)).getId());
                if (memberRole != null) {
                    this.executeRemoveGroupMap(memberRole, host);
                }
            }
        }
        else {
            this.executeRemoveGroupMap(role, host);
        }
    }
    
    public void executeRemoveGroupMap(Role role, int host) {
        if (role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
            Role player = this.roleService.getPlayer(role.getHouse());
            Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(host));
            if (map != null) {
                ConcurrentHashSet<Integer> set = (ConcurrentHashSet)map.get(player.getRoleId());
                if (set != null) {
                    set.remove(role.getRoleId());
                    role.setHouse(0);
                    if (set.size() <= 0) {
                        SessionCache.GROUP_MAPID_ROLEIDS.remove(Integer.valueOf(host));
                    }
                }
            }
        }
    }
    
    public void addGroupMap(Role role, int host, int serial) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Map<Serializable, ConcurrentHashSet<Integer>> map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(serial));
        if (map == null) {
            SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(Integer.valueOf(serial), new ConcurrentHashMap());
            map = (Map)SessionCache.GROUP_MAPID_ROLEIDS.get(Integer.valueOf(serial));
        }
        ConcurrentHashSet<Integer> set = (ConcurrentHashSet)map.get(Integer.valueOf(host));
        if (set == null) {
            set = new ConcurrentHashSet();
            map.put(Integer.valueOf(host), set);
        }
        if (team != null && team.isLeader((int)role.getRoleId())) {
            for (int i = 0; i < team.getList().size(); ++i) {
                Member member = (Member)team.getList().get(i);
                if (member.isInTeam()) {
                    Role memberRole = this.roleService.getPlayer((long)member.getId());
                    if (memberRole != null) {
                        set.add(memberRole.getRoleId());
                        memberRole.setHouse(host);
                    }
                }
            }
        }
        else {
            set.add(role.getRoleId());
            role.setHouse(host);
        }
    }
    
    public void house(Role role) {
        RespHouse respHouse = new RespHouse();
        List<House> list = this.getHouse((long)role.getId());
        for (House house : list) {
            respHouse.getList().add(new HouseEasy(house));
        }
        MessagePusher.pushMessage((long)role.getId(), respHouse);
    }
    
    public void switchoverMotif(Role role, long id) {
        List<House> list = this.getHouse((long)role.getId());
        House house = (House)list.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (house == null || house.getState() == 1) {
            return;
        }
        list.forEach(e -> {
            if (e.getState() == 1) {
                e.setState((byte)0);
                e.save();
                return;
            }
            return;
        });
        house.setState((byte)1);
        house.save();
        this.house(role);
        return;
    }
    
    public void reqBuyFurniture(Role role, int serial) {
        SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(serial));
        if (furniture == null) {
            return;
        }
        if (!this.roleService.deductCurrency(role, Integer.valueOf(furniture.getType()), furniture.getPrice(), "购买家具")) {
            return;
        }
        if (furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
            House house = this.addHouse((long)role.getId(), furniture.getSerial());
            if (house != null) {
                RespHouse respHouse = new RespHouse();
                respHouse.getList().add(new HouseEasy(house));
                MessagePusher.pushMessage((long)role.getId(), respHouse);
            }
        }
        return;
    }
    
    public House addHouse(long id, int serial) {
        SysDataFurniture furniture = (SysDataFurniture)HouseDataPool.furnitureMap.get(Integer.valueOf(serial));
        if (furniture != null && furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
            List<House> list = this.getHouse(id);
            byte state = 0;
            if (list.size() <= 0) {
                state = 1;
            }
            House house = new House(Long.valueOf(this.worker.nextId()), id, furniture.getName(), furniture.getSerial(), state);
            list.add(house);
            house.save();
            return house;
        }
        else {
            return null;
        }
    }
    
    public void openFurnitureStore(Role role) {
        if (role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
            MessagePusher.pushMessage((long)role.getId(), new RespFurnitureStore((byte)0));
            return;
        }
        MessagePusher.pushMessage((long)role.getId(), new RespFurnitureStore((byte)1));
        return;
    }
}
