
package com.kitty.system.game.activity.handler.time;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.boss.model.BossFightParam;
import java.util.Collections;
import java.util.Collection;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Random;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.party.model.PartyMember;
import java.text.MessageFormat;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.RandomUtil;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.enter.Position;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import com.kitty.common.core.SchedulerManager;
import java.io.Serializable;
import com.kitty.system.game.map.model.GroupMapParam;
import com.kitty.system.game.vo.Npc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class PartyBanditHandler extends FightActivityHandler
{
    private static final String DIST = "bandit";
    private static final double PROB = 0.45;
    private static final int[] BANDIT;
    private static final int MIN_TEAM_COUNT = 3;
    private static final int MAX_BANDIT_COUNT = 15;
    private static final int SHOW_MINUTE = 20;
    private static final String CONTENT = "为帮除害/threats_p_bandit|再见/离开";
    private static final String IN_FIGHT = "再见/离开";
    private static final String TEAM_NOT_PARTY = "{0}不是本帮成员哦！#<E:109>#";
    private static final String TIPS = "俺们老大说了：给你们20分钟时间，如果能把俺们兄弟都打到，就饶了你们帮派不抢。出手吧#<E:32>#";
    private static final String TIPS_NOT = "我正在战斗中，等下再来收拾你#<E:32>#";
    private static final String TEAM_COUNT_NOT_ENOUGH = "边凉快去，不够3人的队伍，大爷懒得出手#<E:0>#";
    private static final String FIGHT_END = "{0}的队伍三拳两脚就消灭了一队强盗#<E:81>#，{1}";
    private static final String JOIN_PARTY_TIME = "{0}进入帮派小于24小时，也敢来多管闲事！#<E:109>#";
    
    @Override
    protected int getFightType(final Role role) {
        return FIGHT_TYPE.PARTY_BANDIT.getValue();
    }
    
    public void flushAfterServerStart() {
        final List<SysDataPartyBandit> partyBanditList = TaskDataPool.partyBanditList;
        final Map<Integer, List<SysDataPartyBandit>> banditGradingMap = this.getBanditGrading(partyBanditList);
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        for (final Integer key : PartyDataPool.partyMap.keySet()) {
            final Party party = PartyDataPool.partyMap.get(key);
            if (party == null) {
                this.logger.error("刷新帮派强盗帮派信息为空");
            }
            else {
                final int day = BigDecimalUtil.decimalScaleBig(0, (time - party.getCreateTime()) / 60 / 60 / 24).intValue();
                if (day < 3) {
                    continue;
                }
                if (!SkillAi.canAffectOnPro(0.45)) {
                    continue;
                }
                int count = 0;
                final int scene = PartyDataPool.getScene(party.getLevel());
                final List<SysDataPartyBandit> creationBandit = this.getCreationBandit(banditGradingMap, party);
                for (final SysDataPartyBandit obj : creationBandit) {
                    final Position position = this.getRandomPosition(scene);
                    if (position != null) {
                        final int id = SpringUtils.getBean(BossService.class).getTempNpcId();
                        final Npc npc = new Npc(id, 0, scene, position.getX(), position.getY(), obj.getIcon(), obj.getName(), "", obj.getId(), obj.getLevel(), 0, 24);
                        npc.setVisible(party.getSerial());
                        npc.setEndTime(npc.getCreateTime() + 1200);
                        final GroupMapParam groupMapParam = new GroupMapParam(party.getSerial());
                        this.mapService.addGroupMapNpc(groupMapParam, npc);
                        this.bossService.broadcastNpcShow(groupMapParam, npc);
                        SchedulerManager.getInstance().schedule(() -> this.clearNpcTimeOut(groupMapParam, npc), 1200000L);
                        ++count;
                    }
                }
                if (count <= 0) {
                    continue;
                }
                party.getNutMap().setv("num", count);
                party.getNutMap().setv("bandit", count);
                final ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> this.partyNpcTips(party.getSerial()), 1200000L);
                party.setSchedule(schedule);
                this.partyService.npcPartyChat(party, "出大事了#<E:24>#！一大波强盗入侵我帮闹事，大家快快回帮将他们剿灭啊#<E:52>#。");
            }
        }
    }
    
    public void partyNpcTips(final int serial) {
        final Party party = this.partyService.getParty(serial);
        final int num = party.getNutMap().getInt("bandit", 0);
        if (party.getNutMap().getInt("bandit", 0) > 0) {
            this.partyService.npcPartyChat(party, "20分钟内没有将帮派强盗全部消灭，强盗看着也没什么可抢的，就默默撤离了");
            if (party.getCapital() > 0L) {
                final long construct = party.getCapital() - (int)(this.partyService.getPartyMaxMembers(party.getLevel()) * 2.01467) * num;
                party.setCapital((construct < 0L) ? 0L : construct);
                party.save();
            }
        }
    }
    
    public Map<Integer, List<SysDataPartyBandit>> getBanditGrading(final List<SysDataPartyBandit> partyBanditList) {
        final Map<Integer, List<SysDataPartyBandit>> map = new HashMap<Integer, List<SysDataPartyBandit>>(4);
        map.put(1, partyBanditList.stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList()));
        map.put(2, partyBanditList.stream().filter(e -> e.getLevel() == 2).collect(Collectors.toList()));
        map.put(3, partyBanditList.stream().filter(e -> e.getLevel() == 3).collect(Collectors.toList()));
        map.put(4, partyBanditList.stream().filter(e -> e.getLevel() == 4).collect(Collectors.toList()));
        return map;
    }
    
    public List<SysDataPartyBandit> getCreationBandit(final Map<Integer, List<SysDataPartyBandit>> banditGradingMap, final Party party) {
        final List<SysDataPartyBandit> list = new ArrayList<SysDataPartyBandit>(15);
        final short onlineCount = this.partyService.getPartyOnlineCount(party);
        int count = BigDecimalUtil.decimalScaleInteger(0, onlineCount * 0.75 * (party.getActive() / 100.0) + party.getLevel() * 0.5);
        if (count > 15) {
            count = 15;
        }
        if (count >= 8) {
            List<SysDataPartyBandit> collect = banditGradingMap.get(4);
            int index = RandomUtil.round(0, collect.size() - 1);
            list.add(collect.get(index));
            collect = banditGradingMap.get(3);
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(0, collect.size() - 1);
                list.add(collect.get(index));
            }
            collect = banditGradingMap.get(2);
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(0, collect.size() - 1);
                list.add(collect.get(index));
            }
            collect = banditGradingMap.get(1);
            for (int i = 0; i < 3; ++i) {
                index = RandomUtil.round(0, collect.size() - 1);
                list.add(collect.get(index));
            }
        }
        else if (count >= 6) {
            List<SysDataPartyBandit> collect = banditGradingMap.get(3);
            int index = RandomUtil.round(0, collect.size() - 1);
            list.add(collect.get(index));
            collect = banditGradingMap.get(2);
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(0, collect.size() - 1);
                list.add(collect.get(index));
            }
            collect = banditGradingMap.get(4);
            for (int i = 0; i < 3; ++i) {
                index = RandomUtil.round(0, collect.size() - 1);
                list.add(collect.get(index));
            }
        }
        else {
            count = 0;
        }
        count -= list.size();
        if (count > 0) {
            for (int j = 0; j < count; ++j) {
                final List<SysDataPartyBandit> collect2 = banditGradingMap.get(PartyBanditHandler.BANDIT[RandomUtil.round(0, PartyBanditHandler.BANDIT.length - 1)]);
                if (collect2 != null && collect2.size() > 0) {
                    list.add(collect2.get(RandomUtil.round(0, collect2.size() - 1)));
                }
            }
        }
        return list;
    }
    
    public String getNpcContent(final Role role, final Npc npc) {
        this.setContent(npc, "俺们老大说了：给你们20分钟时间，如果能把俺们兄弟都打到，就饶了你们帮派不抢。出手吧#<E:32>#");
        return "为帮除害/threats_p_bandit|再见/离开";
    }
    
    public String getNpcContentNotFight(final Role role, final Npc npc) {
        if (npc.isInFight()) {
            this.setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
            this.sendNpcContent(role, npc, "再见/离开");
            return "再见/离开";
        }
        final Party party = this.partyService.getParty(role);
        if (party == null) {
            return "";
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team == null || this.teamService.getTeamCount(team) < 3) {
            npc.setTalk("边凉快去，不够3人的队伍，大爷懒得出手#<E:0>#");
            return "";
        }
        String names = this.teamService.checkMember(team, memberRole -> !DataGlobalFunc.isTheSameParty(role.getFaction(), memberRole.getFaction()));
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}不是本帮成员哦！#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        names = this.teamService.checkMember(team, memberRole -> !this.isJoinPartyTime24(party.getMembers().get(memberRole.getRoleId()), time));
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}进入帮派小于24小时，也敢来多管闲事！#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        return null;
    }
    
    public boolean isJoinPartyTime24(final PartyMember member, final int time) {
        if (member == null || time <= 0) {
            return false;
        }
        final int day = BigDecimalUtil.decimalScaleBig(0, (time - member.getJoinTime()) / 60 / 60 / 24).intValue();
        return day >= 1;
    }
    
    public void setContent(final Npc npc, final String talk) {
        final SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            npc.setTalk(MessageFormat.format(talk, obj.getLevel(), obj.getGrade(), obj.getName()));
        }
    }
    
    public void doStartFight(final Role role, final Npc npc) {
        final SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            final Random random = new Random();
            final int index = random.nextInt(obj.getWar().size());
            final SysWarRole warRole = FightDataPool.data_war_role.get(obj.getWar().get(index));
            if (warRole != null) {
                final List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
                if (CollectionUtils.isEmpty((Collection)monsters)) {
                    this.logger.error("帮派强盗进入战斗错误1");
                    this.setNotInFight(npc);
                    return;
                }
                final Integer monsterLevel = Collections.max((Collection<? extends Integer>)this.teamService.getMemberLevel(role));
                monsters.stream().forEach(monster -> {
                    if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                        monster.setName(npc.getName());
                    }
                    else {
                        monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
                    }
                    monster.setMonsterType(FIGHT_TYPE.PARTY_BANDIT.getValue());
                    DataGlobalFunc.createMonster(monsterLevel, monster);
                    return;
                });
                final BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.PARTY_BANDIT.getValue(), 0, npc.getId());
                final Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
                if (fight == null) {
                    this.setNotInFight(npc);
                }
            }
            else {
                this.logger.error("帮派强盗进入战斗错误2");
                this.setNotInFight(npc);
            }
        }
        else {
            this.logger.error("帮派强盗进入战斗错误3");
            this.setNotInFight(npc);
        }
    }
    
    @Override
    protected Npc getBossNpc(final int npcId) {
        return DataCache.GROUP_NPC.get(npcId);
    }
    
    @Override
    public void doFightWinForSingle(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        SpringUtils.getActivityService().addFinishCount(role, ActivityType.FACTION_BANDIT, 1);
    }
    
    @Override
    protected void clearNpcAfterWin(final Role role, final FightEndEvent fightEndEvent, final Npc bossNpc) {
        this.clearNpcAfterFightWin(bossNpc, fightEndEvent);
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public void giveReward(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        final SysDataPartyBandit obj = TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            if (SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.FACTION_BANDIT)) {
                this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), npc.getLevel(), ItemClassify.HERO_EXP), "帮派强盗", true);
                final Integer combatPet = role.getExtendBox().getCombatPet();
                if (combatPet > 0) {
                    final Pet pet = this.petService.selectPet(role).get(combatPet);
                    if (pet != null) {
                        this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), npc.getLevel(), ItemClassify.PET_EXP), true);
                    }
                }
                SpringUtils.getBean(BagService.class).sendReward(role, obj.getAward(), "帮派强盗", obj.getLevel());
            }
            else {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("帮派强盗次数已满，无法获得奖励"));
            }
        }
        else {
            this.logger.error("获取帮派强盗奖励失败：{}", (Object)npc.getNs());
        }
    }
    
    private void clearNpcAfterFightWin(final Npc npc, final FightEndEvent fightEndEvent) {
        this.clearNpc(npc);
        final Party party = this.partyService.getParty(npc.getVisible());
        if (party != null) {
            int num = party.getNutMap().getInt("bandit", 0);
            final List<Role> rolesA = fightEndEvent.getRolesA();
            --num;
            party.getNutMap().setv("bandit", num);
            String msg = "现在还有" + num + "队强盗没有被消灭";
            if (num <= 0) {
                msg = "入侵帮派的强盗已经全部被消灭#<E:90>#。";
                final ScheduledFuture<?> task = party.getSchedule();
                if (task != null && !task.isDone()) {
                    task.cancel(Boolean.FALSE);
                }
                final int bot = num + (int)(this.partyService.getPartyMaxMembers(party.getLevel()) * 2.25);
                this.partyService.addConstruction(party, bot);
            }
            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < rolesA.size(); ++i) {
                final Role role = rolesA.get(i);
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("、");
                }
                stringBuilder.append(role.getName());
            }
            this.partyService.npcPartyChat(party, MessageFormat.format("{0}的队伍三拳两脚就消灭了一队强盗#<E:81>#，{1}", "#<LB1>" + stringBuilder.toString() + "#", msg));
        }
    }
    
    private void clearNpcTimeOut(final GroupMapParam groupMapParam, final Npc npc) {
        if (npc == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearNpc(npc);
            this.mapService.removeGroupMapNpc(groupMapParam, npc);
        }
    }
    
    private void clearNpc(final Npc npc) {
        if (npc == null) {
            return;
        }
        this.mapService.removeGroupMapNpc(new GroupMapParam(npc.getVisible()), npc);
        this.removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
        this.bossService.broadcastNpcHide(new GroupMapParam(npc.getVisible()), npc);
    }
    
    @Override
    protected void clearNpcTimeOut(final Npc npc) {
        this.clearNpcTimeOut(npc);
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final ItemClassify type) {
        if (type == ItemClassify.HERO_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.9E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 27.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5).longValue();
        }
        if (type == ItemClassify.PET_EXP) {
            return BigDecimalUtil.decimalScaleDown(0, 1.9E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 27.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5 * 1.2).longValue();
        }
        return 0L;
    }
    
    static {
        BANDIT = new int[] { 1, 2, 3, 4 };
    }
}
