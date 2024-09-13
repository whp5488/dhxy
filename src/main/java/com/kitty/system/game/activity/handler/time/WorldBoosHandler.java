package com.kitty.system.game.activity.handler.time;

import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.equip.EquipDistKind;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
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
    
    public WorldBoosHandler() {
    }
    
    @Override
    protected int getFightType(Role role) {
        return FIGHT_TYPE.SUPER_BOSS.getValue();
    }
    
    public void flushBoosAfterServerStart() {
        List<SysDataWorldBoos> list = TaskDataPool.worldBoosList;
        for (SysDataWorldBoos obj : list) {
            long count = DataCache.BOOS_OFFICER_SET.values().stream().filter(e -> e.getLevel() == obj.getLevel()).count();
            count = obj.getNum() - count;
            if (count > 0L) {
                for (int i = 0; (long)i < count; ++i) {
                    Position position = this.getRandomPosition(obj.getScene());
                    if (position != null) {
                        int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
                        Npc npc = new Npc(id, 0, obj.getScene(), (int)position.getX(), (int)position.getY(), obj.getIcon(), obj.getName(), obj.getTitle(), obj.getId(), obj.getLevel(), 0, 23);
                        npc.setEndTime(npc.getCreateTime() + 1800);
                        DataCache.BOOS_OFFICER_SET.put(Integer.valueOf(npc.getId()), npc);
                        Set<Integer> set = (Set)DataCache.BOOS_OFFICER_MAP_NPC.computeIfAbsent(Integer.valueOf(obj.getScene()), k -> new HashSet());
                        set.add(Integer.valueOf(npc.getId()));
                        this.bossService.broadcastNpcShow(null, npc);
                        SchedulerManager.getInstance().schedule(() -> this.clearBoosTimeOut(npc), 1800000L);
                    }
                }
            }
            else {
                continue;
            }
        }
    }
    
    public String getNpcContent(Role role, Npc npc) {
        this.setContent(npc, "{0}听过么，连我你都敢惹？");
        return "就是来挑战你的/threats_boos|再见/离开";
    }
    
    public String getNpcContentNotFight(Role role, Npc npc) {
        if (npc.isInFight()) {
            this.setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
            this.sendNpcContent(role, npc, "我要观战/guard|再见/离开");
            return "我要观战/guard|再见/离开";
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
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
    
    public void setContent(Npc npc, String talk) {
        SysDataWorldBoos obj = (SysDataWorldBoos)TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            npc.setTalk(MessageFormat.format(talk, obj.getName()));
        }
    }
    
    public void doStartFight(Role role, Npc npc) {
        SysDataWorldBoos obj = (SysDataWorldBoos)TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            Random random = new Random();
            int index = random.nextInt(obj.getWar().size());
            SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(obj.getWar().get(index));
            if (warRole != null) {
                List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
                if (CollectionUtils.isEmpty(monsters)) {
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
                    if (warRole.getSerial() == 181816){
                        monster.setName(DataGlobalFunc.dynamicName("RandomRobberName"));
                        double rate = (warRole.getSerial() - 181808) * 4;
                        monster.setHp((int) (monster.getHp() * rate));
                        monster.setSp((int) (monster.getSp() * rate));
                        monster.setAp((int) (monster.getAp() * rate));
                        monster.setPAcc(monster.getPAcc() * rate);
                        monster.setPSbl(monster.getPSbl() * rate);
                        monster.setPAcc(monster.getPAcc() * rate);
                        monster.setPSbl(monster.getPSbl() * rate);
                        monster.setPLjPro(monster.getPLjPro() * rate);
                        monster.setPKuangBao(monster.getPKuangBao() * rate);
                        monster.setPPofang(monster.getPPofang() * rate);
                        monster.setPPofangPro(monster.getPPofangPro() * rate);
                        monster.setPFanZhen(monster.getPFanZhen() * rate);
                        monster.setPFanZhenPro(monster.getPFanZhenPro() * rate);
                        monster.setPdeFend(monster.getPdeFend() * rate);
                        monster.setKHunLuan(monster.getKHunLuan() * rate);
                        monster.setKHunShui(monster.getKHunShui() * rate);
                        monster.setKFengYin(monster.getKFengYin() * rate);
                        monster.setKZhongDu(monster.getKZhongDu() * rate);
                        monster.setKFeng(monster.getKFeng() * rate);
                        monster.setKLei(monster.getKLei() * rate);
                        monster.setKShui(monster.getKShui() * rate);
                        monster.setKHuo(monster.getKHuo() * rate);
                        monster.setKZhenShe(monster.getKZhenShe() * rate);
                        monster.setKGuiHuo(monster.getKGuiHuo() * rate);
                        monster.setKSanShi(monster.getKSanShi() * rate);
                        monster.setKYiWang(monster.getKYiWang() * rate);
                        monster.setFkHunLuan(monster.getFkHunLuan() * rate);
                        monster.setFkHunShui(monster.getFkHunShui() * rate);
                        monster.setFkFengYin(monster.getFkFengYin() * rate);
                        monster.setFkZhongDu(monster.getFkZhongDu() * rate);
                        monster.setFkFeng(monster.getFkFeng() * rate);
                        monster.setFkLei(monster.getFkLei() * rate);
                        monster.setFkShui(monster.getFkShui() * rate);
                        monster.setFkHuo(monster.getFkHuo() * rate);
                        monster.setFkZhenShe(monster.getFkZhenShe() * rate);
                        monster.setFkSanShi(monster.getFkSanShi() * rate);
                        monster.setFkGuiHuo(monster.getFkGuiHuo() * rate);
                        monster.setFkYiWang(monster.getFkYiWang() * rate);
                        monster.setDisorderEhan(monster.getDisorderEhan() * rate);
                        monster.setSleepEhan(monster.getSleepEhan() * rate);
                        monster.setSealEhan(monster.getSealEhan() * rate);
                        monster.setPoisonEhan(monster.getPoisonEhan() * rate);
                        monster.setWindEhan(monster.getWindEhan() * rate);
                        monster.setThunderEhan(monster.getThunderEhan() * rate);
                        monster.setRainEhan(monster.getRainEhan() * rate);
                        monster.setFireEhan(monster.getFireEhan() * rate);
                        monster.setAdAtkEhan(monster.getAdAtkEhan() * rate);
                        monster.setAdSpdEhan(monster.getAdSpdEhan() * rate);
                        monster.setAdDefEhan(monster.getAdDefEhan() * rate);
                        monster.setShockEhan(monster.getShockEhan() * rate);
                        monster.setCharmEhan(monster.getCharmEhan() * rate);
                        monster.setSanshiEhan(monster.getSanshiEhan() * rate);
                        monster.setGfireEhan(monster.getGfireEhan() * rate);
                        monster.setForgetEhan(monster.getForgetEhan() * rate);
                        monster.setBreakEhan(monster.getBreakEhan() * rate);
                        monster.setThudEhan(monster.getThudEhan() * rate);
                        monster.setCureEhan(monster.getCureEhan() * rate);
                        monster.setSweepEhan(monster.getSweepEhan() * rate);
                        monster.setFengOdd(monster.getFengOdd() * rate);
                        monster.setLeiOdd(monster.getLeiOdd() * rate);
                        monster.setShuiOdd(monster.getShuiOdd() * rate);
                        monster.setHuoOdd(monster.getHuoOdd() * rate);
                        monster.setSanShiOdd(monster.getSanShiOdd() * rate);
                        monster.setGuiHuoOdd(monster.getGuiHuoOdd() * rate);
                        monster.setConGold(monster.getConGold() * rate);
                        monster.setConWood(monster.getConWood() * rate);
                        monster.setConWater(monster.getConWater() * rate);
                        monster.setConFire(monster.getConFire() * rate);
                        monster.setConSoil(monster.getConSoil() * rate);
                        monster.setMonsterType(FIGHT_TYPE.SUPER_BOSS.getValue());
                        DataGlobalFunc.createMonster(4240, monster);
                    }
                    monster.setMonsterType(FIGHT_TYPE.SUPER_BOSS.getValue());
                    return;
                });
                BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.SUPER_BOSS.getValue(), 0, npc.getId());
                Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
                if (fight == null) {
                    this.setNotInFight(npc);
                }
            }
            else {
                this.logger.error("世界BOOS获取战斗NPC错误，{}", Integer.valueOf(npc.getId()));
                this.setNotInFight(npc);
            }
        }
        else {
            this.setNotInFight(npc);
        }
    }
    
    @Override
    protected Npc getBossNpc(int npcId) {
        return (Npc)DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npcId));
    }
    
    @Override
    public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        try {
            SpringUtils.getActivityService().addFinishCount(role, ActivityType.WORLD_BOOS_OFFICER, 1);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {
        this.clearStarOfficerAfterFightWin(bossNpc);
    }
    
    @Override
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
        long exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((double)role.getExp() * 0.02)).longValue();
        long deduct = this.deduct(this.getBossNpc(fightEndEvent.getNpcId()));
        if (deduct > 0L) {
            exp = deduct;
        }
        long finalExp = exp;
        this.teamService.memberHandleThreadLocal(role, memberRole -> SpringUtils.getRoleService().punishFightDead(memberRole, -finalExp, "由于你的死亡，你丢失了"));
        SpringUtils.getRoleService().goBackBorn(role, new Pos(Integer.valueOf(107), Integer.valueOf(64), Integer.valueOf(5), Integer.valueOf(1002)));
    }
    
    public long deduct(Npc npc) {
        long exp = 0L;
        if (npc != null) {
            SysDataWorldBoos obj = (SysDataWorldBoos)TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
            Param param = (Param)obj.getAward().stream().filter(e -> e.getK().equals(Integer.valueOf(ItemClassify.HERO_EXP.getId())) && e.getT().equals(Integer.valueOf(0))).findFirst().orElse(null);
            if (param != null) {
                exp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((double)(int)param.getV() * 0.2)).longValue();
            }
        }
        return exp;
    }
    
    public void giveReward(Role role, FightEndEvent fightEndEvent, Npc npc) {
        if (this.teamService.getTeam((int)role.getRoleId()) != null && !this.teamService.isInTeam(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("离队无法获得奖励"));
            return;
        }
        if (role.getLastDead() != 0) {
            SysDataWorldBoos obj = (SysDataWorldBoos)TaskDataPool.worldBoosList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
            if (obj != null && SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.WORLD_BOOS_OFFICER)) {
                ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, obj.getAward(), "世界BOOS", obj.getLevel());
                this.specialReward(role, obj.getSpecial(), obj.getLevel(), obj.getName());
            }
        }
        return;
    }
    
    public void specialReward(Role role, List<Param> special, int grade, String cause) {
        if (special.size() <= 0) {
            return;
        }
        int pos = ((BagService)SpringUtils.getBean(BagService.class)).getPos(role, false);
        if (pos <= 0) {
            return;
        }
        List<Lottery> list = new ArrayList<>();
        for (Param param : special) {
            list.add(new Lottery(param.getK(), "", Double.valueOf((double)(int)param.getR() / 1000.0), (int)param.getV()));
        }
        List<Double> rates = new ArrayList(list.size());
        for (Lottery lottery : list) {
            rates.add(lottery.getBili());
        }
        Lottery lottery2 = (Lottery)list.get(LotteryUtils.lottery(rates));
        if (lottery2.getNum() > 0) {
            FiedEquip info = EquipDistKind.getInfo(lottery2.getSerial());
            if (info != null && !info.getType().equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()))) {
                int serial = DataGlobalFunc.randomEquip((int)info.getType(), (int)info.getValue(), (int)role.getRace());
                if (serial > 0) {
                    BagEquip equip = EquipManage.createEquip(Integer.valueOf(serial));
                    if (equip != null) {
                        ((EquipService)SpringUtils.getBean(EquipService.class)).addEquip(role, equip, pos, 0, cause);
                        MessagePusher.notify2Lb(role, 214, role.getName(), cause, String.format("#<MI:%s,MP:%d,M:1,MT:%d,CI:%d}>%s#", equip.getId(), role.getRoleId(), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), DataGlobalFunc.getEquipDesc(serial, DataGlobalFunc.getPj(equip.getType()))), String.format("#<O>X%d#", Integer.valueOf(1)));
                    }
                }
            }
            else {
                int serial = (int)lottery2.getSerial();
                if (serial == EquipDistKind.SUIT.getNote()) {
                    serial = DataGlobalFunc.randomAcsryEgg((int)role.getRace());
                }
                ((BagService)SpringUtils.getBean(BagService.class)).rewardItem(role, serial, lottery2.getNum(), 0, cause);
                ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
                if (itemEasyInfo != null) {
                    MessagePusher.notify2Lb(role, 214, role.getName(), cause, String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", itemEasyInfo.getSerial(), Integer.valueOf(0), Integer.valueOf(0), itemEasyInfo.getSerial(), itemEasyInfo.getSerial(), itemEasyInfo.getName()), String.format("#<ZWG3>X%d#", Integer.valueOf(lottery2.getNum())));
                }
            }
        }
        return;
    }
    
    private void clearStarOfficerTimeOut(Npc npc) {
        if (npc == null) {
            return;
        }
        if (DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npc.getId())) == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearStarOfficer(npc);
            DataCache.BOOS_OFFICER_SET.remove(Integer.valueOf(npc.getId()));
        }
        return;
    }
    
    private void clearStarOfficerAfterFightWin(Npc npc) {
        this.clearStarOfficer(npc);
    }
    
    private void clearBoosTimeOut(Npc npc) {
        if (npc == null) {
            return;
        }
        if (DataCache.BOOS_OFFICER_SET.get(Integer.valueOf(npc.getId())) == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearStarOfficer(npc);
        }
        return;
    }
    
    private void clearStarOfficer(Npc npc) {
        if (npc == null) {
            return;
        }
        DataCache.BOOS_OFFICER_SET.remove(Integer.valueOf(npc.getId()));
        Set<Integer> set = (Set)DataCache.BOOS_OFFICER_MAP_NPC.get(Integer.valueOf(npc.getMap()));
        if (set != null) {
            set.remove(Integer.valueOf(npc.getId()));
            this.removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
            this.bossService.broadcastNpcHide(null, npc);
        }
        return;
    }
    
    @Override
    protected void clearNpcTimeOut(Npc npc) {
        this.clearStarOfficerTimeOut(npc);
    }
}
