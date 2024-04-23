package com.kitty.system.game.activity.handler.timing.hg;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.model.Pos;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.WarMaxRound;
import com.kitty.system.game.activity.common.EventsUtils;
import com.kitty.system.game.activity.message.RespHgPrepare;
import com.kitty.system.game.activity.model.FightInfo;
import com.kitty.system.game.activity.model.user.hg.Hg;
import com.kitty.system.game.activity.model.user.hg.HgPrepare;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.factory.FightFactory;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HgFightHandler extends ActivityHandler {
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
  
  private ConcurrentHashMap<Long, Hg> initiatorMap = new ConcurrentHashMap<>();
  
  private ConcurrentHashMap<Long, Hg> receiverMap = new ConcurrentHashMap<>();
  
  private static final int MAX_ROUND = WarMaxRound.WAR_MAX_ROUND_HG.getValue();
  
  private static final long GOLD = 3000L;
  
  protected int getFightType() {
    return FIGHT_TYPE.HG_FIGHT.getValue();
  }
  
  private boolean isMatchFightType(int fightType) {
    return (getFightType() == fightType);
  }
  
  public void initiator(Role role, int id, String msg) {
    if (role.getId().equals(Integer.valueOf(id)))
      return; 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null) {
      if (!team.isLeader(role.getRoleId().intValue())) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("队员不能发起挑战"));
        return;
      } 
      Member member = team.getList().stream().filter(e -> e.getId().equals(Integer.valueOf(id))).findFirst().orElse(null);
      if (member != null) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("说好的队友情呢？"));
        return;
      } 
    } 
    if (this.initiatorMap.get(role.getId()) != null || this.receiverMap.get(role.getId()) != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你还有未处理的决斗请求，别着急，一个一个来"));
      return;
    } 
    Role player = this.roleService.getOnlinePlayer(id);
    if (player == null)
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("这名玩家不在线，无法决斗")); 
    if (msg != null && msg.length() > 0)
      if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 3000L, "皇宫约战"))
        return;  
    long millis = System.currentTimeMillis();
    Hg hg = creationHg(role, player, millis);
    ScheduledFuture<?> schedule = SchedulerManager.getInstance().scheduleAtFixedRate(() -> timer(role.getId().longValue()), 60000L, 60000L);
    hg.setScheduledFuture(schedule);
    this.initiatorMap.put(role.getId(), hg);
    this.receiverMap.put(player.getId(), new Hg(role.getId().longValue(), (byte)1, millis));
    prepare(hg, 0L);
    if (msg != null && msg.length() > 0) {
      msg = CheckCharUtils.replaceName(msg);
      SpringUtils.getChatService().sendSystem(MessageFormat.format("#<LB>{0}#在皇宫向#<LB>{1}#发起挑战：{2}", new Object[] { role.getName(), player.getName(), msg }), "系统");
    } else {
      MessagePusher.pushMessage(player.getId().longValue(), (Message)new RespNotifyMiscEx(player.getId(), MessageFormat.format("#<G>{0}#向你发起挑战，请打开邮件查看详情", new Object[] { role.getName() }), true));
    } 
  }
  
  public Hg creationHg(Role role, Role player, long millis) {
    Hg hg = new Hg();
    hg.setId(player.getId().longValue());
    hg.setType((byte)0);
    hg.setTime(millis);
    return creation(hg, role);
  }
  
  public void receive(Role role, Npc npc) {
    if (this.teamService.isInTeam(role) && !this.teamService.isTeamLeader(role)) {
      this.npcService.sendNpcContent(role, npc, "", "队员不能接受挑战，请先离开队伍");
      return;
    } 
    Hg hg = this.receiverMap.get(role.getId());
    if (hg == null) {
      this.npcService.sendNpcContent(role, npc, "", "你没有要接受的决斗请求");
      return;
    } 
    hg = creation(hg, role);
    this.receiverMap.put(role.getId(), hg);
    prepare(hg, 0L);
  }
  
  public Hg creation(Hg hg, Role role) {
    hg.getMap().put(role.getId(), Byte.valueOf((byte)0));
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null)
      for (Member member : team.getList()) {
        if (member.isInTeam()) {
          Role r = this.roleService.getPlayer(member.getId().longValue());
          if (r != null)
            hg.getMap().put(r.getId(), Byte.valueOf((byte)0)); 
        } 
      }  
    return hg;
  }
  
  public void accept(Role role, byte type) {
    long id = role.getId().longValue();
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null)
      id = team.getLeaderUid(); 
    Hg initiator = this.initiatorMap.get(Long.valueOf(id));
    if (initiator != null) {
      initiator.getMap().put(role.getId(), Byte.valueOf(type));
      if (type == 1) {
        long count = initiator.getMap().values().stream().filter(e -> (e.byteValue() == 0)).count();
        if (count <= 0L) {
          Hg receiver = this.receiverMap.get(Long.valueOf(initiator.getId()));
          count = receiver.getMap().values().stream().filter(e -> (e.byteValue() == 0)).count();
          if (receiver.getMap().size() > 0 && count <= 0L) {
            close(initiator, receiver);
            doStartFightPackage(initiator.getMap().keySet(), receiver.getMap().keySet());
          } else {
            prepare(initiator, 0L);
          } 
        } else {
          prepare(initiator, 0L);
        } 
      } else {
        return;
      } 
    } else {
      Hg receiver = this.receiverMap.get(Long.valueOf(id));
      if (receiver != null) {
        receiver.getMap().put(role.getId(), Byte.valueOf(type));
        if (type == 1) {
          long count = receiver.getMap().values().stream().filter(e -> (e.byteValue() == 0)).count();
          if (count <= 0L) {
            Hg initiator_ = this.initiatorMap.get(Long.valueOf(receiver.getId()));
            count = initiator_.getMap().values().stream().filter(e -> (e.byteValue() == 0)).count();
            if (count <= 0L) {
              close(initiator_, receiver);
              doStartFightPackage(initiator_.getMap().keySet(), receiver.getMap().keySet());
            } else {
              prepare(receiver, 0L);
            } 
          } else {
            prepare(receiver, 0L);
          } 
        } else {
          return;
        } 
      } 
    } 
  }
  
  public void waive(Role role) {}
  
  public void close(Hg initiator, Hg receiver) {
    cancelTask(initiator.getScheduledFuture());
    cancelTask(receiver.getScheduledFuture());
    this.receiverMap.remove(Long.valueOf(initiator.getId()));
    this.initiatorMap.remove(Long.valueOf(receiver.getId()));
  }
  
  public void prepare(Hg hg, long id) {
    if (hg != null) {
      Set<Long> objects = new HashSet<>(10);
      objects.addAll(hg.getMap().keySet());
      RespHgPrepare respHgPrepare = new RespHgPrepare();
      respHgPrepare.setTime((int)(hg.getTime() / 1000L));
      if (hg.getType() == 0) {
        for (Long key : hg.getMap().keySet()) {
          Role role = this.roleService.getPlayer(key.longValue());
          if (role != null)
            respHgPrepare.getInitiator().add(new HgPrepare(role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), ((Byte)hg.getMap().get(key)).byteValue())); 
        } 
        Hg receiver = this.receiverMap.get(Long.valueOf(hg.getId()));
        if (receiver != null) {
          for (Long key : receiver.getMap().keySet()) {
            Role role = this.roleService.getPlayer(key.longValue());
            if (role != null)
              respHgPrepare.getReceiver().add(new HgPrepare(role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), ((Byte)receiver.getMap().get(key)).byteValue())); 
          } 
          objects.addAll(receiver.getMap().keySet());
        } else {
          this.logger.error("进入应战界面，获取被挑战者信息错误：{}", hg.toString());
        } 
      } else {
        for (Long key : hg.getMap().keySet()) {
          Role role = this.roleService.getPlayer(key.longValue());
          if (role != null)
            respHgPrepare.getReceiver().add(new HgPrepare(role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), ((Byte)hg.getMap().get(key)).byteValue())); 
        } 
        Hg initiator = this.initiatorMap.get(Long.valueOf(hg.getId()));
        if (initiator != null) {
          for (Long key : initiator.getMap().keySet()) {
            Role role = this.roleService.getPlayer(key.longValue());
            if (role != null)
              respHgPrepare.getInitiator().add(new HgPrepare(role.getSerial().intValue(), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getName(), ((Byte)initiator.getMap().get(key)).byteValue())); 
          } 
          objects.addAll(initiator.getMap().keySet());
        } else {
          this.logger.error("进入应战界面，获取挑战者信息错误：{}", hg.toString());
        } 
      } 
      if (id > 0L) {
        MessagePusher.pushMessage(id, (Message)respHgPrepare);
      } else {
        for (Long key : objects)
          MessagePusher.pushMessage(key.longValue(), (Message)respHgPrepare); 
      } 
    } 
  }
  
  public void doStartFightPackage(Set<Long> initiator, Set<Long> receiver) {
    List<Member> listA = new ArrayList<>();
    for (Long id : initiator) {
      Role role = this.roleService.getPlayer(id.longValue());
      if (role != null)
        listA.add(Member.valueOf(role)); 
    } 
    List<Member> listB = new ArrayList<>();
    for (Long id : receiver) {
      Role role = this.roleService.getPlayer(id.longValue());
      if (role != null)
        listB.add(Member.valueOf(role)); 
    } 
    doStartFight(listA, listB);
  }
  
  public void doStartFight(final List<Member> listA, final List<Member> listB) {
    if (listA == null || listB == null)
      return; 
    if (listA.size() < 0 || listB.size() < 0)
      return; 
    EventsUtils.clashPlans(listA, listB, 3000, 5);
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            int fightType = HgFightHandler.this.getFightType();
            Fight fight = FightFactory.getFightFactory(fightType).create(0, new Object[] {fightType, listA, listB });
            fight.setMaxRound(HgFightHandler.MAX_ROUND);
            fight.start();
          }
        }, 5000L);
  }
  
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    ThreadLocalUtil.addLocalTask(fightEndEvent.getRole(), () -> doFightEnd(fightEndEvent.getRole(), fightEndEvent));
  }
  
  private void doFightEnd(Role role, FightEndEvent fightEndEvent) {
    try {
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null && !team.isLeader(role.getRoleId().intValue()))
        return; 
      if (!isMatchFightType(fightEndEvent.getFightType()))
        return; 
      FightInfo fightInfo = EventsUtils.getFightInfo(fightEndEvent.getRolesA(), fightEndEvent.getRolesB(), role.getId().longValue());
      doFightEnd(fightInfo.getItself(), team, fightEndEvent.getStatus());
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("战斗结束后操作错误-------------------------------------------------------");
    } 
  }
  
  public void doFightEnd(List<Long> list, Team team, byte status) {
    if (status == 2) {
      RespMsg respMsg = new RespMsg("你在皇宫决斗中被击败，进入虚弱期");
      for (Long id : list)
        MessagePusher.pushMessage(id.longValue(), (Message)respMsg); 
      if (team != null) {
        this.roleService.goBackBorn(this.roleService.getPlayer(team.getLeaderUid()), new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
      } else {
        this.roleService.goBackBorn(this.roleService.getPlayer(((Long)list.get(0)).longValue()), new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
      } 
    } else if (status == 1) {
      Role player = null;
      if (team != null) {
        player = this.roleService.getPlayer(team.getLeaderUid());
      } else {
        player = this.roleService.getPlayer(((Long)list.get(0)).longValue());
      } 
      if (player != null) {
        Npc npc = SpringUtils.getMapService().getNpc(90040);
        SpringUtils.getChatService().getNpcRespChat(npc, 1, String.format("#<G>%s#%s", new Object[] { player.getName(), "取得了决斗的胜利" }));
      } 
    } 
  }
  
  public void getNearby(Role role) {}
  
  public void timer(long id) {
    int count = 0;
    StringBuilder stringBuilder = null;
    Set<Long> objects = new HashSet<>(10);
    Hg hg = this.initiatorMap.get(Long.valueOf(id));
    if (hg != null) {
      objects.addAll(hg.getMap().keySet());
      this.initiatorMap.remove(Long.valueOf(id));
      for (Long key : hg.getMap().keySet()) {
        if (((Byte)hg.getMap().get(key)).byteValue() == 0) {
          Role role = this.roleService.getPlayer(key.longValue());
          if (role == null)
            continue; 
          if (stringBuilder == null)
            stringBuilder = new StringBuilder(); 
          if (stringBuilder.length() > 0)
            stringBuilder.append("、"); 
          stringBuilder.append(role.getName());
          count++;
          if (count >= 3) {
            stringBuilder.append("...");
            break;
          } 
        } 
      } 
      Hg receiver = this.receiverMap.get(Long.valueOf(hg.getId()));
      this.receiverMap.remove(Long.valueOf(hg.getId()));
      if (receiver != null) {
        objects.addAll(hg.getMap().keySet());
        if (count < 3)
          if (receiver.getMap().size() <= 0) {
            if (stringBuilder == null)
              stringBuilder = new StringBuilder(); 
            Role role = this.roleService.getPlayer(hg.getId());
            if (role != null)
              stringBuilder.append(role.getName()); 
          } else {
            for (Long key : receiver.getMap().keySet()) {
              if (((Byte)receiver.getMap().get(key)).byteValue() == 0) {
                Role role = this.roleService.getPlayer(key.longValue());
                if (role == null)
                  continue; 
                if (stringBuilder.length() > 0)
                  stringBuilder.append("、"); 
                stringBuilder.append(role.getName());
                count++;
                if (count >= 3) {
                  stringBuilder.append("...");
                  break;
                } 
              } 
            } 
          }  
      } 
    } 
    if (stringBuilder != null && 
      stringBuilder.length() > 0) {
      String msg = MessageFormat.format("#<G>{0}#没有准备，无法战斗", new Object[] { stringBuilder.toString() });
      for (Long key : objects)
        MessagePusher.pushMessage(key.longValue(), (Message)new RespMsg(msg)); 
    } 
  }
  
  public void cancelTask(ScheduledFuture<?> task) {
    if (task != null)
      task.cancel(Boolean.FALSE.booleanValue()); 
  }
  
  public boolean isBackToTeam(Team team, Role role) {
    Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
    if (this.initiatorMap.get(leaderRole.getId()) != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("队长正在皇宫决斗，无法归队"));
      return false;
    } 
    Hg receiver = this.receiverMap.get(leaderRole.getId());
    if (receiver != null && receiver.getMap().size() > 0) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("队长正在皇宫决斗，无法归队"));
      return false;
    } 
    return true;
  }
  
  public void handleLogin(Role role) {
    long id = role.getId().longValue();
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    if (team != null)
      id = team.getLeaderUid(); 
    Hg initiator = this.initiatorMap.get(Long.valueOf(id));
    if (initiator != null && initiator.getMap().get(Long.valueOf(id)) != null) {
      prepare(initiator, role.getId().longValue());
    } else {
      Hg receiver = this.receiverMap.get(Long.valueOf(id));
      if (receiver != null && receiver.getMap().get(Long.valueOf(id)) != null)
        prepare(receiver, role.getId().longValue()); 
    } 
    super.handleLogin(role);
  }
}
