package com.kitty.system.game.broadcast;

import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.role.message.RespUpdateAppearance;
import java.util.Set;
import java.util.HashSet;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.enter.message.RespDisappear;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.horse.domain.Horse;
import java.util.List;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.ArrayList;
import com.kitty.system.game.enter.message.RespAppear;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.enter.RespTitle;
import com.kitty.system.game.team.domain.Member;
import com.kitty.common.utils.ConcurrentHashSet;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.team.message.RespShift;
import com.kitty.system.game.enter.RespPositionMove;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.role.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.equip.service.EquipService;
import org.springframework.stereotype.Service;

@Service
public class BroadcastService
{
    @Autowired
    public EquipService equipService;
    @Autowired
    public TitleService titleService;
    @Autowired
    public MapService mapService;
    @Autowired
    public TeamService teamService;
    @Autowired
    public FightNewService fightNewService;
    @Autowired
    public PartyService partyService;
    @Autowired
    public HorseService horseService;
    
    public BroadcastService() {
    }
    
    public Message getMovePacket(Role role) {
        RespPositionMove respPositionMove = new RespPositionMove();
        respPositionMove.setRoleId((int)role.getRoleId());
        respPositionMove.setX((int)role.getPos().getX());
        respPositionMove.setY((int)role.getPos().getY());
        return respPositionMove;
    }
    
    public Message getMoveGoto(int id, int x, int y, int map) {
        RespShift respShift = new RespShift();
        respShift.setId(id);
        respShift.setX(x);
        respShift.setY(y);
        respShift.setMapid(map);
        return respShift;
    }
    
    public boolean inSameScene(Role role, Role otherRole) {
        if (role == null || otherRole == null) {
            return false;
        }
        if (MapDataPool.MAP_KEY_GENERATE.containsKey(role.getPos().getMapId())) {
            ConcurrentHashSet<Integer> roleIds = this.mapService.getRoleIds(new GroupMapParam(role), (int)role.getPos().getMapId());
            if (role.getHouse() > 0 && !role.getRoleId().equals(Integer.valueOf(role.getHouse()))) {
                roleIds = this.mapService.getRoleIds(new GroupMapParam(Integer.valueOf(role.getHouse())), (int)role.getPos().getMapId());
            }
            return roleIds != null && roleIds.contains(otherRole.getRoleId());
        }
        else {
            return role.getPos().getMapId().equals(otherRole.getPos().getMapId());
        }
    }
    
    public Message getTitleInfoNew(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Fight fight = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        String temp = "";
        if (team != null) {
            if (team.isLeader((int)role.getRoleId())) {
                if (team.getList().size() == 5) {
                    temp += "10";
                }
                else {
                    temp += "01";
                }
            }
            else {
                int i = 0;
                while (i < team.getList().size()) {
                    Member member = (Member)team.getList().get(i);
                    if (member.getRoleId().equals(role.getRoleId()) && member.isInTeam()) {
                        temp += "00";
                        break;
                    }
                    ++i;
                }
            }
        }
        else {
            temp += "00";
        }
        if (fight != null && !fight.isOver()) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        if (role.getPkInfoBox().isRedName() && !role.getPos().getMapId().equals(Integer.valueOf(1201))) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        RespTitle respTitle = new RespTitle();
        respTitle.setRoleId((int)role.getRoleId());
        byte test = Byte.parseByte(temp, 2);
        respTitle.setParam(test);
        return respTitle;
    }
    
    public void sendUpdateAppear(Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getUpdateAppear(role));
    }
    
    public Message getAppear(Role role) {
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        RespAppear respAppear = new RespAppear();
        List<FiedValue> list = new ArrayList<>();
        respAppear.setRoleId((int)role.getRoleId());
        respAppear.setPosX((int)role.getPos().getX());
        respAppear.setPosY((int)role.getPos().getY());
        respAppear.setSerial((int)role.getSerial());
        respAppear.setName(role.getName());
        respAppear.setPartyName(SpringUtils.getRoleService().getFactionName(role));
        respAppear.setJob(this.partyService.getJob(role));
        respAppear.setRelive((int)role.getRelive());
        respAppear.setLevel((int)role.getLevel());
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            respAppear.setLeaderId(team.getLeaderRoleId());
            Member member = team.getMember((int)role.getRoleId());
            if (member != null) {
                list.add(new FiedValue("inTeam", (byte)1, Integer.valueOf(member.isInTeam() ? 1 : 2)));
            }
        }
        respAppear.setLight_effect(this.equipService.getWearEffect(role));
        respAppear.setVip((int)role.getVip());
        respAppear.setFly_type(0);
        respAppear.setPortrait(role.getIcon());
        respAppear.setWeapon(role.getWeaponIcon());
        Horse horse = this.horseService.getRideMount(role);
        if (horse != null) {
            respAppear.setMount_icon(horse.getSerial());
            respAppear.setMoveSpeed(horse.getMoveSp());
        }
        int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            respAppear.setMoveSpeed(30);
        }
        List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            respAppear.setGem(num);
        }
        RoleService roleService = SpringUtils.getRoleService();
        respAppear.setTitle(this.titleService.getTitle(role));
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            list.add(new FiedValue("card", (byte)3, Integer.valueOf(role.getMasquerade())));
        }
        List<FiedValue> deyList = roleService.deyList(role.getColor());
        list.addAll(deyList);
        if (list.size() > 0) {
            respAppear.setList(list);
            return respAppear;
        }
        return respAppear;
    }
    
    public void sendAppear(Role role) {
        Message titleInfo = this.getTitleInfoNew(role);
        Message respAppear = this.getAppear(role);
        MessagePusher.pushMessage(role, respAppear);
        MessagePusher.pushMessage(role, titleInfo);
    }
    
    public void sendLocalMessage(Role role, Message message) {
        SceneManager.INSTANCE.sendMessages(role, message);
    }
    
    public Message getDisAppear(int petId, int type) {
        if (petId <= 0) {
            return null;
        }
        RespDisappear respDisappear = new RespDisappear();
        respDisappear.setId(petId);
        respDisappear.setType(type);
        return respDisappear;
    }
    
    public Message getDisAppear(Role meRole) {
        return this.getDisAppear((int)meRole.getRoleId(), LOGIC.LOGICTYPE_HERO.getValue());
    }
    
    public void sendTitleInfo(Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getTitleInfoNew(role));
    }
    
    public void sendTitleInfoNew(Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getTitleInfoNew(role));
    }
    
    public void updateAppearance(Role role) {
        Message appear = this.getUpdateAppear(role);
        MessagePusher.pushMessage(role, appear);
    }
    
    public void updateAppearance(Role role, Role appearRole) {
        if (appearRole == null) {
            return;
        }
        Message appear = this.getUpdateAppear(appearRole);
        MessagePusher.pushMessage(role, appear);
        return;
    }
    
    public void giveAppear(Role role, Team team) {
        if (role != null) {
            Set<Long> enters = new HashSet();
            for (int i = 0; i < team.getList().size(); ++i) {
                Member member = (Member)team.getList().get(i);
                if (!member.getId().equals(role.getId())) {
                    enters.add(member.getId());
                }
            }
            SceneManager.INSTANCE.giveAppear(role, enters);
        }
    }
    
    public Message getUpdateAppear(Role role) {
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        List<FiedValue> list = new ArrayList<>();
        RespUpdateAppearance respUpdateAppearance = new RespUpdateAppearance();
        respUpdateAppearance.setRoleId((int)role.getRoleId());
        respUpdateAppearance.setSerial((int)role.getSerial());
        respUpdateAppearance.setRoleName(role.getName());
        respUpdateAppearance.setPartyName(SpringUtils.getRoleService().getFactionName(role));
        respUpdateAppearance.setJob(this.partyService.getJob(role));
        respUpdateAppearance.setRelive((int)role.getRelive());
        respUpdateAppearance.setLevel((int)role.getLevel());
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            respUpdateAppearance.setLeaderId(team.getLeaderRoleId());
            Member member = team.getMember((int)role.getRoleId());
            if (member != null) {
                list.add(new FiedValue("inTeam", (byte)1, Integer.valueOf(member.isInTeam() ? 1 : 2)));
            }
        }
        respUpdateAppearance.setLight_effect(this.equipService.getWearEffect(role));
        respUpdateAppearance.setVip((int)role.getVip());
        respUpdateAppearance.setFly_type(0);
        respUpdateAppearance.setPortrait(role.getIcon());
        respUpdateAppearance.setWeapon(role.getWeaponIcon());
        Horse horse = this.horseService.getRideMount(role);
        if (horse != null) {
            respUpdateAppearance.setMount_icon(horse.getSerial());
            respUpdateAppearance.setMoveSpeed(horse.getMoveSp());
        }
        int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            respUpdateAppearance.setMoveSpeed(30);
        }
        List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            respUpdateAppearance.setGem(num);
        }
        RoleService roleService = SpringUtils.getRoleService();
        respUpdateAppearance.setTitle(this.titleService.getTitle(role));
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            list.add(new FiedValue("card", (byte)3, Integer.valueOf(role.getMasquerade())));
        }
        List<FiedValue> deyList = roleService.deyList(role.getColor());
        list.addAll(deyList);
        if (list.size() > 0) {
            respUpdateAppearance.setList(list);
            return respUpdateAppearance;
        }
        return respUpdateAppearance;
    }
    
    public void handleOnEquip(Role role, BagEquip equip) {
        if (equip.getPosition() == ADORN.WEAPON.getValue() && equip.getPos() <= 0) {
            int num = DataGlobalFunc.getGemNum(equip);
            if (num >= 10 || num <= 0) {
                this.sendUpdateAppear(role);
            }
        }
    }
}
