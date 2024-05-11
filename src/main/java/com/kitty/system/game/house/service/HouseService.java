
package com.kitty.system.game.house.service;

import com.kitty.system.game.house.message.RespFurnitureStore;
import com.kitty.system.enums.house.HouseFurniture;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.house.message.vo.HouseEasy;
import com.kitty.system.game.house.message.RespHouse;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysDataFurniture;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.mina.cache.SessionCache;
import com.kitty.common.utils.ConcurrentHashSet;
import java.io.Serializable;
import java.util.Map;
import com.kitty.listener.event.TeamJoinEvent;
import com.kitty.listener.event.EnterMapEvent;
import com.kitty.listener.event.LeaveMapEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.house.pool.HouseDataPool;
import com.kitty.system.game.team.domain.Member;
import com.kitty.listener.event.LoginEvent;
import java.util.Collections;
import org.nutz.dao.Condition;
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
    
    public List<House> getHouse(final long id) {
        List<House> houses = DataCache.HOUSE_MAP.get(id);
        if (houses == null) {
            houses = Collections.synchronizedList((List<House>)this.dao.query(House.class, Cnd.where("r_id", "=", (Object)id)));
            DataCache.HOUSE_MAP.put(id, houses);
            return houses;
        }
        return houses;
    }
    
    public House getUseHouse(final long id) {
        final List<House> house = this.getHouse(id);
        return house.stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
            if (member != null && !member.isInTeam()) {
                final Role player = this.roleService.getPlayer(member.getId());
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
    public void handleLeaveMapEvent(final LeaveMapEvent leaveMapEvent) {
        final Role role = leaveMapEvent.getRole();
        if (!HouseDataPool.house.contains(leaveMapEvent.getMapId())) {
            this.removeGroupMap(role, leaveMapEvent.getMapId());
        }
    }
    
    @EventHandler({ EventType.ENTER_MAP })
    public void handleEnterMapEvent(final EnterMapEvent enterMapEvent) {
        final Role role = enterMapEvent.getRole();
        if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0) {
            final Role player = this.roleService.getPlayer(role.getHouse());
            this.addGroupMap(role, player.getRoleId(), role.getPos().getMapId());
        }
    }
    
    @EventHandler({ EventType.TEAM_JOIN })
    public void handleTeamJoinEvent(final TeamJoinEvent joinEvent) {
        final Role role = joinEvent.getRole();
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Role player = this.roleService.getPlayer(team.getLeaderRoleId());
            if (player.getHouse() > 0 && !role.getRoleId().equals(player.getHouse())) {
                final Integer mapId = player.getPos().getMapId();
                Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                if (map == null) {
                    SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(mapId, new ConcurrentHashMap<Serializable, ConcurrentHashSet<Integer>>());
                    map = SessionCache.GROUP_MAPID_ROLEIDS.get(mapId);
                }
                final int house = player.getHouse();
                final ConcurrentHashSet<Integer> set = map.get(house);
                if (set != null) {
                    set.add(role.getRoleId());
                    role.setHouse(house);
                }
            }
        }
    }
    
    public void reqHouseGoHome(final Role role, final byte type) {
        if (HouseDataPool.house.contains(role.getPos().getMapId()) && role.getHouse() > 0 && !role.getRoleId().equals(role.getHouse())) {
            this.removeGroupMap(role, role.getPos().getMapId());
        }
        final House useHouse = this.getUseHouse(role.getId());
        if (useHouse == null) {
            return;
        }
        int map = useHouse.getMotif();
        if (type == 0) {
            final SysDataFurniture furniture = HouseDataPool.furnitureMap.get(map);
            map = furniture.getHouse();
        }
        this.addGroupMap(role, role.getRoleId(), map);
        final String[] split = MapDataPool.mapInfoMap.get(map).getBornPoint().split(":");
        this.mapService.changeMap(role, map, Integer.valueOf(split[0]), Integer.valueOf(split[1]), null);
    }
    
    public void reqHouseFriedVisit(final Role role, final int host) {
        if (role.getRoleId().equals(host)) {
            return;
        }
        final Role player = this.roleService.getPlayer(host);
        if (player == null) {
            return;
        }
        if (HouseDataPool.house.contains(role.getPos().getMapId())) {
            this.removeGroupMap(role, role.getPos().getMapId());
        }
        final House useHouse = this.getUseHouse(player.getId());
        final String[] split = MapDataPool.mapInfoMap.get(useHouse.getMotif()).getBornPoint().split(":");
        this.mapService.changeMap(role, useHouse.getMotif(), Integer.valueOf(split[0]), Integer.valueOf(split[1]), null);
        this.addGroupMap(role, host, useHouse.getMotif());
    }
    
    public void removeGroupMap(final Role role, final int host) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            for (int i = 0; i < team.getList().size(); ++i) {
                final Role memberRole = this.roleService.getPlayer(team.getList().get(i).getId());
                if (memberRole != null) {
                    this.executeRemoveGroupMap(memberRole, host);
                }
            }
        }
        else {
            this.executeRemoveGroupMap(role, host);
        }
    }
    
    public void executeRemoveGroupMap(final Role role, final int host) {
        if (role.getHouse() > 0 && !role.getRoleId().equals(role.getHouse())) {
            final Role player = this.roleService.getPlayer(role.getHouse());
            final Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(host);
            if (map != null) {
                final ConcurrentHashSet<Integer> set = map.get(player.getRoleId());
                if (set != null) {
                    set.remove(role.getRoleId());
                    role.setHouse(0);
                    if (set.size() <= 0) {
                        SessionCache.GROUP_MAPID_ROLEIDS.remove(host);
                    }
                }
            }
        }
    }
    
    public void addGroupMap(final Role role, final int host, final int serial) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        Map<Serializable, ConcurrentHashSet<Integer>> map = SessionCache.GROUP_MAPID_ROLEIDS.get(serial);
        if (map == null) {
            SessionCache.GROUP_MAPID_ROLEIDS.putIfAbsent(serial, new ConcurrentHashMap<Serializable, ConcurrentHashSet<Integer>>());
            map = SessionCache.GROUP_MAPID_ROLEIDS.get(serial);
        }
        ConcurrentHashSet<Integer> set = map.get(host);
        if (set == null) {
            set = new ConcurrentHashSet<Integer>();
            map.put(host, set);
        }
        if (team != null && team.isLeader(role.getRoleId())) {
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (member.isInTeam()) {
                    final Role memberRole = this.roleService.getPlayer(member.getId());
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
    
    public void house(final Role role) {
        final RespHouse respHouse = new RespHouse();
        final List<House> list = this.getHouse(role.getId());
        for (final House house : list) {
            respHouse.getList().add(new HouseEasy(house));
        }
        MessagePusher.pushMessage(role.getId(), respHouse);
    }
    
    public void switchoverMotif(final Role role, final long id) {
        final List<House> list = this.getHouse(role.getId());
        final House house = list.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (house == null || house.getState() == 1) {
            return;
        }
        list.forEach(e -> {
            if (e.getState() == 1) {
                e.setState((byte)0);
                e.save();
                return;
            }
            else {
                return;
            }
        });
        house.setState((byte)1);
        house.save();
        this.house(role);
    }
    
    public void reqBuyFurniture(final Role role, final int serial) {
        final SysDataFurniture furniture = HouseDataPool.furnitureMap.get(serial);
        if (furniture == null) {
            return;
        }
        if (!this.roleService.deductCurrency(role, furniture.getType(), furniture.getPrice(), "购买家具")) {
            return;
        }
        if (furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
            final House house = this.addHouse(role.getId(), furniture.getSerial());
            if (house != null) {
                final RespHouse respHouse = new RespHouse();
                respHouse.getList().add(new HouseEasy(house));
                MessagePusher.pushMessage(role.getId(), respHouse);
            }
        }
    }
    
    public House addHouse(final long id, final int serial) {
        final SysDataFurniture furniture = HouseDataPool.furnitureMap.get(serial);
        if (furniture != null && furniture.getClassify() == HouseFurniture.MOTIF.getType()) {
            final List<House> list = this.getHouse(id);
            byte state = 0;
            if (list.size() <= 0) {
                state = 1;
            }
            final House house = new House(this.worker.nextId(), id, furniture.getName(), furniture.getSerial(), state);
            list.add(house);
            house.save();
            return house;
        }
        return null;
    }
    
    public void openFurnitureStore(final Role role) {
        if (role.getHouse() > 0 && !role.getRoleId().equals(role.getHouse())) {
            MessagePusher.pushMessage(role.getId(), new RespFurnitureStore((byte)0));
            return;
        }
        MessagePusher.pushMessage(role.getId(), new RespFurnitureStore((byte)1));
    }
}
