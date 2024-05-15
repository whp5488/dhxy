
package com.kitty.system.game.pk.service;

import com.kitty.system.game.pk.message.RespReleasePrisonerSuccess;
import com.kitty.system.game.pk.message.RespReleaseInfo;
import com.kitty.system.game.pk.message.vo.PrisonerInfo;
import com.kitty.system.game.pk.message.RespPrisonerInfo;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.pk.message.RespTraceRole;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.pk.message.RespSearchRole;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.system.common.message.RespMsg;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.vo.Npc;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.Map;
import com.kitty.system.game.player.bean.PkRecord;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.equip.service.EquipService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Service;
import com.kitty.system.game.activity.service.ActivityHandler;

@Service
public class PkService extends ActivityHandler
{
    @Autowired
    RoleService roleService;
    @Autowired
    EquipService equipService;
    @Autowired
    TeamService teamService;
    @Autowired
    MapService mapService;
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        this.doFightEnd(fightEndEvent.getRole(), fightEndEvent);
    }
    
    @Override
    public void handleHeartBeat(final Role role, final long prevHeartTime) {
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        final PkInfoBox box = role.getPkInfoBox();
        if (box.getIncense() > 0 && currTime >= box.getIncense()) {
            box.setIncense(0);
            this.roleService.stateRefresh(role);
            role.save();
        }
        if (role.getPkInfoBox().isRedName()) {
            boolean flag = false;
            if (box.getConceal() > 0 && currTime >= box.getConceal()) {
                flag = true;
                box.setConceal(0);
            }
            if (box.getRedTime() > 0 && currTime >= box.getRedTime()) {
                flag = true;
                box.setRedTime(0);
            }
            if (box.getPrisonTime() > 0 && currTime >= box.getPrisonTime()) {
                flag = true;
                this.removeFromPrison(role, 1011, 124, 67);
            }
            if (flag) {
                this.roleService.stateRefresh(role);
                role.save();
            }
        }
    }
    
    public void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        if (!this.isMatchFightType(role, fightEndEvent.getFightType())) {
            return;
        }
        switch (fightEndEvent.getStatus()) {
            case 1: {
                this.doFightWin(role, fightEndEvent);
                break;
            }
            case 2: {
                this.doFightFail(role, fightEndEvent);
                break;
            }
            case 3: {
                this.doFightRunAway(role, fightEndEvent);
                this.doFightFail(role, fightEndEvent);
                break;
            }
            case 40: {
                this.doFightMaxRound(role, fightEndEvent);
                break;
            }
        }
    }
    
    private boolean isMatchFightType(final Role role, final int fightType) {
        return FIGHT_TYPE.FORCE_PK.getValue() == fightType;
    }
    
    public void doFightWin(final Role role, final FightEndEvent fightEndEvent) {
        if (this.isASide(role, fightEndEvent.getRolesA()) && !this.isAllRedName(fightEndEvent.getRolesB())) {
            final PkInfoBox box = role.getPkInfoBox();
            final SysMapInfo mapInfo = this.mapService.getMap(role.getPos().getMapId());
            if (role.getLastDead() > 0 && box.getConceal() <= 0 && !box.isRedName() && mapInfo.getIsWanted() != 1) {
                box.setConceal((int)((System.currentTimeMillis() + 60000L) / 1000L));
                MessagePusher.notify2Player(role, 1107);
                role.save();
            }
            box.setRedName(true);
            box.setRedTime((int)((System.currentTimeMillis() + 28800000L) / 1000L));
            this.roleService.stateRefresh(role);
            SpringUtils.getBroadcastService().sendTitleInfo(role);
            this.checkAfterWin(role);
        }
        if (role.getLastDead() == 0) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null && !team.isLeader(role.getRoleId())) {
                this.teamService.leaveTemp(role, true);
            }
            role.setVisible(1201);
            this.roleService.goBackBorn(role, new Pos(107, 64, 5, 1002));
            final long exp = BigDecimalUtil.decimalScaleDown(0, FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive()) * 0.2).longValue();
            this.teamService.memberHandleThreadLocal(role, memberRole -> this.roleService.punishFightDead(memberRole, -exp, "由于你的死亡，你丢失了"));
        }
    }
    
    public boolean isASide(final Role role, final List<Role> rolesA) {
        if (rolesA != null) {
            for (final Role roleA : rolesA) {
                if (role.getId().equals(roleA.getId())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isAllRedName(final List<Role> roles) {
        if (roles != null) {
            for (final Role role : roles) {
                if (!role.getPkInfoBox().isRedName()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        this.roleService.goBackBorn(role, new Pos(107, 64, 5, 1002));
        final long exp = BigDecimalUtil.decimalScaleDown(0, FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive()) * 0.2).longValue();
        this.roleService.punishFightDead(role, -exp, "由于你的死亡，你丢失了");
        if (!this.teamService.isInTeam(role) || this.teamService.isTeamLeader(role)) {
            Role killRole = null;
            if (this.isASide(role, fightEndEvent.getRolesA())) {
                killRole = this.getOperRole(fightEndEvent.getRolesB());
            }
            else {
                killRole = this.getOperRole(fightEndEvent.getRolesA());
            }
            if (killRole != null) {
                MessagePusher.notify2Player(role, 1105, killRole.getName());
                this.addKillRole(killRole, role);
                this.addBeKillRole(role, killRole);
            }
        }
    }
    
    private void addKillRole(final Role role, final Role targetRole) {
        if (role.getPkInfoBox().getKillRoles() == null) {
            role.getPkInfoBox().setKillRoles(new HashMap<Long, PkRecord>());
        }
        final int now = (int)(System.currentTimeMillis() / 1000L);
        final PkRecord pkRecord = new PkRecord(targetRole.getId(), now);
        role.getPkInfoBox().getKillRoles().put(pkRecord.getId(), pkRecord);
    }
    
    private void addBeKillRole(final Role role, final Role targetRole) {
        if (role.getPkInfoBox().getBeKillRoles() == null) {
            role.getPkInfoBox().setBeKillRoles(new HashMap<Long, PkRecord>());
        }
        final int now = (int)(System.currentTimeMillis() / 1000L);
        final PkRecord pkRecord = new PkRecord(targetRole.getId(), now);
        role.getPkInfoBox().getBeKillRoles().put(pkRecord.getId(), pkRecord);
    }
    
    private Role getOperRole(final List<Role> roles) {
        if (roles != null && roles.size() > 0) {
            if (roles.size() == 1) {
                return roles.get(0);
            }
            for (final Role role : roles) {
                if (this.teamService.isTeamLeader(role)) {
                    return role;
                }
            }
        }
        return null;
    }
    
    public void doFightRunAway(final Role role, final FightEndEvent fightEndEvent) {
    }
    
    public void doFightMaxRound(final Role role, final FightEndEvent fightEndEvent) {
        if (this.isASide(role, fightEndEvent.getRolesA())) {
            final long exp = BigDecimalUtil.decimalScaleDown(0, FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive()) * 0.2).longValue();
            this.roleService.punishFightDead(role, -exp, "由于你的死亡，你丢失了");
        }
    }
    
    public boolean isInPrisonTime(final Role role) {
        return role.getPkInfoBox().getPrisonTime() > 0;
    }
    
    public void checkEnterMap(final Role role) {
        this.teamService.memberHandle(role, memberRole -> {
            if (this.isNeedInPrison(memberRole)) {
                this.add2Prison(memberRole);
            }
        });
    }
    
    public void checkAfterWin(final Role role) {
        this.teamService.memberHandle(role, memberRole -> {
            if (this.isNeedInPrison(memberRole)) {
                this.add2Prison(memberRole);
            }
        });
    }
    
    private boolean isNeedInPrison(final Role role) {
        if (role.getVisible() == 1201) {
            role.setVisible(1);
            return false;
        }
        if (DataGlobalFunc.isInMap(role, 1201)) {
            return false;
        }
        if (role.getPkInfoBox().isRedName()) {
            final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            final SysMapInfo mapInfo = this.mapService.getMap(role.getPos().getMapId());
            if (mapInfo.getIsWanted() == 1 && role.getPkInfoBox().getRedTime() > currTime) {
                return true;
            }
        }
        return false;
    }
    
    public void add2Prison(final Role role) {
        if (this.teamService.isInTeam(role)) {
            this.teamService.leave(role, false);
        }
        final PkInfoBox pkInfoBox = role.getPkInfoBox();
        int x = 62;
        int y = 5;
        int time = 60;
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1201);
        final int size = pkInfoBox.getKillRoles().size();
        if (size > 3 && size < 6) {
            x = 27;
            y = 34;
            time = 90;
            final int round = RandomUtil.round(1, 10);
            if (round < 5) {
                x = 10;
                y = 43;
            }
        }
        else if (size > 6) {
            x = 63;
            y = 57;
            time = 120;
        }
        pkInfoBox.setConceal(0);
        pkInfoBox.setRedTime(0);
        reqTelePort.setX(x);
        reqTelePort.setY(y);
        this.mapService.changeMap(role, reqTelePort, null);
        pkInfoBox.setPrisonTime((int)((System.currentTimeMillis() + time * 60000L) / 1000L));
        this.roleService.stateRefresh(role);
        role.save();
    }
    
    public void removeFromPrison(final Role role, final int map, final int x, final int y) {
        role.getPkInfoBox().setRedName(false);
        role.getPkInfoBox().setRedTime(0);
        role.getPkInfoBox().setPrisonTime(0);
        this.mapService.changeMap(role, new ReqTelePort(map, x, y), "runs_prison");
    }
    
    public void loginCheckFromPrison(final Role role) {
        final Pos pos = role.getPos();
        if (pos.getMapId().equals(1201)) {
            final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
            final PkInfoBox box = role.getPkInfoBox();
            if (box.getPrisonTime() < currTime) {
                box.setRedName(false);
                box.setRedTime(0);
                role.getPkInfoBox().setPrisonTime(0);
                pos.setX(44);
                pos.setY(19);
            }
        }
    }
    
    public void npcButton(final Role role, final String msg, final Npc npc) {
        if (SpringUtils.getBean(PkService.class).isInPrisonTime(role)) {
            MessagePusher.notify2Player(role, 1113);
            return;
        }
        final ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1011);
        reqTelePort.setX(124);
        reqTelePort.setY(67);
        SpringUtils.getMapService().changeMap(role, reqTelePort, msg);
    }
    
    public void searchRole(final IoSession session, final String name) {
        if (name.length() <= 0) {
            MessagePusher.pushMessage(session, new RespMsg("你要找的人目前不在大话的世界中#<E:17>#"));
            return;
        }
        final List<Role> collect = DataCache.ONLINE_ROLES.values().stream().filter(e -> e.getName().contains(name) || e.getRoleId().toString().equals(name)).collect(Collectors.toList());
        if (collect.size() <= 0) {
            MessagePusher.pushMessage(session, new RespMsg("你要找的人目前不在大话的世界中#<E:17>#"));
            return;
        }
        final List<SearchFriend> objects = new ArrayList<SearchFriend>();
        for (final Role role : collect) {
            objects.add(new SearchFriend(role.getRoleId(), role.getName(), role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue()));
        }
        final RespSearchRole searchRole = new RespSearchRole();
        searchRole.setList(objects);
        MessagePusher.pushMessage(session, searchRole);
    }
    
    public void traceRole(final IoSession session, final int roleId) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (!this.roleService.isOnline(roleId)) {
            MessagePusher.pushMessage(session, new RespMsg("你要找的人目前刚刚离开了大话的世界#<E:17>#"));
            return;
        }
        final Role player = this.roleService.getOnlinePlayer(roleId);
        if (player == null) {
            MessagePusher.pushMessage(session, new RespMsg("你要找的人目前刚刚离开了大话的世界#<E:17>#"));
            return;
        }
        final Pos pos = player.getPos();
        if (pos.getMapId().equals(3002) || pos.getMapId().equals(3002) || pos.getMapId().equals(3002)) {
            MessagePusher.pushMessage(session, new RespMsg(player.getName() + "在帮派，该场景不可被追踪"));
            return;
        }
        if (pos.getMapId().equals(3003)) {
            MessagePusher.pushMessage(session, new RespMsg(player.getName() + "在水陆大会，该场景不可被追踪"));
            return;
        }
        if (pos.getMapId().equals(4154)) {
            MessagePusher.pushMessage(session, new RespMsg(player.getName() + "在英雄争霸赛，该场景不可被追踪"));
            return;
        }
        if (pos.getMapId().equals(3001)) {
            MessagePusher.pushMessage(session, new RespMsg(player.getName() + "在帮派大战，该场景不可被追踪"));
            return;
        }
        if (pos.getMapId().equals(1201)) {
            MessagePusher.pushMessage(session, new RespMsg(player.getName() + "在天牢，该场景不可被追踪"));
            return;
        }
        final boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000L, "千里追踪");
        if (!currency) {
            return;
        }
        role.pushTempCache("trace", pos.getMapId() + "-" + pos.getX() + "-" + pos.getY());
        final RespTraceRole traceRole = new RespTraceRole();
        traceRole.setRoleId(player.getRoleId());
        traceRole.setSerial(player.getSerial());
        traceRole.setName(player.getName());
        traceRole.setRelive(player.getRelive().byteValue());
        traceRole.setLevel(player.getLevel().shortValue());
        traceRole.setWeapon(player.getWeaponIcon());
        traceRole.setLightEffect(this.equipService.getWearEffect(role));
        final List<WearEquip> wearEquips = this.equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            traceRole.setGem(num);
        }
        traceRole.setList(this.roleService.deyList(role.getColor()));
        traceRole.setMap(pos.getMapId().shortValue());
        traceRole.setMap(pos.getX().shortValue());
        traceRole.setMap(pos.getY().shortValue());
        MessagePusher.pushMessage(session, traceRole);
    }
    
    public void traceGo(final IoSession session) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Object trace = role.getTempCache("trace", null);
        if (trace == null) {
            return;
        }
        final String[] split = trace.toString().split("-");
        if (split.length < 3) {
            return;
        }
        final ReqTelePort reqTelePort = new ReqTelePort(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(split[2]));
        this.mapService.changeMap(role, reqTelePort, null);
    }
    
    public void viewPrisoner(final Role role, final Npc npc) {
        final RespPrisonerInfo respPrisonerInfo = new RespPrisonerInfo();
        final List<Role> prisonRoles = this.getPrisonRoles();
        for (final Role prisonRole : prisonRoles) {
            respPrisonerInfo.getList().add(new PrisonerInfo(prisonRole));
        }
        MessagePusher.pushMessage(role.getId(), respPrisonerInfo);
    }
    
    public List<Role> getPrisonRoles() {
        final List<Role> prisonRoles = new ArrayList<Role>();
        DataCache.ONLINE_ROLES.values().forEach(role -> {
            if (role.getPkInfoBox().getPrisonTime() > 0) {
                prisonRoles.add(role);
            }
            return;
        });
        return prisonRoles;
    }
    
    public void releaseInfo(final Role role, final int id) {
        final Role targetRole = this.roleService.getOnlinePlayer(id);
        if (targetRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("该玩家不在线，无法保释"));
            return;
        }
        if (!this.isInPrisonTime(targetRole)) {
            MessagePusher.notify2Player(role, 1119, targetRole.getName());
            return;
        }
        final RespReleaseInfo releaseInfo = new RespReleaseInfo();
        releaseInfo.setRoleId(targetRole.getRoleId());
        releaseInfo.setSerial(targetRole.getSerial());
        releaseInfo.setName(targetRole.getName());
        releaseInfo.setRelive(targetRole.getRelive().byteValue());
        releaseInfo.setLevel(targetRole.getLevel().shortValue());
        releaseInfo.setWeapon(targetRole.getWeaponIcon());
        releaseInfo.setLightEffect(this.equipService.getWearEffect(targetRole));
        final List<WearEquip> wearEquips = this.equipService.getEquipValid(targetRole, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            releaseInfo.setGem(num);
        }
        releaseInfo.setList(this.roleService.deyList(targetRole.getColor()));
        releaseInfo.setBail(String.valueOf(10000L));
        MessagePusher.pushMessage(role, releaseInfo);
    }
    
    public void confirmReleasePrisoner(final Role role, final int id) {
        final Role targetRole = this.roleService.getOnlinePlayer(id);
        if (targetRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("该玩家不在线，无法保释"));
            return;
        }
        if (!this.isInPrisonTime(targetRole)) {
            MessagePusher.notify2Player(role, 1119, targetRole.getName());
            return;
        }
        if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 10000L, "监狱保释")) {
            return;
        }
        this.removeFromPrison(targetRole, 1201, 47, 24);
        this.roleService.stateRefresh(targetRole);
        targetRole.save();
        MessagePusher.pushMessage(role, new RespReleasePrisonerSuccess(targetRole.getRoleId()));
    }
}
