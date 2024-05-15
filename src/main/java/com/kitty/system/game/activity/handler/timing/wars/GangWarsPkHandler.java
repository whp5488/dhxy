
package com.kitty.system.game.activity.handler.timing.wars;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.message.RespNearby;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.activity.message.vo.Nearby;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.factory.FightFactory;
import java.util.Collection;
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
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
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
    
    protected int getFightType() {
        return FIGHT_TYPE.GANG_WARS.getValue();
    }
    
    private boolean isMatchFightType(final int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void start() {
        this.logger.error("帮派大战对决开始");
        try {
            final GangWarsData objData = this.gangWarsHandler.getObjData();
            objData.setStage((byte)1);
            for (final Integer key : objData.getWarsMap().keySet()) {
                final GangWarsInfo warsInfo = objData.getWarsMap().get(key);
                final Party party = this.partyService.getParty(key);
                final Party enemy = this.partyService.getParty(warsInfo.getEnemy());
                if (party != null && enemy != null) {
                    this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(370, this.gangWarsHandler.getBye(warsInfo.getGrade()), enemy.getName()));
                }
            }
            for (final Integer serial : objData.getPkData().keySet()) {
                final Map<Long, GangWarsPkData> dataMap = objData.getPkData().get(serial);
                final GangWarsInfo sideInfo = objData.getWarsMap().get(serial);
                if (sideInfo.getCombatResults() != 3) {
                    continue;
                }
                final Map<Long, GangWarsPkData> enemyDataMap = objData.getPkData().get(sideInfo.getEnemy());
                if (enemyDataMap.size() <= 0) {
                    final GangWarsInfo enemyInfo = objData.getWarsMap().get(sideInfo.getEnemy());
                    sideInfo.setCombatResults((byte)1);
                    enemyInfo.setCombatResults((byte)0);
                    this.gangWarsHandler.end(serial, sideInfo, enemyInfo);
                }
                else {
                    for (final Long id : dataMap.keySet()) {
                        final GangWarsPkData warsPkData = dataMap.get(id);
                        if (warsPkData.getPosition() != 3) {
                            this.gangWarsHandler.sendRespIcon(id, serial);
                        }
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("帮战PK阶段开始错误：{}", e.getMessage());
        }
    }
    
    public void reqOperateEquipment(final Role role, final int type, final String string) {
        if (this.teamService.getTeamCount(role) < 3) {
            MessagePusher.pushMessage(role, new RespMsg("人数不足#<R>3#人，不能操作"));
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getCombatResults() != 3) {
            return;
        }
        if (type == 0) {
            final Equipment equipment = objData.getCannonMap().get(warsInfo.getVisible());
            if (equipment == null) {
                final Npc npc = this.mapService.getNpc(90996);
                MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "燃烧吧神龙大炮/activate_cannon|我只是参观的/离开#<E:17>#", String.format(npc.getTalk(), 180)));
                return;
            }
            if (equipment.getStance() == warsInfo.getStance()) {
                MessagePusher.pushMessage(role, new RespMsg(String.format("我帮#<R>%s#正在准备发射", equipment.getName())));
                return;
            }
            final Npc npc = this.mapService.getNpc(90996);
            MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "是/break_off_cannon|否/离开", "对方正在操作神龙大炮，要阻止吗？"));
        }
        else {
            if (type != 1) {
                return;
            }
            byte stance = 0;
            if (!NumberUtil.isNumber(string)) {
                return;
            }
            stance = Byte.valueOf(string);
            if (stance < 0 || stance > 1) {
                this.logger.error("玩家：{}，攻击城门立场错误{}", role.getRoleId(), string);
                return;
            }
            if (warsInfo.getStance() == stance) {
                final Party sideParty = this.partyService.getParty(role.getFaction());
                final Equipment equipment2 = objData.getGateMap().get(warsInfo.getEnemy());
                if (equipment2 != null) {
                    final Npc npc2 = this.mapService.getNpc(90995);
                    npc2.setName(sideParty.getLevel() + "级城门");
                    MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc2, "是/break_off_gate|否/离开", "对方正在进攻城门，要阻止吗？"));
                }
                return;
            }
            final Party enemyParty = this.partyService.getParty(warsInfo.getEnemy());
            final Equipment equipment2 = objData.getGateMap().get(role.getFaction());
            if (equipment2 != null) {
                if (team.getId() == equipment2.getTeam()) {
                    final Npc npc2 = this.mapService.getNpc(90995);
                    npc2.setName(enemyParty.getLevel() + "级城门");
                    MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc2, "停止攻击/off_cannon|根本停不下来/离开#<E:17>#", "我队正在向对方城门发起攻击"));
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", equipment2.getName())));
                }
                return;
            }
            final Npc npc2 = this.mapService.getNpc(90995);
            npc2.setName(enemyParty.getLevel() + "级城门");
            final GangWarsInfo enemyInfo = objData.getWarsMap().get(warsInfo.getEnemy());
            MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc2, "为了我帮，开打/atk_cannon|还是先干点别的/离开#<E:17>#", "这是敌方城门，耐久剩余#<R>" + enemyInfo.getGateLife() + "#"));
        }
    }
    
    public void operateCannon(final Role role, final String type) {
        if (this.teamService.getTeamCount(role) < 3) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team.getLeaderUid() != role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getCombatResults() != 3) {
            return;
        }
        final Equipment equipment = objData.getCannonMap().get(warsInfo.getVisible());
        if ("activate_cannon".equals(type)) {
            if (equipment == null) {
                final int time = new Long(System.currentTimeMillis() / 1000L).intValue() + 20;
                final ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        GangWarsPkHandler.this.cannonTimer(role.getFaction());
                    }
                }, 20000L);
                final RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)2, time);
                final List<Long> members = this.operateMembers(team, objData.getPkData().get(role.getFaction()), respGangWarsRoleState, (byte)2);
                objData.getCannonMap().put(warsInfo.getVisible(), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, time));
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
            final Map<Long, GangWarsPkData> enemyWarsPkDataMap = objData.getPkData().get(warsInfo.getEnemy());
            if (enemyWarsPkDataMap == null) {
                return;
            }
            final Map<Long, GangWarsPkData> sideWarsPkDataMap = objData.getPkData().get(role.getFaction());
            if (sideWarsPkDataMap == null) {
                return;
            }
            this.cancelTask(equipment.getScheduledFuture());
            objData.getCannonMap().remove(warsInfo.getVisible());
            this.abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)2, "敌方阻止，操作神龙大炮失败");
        }
    }
    
    public void cannonTimer(final int faction) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final List<Long> objects = new ArrayList<Long>();
        final GangWarsInfo sideInfo = objData.getWarsMap().get(faction);
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            return;
        }
        final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(faction);
        final StringBuilder stringBuilder = new StringBuilder();
        final Equipment equipment = objData.getCannonMap().get(sideInfo.getVisible());
        for (final Long id : equipment.getList()) {
            final GangWarsPkData gangWarsPkData = warsPkDataMap.get(id);
            if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
                objects.add(id);
                gangWarsPkData.setState((byte)0);
            }
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("、");
                }
                stringBuilder.append(player.getName());
            }
        }
        objData.getCannonMap().remove(sideInfo.getVisible());
        final GangWarsInfo enemyInfo = objData.getWarsMap().get(sideInfo.getEnemy());
        final int gateLife = enemyInfo.getGateLife() - 180;
        enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
        final int count = objData.getRecordMap().getInt(faction + "_cannon", 0);
        objData.getRecordMap().setv(faction + "_cannon", count + 1);
        this.updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)2);
        final Party enemyParty = this.partyService.getParty(sideInfo.getEnemy());
        if (enemyParty != null && stringBuilder.length() > 0) {
            this.partyService.npcPartyChat(enemyParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", stringBuilder.toString(), "我方", 180));
            final Party sideParty = this.partyService.getParty(faction);
            if (sideParty != null) {
                this.partyService.npcPartyChat(sideParty, String.format("%s#<EP>启动龙神大炮击中#%s#<EP>总部，总部耐久度降低#%d点", stringBuilder.toString(), enemyParty.getName(), 180));
            }
        }
    }
    
    public void operateGate(final Role role, final String type) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
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
    }
    
    public void atkGate(final Role role, final GangWarsData objData, final GangWarsInfo warsInfo) {
        final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(role.getFaction());
        if (warsPkDataMap == null) {
            return;
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Equipment equipment = objData.getGateMap().get(role.getFaction());
        if (equipment == null) {
            final ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.gateTimer(role.getFaction()), 10000L, 10000L);
            final RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)3, 0);
            final List<Long> members = this.operateMembers(team, warsPkDataMap, respGangWarsRoleState, (byte)3);
            objData.getGateMap().put(role.getFaction(), new Equipment(schedule, warsInfo.getStance(), team.getId(), role.getName(), members, 0));
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg(String.format("我方#<R>%s#的队伍正在进攻城门", equipment.getName())));
        }
    }
    
    public void offGate(final Role role, final GangWarsData objData, final String tips) {
        final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(role.getFaction());
        if (warsPkDataMap == null) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team.getLeaderUid() != role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        final Equipment equipment = objData.getGateMap().get(role.getFaction());
        if (equipment == null) {
            return;
        }
        if (team.getId() != equipment.getTeam()) {
            return;
        }
        this.cancelTask(equipment.getScheduledFuture());
        objData.getGateMap().remove(role.getFaction());
        final RespOffRoleState offRoleState = new RespOffRoleState("wars", (byte)3);
        final RespNotifyMiscEx miscEx = new RespNotifyMiscEx(tips);
        final ChatService chatService = SpringUtils.getChatService();
        for (final Long id : equipment.getList()) {
            final GangWarsPkData warsPkData = warsPkDataMap.get(id);
            if (warsPkData != null) {
                warsPkData.setState((byte)0);
            }
            MessagePusher.pushMessage(id, offRoleState);
            MessagePusher.pushMessage(id, miscEx);
            chatService.sendInfo(id, tips);
        }
    }
    
    public void breakOffGate(final Role role, final GangWarsData objData, final GangWarsInfo warsInfo) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team.getLeaderUid() != role.getId()) {
            MessagePusher.pushMessage(role, new RespMsg("老老实实听从队长指挥吧"));
            return;
        }
        final Equipment equipment = objData.getGateMap().get(warsInfo.getEnemy());
        if (equipment == null) {
            MessagePusher.pushMessage(role, new RespMsg("敌方已经停止了攻击城门"));
            return;
        }
        final Map<Long, GangWarsPkData> enemyWarsPkDataMap = objData.getPkData().get(warsInfo.getEnemy());
        if (enemyWarsPkDataMap == null) {
            return;
        }
        final Map<Long, GangWarsPkData> sideWarsPkDataMap = objData.getPkData().get(role.getFaction());
        if (sideWarsPkDataMap == null) {
            return;
        }
        this.cancelTask(equipment.getScheduledFuture());
        objData.getGateMap().remove(warsInfo.getEnemy());
        this.abortOperate(role, equipment, team, sideWarsPkDataMap, enemyWarsPkDataMap, (byte)3, "敌方阻止，停止攻击城门");
    }
    
    public void abortOperate(final Role role, final Equipment equipment, final Team team, final Map<Long, GangWarsPkData> sideWarsPkDataMap, final Map<Long, GangWarsPkData> enemyWarsPkDataMap, final byte state, final String msg) {
        final RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member.isInTeam()) {
                final GangWarsPkData warsPkData = enemyWarsPkDataMap.get(member.getId());
                if (warsPkData != null) {
                    warsPkData.setState((byte)0);
                }
                MessagePusher.pushMessage(member.getId(), offRoleState);
            }
        }
        final Team enemyTeam = this.teamService.getTeamByTeamId(equipment.getTeam());
        if (enemyTeam == null) {
            return;
        }
        final Role player = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        if (player == null) {
            return;
        }
        for (int j = 0; j < enemyTeam.getList().size(); ++j) {
            final Member member2 = enemyTeam.getList().get(j);
            if (member2.isInTeam()) {
                MessagePusher.pushMessage(member2.getId(), new RespNotifyMiscEx(member2.getId(), msg, true));
            }
        }
        this.upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
        this.upMemberState(sideWarsPkDataMap, team, (byte)1);
        this.doStartFightPackage(role, team, player, enemyTeam);
    }
    
    public void upMemberState(final Map<Long, GangWarsPkData> warsPkDataMap, final Team team, final byte state) {
        for (int i = 0; i < team.getList().size(); ++i) {
            final Member member = team.getList().get(i);
            if (member.isInTeam()) {
                this.upMember(member.getId(), warsPkDataMap, state, 0);
            }
        }
    }
    
    public void upMember(final Long id, final Map<Long, GangWarsPkData> warsPkDataMap, final byte state, final int time) {
        final GangWarsPkData side = warsPkDataMap.get(id);
        if (side != null) {
            side.setState(state);
            if (time > 0) {
                side.setRebirth(time);
                side.setPosition((byte)0);
            }
        }
    }
    
    public List<Long> operateMembers(final Team team, final Map<Long, GangWarsPkData> warsPkDataMap, final RespGangWarsRoleState respGangWarsRoleState, final byte state) {
        final List<Long> objects = new ArrayList<Long>();
        for (final Member member : team.getList()) {
            if (member.isInTeam()) {
                MessagePusher.pushMessage(member.getId(), respGangWarsRoleState);
                final GangWarsPkData gangWarsPkData = warsPkDataMap.get(member.getId());
                if (gangWarsPkData == null) {
                    continue;
                }
                gangWarsPkData.setState(state);
                objects.add(member.getId());
            }
        }
        return objects;
    }
    
    public void gateTimer(final int faction) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final List<Long> objects = new ArrayList<Long>();
        final GangWarsInfo sideInfo = objData.getWarsMap().get(faction);
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            final Equipment equipment = objData.getGateMap().get(sideInfo.getVisible());
            if (equipment != null) {
                this.cancelTask(equipment.getScheduledFuture());
                objData.getGateMap().remove(sideInfo.getVisible());
            }
            return;
        }
        final Equipment equipment = objData.getGateMap().get(faction);
        if (equipment != null) {
            final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(faction);
            for (final Long id : equipment.getList()) {
                final GangWarsPkData gangWarsPkData = warsPkDataMap.get(id);
                if (gangWarsPkData != null && gangWarsPkData.getPosition() != 3) {
                    objects.add(id);
                    gangWarsPkData.setState((byte)0);
                    MessagePusher.pushMessage(id, new RespMsg("本队对敌方#<O>城门#造成了#<G>30点伤害##<E:50>#"));
                }
            }
            final GangWarsInfo enemyInfo = objData.getWarsMap().get(sideInfo.getEnemy());
            final int gateLife = enemyInfo.getGateLife() - 30;
            enemyInfo.setGateLife((short)((gateLife < 0) ? 0 : gateLife));
            this.updateGateLife(faction, sideInfo, enemyInfo, objects, (byte)3);
        }
    }
    
    public void updateGateLife(final int serial, final GangWarsInfo sideInfo, final GangWarsInfo enemyInfo, final List<Long> objects, final byte type) {
        if (enemyInfo.getGateLife() <= 0) {
            sideInfo.setCombatResults((byte)1);
            enemyInfo.setCombatResults((byte)0);
            this.gangWarsHandler.end(serial, sideInfo, enemyInfo);
        }
        else {
            final GangWarsData objData = this.gangWarsHandler.getObjData();
            if (objData == null) {
                return;
            }
            RespGangWarsGateLife gangWarsGateLife = new RespGangWarsGateLife(sideInfo.getGateLife(), enemyInfo.getGateLife(), type, enemyInfo.getStance());
            for (final Long id : objects) {
                MessagePusher.pushMessage(id, gangWarsGateLife);
            }
            final Map<Long, GangWarsPkData> sideMemberMap = objData.getPkData().get(serial);
            for (final Long id2 : sideMemberMap.keySet()) {
                if (!objects.contains(id2)) {
                    MessagePusher.pushMessage(id2, gangWarsGateLife);
                }
            }
            final Map<Long, GangWarsPkData> enemyMemberMap = objData.getPkData().get(sideInfo.getEnemy());
            gangWarsGateLife = new RespGangWarsGateLife(enemyInfo.getGateLife(), sideInfo.getGateLife(), (byte)2, enemyInfo.getStance());
            for (final Long id3 : enemyMemberMap.keySet()) {
                if (!objects.contains(id3)) {
                    MessagePusher.pushMessage(id3, gangWarsGateLife);
                }
            }
        }
    }
    
    public void requestFight(final Role role, final int id) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final Role player = this.roleService.getPlayer(id);
        if (player == null || role.getFaction().equals(player.getFaction())) {
            return;
        }
        final int count = this.teamService.getTeamCount(role);
        if (count < 3) {
            MessagePusher.pushMessage(role, new RespMsg("人数不足#<R>3#人，不能发起攻击"));
            return;
        }
        final GangWarsInfo gangWarsInfo = objData.getWarsMap().get(role.getFaction());
        if (gangWarsInfo.getCombatResults() != 3 || !player.getFaction().equals(gangWarsInfo.getEnemy())) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(player.getRoleId());
        if (fight != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("对方在战斗中"));
            return;
        }
        this.doStartFightPackage(role, this.teamService.getTeam(role.getRoleId()), objData.getPkData().get(role.getFaction()), objData.getPkData().get(player.getFaction()), player);
    }
    
    public void doStartFightPackage(final Role role, final Team team, final Map<Long, GangWarsPkData> sideWarsPkDataMap, final Map<Long, GangWarsPkData> enemyWarsPkDataMap, final Role player) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        this.upMemberState(sideWarsPkDataMap, team, (byte)1);
        final Team enemyTeam = this.teamService.getTeam(player.getRoleId());
        Role enemyLeader = player;
        final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(player.getFaction());
        if (enemyTeam == null) {
            if (!this.isStartFight(role.getId(), warsPkDataMap.get(player.getId()))) {
                return;
            }
            this.upMember(player.getId(), enemyWarsPkDataMap, (byte)1, 0);
        }
        else {
            for (int i = 0; i < enemyTeam.getList().size(); ++i) {
                final Member member = enemyTeam.getList().get(i);
                if (member.isInTeam() && !this.isStartFight(role.getId(), warsPkDataMap.get(member.getId()))) {
                    return;
                }
            }
            this.upMemberState(enemyWarsPkDataMap, enemyTeam, (byte)1);
            enemyLeader = this.roleService.getPlayer(enemyTeam.getLeaderUid());
        }
        final GangWarsInfo gangWarsInfo = objData.getWarsMap().get(role.getFaction());
        Equipment equipment = objData.getCannonMap().get(gangWarsInfo.getVisible());
        if (equipment != null && equipment.getList().contains(player.getId())) {
            this.startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)2);
        }
        equipment = objData.getGateMap().get(player.getFaction());
        if (equipment != null) {
            this.startFightBreakOff(role, player, equipment, objData, enemyTeam, (byte)3);
        }
        this.doStartFightPackage(role, team, enemyLeader, enemyTeam);
    }
    
    private void startFightBreakOff(final Role role, final Role player, final Equipment equipment, final GangWarsData objData, final Team enemyTeam, final byte state) {
        final RespOffRoleState offRoleState = new RespOffRoleState("wars", state);
        this.cancelTask(equipment.getScheduledFuture());
        final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
        if (state == 3) {
            objData.getGateMap().remove(warsInfo.getEnemy());
        }
        else {
            objData.getCannonMap().remove(warsInfo.getVisible());
        }
        if (enemyTeam != null) {
            for (int i = 0; i < enemyTeam.getList().size(); ++i) {
                final Member member = enemyTeam.getList().get(i);
                if (member.isInTeam()) {
                    MessagePusher.pushMessage(member.getId(), offRoleState);
                }
            }
        }
        else {
            MessagePusher.pushMessage(player.getId(), offRoleState);
        }
    }
    
    private boolean isStartFight(final long id, final GangWarsPkData enemy) {
        if (enemy != null) {
            if (enemy.getState() == 1 || enemy.getState() == 4) {
                MessagePusher.pushMessage(id, new RespMsg("对方在战斗中"));
                return false;
            }
            if (enemy.getRebirth() > 0) {
                final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
                if (enemy.getRebirth() > currTime) {
                    MessagePusher.pushMessage(id, new RespMsg("对方正在保护阶段"));
                    return false;
                }
                enemy.setRebirth(0);
            }
        }
        return true;
    }
    
    public void doStartFightPackage(final Role roleA, final Team teamA, final Role roleB, final Team teamB) {
        final List<Member> listA = new ArrayList<Member>();
        if (teamA == null) {
            listA.add(Member.valueOf(roleA));
        }
        else {
            listA.addAll(this.fightNewService.excuteMemberLeaveTemp(roleA, teamA));
        }
        final List<Member> listB = new ArrayList<Member>();
        if (teamB == null) {
            listB.add(Member.valueOf(roleB));
        }
        else {
            listB.addAll(this.fightNewService.excuteMemberLeaveTemp(roleB, teamB));
        }
        this.doStartFight(listA, listB);
    }
    
    public void doStartFight(final List<Member> listA, final List<Member> listB) {
        if (listA == null || listB == null) {
            return;
        }
        if (listA.size() < 0 || listB.size() < 0) {
            return;
        }
        final int fightType = this.getFightType();
        final Fight fight = FightFactory.getFightFactory(fightType).create(3001, fightType, listA, listB);
        fight.setMaxRound(GangWarsPkHandler.MAX_ROUND);
        fight.start();
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        try {
            if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            if (!role.getPos().getMapId().equals(3001)) {
                return;
            }
            final GangWarsData objData = this.gangWarsHandler.getObjData();
            if (objData == null) {
                return;
            }
            final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
            if (warsInfo == null || warsInfo.getCombatResults() != 3) {
                return;
            }
            final Team team = this.teamService.getTeam(role.getRoleId());
            final Map<Long, GangWarsPkData> sideWarsPkDataMap = objData.getPkData().get(role.getFaction());
            if (sideWarsPkDataMap == null) {
                return;
            }
            int time = 0;
            final RespGangWarsRoleState respGangWarsRoleState = new RespGangWarsRoleState((byte)0, time);
            if (fightEndEvent.getStatus() == 2) {
                time = new Long(System.currentTimeMillis() / 1000L).intValue() + 90;
                respGangWarsRoleState.setTime(time);
            }
            final FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId());
            for (final Long id : fightInfo.getItself()) {
                this.upMember(id, sideWarsPkDataMap, (byte)0, time);
                if (time > 0) {
                    MessagePusher.pushMessage(id, respGangWarsRoleState);
                }
            }
            this.doFightEnd(role, team, fightEndEvent.getStatus(), objData, warsInfo.getStance());
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    public void doFightEnd(final Role role, final Team team, final byte status, final GangWarsData objData, final byte stance) {
        if (status == 2) {
            int x = 24;
            int y = 19;
            if (stance != 1) {
                x = 195;
                y = 109;
            }
            if (team != null) {
                this.teamService.memberHandleThreadLocal(team, memberRole -> memberRole.pushTempCache("not_check_change_map", true));
            }
            else {
                role.pushTempCache("not_check_change_map", true);
            }
            SpringUtils.getMapService().changeMap(role, new ReqTelePort(3001, x, y), null);
        }
        if (status == 1) {
            final int anInt = objData.getRecordMap().getInt(role.getFaction() + "_win", 0);
            objData.getRecordMap().setv(role.getFaction() + "_win", anInt + 1);
        }
    }
    
    public void getNearby(final Role role) {
        final GangWarsData objData = this.gangWarsHandler.getObjData();
        if (objData == null) {
            return;
        }
        final GangWarsInfo sideInfo = objData.getWarsMap().get(role.getFaction());
        if (sideInfo == null || sideInfo.getCombatResults() != 3) {
            return;
        }
        final Map<Long, GangWarsPkData> warsPkDataMap = objData.getPkData().get(sideInfo.getEnemy());
        if (warsPkDataMap == null) {
            return;
        }
        final List<Nearby> objects = new ArrayList<Nearby>(warsPkDataMap.size());
        for (final Long id : warsPkDataMap.keySet()) {
            final GangWarsPkData data = warsPkDataMap.get(id);
            if (data.getPosition() != 1) {
                continue;
            }
            final Role player = this.roleService.getPlayer(id);
            if (player == null) {
                continue;
            }
            if (this.teamService.isInTeam(player) && !this.teamService.isTeamLeader(player)) {
                continue;
            }
            if (!DataGlobalFunc.visual(role.getPos().getX(), role.getPos().getY(), player.getPos().getX(), player.getPos().getY(), 50.0)) {
                continue;
            }
            objects.add(new Nearby(player));
        }
        if (objects.size() > 0) {
            MessagePusher.pushMessage(role, new RespNearby(objects));
        }
    }
    
    public void cancelTask(final ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_GANG_WARS.getValue();
    }
}
