package com.kitty.system.game.task.service.handler.activity;

import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.game.task.message.RespQuestionHallRank;
import java.util.Arrays;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.email.domain.EmailSystem;
import java.text.MessageFormat;
import com.kitty.system.game.task.domain.vo.RankHallQuestion;
import java.util.ArrayList;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.game.task.message.RespQuestionHall;
import com.kitty.system.game.task.domain.vo.RecordHallQuestion;
import java.util.Iterator;
import com.kitty.common.core.SchedulerManager;
import java.util.Comparator;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.service.AsyncService;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.game.task.message.RespQuestionProvince;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.game.task.message.RespQuestionCountry;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.message.RespQuestionConfirm;
import com.kitty.system.game.confirm.model.QuestionKjConfirm;
import com.kitty.system.game.vo.Npc;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import com.kitty.listener.event.LoginEvent;
import java.util.Calendar;
import com.kitty.common.utils.DateUtils;
import java.util.Date;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.task.TaskType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.kitty.system.game.task.service.handler.TimeTaskHandler;

@Component
public class KejuTaskHandler extends TimeTaskHandler
{
    private Map<Integer, Boolean> memberMap;
    public static int hallNum;
    public static int question;
    private static final int MAP = 3007;
    private static final int MAP_X = 60;
    private static final int MAP_Y = 25;
    private static final int EXIT_MAP = 1000;
    private static final int EXIT_X = 60;
    private static final int EXIT_Y = 25;
    public static final int FIRST_TASK_ID = 1730001;
    public static final List<Integer> TITLE;
    private static final List<String> PERIOD;
    public static final String ACTIVITY_END_TALK = "本届科举%s已圆满结束了，少侠下届再来吧";
    public static final String HALL_TIPS = "兹宣布本届科举考试殿试名单:#<PRB0>%s#:#<LB>%s#请以上考生于今日22:00前找皇宫#<PRB0>房玄龄#入场。";
    private static final String INVITE_JOIN_LIMIT = "当前地图无法邀请";
    private static final String ACCEPT_JOIN_LIMIT = "该地图无法操作申请队员";
    private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
    
    public KejuTaskHandler() {
        this.memberMap = new HashMap();
    }
    
    @Override
    public TaskType getTaskType() {
        return TaskType.KJ;
    }
    
    @Override
    public boolean canGiveUpTask(Role role, TaskSet taskSet) {
        return true;
    }
    
    @Override
    public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
        TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
        return taskInfo;
    }
    
    public static boolean isActivity() {
        String day = DateUtils.dayForWeek(new Date());
        return KejuTaskHandler.PERIOD.contains(day);
    }
    
    public static boolean isActivityEnd(int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(11, hour);
        cal.set(12, minute);
        cal.set(13, 0);
        return System.currentTimeMillis() >= cal.getTimeInMillis();
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        if (!isActivity()) {
            return;
        }
        Role role = loginEvent.getRole();
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (!isActivityEnd(21, 50)) {
            Calendar cal = Calendar.getInstance();
            cal.set(11, 21);
            cal.set(12, 50);
            cal.set(13, 0);
            int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
            RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
            if (recordQuestion == null) {
                this.acceptTask(role, expire, 1730001);
                RecordQuestion record = new RecordQuestion();
                record.setI((int)role.getRoleId());
                this.redisService.set("country:" + role.getId(), record, -1L);
            }
            else if (recordQuestion != null && recordQuestion.getS() > 0) {
                int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
                int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
                if (minutes >= 10) {
                    recordQuestion.setE(current + mistakes * 30);
                    this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
                    this.redisService.set("country:" + role.getId(), recordQuestion, -1L);
                    return;
                }
            }
        }
        if (role.getPos().getMapId().equals(Integer.valueOf(3007)) && this.memberMap.get(role.getRoleId()) == null) {
            this.leave(role, false);
        }
        else if (role.getPos().getMapId().equals(Integer.valueOf(3007)) && (boolean)this.memberMap.get(role.getRoleId())) {
            MessagePusher.pushMessage((long)role.getId(), new RespTaskIcon(2000, "kj", (byte)1));
        }
        return;
    }
    
    public boolean acceptTask(Role role, int expire, int serial) {
        TaskSet taskSet = this.taskService.getTaskSet(serial);
        return this.acceptTask(role, taskSet, expire);
    }
    
    public void reqQuestionXs(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        if (isActivityEnd(21, 50)) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "乡试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
        if (recordQuestion != null) {
            if (recordQuestion.getE() > 0) {
                int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
                int seconds = differenceMillis % 60;
                int minutes = differenceMillis / 60 % 60;
                if (minutes >= 10) {
                    String talk = "你已完成乡试，考试用时" + minutes + "分钟" + seconds + "秒。#<E:50>#";
                    this.npcService.sendNpcContent(role, npc, "", talk);
                    return;
                }
                String talk = "你已完成乡试，考试用时" + minutes + "分钟" + seconds + "秒。#<E:50>#\n恭喜少侠获得省试资格，请于今日21:50前找国子监#<PRB0>礼部官员#，参加省试。";
                this.npcService.sendNpcContent(role, npc, "", talk);
                return;
            }
            else {
                if (recordQuestion.getS() > 0) {
                    this.npcService.sendNpcContent(role, npc, "乡试（未完成）/get_xs|才疏学浅，在下告辞#<E:23>#/离开", null);
                    return;
                }
                this.npcService.sendNpcContent(role, npc, "我就是来考的/get_xs|才疏学浅，在下告辞#<E:23>#/离开", null);
                return;
            }
        }
        else {
            return;
        }
    }
    
    public void getQuestionXs(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        if (isActivityEnd(21, 50)) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "乡试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null || recordQuestion.getE() > 0) {
            return;
        }
        if (recordQuestion.getS() <= 0) {
            role.setConfirm(new QuestionKjConfirm((byte)0));
            MessagePusher.pushMessage(role, new RespQuestionConfirm());
            return;
        }
        this.conductQuestionXs(role, recordQuestion, npc, false);
        return;
    }
    
    public void conductQuestionXs(Role role, RecordQuestion recordQuestion, Npc npc, boolean flag) {
        if (!isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "乡试"));
            return;
        }
        if (recordQuestion.getS() <= 0) {
            recordQuestion.setS(current);
        }
        if (flag) {
            SysCountryQuestion question = getQuestionXs(TaskDataPool.countryQuestion);
            recordQuestion.setQ(question.getSerial());
        }
        int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
        int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
        if (minutes >= 10) {
            recordQuestion.setE(current + mistakes * 30);
            int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
            String talk = "你已完成乡试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。#<E:50>#";
            this.npcService.sendNpcContent(role, npc, "", talk);
            this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
            this.redisService.set("country:" + role.getId(), recordQuestion, -1L);
            return;
        }
        RespQuestionCountry questionCountry = new RespQuestionCountry();
        questionCountry.setTime(recordQuestion.getS());
        questionCountry.setSerial(recordQuestion.getQ());
        questionCountry.setNum(recordQuestion.getR().size() + 1);
        questionCountry.setMistakes(mistakes);
        MessagePusher.pushMessage((long)role.getId(), questionCountry);
        this.redisService.set("country:" + role.getId(), recordQuestion, -1L);
        return;
    }
    
    public static SysCountryQuestion getQuestionXs(Map<Integer, SysCountryQuestion> questionMap) {
        Integer[] keys = (Integer[])questionMap.keySet().toArray(new Integer[0]);
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(keys.length - 1));
        Integer randomKey = keys[random];
        return (SysCountryQuestion)questionMap.get(randomKey);
    }
    
    public void reqQuestionSs(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        if (isActivityEnd(21, 50)) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "省试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("province:" + role.getId(), RecordQuestion.class);
        if (recordQuestion != null) {
            if (recordQuestion.getE() > 0) {
                int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
                int seconds = differenceMillis % 60;
                int minutes = differenceMillis / 60 % 60;
                String talk = "你已完成省试，考试用时" + minutes + "分钟" + seconds + "秒。殿试名单将于21:51公布，请留意放榜通知。";
                this.npcService.sendNpcContent(role, npc, "", talk);
                return;
            }
            if (recordQuestion.getS() > 0) {
                this.npcService.sendNpcContent(role, npc, "省试（未完成）/get_ss|才疏学浅，在下告辞#<E:23>#/离开", null);
                return;
            }
            this.npcService.sendNpcContent(role, npc, "我就是来考的/get_ss|才疏学浅，在下告辞#<E:23>#/离开", null);
            return;
        }
        else {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("您还没有参加该活动的资格哦..."));
            return;
        }
    }
    
    public void getQuestionSs(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        if (isActivityEnd(21, 50)) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "省试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("province:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null || recordQuestion.getE() > 0) {
            return;
        }
        if (recordQuestion.getS() <= 0) {
            role.setConfirm(new QuestionKjConfirm((byte)1));
            MessagePusher.pushMessage(role, new RespQuestionConfirm((byte)1));
            return;
        }
        this.conductQuestionSs(role, recordQuestion, npc, false);
        return;
    }
    
    public void conductQuestionSs(Role role, RecordQuestion recordQuestion, Npc npc, boolean flag) {
        if (!isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "省试"));
            return;
        }
        if (recordQuestion.getS() <= 0) {
            recordQuestion.setS(current);
        }
        if (flag) {
            SysProvinceQuestion question = getQuestionSs(TaskDataPool.provinceQuestion, recordQuestion.getT());
            recordQuestion.setQ(question.getSerial());
        }
        int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
        RespQuestionProvince questionProvince = new RespQuestionProvince();
        questionProvince.setTime(recordQuestion.getS());
        questionProvince.setSerial(recordQuestion.getQ());
        questionProvince.setNum(recordQuestion.getR().size() + 1);
        questionProvince.setMistakes(mistakes);
        MessagePusher.pushMessage((long)role.getId(), questionProvince);
        this.redisService.set("province:" + role.getId(), recordQuestion, -1L);
        return;
    }
    
    public static SysProvinceQuestion getQuestionSs(Map<Integer, SysProvinceQuestion> questionMap, byte type) {
        List<SysProvinceQuestion> list = (List)questionMap.values().stream().filter(e -> e.getType() == type).collect(Collectors.toList());
        int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
        return (SysProvinceQuestion)list.get(index);
    }
    
    public void hallCertification() {
        if (!isActivity()) {
            return;
        }
        ((AsyncService)SpringUtils.getBean(AsyncService.class)).clearKjTitle();
        List<RecordQuestion> list = this.redisService.getContain("province:*", RecordQuestion.class);
        Map<Byte, List<RecordQuestion>> map = (Map)list.stream().filter(e -> e.getE() > 0).collect(Collectors.groupingBy(RecordQuestion::getT));
        for (Byte key : map.keySet()) {
            String course = getCourseTitle((byte)key);
            if (course == null) {
                continue;
            }
            List<RecordQuestion> recordQuestionList = (List)map.get(key);
            if (recordQuestionList.size() <= 0) {
                continue;
            }
            List<RecordQuestion> collect = (List)recordQuestionList.stream().sorted(Comparator.comparing(RecordQuestion::getE)).limit(40L).collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            for (RecordQuestion recordQuestion : collect) {
                Role role = this.roleService.getPlayer(recordQuestion.getI());
                if (role == null) {
                    continue;
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("、");
                }
                stringBuilder.append(role.getName());
                this.redisService.set("hall:" + recordQuestion.getI(), Integer.valueOf(recordQuestion.getI()), -1L);
            }
            SpringUtils.getChatService().sendSystem(String.format("兹宣布本届科举考试殿试名单:#<PRB0>%s#:#<LB>%s#请以上考生于今日22:00前找皇宫#<PRB0>房玄龄#入场。", course, stringBuilder.toString()), "系统");
        }
        KejuTaskHandler.hallNum = 0;
        KejuTaskHandler.question = 0;
        this.memberMap = new HashMap();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 22);
        cal.set(12, 0);
        cal.set(13, 5);
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                KejuTaskHandler.this.scheduled();
            }
        }, cal.getTimeInMillis() - System.currentTimeMillis());
        return;
    }
    
    public void scheduled() {
        ((AsyncService)SpringUtils.getBean(AsyncService.class)).kjHallQuestionAward(this.memberMap, KejuTaskHandler.question, KejuTaskHandler.hallNum);
        if (KejuTaskHandler.hallNum >= 10) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        ++KejuTaskHandler.hallNum;
        SysHallQuestion questionDs = getQuestionDs(TaskDataPool.hallQuestion);
        KejuTaskHandler.question = questionDs.getSerial();
        for (Integer key : this.memberMap.keySet()) {
            Boolean flag = (Boolean)this.memberMap.get(key);
            if (!(boolean)flag) {
                continue;
            }
            Role player = this.roleService.getPlayer((int)key);
            if (player != null) {
                List<RecordHallQuestion> list = this.redisService.getContain("hall:" + player.getRoleId() + "_*", RecordHallQuestion.class);
                RecordHallQuestion question = new RecordHallQuestion();
                question.setS(current);
                question.setQ(questionDs.getSerial());
                this.redisService.set("hall:" + player.getRoleId() + "_" + questionDs.getSerial() + "_" + KejuTaskHandler.hallNum, question, -1L);
                RespQuestionHall respQuestionHall = new RespQuestionHall();
                respQuestionHall.setTime(current);
                respQuestionHall.setSerial(questionDs.getSerial());
                respQuestionHall.setNum(KejuTaskHandler.hallNum);
                long count = list.stream().filter(e -> e.getR() <= 0).count();
                respQuestionHall.setMistakes((int)((count < 0L) ? 0L : count));
                MessagePusher.pushMessage((long)player.getId(), respQuestionHall);
            }
            else {
                continue;
            }
        }
        SchedulerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                KejuTaskHandler.this.scheduled();
            }
        }, 30000L);
        return;
    }
    
    public static SysHallQuestion getQuestionDs(Map<Integer, SysHallQuestion> questionMap) {
        Integer[] keys = (Integer[])questionMap.keySet().toArray(new Integer[0]);
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(keys.length - 1));
        Integer randomKey = keys[random];
        return (SysHallQuestion)questionMap.get(randomKey);
    }
    
    public static String getCourseTitle(byte type) {
        switch (type) {
            case 1: {
                return "常识科";
            }
            case 2: {
                return "地理科";
            }
            case 3: {
                return "历史科";
            }
            case 4: {
                return "文学科";
            }
            case 5: {
                return "饮食科";
            }
            case 6: {
                return "游戏科";
            }
            default: {
                return null;
            }
        }
    }
    
    public void reqQuestionDs(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 52);
        cal.set(13, 0);
        int enterTime = new Long(cal.getTimeInMillis() / 1000L).intValue();
        cal.set(11, 22);
        cal.set(12, 16);
        cal.set(13, 0);
        int endTime = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current < enterTime) {
            this.npcService.sendNpcContent(role, npc, "", "科举殿试将于21:52开放进场，还有段时间，可以再温习下#<E:46>#");
        }
        else if (current >= enterTime && current < endTime) {
            this.npcService.sendNpcContent(role, npc, "进场殿试/enter_ds|我就看看，又来一车砖/离开", "能参加殿试者皆我大唐之栋梁，我房玄龄贵为当朝尚书，却不曾参加过殿试，想想真是好生羡慕你们啊");
        }
        else {
            this.npcService.sendNpcContent(role, npc, "查看金榜/kj_rank|再见/离开", "本届科举已经圆满结束了。");
        }
        return;
    }
    
    public void enter(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 52);
        cal.set(13, 0);
        int enterTime = new Long(cal.getTimeInMillis() / 1000L).intValue();
        cal.set(11, 22);
        cal.set(12, 16);
        cal.set(13, 0);
        int endTime = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= enterTime && current < endTime) {
            Integer hall = (Integer)this.redisService.get("hall:" + role.getRoleId(), Integer.class);
            if (hall == null || !role.getRoleId().equals(hall)) {
                this.npcService.sendNpcContent(role, npc, "", "殿试名单里没看到你啊，不要想冒名顶替#<E:46>#");
                return;
            }
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null) {
                this.npcService.sendNpcContent(role, npc, "", "科举无法组队哦！请先离队再来吧");
                return;
            }
            ReqTelePort reqTelePort = new ReqTelePort();
            reqTelePort.setMapId(3007);
            reqTelePort.setX(60);
            reqTelePort.setY(25);
            SpringUtils.getMapService().changeMap(role, reqTelePort, null);
            MessagePusher.pushMessage((long)role.getId(), new RespTaskIcon(2000, "kj", (byte)1));
            this.memberMap.put(role.getRoleId(), Boolean.valueOf(true));
        }
        return;
    }
    
    public void leave(Role role, boolean flag) {
        ReqTelePort reqTelePort = new ReqTelePort();
        reqTelePort.setMapId(1000);
        reqTelePort.setX(60);
        reqTelePort.setY(25);
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
        MessagePusher.pushMessage((long)role.getId(), new RespTaskIcon(2000, "kj", (byte)0));
        Boolean aBoolean = (Boolean)this.memberMap.get(role.getRoleId());
        if (aBoolean != null) {
            this.memberMap.put(role.getRoleId(), Boolean.valueOf(false));
        }
        if (flag) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("殿试结束，考生离场"));
        }
    }
    
    @Override
    public void handleHeartBeat(Role role, long prevHeartTime) {
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        TaskDataPool.taskKjList.forEach(obj -> {
            if (role.getTask().isAccepted(obj.getSerial())) {
                DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(obj.getSerial()));
                if (doingTask != null && currTime >= doingTask.getExpire()) {
                    TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
                    this.giveUpTask(role, taskSet);
                }
            }
            return;
        });
    }
    
    public boolean isInMap(Role role) {
        return role != null && DataGlobalFunc.isInMap(role, 3007);
    }
    
    public boolean isFoundJoinTeam(Role role) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespMsg("当前场景禁止组队"));
            return false;
        }
        return true;
    }
    
    public boolean isRequestJoinTeam(Role role) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespMsg("当前场景禁止组队"));
            return false;
        }
        return true;
    }
    
    public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespMsg("当前地图无法邀请"));
            return false;
        }
        Role invitRole = this.roleService.getPlayer(inviteRoleId);
        if (invitRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("对方所在地图无法接受邀请哦。"));
            return false;
        }
        if (this.isInMap(invitRole)) {
            MessagePusher.pushMessage(role, new RespMsg("当前地图无法邀请"));
            return false;
        }
        return true;
    }
    
    public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
        if (this.isInMap(role)) {
            MessagePusher.pushMessage(role, new RespMsg("该地图无法操作申请队员"));
            return false;
        }
        Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
        if (this.isInMap(joinRole)) {
            MessagePusher.pushMessage(role, new RespMsg("该地图无法操作申请队员"));
            return false;
        }
        return true;
    }
    
    public boolean isBackToTeam(Team team, Role memberRole) {
        Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
        if (this.isInMap(leaderRole)) {
            MessagePusher.pushMessage(memberRole, new RespMsg("当前场景禁止组队"));
            return false;
        }
        return true;
    }
    
    public boolean isCompetePk(Role role, Role pkRole) {
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
    
    public void rank() {
        ArrayList<RankHallQuestion> objects = new ArrayList<>();
        for (Integer key : this.memberMap.keySet()) {
            Role player = this.roleService.getPlayer((int)key);
            if (player == null) {
                continue;
            }
            int elapsed = 0;
            List<RecordHallQuestion> list = this.redisService.getContain("hall:" + player.getRoleId() + "_*", RecordHallQuestion.class);
            if (list == null) {
                continue;
            }
            for (RecordHallQuestion hallQuestion : list) {
                if (hallQuestion.getT() <= 0 || hallQuestion.getR() < 0) {
                    elapsed += 60;
                }
                else if (hallQuestion.getR() == 1) {
                    elapsed += hallQuestion.getT() - hallQuestion.getS();
                }
                else if (hallQuestion.getR() == 0) {
                    elapsed += hallQuestion.getT() - hallQuestion.getS() + 30;
                }
                else {
                    continue;
                }
            }
            if (list.size() < 10) {
                elapsed += (10 - list.size()) * 60;
                this.logger.error("玩家:{}中途退出已答题：{}道", player.getName(), Integer.valueOf(list.size()));
            }
            objects.add(new RankHallQuestion((long)player.getId(), player.getName(), elapsed));
        }
        List<RankHallQuestion> collect = (List)objects.stream().sorted(Comparator.comparing(RankHallQuestion::getT)).limit(100L).collect(Collectors.toList());
        String content = "恭喜少侠在本次科举考试中，取得{0}的好成绩";
        long current = System.currentTimeMillis();
        List<RankHallQuestion> questions = new ArrayList<>();
        String contentEmail = MessageFormat.format(content, "进士");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < collect.size(); ++i) {
            if (i < 3) {
                RankHallQuestion rankHallQuestion = (RankHallQuestion)collect.get(i);
                this.rankHallQuestionTop3(i, rankHallQuestion, current, content);
                questions.add(rankHallQuestion);
            }
            else {
                RankHallQuestion question = (RankHallQuestion)collect.get(i);
                if (stringBuilder.length() < 3) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("、");
                    }
                    stringBuilder.append(question.getN());
                }
                Role player2 = this.roleService.getPlayer(((RankHallQuestion)collect.get(i)).getI());
                if (player2 != null) {
                    EmailSystem emailSystem = new EmailSystem();
                    emailSystem.setEmailTitle("系统邮件");
                    emailSystem.setSender(1);
                    emailSystem.setRecipient((long)player2.getId());
                    emailSystem.setContent(contentEmail);
                    emailSystem.setExpirationTime(current + 86400000L);
                    List<EmailSystemAccessory> accessory = new ArrayList<>();
                    accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP, String.valueOf(DataExpFunc.calculateKj((int)player2.getLevel(), (int)player2.getRelive(), ItemClassify.HERO_EXP.getId(), 1))));
                    accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP_PET, String.valueOf(DataExpFunc.calculateKj((int)player2.getLevel(), (int)player2.getRelive(), ItemClassify.HERO_EXP.getId(), 1))));
                    accessory.add(new EmailSystemAccessory((byte)6, (int)KejuTaskHandler.TITLE.get(KejuTaskHandler.TITLE.size() - 1), "0"));
                    emailSystem.setAccessory(accessory);
                    this.emailService.addEmailSystem(emailSystem);
                }
            }
        }
        String rest = "";
        if (stringBuilder.length() > 0) {
            rest = MessageFormat.format("#<LB>赐{1}#等{2}人#<PRB0>进士及第#。", stringBuilder.toString(), Integer.valueOf(collect.size() - 3));
        }
        String msg = "兹宣布本届科举考试殿试成绩，皇帝制曰，本届策试天下文举,#<PRB0>状元##<LB>{0}#，#<PRB0>榜眼##<LB>{1}#，#<PRB0>探花##<LB>{2}。{3}# 造示";
        SpringUtils.getChatService().sendSystem(MessageFormat.format(msg, ((RankHallQuestion)questions.get(0)).getN(), ((RankHallQuestion)questions.get(1)).getN(), ((RankHallQuestion)questions.get(2)).getN(), rest), "系统");
        this.clearRedis();
        this.redisService.set("kjTop", JsonUtils.object2String(questions), 36000L);
    }
    
    public void rankHallQuestionTop3(int index, RankHallQuestion rankHallQuestion, long current, String content) {
        String msg = content;
        if (index == 0) {
            msg = MessageFormat.format(content, "状元");
        }
        else if (index == 1) {
            msg = MessageFormat.format(content, "榜眼");
        }
        else if (index == 2) {
            msg = MessageFormat.format(content, "探花");
        }
        else {
            return;
        }
        Role player = this.roleService.getPlayer(rankHallQuestion.getI());
        if (player == null) {
            return;
        }
        EmailSystem emailSystem = new EmailSystem();
        emailSystem.setEmailTitle("系统邮件");
        emailSystem.setSender(1);
        emailSystem.setRecipient((long)player.getId());
        emailSystem.setContent(msg);
        emailSystem.setExpirationTime(current + 86400000L);
        List<EmailSystemAccessory> accessory = new ArrayList<>();
        accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP, String.valueOf(DataExpFunc.calculateKj((int)player.getLevel(), (int)player.getRelive(), ItemClassify.HERO_EXP.getId(), 10 - index))));
        accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP_PET, String.valueOf(DataExpFunc.calculateKj((int)player.getLevel(), (int)player.getRelive(), ItemClassify.HERO_EXP.getId(), 12 - index))));
        accessory.add(new EmailSystemAccessory((byte)1, getTopAward(index), "1"));
        accessory.add(new EmailSystemAccessory((byte)6, (int)KejuTaskHandler.TITLE.get(index), "0"));
        emailSystem.setAccessory(accessory);
        this.emailService.addEmailSystem(emailSystem);
        return;
    }
    
    public void clearRedis() {
        this.redisService.delContain("country:*");
        this.redisService.delContain("province:*");
        this.redisService.delContain("hall:*");
    }
    
    public static int getTopAward(int top) {
        if (top == 0) {
            List<Integer> list = Arrays.asList(Integer.valueOf(75011), Integer.valueOf(75012), Integer.valueOf(75013), Integer.valueOf(75014), Integer.valueOf(75015));
            return (int)list.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1)));
        }
        if (top == 1) {
            List<Integer> list = Arrays.asList(Integer.valueOf(75006), Integer.valueOf(75007), Integer.valueOf(75008), Integer.valueOf(75009), Integer.valueOf(75010));
            return (int)list.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1)));
        }
        List<Integer> list = Arrays.asList(Integer.valueOf(75001), Integer.valueOf(75002), Integer.valueOf(75003), Integer.valueOf(75004), Integer.valueOf(75005));
        return (int)list.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1)));
    }
    
    public void reqKjRank(Role role, Npc npc) {
        if (!isActivity()) {
            return;
        }
        String string = (String)this.redisService.get("kjTop", String.class);
        if (string == null) {
            return;
        }
        List<RankHallQuestion> list = (List)JsonUtils.string2Collection(string, List.class, RankHallQuestion.class);
        List<RankHallQuestion> collect = (List)list.stream().sorted(Comparator.comparing(RankHallQuestion::getT)).limit(3L).collect(Collectors.toList());
        RespQuestionHallRank questionHallRank = new RespQuestionHallRank();
        for (RankHallQuestion obj : collect) {
            questionHallRank.getList().add(obj.getN());
        }
        MessagePusher.pushMessage((long)role.getId(), questionHallRank);
        return;
    }
    
    public void award(Role role, SysCommonAward commonAward) {
        try {
            if (commonAward != null && commonAward.getAward() != null && commonAward.getAward().size() > 0) {
                List<Param> award = commonAward.getAward();
                List<Lottery> list = new ArrayList<>();
                for (int i = 0; i < award.size(); ++i) {
                    list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf((double)(int)((Param)award.get(i)).getR() / 1000.0)));
                }
                List<Double> orignalRates = new ArrayList(list.size());
                for (Lottery lottery : list) {
                    orignalRates.add(lottery.getBili());
                }
                int index = LotteryUtils.lottery(orignalRates);
                Param param = (Param)award.get(index);
                if ((int)param.getK() <= 0) {
                    return;
                }
                ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, (int)param.getK(), (int)param.getV(), param.getBd(), true, true, "科举");
            }
            if (commonAward != null && commonAward.getSpecial() != null && commonAward.getSpecial().size() > 0) {
                List<Param> special = commonAward.getSpecial();
                List<Lottery> list = new ArrayList<>();
                for (int i = 0; i < special.size(); ++i) {
                    list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf((double)(int)((Param)special.get(i)).getR() / 1000.0)));
                }
                List<Double> orignalRates = new ArrayList(list.size());
                for (Lottery lottery : list) {
                    orignalRates.add(lottery.getBili());
                }
                int index = LotteryUtils.lottery(orignalRates);
                Param param = (Param)special.get(index);
                if ((int)param.getK() <= 0) {
                    return;
                }
                ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, (int)param.getK(), (int)param.getV(), param.getBd(), true, true, "科举");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    static {
        KejuTaskHandler.hallNum = 0;
        KejuTaskHandler.question = 0;
        TITLE = Arrays.asList(Integer.valueOf(310001), Integer.valueOf(310002), Integer.valueOf(310003), Integer.valueOf(310004));
        PERIOD = Arrays.asList("5");
    }
}
