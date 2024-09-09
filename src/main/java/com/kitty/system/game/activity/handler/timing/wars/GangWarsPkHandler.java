package com.kitty.system.game.activity.handler.timing.wars;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.message.RespNearby;
import com.kitty.system.game.activity.message.vo.Nearby;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.activity.message.RespGangWarsGateLife;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.message.RespOffRoleState;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.activity.message.RespGangWarsRoleState;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.party.model.Party;
import java.util.Iterator;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.model.user.wars.GangWarsPkData;
import java.util.Map;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class GangWarsPkHandler extends ActivityHandler
{
    @Autowired
    GangWarsHandler gangWarsHandler;
    @Autowired
    protected MapService mapService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    protected FightNewService fightNewService;
    @Autowired
    PartyService partyService;
    @Autowired
    RoleService roleService;
    @Autowired
    NpcService npcService;
    @Autowired
    ServerService serverService;
    private static final int MAX_ROUND;
    private static final String ATK_GATE_TIPS = "本队对敌方#<O>城门#造成了#<G>30点伤害##<E:50>#";
    private static final String EQUIPMENT_GATE_TIPS = "我方#<R>%s#的队伍正在进攻城门";
    private static final String EQUIPMENT_CANNON_TIPS = "我帮#<R>%s#正在准备发射";
    private static final String ACTIVATE_CANNON = "燃烧吧神龙大炮/activate_cannon|我只是参观的/离开#<E:17>#";
    private static final String BREAK_OFF_CANNON = "是/break_off_cannon|否/离开";
    private static final String EQUIPMENT_ATK_GATE_TIPS = "为了我帮，开打/atk_cannon|还是先干点别的/离开#<E:17>#";
    private static final String IS_EQUIPMENT_GATE_TIPS = "停止攻击/off_cannon|根本停不下来/离开#<E:17>#";
    private static final String IS_EQUIPMENT_GATE_BREAK_OFF_TIPS = "是/break_off_gate|否/离开";
    private static final String GATE_BEAR_ATK_TIPS = "%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点";
    private static final String REQUEST_FIGHT_NOT = "人数不足#<R>3#人，不能发起攻击";
    private static final String REQUEST_CANNON_FIGHT_NOT = "人数不足#<R>3#人，不能操作";
    
    public GangWarsPkHandler() {
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.GANG_WARS.getValue();
    }
    
    private boolean isMatchFightType(int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("帮派大战对决开始");
        try {
            GangWarsData objData = this.gangWarsHandler.getObjData();
            objData.setStage((byte)1);
            for (Integer key : objData.getWarsMap().keySet()) {
                GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(key);
                Party party = this.partyService.getParty(key);
                Party enemy = this.partyService.getParty(Integer.valueOf(warsInfo.getEnemy()));
                if (party != null && enemy != null) {
                    this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(370, this.gangWarsHandler.getBye(warsInfo.getGrade()), enemy.getName()));
                }
                else {
                    continue;
                }
            }
            for (Integer serial : objData.getPkData().keySet()) {
                Map<Long, GangWarsPkData> dataMap = (Map)objData.getPkData().get(serial);
                GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(serial);
                if (sideInfo.getCombatResults() != 3) {
                    continue;
                }
                Map<Long, GangWarsPkData> enemyDataMap = (Map)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
                if (enemyDataMap.size() <= 0) {
                    GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
                    sideInfo.setCombatResults((byte)1);
                    enemyInfo.setCombatResults((byte)0);
                    this.gangWarsHandler.end((int)serial, sideInfo, enemyInfo);
                }
                else {
                    for (Long id : dataMap.keySet()) {
                        GangWarsPkData warsPkData = (GangWarsPkData)dataMap.get(id);
                        if (warsPkData.getPosition() != 3) {
                            this.gangWarsHandler.sendRespIcon((long)id, (int)serial);
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("帮战PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void reqOperateEquipment(Role role, int type, String string) {
        if (this.teamService.getTeamCount(role) < 3) {
            MessagePusher.pushMessage(role, new RespMsg("人数不足#<R>3#人，不能操作"));
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getCombatResults() != 3) {
            return;
        }
        if (type == 0) {
            Equipment equipment2 = (Equipment)objData.getCannonMap().get(Integer.valueOf(warsInfo.getVisible()));
            if (equipment2 == null) {
                Npc npc2 = this.mapService.getNpc(90996);
                MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc2, "燃烧吧神龙大炮/activate_cannon|我只是参观的/离开#<E:17>#", String.format(npc2.getTalk(), Short.valueOf((short)180))));
                return;
            }
            if (equipment2.getStance() == warsInfo.getStance()) {
                MessagePusher.pushMessage(role, new RespMsg(String.format("我帮#<R>%s#正在准备发射", equipment2.getName())));
                return;
            }
            Npc npc2 = this.mapService.getNpc(90996);
            MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc2, "是/break_off_cannon|否/离开", "对方正在操作神龙大炮，要阻止吗？"));
            return;
        }
        else if (type == 1) {
            byte stance = 0;
            if (NumberUtil.isNumber(string)) {
                stance = (byte)Byte.valueOf(string);
                if (stance < 0 || stance > 1) {
                    this.logger.error("玩家：{}，攻击城门立场错误{}", role.getRoleId(), string);
                    return;
                }
                if (warsInfo.getStance() == stance) {
                    Party sideParty = this.partyService.getParty(role.getFaction());
                    Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(warsInfo.getEnemy()));
                    if (equipment != null) {
                        Npc npc = this.mapService.getNpc(90995);
                        npc.setName((int)sideParty.getLevel() + "级城门");
                        MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "是/break_off_gate|否/离开", "对方正在进攻城门，要阻止吗？"));
                    }
                    return;
                }
                else {
                    Party enemyParty = this.partyService.getParty(Integer.valueOf(warsInfo.getEnemy()));
                    Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
                    if (equipment != null) {
                        if (team.getId() == equipment.getTeam()) {
                            Npc npc = this.mapService.getNpc(90995);
                            npc.setName((int)enemyParty.getLevel() + "级城门");
                            MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "停止攻击/off_cannon|根本停不下来/离开#<E:17>#", "我队正在向对方城门发起攻击"));
                        }
                        else {
                            MessagePusher.pushMessage(role, new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", equipment.getName())));
                        }
                        return;
                    }
                    else {
                        Npc npc = this.mapService.getNpc(90995);
                        npc.setName((int)enemyParty.getLevel() + "级城门");
                        GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(warsInfo.getEnemy()));
                        MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "为了我帮，开打/atk_cannon|还是先干点别的/离开#<E:17>#", "这是敌方城门，耐久剩余#<R>" + (int)enemyInfo.getGateLife() + "#"));
                        return;
                    }
                }
            }
            else {
                return;
            }
        }
        else {
            return;
        }
    }
    
    public void operateCannon(final Role role, String type) {
        if (this.teamService.getTeamCount(role) < 3) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team.getLeaderUid() != (long)role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getCombatResults() != 3) {
            return;
        }
        Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(warsInfo.getVisible()));
        if ("activate_cannon".equals(type)) {
            if (equipment == null) {
                int time = new Long(System.currentTimeMillis() / 1000L).intValue() + 20;
                ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        GangWarsPkHandler.this.cannonTimer((int)role.getFaction());
                    }
                }, 20000L);
                RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)2, time);
                List<Long> members = this.operateMembers(team, (Map)objData.getPkData().get(role.getFaction()), respGangWarsRoleState, (byte)2);
                objData.getCannonMap().put(Integer.valueOf(warsInfo.getVisible()), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, time));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("大炮引线已被其他玩家点燃"));
            }
        }
        else if ("break_off_cannon".equals(type)) {
            if (equipment == null) {
                MessagePusher.pushMessage(role, new RespMsg("神龙大炮引线已熄灭"));
                return;
            }
            Map<Long, GangWarsPkData> enemyWarsPkDataMap = (Map)objData.getPkData().get(Integer.valueOf(warsInfo.getEnemy()));
            if (enemyWarsPkDataMap == null) {
                return;
            }
            Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map)objData.getPkData().get(role.getFaction());
            if (sideWarsPkDataMap == null) {
                return;
            }
            this.cancelTask(equipment.getScheduledFuture());
            objData.getCannonMap().remove(Integer.valueOf(warsInfo.getVisible()));
            this.abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)2, "敌方阻止，操作神龙大炮失败");
        }
        return;
    }
    
    public void cannonTimer(int faction) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        List<Long> objects = new ArrayList<>();
        GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(faction));
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            return;
        }
        Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(Integer.valueOf(faction));
        StringBuilder stringBuilder = new StringBuilder();
        Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(sideInfo.getVisible()));
        for (Long id : equipment.getList()) {
            GangWarsPkData gangWarsPkData = (GangWarsPkData)warsPkDataMap.get(id);
            if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
                objects.add(id);
                gangWarsPkData.setState((byte)0);
            }
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("、");
                }
                stringBuilder.append(player.getName());
            }
        }
        objData.getCannonMap().remove(Integer.valueOf(sideInfo.getVisible()));
        GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
        int gateLife = enemyInfo.getGateLife() - 180;
        enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
        int count = objData.getRecordMap().getInt(faction + "_cannon", 0);
        objData.getRecordMap().setv(faction + "_cannon", Integer.valueOf(count + 1));
        this.updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)2);
        Party enemyParty = this.partyService.getParty(Integer.valueOf(sideInfo.getEnemy()));
        if (enemyParty != null && stringBuilder.length() > 0) {
            this.partyService.npcPartyChat(enemyParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", stringBuilder.toString(), "我方", Short.valueOf((short)180)));
            Party sideParty = this.partyService.getParty(Integer.valueOf(faction));
            if (sideParty != null) {
                this.partyService.npcPartyChat(sideParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", stringBuilder.toString(), enemyParty.getName(), Short.valueOf((short)180)));
            }
        }
        return;
    }
    
    public void operateGate(Role role, String type) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getCombatResults() != 3) {
            return;
        }
        if ("atk_cannon".equals(type)) {
            this.atkGate(role, objData, warsInfo);
        }
        else if ("off_cannon".equals(type)) {
            this.offGate(role, objData, "队长停止了攻击敌方城门");
        }
        else if ("break_off_gate".equals(type)) {
            this.breakOffGate(role, objData, warsInfo);
        }
        return;
    }
    
    public void atkGate(Role role, GangWarsData objData, GangWarsInfo warsInfo) {
        Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(role.getFaction());
        if (warsPkDataMap == null) {
            return;
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
        if (equipment == null) {
            ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.gateTimer((int)role.getFaction()), 10000L, 10000L);
            RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)3, 0);
            List<Long> members = this.operateMembers(team, warsPkDataMap, respGangWarsRoleState, (byte)3);
            objData.getGateMap().put(role.getFaction(), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, 0));
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", equipment.getName())));
        }
        return;
    }
    
    public void offGate(Role role, GangWarsData objData, String tips) {
        Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(role.getFaction());
        if (warsPkDataMap == null) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team.getLeaderUid() != (long)role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        Equipment equipment = (Equipment)objData.getGateMap().get(role.getFaction());
        if (equipment == null) {
            return;
        }
        if (team.getId() != equipment.getTeam()) {
            return;
        }
        this.cancelTask(equipment.getScheduledFuture());
        objData.getGateMap().remove(role.getFaction());
        RespOffRoleState offRoleState = new RespOffRoleState("wars", (byte)3);
        RespNotifyMiscEx miscEx = new RespNotifyMiscEx(tips);
        ChatService chatService = SpringUtils.getChatService();
        for (Long id : equipment.getList()) {
            GangWarsPkData warsPkData = (GangWarsPkData)warsPkDataMap.get(id);
            if (warsPkData != null) {
                warsPkData.setState((byte)0);
            }
            MessagePusher.pushMessage((long)id, offRoleState);
            MessagePusher.pushMessage((long)id, miscEx);
            chatService.sendInfo(id, tips);
        }
        return;
    }
    
    public void breakOffGate(Role role, GangWarsData objData, GangWarsInfo warsInfo) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team.getLeaderUid() != (long)role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(warsInfo.getEnemy()));
        if (equipment == null) {
            MessagePusher.pushMessage(role, new RespMsg("敌方已经停止了攻击城门"));
            return;
        }
        Map<Long, GangWarsPkData> enemyWarsPkDataMap = (Map)objData.getPkData().get(Integer.valueOf(warsInfo.getEnemy()));
        if (enemyWarsPkDataMap == null) {
            return;
        }
        Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map)objData.getPkData().get(role.getFaction());
        if (sideWarsPkDataMap == null) {
            return;
        }
        this.cancelTask(equipment.getScheduledFuture());
        objData.getGateMap().remove(Integer.valueOf(warsInfo.getEnemy()));
        this.abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)3, "敌方阻止，停止攻击城门");
        return;
    }
    
    public void abortOperate(Role role, Equipment equipment, Team team, Map<Long, GangWarsPkData> sideWarsPkDataMap, Map<Long, GangWarsPkData> enemyWarsPkDataMap, byte state, String msg) {
        RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
        for (int i = 0; i < team.getList().size(); ++i) {
            Member member = (Member)team.getList().get(i);
            if (member.isInTeam()) {
                GangWarsPkData warsPkData = (GangWarsPkData)enemyWarsPkDataMap.get(member.getId());
                if (warsPkData != null) {
                    warsPkData.setState((byte)0);
                }
                MessagePusher.pushMessage((long)member.getId(), offRoleState);
            }
        }
        Team enemyTeam = this.teamService.getTeamByTeamId(equipment.getTeam());
        if (enemyTeam == null) {
            return;
        }
        Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        if (player == null) {
            return;
        }
        for (int j = 0; j < enemyTeam.getList().size(); ++j) {
            Member member2 = (Member)enemyTeam.getList().get(j);
            if (member2.isInTeam()) {
                MessagePusher.pushMessage((long)member2.getId(), new RespNotifyMiscEx(member2.getId(), msg, true));
            }
        }
        this.upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
        this.upMemberState(sideWarsPkDataMap, team, (byte)1);
        this.doStartFightPackage(role, team, player, enemyTeam);
        return;
    }
    
    public void upMemberState(Map<Long, GangWarsPkData> warsPkDataMap, Team team, byte state) {
        for (int i = 0; i < team.getList().size(); ++i) {
            Member member = (Member)team.getList().get(i);
            if (member.isInTeam()) {
                this.upMember(member.getId(), warsPkDataMap, state, 0);
            }
        }
    }
    
    public void upMember(Long id, Map<Long, GangWarsPkData> warsPkDataMap, byte state, int time) {
        GangWarsPkData side = (GangWarsPkData)warsPkDataMap.get(id);
        if (side != null) {
            side.setState(state);
            if (time > 0) {
                side.setRebirth(time);
                side.setPosition((byte)0);
            }
        }
    }
    
    public List<Long> operateMembers(Team team, Map<Long, GangWarsPkData> warsPkDataMap, RespGangWarsRoleState respGangWarsRoleState, byte state) {
        List<Long> objects = new ArrayList<>();
        for (Member member : team.getList()) {
            if (member.isInTeam()) {
                MessagePusher.pushMessage((long)member.getId(), respGangWarsRoleState);
                GangWarsPkData gangWarsPkData = (GangWarsPkData)warsPkDataMap.get(member.getId());
                if (gangWarsPkData != null) {
                    gangWarsPkData.setState(state);
                    objects.add(member.getId());
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return objects;
    }
    
    public void gateTimer(int faction) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        List<Long> objects = new ArrayList<>();
        GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(faction));
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(sideInfo.getVisible()));
            if (equipment != null) {
                this.cancelTask(equipment.getScheduledFuture());
                objData.getGateMap().remove(Integer.valueOf(sideInfo.getVisible()));
            }
            return;
        }
        else {
            Equipment equipment = (Equipment)objData.getGateMap().get(Integer.valueOf(faction));
            if (equipment != null) {
                Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(Integer.valueOf(faction));
                for (Long id : equipment.getList()) {
                    GangWarsPkData gangWarsPkData = (GangWarsPkData)warsPkDataMap.get(id);
                    if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
                        objects.add(id);
                        gangWarsPkData.setState((byte)0);
                        MessagePusher.pushMessage((long)id, new RespMsg("本队对敌方#<O>城门#造成了#<G>30点伤害##<E:50>#"));
                    }
                    else {
                        continue;
                    }
                }
                GangWarsInfo enemyInfo = (GangWarsInfo)objData.getWarsMap().get(Integer.valueOf(sideInfo.getEnemy()));
                int gateLife = enemyInfo.getGateLife() - 30;
                enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
                this.updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)3);
            }
            return;
        }
    }
    
    public void updateGateLife(int serial, GangWarsInfo sideInfo, GangWarsInfo enemyInfo, List<Long> objects, byte type) {
        if (enemyInfo.getGateLife() <= 0) {
            sideInfo.setCombatResults((byte)1);
            enemyInfo.setCombatResults((byte)0);
            this.gangWarsHandler.end(serial, sideInfo, enemyInfo);
        }
        else {
            GangWarsData objData = this.gangWarsHandler.getObjData();
            if (objData == null) {
                return;
            }
            RespGangWarsGateLife gangWarsGateLife = new RespGangWarsGateLife(sideInfo.getGateLife(), enemyInfo.getGateLife(), type, enemyInfo.getStance());
            for (Long id : objects) {
                MessagePusher.pushMessage((long)id, gangWarsGateLife);
            }
            Map<Long, GangWarsPkData> sideMemberMap = (Map)objData.getPkData().get(Integer.valueOf(serial));
            for (Long id2 : sideMemberMap.keySet()) {
                if (!objects.contains(id2)) {
                    MessagePusher.pushMessage((long)id2, gangWarsGateLife);
                }
                else {
                    continue;
                }
            }
            Map<Long, GangWarsPkData> enemyMemberMap = (Map)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
            gangWarsGateLife = new RespGangWarsGateLife(enemyInfo.getGateLife(), sideInfo.getGateLife(), (byte)2, enemyInfo.getStance());
            for (Long id3 : enemyMemberMap.keySet()) {
                if (!objects.contains(id3)) {
                    MessagePusher.pushMessage((long)id3, gangWarsGateLife);
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public void requestFight(Role role, int id) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        Role player = this.roleService.getPlayer(id);
        if (player == null || role.getFaction().equals(player.getFaction())) {
            return;
        }
        int count = this.teamService.getTeamCount(role);
        if (count < 3) {
            MessagePusher.pushMessage(role, new RespMsg("人数不足#<R>3#人，不能发起攻击"));
            return;
        }
        GangWarsInfo gangWarsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (gangWarsInfo.getCombatResults() != 3 || !player.getFaction().equals(Integer.valueOf(gangWarsInfo.getEnemy()))) {
            return;
        }
        Fight fight = this.fightNewService.getFightByRoleId((int)player.getRoleId());
        if (fight != null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("对方在战斗中"));
            return;
        }
        this.doStartFightPackage(role, this.teamService.getTeam((int)role.getRoleId()), (Map)objData.getPkData().get(role.getFaction()), (Map)objData.getPkData().get(player.getFaction()), player);
        return;
    }
    
    public void doStartFightPackage(Role role, Team team, Map<Long, GangWarsPkData> sideWarsPkDataMap, Map<Long, GangWarsPkData> enemyWarsPkDataMap, Role player) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        this.upMemberState(sideWarsPkDataMap, team, (byte)1);
        Team enemyTeam = this.teamService.getTeam((int)player.getRoleId());
        Role enemyLeader = player;
        Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(player.getFaction());
        if (enemyTeam == null) {
            if (!this.isStartFight((long)role.getId(), (GangWarsPkData)warsPkDataMap.get(player.getId()))) {
                return;
            }
            this.upMember(player.getId(), enemyWarsPkDataMap, (byte)1, 0);
        }
        else {
            int i = 0;
            while (i < enemyTeam.getList().size()) {
                Member member = (Member)enemyTeam.getList().get(i);
                if (member.isInTeam() && !this.isStartFight((long)role.getId(), (GangWarsPkData)warsPkDataMap.get(member.getId()))) {
                    return;
                }
                ++i;
            }
            this.upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
            enemyLeader = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        }
        GangWarsInfo gangWarsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        Equipment equipment = (Equipment)objData.getCannonMap().get(Integer.valueOf(gangWarsInfo.getVisible()));
        if (equipment != null && equipment.getList().contains(player.getId())) {
            this.startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)2);
        }
        equipment = (Equipment)objData.getGateMap().get(player.getFaction());
        if (equipment != null) {
            this.startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)3);
        }
        this.doStartFightPackage(role, team, enemyLeader, enemyTeam);
        return;
    }
    
    private void startFightBreakOff(Role role, Role player, Equipment equipment, GangWarsData objData, Team enemyTeam, byte state) {
        RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
        this.cancelTask(equipment.getScheduledFuture());
        GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (state == 3) {
            objData.getGateMap().remove(Integer.valueOf(warsInfo.getEnemy()));
        }
        else {
            objData.getCannonMap().remove(Integer.valueOf(warsInfo.getVisible()));
        }
        if (enemyTeam != null) {
            for (int i = 0; i < enemyTeam.getList().size(); ++i) {
                Member member = (Member)enemyTeam.getList().get(i);
                if (member.isInTeam()) {
                    MessagePusher.pushMessage((long)member.getId(), offRoleState);
                }
            }
        }
        else {
            MessagePusher.pushMessage((long)player.getId(), offRoleState);
        }
    }
    
    private boolean isStartFight(long id, GangWarsPkData enemy) {
        if (enemy != null) {
            if (enemy.getState() == 1 || enemy.getState() == 4) {
                MessagePusher.pushMessage(id, new RespMsg("对方在战斗中"));
                return false;
            }
            if (enemy.getRebirth() > 0) {
                int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
                if (enemy.getRebirth() > currTime) {
                    MessagePusher.pushMessage(id, new RespMsg("对方正在保护阶段"));
                    return false;
                }
                enemy.setRebirth(0);
            }
        }
        return true;
    }
    
    public void doStartFightPackage(Role roleA, Team teamA, Role roleB, Team teamB) {
        List<Member> listA = new ArrayList<>();
        if (teamA == null) {
            listA.add(Member.valueOf(roleA));
        }
        else {
            listA.addAll(this.fightNewService.excuteMemberLeaveTemp(roleA, teamA));
        }
        List<Member> listB = new ArrayList<>();
        if (teamB == null) {
            listB.add(Member.valueOf(roleB));
        }
        else {
            listB.addAll(this.fightNewService.excuteMemberLeaveTemp(roleB, teamB));
        }
        this.doStartFight(listA, listB);
    }
    
    public void doStartFight(List<Member> listA, List<Member> listB) {
        if (listA == null || listB == null) {
            return;
        }
        if (listA.size() < 0 || listB.size() < 0) {
            return;
        }
        int fightType = this.getFightType();
        Fight fight = FightFactory.getFightFactory(fightType).create(3001, Integer.valueOf(fightType), listA, listB);
        fight.setMaxRound(GangWarsPkHandler.MAX_ROUND);
        fight.start();
        return;
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            if (!role.getPos().getMapId().equals(Integer.valueOf(3001))) {
                return;
            }
            GangWarsData objData = this.gangWarsHandler.getObjData();
            if (objData == null) {
                return;
            }
            GangWarsInfo warsInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
            if (warsInfo == null || warsInfo.getCombatResults() != 3) {
                return;
            }
            Team team = this.teamService.getTeam((int)role.getRoleId());
            Map<Long, GangWarsPkData> sideWarsPkDataMap = (Map)objData.getPkData().get(role.getFaction());
            if (sideWarsPkDataMap == null) {
                return;
            }
            int time = 0;
            RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)0, time);
            if (fightEndEvent.getStatus() == 2) {
                time = new Long(System.currentTimeMillis() / 1000L).intValue() + 90;
                respGangWarsRoleState.setTime(time);
            }
            FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), (long)role.getId());
            for (Long id : fightInfo.getItself()) {
                this.upMember(id, sideWarsPkDataMap, (byte)0, time);
                if (time > 0) {
                    MessagePusher.pushMessage((long)id, respGangWarsRoleState);
                }
                else {
                    continue;
                }
            }
            this.doFightEnd(role, team, fightEndEvent.getStatus(), objData, warsInfo.getStance());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    public void doFightEnd(Role role, Team team, byte status, GangWarsData objData, byte stance) {
        if (status == 2) {
            int x = 24;
            int y = 19;
            if (stance != 1) {
                x = 195;
                y = 109;
            }
            if (team != null) {
                this.teamService.memberHandleThreadLocal(team, memberRole -> memberRole.pushTempCache("not_check_change_map", Boolean.valueOf(true)));
            }
            else {
                role.pushTempCache("not_check_change_map", Boolean.valueOf(true));
            }
            SpringUtils.getMapService().changeMap(role, new ReqTelePort(3001, x, y), null);
        }
        if (status == 1) {
            int anInt = objData.getRecordMap().getInt(role.getFaction() + "_win", 0);
            objData.getRecordMap().setv(role.getFaction() + "_win", Integer.valueOf(anInt + 1));
        }
    }
    
    public void getNearby(Role role) {
        GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        GangWarsInfo sideInfo = (GangWarsInfo)objData.getWarsMap().get(role.getFaction());
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            return;
        }
        Map<Long, GangWarsPkData> warsPkDataMap = (Map)objData.getPkData().get(Integer.valueOf(sideInfo.getEnemy()));
        if (warsPkDataMap == null) {
            return;
        }
        List<Nearby> objects = new ArrayList(warsPkDataMap.size());
        for (Long id : warsPkDataMap.keySet()) {
            GangWarsPkData data = (GangWarsPkData)warsPkDataMap.get(id);
            if (data.getPosition() != 1) {
                continue;
            }
            Role player = this.roleService.getPlayer((long)id);
            if (player == null) {
                continue;
            }
            if (this.teamService.isInTeam(player) && !this.teamService.isTeamLeader(player)) {
                continue;
            }
            if (DataGlobalFunc.visual((int)role.getPos().getX(), (int)role.getPos().getY(), (int)player.getPos().getX(), (int)player.getPos().getY(), 50.0)) {
                objects.add(new Nearby(player));
            }
            else {
                continue;
            }
        }
        if (objects.size() > 0) {
            MessagePusher.pushMessage(role, new RespNearby(objects));
        }
        return;
    }
    
    public void cancelTask(ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel((boolean)Boolean.FALSE);
        }
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_GANG_WARS.getValue();
    }
}
