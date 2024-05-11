
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
import java.util.Collection;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.enter.message.RespAppear;
import com.kitty.system.game.scene.SceneManager;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.enter.RespTitle;
import com.kitty.system.game.team.domain.Member;
import com.kitty.common.utils.ConcurrentHashSet;
import java.io.Serializable;
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
    
    public Message getMovePacket(final Role role) {
        final RespPositionMove respPositionMove = new RespPositionMove();
        respPositionMove.setRoleId(role.getRoleId());
        respPositionMove.setX(role.getPos().getX());
        respPositionMove.setY(role.getPos().getY());
        return respPositionMove;
    }
    
    public Message getMoveGoto(final int id, final int x, final int y, final int map) {
        final RespShift respShift = new RespShift();
        respShift.setId(id);
        respShift.setX(x);
        respShift.setY(y);
        respShift.setMapid(map);
        return respShift;
    }
    
    public boolean inSameScene(final Role role, final Role otherRole) {
        if (role == null || otherRole == null) {
            return false;
        }
        if (MapDataPool.MAP_KEY_GENERATE.containsKey(role.getPos().getMapId())) {
            ConcurrentHashSet roleIds = this.mapService.getRoleIds(new GroupMapParam(role), role.getPos().getMapId());
            if (role.getHouse() > 0 && !role.getRoleId().equals(role.getHouse())) {
                roleIds = this.mapService.getRoleIds(new GroupMapParam(role.getHouse()), role.getPos().getMapId());
            }
            return roleIds != null && roleIds.contains(otherRole.getRoleId());
        }
        return role.getPos().getMapId().equals(otherRole.getPos().getMapId());
    }
    
    public Message getTitleInfoNew(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        String temp = "";
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                if (team.getList().size() == 5) {
                    temp += "10";
                }
                else {
                    temp += "01";
                }
            }
            else {
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member.getRoleId().equals(role.getRoleId()) && member.isInTeam()) {
                        temp += "00";
                        break;
                    }
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
        if (role.getPkInfoBox().isRedName() && !role.getPos().getMapId().equals(1201)) {
            temp += "1";
        }
        else {
            temp += "0";
        }
        final RespTitle respTitle = new RespTitle();
        respTitle.setRoleId(role.getRoleId());
        final byte test = Byte.parseByte(temp, 2);
        respTitle.setParam(test);
        return respTitle;
    }
    
    public void sendUpdateAppear(final Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getUpdateAppear(role));
    }
    
    public Message getAppear(final Role role) {
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        final RespAppear respAppear = new RespAppear();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        respAppear.setRoleId(role.getRoleId());
        respAppear.setPosX(role.getPos().getX());
        respAppear.setPosY(role.getPos().getY());
        respAppear.setSerial(role.getSerial());
        respAppear.setName(role.getName());
        respAppear.setPartyName(SpringUtils.getRoleService().getFactionName(role));
        respAppear.setJob(this.partyService.getJob(role));
        respAppear.setRelive(role.getRelive());
        respAppear.setLevel(role.getLevel());
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            respAppear.setLeaderId(team.getLeaderRoleId());
            final Member member = team.getMember(role.getRoleId());
            if (member != null) {
                list.add(new FiedValue("inTeam", (byte)1, member.isInTeam() ? 1 : 2));
            }
        }
        respAppear.setLight_effect(this.equipService.getWearEffect(role));
        respAppear.setVip(role.getVip());
        respAppear.setFly_type(0);
        respAppear.setPortrait(role.getIcon());
        respAppear.setWeapon(role.getWeaponIcon());
        final Horse horse = this.horseService.getRideMount(role);
        if (horse != null) {
            respAppear.setMount_icon(horse.getSerial());
            respAppear.setMoveSpeed(horse.getMoveSp());
        }
        final int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            respAppear.setMoveSpeed(30);
        }
        final List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            respAppear.setGem(num);
        }
        final RoleService roleService = SpringUtils.getRoleService();
        respAppear.setTitle(this.titleService.getTitle(role));
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            list.add(new FiedValue("card", (byte)3, role.getMasquerade()));
        }
        final List<FiedValue> deyList = roleService.deyList(role.getColor());
        list.addAll(deyList);
        if (list.size() > 0) {
            respAppear.setList(list);
        }
        return respAppear;
    }
    
    public void sendAppear(final Role role) {
        final Message titleInfo = this.getTitleInfoNew(role);
        final Message respAppear = this.getAppear(role);
        MessagePusher.pushMessage(role, respAppear);
        MessagePusher.pushMessage(role, titleInfo);
    }
    
    public void sendLocalMessage(final Role role, final Message message) {
        SceneManager.INSTANCE.sendMessages(role, message);
    }
    
    public Message getDisAppear(final int petId, final int type) {
        if (petId <= 0) {
            return null;
        }
        final RespDisappear respDisappear = new RespDisappear();
        respDisappear.setId(petId);
        respDisappear.setType(type);
        return respDisappear;
    }
    
    public Message getDisAppear(final Role meRole) {
        return this.getDisAppear(meRole.getRoleId(), LOGIC.LOGICTYPE_HERO.getValue());
    }
    
    public void sendTitleInfo(final Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getTitleInfoNew(role));
    }
    
    public void sendTitleInfoNew(final Role role) {
        SceneManager.INSTANCE.sendMessages(role, this.getTitleInfoNew(role));
    }
    
    public void updateAppearance(final Role role) {
        final Message appear = this.getUpdateAppear(role);
        MessagePusher.pushMessage(role, appear);
    }
    
    public void updateAppearance(final Role role, final Role appearRole) {
        if (appearRole == null) {
            return;
        }
        final Message appear = this.getUpdateAppear(appearRole);
        MessagePusher.pushMessage(role, appear);
    }
    
    public void giveAppear(final Role role, final Team team) {
        if (role != null) {
            final Set<Long> enters = new HashSet<Long>();
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (!member.getId().equals(role.getId())) {
                    enters.add(member.getId());
                }
            }
            SceneManager.INSTANCE.giveAppear(role, enters);
        }
    }
    
    public Message getUpdateAppear(final Role role) {
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        final RespUpdateAppearance respUpdateAppearance = new RespUpdateAppearance();
        respUpdateAppearance.setRoleId(role.getRoleId());
        respUpdateAppearance.setSerial(role.getSerial());
        respUpdateAppearance.setRoleName(role.getName());
        respUpdateAppearance.setPartyName(SpringUtils.getRoleService().getFactionName(role));
        respUpdateAppearance.setJob(this.partyService.getJob(role));
        respUpdateAppearance.setRelive(role.getRelive());
        respUpdateAppearance.setLevel(role.getLevel());
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            respUpdateAppearance.setLeaderId(team.getLeaderRoleId());
            final Member member = team.getMember(role.getRoleId());
            if (member != null) {
                list.add(new FiedValue("inTeam", (byte)1, member.isInTeam() ? 1 : 2));
            }
        }
        respUpdateAppearance.setLight_effect(this.equipService.getWearEffect(role));
        respUpdateAppearance.setVip(role.getVip());
        respUpdateAppearance.setFly_type(0);
        respUpdateAppearance.setPortrait(role.getIcon());
        respUpdateAppearance.setWeapon(role.getWeaponIcon());
        final Horse horse = this.horseService.getRideMount(role);
        if (horse != null) {
            respUpdateAppearance.setMount_icon(horse.getSerial());
            respUpdateAppearance.setMoveSpeed(horse.getMoveSp());
        }
        final int moveAdditionTime = role.getExtendBox().getState().getInt("shenxing", 0);
        if (moveAdditionTime > 0 && moveAdditionTime > currTime) {
            respUpdateAppearance.setMoveSpeed(30);
        }
        final List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            respUpdateAppearance.setGem(num);
        }
        final RoleService roleService = SpringUtils.getRoleService();
        respUpdateAppearance.setTitle(this.titleService.getTitle(role));
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            list.add(new FiedValue("card", (byte)3, role.getMasquerade()));
        }
        final List<FiedValue> deyList = roleService.deyList(role.getColor());
        list.addAll(deyList);
        if (list.size() > 0) {
            respUpdateAppearance.setList(list);
        }
        return respUpdateAppearance;
    }
    
    public void handleOnEquip(final Role role, final BagEquip equip) {
        if (equip.getPosition() == ADORN.WEAPON.getValue() && equip.getPos() <= 0) {
            final int num = DataGlobalFunc.getGemNum(equip);
            if (num >= 10 || num <= 0) {
                this.sendUpdateAppear(role);
            }
        }
    }
}
