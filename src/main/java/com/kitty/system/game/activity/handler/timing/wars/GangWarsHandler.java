
package com.kitty.system.game.activity.handler.timing.wars;

import org.nutz.json.Json;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.message.RespOffRoleState;
import com.kitty.system.game.activity.message.RespGangWarsRoleState;
import com.kitty.common.model.Pos;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.npc.message.RespNpcContent;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.activity.message.RespGangWarsInfo;
import com.kitty.system.game.activity.message.RespGangWarsIcon;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.activity.message.RespGangWarsMatchInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.team.domain.Member;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.DateUtils;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.activity.model.user.wars.GangWarsPkData;
import java.util.HashMap;
import com.kitty.system.game.party.model.PartyWarsRecord;
import com.kitty.common.utils.text.Convert;
import java.util.Collection;
import com.kitty.common.utils.ArraysUtil;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.party.pool.PartyDataPool;
import java.util.ArrayList;
import com.kitty.system.game.party.model.Party;
import java.util.Iterator;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.activity.common.EventsUtils;
import java.util.Date;
import java.util.Calendar;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsTimeSlot;
import java.util.concurrent.ScheduledFuture;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.model.user.wars.GangWarsSet;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.springframework.stereotype.Component;
import com.kitty.system.game.activity.service.ActivityHandler;

@Component
public class GangWarsHandler extends ActivityHandler
{
    @Autowired
    IdWorker worker;
    @Autowired
    protected RoleService roleService;
    @Autowired
    protected PetService petService;
    @Autowired
    protected TeamService teamService;
    @Autowired
    PartyService partyService;
    @Autowired
    ChatService chatService;
    @Autowired
    BagService bagService;
    @Autowired
    NpcService npcService;
    private GangWarsSet objSet;
    private GangWarsData objData;
    @Autowired
    ServerService serverService;
    public byte round;
    public static final int MAP = 3001;
    public static final int MAP_X_A = 195;
    public static final int MAP_Y_A = 109;
    public static final int FIGHT_MAP_X_A = 160;
    public static final int FIGHT_MAP_Y_A = 80;
    public static final int MAP_X_B = 24;
    public static final int MAP_Y_B = 19;
    public static final int FIGHT_MAP_X_B = 49;
    public static final int FIGHT_MAP_Y_B = 40;
    private static final int EXIT_MAP = 1011;
    private static final int EXIT_X = 234;
    private static final int EXIT_Y = 16;
    private static final int GROUP_NUM = 8;
    private ScheduledFuture scheduledFuture;
    private static final String ACTIVITY_NAME = "帮派大战";
    private static final String TIME_FORMAT_PATTERN = "yyyy-MM-dd-HH:mm:ss";
    private static final String FIGHT_LEVEL_NOT_ENOUGH = "等级达到&d级方可参加帮派大战。";
    private static final String REBIRTH_LIMIT_TIPS = "因刚刚复活，需要等待#<R>%s#才可以重新进入战场";
    private static final String CLASH = "#<LZ>%s##<EP>对阵##<LZ>%s#";
    private static final String WIN_TIPS = "#<EP>敌方城门耐久降为%d，我方在帮派大战中战胜##<LZ>%s##<EP>帮派##<E:86>#";
    private static final String FAIL_TIPS = "#<EP>我方城门耐久降为%d，我方在帮派大战中被##<LZ>%s##<EP>帮派击败##<E:86>#";
    private static final String DUEL_ENT_TIPS = "#<EP>帮派大战%s第%d轮，##<LZ>%s##<EP>战胜##<LZ>%s#";
    private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
    
    public GangWarsHandler() {
        this.round = 1;
    }
    
    public void prepareOnce(final GangWarsTimeSlot timeSlot) {
        this.initActivityData(timeSlot);
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.addBroadcastJob();
        this.initMatchingParty();
    }
    
    private void initActivityData(final GangWarsTimeSlot timeSlot) {
        final GangWarsData objData = new GangWarsData();
        this.initTime(objData, timeSlot.getSignUpTime(), timeSlot.getStartTime());
        this.objData = objData;
    }
    
    public void initTime(final GangWarsData objData, final String signUpTime, final String startTime) {
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final GangWarsSet objSet = this.getObjSet();
        final String[] signUp = signUpTime.split(":");
        calendarStart.set(11, Integer.valueOf(signUp[0]));
        calendarStart.set(12, Integer.valueOf(signUp[1]));
        calendarStart.set(13, 0);
        objData.setSignUpTime(EventsUtils.getTime(calendarStart));
        final String[] start = startTime.split(":");
        calendarStart.set(11, Integer.valueOf(start[0]));
        calendarStart.set(12, Integer.valueOf(start[1]));
        calendarStart.set(13, 0);
        objData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, objSet.getSustain());
        objData.setEndTime(EventsUtils.getTime(calendarStart));
    }
    
    private void addCheckJob() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.objData.getStartTime().getTime());
        calendar.add(11, -1);
        final long initialDelay = this.objData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加定时检测的定时任务==={}", (Object)initialDelay);
        try {
            if (this.scheduledFuture != null) {
                this.scheduledFuture.cancel(false);
            }
        }
        catch (final Exception e) {
            this.logger.error("定时器关闭==", (Throwable)e);
        }
    }
    
    private void addStartJob() {
        final long initialDelay = this.objData.getStartTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加帮派大战开始的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> SpringUtils.getBean(GangWarsPkHandler.class).start(), initialDelay);
    }
    
    private void addEndJob() {
        final long initialDelay = this.objData.getEndTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加帮派大战结束的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> this.end(), initialDelay);
    }
    
    private void addBroadcastJob() {
        final long initialDelay = this.objData.getSignUpTime().getTimeInMillis() - System.currentTimeMillis();
        this.logger.error("添加帮派大战播报的定时任务==={}", (Object)initialDelay);
        SchedulerManager.getInstance().schedule(() -> this.broadcast(), initialDelay);
    }
    
    public void broadcast() {
        for (final String string : this.objData.getAnnounce()) {
            this.chatService.sendSystem(string, "role");
        }
        for (final Integer key : this.objData.getWarsMap().keySet()) {
            final Party party = this.partyService.getParty(key);
            if (party != null) {
                final GangWarsInfo warsInfo = this.objData.getWarsMap().get(key);
                this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(368, this.getBye(warsInfo.getGrade())));
            }
        }
        for (final Integer key : this.objData.getByeMap().keySet()) {
            final Party party = this.partyService.getParty(key);
            if (party != null) {
                this.partyService.npcPartyChat(party, I18nIdDataPool.getI18nContent(369, this.getBye(this.objData.getByeMap().get(key))));
            }
        }
    }
    
    private void initMatchingParty() {
        final List<Integer> gradeA = new ArrayList<Integer>();
        final List<Integer> gradeB = new ArrayList<Integer>();
        final List<Integer> gradeC = new ArrayList<Integer>();
        final List<Integer> gradeD = new ArrayList<Integer>();
        final List<Integer> gradeE = new ArrayList<Integer>();
        final Map<Integer, Party> partyMap = PartyDataPool.partyMap;
        List<Party> list = partyMap.values().stream().sorted(Comparator.comparing(Party::getRating).thenComparing(Party::getLevel, Comparator.reverseOrder()).thenComparing(Party::getActive, Comparator.reverseOrder()).thenComparing(Party::getMemberNum, Comparator.reverseOrder())).collect(Collectors.toList());
        if (list.size() > 32) {
            list = list.subList(0, 32);
        }
        for (int i = 0; i < list.size(); ++i) {
            final Party party = list.get(i);
            if (gradeA.size() < 8) {
                this.listAdd(gradeA, gradeB, party.getSerial());
            }
            else if (gradeB.size() < 8) {
                this.listAdd(gradeB, gradeC, party.getSerial());
            }
            else if (gradeC.size() < 8) {
                this.listAdd(gradeC, gradeD, party.getSerial());
            }
            else if (gradeD.size() < 8) {
                gradeD.add(party.getSerial());
            }
            else {
                gradeE.add(party.getSerial());
            }
        }
        if (gradeA.size() < 8) {
            this.substitution(gradeA, gradeB);
        }
        if (gradeB.size() < 8) {
            this.substitution(gradeB, gradeC);
        }
        if (gradeC.size() < 8) {
            this.substitution(gradeC, gradeD);
        }
        if (gradeC.size() >= 8 && gradeD.size() < 8) {
            this.substitution(gradeD, gradeE);
        }
        if (gradeA.size() > 0) {
            this.match(gradeA, 1);
        }
        if (gradeB.size() > 0) {
            this.match(gradeB, 2);
        }
        if (gradeC.size() > 0) {
            this.match(gradeC, 3);
        }
        if (gradeD.size() > 0) {
            this.match(gradeD, 4);
        }
    }
    
    public String getBye(final int bye) {
        String name = null;
        switch (bye) {
            case 1: {
                name = "甲级";
                break;
            }
            case 2: {
                name = "已级";
                break;
            }
            case 3: {
                name = "丙级";
                break;
            }
            case 4: {
                name = "丁级";
                break;
            }
            default: {
                name = "丁级";
                break;
            }
        }
        return name;
    }
    
    public void listAdd(final List<Integer> listA, final List<Integer> listB, final int serial) {
        if (listA.size() < 6) {
            listA.add(serial);
        }
        else if (listB.size() < 2) {
            listB.add(serial);
        }
        else {
            listA.add(serial);
        }
    }
    
    public void substitution(final List<Integer> listA, final List<Integer> listB) {
        for (int num = 8 - listA.size(), i = 0; i < num; ++i) {
            if (listB.size() > 0) {
                listA.add(listB.get(0));
                listB.remove(0);
            }
        }
    }
    
    public void match(final List<Integer> list, final int bye) {
        final String byeString = this.getBye(bye);
        final StringBuilder stringBuilder = new StringBuilder();
        final List<Integer> objects = new ArrayList<Integer>();
        final Map<Integer, Party> partyMap = PartyDataPool.partyMap;
        for (int i = 0; i < list.size(); ++i) {
            final int key = list.get(i);
            if (!objects.contains(key)) {
                final Party sideParty = partyMap.get(key);
                if (sideParty != null) {
                    objects.add(sideParty.getSerial());
                    final List<Integer> notMatchList = new ArrayList<Integer>(ArraysUtil.getDifferListByMapPlus(list, objects));
                    if (notMatchList.size() <= 0) {
                        this.objData.getByeMap().put(sideParty.getSerial(), bye);
                    }
                    else {
                        final List<Integer> enemySerialList = sideParty.getWarsEnemySerial();
                        List<Integer> canMatchList = new ArrayList<Integer>(ArraysUtil.getDislodgeReDo(notMatchList, enemySerialList));
                        if (canMatchList.size() <= 0) {
                            canMatchList = notMatchList;
                        }
                        final Integer serial = canMatchList.get(0);
                        final Party enemyParty = partyMap.get(serial);
                        if (enemyParty == null) {
                            this.objData.getByeMap().put(sideParty.getSerial(), bye);
                        }
                        else {
                            objects.add(enemyParty.getSerial());
                            this.warsRecord(sideParty, enemyParty, bye);
                            if (i == 0) {
                                stringBuilder.append(byeString + "帮战第" + Convert.getChineseFromArabic((int)this.round) + "轮名单:");
                            }
                            else {
                                stringBuilder.append("、");
                            }
                            stringBuilder.append(String.format("#<LZ>%s##<EP>对阵##<LZ>%s#", sideParty.getName(), enemyParty.getName()));
                        }
                    }
                }
            }
        }
        if (stringBuilder.length() > 0) {
            this.objData.getAnnounce().add(stringBuilder.toString());
        }
    }
    
    public void warsRecord(final Party sideParty, final Party enemyParty, final int bye) {
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        final PartyWarsRecord sideRecord = new PartyWarsRecord(this.worker.nextId(), sideParty.getSerial(), time, (byte)3, enemyParty.getSerial(), enemyParty.getName(), 0, 0);
        sideParty.getWarsRecordList().add(sideRecord);
        sideRecord.save();
        final PartyWarsRecord enemyRecord = new PartyWarsRecord(this.worker.nextId(), enemyParty.getSerial(), time, (byte)3, sideParty.getSerial(), sideParty.getName(), 0, 0);
        enemyParty.getWarsRecordList().add(enemyRecord);
        enemyRecord.save();
        final Integer visible = sideParty.getSerial();
        this.objData.getPkData().put(sideParty.getSerial(), new HashMap<Long, GangWarsPkData>(sideParty.getMembers().size()));
        this.objData.getPkData().put(enemyParty.getSerial(), new HashMap<Long, GangWarsPkData>(enemyParty.getMembers().size()));
        this.objData.getWarsMap().put(sideParty.getSerial(), new GangWarsInfo((byte)0, visible, bye, enemyParty.getSerial()));
        this.objData.getWarsMap().put(enemyParty.getSerial(), new GangWarsInfo((byte)1, visible, bye, sideParty.getSerial()));
    }
    
    private void end() {
        if (this.scheduledFuture != null) {
            this.scheduledFuture.cancel(Boolean.FALSE);
        }
        this.objData.setStage((byte)2);
        for (final int key : this.objData.getWarsMap().keySet()) {
            final GangWarsInfo side = this.objData.getWarsMap().get(key);
            if (side.getCombatResults() == 3) {
                int win = key;
                final GangWarsInfo enemy = this.objData.getWarsMap().get(side.getEnemy());
                if (enemy.getGateLife() > side.getGateLife()) {
                    win = side.getEnemy();
                }
                else {
                    final int round = RandomUtil.round(1, 100);
                    if (round > 50) {
                        win = side.getEnemy();
                    }
                }
                if (win == key) {
                    side.setCombatResults((byte)1);
                    enemy.setCombatResults((byte)0);
                }
                else {
                    enemy.setCombatResults((byte)1);
                    side.setCombatResults((byte)0);
                }
                this.end(key, side, enemy);
            }
        }
        for (final Equipment value : this.objData.getCannonMap().values()) {
            if (value.getScheduledFuture() != null) {
                value.getScheduledFuture().cancel(Boolean.FALSE);
            }
        }
        for (final Equipment value : this.objData.getGateMap().values()) {
            if (value.getScheduledFuture() != null) {
                value.getScheduledFuture().cancel(Boolean.FALSE);
            }
        }
        this.objData = null;
        ++this.round;
        final String day = DateUtils.dayForWeek(new Date());
        final GangWarsTimeSlot timeSlot = this.objSet.getDayOfWeek().stream().filter(e -> e.getDayOfWeek().equals(day) && e.getRound() == this.round).findFirst().orElse(null);
        if (timeSlot != null) {
            this.notifyAt(timeSlot.getRound());
        }
    }
    
    public void end(final int serial, final GangWarsInfo side, final GangWarsInfo enemy) {
        this.updatePartyWarsRecord(serial, side.getCombatResults());
        this.updatePartyWarsRecord(side.getEnemy(), enemy.getCombatResults());
        this.sendResult(serial, side.getCombatResults());
        this.sendResult(side.getEnemy(), enemy.getCombatResults());
        this.clearTimer(serial, side.getVisible(), side.getEnemy());
        final Party sideParty = this.partyService.getParty(serial);
        final Party enemyParty = this.partyService.getParty(side.getEnemy());
        if (sideParty != null && enemyParty != null) {
            int sideNum = 10;
            final Map<Long, GangWarsPkData> sideDataMap = this.objData.getPkData().get(serial);
            if (sideDataMap != null || sideDataMap.size() > sideNum) {
                sideNum = sideDataMap.size();
            }
            final long construction = this.getConstruction(sideParty.getLevel(), sideNum, side.getCombatResults());
            this.partyService.addConstruction(sideParty, construction);
            this.partyService.npcPartyChat(sideParty, String.format("参战奖励：帮派%s%d", (sideParty.getLevel() < 4) ? "建设" : "资金", construction));
            this.partyService.npcPartyChat(sideParty, String.format("#<EP>敌方城门耐久降为%d，我方在帮派大战中战胜##<LZ>%s##<EP>帮派##<E:86>#", enemy.getGateLife(), enemyParty.getName()));
            int enemyNum = 10;
            final Map<Long, GangWarsPkData> enemyDataMap = this.objData.getPkData().get(side.getEnemy());
            if (enemyDataMap != null || enemyDataMap.size() > enemyNum) {
                enemyNum = enemyDataMap.size();
            }
            final long enemyConstruction = this.getConstruction(enemyParty.getLevel(), enemyNum, enemy.getCombatResults());
            this.partyService.addConstruction(enemyParty, enemyConstruction);
            this.partyService.npcPartyChat(enemyParty, String.format("参战奖励：帮派%s%d", (enemyParty.getLevel() < 4) ? "建设" : "资金", enemyConstruction));
            this.partyService.npcPartyChat(enemyParty, String.format("#<EP>我方城门耐久降为%d，我方在帮派大战中被##<LZ>%s##<EP>帮派击败##<E:86>#", enemy.getGateLife(), sideParty.getName()));
            this.chatService.sendSystem(String.format("#<EP>帮派大战%s第%d轮，##<LZ>%s##<EP>战胜##<LZ>%s#", this.getBye(side.getGrade()), this.round, sideParty.getName(), enemyParty.getName()), "role");
        }
    }
    
    public long getConstruction(final int cnt, final int factor, final byte isWin) {
        double coefficient = 9.3889;
        if (isWin == 0) {
            coefficient = 5.3889;
        }
        return BigDecimalUtil.decimalScaleDown(0, 500.0 + (6.1228 * cnt + factor * coefficient) * factor * 0.0866).longValue();
    }
    
    public void clearTimer(final int side, final int visible, final int enemy) {
        final Equipment cannon = this.objData.getCannonMap().get(visible);
        if (cannon != null) {
            if (cannon.getScheduledFuture() != null) {
                cannon.getScheduledFuture().cancel(Boolean.FALSE);
            }
            this.objData.getCannonMap().remove(visible);
        }
        final Equipment sideGate = this.objData.getGateMap().get(side);
        if (sideGate != null) {
            if (sideGate.getScheduledFuture() != null) {
                sideGate.getScheduledFuture().cancel(Boolean.FALSE);
            }
            this.objData.getGateMap().remove(side);
        }
        final Equipment enemyGate = this.objData.getGateMap().get(enemy);
        if (enemyGate != null) {
            if (enemyGate.getScheduledFuture() != null) {
                enemyGate.getScheduledFuture().cancel(Boolean.FALSE);
            }
            this.objData.getGateMap().remove(enemy);
        }
    }
    
    public void sendResult(final int serial, final byte result) {
        final GangWarsInfo sideWarsInfo = this.objData.getWarsMap().get(serial);
        if (sideWarsInfo == null) {
            return;
        }
        final GangWarsInfo enemyWarsInfo = this.objData.getWarsMap().get(sideWarsInfo.getEnemy());
        final Map<Long, GangWarsPkData> enemyPkDataMap = this.objData.getPkData().get(sideWarsInfo.getEnemy());
        final Party enemyParty = this.partyService.getParty(sideWarsInfo.getEnemy());
        final Map<Long, GangWarsPkData> sidePkDataMap = this.objData.getPkData().get(serial);
        final Party sideParty = this.partyService.getParty(serial);
        final Map<Long, GangWarsPkData> warsPkDataMap = this.objData.getPkData().get(serial);
        final RespGangWarsMatchInfo matchInfo = this.getGangWarsMatchInfo(sideParty, sideWarsInfo, sidePkDataMap, enemyParty, enemyWarsInfo, enemyPkDataMap);
        matchInfo.setType(result);
        for (final Long id : warsPkDataMap.keySet()) {
            final GangWarsPkData data = warsPkDataMap.get(id);
            if (data.getPosition() == 3) {
                continue;
            }
            final Role player = this.roleService.getPlayer(id);
            if (player == null) {
                continue;
            }
            this.leave(player);
            final Team team = this.teamService.getTeam(player.getRoleId());
            if (team != null) {
                final Member member = team.getList().stream().filter(e -> e.getId().equals(player.getId())).findFirst().orElse(null);
                if (member != null && !member.isInTeam()) {
                    MessagePusher.pushMessage(id, matchInfo);
                }
                else {
                    this.teamService.memberHandleThreadLocal(team, memberRole -> MessagePusher.pushMessage(memberRole.getId(), matchInfo));
                }
            }
            else {
                MessagePusher.pushMessage(id, matchInfo);
            }
        }
        final byte level = sideParty.getLevel();
        final int num = sidePkDataMap.size();
        for (final Long id2 : warsPkDataMap.keySet()) {
            final Role player2 = this.roleService.getPlayer(id2);
            this.award(player2, level, sideWarsInfo.getCombatResults(), num);
        }
    }
    
    public void award(final Role role, final byte level, final byte combatResults, final int num) {
        if (role != null) {
            this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), level, num, ItemClassify.HERO_EXP, combatResults), "帮战奖励", true);
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), level, num, ItemClassify.PET_EXP, combatResults), true);
            }
            this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, calculate(role.getLevel(), role.getRelive(), level, num, ItemClassify.RESTYPE_TRIBUTE, combatResults), "帮战奖励", true, false);
        }
    }
    
    public void updatePartyWarsRecord(final int serial, final byte results) {
        final Party party = this.partyService.getParty(serial);
        if (party != null) {
            final PartyWarsRecord warsRecord = party.getWarsRecordList().stream().filter(e -> e.getCombatResults() == 3).findFirst().orElse(null);
            if (warsRecord != null) {
                warsRecord.setCombatResults(results);
                warsRecord.save();
            }
        }
    }
    
    public void enterMap(final Role role, final Npc npc) {
        final String talk = this.canEnter(role);
        if (talk != null) {
            if (!talk.isEmpty()) {
                SpringUtils.getBean(NpcService.class).sendNpcContent(role, npc, "", talk);
            }
        }
        else {
            this.doEnterMap(role);
        }
    }
    
    public String canEnter(final Role role) {
        if (role.getFaction() <= 0) {
            MessagePusher.pushMessage(role, new RespMsg("你当前没有帮派"));
            return "";
        }
        final GangWarsData objData = this.getObjData();
        if (objData == null) {
            MessagePusher.pushMessage(role, new RespMsg("你们帮目前没有帮战"));
            return "";
        }
        final Calendar calendar = Calendar.getInstance();
        if (calendar.before(objData.getSignUpTime())) {
            MessagePusher.pushMessage(role, new RespMsg("你们帮目前没有帮战"));
            return "";
        }
        if (objData.getByeMap().get(role.getFaction()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("本帮已经轮空获胜，无需进场"));
            return "";
        }
        final GangWarsInfo warsInfo = objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null || warsInfo.getGateLife() <= 0 || warsInfo.getCombatResults() != 3) {
            MessagePusher.pushMessage(role, new RespMsg("你们帮目前没有帮战"));
            return "";
        }
        if (DataGlobalFunc.needGradeJudge(0, this.objSet.getMinLevel(), role.getRelive(), role.getLevel(), "") != null) {
            return String.format("等级达到&d级方可参加帮派大战。", this.objSet.getMinLevel());
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
            if (member != null && !member.isInTeam()) {
                return null;
            }
            for (int i = 0; i < team.getList().size(); ++i) {
                member = team.getList().get(i);
                if (member.isInTeam()) {
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null && !player.getFaction().equals(role.getFaction())) {
                        this.teamService.leaveTemp(player, true);
                    }
                }
            }
        }
        return null;
    }
    
    public void doEnterMap(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        final GangWarsInfo warsInfo = this.objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null) {
            return;
        }
        int x = 195;
        int y = 109;
        if (warsInfo.getStance() == 1) {
            x = 24;
            y = 19;
        }
        this.enter(role, team, new ReqTelePort(3001, x, y));
    }
    
    public void enter(final Role role, final Team team, final ReqTelePort reqTelePort) {
        final Map<Long, GangWarsPkData> pkDataMap = this.objData.getPkData().get(role.getFaction());
        if (pkDataMap == null) {
            return;
        }
        final GangWarsInfo warsInfo = this.objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null) {
            return;
        }
        if (team != null) {
            final Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
            if (member != null && member.isInTeam()) {
                this.teamService.memberHandle(team, memberRole -> this.enterUpData(memberRole, pkDataMap));
            }
            else {
                this.enterUpData(role, pkDataMap);
            }
        }
        else {
            this.enterUpData(role, pkDataMap);
        }
        if (reqTelePort != null) {
            if (team != null) {
                final Member member = team.getList().stream().filter(e -> e.getId().equals(role.getId())).findFirst().orElse(null);
                if (member != null && member.isInTeam()) {
                    this.teamService.memberHandle(team, memberRole -> memberRole.setVisible(warsInfo.getVisible()));
                    this.teamService.memberHandle(team, memberRole -> this.sendRespIcon(memberRole.getId(), memberRole.getFaction()));
                }
                else {
                    role.setVisible(warsInfo.getVisible());
                    this.sendRespIcon(role.getId(), role.getFaction());
                }
            }
            else {
                role.setVisible(warsInfo.getVisible());
                this.sendRespIcon(role.getId(), role.getFaction());
            }
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        }
    }
    
    public void enterUpData(final Role role, final Map<Long, GangWarsPkData> pkDataMap) {
        final GangWarsPkData data = pkDataMap.get(role.getRoleId());
        if (data == null) {
            pkDataMap.put(role.getId(), new GangWarsPkData());
        }
    }
    
    public void sendRespIcon(final long id, final int faction) {
        if (this.objData != null) {
            MessagePusher.pushMessage(id, this.getRespIcon(faction));
        }
    }
    
    public RespGangWarsIcon getRespIcon(final int faction) {
        final GangWarsInfo sideWarsInfo = this.objData.getWarsMap().get(faction);
        if (sideWarsInfo == null) {
            return null;
        }
        final RespGangWarsIcon respIcon = new RespGangWarsIcon();
        respIcon.setStance(sideWarsInfo.getStance());
        final Party enemyParty = this.partyService.getParty(sideWarsInfo.getEnemy());
        final Party sideParty = this.partyService.getParty(faction);
        respIcon.setEnemyGrade(enemyParty.getLevel());
        respIcon.setEnemyName(enemyParty.getName());
        respIcon.setSideGrade(sideParty.getLevel());
        respIcon.setSideName(sideParty.getName());
        if (this.objData.getStage() == 1) {
            final GangWarsInfo enemyWarsInfo = this.objData.getWarsMap().get(enemyParty.getSerial());
            respIcon.setEnemyLife(enemyWarsInfo.getGateLife());
            respIcon.setSideLife(sideWarsInfo.getGateLife());
            respIcon.setState((byte)1);
            respIcon.setTime((int)(this.objData.getStartTime().getTimeInMillis() / 1000L));
        }
        return respIcon;
    }
    
    public void eventInfo(final Role role) {
        if (this.objData == null || this.objData.getStage() == 2) {
            return;
        }
        if (!this.isInMap(role)) {
            return;
        }
        final GangWarsInfo sideWarsInfo = this.objData.getWarsMap().get(role.getFaction());
        if (sideWarsInfo == null) {
            return;
        }
        final GangWarsInfo enemyWarsInfo = this.objData.getWarsMap().get(sideWarsInfo.getEnemy());
        final Map<Long, GangWarsPkData> enemyPkDataMap = this.objData.getPkData().get(sideWarsInfo.getEnemy());
        final Party enemyParty = this.partyService.getParty(sideWarsInfo.getEnemy());
        final Map<Long, GangWarsPkData> sidePkDataMap = this.objData.getPkData().get(role.getFaction());
        final Party sideParty = this.partyService.getParty(role.getFaction());
        if (this.objData.getStage() == 1) {
            MessagePusher.pushMessage(role, this.getGangWarsMatchInfo(sideParty, sideWarsInfo, sidePkDataMap, enemyParty, enemyWarsInfo, enemyPkDataMap));
        }
        else {
            final RespGangWarsInfo gangWarsInfo = new RespGangWarsInfo();
            gangWarsInfo.setEnemyName(enemyParty.getName());
            gangWarsInfo.setEnemyNum((short)enemyPkDataMap.size());
            gangWarsInfo.setSideName(sideParty.getName());
            gangWarsInfo.setSideNum((short)sidePkDataMap.size());
            MessagePusher.pushMessage(role, gangWarsInfo);
        }
    }
    
    public RespGangWarsMatchInfo getGangWarsMatchInfo(final Party sideParty, final GangWarsInfo sideWarsInfo, final Map<Long, GangWarsPkData> sidePkDataMap, final Party enemyParty, final GangWarsInfo enemyWarsInfo, final Map<Long, GangWarsPkData> enemyPkDataMap) {
        final RespGangWarsMatchInfo gangWarsMatchInfo = new RespGangWarsMatchInfo();
        final NutConcurrentMap recordMap = this.objData.getRecordMap();
        final Integer enemyPartySerial = enemyParty.getSerial();
        gangWarsMatchInfo.setEnemyName(enemyParty.getName());
        gangWarsMatchInfo.setEnemyNum((short)enemyPkDataMap.size());
        gangWarsMatchInfo.setEnemyGateLife(enemyWarsInfo.getGateLife());
        gangWarsMatchInfo.setSideWinRonda((short)recordMap.getInt(enemyPartySerial + "_win", 0));
        gangWarsMatchInfo.setSideContestWinRonda((short)recordMap.getInt(enemyPartySerial + "_contest", 0));
        gangWarsMatchInfo.setSidePylonRonda((short)recordMap.getInt(enemyPartySerial + "_pylon", 0));
        gangWarsMatchInfo.setSideCannonRonda((short)recordMap.getInt(enemyPartySerial + "_cannon", 0));
        final Integer sidePartySerial = sideParty.getSerial();
        gangWarsMatchInfo.setSideName(sideParty.getName());
        gangWarsMatchInfo.setSideNum((short)sidePkDataMap.size());
        gangWarsMatchInfo.setSideGateLife(sideWarsInfo.getGateLife());
        gangWarsMatchInfo.setSideWinRonda((short)recordMap.getInt(sidePartySerial + "_win", 0));
        gangWarsMatchInfo.setSideContestWinRonda((short)recordMap.getInt(sidePartySerial + "_contest", 0));
        gangWarsMatchInfo.setSidePylonRonda((short)recordMap.getInt(sidePartySerial + "_pylon", 0));
        gangWarsMatchInfo.setSideCannonRonda((short)recordMap.getInt(sidePartySerial + "_cannon", 0));
        return gangWarsMatchInfo;
    }
    
    public void goToMap(final Role role, final Npc npc, final String type) {
        if (this.objData == null) {
            SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
            return;
        }
        int x = 0;
        int y = 0;
        byte position = 0;
        final GangWarsInfo warsInfo = this.objData.getWarsMap().get(role.getFaction());
        if (warsInfo == null) {
            SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
            return;
        }
        final Map<Long, GangWarsPkData> warsPkDataMap = this.objData.getPkData().get(role.getFaction());
        if (warsPkDataMap == null) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        final byte stance = warsInfo.getStance();
        if ("to_fight_wars".equals(type)) {
            final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
            if (team == null) {
                final GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
                if (gangWarsPkData.getRebirth() > time) {
                    MessagePusher.pushMessage(role, this.npcService.getRespNpcContent(role, npc, "", String.format("因刚刚复活，需要等待#<R>%s#才可以重新进入战场", String.format("%s(%d秒)", role.getName(), gangWarsPkData.getRebirth() - time))));
                    return;
                }
                gangWarsPkData.setRebirth(0);
            }
            else {
                final StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    if (member.isInTeam()) {
                        final GangWarsPkData gangWarsPkData2 = warsPkDataMap.get(role.getId());
                        if (gangWarsPkData2.getRebirth() > time) {
                            final Role player = this.roleService.getPlayer(member.getId());
                            if (player != null) {
                                if (stringBuilder.length() > 0) {
                                    stringBuilder.append("、");
                                }
                                final String format = String.format("%s(%d秒)", player.getName(), gangWarsPkData2.getRebirth() - time);
                                stringBuilder.append(format);
                            }
                        }
                    }
                }
                if (stringBuilder.length() > 0) {
                    final RespNpcContent respNpcContent = this.npcService.getRespNpcContent(role, npc, "", String.format("因刚刚复活，需要等待#<R>%s#才可以重新进入战场", stringBuilder.toString()));
                    this.teamService.memberHandle(team, memberRole -> MessagePusher.pushMessage(memberRole, respNpcContent));
                    return;
                }
            }
            if (stance == 1) {
                x = 49;
                y = 40;
            }
            else {
                x = 160;
                y = 80;
            }
            position = 1;
        }
        else if ("to_home_wars".equals(type)) {
            if (stance == 1) {
                x = 24;
                y = 19;
            }
            else {
                x = 195;
                y = 109;
            }
        }
        if (x > 0 && y > 0) {
            if (team != null) {
                this.teamService.memberHandleThreadLocal(team, memberRole -> memberRole.pushTempCache("not_check_change_map", true));
                final byte finalPosition = position;
                this.teamService.memberHandleThreadLocal(team, memberRole -> this.updatePosition(role, finalPosition));
            }
            else {
                role.pushTempCache("not_check_change_map", true);
                this.updatePosition(role, position);
            }
            SpringUtils.getMapService().changeMap(role, new ReqTelePort(3001, x, y), null);
        }
    }
    
    public void updatePosition(final Role role, final byte position) {
        if (this.objData != null) {
            final Map<Long, GangWarsPkData> dataMap = this.objData.getPkData().get(role.getFaction());
            if (dataMap != null) {
                final GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
                if (gangWarsPkData != null) {
                    gangWarsPkData.updatePosition(position);
                }
            }
        }
    }
    
    public void leave(final Role role) {
        if (role != null) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null) {
                final GangWarsInfo gangWarsInfo = this.objData.getWarsMap().get(role.getFaction());
                if (this.teamService.getTeamCount(team) > 1 && this.objData.getStage() != 2) {
                    final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
                    if (leaderRole != null && leaderRole.getId().equals(role.getId())) {
                        this.teamService.leave(role, false);
                    }
                    final Map<Long, GangWarsPkData> warsPkDataMap = this.objData.getPkData().get(role.getFaction());
                    final GangWarsPkData gangWarsPkData = warsPkDataMap.get(role.getId());
                    gangWarsPkData.setPosition((byte)3);
                    gangWarsPkData.setState((byte)0);
                }
                else if (this.objData.getStage() == 2 || gangWarsInfo.getCombatResults() < 3) {
                    final Map<Long, GangWarsPkData> warsPkDataMap2 = this.objData.getPkData().get(role.getFaction());
                    final Role leaderRole2 = this.roleService.getPlayer(team.getLeaderUid());
                    if (leaderRole2 != null && leaderRole2.getId().equals(role.getId())) {
                        for (final Member member : team.getList()) {
                            if (member.isInTeam()) {
                                final Role player = this.roleService.getPlayer(member.getId());
                                if (player != null) {
                                    final GangWarsPkData gangWarsPkData2 = warsPkDataMap2.get(player.getId());
                                    if (gangWarsPkData2 != null) {
                                        gangWarsPkData2.setPosition((byte)3);
                                        gangWarsPkData2.setState((byte)0);
                                        player.pushTempCache("not_check_change_map", true);
                                        MessagePusher.pushMessage(player, new RespTaskIcon(3003, "wars", (byte)0));
                                    }
                                }
                                this.leaveMap(leaderRole2);
                                return;
                            }
                        }
                    }
                }
            }
            else {
                final Map<Long, GangWarsPkData> warsPkDataMap3 = this.objData.getPkData().get(role.getFaction());
                if (warsPkDataMap3 != null) {
                    final GangWarsPkData gangWarsPkData3 = warsPkDataMap3.get(role.getId());
                    if (gangWarsPkData3 != null) {
                        gangWarsPkData3.setPosition((byte)3);
                        gangWarsPkData3.setState((byte)0);
                    }
                }
            }
            this.leaveMap(role);
        }
    }
    
    public void leaveMap(final Role role) {
        if (role == null) {
            return;
        }
        if (!this.isInMap(role)) {
            return;
        }
        this.updatePosition(role, (byte)3);
        role.pushTempCache("not_check_change_map", true);
        SpringUtils.getMapService().changeMap(role, new ReqTelePort(1011, 234, 16), null);
        final RespTaskIcon respTaskIcon = new RespTaskIcon(3001, "wars", (byte)0);
        MessagePusher.pushMessage(role, respTaskIcon);
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final int factor, final ItemClassify type, final byte isWin) {
        double exp = 0.0;
        if (ItemClassify.HERO_EXP == type) {
            exp = factor * 0.0866 * (1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * ((isWin == 1) ? 2.5 : 1.5)) / 30.0 * 1.2 * (6.0E-4 * cnt + 0.83));
        }
        if (ItemClassify.PET_EXP == type) {
            exp = 2.4 * factor * 0.0866 * (1.6E7 * (1.0 + (grade + rei * 15) / 140.0 * ((isWin == 1) ? 2.5 : 1.5)) / 30.0 * 1.2 * (6.0E-4 * cnt + 0.83));
        }
        if (ItemClassify.RESTYPE_TRIBUTE == type) {
            return BigDecimalUtil.decimalScaleDown(0, 69.0 + (cnt + factor) * ((isWin == 1) ? 9.575 : 6.575) * (6.0E-4 * cnt + 0.83)).longValue() * 2L;
        }
        return BigDecimalUtil.decimalScaleDown(0, exp).longValue();
    }
    
    public boolean isBackToTeam(final Team team, final Role memberRole) {
        final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
        final boolean inMapA = this.isInMap(leaderRole);
        final boolean inMapB = this.isInMap(memberRole);
        if (inMapA && !inMapB) {
            final Pos pos = leaderRole.getPos();
            MessagePusher.pushMessage(memberRole, new RespMsg(String.format("队伍在#<O>帮战#中 #<LG1>(%d，%d)# ，暂时无法归队。", pos.getX(), pos.getY())));
            return false;
        }
        if (inMapA && inMapB && !leaderRole.getFaction().equals(memberRole.getFaction())) {
            final Pos pos = leaderRole.getPos();
            MessagePusher.pushMessage(memberRole, new RespMsg(String.format("队伍在#<O>帮战#中 #<LG1>(%d，%d)# ，你与队长不同属于一个帮派，暂时无法归队。", pos.getX(), pos.getY())));
            return false;
        }
        return true;
    }
    
    @Override
    public void handleLogin(final Role role) {
        final boolean inMap = this.isInMap(role);
        if (!inMap) {
            return;
        }
        if (this.objData == null || System.currentTimeMillis() >= this.objData.getEndTime().getTimeInMillis()) {
            this.leaveMap(role);
            return;
        }
        if (this.objData != null) {
            boolean is = true;
            final Map<Long, GangWarsPkData> dataMap = this.objData.getPkData().get(role.getFaction());
            final GangWarsInfo gangWarsInfo = this.objData.getWarsMap().get(role.getFaction());
            if (dataMap != null && gangWarsInfo != null) {
                final GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
                if (gangWarsPkData != null && gangWarsInfo.getCombatResults() == 3) {
                    is = false;
                }
                if (is) {
                    this.leaveMap(role);
                }
                else {
                    this.sendRespIcon(role.getId(), role.getFaction());
                    boolean flag_ = true;
                    if (gangWarsPkData.getPosition() == 0 && gangWarsPkData.getRebirth() > 0) {
                        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
                        if (gangWarsPkData.getRebirth() > time) {
                            flag_ = false;
                            MessagePusher.pushMessage(role.getId(), new RespGangWarsRoleState((byte)0, gangWarsPkData.getRebirth()));
                        }
                        else {
                            gangWarsPkData.setRebirth(0);
                        }
                    }
                    if (flag_) {
                        final Equipment equipment = this.objData.getGateMap().get(role.getFaction());
                        if (equipment != null && equipment.getList().contains(role.getId())) {
                            flag_ = false;
                            MessagePusher.pushMessage(role.getId(), new RespGangWarsRoleState((byte)3, 0));
                        }
                    }
                    if (flag_) {
                        final Equipment equipment = this.objData.getCannonMap().get(gangWarsInfo.getVisible());
                        if (equipment != null && equipment.getList().contains(role.getId())) {
                            MessagePusher.pushMessage(role.getId(), new RespGangWarsRoleState((byte)2, equipment.getTime()));
                        }
                    }
                }
            }
        }
        super.handleLogin(role);
    }
    
    @Override
    public void handleTeamLeaveMoment(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        this.teamLeave(role, this.roleService.getPlayer(team.getLeaderUid()));
    }
    
    @Override
    public void handleTeamLeave(final Role role, final boolean isLeader, final Team team) {
        this.teamLeave(role, this.roleService.getPlayer(team.getLeaderUid()));
    }
    
    @Override
    public void handleTeamJoinEvent(final Role role) {
        if (this.objData == null) {
            return;
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Map<Long, GangWarsPkData> dataMap = this.objData.getPkData().get(role.getFaction());
            final GangWarsInfo gangWarsInfo = this.objData.getWarsMap().get(role.getFaction());
            if (dataMap != null && gangWarsInfo != null) {
                Equipment equipment = this.objData.getGateMap().get(role.getFaction());
                if (equipment != null) {
                    equipment.getList().add(role.getId());
                    MessagePusher.pushMessage(role.getId(), new RespGangWarsRoleState((byte)3, equipment.getTime()));
                    return;
                }
                equipment = this.objData.getCannonMap().get(gangWarsInfo.getVisible());
                if (equipment != null) {
                    equipment.getList().add(role.getId());
                    MessagePusher.pushMessage(role.getId(), new RespGangWarsRoleState((byte)2, equipment.getTime()));
                }
            }
        }
    }
    
    public void teamLeave(final Role role, final Role leader) {
        if (this.objData == null || leader == null) {
            return;
        }
        final Map<Long, GangWarsPkData> dataMap = this.objData.getPkData().get(role.getFaction());
        final GangWarsInfo gangWarsInfo = this.objData.getWarsMap().get(role.getFaction());
        if (dataMap == null || gangWarsInfo == null) {
            return;
        }
        final GangWarsPkData gangWarsPkData = dataMap.get(role.getId());
        if (gangWarsPkData == null || gangWarsInfo.getCombatResults() != 3) {
            return;
        }
        Equipment equipment = this.objData.getGateMap().get(role.getFaction());
        if (equipment != null && equipment.getList().contains(role.getId())) {
            equipment.getList().removeIf(e -> e.equals(role.getId()));
            final GangWarsPkData gangWarsPkData2 = dataMap.get(role.getId());
            if (gangWarsPkData2 != null) {
                gangWarsPkData2.setState((byte)0);
            }
            MessagePusher.pushMessage(role.getId(), new RespOffRoleState("wars", (byte)3));
            if (equipment.getList().size() < 3) {
                SpringUtils.getBean(GangWarsPkHandler.class).offGate(leader, this.objData, "队伍人数不足#<R>3#终止操作");
            }
            return;
        }
        equipment = this.objData.getCannonMap().get(gangWarsInfo.getVisible());
        if (equipment != null && equipment.getList().contains(role.getId())) {
            equipment.getList().removeIf(e -> e.equals(role.getId()));
            final GangWarsPkData gangWarsPkData2 = dataMap.get(role.getId());
            if (gangWarsPkData2 != null) {
                gangWarsPkData2.setState((byte)0);
            }
            final RespOffRoleState offRoleState = new RespOffRoleState("wars", (byte)2);
            MessagePusher.pushMessage(role.getId(), offRoleState);
            if (equipment.getList().size() < 3) {
                final GangWarsPkHandler bean = SpringUtils.getBean(GangWarsPkHandler.class);
                bean.cancelTask(equipment.getScheduledFuture());
                this.objData.getCannonMap().remove(gangWarsInfo.getVisible());
                final Team team = this.teamService.getTeam(leader.getRoleId());
                if (team != null) {
                    for (int i = 0; i < team.getList().size(); ++i) {
                        final Member member = team.getList().get(i);
                        if (member.isInTeam()) {
                            final GangWarsPkData warsPkData = dataMap.get(member.getId());
                            if (warsPkData != null) {
                                warsPkData.setState((byte)0);
                            }
                            MessagePusher.pushMessage(member.getId(), offRoleState);
                            MessagePusher.pushMessage(member.getId(), new RespNotifyMiscEx(member.getId(), "队伍人数不足#<R>3#终止操作", true));
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void handleFightEnd(final FightEndEvent fightEndEvent) {
    }
    
    public boolean isInMap(final Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 3001);
    }
    
    public boolean isCompetePk(final Role role, final Role pkRole) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("你现在不能与人切磋。"));
            return false;
        }
        if (this.isInMap(pkRole)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("你现在不能与人切磋。"));
            return false;
        }
        return true;
    }
    
    public void loadCommonSet(final Map<String, String> commonSetMap) {
        this.loadSet(commonSetMap);
    }
    
    private void loadSet(final Map<String, String> commonSetMap) {
        final String value = commonSetMap.get("wars_team_set");
        GangWarsSet objSet = null;
        if (value != null) {
            objSet = (GangWarsSet)Json.fromJson(GangWarsSet.class, (CharSequence)value);
        }
        else {
            objSet = new GangWarsSet();
        }
        this.objSet = objSet;
    }
    
    public void notifyAt(final byte round) {
        final String day = DateUtils.dayForWeek(new Date());
        final GangWarsTimeSlot timeSlot = this.objSet.getDayOfWeek().stream().filter(e -> e.getDayOfWeek().equals(day) && e.getRound() == round).findFirst().orElse(null);
        if (timeSlot == null) {
            return;
        }
        this.round = round;
        this.prepareOnce(timeSlot);
    }
    
    public void cs() {
        this.objData = new GangWarsData();
        final Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTime(new Date());
        final GangWarsSet objSet = this.getObjSet();
        calendarStart.add(12, 1);
        this.objData.setSignUpTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, 2);
        this.objData.setStartTime(EventsUtils.getTime(calendarStart));
        calendarStart.add(12, objSet.getSustain());
        this.objData.setEndTime(EventsUtils.getTime(calendarStart));
        this.addCheckJob();
        this.addStartJob();
        this.addEndJob();
        this.addBroadcastJob();
        this.initMatchingParty();
    }
    
    public GangWarsSet getObjSet() {
        return this.objSet;
    }
    
    public GangWarsData getObjData() {
        return this.objData;
    }
    
    public void setObjData(final GangWarsData objData) {
        this.objData = objData;
    }
}
