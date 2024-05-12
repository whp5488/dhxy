
package com.kitty.system.game.activity.handler.timing.hg;

import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.common.model.Pos;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import com.kitty.system.game.activity.model.user.hg.HgPrepare;
import com.kitty.system.game.activity.message.RespHgPrepare;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import com.kitty.system.game.vo.Npc;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.team.domain.Member;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.model.user.hg.Hg;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.npc.service.NpcService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class HgFightHandler extends ActivityHandler
{
    @Autowired
    RoleService roleService;
    @Autowired
    NpcService npcService;
    @Autowired
    TeamService teamService;
    @Autowired
    protected FightNewService fightNewService;
    @Autowired
    ServerService serverService;
    private ConcurrentHashMap<Long, Hg> initiatorMap;
    private ConcurrentHashMap<Long, Hg> receiverMap;
    private static final int MAX_ROUND;
    private static final long GOLD = 3000L;
    
    public HgFightHandler() {
        this.initiatorMap = new ConcurrentHashMap<Long, Hg>();
        this.receiverMap = new ConcurrentHashMap<Long, Hg>();
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.HG_FIGHT.getValue();
    }
    
    private boolean isMatchFightType(final int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void initiator(final Role role, final int id, String msg) {
        if (role.getId().equals(id)) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            if (!team.isLeader(role.getRoleId())) {
                MessagePusher.pushMessage(role.getId(), new RespMsg("队员不能发起挑战"));
                return;
            }
            final Member member = team.getList().stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
            if (member != null) {
                MessagePusher.pushMessage(role.getId(), new RespMsg("说好的队友情呢？"));
                return;
            }
        }
        if (this.initiatorMap.get(role.getId()) != null || this.receiverMap.get(role.getId()) != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("你还有未处理的决斗请求，别着急，一个一个来"));
            return;
        }
        final Role player = this.roleService.getOnlinePlayer(id);
        if (player == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("这名玩家不在线，无法决斗"));
        }
        if (msg != null && msg.length() > 0 && !this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 3000L, "皇宫约战")) {
            return;
        }
        final long millis = System.currentTimeMillis();
        final Hg hg = this.creationHg(role, player, millis);
        final ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.timer(role.getId()), 60000L, 60000L);
        hg.setScheduledFuture(schedule);
        this.initiatorMap.put(role.getId(), hg);
        this.receiverMap.put(player.getId(), new Hg(role.getId(), (byte)1, millis));
        this.prepare(hg, 0L);
        if (msg != null && msg.length() > 0) {
            msg = CheckCharUtils.replaceName(msg);
            SpringUtils.getChatService().sendSystem(MessageFormat.format("#<LB>{0}#在皇宫向#<LB>{1}#发起挑战：{2}", role.getName(), player.getName(), msg), "系统");
        }
        else {
            MessagePusher.pushMessage(player.getId(), new RespNotifyMiscEx(player.getId(), MessageFormat.format("#<G>{0}#向你发起挑战，请打开邮件查看详情", role.getName()), true));
        }
    }
    
    public Hg creationHg(final Role role, final Role player, final long millis) {
        final Hg hg = new Hg();
        hg.setId(player.getId());
        hg.setType((byte)0);
        hg.setTime(millis);
        return this.creation(hg, role);
    }
    
    public void receive(final Role role, final Npc npc) {
        if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
            this.npcService.sendNpcContent(role, npc, "", "队员不能接受挑战，请先离开队伍");
            return;
        }
        Hg hg = this.receiverMap.get(role.getId());
        if (hg == null) {
            this.npcService.sendNpcContent(role, npc, "", "你没有要接受的决斗请求");
            return;
        }
        hg = this.creation(hg, role);
        this.receiverMap.put(role.getId(), hg);
        this.prepare(hg, 0L);
    }
    
    public Hg creation(final Hg hg, final Role role) {
        hg.getMap().put(role.getId(), (byte)0);
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            for (final Member member : team.getList()) {
                if (member.isInTeam()) {
                    final Role r = this.roleService.getPlayer(member.getId());
                    if (r == null) {
                        continue;
                    }
                    hg.getMap().put(r.getId(), (byte)0);
                }
            }
        }
        return hg;
    }
    
    public void accept(final Role role, final byte type) {
        long id = role.getId();
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            id = team.getLeaderUid();
        }
        final Hg initiator = this.initiatorMap.get(id);
        if (initiator != null) {
            initiator.getMap().put(role.getId(), type);
            if (type != 1) {
                return;
            }
            long count = initiator.getMap().values().stream().filter(e -> e == 0).count();
            if (count <= 0L) {
                final Hg receiver = this.receiverMap.get(initiator.getId());
                count = receiver.getMap().values().stream().filter(e -> e == 0).count();
                if (receiver.getMap().size() > 0 && count <= 0L) {
                    this.close(initiator, receiver);
                    this.doStartFightPackage(initiator.getMap().keySet(), receiver.getMap().keySet());
                }
                else {
                    this.prepare(initiator, 0L);
                }
            }
            else {
                this.prepare(initiator, 0L);
            }
        }
        else {
            final Hg receiver2 = this.receiverMap.get(id);
            if (receiver2 != null) {
                receiver2.getMap().put(role.getId(), type);
                if (type != 1) {
                    return;
                }
                long count2 = receiver2.getMap().values().stream().filter(e -> e == 0).count();
                if (count2 <= 0L) {
                    final Hg initiator_ = this.initiatorMap.get(receiver2.getId());
                    count2 = initiator_.getMap().values().stream().filter(e -> e == 0).count();
                    if (count2 <= 0L) {
                        this.close(initiator_, receiver2);
                        this.doStartFightPackage(initiator_.getMap().keySet(), receiver2.getMap().keySet());
                    }
                    else {
                        this.prepare(receiver2, 0L);
                    }
                }
                else {
                    this.prepare(receiver2, 0L);
                }
            }
        }
    }
    
    public void waive(final Role role) {
    }
    
    public void close(final Hg initiator, final Hg receiver) {
        this.cancelTask(initiator.getScheduledFuture());
        this.cancelTask(receiver.getScheduledFuture());
        this.receiverMap.remove(initiator.getId());
        this.initiatorMap.remove(receiver.getId());
    }
    
    public void prepare(final Hg hg, final long id) {
        if (hg != null) {
            final Set<Long> objects = new HashSet<Long>(10);
            objects.addAll(hg.getMap().keySet());
            final RespHgPrepare respHgPrepare = new RespHgPrepare();
            respHgPrepare.setTime((int)(hg.getTime() / 1000L));
            if (hg.getType() == 0) {
                for (final Long key : hg.getMap().keySet()) {
                    final Role role = this.roleService.getPlayer(key);
                    if (role != null) {
                        respHgPrepare.getInitiator().add(new HgPrepare(role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), hg.getMap().get(key)));
                    }
                }
                final Hg receiver = this.receiverMap.get(hg.getId());
                if (receiver != null) {
                    for (final Long key2 : receiver.getMap().keySet()) {
                        final Role role2 = this.roleService.getPlayer(key2);
                        if (role2 != null) {
                            respHgPrepare.getReceiver().add(new HgPrepare(role2.getSerial(), role2.getRelive().byteValue(), role2.getLevel().shortValue(), role2.getName(), receiver.getMap().get(key2)));
                        }
                    }
                    objects.addAll(receiver.getMap().keySet());
                }
                else {
                    this.logger.error("进入应战界面，获取被挑战者信息错误：{}", hg);
                }
            }
            else {
                for (final Long key : hg.getMap().keySet()) {
                    final Role role = this.roleService.getPlayer(key);
                    if (role != null) {
                        respHgPrepare.getReceiver().add(new HgPrepare(role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), hg.getMap().get(key)));
                    }
                }
                final Hg initiator = this.initiatorMap.get(hg.getId());
                if (initiator != null) {
                    for (final Long key2 : initiator.getMap().keySet()) {
                        final Role role2 = this.roleService.getPlayer(key2);
                        if (role2 != null) {
                            respHgPrepare.getInitiator().add(new HgPrepare(role2.getSerial(), role2.getRelive().byteValue(), role2.getLevel().shortValue(), role2.getName(), initiator.getMap().get(key2)));
                        }
                    }
                    objects.addAll(initiator.getMap().keySet());
                }
                else {
                    this.logger.error("进入应战界面，获取挑战者信息错误：{}", hg);
                }
            }
            if (id > 0L) {
                MessagePusher.pushMessage(id, respHgPrepare);
            }
            else {
                for (final Long key : objects) {
                    MessagePusher.pushMessage(key, respHgPrepare);
                }
            }
        }
    }
    
    public void doStartFightPackage(final Set<Long> initiator, final Set<Long> receiver) {
        final List<Member> listA = new ArrayList<Member>();
        for (final Long id : initiator) {
            final Role role = this.roleService.getPlayer(id);
            if (role != null) {
                listA.add(Member.valueOf(role));
            }
        }
        final List<Member> listB = new ArrayList<Member>();
        for (final Long id2 : receiver) {
            final Role role2 = this.roleService.getPlayer(id2);
            if (role2 != null) {
                listB.add(Member.valueOf(role2));
            }
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
        EventsUtils.clashPlans(listA, listB, 3000, 5);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                final int fightType = HgFightHandler.this.getFightType();
                final Fight fight = FightFactory.getFightFactory(fightType).create(0, fightType, listA, listB);
                fight.setMaxRound(HgFightHandler.MAX_ROUND);
                fight.start();
            }
        }, 5000L);
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(final Role role, final FightEndEvent fightEndEvent) {
        try {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null && !team.isLeader(role.getRoleId())) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            final FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId());
            this.doFightEnd(fightInfo.getItself(), team, fightEndEvent.getStatus());
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    public void doFightEnd(final List<Long> list, final Team team, final byte status) {
        if (status == 2) {
            final RespMsg respMsg = new RespMsg("你在皇宫决斗中被击败，进入虚弱期");
            for (final Long id : list) {
                MessagePusher.pushMessage(id, respMsg);
            }
            if (team != null) {
                this.roleService.goBackBorn(this.roleService.getPlayer(team.getLeaderUid()), new Pos(107, 64, 5, 1002));
            }
            else {
                this.roleService.goBackBorn(this.roleService.getPlayer(list.get(0)), new Pos(107, 64, 5, 1002));
            }
        }
        else if (status == 1) {
            Role player = null;
            if (team != null) {
                player = this.roleService.getPlayer(team.getLeaderUid());
            }
            else {
                player = this.roleService.getPlayer(list.get(0));
            }
            if (player != null) {
                final Npc npc = SpringUtils.getMapService().getNpc(90040);
                SpringUtils.getChatService().getNpcRespChat(npc, 1, String.format("#<G>%s#%s", player.getName(), "取得了决斗的胜利"));
            }
        }
    }
    
    public void getNearby(final Role role) {
    }
    
    public void timer(final long id) {
        int count = 0;
        StringBuilder stringBuilder = null;
        final Set<Long> objects = new HashSet<Long>(10);
        final Hg hg = this.initiatorMap.get(id);
        if (hg != null) {
            objects.addAll(hg.getMap().keySet());
            this.initiatorMap.remove(id);
            for (final Long key : hg.getMap().keySet()) {
                if (hg.getMap().get(key) == 0) {
                    final Role role = this.roleService.getPlayer(key);
                    if (role == null) {
                        continue;
                    }
                    if (stringBuilder == null) {
                        stringBuilder = new StringBuilder();
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("、");
                    }
                    stringBuilder.append(role.getName());
                    if (++count >= 3) {
                        stringBuilder.append("...");
                        break;
                    }
                    continue;
                }
            }
            final Hg receiver = this.receiverMap.get(hg.getId());
            this.receiverMap.remove(hg.getId());
            if (receiver != null) {
                objects.addAll(hg.getMap().keySet());
                if (count < 3) {
                    if (receiver.getMap().size() <= 0) {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder();
                        }
                        final Role role2 = this.roleService.getPlayer(hg.getId());
                        if (role2 != null) {
                            stringBuilder.append(role2.getName());
                        }
                    }
                    else {
                        for (final Long key2 : receiver.getMap().keySet()) {
                            if (receiver.getMap().get(key2) == 0) {
                                final Role role3 = this.roleService.getPlayer(key2);
                                if (role3 == null) {
                                    continue;
                                }
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append("、");
                                }
                                stringBuilder.append(role3.getName());
                                if (++count >= 3) {
                                    stringBuilder.append("...");
                                    break;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
        if (stringBuilder != null && stringBuilder.length() > 0) {
            final String msg = MessageFormat.format("#<G>{0}#没有准备，无法战斗", stringBuilder.toString());
            for (final Long key2 : objects) {
                MessagePusher.pushMessage(key2, new RespMsg(msg));
            }
        }
    }
    
    public void cancelTask(final ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    public boolean isBackToTeam(final Team team, final Role role) {
        final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (this.initiatorMap.get(leaderRole.getId()) != null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("队长正在皇宫决斗，无法归队"));
            return false;
        }
        final Hg receiver = this.receiverMap.get(leaderRole.getId());
        if (receiver != null && receiver.getMap().size() > 0) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("队长正在皇宫决斗，无法归队"));
            return false;
        }
        return true;
    }
    
    @Override
    public void handleLogin(final Role role) {
        long id = role.getId();
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            id = team.getLeaderUid();
        }
        final Hg initiator = this.initiatorMap.get(id);
        if (initiator != null && initiator.getMap().get(id) != null) {
            this.prepare(initiator, role.getId());
        }
        else {
            final Hg receiver = this.receiverMap.get(id);
            if (receiver != null && receiver.getMap().get(id) != null) {
                this.prepare(receiver, role.getId());
            }
        }
        super.handleLogin(role);
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_HG.getValue();
    }
}
