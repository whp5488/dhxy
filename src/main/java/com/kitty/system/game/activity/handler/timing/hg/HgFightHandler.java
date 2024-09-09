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
import java.util.HashSet;
import java.util.Iterator;
import com.kitty.system.game.vo.Npc;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.team.domain.Member;
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
        this.initiatorMap = new ConcurrentHashMap();
        this.receiverMap = new ConcurrentHashMap();
    }
    
    protected int getFightType() {
        return FIGHT_TYPE.HG_FIGHT.getValue();
    }
    
    private boolean isMatchFightType(int fightType) {
        return this.getFightType() == fightType;
    }
    
    public void initiator(Role role, int id, String msg) {
        if (role.getId().equals(Integer.valueOf(id))) {
            return;
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            if (!team.isLeader((int)role.getRoleId())) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg("队员不能发起挑战"));
                return;
            }
            Member member = (Member)team.getList().stream().filter(e -> e.getId().equals(Integer.valueOf(id))).findFirst().orElse(null);
            if (member != null) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg("说好的队友情呢？"));
                return;
            }
        }
        if (this.initiatorMap.get(role.getId()) != null || this.receiverMap.get(role.getId()) != null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("你还有未处理的决斗请求，别着急，一个一个来"));
            return;
        }
        Role player = this.roleService.getOnlinePlayer(id);
        if (player == null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("这名玩家不在线，无法决斗"));
        }
        if (msg != null && msg.length() > 0 && !this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 3000L, "皇宫约战")) {
            return;
        }
        long millis = System.currentTimeMillis();
        Hg hg = this.creationHg(role, player, millis);
        ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> this.timer((long)role.getId()), 60000L, 60000L);
        hg.setScheduledFuture(schedule);
        this.initiatorMap.put(role.getId(), hg);
        this.receiverMap.put(player.getId(), new Hg((long)role.getId(), (byte)1, millis));
        this.prepare(hg, 0L);
        if (msg != null && msg.length() > 0) {
            msg = CheckCharUtils.replaceName(msg);
            SpringUtils.getChatService().sendSystem(MessageFormat.format("#<LB>{0}#在皇宫向#<LB>{1}#发起挑战：{2}", role.getName(), player.getName(), msg), "系统");
        }
        else {
            MessagePusher.pushMessage((long)player.getId(), new RespNotifyMiscEx(player.getId(), MessageFormat.format("#<G>{0}#向你发起挑战，请打开邮件查看详情", role.getName()), true));
        }
        return;
    }
    
    public Hg creationHg(Role role, Role player, long millis) {
        Hg hg = new Hg();
        hg.setId((long)player.getId());
        hg.setType((byte)0);
        hg.setTime(millis);
        return this.creation(hg, role);
    }
    
    public void receive(Role role, Npc npc) {
        if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
            this.npcService.sendNpcContent(role, npc, "", "队员不能接受挑战，请先离开队伍");
            return;
        }
        Hg hg = (Hg)this.receiverMap.get(role.getId());
        if (hg == null) {
            this.npcService.sendNpcContent(role, npc, "", "你没有要接受的决斗请求");
            return;
        }
        hg = this.creation(hg, role);
        this.receiverMap.put(role.getId(), hg);
        this.prepare(hg, 0L);
        return;
    }
    
    public Hg creation(Hg hg, Role role) {
        hg.getMap().put(role.getId(), Byte.valueOf((byte)0));
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            for (Member member : team.getList()) {
                if (member.isInTeam()) {
                    Role r = this.roleService.getPlayer((long)member.getId());
                    if (r != null) {
                        hg.getMap().put(r.getId(), Byte.valueOf((byte)0));
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            return hg;
        }
        return hg;
    }
    
    public void accept(Role role, byte type) {
        long id = (long)role.getId();
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            id = team.getLeaderUid();
        }
        Hg initiator = (Hg)this.initiatorMap.get(Long.valueOf(id));
        if (initiator != null) {
            initiator.getMap().put(role.getId(), Byte.valueOf(type));
            if (type == 1) {
                long count2 = initiator.getMap().values().stream().filter(e-> (byte)e == 0).count();
                if (count2 <= 0L) {
                    Hg receiver2 = (Hg)this.receiverMap.get(Long.valueOf(initiator.getId()));
                    count2 = receiver2.getMap().values().stream().filter(e -> (byte)e == 0).count();
                    if (receiver2.getMap().size() > 0 && count2 <= 0L) {
                        this.close(initiator, receiver2);
                        this.doStartFightPackage(initiator.getMap().keySet(), receiver2.getMap().keySet());
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
                return;
            }
        }
        else {
            Hg receiver = (Hg)this.receiverMap.get(Long.valueOf(id));
            if (receiver != null) {
                receiver.getMap().put(role.getId(), Byte.valueOf(type));
                if (type == 1) {
                    long count = receiver.getMap().values().stream().filter(e -> (byte)e == 0).count();
                    if (count <= 0L) {
                        Hg initiator_ = (Hg)this.initiatorMap.get(Long.valueOf(receiver.getId()));
                        count = initiator_.getMap().values().stream().filter(e -> (byte)e == 0).count();
                        if (count <= 0L) {
                            this.close(initiator_, receiver);
                            this.doStartFightPackage(initiator_.getMap().keySet(), receiver.getMap().keySet());
                        }
                        else {
                            this.prepare(receiver, 0L);
                        }
                    }
                    else {
                        this.prepare(receiver, 0L);
                    }
                }
                else {
                    return;
                }
            }
        }
    }
    
    public void waive(Role role) {
    }
    
    public void close(Hg initiator, Hg receiver) {
        this.cancelTask(initiator.getScheduledFuture());
        this.cancelTask(receiver.getScheduledFuture());
        this.receiverMap.remove(Long.valueOf(initiator.getId()));
        this.initiatorMap.remove(Long.valueOf(receiver.getId()));
    }
    
    public void prepare(Hg hg, long id) {
        if (hg != null) {
            Set<Long> objects = new HashSet(10);
            objects.addAll(hg.getMap().keySet());
            RespHgPrepare respHgPrepare = new RespHgPrepare();
            respHgPrepare.setTime((int)(hg.getTime() / 1000L));
            if (hg.getType() == 0) {
                for (Long key : hg.getMap().keySet()) {
                    Role role = this.roleService.getPlayer((long)key);
                    if (role != null) {
                        respHgPrepare.getInitiator().add(new HgPrepare((int)role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), (byte)hg.getMap().get(key)));
                    }
                    else {
                        continue;
                    }
                }
                Hg receiver = (Hg)this.receiverMap.get(Long.valueOf(hg.getId()));
                if (receiver != null) {
                    for (Long key2 : receiver.getMap().keySet()) {
                        Role role2 = this.roleService.getPlayer((long)key2);
                        if (role2 != null) {
                            respHgPrepare.getReceiver().add(new HgPrepare((int)role2.getSerial(), role2.getRelive().byteValue(), role2.getLevel().shortValue(), role2.getName(), (byte)receiver.getMap().get(key2)));
                        }
                        else {
                            continue;
                        }
                    }
                    objects.addAll(receiver.getMap().keySet());
                }
                else {
                    this.logger.error("进入应战界面，获取被挑战者信息错误：{}", hg.toString());
                }
            }
            else {
                for (Long key : hg.getMap().keySet()) {
                    Role role = this.roleService.getPlayer((long)key);
                    if (role != null) {
                        respHgPrepare.getReceiver().add(new HgPrepare((int)role.getSerial(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), (byte)hg.getMap().get(key)));
                    }
                    else {
                        continue;
                    }
                }
                Hg initiator = (Hg)this.initiatorMap.get(Long.valueOf(hg.getId()));
                if (initiator != null) {
                    for (Long key2 : initiator.getMap().keySet()) {
                        Role role2 = this.roleService.getPlayer((long)key2);
                        if (role2 != null) {
                            respHgPrepare.getInitiator().add(new HgPrepare((int)role2.getSerial(), role2.getRelive().byteValue(), role2.getLevel().shortValue(), role2.getName(), (byte)initiator.getMap().get(key2)));
                        }
                        else {
                            continue;
                        }
                    }
                    objects.addAll(initiator.getMap().keySet());
                }
                else {
                    this.logger.error("进入应战界面，获取挑战者信息错误：{}", hg.toString());
                }
            }
            if (id > 0L) {
                MessagePusher.pushMessage(id, respHgPrepare);
            }
            else {
                for (Long key : objects) {
                    MessagePusher.pushMessage((long)key, respHgPrepare);
                }
            }
        }
    }
    
    public void doStartFightPackage(Set<Long> initiator, Set<Long> receiver) {
        List<Member> listA = new ArrayList<>();
        for (Long id : initiator) {
            Role role = this.roleService.getPlayer((long)id);
            if (role != null) {
                listA.add(Member.valueOf(role));
            }
            else {
                continue;
            }
        }
        List<Member> listB = new ArrayList<>();
        for (Long id2 : receiver) {
            Role role2 = this.roleService.getPlayer((long)id2);
            if (role2 != null) {
                listB.add(Member.valueOf(role2));
            }
            else {
                continue;
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
                int fightType = HgFightHandler.this.getFightType();
                Fight fight = FightFactory.getFightFactory(fightType).create(0, Integer.valueOf(fightType), listA, listB);
                fight.setMaxRound(HgFightHandler.MAX_ROUND);
                fight.start();
            }
        }, 5000L);
        return;
    }
    
    @Override
    public void handleFightEnd(FightEndEvent fightEndEvent) {
        ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> this.doFightEnd(fightEndEvent.getRole(), fightEndEvent));
    }
    
    private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
        try {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null && !team.isLeader((int)role.getRoleId())) {
                return;
            }
            if (!this.isMatchFightType(fightEndEvent.getFightType())) {
                return;
            }
            FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), (long)role.getId());
            this.doFightEnd(fightInfo.getItself(), team, fightEndEvent.getStatus());
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("战斗结束后操作错误-------------------------------------------------------");
        }
    }
    
    public void doFightEnd(List<Long> list, Team team, byte status) {
        if (status == 2) {
            RespMsg respMsg = new RespMsg("你在皇宫决斗中被击败，进入虚弱期");
            for (Long id : list) {
                MessagePusher.pushMessage((long)id, respMsg);
            }
            if (team != null) {
                this.roleService.goBackBorn(this.roleService.getPlayer(team.getLeaderUid()), new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
            }
            else {
                this.roleService.goBackBorn(this.roleService.getPlayer((long)list.get(0)), new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
            }
        }
        else if (status == 1) {
            Role player = null;
            if (team != null) {
                player = this.roleService.getPlayer(team.getLeaderUid());
            }
            else {
                player = this.roleService.getPlayer((long)list.get(0));
            }
            if (player != null) {
                Npc npc = SpringUtils.getMapService().getNpc(90040);
                SpringUtils.getChatService().getNpcRespChat(npc, 1, String.format("#<G>%s#%s", player.getName(), "取得了决斗的胜利"));
            }
        }
    }
    
    public void getNearby(Role role) {
    }
    
    public void timer(long id) {
        int count = 0;
        StringBuilder stringBuilder = null;
        Set<Long> objects = new HashSet(10);
        Hg hg = (Hg)this.initiatorMap.get(Long.valueOf(id));
        if (hg != null) {
            objects.addAll(hg.getMap().keySet());
            this.initiatorMap.remove(Long.valueOf(id));
            for (Long key : hg.getMap().keySet()) {
                if ((byte)hg.getMap().get(key) == 0) {
                    Role role = this.roleService.getPlayer((long)key);
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
                else {
                    continue;
                }
            }
            Hg receiver = (Hg)this.receiverMap.get(Long.valueOf(hg.getId()));
            this.receiverMap.remove(Long.valueOf(hg.getId()));
            if (receiver != null) {
                objects.addAll(hg.getMap().keySet());
                if (count < 3) {
                    if (receiver.getMap().size() <= 0) {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder();
                        }
                        Role role3 = this.roleService.getPlayer(hg.getId());
                        if (role3 != null) {
                            stringBuilder.append(role3.getName());
                        }
                    }
                    else {
                        for (Long key2 : receiver.getMap().keySet()) {
                            if ((byte)receiver.getMap().get(key2) == 0) {
                                Role role2 = this.roleService.getPlayer((long)key2);
                                if (role2 == null) {
                                    continue;
                                }
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append("、");
                                }
                                stringBuilder.append(role2.getName());
                                if (++count >= 3) {
                                    stringBuilder.append("...");
                                    break;
                                }
                                continue;
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        if (stringBuilder != null && stringBuilder.length() > 0) {
            String msg = MessageFormat.format("#<G>{0}#没有准备，无法战斗", stringBuilder.toString());
            for (Long key2 : objects) {
                MessagePusher.pushMessage(key2, new RespMsg(msg));
            }
        }
    }
    
    public void cancelTask(ScheduledFuture<?> task) {
        if (task != null) {
            task.cancel((boolean)Boolean.FALSE);
        }
    }
    
    public boolean isBackToTeam(Team team, Role role) {
        Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        if (this.initiatorMap.get(leaderRole.getId()) != null) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("队长正在皇宫决斗，无法归队"));
            return false;
        }
        Hg receiver = (Hg)this.receiverMap.get(leaderRole.getId());
        if (receiver != null && receiver.getMap().size() > 0) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("队长正在皇宫决斗，无法归队"));
            return false;
        }
        return true;
    }
    
    @Override
    public void handleLogin(Role role) {
        long id = (long)role.getId();
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            id = team.getLeaderUid();
        }
        Hg initiator = (Hg)this.initiatorMap.get(Long.valueOf(id));
        if (initiator != null && initiator.getMap().get(Long.valueOf(id)) != null) {
            this.prepare(initiator, (long)role.getId());
        }
        else {
            Hg receiver = (Hg)this.receiverMap.get(Long.valueOf(id));
            if (receiver != null && receiver.getMap().get(Long.valueOf(id)) != null) {
                this.prepare(receiver, (long)role.getId());
            }
        }
        super.handleLogin(role);
    }
    
    static {
        MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_HG.getValue();
    }
}
