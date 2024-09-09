package com.kitty.system.game.task.service.handler.dungeon;

import java.io.Serializable;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.wardef.HERO;
import com.kitty.system.enums.war.wardef.RACE;
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
import com.kitty.system.common.message.RespMsg;
import java.util.Arrays;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import java.util.concurrent.ScheduledFuture;
import java.util.Iterator;
import com.kitty.system.game.confirm.model.CreateDungeonCmConfirm;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.task.message.RespBallotResult;
import com.kitty.system.game.task.message.vo.BallotInfo;
import java.util.ArrayList;
import com.kitty.system.game.task.message.RespTeamBallot;
import java.util.HashMap;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.i18n.P_I18nId;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.team.domain.Team;
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
public class DungeonCmHandler extends TaskHandler
{
    private static final int FIRST_TASK_ID = 2700001;
    private static final int MIN_TEAM_COUNT = 1;
    private static final int MIN_RELIVE = 2;
    private static final int MIN_LEVEL = 130;
    private static final int MAX_LEVEL_DIFF = 15;
    private static final String DUNGEON_NAME = "地宫除魔";
    private static final int SHOW_SECS = 30;
    private static final Map<String, Integer> DUNGEON_STAGE_FIGHT_MAP;
    private static final List<Integer> DUNGEON_MAP;
    public static final int DUNGEON_ENTER_MAP;
    private static final int DUNGEON_ENTER_X = 23;
    private static final int DUNGEON_ENTER_Y = 25;
    private static final int EXIT_MAP = 1000;
    private static final int EXIT_X = 64;
    private static final int EXIT_Y = 16;
    private double PROB;
    
    public DungeonCmHandler() {
        this.PROB = 0.35;
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.CM;
    }
    
    @Override
    public boolean resetTask(Role role, TaskSet taskSet) {
        return false;
    }
    
    @PostConstruct
    public void init() {
        MapDataPool.MAP_KEY_GENERATE.put(DungeonCmHandler.DUNGEON_ENTER_MAP, role -> DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
    }
    
    public String getDifficulty(String type) {
        String difficulty = "#<LG1>普通#";
        if ("d_f_z".equals(type)) {
            difficulty = "#<B>卓越#";
            return difficulty;
        }
        return difficulty;
    }
    
    public void chooseEnterDungeon(Role role, Npc npc, String type) {
        this.createDungeonAndBallot(role, npc, type);
    }
    
    private void createDungeonAndBallot(Role role, Npc npc, String type) {
        String talk = this.checkCreateDungeon(role);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        Team team = this.teamService.getTeam(role.getRoleId());
        Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        this.enterDungeon(team, leaderRole, type);
    }
    
    private String checkCreateDungeon(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team == null) {
            return String.format("请至少组满%d位大侠再来挑战吧！#<E:99>#", Integer.valueOf(1));
        }
        if (!this.teamService.isTeamLeader(role) || team.getList().size() < 1) {
            return String.format("请至少组满%d位大侠再来挑战吧！#<E:99>#", Integer.valueOf(1));
        }
        String names = this.teamService.getNotInTeam(team);
        if (names != null) {
            return String.format("队伍中#<Y>%s#处于暂离状态，不能创建副本", names);
        }
        names = this.teamService.checkMember(team, memberRole -> this.teamService.checkMember(team, mRole -> (int)memberRole.getRelive() * 1000 + (int)memberRole.getLevel() - ((int)mRole.getRelive() * 1000 + (int)mRole.getLevel()) > 15) != null);
        if (names != null) {
            return String.format("队伍中的成员的最大等级差不能超过#<R>%s级#", Integer.valueOf(15));
        }
        if (this.teamService.getTeamCount(role) < 1) {
            MessagePusher.notify2Player(role, 1501, "地宫除魔", Integer.valueOf(1));
            return "";
        }
        names = this.teamService.checkMember(role, memberRole -> DataGlobalFunc.needGradeJudge(2, 130, (int)memberRole.getRelive(), (int)memberRole.getLevel(), "#<R>角色等级#不足") != null);
        if (names == null) {
            return null;
        }
        P_I18nId idResource = I18nIdDataPool.ids.get(Integer.valueOf(1523));
        if (idResource != null) {
            return String.format(idResource.getContent(), names, String.format("%d转%d级", Integer.valueOf(2), Integer.valueOf(130)));
        }
        return null;
    }
    
    private void enterDungeon(Team team, Role leaderRole, String type) {
        this.createDungeon(team);
        TaskSet taskSet = this.taskService.getTaskSet(2700001);
        taskSet.setPara(type);
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.acceptTask(memberRole, taskSet, -1));
        this.doEnterDungeon(leaderRole);
        this.teamService.memberHandleThreadLocal(team, memberRole -> this.taskScreenIcon(memberRole, taskSet.getSerial(), type, (byte)1));
    }
    
    public void createDungeonBallot(Role role, Npc npc, String type) {
        if (this.teamService.isTeamLeader(role)) {
            Map<Integer, Short> ballotResults = new HashMap<>(5);
            this.teamService.memberHandle(role, memberRole -> ballotResults.put(memberRole.getRoleId(), Short.valueOf((short)0)));
            ballotResults.put(role.getRoleId(), (short) 1);
            RespTeamBallot respTeamBallot = new RespTeamBallot();
            respTeamBallot.setTeamLeader(this.teamService.isTeamLeader(role));
            String difficulty = this.getDifficulty(type);
            String msg = String.format("正在进入%s难度#<R>%s#，请各位大侠确认！", difficulty, "地宫除魔");
            respTeamBallot.setMsg(msg);
            long millis = System.currentTimeMillis();
            respTeamBallot.setEndTime(new Long(millis / 1000L).intValue() + 30);
            String title = String.format("进入%s", "地宫除魔");
            respTeamBallot.setTitle(title);
            this.teamService.pushMessage(role, respTeamBallot);
            List<BallotInfo> ballotInfoList = new ArrayList<>();
            ballotInfoList.add(new BallotInfo(role.getSerial(), role.getName(), (short)1));
            for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
                if (!entry.getKey().equals(role.getRoleId())) {
                	Role memberRole = this.roleService.getPlayer(entry.getKey());
                    if (memberRole == null) {
                        continue;
                    }
                    BallotInfo ballotInfo = new BallotInfo(memberRole.getSerial(), memberRole.getName(), entry.getValue());
                    ballotInfoList.add(ballotInfo);
                }
            }
            RespBallotResult respBallotResult = new RespBallotResult();
            respBallotResult.setResultList(ballotInfoList);
            this.teamService.pushMessage(role, respBallotResult);
            ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> this.countdown(role, npc), 30000L);
            role.setConfirm(new CreateDungeonCmConfirm(ballotResults, schedule, type));
        }
    }
    
    public void createBurgeonBallotDisagree(Role role) {
        Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            CreateDungeonCmConfirm confirm = (CreateDungeonCmConfirm)leaderRole.getConfirm();
            Map<Integer, Short> ballotResults = confirm.getBallotResults();
            ballotResults.put(role.getRoleId(), (short) 2);
            RespBallotResult respBallotResult = this.getRespBallotResult(role);
            this.teamService.pushMessageToTeam(team, respBallotResult);
            if (this.isAllOp(ballotResults)) {
                this.cancelTask(confirm.getSchedule());
                this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
                this.refuseInform(team, ballotResults);
            }
        }
    }
    
    private RespBallotResult getRespBallotResult(Role role) {
        Team team = this.teamService.getTeam(role.getRoleId());
        Role leaderRole;
        Map<Integer, Short> ballotResults;
        if (team.isLeader(role.getRoleId())) {
            leaderRole = role;
            CreateDungeonCmConfirm confirm = (CreateDungeonCmConfirm)role.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        else {
            leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            CreateDungeonCmConfirm confirm = (CreateDungeonCmConfirm)leaderRole.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        ArrayList<BallotInfo> ballotInfoList = new ArrayList<>();
        if (leaderRole != null) {
            BallotInfo ballotInfo = new BallotInfo(leaderRole.getSerial(), leaderRole.getName(), (short)1);
            ballotInfoList.add(ballotInfo);
        }
        for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
            Role memberRole = this.roleService.getPlayer(entry.getKey());
            if (memberRole != null) {
                if (memberRole.getRoleId().equals(leaderRole.getRoleId())) {
                    continue;
                }
                BallotInfo ballotInfo2 = new BallotInfo(memberRole.getSerial(), memberRole.getName(), entry.getValue());
                ballotInfoList.add(ballotInfo2);
            }
        }
        RespBallotResult respBallotResult = new RespBallotResult();
        respBallotResult.setResultList(ballotInfoList);
        return respBallotResult;
    }
    
    public void createBurgeonBallotAgree(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        CreateDungeonCmConfirm confirm = (CreateDungeonCmConfirm)leaderRole.getConfirm();
        Map<Integer, Short> ballotResults = confirm.getBallotResults();
        ballotResults.put(role.getRoleId(), Short.valueOf((short)1));
        RespBallotResult respBallotResult = this.getRespBallotResult(role);
        this.teamService.pushMessageToTeam(team, respBallotResult);
        if (this.isAllOp(ballotResults)) {
            this.cancelTask(confirm.getSchedule());
            this.teamService.memberHandle(team, memberRole -> this.npcService.closeDlg(memberRole, "DungeonVoteDlg"));
            boolean allAgree = true;
            for (short result : ballotResults.values()) {
                if (result != 1) {
                    allAgree = false;
                    break;
                }
            }
            if (allAgree) {
                this.enterDungeon(team, leaderRole, confirm.getType());
            }
            else {
                this.refuseInform(team, ballotResults);
            }
        }
    }
    
    public void refuseInform(Team team, Map<Integer, Short> ballotResults) {
        if (team != null && ballotResults.size() > 0) {
            Npc npc = new Npc(DataCache.PLACE_NPC.get(90045));
            StringBuffer buffer = new StringBuffer();
            for (Integer key : ballotResults.keySet()) {
                short value = ballotResults.get(key);
                if (2 == value) {
                    Role player = this.roleService.getPlayer(key);
                    if (player == null) {
                        continue;
                    }
                    buffer.append(player.getName() + "、");
                }
            }
            if (buffer.length() > 1) {
                this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer + "没有确定，无法开始地宫除魔"));
            }
        }
    }
    
    public void cancelTask(ScheduledFuture<?> task) {
        if (task != null && !task.isDone()) {
            task.cancel(Boolean.FALSE);
        }
    }
    
    public void countdown(Role role, Npc npc) {
        CreateDungeonCmConfirm createDungeonMwkFrontConfirm = (CreateDungeonCmConfirm)role.getConfirm();
        if (createDungeonMwkFrontConfirm != null) {
            Map<Integer, Short> ballotResults = createDungeonMwkFrontConfirm.getBallotResults();
            if (!this.isAllOp(ballotResults)) {
                StringBuffer buffer = new StringBuffer();
                for (Integer key : ballotResults.keySet()) {
                    short value = ballotResults.get(key);
                    if (0 == value) {
                        Role player = this.roleService.getPlayer(key);
                        if (player == null) {
                            continue;
                        }
                        buffer.append(player.getName() + "、");
                    }
                }
                if (buffer.length() > 1) {
                    Team team = this.teamService.getTeam(role.getRoleId());
                    this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer + "没有确定，无法开始地宫除魔"));
                }
            }
        }
    }
    
    public int getDungeonFinish(Role role) {
        return role.getTaskProgress().getFinishRecord().getInt(ActivityType.CM_TASK.getActivityId(), 0);
    }
    
    private void addActivityCount(Role role) {
        int dungeonFinish = this.getDungeonFinish(role) + 1;
        dungeonFinish = ((dungeonFinish > ActivityType.CM_TASK.getTotalCount()) ? ActivityType.CM_TASK.getTotalCount() : dungeonFinish);
        role.getTaskProgress().getFinishRecord().setv(ActivityType.CM_TASK.getActivityId(), Integer.valueOf(dungeonFinish));
    }
    
    private void doEnterDungeon(Role role) {
        this.mapService.changeMap(role, new ReqTelePort(DungeonCmHandler.DUNGEON_ENTER_MAP, 23, 25), null);
    }
    
    private boolean isAllOp(Map<Integer, Short> ballotResults) {
        boolean allOp = true;
        for (short result : ballotResults.values()) {
            if (result == 0) {
                allOp = false;
                break;
            }
        }
        return allOp;
    }
    
    private boolean isDungeonMember(Role role) {
        return role != null && DataCache.ROLE_DUNGEON.containsKey(role.getId());
    }
    
    public void startFight(Role role, Npc npc, String type) {
        String talk = this.getNpcContentNotFight(role, npc);
        if (talk != null) {
            this.npcService.sendNpcContent(role, npc, "", talk);
            return;
        }
        TaskSet taskSet = this.getTaskSetByType(role);
        if (type == null) {
            int serial3 = (int)DungeonCmHandler.DUNGEON_STAGE_FIGHT_MAP.get("d_f_c");
            String para = TaskHandler.getTaskPara(role, taskSet);
            if (para != null && DungeonCmHandler.DUNGEON_STAGE_FIGHT_MAP.get(para) != null) {
                serial3 = (int)DungeonCmHandler.DUNGEON_STAGE_FIGHT_MAP.get(para);
            }
            this.taskService.taskTriggerFight(role, taskSet, serial3);
        }
        else {
            String[] split = type.split("_");
            if (split.length < 1) {
                return;
            }
            String serial2 = split[split.length - 1];
            boolean number = NumberUtil.isNumber(serial2);
            if (number) {
                Team team = this.teamService.getTeam(role.getRoleId());
                this.teamService.memberHandleThreadLocal(team, memberRole -> role.pushTempCache(String.valueOf(TaskType.CM), type));
                this.taskService.taskTriggerFight(role, taskSet, Integer.valueOf(serial2));
            }
        }
        return;
    }
    
    private String getNpcContentNotFight(Role role, Npc npc) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return "请老老实实打完你的战斗";
        }
        if (this.teamService.getTeamCount(role) < 1) {
            return String.format("请组队满%d人再来战斗", 1);
        }
        return null;
    }
    
    public void clear(Role role, List<SysDataTaskMo> taskMoList) {
        Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                for (Member member : team.getList()) {
                    Role player = this.roleService.getPlayer(member.getId());
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
    
    public void clearDungeonTask(Role role, List<SysDataTaskMo> taskMoList) {
        for (SysDataTaskMo taskMo : taskMoList) {
            TaskSet taskSet = this.taskService.getTaskSet(taskMo.getSerial());
            if (this.taskService.isTask(role, taskSet)) {
                this.delTask(role, taskSet);
                this.deleteDungeon(this.getDungeon(role));
            }
        }
    }
    
    public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
        Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        List<Integer> list = Arrays.asList(Integer.valueOf(DungeonCmHandler.DUNGEON_ENTER_MAP));
        if (!this.isCanTeam(role, invitRole, list)) {
            if (this.isInDungeon(role, list)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法邀请。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
        Role joinRole = this.roleService.getOnlinePlayer(joinRoleId);
        if (!this.isCanTeam(role, joinRole, DungeonCmHandler.DUNGEON_MAP)) {
            if (this.isInDungeon(role, DungeonCmHandler.DUNGEON_MAP)) {
                MessagePusher.pushMessage(role, new RespMsg("本地图无法操作申请队员。"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受操作。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean isBackToTeam(Team team, Role memberRole) {
        Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        if (!this.isCanTeam(leaderRole, memberRole, DungeonCmHandler.DUNGEON_MAP)) {
            if (this.isInDungeon(leaderRole, DungeonCmHandler.DUNGEON_MAP)) {
                MessagePusher.pushMessage(memberRole, new RespMsg("队长所在地图无法直接进入。"));
            }
            else {
                MessagePusher.pushMessage(memberRole, new RespMsg("本地图无法直接进入。"));
            }
            return false;
        }
        else {
            return true;
        }
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        boolean flag = false;
        List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        if (role.getPos().getMapId().equals(Integer.valueOf(DungeonCmHandler.DUNGEON_ENTER_MAP))) {
            DoingTask doingTask = null;
            for (SysDataTaskMo task : taskMoList) {
                if (role.getTask().isAccepted(task.getSerial())) {
                    flag = true;
                    doingTask = role.getTask().getDoingTask().get(task.getSerial());
                    break;
                }
            }
            if (flag) {
                if (doingTask != null && doingTask.getPara() != null) {
                    if (!this.isDungeonMember(role)) {
                        List<Long> memberList = new ArrayList<>();
                        memberList.add(role.getId());
                        Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
                        DataCache.DUNGEON_MAP.put(dugeon.getId(), dugeon);
                        for (long uid : memberList) {
                            DataCache.ROLE_DUNGEON.put(uid, dugeon.getId());
                        }
                    }
                    Map<Integer, Short> ballotResults = new HashMap<>(5);
                    role.setConfirm(new CreateDungeonCmConfirm(ballotResults, null, doingTask.getPara()));
                    this.init();
                    this.taskScreenIcon(role, doingTask.getTaskId(), doingTask.getPara(), (byte)1);
                }
                else {
                    this.leave(role);
                }
            }
            else {
                this.leaveDungeon(role, 1000, 64, 16, null);
            }
        }
    }
    
    public void leave(Role role) {
        Team team = this.teamService.getTeam(role.getRoleId());
        if (team != null && !team.isLeader(role.getRoleId())) {
            MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
            return;
        }
        List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        this.closeTaskIcon(team, role, taskMoList);
        this.clear(role, TaskDataPool.taskMoList);
        this.clear(role, TaskDataPool.taskMoList);
        this.leaveDungeon(role, 1000, 64, 16, null);
    }
    
    public void closeTaskIcon(Team team, Role role, List<SysDataTaskMo> taskMoList) {
        if (team != null) {
            if (team.isLeader(role.getRoleId())) {
                for (Member member : team.getList()) {
                    Role player = this.roleService.getPlayer(member.getId());
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
    
    public void closeTaskIcon(Role role, DoingTask dungeonTask) {
        int serial = 0;
        String difficulty = null;
        if (dungeonTask != null) {
            serial = dungeonTask.getTaskId();
            difficulty = dungeonTask.getPara();
        }
        this.taskScreenIcon(role, serial, difficulty, (byte)0);
    }
    
    public void closeTaskIcon(Team team, Role role, int serial, String difficulty) {
        if (team != null) {
            if (team.isLeader((int)role.getRoleId())) {
                for (Member member : team.getList()) {
                    Role player = this.roleService.getPlayer(member.getId());
                    if (player != null) {
                        this.taskScreenIcon(role, serial, difficulty, (byte)0);
                    }
                }
            }
        }
        else {
            this.taskScreenIcon(role, serial, difficulty, (byte)0);
        }
    }
    
    public DoingTask getDungeonTask(List<SysDataTaskMo> taskMoList, Role role) {
        DoingTask doingTask = null;
        for (SysDataTaskMo task : taskMoList) {
            if (role.getTask().isAccepted(task.getSerial())) {
                doingTask = role.getTask().getDoingTask().get(task.getSerial());
                break;
            }
        }
        return doingTask;
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        String difficulty = "d_f_c";
        List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        DoingTask doingTask = this.getDungeonTask(taskMoList, role);
        if (taskSet.getSerial() == 2700001) {
            this.addActivityCount(role);
        }
        boolean flag = true;
        int roleFinishCount = this.getDungeonFinish(role);
        if (roleFinishCount < ActivityType.CM_TASK.getTotalCount()) {
            String type = null;
            if (doingTask == null) {
                flag = false;
                difficulty = "d_f_c";
                CreateDungeonCmConfirm createDungeonMwkFrontConfirm = (CreateDungeonCmConfirm)role.getConfirm();
                if (createDungeonMwkFrontConfirm != null) {
                    difficulty = createDungeonMwkFrontConfirm.getType();
                }
                type = role.getTempCache(String.valueOf(TaskType.CM), "threats_mo_c_270002");
            }
            else if (doingTask.getPara() != null) {
                difficulty = doingTask.getPara();
            }
            this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), difficulty, ItemClassify.HERO_EXP, flag), "魔王窟前殿-副本", true);
            Integer combatPet = role.getExtendBox().getCombatPet();
            if ((int)combatPet > 0) {
                Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), difficulty, ItemClassify.PET_EXP, flag), true);
                }
            }
            this.specialReward(role, difficulty, flag, type);
        }
        if (this.teamService.isTeamLeader(role) && doingTask == null) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            this.closeTaskIcon(team, role, taskSet.getSerial(), difficulty);
            this.leaveDungeon(role, 1000, 64, 16, null);
        }
    }
    
    private static long calculate(int grade, int rei, String difficulty, ItemClassify type, boolean flag) {
        double fold = ActivityType.CM_TASK.getFold();
        double exp = (double)BigDecimalUtil.decimalScaleDown(0, 1.6E7 * (1.0 + ((double) grade + (double) rei * 15.0) / 140.0 * 2.5) / 30.0).longValue();
        if (ItemClassify.PET_EXP == type) {
            exp = (double)BigDecimalUtil.decimalScaleDown(0, exp * 1.8).longValue();
        }
        if (difficulty.equals("d_f_z")) {
            exp *= 3.0;
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
    
    public void specialReward(Role role, String difficulty, boolean flag, String type) {
        double prob = this.PROB;
        int num = 1;
        if (difficulty.equals("d_f_z")) {
            num += RandomUtil.round(1, 3);
            prob += 0.3;
        }
        if (!flag && type != null && type.equals("threats_mo_z_270003")) {
            num += RandomUtil.round(1, 2);
        }
        if (SkillAi.canAffectOnPro(prob)) {
            List<Lottery> lotteries = getPlayerAcsry(role.getRace(), role.getSex());
            if (lotteries.isEmpty()) {
                return;
            }
            for (int i = 0; i < num; ++i) {
                Lottery lottery = lottery(lotteries);
                if (lottery.getNum() > 0) {
                    SpringUtils.getBean(BagService.class).rewardItem(role, lottery.getSerial(), lottery.getNum(), 0, "地宫除魔");
                }
            }
        }
    }
    
    public static Lottery lottery(List<Lottery> list) {
        List<Double> rates = new ArrayList<>(list.size());
        for (Lottery lottery : list) {
            rates.add(lottery.getBili());
        }
        return list.get(LotteryUtils.lottery(rates));
    }
    
    public static List<Lottery> getPlayerAcsry(Integer race, Integer sex) {
        double min = 3.0;
        List<SysDataTaskMoAward> moAwardList = TaskDataPool.moAwardMap.get(Integer.valueOf(TaskType.CM.getId()));
        if (moAwardList == null) {
            return new ArrayList<>();
        }
        List<Lottery> list = new ArrayList<>();
        if ((int)race == RACE.RACE_REN.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = moAwardList.stream().filter(e -> e.getRenMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(award.getSerial(), "", (double) award.getRenMale(), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getRenFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getRenFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_MO.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getMoMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getMoMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getMoFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getMoFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_XIAN.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getXianMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getXianMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getXianFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getXianFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_GUI.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getGuiMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getGuiMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getGuiFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getGuiFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_LONG.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getLongMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getLongMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getLongFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)(int)award.getLongFemale()), 1));
                }
            }
        }
        return list;
    }
    
    static {
        DUNGEON_STAGE_FIGHT_MAP = new HashMap<String, Integer>() {
            {
                this.put("d_f_c", Integer.valueOf(270001));
                this.put("d_f_z", Integer.valueOf(270001));
            }
        };
        DUNGEON_MAP = Arrays.asList(Integer.valueOf(5501));
        DUNGEON_ENTER_MAP = (int)DungeonCmHandler.DUNGEON_MAP.get(0);
    }
}
