package com.kitty.system.game.activity.handler.time;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysWarRole;
import java.text.MessageFormat;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class BurningHotHandler extends FightActivityHandler {
  private static final int NEED_LEVEL = 60;
  
  private static final int SERIAL = 651005;
  
  private static final String CONTENT = "休得撒野/threats_har|我只是路过/离开";
  
  private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足60级#<E:76>#，还是练练再来吧#<E:109>#";
  
  private static final String TIPS = "你是猴子派来的救兵吗？";
  
  protected int getFightType(Role role) {
    return FIGHT_TYPE.BURNING_HOT.getValue();
  }
  
  public String getNpcContent(Role role, Npc npc) {
    setContent(npc, "你是猴子派来的救兵吗？");
    return "休得撒野/threats_har|我只是路过/离开";
  }
  
  public String getNpcContentNotFight(Role role, Npc npc) {
    Team team = this.teamService.getTeam(role.getRoleId().intValue());
    String names = null;
    if (this.teamService.isInTeam(role)) {
      names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 60));
    } else if (!SpringUtils.getActivityService().isLevel(role, 60)) {
      names = role.getName();
    } 
    if (names != null) {
      npc.setTalk(MessageFormat.format("{0}等级竟然不足60级#<E:76>#，还是练练再来吧#<E:109>#", new Object[] { "#<LB1>" + names + "#" }));
      return "";
    } 
    if (names != null)
      return names; 
    return null;
  }
  
  public void setContent(Npc npc, String talk) {
    npc.setTalk(MessageFormat.format(talk, new Object[] { npc.getName() }));
  }
  
  public void doStartFight(Role role, Npc npc) {
    SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(Integer.valueOf(651005));
    if (warRole != null) {
      List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
      if (CollectionUtils.isEmpty(monsters))
        return; 
      monsters.stream().forEach(monster -> {
            if (monster.getPos() == 10103 || monster.getPos() == 10003) {
              monster.setName(npc.getName());
            } else {
              monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
            } 
            monster.setMonsterType(FIGHT_TYPE.BURNING_HOT.getValue());
          });
      BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.BURNING_HOT.getValue(), 0, npc.getId());
      this.bossService.startFightToBoss(role, bossFightParam);
    } 
  }
  
  protected Npc getBossNpc(int npcId) {
    SysNpcInfo sysNpcInfo = (SysNpcInfo)DataCache.ID_NPC.get(Integer.valueOf(npcId));
    return new Npc(sysNpcInfo);
  }
  
  public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
    super.doFightWinForSingle(role, fightEndEvent, npc);
    SpringUtils.getActivityService().addFinishCount(role, ActivityType.BURNING_HOT, 1);
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
    if (SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.BURNING_HOT)) {
      this.roleService.addExp(role, calculate(role.getLevel().intValue(), role.getRelive().intValue(), 2, ItemClassify.HERO_EXP), "红孩儿", true);
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
        if (pet != null)
          this.petService.addExp(role, pet, calculate(pet.getPetLv().intValue(), pet.getRebirth().intValue(), 2, ItemClassify.PET_EXP), true); 
      } 
      specialReward(role);
    } 
  }
  
  public void specialReward(Role role) {
    BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
    List<Item> list = bagService.taskRewardDynamic("hhr");
    for (Item item : list) {
      bagService.rewardItem(role, item.getItem().intValue(), item.getNum().intValue(), 0, "击杀红孩儿");
      if (item.getOthers().intValue() > 0) {
        ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(item.getItem());
        if (itemEasyInfo != null)
          MessagePusher.notify2Lb(role, 213, new Object[] { role.getName(), String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { itemEasyInfo.getSerial(), Integer.valueOf(0), Integer.valueOf(0), itemEasyInfo.getSerial(), itemEasyInfo.getSerial(), itemEasyInfo.getName() }), String.format("#<ZWG3>X%d#", new Object[] { item.getNum() }) }); 
      } 
    } 
  }
  
  private static long calculate(int grade, int rei, int cnt, ItemClassify type) {
    double fold = ActivityType.BURNING_HOT.getFold();
    double exp = 0.0D;
    if (type == ItemClassify.HERO_EXP) {
      exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 28.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D;
    } else if (type == ItemClassify.PET_EXP) {
      exp = 1.6E7D * (1.0D + (grade + rei * 15.0D) / 140.0D * 2.5D) / 30.0D / 28.13D * (0.2D + 0.1D * cnt + Math.pow(cnt, 0.45D)) * 1.5D * 1.2D;
    } 
    return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0D) ? (exp * fold) : exp)).longValue();
  }
  
  public boolean isSetInFight() {
    return false;
  }
  
  protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {}
  
  protected void clearNpcTimeOut(Npc npc) {}
}
