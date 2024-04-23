package com.kitty.system.game.activity.handler.time;

import com.kitty.common.bean.Lottery;
import com.kitty.common.core.SchedulerManager;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.equip.EquipDistKind;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.Position;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.vo.Param;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class WorldBoosHandler extends FightActivityHandler {
  private static final int NEED_LEVEL = 35;
  
  private static final int SHOW_MINUTE = 30;
  
  private static final String CONTENT = "就是来挑战你的/threats_boos|再见/离开";
  
  private static final String IN_FIGHT = "我要观战/guard|再见/离开";
  
  private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足35级#<E:76>#，还是练练再来吧#<E:109>#";
  
  private static final String TIPS = "{0}听过么，连我你都敢惹？";
  
  private static final String TIPS_NOT = "我正在战斗中，等下再来收拾你#<E:32>#";
  
  protected int getFightType(Role role) {
    return FIGHT_TYPE.SUPER_BOSS.getValue();
  }
  
  public void flushBoosAfterServerStart() {
    List<SysDataWorldBoos> list = TaskDataPool.worldBoosList;
    for (SysDataWorldBoos obj : list) {
      long count = DataCache.BOOS_OFFICER_SET.values().stream().filter(e -> (e.getLevel() == obj.getLevel())).count();
      count = obj.getNum() - count;
      if (count > 0L)
        for (int i = 0; i < count; i++) {
          Position position = getRandomPosition(obj.getScene());
          if (position != null) {
            int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
            Npc npc = new Npc(id, 0, obj.getScene(), position.getX().intValue(), position.getY().intValue(), obj.getIcon(), obj.getName(), obj.getTitle(), obj.getId(), obj.getLevel(), 0, 23);
            npc.setEndTime(npc.getCreateTime() + 1800);
            DataCache.BOOS_OFFICER_SET.put(Integer.valueOf(npc.getId()), npc);
            Set<Integer> set = DataCache.BOOS_OFFICER_MAP_NPC.computeIfAbsent(Integer.valueOf(obj.getScene()), k -> new HashSet());
            set.add(Integer.valueOf(npc.getId()));
            this.bossService.broadcastNpcShow(null, npc);
            SchedulerManager.getInstance().schedule(() -> clearBoosTimeOut(npc), 1800000L);
          } 
        }  
    } 
  }
  
  public String getNpcContent(Role role, Npc npc) {
    setContent(npc, "{0}听过么，连我你都敢惹？");
    return "就是来挑战你的/threats_boos|再见/离开";
  }
  
  public String getNpcContentNotFight(Role role, Npc npc) {
    if (npc.isInFight()) {
      setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
      sendNpcContent(role, npc, "我要观战/guard|再见/离开");
      return "我要观战/guard|再见/离开";
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = null;
    if (this.teamService.isInTeam(role)) {
      names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 35));
    } else if (!SpringUtils.getActivityService().isLevel(role, 35)) {
      names = role.getName();
    } 
    if (names != null) {
      npc.setTalk(MessageFormat.format("{0}等级竟然不足35级#<E:76>#，还是练练再来吧#<E:109>#", new Object[] { "#<LB1>" + names + "#" }));
      return "";
    } 
    if (names != null)
      return names; 
    return null;
  }
  
  public void setContent(Npc npc, String talk) {
    SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (obj != null)
      npc.setTalk(MessageFormat.format(talk, new Object[] { obj.getName() })); 
  }
  
  public void doStartFight(Role role, Npc npc) {
    SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (obj != null) {
      Random random = new Random();
      int index = random.nextInt(obj.getWar().size());
      SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(obj.getWar().get(index));
      if (warRole != null) {
        List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
        if (CollectionUtils.isEmpty(monsters)) {
          setNotInFight(npc);
          return;
        } 
        monsters.stream().forEach(monster -> {
              if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                monster.setName(npc.getName());
              } else {
                monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
              } 
              monster.setMonsterType(FIGHT_TYPE.SUPER_BOSS.getValue());
            });
        BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.SUPER_BOSS.getValue(), 0, npc.getId());
        Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
        if (fight == null)
          setNotInFight(npc); 
      } else {
        this.logger.error("世界BOOS获取战斗NPC错误，{}", Integer.valueOf(npc.getId()));
        setNotInFight(npc);
      } 
    } else {
      setNotInFight(npc);
    } 
  }
  
  protected Npc getBossNpc(int npcId) {
    return (Npc)DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npcId));
  }
  
  public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
    super.doFightWinForSingle(role, fightEndEvent, npc);
    try {
      SpringUtils.getActivityService().addFinishCount(role, ActivityType.WORLD_BOOS_OFFICER, 1);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {
    clearStarOfficerAfterFightWin(bossNpc);
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
    long exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(role.getExp() * 0.02D)).longValue();
    long deduct = deduct(getBossNpc(fightEndEvent.getNpcId()));
    if (deduct > 0L)
      exp = deduct; 
    long finalExp = exp;
    this.teamService.memberHandleThreadLocal(role, memberRole -> SpringUtils.getRoleService().punishFightDead(memberRole, -finalExp, "由于你的死亡，你丢失了"));
    SpringUtils.getRoleService().goBackBorn(role, new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
  }
  
  public long deduct(Npc npc) {
    long exp = 0L;
    if (npc != null) {
      SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
      Param param = obj.getAward().stream().filter(e -> (e.getK().equals(Integer.valueOf(ItemClassify.HERO_EXP.getId())) && e.getT().equals(Integer.valueOf(0)))).findFirst().orElse(null);
      if (param != null)
        exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(param.getV().intValue() * 0.2D)).longValue(); 
    } 
    return exp;
  }
  
  public void giveReward(Role role, FightEndEvent fightEndEvent, Npc npc) {
    if (this.teamService.getTeam(role.getRoleId().intValue()) != null)
      if (!this.teamService.isInTeam(role)) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("离队无法获得奖励"));
        return;
      }  
    if (role.getLastDead() != 0) {
      SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
      if (obj != null && SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.WORLD_BOOS_OFFICER)) {
        ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, obj.getAward(), "世界BOOS", obj.getLevel());
        specialReward(role, obj.getSpecial(), obj.getLevel(), obj.getName());
      } 
    } 
  }
  
  public void specialReward(Role role, List<Param> special, int grade, String cause) {
    if (special.size() <= 0)
      return; 
    int pos = ((BagService)SpringUtils.getBean(BagService.class)).getPos(role, false);
    if (pos <= 0)
      return; 
    List<Lottery> list = new ArrayList<>();
    for (Param param : special)
      list.add(new Lottery(param.getK(), "", Double.valueOf(param.getR().intValue() / 1000.0D), param.getV().intValue())); 
    List<Double> rates = new ArrayList<>(list.size());
    for (Lottery lottery1 : list)
      rates.add(lottery1.getBili()); 
    Lottery lottery = list.get(LotteryUtils.lottery(rates));
    if (lottery.getNum() > 0) {
      FiedEquip info = EquipDistKind.getInfo(lottery.getSerial());
      if (info != null && !info.getType().equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()))) {
        int serial = DataGlobalFunc.randomEquip(info.getType().intValue(), info.getValue().intValue(), role.getRace().intValue());
        if (serial > 0) {
          BagEquip equip = EquipManage.createEquip(Integer.valueOf(serial));
          if (equip != null) {
            ((EquipService)SpringUtils.getBean(EquipService.class)).addEquip(role, equip, pos, 0, cause);
            MessagePusher.notify2Lb(role, 214, new Object[] { role.getName(), cause, String.format("#<MI:%s,MP:%d,M:1,MT:%d,CI:%d}>%s#", new Object[] { equip.getId(), role.getRoleId(), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), DataGlobalFunc.getEquipDesc(serial, DataGlobalFunc.getPj(equip.getType())) }), String.format("#<O>X%d#", new Object[] { Integer.valueOf(1) }) });
          } 
        } 
      } else {
        int serial = lottery.getSerial().intValue();
        if (serial == EquipDistKind.SUIT.getNote())
          serial = DataGlobalFunc.randomAcsryEgg(role.getRace().intValue()); 
        ((BagService)SpringUtils.getBean(BagService.class)).rewardItem(role, serial, lottery.getNum(), 0, cause);
        ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
        if (itemEasyInfo != null)
          MessagePusher.notify2Lb(role, 214, new Object[] { role.getName(), cause, String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { itemEasyInfo.getSerial(), Integer.valueOf(0), Integer.valueOf(0), itemEasyInfo.getSerial(), itemEasyInfo.getSerial(), itemEasyInfo.getName() }), String.format("#<ZWG3>X%d#", new Object[] { Integer.valueOf(lottery.getNum()) }) }); 
      } 
    } 
  }
  
  private void clearStarOfficerTimeOut(Npc npc) {
    if (npc == null)
      return; 
    if (DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npc.getId())) == null)
      return; 
    if (npc.isInFight()) {
      this.bossService.broadcastNpcHide(null, npc);
    } else {
      clearStarOfficer(npc);
      DataCache.BOOS_OFFICER_SET.remove(Integer.valueOf(npc.getId()));
    } 
  }
  
  private void clearStarOfficerAfterFightWin(Npc npc) {
    clearStarOfficer(npc);
  }
  
  private void clearBoosTimeOut(Npc npc) {
    if (npc == null)
      return; 
    if (DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npc.getId())) == null)
      return; 
    if (npc.isInFight()) {
      this.bossService.broadcastNpcHide(null, npc);
    } else {
      clearStarOfficer(npc);
    } 
  }
  
  private void clearStarOfficer(Npc npc) {
    if (npc == null)
      return; 
    DataCache.BOOS_OFFICER_SET.remove(Integer.valueOf(npc.getId()));
    Set<Integer> set = (Set<Integer>)DataCache.BOOS_OFFICER_MAP_NPC.get(Integer.valueOf(npc.getMap()));
    if (set != null) {
      set.remove(Integer.valueOf(npc.getId()));
      removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
      this.bossService.broadcastNpcHide(null, npc);
    } 
  }
  
  protected void clearNpcTimeOut(Npc npc) {
    clearStarOfficerTimeOut(npc);
  }
}
