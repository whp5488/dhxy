package com.kitty.system.game.activity.handler.time;

import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.boss.model.BossFightParam;
import java.util.Collections;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import java.util.Random;
import com.kitty.system.game.team.domain.Team;
import java.text.MessageFormat;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.RandomUtil;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.enter.Position;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import com.kitty.common.core.SchedulerManager;
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
    
    public PartyBanditHandler() {
    }
    
    @Override
    protected int getFightType(Role role) {
        return FIGHT_TYPE.PARTY_BANDIT.getValue();
    }
    
    public void flushAfterServerStart() {
        List<SysDataPartyBandit> partyBanditList = TaskDataPool.partyBanditList;
        Map<Integer, List<SysDataPartyBandit>> banditGradingMap = this.getBanditGrading(partyBanditList);
        int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        for (Integer key : PartyDataPool.partyMap.keySet()) {
            Party party = (Party)PartyDataPool.partyMap.get(key);
            if (party == null) {
                this.logger.error("刷新帮派强盗帮派信息为空");
            }
            else {
                int day = BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Integer.valueOf((time - party.getCreateTime()) / 60 / 60 / 24)).intValue();
                if (day < 3) {
                    continue;
                }
                if (!SkillAi.canAffectOnPro(0.45)) {
                    continue;
                }
                int count = 0;
                int scene = PartyDataPool.getScene(party.getLevel());
                List<SysDataPartyBandit> creationBandit = this.getCreationBandit(banditGradingMap, party);
                for (SysDataPartyBandit obj : creationBandit) {
                    Position position = this.getRandomPosition(scene);
                    if (position != null) {
                        int id = ((BossService)SpringUtils.getBean(BossService.class)).getTempNpcId();
                        Npc npc = new Npc(id, 0, scene, (int)position.getX(), (int)position.getY(), obj.getIcon(), obj.getName(), "", obj.getId(), obj.getLevel(), 0, 24);
                        npc.setVisible((int)party.getSerial());
                        npc.setEndTime(npc.getCreateTime() + 1200);
                        GroupMapParam groupMapParam = new GroupMapParam(party.getSerial());
                        this.mapService.addGroupMapNpc(groupMapParam, npc);
                        this.bossService.broadcastNpcShow(groupMapParam, npc);
                        SchedulerManager.getInstance().schedule(() -> this.clearNpcTimeOut(groupMapParam, npc), 1200000L);
                        ++count;
                    }
                    else {
                        continue;
                    }
                }
                if (count > 0) {
                    party.getNutMap().setv("num", Integer.valueOf(count));
                    party.getNutMap().setv("bandit", Integer.valueOf(count));
                    ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> this.partyNpcTips((int)party.getSerial()), 1200000L);
                    party.setSchedule(schedule);
                    this.partyService.npcPartyChat(party, "出大事了#<E:24>#！一大波强盗入侵我帮闹事，大家快快回帮将他们剿灭啊#<E:52>#。");
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public void partyNpcTips(int serial) {
        Party party = this.partyService.getParty(Integer.valueOf(serial));
        int num = party.getNutMap().getInt("bandit", 0);
        if (party.getNutMap().getInt("bandit", 0) > 0) {
            this.partyService.npcPartyChat(party, "20分钟内没有将帮派强盗全部消灭，强盗看着也没什么可抢的，就默默撤离了");
            if (party.getCapital() > 0L) {
                long construct = party.getCapital() - (long)((int)((double)this.partyService.getPartyMaxMembers((int)party.getLevel()) * 2.01467) * num);
                party.setCapital((construct < 0L) ? 0L : construct);
                party.save();
            }
        }
    }
    
    public Map<Integer, List<SysDataPartyBandit>> getBanditGrading(List<SysDataPartyBandit> partyBanditList) {
        Map<Integer, List<SysDataPartyBandit>> map = new HashMap(4);
        map.put(Integer.valueOf(1), partyBanditList.stream().filter(e -> e.getLevel() == 1).collect(Collectors.toList()));
        map.put(Integer.valueOf(2), partyBanditList.stream().filter(e -> e.getLevel() == 2).collect(Collectors.toList()));
        map.put(Integer.valueOf(3), partyBanditList.stream().filter(e -> e.getLevel() == 3).collect(Collectors.toList()));
        map.put(Integer.valueOf(4), partyBanditList.stream().filter(e -> e.getLevel() == 4).collect(Collectors.toList()));
        return map;
    }
    
    public List<SysDataPartyBandit> getCreationBandit(Map<Integer, List<SysDataPartyBandit>> banditGradingMap, Party party) {
        List<SysDataPartyBandit> list = new ArrayList(15);
        short onlineCount = this.partyService.getPartyOnlineCount(party);
        int count = BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), Double.valueOf((double)onlineCount * 0.75 * ((double)party.getActive() / 100.0) + (double)party.getLevel() * 0.5));
        if (count > 15) {
            count = 15;
        }
        if (count >= 8) {
            List<SysDataPartyBandit> collect = (List)banditGradingMap.get(Integer.valueOf(4));
            int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
            list.add(collect.get(index));
            collect = (List)banditGradingMap.get(Integer.valueOf(3));
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                list.add(collect.get(index));
            }
            collect = (List)banditGradingMap.get(Integer.valueOf(2));
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                list.add(collect.get(index));
            }
            collect = (List)banditGradingMap.get(Integer.valueOf(1));
            for (int i = 0; i < 3; ++i) {
                index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                list.add(collect.get(index));
            }
        }
        else if (count >= 6) {
            List<SysDataPartyBandit> collect = (List)banditGradingMap.get(Integer.valueOf(3));
            int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
            list.add(collect.get(index));
            collect = (List)banditGradingMap.get(Integer.valueOf(2));
            for (int i = 0; i < 2; ++i) {
                index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                list.add(collect.get(index));
            }
            collect = (List)banditGradingMap.get(Integer.valueOf(4));
            for (int i = 0; i < 3; ++i) {
                index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
                list.add(collect.get(index));
            }
        }
        else {
            count = 0;
        }
        count -= list.size();
        if (count > 0) {
            for (int j = 0; j < count; ++j) {
                List<SysDataPartyBandit> collect2 = (List)banditGradingMap.get(Integer.valueOf(PartyBanditHandler.BANDIT[RandomUtil.round(Integer.valueOf(0), Integer.valueOf(PartyBanditHandler.BANDIT.length - 1))]));
                if (collect2 != null && collect2.size() > 0) {
                    list.add(collect2.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect2.size() - 1))));
                }
            }
            return list;
        }
        return list;
    }
    
    public String getNpcContent(Role role, Npc npc) {
        this.setContent(npc, "俺们老大说了：给你们20分钟时间，如果能把俺们兄弟都打到，就饶了你们帮派不抢。出手吧#<E:32>#");
        return "为帮除害/threats_p_bandit|再见/离开";
    }
    
    public String getNpcContentNotFight(Role role, Npc npc) {
        if (npc.isInFight()) {
            this.setContent(npc, "我正在战斗中，等下再来收拾你#<E:32>#");
            this.sendNpcContent(role, npc, "再见/离开");
            return "再见/离开";
        }
        Party party = this.partyService.getParty(role);
        if (party == null) {
            return "";
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team == null || this.teamService.getTeamCount(team) < 3) {
            npc.setTalk("边凉快去，不够3人的队伍，大爷懒得出手#<E:0>#");
            return "";
        }
        String names = this.teamService.checkMember(team, memberRole -> !DataGlobalFunc.isTheSameParty((int)role.getFaction(), (int)memberRole.getFaction()));
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}不是本帮成员哦！#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        names = this.teamService.checkMember(team, memberRole -> !this.isJoinPartyTime24((PartyMember)party.getMembers().get(memberRole.getRoleId()), time));
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}进入帮派小于24小时，也敢来多管闲事！#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        return null;
    }
    
    public boolean isJoinPartyTime24(PartyMember member, int time) {
        if (member == null || time <= 0) {
            return false;
        }
        int day = BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Integer.valueOf((time - member.getJoinTime()) / 60 / 60 / 24)).intValue();
        return day >= 1;
    }
    
    public void setContent(Npc npc, String talk) {
        SysDataPartyBandit obj = (SysDataPartyBandit)TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            npc.setTalk(MessageFormat.format(talk, Integer.valueOf(obj.getLevel()), Integer.valueOf(obj.getGrade()), obj.getName()));
        }
    }
    
    public void doStartFight(Role role, Npc npc) {
        SysDataPartyBandit obj = (SysDataPartyBandit)TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            Random random = new Random();
            int index = random.nextInt(obj.getWar().size());
            SysWarRole warRole = (SysWarRole)FightDataPool.data_war_role.get(obj.getWar().get(index));
            if (warRole != null) {
                List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
                if (CollectionUtils.isEmpty(monsters)) {
                    this.logger.error("帮派强盗进入战斗错误1");
                    this.setNotInFight(npc);
                    return;
                }
                Integer monsterLevel = (Integer)Collections.max(this.teamService.getMemberLevel(role));
                monsters.stream().forEach(monster -> {
                    if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                        monster.setName(npc.getName());
                    }
                    else {
                        monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
                    }
                    monster.setMonsterType(FIGHT_TYPE.PARTY_BANDIT.getValue());
                    DataGlobalFunc.createMonster((int)monsterLevel, monster);
                    return;
                });
                BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.PARTY_BANDIT.getValue(), 0, npc.getId());
                Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
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
    protected Npc getBossNpc(int npcId) {
        return (Npc)DataCache.GROUP_NPC.get(Integer.valueOf(npcId));
    }
    
    @Override
    public void doFightWinForSingle(Role role, FightEndEvent fightEndEvent, Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        SpringUtils.getActivityService().addFinishCount(role, ActivityType.FACTION_BANDIT, 1);
    }
    
    @Override
    protected void clearNpcAfterWin(Role role, FightEndEvent fightEndEvent, Npc bossNpc) {
        this.clearNpcAfterFightWin(bossNpc, fightEndEvent);
    }
    
    @Override
    public void doFightFail(Role role, FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public void giveReward(Role role, FightEndEvent fightEndEvent, Npc npc) {
        SysDataPartyBandit obj = (SysDataPartyBandit)TaskDataPool.partyBanditList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (obj != null) {
            if (SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.FACTION_BANDIT)) {
                this.roleService.addExp(role, calculate((int)role.getLevel(), (int)role.getRelive(), npc.getLevel(), ItemClassify.HERO_EXP), "帮派强盗", true);
                Integer combatPet = role.getExtendBox().getCombatPet();
                if ((int)combatPet > 0) {
                    Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                    if (pet != null) {
                        this.petService.addExp(role, pet, calculate((int)pet.getPetLv(), (int)pet.getRebirth(), npc.getLevel(), ItemClassify.PET_EXP), true);
                    }
                }
                ((BagService)SpringUtils.getBean(BagService.class)).sendReward(role, obj.getAward(), "帮派强盗", obj.getLevel());
            }
            else {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("帮派强盗次数已满，无法获得奖励"));
            }
        }
        else {
            this.logger.error("获取帮派强盗奖励失败：{}", Integer.valueOf(npc.getNs()));
        }
    }
    
    private void clearNpcAfterFightWin(Npc npc, FightEndEvent fightEndEvent) {
        this.clearNpc(npc);
        Party party = this.partyService.getParty(Integer.valueOf(npc.getVisible()));
        if (party != null) {
            int num = party.getNutMap().getInt("bandit", 0);
            List<Role> rolesA = fightEndEvent.getRolesA();
            --num;
            party.getNutMap().setv("bandit", Integer.valueOf(num));
            String msg = "现在还有" + num + "队强盗没有被消灭";
            if (num <= 0) {
                msg = "入侵帮派的强盗已经全部被消灭#<E:90>#。";
                ScheduledFuture<?> task = party.getSchedule();
                if (task != null && !task.isDone()) {
                    task.cancel((boolean)Boolean.FALSE);
                }
                int bot = num + (int)((double)this.partyService.getPartyMaxMembers((int)party.getLevel()) * 2.25);
                this.partyService.addConstruction(party, (long)bot);
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < rolesA.size(); ++i) {
                Role role = (Role)rolesA.get(i);
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("、");
                }
                stringBuilder.append(role.getName());
            }
            this.partyService.npcPartyChat(party, MessageFormat.format("{0}的队伍三拳两脚就消灭了一队强盗#<E:81>#，{1}", "#<LB1>" + stringBuilder.toString() + "#", msg));
        }
    }
    
    private void clearNpcTimeOut(GroupMapParam groupMapParam, Npc npc) {
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
        return;
    }
    
    private void clearNpc(Npc npc) {
        if (npc == null) {
            return;
        }
        this.mapService.removeGroupMapNpc(new GroupMapParam(Integer.valueOf(npc.getVisible())), npc);
        this.removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
        this.bossService.broadcastNpcHide(new GroupMapParam(Integer.valueOf(npc.getVisible())), npc);
        return;
    }
    
    @Override
    protected void clearNpcTimeOut(Npc npc) {
        this.clearNpcTimeOut(npc);
    }
    
    private static long calculate(int grade, int rei, int cnt, ItemClassify type) {
        if (type == ItemClassify.HERO_EXP) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 27.13 * (0.2 + 0.1 * (double)cnt + Math.pow((double)cnt, 0.45)) * 1.5)).longValue();
        }
        if (type == ItemClassify.PET_EXP) {
            return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.9E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0 / 27.13 * (0.2 + 0.1 * (double)cnt + Math.pow((double)cnt, 0.45)) * 1.5 * 1.2)).longValue();
        }
        return 0L;
    }
    
    static {
        BANDIT = new int[] { 1, 2, 3, 4 };
    }
}
