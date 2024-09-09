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
import com.kitty.system.game.confirm.model.CreateDungeonMwkFrontConfirm;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.task.message.RespBallotResult;
import com.kitty.system.game.task.message.vo.BallotInfo;
import java.util.ArrayList;
import com.kitty.system.game.task.message.RespTeamBallot;
import java.util.HashMap;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.i18n.P_I18nId;
import com.kitty.system.common.data.DataGlobalFunc;
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
    public boolean resetTask(Role role, TaskSet taskSet) {
        return false;
    }
    
    @PostConstruct
    public void init() {
        MapDataPool.MAP_KEY_GENERATE.put(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP), role -> (Long)DataCache.ROLE_DUNGEON.getOrDefault(role.getId(), Long.valueOf(0L)));
    }
    
    public String getDifficulty(String type) {
        String difficulty = "#<LG1>普通#";
        if ("m_f_z".equals(type)) {
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
        this.createDungeonBallot(role, npc, type);
        return;
    }
    
    private String checkCreateDungeon(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
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
        names = this.teamService.checkMember(team, memberRole -> this.teamService.checkMember(team, mRole -> (int)memberRole.getRelive() * 1000 + (int)memberRole.getLevel() - ((int)mRole.getRelive() * 1000 + (int)mRole.getLevel()) > 15) != null);
        if (names != null) {
            return I18nIdDataPool.getI18nContent(1536, Integer.valueOf(15));
        }
        if (this.teamService.getTeamCount(role) < 3) {
            MessagePusher.notify2Player(role, 1501, "魔王窟前殿", Integer.valueOf(3));
            return "";
        }
        names = this.teamService.checkMember(role, memberRole -> DataGlobalFunc.needGradeJudge(2, 130, (int)memberRole.getRelive(), (int)memberRole.getLevel(), "#<R>角色等级#不足") != null);
        if (names != null) {
            P_I18nId idResource = (P_I18nId)I18nIdDataPool.ids.get(Integer.valueOf(1523));
            if (idResource != null) {
                String content = String.format(idResource.getContent(), names, String.format("%d转%d级", Integer.valueOf(2), Integer.valueOf(130)));
                return content;
            }
            return null;
        }
        else {
            return null;
        }
    }
    
    public void createDungeonBallot(Role role, Npc npc, String type) {
        if (this.teamService.isTeamLeader(role)) {
            Map<Integer, Short> ballotResults = new HashMap(5);
            this.teamService.memberHandle(role, memberRole -> {
                Short n = (Short)ballotResults.put(memberRole.getRoleId(), Short.valueOf((short)0));
                return;
            });
            ballotResults.put(role.getRoleId(), Short.valueOf((short)1));
            RespTeamBallot respTeamBallot = new RespTeamBallot();
            respTeamBallot.setTeamLeader(this.teamService.isTeamLeader(role));
            String difficulty = this.getDifficulty(type);
            String msg = I18nIdDataPool.getI18nContent(1506, difficulty, "魔王窟前殿");
            respTeamBallot.setMsg(msg);
            long millis = System.currentTimeMillis();
            respTeamBallot.setEndTime(new Long(millis / 1000L).intValue() + 30);
            String title = I18nIdDataPool.getI18nContent(1507, "魔王窟前殿");
            respTeamBallot.setTitle(title);
            this.teamService.pushMessage(role, respTeamBallot);
            List<BallotInfo> ballotInfoList = new ArrayList<>();
            ballotInfoList.add(new BallotInfo((int)role.getSerial(), role.getName(), (short)1));
            for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
                if (!((Integer)entry.getKey()).equals(role.getRoleId())) {
                	Role  memberRole = this.roleService.getPlayer((int)entry.getKey());
                    if (memberRole == null) {
                        continue;
                    }
                    BallotInfo ballotInfo = new BallotInfo((int)memberRole.getSerial(), memberRole.getName(), (short)entry.getValue());
                    ballotInfoList.add(ballotInfo);
                }
                else {
                    continue;
                }
            }
            RespBallotResult respBallotResult = new RespBallotResult();
            respBallotResult.setResultList(ballotInfoList);
            this.teamService.pushMessage(role, respBallotResult);
            ScheduledFuture<?> schedule = SchedulerManager.getInstance().schedule(() -> this.countdown(role, npc), 30000L);
            role.setConfirm(new CreateDungeonMwkFrontConfirm(ballotResults, schedule, type));
        }
    }
    
    public void createBurgeonBallotDisagree(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            Role leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
            Map<Integer, Short> ballotResults = confirm.getBallotResults();
            ballotResults.put(role.getRoleId(), Short.valueOf((short)2));
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
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Map<Integer, Short> ballotResults = null;
        Role leaderRole = null;
        if (team.isLeader((int)role.getRoleId())) {
            leaderRole = role;
            CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        else {
            leaderRole = this.roleService.getPlayer(team.getLeaderUid());
            CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
            ballotResults = confirm.getBallotResults();
        }
        ArrayList<BallotInfo> ballotInfoList = new ArrayList<>();
        if (leaderRole != null) {
            BallotInfo ballotInfo = new BallotInfo((int)leaderRole.getSerial(), leaderRole.getName(), (short)1);
            ballotInfoList.add(ballotInfo);
        }
        for (Map.Entry<Integer, Short> entry : ballotResults.entrySet()) {
            Role memberRole = this.roleService.getPlayer((int)entry.getKey());
            if (memberRole == null) {
                continue;
            }
            if (memberRole.getRoleId().equals(leaderRole.getRoleId())) {
                continue;
            }
            BallotInfo ballotInfo2 = new BallotInfo((int)memberRole.getSerial(), memberRole.getName(), (short)entry.getValue());
            ballotInfoList.add(ballotInfo2);
        }
        RespBallotResult respBallotResult = new RespBallotResult();
        respBallotResult.setResultList(ballotInfoList);
        return respBallotResult;
    }
    
    public void createBurgeonBallotAgree(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        Role leaderRole = this.roleService.getOnlinePlayer(team.getLeaderUid());
        CreateDungeonMwkFrontConfirm confirm = (CreateDungeonMwkFrontConfirm)leaderRole.getConfirm();
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
                }
                else {
                    continue;
                }
            }
            if (allAgree) {
                this.createDungeon(team);
                TaskSet taskSet = this.taskService.getTaskSet(1700001);
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
    
    public void refuseInform(Team team, Map<Integer, Short> ballotResults) {
        if (team != null && ballotResults.size() > 0) {
            Npc npc = new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(90236)));
            StringBuffer buffer = new StringBuffer();
            for (Integer key : ballotResults.keySet()) {
                short value = (short)ballotResults.get(key);
                if (2 == value) {
                    Role player = this.roleService.getPlayer((int)key);
                    if (player != null) {
                        buffer.append(player.getName() + "、");
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            if (buffer.length() > 1) {
                this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿"));
            }
        }
    }
    
    public void cancelTask(ScheduledFuture<?> task) {
        if (task != null && !task.isDone()) {
            task.cancel((boolean)Boolean.FALSE);
        }
    }
    
    public void countdown(Role role, Npc npc) {
        CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
        if (createDungeonMwkFrontConfirm != null) {
            Map<Integer, Short> ballotResults = createDungeonMwkFrontConfirm.getBallotResults();
            if (!this.isAllOp(ballotResults)) {
                StringBuffer buffer = new StringBuffer();
                for (Integer key : ballotResults.keySet()) {
                    short value = (short)ballotResults.get(key);
                    if (0 == value) {
                        Role player = this.roleService.getPlayer((int)key);
                        if (player != null) {
                            buffer.append(player.getName() + "、");
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        continue;
                    }
                }
                if (buffer.length() > 1) {
                    Team team = this.teamService.getTeam((int)role.getRoleId());
                    this.teamService.memberHandle(team, memberRole -> this.npcService.sendNpcContent(memberRole, npc, "", buffer.toString() + "没有确定，无法开始" + "魔王窟前殿"));
                }
            }
        }
    }
    
    public int getDungeonFinish(Role role) {
        return role.getTaskProgress().getFinishRecord().getInt(ActivityType.MWK_FRONT.getActivityId(), 0);
    }
    
    private void addActivityCount(Role role) {
        int dungeonFinish = this.getDungeonFinish(role) + 1;
        dungeonFinish = ((dungeonFinish > ActivityType.MWK_FRONT.getTotalCount()) ? ActivityType.MWK_FRONT.getTotalCount() : dungeonFinish);
        role.getTaskProgress().getFinishRecord().setv(ActivityType.MWK_FRONT.getActivityId(), Integer.valueOf(dungeonFinish));
    }
    
    private void doEnterDungeon(Role role) {
        this.mapService.changeMap(role, new ReqTelePort(DungeonMwkFront.DUNGEON_ENTER_MAP, 23, 25), null);
    }
    
    private boolean isAllOp(Map<Integer, Short> ballotResults) {
        boolean allOp = true;
        for (short result : ballotResults.values()) {
            if (result == 0) {
                allOp = false;
            }
            else {
                continue;
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
            int serial2 = (int)DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get("m_f_c");
            String para = TaskHandler.getTaskPara(role, taskSet);
            if (para != null && DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get(para) != null) {
                serial2 = (int)DungeonMwkFront.DUNGEON_STAGE_FIGHT_MAP.get(para);
            }
            this.taskService.taskTriggerFight(role, taskSet, serial2);
        }
        else {
            String[] split = type.split("_");
            if (split.length < 1) {
                return;
            }
            String serial = split[split.length - 1];
            boolean number = NumberUtil.isNumber(serial);
            if (number) {
                Team team = this.teamService.getTeam((int)role.getRoleId());
                this.teamService.memberHandleThreadLocal(team, memberRole -> role.pushTempCache(String.valueOf(TaskType.MWK_FRONT), type));
                this.taskService.taskTriggerFight(role, taskSet, (int)Integer.valueOf(serial));
            }
        }
        return;
    }
    
    private String getNpcContentNotFight(Role role, Npc npc) {
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return "请老老实实打完你的战斗";
        }
        if (this.teamService.getTeamCount(role) < 3) {
            return String.format("请组队满%d人再来战斗", Integer.valueOf(3));
        }
        return null;
    }
    
    public void clear(Role role, List<SysDataTaskMo> taskMoList) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null) {
            if (team.isLeader((int)role.getRoleId())) {
                for (Member member : team.getList()) {
                    Role player = this.roleService.getPlayer((long)member.getId());
                    if (player != null) {
                        this.clearDungeonTask(player, taskMoList);
                    }
                    else {
                        continue;
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
            else {
                continue;
            }
        }
    }
    
    public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
        Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        List<Integer> list = Arrays.asList(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP));
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
        if (!this.isCanTeam(role, joinRole, DungeonMwkFront.DUNGEON_MAP)) {
            if (this.isInDungeon(role, DungeonMwkFront.DUNGEON_MAP)) {
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
        if (!this.isCanTeam(leaderRole, memberRole, DungeonMwkFront.DUNGEON_MAP)) {
            if (this.isInDungeon(leaderRole, DungeonMwkFront.DUNGEON_MAP)) {
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
        if (role.getPos().getMapId().equals(Integer.valueOf(DungeonMwkFront.DUNGEON_ENTER_MAP))) {
            DoingTask doingTask = null;
            for (SysDataTaskMo task : taskMoList) {
                if (role.getTask().isAccepted(task.getSerial())) {
                    flag = true;
                    doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
                    break;
                }
                continue;
            }
            if (flag) {
                if (doingTask != null && doingTask.getPara() != null) {
                    if (!this.isDungeonMember(role)) {
                        List<Long> memberList = new ArrayList<>();
                        memberList.add(role.getId());
                        Dugeon dugeon = new Dugeon(IdGenerator.getNextId(), memberList);
                        DataCache.DUNGEON_MAP.put(Long.valueOf(dugeon.getId()), dugeon);
                        for (long uid : memberList) {
                            DataCache.ROLE_DUNGEON.put(Long.valueOf(uid), Long.valueOf(dugeon.getId()));
                        }
                    }
                    Map<Integer, Short> ballotResults = new HashMap(5);
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
    
    public void leave(Role role) {
        Team team = this.teamService.getTeam((int)role.getRoleId());
        if (team != null && !team.isLeader((int)role.getRoleId())) {
            MessagePusher.pushMessage(role, new RespMsg("既然是集体行动，还是老老实实听从队长的指挥啦。"));
            return;
        }
        List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        this.closeTaskIcon(team, role, taskMoList);
        this.clear(role, TaskDataPool.taskMoList);
        this.leaveDungeon(role, 1004, 36, 97, null);
        return;
    }
    
    public void closeTaskIcon(Team team, Role role, List<SysDataTaskMo> taskMoList) {
        if (team != null) {
            if (team.isLeader((int)role.getRoleId())) {
                for (Member member : team.getList()) {
                    Role player = this.roleService.getPlayer((long)member.getId());
                    if (player != null) {
                        this.closeTaskIcon(player, this.getDungeonTask(taskMoList, role));
                    }
                    else {
                        continue;
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
    
    public DoingTask getDungeonTask(List<SysDataTaskMo> taskMoList, Role role) {
        DoingTask doingTask = null;
        for (SysDataTaskMo task : taskMoList) {
            if (role.getTask().isAccepted(task.getSerial())) {
                doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(task.getSerial()));
                break;
            }
            continue;
        }
        return doingTask;
    }
    
    @Override
    public void giveReward(Role role, TaskSet taskSet) {
        String difficulty = "m_f_c";
        List<SysDataTaskMo> taskMoList = TaskDataPool.taskMoList;
        DoingTask doingTask = this.getDungeonTask(taskMoList, role);
        if (taskSet.getSerial() == 1700001) {
            this.addActivityCount(role);
        }
        boolean flag = true;
        if (this.getDungeonFinish(role) < ActivityType.MWK_FRONT.getTotalCount()) {
            String type = null;
            if (doingTask == null) {
                flag = false;
                difficulty = "m_f_c";
                CreateDungeonMwkFrontConfirm createDungeonMwkFrontConfirm = (CreateDungeonMwkFrontConfirm)role.getConfirm();
                if (createDungeonMwkFrontConfirm != null) {
                    difficulty = createDungeonMwkFrontConfirm.getType();
                }
                type = (String)role.getTempCache(String.valueOf(TaskType.MWK_FRONT), "threats_mo_c_170002");
            }
            else if (doingTask.getPara() != null) {
                difficulty = doingTask.getPara();
            }
            this.roleService.addExp(role, calculate((int)role.getLevel(), (int)role.getRelive(), difficulty, ItemClassify.HERO_EXP, flag), "魔王窟前殿-副本", true);
            Integer combatPet = role.getExtendBox().getCombatPet();
            if ((int)combatPet > 0) {
                Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    this.petService.addExp(role, pet, calculate((int)pet.getPetLv(), (int)pet.getRebirth(), difficulty, ItemClassify.PET_EXP, flag), true);
                }
            }
            this.specialReward(role, difficulty, flag, type);
        }
        if (this.teamService.isTeamLeader(role) && doingTask == null) {
            this.closeTaskIcon(this.teamService.getTeam((int)role.getRoleId()), role, taskMoList);
            this.leaveDungeon(role, 1004, 36, 97, null);
        }
    }
    
    private static long calculate(int grade, int rei, String difficulty, ItemClassify type, boolean flag) {
        double fold = ActivityType.MWK_FRONT.getFold();
        double exp = (double)BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7 * (1.0 + ((double)grade + (double)rei * 15.0) / 140.0 * 2.5) / 30.0)).longValue();
        if (ItemClassify.PET_EXP == type) {
            exp = (double)BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(exp * 1.8)).longValue();
        }
        if (difficulty.equals("m_f_z")) {
            exp *= 3.0;
        }
        return BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((fold > 1.0) ? (exp * fold) : exp)).longValue();
    }
    
    public void specialReward(Role role, String difficulty, boolean flag, String type) {
        double prob = this.PROB;
        int num = 1;
        if (difficulty.equals("m_f_z")) {
            num += RandomUtil.round(Integer.valueOf(1), Integer.valueOf(3));
            prob += 0.3;
        }
        if (!flag && type != null && type.equals("threats_mo_z_170003")) {
            num += RandomUtil.round(Integer.valueOf(1), Integer.valueOf(2));
        }
        if (SkillAi.canAffectOnPro(prob)) {
            List<Lottery> lotteries = getPlayerAcsry(role.getRace(), role.getSex());
            for (int i = 0; i < num; ++i) {
                Lottery lottery = lottery(lotteries);
                if (lottery.getNum() > 0) {
                    ((BagService)SpringUtils.getBean(BagService.class)).rewardItem(role, (int)lottery.getSerial(), lottery.getNum(), 0, "魔王窟前殿");
                }
            }
        }
    }
    
    public static Lottery lottery(List<Lottery> list) {
        List<Double> rates = new ArrayList(list.size());
        for (Lottery lottery : list) {
            rates.add(lottery.getBili());
        }
        return (Lottery)list.get(LotteryUtils.lottery(rates));
    }
    
    public static List<Lottery> getPlayerAcsry(Integer race, Integer sex) {
        double min = 3.0;
        List<SysDataTaskMoAward> moAwardList = (List)TaskDataPool.moAwardMap.get(Integer.valueOf(TaskType.MWK_FRONT.getId()));
        List<Lottery> list = new ArrayList<>();
        if ((int)race == RACE.RACE_REN.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getRenMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getRenMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getRenFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getRenFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_MO.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getMoMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getMoMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getMoFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getMoFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_XIAN.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getXianMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getXianMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getXianFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getXianFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_GUI.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getGuiMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getGuiMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getGuiFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getGuiFemale()), 1));
                }
            }
        }
        else if ((int)race == RACE.RACE_LONG.getValue()) {
            if ((int)sex == HERO.HERO_MALE.getValue()) {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getLongMale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getLongMale()), 1));
                }
            }
            else {
                List<SysDataTaskMoAward> collect = (List)moAwardList.stream().filter(e -> (int)e.getLongFemale() > 0).collect(Collectors.toList());
                for (SysDataTaskMoAward award : collect) {
                    list.add(new Lottery(Integer.valueOf(award.getSerial()), "", Double.valueOf((double)award.getLongFemale()), 1));
                }
            }
        }
        return list;
    }
    
    static {
        DUNGEON_STAGE_FIGHT_MAP = new HashMap<String, Integer>() {
            {
                this.put("m_f_c", Integer.valueOf(170001));
                this.put("m_f_z", Integer.valueOf(170001));
            }
        };
        DUNGEON_MAP = Arrays.asList(Integer.valueOf(2011));
        DUNGEON_ENTER_MAP = (int)DungeonMwkFront.DUNGEON_MAP.get(0);
    }
}
