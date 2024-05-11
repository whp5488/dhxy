
package com.kitty.system.game.activity.handler.time;

import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.equip.EquipDistKind;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.common.model.Pos;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.Collection;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Random;
import com.kitty.system.game.team.domain.Team;
import java.text.MessageFormat;
import com.kitty.system.game.enter.Position;
import java.util.Iterator;
import java.util.List;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.map.model.GroupMapParam;
import java.util.HashSet;
import java.util.Set;
import com.kitty.system.game.vo.Npc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.boss.service.BossService;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class WorldBoosHandler extends FightActivityHandler
{
    private static final int NEED_LEVEL = 35;
    private static final int SHOW_MINUTE = 30;
    private static final String CONTENT = "就是来挑战你的/threats_boos|再见/离开";
    private static final String IN_FIGHT = "我要观战/guard|再见/离开";
    private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足35级#<E:76>#，还是练练再来吧#<E:109>#";
    private static final String TIPS = "{0}听过么，连我你都敢惹？";
    private static final String TIPS_NOT = "我正在战斗中，等下再来收拾你#<E:32>#";
    
    @Override
    protected int getFightType(final Role role) {
        return FIGHT_TYPE.SUPER_BOSS.getValue();
    }
    
    public void flushBoosAfterServerStart() {
        final List<SysDataWorldBoos> list = TaskDataPool.worldBoosList;
        for (final SysDataWorldBoos obj : list) {
            long count = DataCache.BOOS_OFFICER_SET.values().stream().filter(e -> e.getLevel() == obj.getLevel()).count();
            count = obj.getNum() - count;
            if (count > 0L) {
                for (int i = 0; i < count; ++i) {
                    final Position position = this.getRandomPosition(obj.getScene());
                    if (position != null) {
                        final int id = SpringUtils.getBean(BossService.class).getTempNpcId();
                        final Npc npc = new Npc(id, 0, obj.getScene(), position.getX(), position.getY(), obj.getIcon(), obj.getName(), obj.getTitle(), obj.getId(), obj.getLevel(), 0, 23);
                        npc.setEndTime(npc.getCreateTime() + 1800);
                        DataCache.BOOS_OFFICER_SET.put(npc.getId(), npc);
                        final Set<Integer> set = DataCache.BOOS_OFFICER_MAP_NPC.computeIfAbsent(Integer.valueOf(obj.getScene()), k -> new HashSet());
                        set.add(npc.getId());
                        this.bossService.broadcastNpcShow(null, npc);
                        SchedulerManager.getInstance().schedule(() -> this.clearBoosTimeOut(npc), 1800000L);
                    }
                }
            }
        }
    }
    
    public String getNpcContent(final Role role, final Npc npc) {
        this.setContent(npc, "{0}听过么，连我你都敢惹？");
        return "就是来挑战你的/threats_boos|再见/离开";
    }
    
    public String getNpcContentNotFight(final Role role, final Npc npc) {
        if (npc.isInFight()) {
            this.setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
            this.sendNpcContent(role, npc, "我要观战/guard|再见/离开");
            return "我要观战/guard|再见/离开";
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        String names = null;
        if (this.teamService.isInTeam(role)) {
            names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 35));
        }
        else if (!SpringUtils.getActivityService().isLevel(role, 35)) {
            names = role.getName();
        }
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}等级竟然不足35级#<E:76>#，还是练练再来吧#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        if (names != null) {
            return names;
        }
        return null;
    }
    
    public void setContent(final Npc npc, final String talk) {
        final SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            npc.setTalk(MessageFormat.format(talk, obj.getName()));
        }
    }
    
    public void doStartFight(final Role role, final Npc npc) {
        final SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            final Random random = new Random();
            final int index = random.nextInt(obj.getWar().size());
            final SysWarRole warRole = FightDataPool.data_war_role.get(obj.getWar().get(index));
            if (warRole != null) {
                final List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
                if (CollectionUtils.isEmpty((Collection)monsters)) {
                    this.setNotInFight(npc);
                    return;
                }
                monsters.stream().forEach(monster -> {
                    if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                        monster.setName(npc.getName());
                    }
                    else {
                        monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
                    }
                    monster.setMonsterType(FIGHT_TYPE.SUPER_BOSS.getValue());
                    return;
                });
                final BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.SUPER_BOSS.getValue(), 0, npc.getId());
                final Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
                if (fight == null) {
                    this.setNotInFight(npc);
                }
            }
            else {
                this.logger.error("世界BOOS获取战斗NPC错误，{}", (Object)npc.getId());
                this.setNotInFight(npc);
            }
        }
        else {
            this.setNotInFight(npc);
        }
    }
    
    @Override
    protected Npc getBossNpc(final int npcId) {
        return DataCache.BOOS_OFFICER_SET.get(npcId);
    }
    
    @Override
    public void doFightWinForSingle(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        try {
            SpringUtils.getActivityService().addFinishCount(role, ActivityType.WORLD_BOOS_OFFICER, 1);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void clearNpcAfterWin(final Role role, final FightEndEvent fightEndEvent, final Npc bossNpc) {
        this.clearStarOfficerAfterFightWin(bossNpc);
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
        long exp = BigDecimalUtil.decimalScaleDown(0, role.getExp() * 0.02).longValue();
        final long deduct = this.deduct(this.getBossNpc(fightEndEvent.getNpcId()));
        if (deduct > 0L) {
            exp = deduct;
        }
        final long finalExp = exp;
        this.teamService.memberHandleThreadLocal(role, memberRole -> SpringUtils.getRoleService().punishFightDead(memberRole, -finalExp, "由于你的死亡，你丢失了"));
        SpringUtils.getRoleService().goBackBorn(role, new Pos(107, 64, 5, 1002));
    }
    
    public long deduct(final Npc npc) {
        long exp = 0L;
        if (npc != null) {
            final SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
            final Param param = obj.getAward().stream().filter(e -> e.getK().equals(ItemClassify.HERO_EXP.getId()) && e.getT().equals(0)).findFirst().orElse(null);
            if (param != null) {
                exp = BigDecimalUtil.decimalScaleDown(0, param.getV() * 0.2).longValue();
            }
        }
        return exp;
    }
    
    public void giveReward(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        if (this.teamService.getTeam(role.getRoleId()) != null && !this.teamService.isInTeam(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("离队无法获得奖励"));
            return;
        }
        if (role.getLastDead() != 0) {
            final SysDataWorldBoos obj = TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
            if (obj != null && SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.WORLD_BOOS_OFFICER)) {
                SpringUtils.getBean(BagService.class).sendReward(role, obj.getAward(), "世界BOOS", obj.getLevel());
                this.specialReward(role, obj.getSpecial(), obj.getLevel(), obj.getName());
            }
        }
    }
    
    public void specialReward(final Role role, final List<Param> special, final int grade, final String cause) {
        if (special.size() <= 0) {
            return;
        }
        final int pos = SpringUtils.getBean(BagService.class).getPos(role, false);
        if (pos <= 0) {
            return;
        }
        final List<Lottery> list = new ArrayList<Lottery>();
        for (final Param param : special) {
            list.add(new Lottery(param.getK(), "", param.getR() / 1000.0, param.getV()));
        }
        final List<Double> rates = new ArrayList<Double>(list.size());
        for (final Lottery lottery : list) {
            rates.add(lottery.getBili());
        }
        final Lottery lottery2 = list.get(LotteryUtils.lottery(rates));
        if (lottery2.getNum() > 0) {
            final FiedEquip info = EquipDistKind.getInfo(lottery2.getSerial());
            if (info != null && !info.getType().equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue())) {
                final int serial = DataGlobalFunc.randomEquip(info.getType(), info.getValue(), role.getRace());
                if (serial > 0) {
                    final BagEquip equip = EquipManage.createEquip(serial);
                    if (equip != null) {
                        SpringUtils.getBean(EquipService.class).addEquip(role, equip, pos, 0, cause);
                        MessagePusher.notify2Lb(role, 214, role.getName(), cause, String.format("#<MI:%s,MP:%d,M:1,MT:%d,CI:%d}>%s#", equip.getId(), role.getRoleId(), equip.getType(), equip.getType(), DataGlobalFunc.getEquipDesc(serial, DataGlobalFunc.getPj(equip.getType()))), String.format("#<O>X%d#", 1));
                    }
                }
            }
            else {
                int serial = lottery2.getSerial();
                if (serial == EquipDistKind.SUIT.getNote()) {
                    serial = DataGlobalFunc.randomAcsryEgg(role.getRace());
                }
                SpringUtils.getBean(BagService.class).rewardItem(role, serial, lottery2.getNum(), 0, cause);
                final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(serial);
                if (itemEasyInfo != null) {
                    MessagePusher.notify2Lb(role, 214, role.getName(), cause, String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", itemEasyInfo.getSerial(), 0, 0, itemEasyInfo.getSerial(), itemEasyInfo.getSerial(), itemEasyInfo.getName()), String.format("#<ZWG3>X%d#", lottery2.getNum()));
                }
            }
        }
    }
    
    private void clearStarOfficerTimeOut(final Npc npc) {
        if (npc == null) {
            return;
        }
        if (DataCache.BOOS_OFFICER_SET.get(npc.getId()) == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearStarOfficer(npc);
            DataCache.BOOS_OFFICER_SET.remove(npc.getId());
        }
    }
    
    private void clearStarOfficerAfterFightWin(final Npc npc) {
        this.clearStarOfficer(npc);
    }
    
    private void clearBoosTimeOut(final Npc npc) {
        if (npc == null) {
            return;
        }
        if (DataCache.BOOS_OFFICER_SET.get(npc.getId()) == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearStarOfficer(npc);
        }
    }
    
    private void clearStarOfficer(final Npc npc) {
        if (npc == null) {
            return;
        }
        DataCache.BOOS_OFFICER_SET.remove(npc.getId());
        final Set<Integer> set = DataCache.BOOS_OFFICER_MAP_NPC.get(npc.getMap());
        if (set != null) {
            set.remove(npc.getId());
            this.removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
            this.bossService.broadcastNpcHide(null, npc);
        }
    }
    
    @Override
    protected void clearNpcTimeOut(final Npc npc) {
        this.clearStarOfficerTimeOut(npc);
    }
}
