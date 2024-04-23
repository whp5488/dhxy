package com.kitty.system.game.activity.handler.time;

import com.kitty.common.core.SchedulerManager;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.Position;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataSealDemon;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysWarRole;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class SealDemonHandler extends FightActivityHandler {
  private static final int NEED_LEVEL = 33;
  
  private static final int SHOW_MINUTE = 15;
  
  private static final String CONTENT = "确定/threats_seal|取消/离开";
  
  private static final String IN_FIGHT = "我要观战/guard|取消/离开";
  
  private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足33级#<E:76>#，还是练练再来吧#<E:109>#";
  
  private static final String TIPS = "  确定要进入与{0}的战斗吗？\n（推荐等级:{1})";
  
  private static final String TIPS_NOT = "  我正在战斗中，等下再来收拾你#<E:32>#";
  
  private static final String NOTICE = "#<B>{0}#在挖宝时不小心打开了高僧封印妖魔的箱子，一群妖魔冲天而出,正在#<r:181,g:115,b:52>{1}#大肆荼害百姓!各路英雄快前往剿灭啊!";
  
  protected int getFightType(Role role) {
    return FIGHT_TYPE.SEAL_DEMON.getValue();
  }
  
  public boolean flushConditionByLevel(Role role, int num) {
    List<Integer> list = calculateFloat(role.getLevel().intValue(), 33);
    int scene = create(role, list, true);
    if (scene > 0) {
      num--;
      if (num > 0) {
        List<Integer> newList = scope(scene, list);
        for (int i = 0; i < num; i++)
          create(role, newList, false); 
      } 
    } 
    return false;
  }
  
  public int create(Role role, List<Integer> list, boolean is) {
    Random random = new Random();
    int index = random.nextInt(list.size());
    int grade = ((Integer)list.get(index)).intValue();
    List<SysDataSealDemon> collect = (List<SysDataSealDemon>)TaskDataPool.sealDemonList.stream().filter(e -> (e.getGrade() == grade)).collect(Collectors.toList());
    if (collect != null && collect.size() > 0) {
      SysDataSealDemon object = collect.get(random.nextInt(collect.size()));
      long count = DataCache.SEAL_DEMON_SET.values().stream().filter(e -> (e.getLevel() == object.getGrade())).count();
      count = object.getNum() - count;
      if (count > 0L) {
        boolean load = load(object, role, is);
        if (load)
          return object.getScene(); 
      } 
    } 
    return -1;
  }
  
  public void flushAfterServerStart() {
    ArrayList<Integer> objects = new ArrayList<>();
    List<SysDataSealDemon> sealDemonList = TaskDataPool.sealDemonList;
    for (SysDataSealDemon object : sealDemonList) {
      long count = DataCache.SEAL_DEMON_SET.values().stream().filter(e -> (e.getLevel() == object.getGrade())).count();
      count = object.getNum() - count;
      if (count > 0L)
        for (int i = 0; i < count; i++) {
          load(object, (Role)null, !objects.contains(Integer.valueOf(object.getScene())));
          objects.add(Integer.valueOf(object.getScene()));
        }  
    } 
  }
  
  public boolean load(SysDataSealDemon object, Role role, boolean is) {
    Position position = getRandomPosition(object.getScene());
    if (position != null) {
      int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(object.getWar().size() - 1));
      Integer warSerial = object.getWar().get(index);
      SysDataMonster monster = DataGlobalFunc.getMainMonster(warSerial);
      if (monster == null) {
        this.logger.error("加载封妖数据错误 = {}", Integer.valueOf(object.getId()));
        return false;
      } 
      int icon = monster.getShape().intValue();
      if (monster.getShape().intValue() <= 1)
        icon = DataGlobalFunc.getFormula(role, monster.getShape()).intValue(); 
      int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
      Npc npc = new Npc(id, 0, object.getScene(), position.getX().intValue(), position.getY().intValue(), icon, object.getName(), "", object.getId(), object.getGrade(), warSerial.intValue(), 21);
      npc.setEndTime(npc.getCreateTime() + 900);
      DataCache.SEAL_DEMON_SET.put(Integer.valueOf(npc.getId()), npc);
      Set<Integer> set = DataCache.SEAL_DEMON_MAP_NPC.computeIfAbsent(Integer.valueOf(object.getScene()), k -> new HashSet());
      set.add(Integer.valueOf(npc.getId()));
      this.bossService.broadcastNpcShow(null, npc);
      SchedulerManager.getInstance().schedule(() -> clearBoosOfficerTimeOut(npc), 900000L);
      if (is && 
        role != null) {
        SysMapInfo map = SpringUtils.getMapService().getMap(object.getScene());
        if (map != null)
          SpringUtils.getChatService().sendLaba(MessageFormat.format("#<B>{0}#在挖宝时不小心打开了高僧封印妖魔的箱子，一群妖魔冲天而出,正在#<r:181,g:115,b:52>{1}#大肆荼害百姓!各路英雄快前往剿灭啊!", new Object[] { role.getName(), map.getName() })); 
      } 
      return true;
    } 
    return false;
  }
  
  public String getNpcContent(Role role, Npc npc) {
    setContent(npc, "  确定要进入与{0}的战斗吗？\n（推荐等级:{1})");
    return "确定/threats_seal|取消/离开";
  }
  
  public String getNpcContentNotFight(Role role, Npc npc) {
    if (npc.isInFight()) {
      setContent(npc, "  我正在战斗中，等下再来收拾你#<E:32>#");
      sendNpcContent(role, npc, "我要观战/guard|取消/离开");
      return "我要观战/guard|取消/离开";
    } 
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = null;
    if (this.teamService.isInTeam(role)) {
      names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 33));
    } else if (!SpringUtils.getActivityService().isLevel(role, 33)) {
      names = role.getName();
    } 
    if (names != null) {
      npc.setTalk(MessageFormat.format("{0}等级竟然不足33级#<E:76>#，还是练练再来吧#<E:109>#", new Object[] { "#<LB1>" + names + "#" }));
      return "";
    } 
    return null;
  }
  
  public void setContent(Npc npc, String talk) {
    SysDataSealDemon sealDemon = TaskDataPool.sealDemonList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (sealDemon != null)
      npc.setTalk(MessageFormat.format(talk, new Object[] { sealDemon.getName(), Integer.valueOf(sealDemon.getGrade()) })); 
  }
  
  public void doStartFight(Role role, Npc npc) {
    SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(Integer.valueOf(npc.getWarSerial()));
    if (warRole != null) {
      List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
      if (CollectionUtils.isEmpty(monsters)) {
        setNotInFight(npc);
        return;
      } 
      monsters.stream().forEach(monster -> {
            if (monster.getShape().intValue() <= 0)
              monster.setShape(Integer.valueOf(npc.getIcon())); 
            monster.setMonsterType(FIGHT_TYPE.SEAL_DEMON.getValue());
          });
      BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.SEAL_DEMON.getValue(), 0, npc.getId());
      Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
      if (fight == null)
        setNotInFight(npc); 
    } else {
      setNotInFight(npc);
    } 
  }
  
  protected Npc getBossNpc(int npcId) {
    return (Npc)DataCache.SEAL_DEMON_SET.get(Integer.valueOf(npcId));
  }
  
  public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
    super.doFightWinForSingle(role, fightEndEvent, npc);
    try {
      SpringUtils.getActivityService().addFinishCount(role, ActivityType.SEAL_OFFICER, 1);
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {
    clearStarOfficerAfterFightWin(bossNpc);
  }
  
  public void doFightFail(Role role, FightEndEvent fightEndEvent) {
    super.doFightFail(role, fightEndEvent);
  }
  
  public void giveReward(Role role, FightEndEvent fightEndEvent, Npc npc) {
    if (this.teamService.getTeam(role.getRoleId().intValue()) != null)
      if (!this.teamService.isInTeam(role)) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("离队无法获得奖励"));
        return;
      }  
    SysDataSealDemon object = TaskDataPool.sealDemonList.stream().filter(e -> (e.getId() == npc.getNs())).findFirst().orElse(null);
    if (object != null && SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.SEAL_OFFICER)) {
      this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.HERO_EXP), "", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), ItemClassify.PET_EXP), true);
      } 
      this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(calculate(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.RESTYPE_COIN)), "封妖", true, false);
      ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, object.getAward(), "封妖", 0);
    } 
  }
  
  private static long calculate(int grade, int rei, ItemClassify type) {
    if (ItemClassify.HERO_EXP == type)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((RandomUtil.round(Integer.valueOf(95), Integer.valueOf(105)) / 100) * 1.6E7D * (1.0D + (grade / 140) * 2.5D) / 30.0D / 10.0D)).longValue(); 
    if (ItemClassify.PET_EXP == type)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((RandomUtil.round(Integer.valueOf(95), Integer.valueOf(105)) / 100) * 1.2D * 1.6E7D * (1.0D + (grade / 140) * 2.5D) / 30.0D / 10.0D)).longValue(); 
    if (ItemClassify.RESTYPE_COIN == type)
      return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Integer.valueOf(RandomUtil.round(Integer.valueOf(95), Integer.valueOf(105)) / 100 * 20000)).longValue(); 
    return 0L;
  }
  
  private void clearBoosOfficerTimeOut(Npc npc) {
    if (npc == null)
      return; 
    if (DataCache.SEAL_DEMON_SET.get(Integer.valueOf(npc.getId())) == null)
      return; 
    if (npc.isInFight()) {
      this.bossService.broadcastNpcHide(null, npc);
    } else {
      clearStarOfficer(npc);
      DataCache.SEAL_DEMON_SET.remove(Integer.valueOf(npc.getId()));
    } 
  }
  
  private void clearStarOfficerAfterFightWin(Npc npc) {
    clearStarOfficer(npc);
  }
  
  private void clearStarOfficer(Npc npc) {
    if (npc == null)
      return; 
    DataCache.SEAL_DEMON_SET.remove(Integer.valueOf(npc.getId()));
    Set<Integer> set = (Set<Integer>)DataCache.SEAL_DEMON_MAP_NPC.get(Integer.valueOf(npc.getMap()));
    if (set != null) {
      set.remove(Integer.valueOf(npc.getId()));
      removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
      this.bossService.broadcastNpcHide(null, npc);
    } 
  }
  
  protected void clearNpcTimeOut(Npc npc) {
    clearBoosOfficerTimeOut(npc);
  }
}
