
package com.kitty.system.game.task.service.handler.dungeon;

import java.io.Serializable;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.HERO;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.common.thread.IdGenerator;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.listener.event.LoginEvent;
import java.util.Arrays;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import java.util.concurrent.ScheduledFuture;
import java.util.Iterator;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.confirm.model.CreateDungeonMwkFrontConfirm;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.task.message.RespBallotResult;
import com.kitty.system.game.task.message.vo.BallotInfo;
import java.util.ArrayList;
import com.kitty.mina.message.Message;
import com.kitty.system.game.task.message.RespTeamBallot;
import java.util.HashMap;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.i18n.P_I18nId;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.vo.Npc;
import javax.annotation.PostConstruct;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.kitty.system.game.task.service.handler.TaskHandler;

@Component
public class DungeonMwkFront extends TaskHandler
{
    private static final int FIRST_TASK_ID = 1700001;
    private static final int MIN_TEAM_COUNT = 3;
    private static final int MIN_RELIVE = 2;
    private static final int MIN_LEVEL = 130;
    private static final int MAX_LEVEL_DIFF = 15;
    private static final String DUNGEON_NAME = "魔王窟前殿";
    private static final int SHOW_SECS = 30;
    private static final Map<String, Integer> DUNGEON_STAGE_FIGHT_MAP;
    private static final List<Integer> DUNGEON_MAP;
    public static final int DUNGEON_ENTER_MAP;
    private static final int DUNGEON_ENTER_X = 23;
    private static final int DUNGEON_ENTER_Y = 25;
    private static final int EXIT_MAP = 1004;
    private static final int EXIT_X = 36;
    private static final int EXIT_Y = 97;
    private double PROB;
    
    public DungeonMwkFront() {
        this.PROB = 0.35;
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.MWK_FRONT;
    }
    
    @Override
    public boolean resetTask(final Role role, final TaskSet taskSet) {
        return false;
    }
    
    @PostConstruct
    public void init() {
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP), role -> DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), 0L));
    }
    
    public String getDifficulty(final String type) {
        String difficulty = "#<LG1>普通#";
        if ("m_f_z".equals(type)) {
            difficulty = "#<B>卓越#";
        }
        return difficulty;
    }
    
    public void chooseEnterDungeon(final Role role, final Npc npc, final String type) {
        this.createDungeonAndBallot(role, npc, type);
    }
    
    private void createDungeonAndBallot(final Role role, final Npc npc, final String type) {
        final String talk = this.checkCreateDungeon(role);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        this.createDungeonBallot(role, npc, type);
    }
    
    private String checkCreateDungeon(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team == null) {
            return I18nIdDataPool.getI18nContent(1500);
        }
        if (!this.teamService.isTeamLeader(role) || team.getList().size() < 3) {
            return I18nIdDataPool.getI18nContent(1500);
        }
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return I18nIdDataPool.getI18nContent(1510, names);
        }
        names = this.teamService.checkMember(team, memberRole -> this.teamService.checkMember(team, mRole -> memberRole.getRelive() * 1000 + memberRole.getLevel() - (mRole.getRelive() * 1000 + mRole.getLevel()) > 15) != null);
        if (names != null) {
            return I18nIdDataPool.getI18nContent(1536, 15);
        }
        if (this.teamService.getTeamCount(role) < 3) {
            MessagePusher.notify2Player(role, 1501, "魔王窟前殿", 3);
            return "";
        }
        names = this.teamService.checkMember(role, memberRole -> DataGlobalFunc.needGradeJudge(2, 130, memberRole.getRelive(), memberRole.getLevel(), "#<R>角色等级#不足") != null);
        if (names == null) {
            return null;
        }
        final P_I18nId idResource = I18nIdDataPool.ids.get(1523);
        if (idResource != null) {
            final String content = String.format(idResource.getContent(), names, String.format("%d转%d级", 2, 130));
            return content;
        }
        return null;
    }
    
    public void createDungeonBallot(final Role role, final Npc npc, final String type) {
        if (this.teamService.isTeamLeader(role)) {
            final Map<Integer, Short> ballotResults = new HashMap<Integer, Short>(5);
            this.teamService.memberHandle(role, memberRole -> {
                final Short n = ballotResults.put(memberRole.getRoleId(), (short) 0);
                return;
            });
            ballotResults.put(role.getRoleId(), (short)1);
            final RespTeamBallot respTeamBallot = new RespTeamBallot();
            respTeamBallot.setTeamLeader(this.teamService.isTeamLeader(role));
            final String difficulty = this.getDifficulty(type);
            final String msg = I18nIdDataPool.getI18nContent(1506, difficulty, "魔王窟前殿");
            respTeamBallot.setMsg(msg);
            final long millis = System.currentTimeMillis();
            respTeamBallot.setEndTime(new Long(millis / 1000L).intValue() + 30);
            final String title = I18nIdDataPool.getI18nContent(1507, "魔王窟前殿");
            respTeamBallot.setTitle(title);
            this.teamService.pushMessage(role, respTeamBallot);
            final List<BallotInfo> ballotInfoList = new ArrayList<BallotInfo>();
            ballotInfoList.add(new BallotInfo(role.getSerial(), role.getName(), (short)1));
            for (final Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
                if (!entry.getKey().equals(role.getRoleId())) {
                    Role memberRole = this.roleService.getPlayer(entry.getKey());
                    if (memberRole == null) {
                        continue;
                    }
                    final BallotInfo ballotInfo = new BallotInfo(memberRole.getSerial(), memberRole.getName(), entry.getValue());
                    ballotInfoList.add(ballotInfo);
                }
            }
            final RespBallotResult respBallotResult = new RespBallotResult();
            respBallotResult.setResultList(ballotInfoList);
            this.teamService.pushMessage(role, respBallotResult);
            final ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> this.countdown(role, npc), 30000L);
            role.setConfirm(new CreateDungeonMwkFrontConfirm(ballotResults, schedule, type));
        }
    }
    
    public void createBurgeonBallotDisagree(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            final Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            final CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
            final Map<Integer, Short> ballotResults = confirm.getBallotResults();
            ballotResults.put(role.getRoleId(), (short)2);
            final RespBallotResult respBallotResult = this.getRespBallotResult(role);
            this.teamService.pushMessageToTeam(team, respBallotResult);
            if (this.isAllOp(ballotResults)) {
                this.cancelTask(confirm.getSchedule());
                this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
                this.refuseInform(team, ballotResults);
            }
        }
    }
    
    private RespBallotResult getRespBallotResult(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        Map<Integer, Short> ballotResults = null;
        Role leaderRole = null;
        if (team.isLeader(role.getRoleId())) {
            leaderRole = role;
            final CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        else {
            leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            final CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        final ArrayList<BallotInfo> ballotInfoList = new ArrayList<BallotInfo>();
        if (leaderRole != null) {
            final BallotInfo ballotInfo = new BallotInfo(leaderRole.getSerial(), leaderRole.getName(), (short)1);
            ballotInfoList.add(ballotInfo);
        }
        for (final Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
            final Role memberRole = this.roleService.getPlayer(entry.getKey());
            if (memberRole != null) {
                if (memberRole.getRoleId().equals(leaderRole.getRoleId())) {
                    continue;
                }
                final BallotInfo ballotInfo2 = new BallotInfo(memberRole.getSerial(), memberRole.getName(), entry.getValue());
                ballotInfoList.add(ballotInfo2);
            }
        }
        final RespBallotResult respBallotResult = new RespBallotResult();
        respBallotResult.setResultList(ballotInfoList);
        return respBallotResult;
    }
    
    public void createBurgeonBallotAgree(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        final Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        final CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
        final Map<Integer, Short> ballotResults = confirm.getBallotResults();
        ballotResults.put(role.getRoleId(), (short)1);
        final RespBallotResult respBallotResult = this.getRespBallotResult(role);
        this.teamService.pushMessageToTeam(team, respBallotResult);
        if (this.isAllOp(ballotResults)) {
            this.cancelTask(confirm.getSchedule());
            this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
            boolean allAgree = true;
            for (final short result : ballotResults.values()) {
                if (result != 1) {
                    allAgree = false;
                }
            }
            if (allAgree) {
                this.createDungeon(team);
                final TaskSet taskSet = this.taskService.getTaskSet(1700001);
                taskSet.setPara(confirm.getType());
                this.teamService.memberHandleThreadLocal(team, memberRole -> this.acceptTask(memberRole, taskSet, -1));
                this.doEnterDungeon(leaderRole);
                this.teamService.memberHandleThreadLocal(team, memberRole -> this.taskScreenIcon(memberRole, taskSet.getSerial(), confirm.getType(), (byte)1));
            }
            else {
                this.refuseInform(team, ballotResults);
            }
        }
    }
    
    public void refuseInform(final Team team, final Map<Integer, Short> ballotResults) {
        if (team != null && ballotResults.size() > 0) {
            final Npc npc = new Npc(DataCache.PLACE_NPC.get(90236));
            final StringBuffer buffer = new StringBuffer();
            for (final Integer key : ballotResults.keySet()) {
                final short value = ballotResults.get(key);
                if (2 == value) {
                    final Role player = this.roleService.getPlayer(key);
                    if (player == null) {
                        continue;
                    }
                    buffer.append(player.getName() + "、");
                }
            }
            if (buffer.length() > 1) {
                this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿"));
            }
        }
    }
    
    public void cancelTask(final ScheduledFuture<?> task) {
        if (task != null && !task.isDone()) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    public void countdown(final Role role, final Npc npc) {
        final CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
        if (createDungeonMwkFrontConfirm != null) {
            final Map<Integer, Short> ballotResults = createDungeonMwkFrontConfirm.getBallotResults();
            if (!this.isAllOp(ballotResults)) {
                final StringBuffer buffer = new StringBuffer();
                for (final Integer key : ballotResults.keySet()) {
                    final short value = ballotResults.get(key);
                    if (0 == value) {
                        final Role player = this.roleService.getPlayer(key);
                        if (player == null) {
                            continue;
                        }
                        buffer.append(player.getName() + "、");
                    }
                }
                if (buffer.length() > 1) {
                    final Team team = this.teamService.getTeam(role.getRoleId());
                    this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿"));
                }
            }
        }
    }
    
    public int getDungeonFinish(final Role role) {
        return role.getTaskProgress().getFinishRecord().getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
    }
    
    private void addActivityCount(final Role role) {
        int dungeonFinish = this.getDungeonFinish(role) + 1;
        dungeonFinish = ((dungeonFinish > ActivityType.MWK_FRONT.getTotalCount()) ? ActivityType.MWK_FRONT.getTotalCount() : dungeonFinish);
        role.getTaskProgress().getFinishRecord().setv(ActivityType.MWK_FRONT.getActivityId(), dungeonFinish);
    }
    
    private void doEnterDungeon(final Role role) {
        this.mapService.changeMap(role, new ReqTelePort(DungeonMwkFront.DUNGEON_ENTER_MAP, 23, 25), null);
    }
    
    private boolean isAllOp(final Map<Integer, Short> ballotResults) {
        boolean allOp = true;
        for (final short result : ballotResults.values()) {
            if (result == 0) {
                allOp = false;
            }
        }
        return allOp;
    }
    
    private boolean isDungeonMember(final Role role) {
        return role != null && DataCache.ROLE_DUNGEON.containsKey(role.getId());
    }
    
    public void startFight(final Role role, final Npc npc, final String type) {
        final String talk = this.getNpcContentNotFight(role, npc);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        final TaskSet taskSet = this.getTaskSetByType(role);
        if (type == null) {
            int serial = DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get("m_f_c");
            final String para = TaskHandler.getTaskPara(role, taskSet);
            if (para != null && DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get(para) != null) {
                serial = DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get(para);
            }
            this.taskService.taskTriggerFight(role, taskSet, serial);
        }
        else {
            final String[] split = type.split("_");
            if (split.length < 1) {
                return;
            }
            final String serial2 = split[split.length - 1];
            final boolean number = NumberUtil.isNumber(serial2);
            if (number) {
                final Team team = this.teamService.getTeam(role.getRoleId());
                this.teamService.memberHandleThreadLocal(team, memberRole -> role.pushTempCache(String.valueOf(TaskType.MWK_FRONT), type));
                this.taskService.taskTriggerFight(role, taskSet, Integer.valueOf(serial2));
            }
        }
    }
    
    private String getNpcContentNotFight(final Role role, final Npc npc) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return "请老老实实打完你的战斗";
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return String.format("请组队满%d人再来战斗", 3);
        }
        return null;
    }
    
    public void clear(final Role role, final List<SysDataTaskMo> taskMoList) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                for (final Member member : team.getList()) {
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        this.clearDungeonTask(player, taskMoList);
                    }
                }
            }
        }
        else {
            this.clearDungeonTask(role, taskMoList);
        }
    }
    
    public void clearDungeonTask(final Role role, final List<SysDataTaskMo> taskMoList) {
        for (final SysDataTaskMo taskMo : taskMoList) {
            final TaskSet taskSet = this.taskService.getTaskSet(taskMo.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                this.delTask(role, taskSet);
                this.deleteDungeon(this.getDungeon(role));
            }
        }
    }
    
    public boolean isInviteJoinTeam(final Role role, final int inviteRoleId) {
        final Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        final List<Integer> list = Arrays.asList(DungeonMwkFront.DUNGEON_ENTER_MAP);
        if (!this.isCanTeam(role, invitRole, list)) {
            if (this.isInDungeon(role, list)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法邀请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请。"));
            }
            return false;
        }
        return true;
    }
    
    public boolean isAcceptJoinTeam(final Role role, final int joinRoleId) {
        final Role joinRole = this.roleService.getOnlinePlayer(joinRoleId);
        if (!this.isCanTeam(role, joinRole, DungeonMwkFront.DUNGEON_MAP)) {
            if (this.isInDungeon(role, DungeonMwkFront.DUNGEON_MAP)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法操作申请队员。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受操作。"));
            }
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(final Team team, final Role memberRole) {
        final Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        if (!this.isCanTeam(leaderRole, memberRole, DungeonMwkFront.DUNGEON_MAP)) {
            if (this.isInDungeon(leaderRole, DungeonMwkFront.DUNGEON_MAP)) {
                MessagePusher.pushMessage(memberRole, new RespMsg("队长所在地图无法直接进入。"));
            }
            else {
                MessagePusher.pushMessage(memberRole, new RespMsg("本地图无法直接进入。"));
            }
            return false;
        }
        return true;
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        boolean flag = false;
        final List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        if (role.getPos().getMapId().equals(DungeonMwkFront.DUNGEON_ENTER_MAP)) {
            DoingTask doingTask = null;
            for (final SysDataTaskMo task : taskMoList) {
                if (role.getTask().isAccepted(task.getSerial())) {
                    flag = true;
                    doingTask = role.getTask().getDoingTask().get(task.getSerial());
                    break;
                }
            }
            if (flag) {
                if (doingTask != null && doingTask.getPara() != null) {
                    if (!this.isDungeonMember(role)) {
                        final List<Long> memberList = new ArrayList<Long>();
                        memberList.add(role.getId());
                        final Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
                        DataCache.DUNGEON_MAP.put(dugeon.getId(), dugeon);
                        for (final long uid : memberList) {
                            DataCache.ROLE_DUNGEON.put(uid, dugeon.getId());
                        }
                    }
                    final Map<Integer, Short> ballotResults = new HashMap<Integer, Short>(5);
                    role.setConfirm(new CreateDungeonMwkFrontConfirm(ballotResults, null, doingTask.getPara()));
                    this.init();
                    this.taskScreenIcon(role, doingTask.getTaskId(), doingTask.getPara(), (byte)1);
                }
                else {
                    this.leave(role);
                }
            }
            else {
                this.leaveDungeon(role, 1004, 36, 97, null);
            }
        }
    }
    
    public void leave(final Role role) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && !team.isLeader(role.getRoleId())) {
            MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
            return;
        }
        final List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        this.closeTaskIcon(team, role, taskMoList);
        this.clear(role, TaskDataPool.taskMoList);
        this.leaveDungeon(role, 1004, 36, 97, null);
    }
    
    public void closeTaskIcon(final Team team, final Role role, final List<SysDataTaskMo> taskMoList) {
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                for (final Member member : team.getList()) {
                    final Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        this.closeTaskIcon(player, this.getDungeonTask(taskMoList, role));
                    }
                }
            }
        }
        else {
            this.closeTaskIcon(role, this.getDungeonTask(taskMoList, role));
        }
    }
    
    public void closeTaskIcon(final Role role, final DoingTask dungeonTask) {
        int serial = 0;
        String difficulty = null;
        if (dungeonTask != null) {
            serial = dungeonTask.getTaskId();
            difficulty = dungeonTask.getPara();
        }
        this.taskScreenIcon(role, serial, difficulty, (byte)0);
    }
    
    public DoingTask getDungeonTask(final List<SysDataTaskMo> taskMoList, final Role role) {
        DoingTask doingTask = null;
        for (final SysDataTaskMo task : taskMoList) {
            if (role.getTask().isAccepted(task.getSerial())) {
                doingTask = role.getTask().getDoingTask().get(task.getSerial());
                break;
            }
        }
        return doingTask;
    }
    
    @Override
    public void giveReward(final Role role, final TaskSet taskSet) {
        String difficulty = "m_f_c";
        final List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        final DoingTask doingTask = this.getDungeonTask(taskMoList, role);
        if (taskSet.getSerial() == 1700001) {
            this.addActivityCount(role);
        }
        boolean flag = true;
        if (this.getDungeonFinish(role) < ActivityType.MWK_FRONT.getTotalCount()) {
            String type = null;
            if (doingTask == null) {
                flag = false;
                difficulty = "m_f_c";
                final CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
                if (createDungeonMwkFrontConfirm != null) {
                    difficulty = createDungeonMwkFrontConfirm.getType();
                }
                type = role.getTempCache(String.valueOf(TaskType.MWK_FRONT), "threats_mo_c_170002");
            }
            else if (doingTask.getPara() != null) {
                difficulty = doingTask.getPara();
            }
            this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), difficulty, ItemClassify.HERO_EXP, flag), "魔王窟前殿-副本", true);
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), difficulty, ItemClassify.PET_EXP, flag), true);
                }
            }
            this.specialReward(role, difficulty, flag, type);
        }
        if (this.teamService.isTeamLeader(role) && doingTask == null) {
            this.closeTaskIcon(this.teamService.getTeam(role.getRoleId()), role, taskMoList);
            this.leaveDungeon(role, 1004, 36, 97, null);
        }
    }
    
    private static long calculate(final int grade, final int rei, final String difficulty, final ItemClassify type, final boolean flag) {
        final double fold = ActivityType.MWK_FRONT.getFold();
        double exp = (double)BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0).longValue();
        if (ItemClassify.PET_EXP == type) {
            exp = (double)BigDecimalUtil.decimalScaleDown(0, exp * 1.8).longValue();
        }
        if (difficulty.equals("m_f_z")) {
            exp *= 3.0;
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
    
    public void specialReward(final Role role, final String difficulty, final boolean flag, final String type) {
        double prob = this.PROB;
        int num = 1;
        if (difficulty.equals("m_f_z")) {
            num += RandomUtil.round(1, 3);
            prob += 0.3;
        }
        if (!flag && type != null && type.equals("threats_mo_z_170003")) {
            num += RandomUtil.round(1, 2);
        }
        if (SkillAi.canAffectOnPro(prob)) {
            final List<Lottery> lotteries = getPlayerAcsry(role.getRace(), role.getSex());
            for (int i = 0; i < num; ++i) {
                final Lottery lottery = lottery(lotteries);
                if (lottery.getNum() > 0) {
                    SpringUtils.getBean(BagService.class).rewardItem(role, lottery.getSerial(), lottery.getNum(), 0, "魔王窟前殿");
                }
            }
        }
    }
    
    public static Lottery lottery(final List<Lottery> list) {
        final List<Double> rates = new ArrayList<Double>(list.size());
        for (final Lottery lottery : list) {
            rates.add(lottery.getBili());
        }
        return list.get(LotteryUtils.lottery(rates));
    }
    
    public static List<Lottery> getPlayerAcsry(final Integer race, final Integer sex) {
        final double min = 3.0;
        final List<SysDataTaskMoAward> moAwardList = TaskDataPool.moAwardMap.get(TaskType.MWK_FRONT.getId());
        final List<Lottery> list = new ArrayList<Lottery>();
        if (race == RACE.RACE_REN.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getRenMale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getRenMale(), 1));
                }
            }
            else {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getRenFemale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getRenFemale(), 1));
                }
            }
        }
        else if (race == RACE.RACE_MO.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getMoMale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getMoMale(), 1));
                }
            }
            else {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getMoFemale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getMoFemale(), 1));
                }
            }
        }
        else if (race == RACE.RACE_XIAN.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getXianMale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getXianMale(), 1));
                }
            }
            else {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getXianFemale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getXianFemale(), 1));
                }
            }
        }
        else if (race == RACE.RACE_GUI.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getGuiMale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getGuiMale(), 1));
                }
            }
            else {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getGuiFemale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getGuiFemale(), 1));
                }
            }
        }
        else if (race == RACE.RACE_LONG.getValue()) {
            if (sex == HERO.HERO_MALE.getValue()) {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getLongMale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getLongMale(), 1));
                }
            }
            else {
                final List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getLongFemale() > 0).collect(Collectors.toList());
                for (final SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double)award.getLongFemale(), 1));
                }
            }
        }
        return list;
    }
    
    static {
        DUNGEON_STAGE_FIGHT_MAP = new HashMap<String, Integer>() {
            {
                this.put("m_f_c", 170001);
                this.put("m_f_z", 170001);
            }
        };
        DUNGEON_MAP = Arrays.asList(2011);
        DUNGEON_ENTER_MAP = DungeonMwkFront.DUNGEON_MAP.get(0);
    }
}
