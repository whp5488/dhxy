package com.kitty.system.game.task.service.handler.activity;

import com.kitty.common.bean.Lottery;
import com.kitty.common.core.SchedulerManager;
import com.kitty.common.utils.DateUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.confirm.model.QuestionKjConfirm;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.service.AsyncService;
import com.kitty.system.game.task.domain.DoingTask;
import com.kitty.system.game.task.domain.vo.RankHallQuestion;
import com.kitty.system.game.task.domain.vo.RecordHallQuestion;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import com.kitty.system.game.task.message.RespQuestionConfirm;
import com.kitty.system.game.task.message.RespQuestionCountry;
import com.kitty.system.game.task.message.RespQuestionHall;
import com.kitty.system.game.task.message.RespQuestionHallRank;
import com.kitty.system.game.task.message.RespQuestionProvince;
import com.kitty.system.game.task.message.RespTaskIcon;
import com.kitty.system.game.task.message.vo.TaskInfo;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.task.service.handler.TimeTaskHandler;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class KejuTaskHandler extends TimeTaskHandler {
  private Map<Integer, Boolean> memberMap = new HashMap<>();
  
  public static int hallNum = 0;
  
  public static int question = 0;
  
  private static final int MAP = 3007;
  
  private static final int MAP_X = 60;
  
  private static final int MAP_Y = 25;
  
  private static final int EXIT_MAP = 1000;
  
  private static final int EXIT_X = 60;
  
  private static final int EXIT_Y = 25;
  
  public static final int FIRST_TASK_ID = 1730001;
  
  public static final List<Integer> TITLE = Arrays.asList(new Integer[] { Integer.valueOf(310001), Integer.valueOf(310002), Integer.valueOf(310003), Integer.valueOf(310004) });
  
  private static final List<String> PERIOD = Arrays.asList(new String[] { "5" });
  
  public static final String ACTIVITY_END_TALK = "本届科举%s已圆满结束了，少侠下届再来吧";
  
  public static final String HALL_TIPS = "兹宣布本届科举考试殿试名单:#<PRB0>%s#:#<LB>%s#请以上考生于今日22:00前找皇宫#<PRB0>房玄龄#入场。";
  
  private static final String INVITE_JOIN_LIMIT = "当前地图无法邀请";
  
  private static final String ACCEPT_JOIN_LIMIT = "该地图无法操作申请队员";
  
  private static final String COMPETE_PK_LIMIT = "你现在不能与人切磋。";
  
  public TaskType getTaskType() {
    return TaskType.KJ;
  }
  
  public boolean canGiveUpTask(Role role, TaskSet taskSet) {
    return true;
  }
  
  public TaskInfo getTaskInfo(Role role, TaskSet taskSet) {
    TaskInfo taskInfo = super.getTaskInfo(role, taskSet);
    return taskInfo;
  }
  
  public static boolean isActivity() {
    String day = DateUtils.dayForWeek(new Date());
    if (!PERIOD.contains(day))
      return false; 
    return true;
  }
  
  public static boolean isActivityEnd(int hour, int minute) {
    Calendar cal = Calendar.getInstance();
    cal.set(11, hour);
    cal.set(12, minute);
    cal.set(13, 0);
    if (System.currentTimeMillis() >= cal.getTimeInMillis())
      return true; 
    return false;
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    if (!isActivity())
      return; 
    Role role = loginEvent.getRole();
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    if (!isActivityEnd(21, 50)) {
      Calendar cal = Calendar.getInstance();
      cal.set(11, 21);
      cal.set(12, 50);
      cal.set(13, 0);
      int expire = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
      RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
      if (recordQuestion == null) {
        acceptTask(role, expire, 1730001);
        RecordQuestion record = new RecordQuestion();
        record.setI(role.getRoleId().intValue());
        this.redisService.set("country:" + role.getId(), (Serializable)record, -1L);
      } else if (recordQuestion != null && recordQuestion.getS() > 0) {
        int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
        int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
        if (minutes >= 10) {
          recordQuestion.setE(current + mistakes * 30);
          this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
          this.redisService.set("country:" + role.getId(), (Serializable)recordQuestion, -1L);
          return;
        } 
      } 
    } 
    if (role.getPos().getMapId().equals(Integer.valueOf(3007)) && this.memberMap.get(role.getRoleId()) == null) {
      leave(role, false);
    } else if (role.getPos().getMapId().equals(Integer.valueOf(3007)) && ((Boolean)this.memberMap.get(role.getRoleId())).booleanValue()) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespTaskIcon(2000, "kj", (byte)1));
    } 
  }
  
  public boolean acceptTask(Role role, int expire, int serial) {
    TaskSet taskSet = this.taskService.getTaskSet(serial);
    return acceptTask(role, taskSet, expire);
  }
  
  public void reqQuestionXs(Role role, Npc npc) {
    if (!isActivity())
      return; 
    if (isActivityEnd(21, 50)) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "乡试" }));
      return;
    } 
    RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
    if (recordQuestion != null) {
      if (recordQuestion.getE() > 0) {
        int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
        int seconds = differenceMillis % 60;
        int minutes = differenceMillis / 60 % 60;
        if (minutes >= 10) {
          String str = "你已完成乡试，考试用时" + minutes + "分钟" + seconds + "秒。#<E:50>#";
          this.npcService.sendNpcContent(role, npc, "", str);
          return;
        } 
        String talk = "你已完成乡试，考试用时" + minutes + "分钟" + seconds + "秒。#<E:50>#\n恭喜少侠获得省试资格，请于今日21:50前找国子监#<PRB0>礼部官员#，参加省试。";
        this.npcService.sendNpcContent(role, npc, "", talk);
        return;
      } 
    } else {
      return;
    } 
    if (recordQuestion.getS() > 0) {
      this.npcService.sendNpcContent(role, npc, "乡试（未完成）/get_xs|才疏学浅，在下告辞#<E:23>#/离开", null);
      return;
    } 
    this.npcService.sendNpcContent(role, npc, "我就是来考的/get_xs|才疏学浅，在下告辞#<E:23>#/离开", null);
  }
  
  public void getQuestionXs(Role role, Npc npc) {
    if (!isActivity())
      return; 
    if (isActivityEnd(21, 50)) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "乡试" }));
      return;
    } 
    RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
    if (recordQuestion == null || recordQuestion.getE() > 0)
      return; 
    if (recordQuestion.getS() <= 0) {
      role.setConfirm((RoleConfirm)new QuestionKjConfirm((byte)0));
      MessagePusher.pushMessage(role, (Message)new RespQuestionConfirm());
      return;
    } 
    conductQuestionXs(role, recordQuestion, npc, false);
  }
  
  public void conductQuestionXs(Role role, RecordQuestion recordQuestion, Npc npc, boolean flag) {
    if (!isActivity())
      return; 
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Calendar cal = Calendar.getInstance();
    cal.set(11, 21);
    cal.set(12, 50);
    cal.set(13, 0);
    int expire = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    if (current >= expire) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "乡试" }));
      return;
    } 
    if (recordQuestion.getS() <= 0)
      recordQuestion.setS(current); 
    if (flag) {
      SysCountryQuestion question = getQuestionXs(TaskDataPool.countryQuestion);
      recordQuestion.setQ(question.getSerial());
    } 
    int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
    int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
    if (minutes >= 10) {
      recordQuestion.setE(current + mistakes * 30);
      int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
      String talk = "你已完成乡试，考试用时" + (differenceMillis / 60 % 60) + "分钟" + (differenceMillis % 60) + "秒。#<E:50>#";
      this.npcService.sendNpcContent(role, npc, "", talk);
      this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
      this.redisService.set("country:" + role.getId(), (Serializable)recordQuestion, -1L);
      return;
    } 
    RespQuestionCountry questionCountry = new RespQuestionCountry();
    questionCountry.setTime(recordQuestion.getS());
    questionCountry.setSerial(recordQuestion.getQ());
    questionCountry.setNum(recordQuestion.getR().size() + 1);
    questionCountry.setMistakes(mistakes);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)questionCountry);
    this.redisService.set("country:" + role.getId(), (Serializable)recordQuestion, -1L);
  }
  
  public static SysCountryQuestion getQuestionXs(Map<Integer, SysCountryQuestion> questionMap) {
    Integer[] keys = (Integer[])questionMap.keySet().toArray((Object[])new Integer[0]);
    int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(keys.length - 1));
    Integer randomKey = keys[random];
    return questionMap.get(randomKey);
  }
  
  public void reqQuestionSs(Role role, Npc npc) {
    if (!isActivity())
      return; 
    if (isActivityEnd(21, 50)) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "省试" }));
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
    } else {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("您还没有参加该活动的资格哦..."));
      return;
    } 
    if (recordQuestion.getS() > 0) {
      this.npcService.sendNpcContent(role, npc, "省试（未完成）/get_ss|才疏学浅，在下告辞#<E:23>#/离开", null);
      return;
    } 
    this.npcService.sendNpcContent(role, npc, "我就是来考的/get_ss|才疏学浅，在下告辞#<E:23>#/离开", null);
  }
  
  public void getQuestionSs(Role role, Npc npc) {
    if (!isActivity())
      return; 
    if (isActivityEnd(21, 50)) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "省试" }));
      return;
    } 
    RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("province:" + role.getId(), RecordQuestion.class);
    if (recordQuestion == null || recordQuestion.getE() > 0)
      return; 
    if (recordQuestion.getS() <= 0) {
      role.setConfirm((RoleConfirm)new QuestionKjConfirm((byte)1));
      MessagePusher.pushMessage(role, (Message)new RespQuestionConfirm((byte)1));
      return;
    } 
    conductQuestionSs(role, recordQuestion, npc, false);
  }
  
  public void conductQuestionSs(Role role, RecordQuestion recordQuestion, Npc npc, boolean flag) {
    if (!isActivity())
      return; 
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Calendar cal = Calendar.getInstance();
    cal.set(11, 21);
    cal.set(12, 50);
    cal.set(13, 0);
    int expire = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    if (current >= expire) {
      this.npcService.sendNpcContent(role, npc, "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", new Object[] { "省试" }));
      return;
    } 
    if (recordQuestion.getS() <= 0)
      recordQuestion.setS(current); 
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
    MessagePusher.pushMessage(role.getId().longValue(), (Message)questionProvince);
    this.redisService.set("province:" + role.getId(), (Serializable)recordQuestion, -1L);
  }
  
  public static SysProvinceQuestion getQuestionSs(Map<Integer, SysProvinceQuestion> questionMap, byte type) {
    List<SysProvinceQuestion> list = (List<SysProvinceQuestion>)questionMap.values().stream().filter(e -> (e.getType() == type)).collect(Collectors.toList());
    int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
    return list.get(index);
  }
  
  public void hallCertification() {
    if (!isActivity())
      return; 
    ((AsyncService)SpringUtils.getBean(AsyncService.class)).clearKjTitle();
    List<RecordQuestion> list = this.redisService.getContain("province:*", RecordQuestion.class);
    Map<Byte, List<RecordQuestion>> map = (Map<Byte, List<RecordQuestion>>)list.stream().filter(e -> (e.getE() > 0)).collect(Collectors.groupingBy(RecordQuestion::getT));
    for (Byte key : map.keySet()) {
      String course = getCourseTitle(key.byteValue());
      if (course == null)
        continue; 
      List<RecordQuestion> recordQuestionList = map.get(key);
      if (recordQuestionList.size() <= 0)
        continue; 
      List<RecordQuestion> collect = (List<RecordQuestion>)recordQuestionList.stream().sorted(Comparator.comparing(RecordQuestion::getE)).limit(40L).collect(Collectors.toList());
      StringBuilder stringBuilder = new StringBuilder();
      for (RecordQuestion recordQuestion : collect) {
        Role role = this.roleService.getPlayer(recordQuestion.getI());
        if (role == null)
          continue; 
        if (stringBuilder.length() > 0)
          stringBuilder.append("、"); 
        stringBuilder.append(role.getName());
        this.redisService.set("hall:" + recordQuestion.getI(), Integer.valueOf(recordQuestion.getI()), -1L);
      } 
      SpringUtils.getChatService().sendSystem(String.format("兹宣布本届科举考试殿试名单:#<PRB0>%s#:#<LB>%s#请以上考生于今日22:00前找皇宫#<PRB0>房玄龄#入场。", new Object[] { course, stringBuilder.toString() }), "系统");
    } 
    hallNum = 0;
    question = 0;
    this.memberMap = new HashMap<>();
    Calendar cal = Calendar.getInstance();
    cal.set(11, 22);
    cal.set(12, 0);
    cal.set(13, 5);
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            KejuTaskHandler.this.scheduled();
          }
        },  cal.getTimeInMillis() - System.currentTimeMillis());
  }
  
  public void scheduled() {
    ((AsyncService)SpringUtils.getBean(AsyncService.class)).kjHallQuestionAward(this.memberMap, KejuTaskHandler.question, hallNum);
    if (hallNum >= 10)
      return; 
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    hallNum++;
    SysHallQuestion questionDs = getQuestionDs(TaskDataPool.hallQuestion);
    KejuTaskHandler.question = questionDs.getSerial();
    for (Integer key : this.memberMap.keySet()) {
      Boolean flag = this.memberMap.get(key);
      if (!flag.booleanValue())
        continue; 
      Role player = this.roleService.getPlayer(key.intValue());
      if (player != null) {
        List<RecordHallQuestion> list = this.redisService.getContain("hall:" + player.getRoleId() + "_*", RecordHallQuestion.class);
        RecordHallQuestion question = new RecordHallQuestion();
        question.setS(current);
        question.setQ(questionDs.getSerial());
        this.redisService.set("hall:" + player.getRoleId() + "_" + questionDs.getSerial() + "_" + hallNum, (Serializable)question, -1L);
        RespQuestionHall respQuestionHall = new RespQuestionHall();
        respQuestionHall.setTime(current);
        respQuestionHall.setSerial(questionDs.getSerial());
        respQuestionHall.setNum(hallNum);
        long count = list.stream().filter(e -> (e.getR() <= 0)).count();
        respQuestionHall.setMistakes((int)((count < 0L) ? 0L : count));
        MessagePusher.pushMessage(player.getId().longValue(), (Message)respQuestionHall);
      } 
    } 
    SchedulerManager.getInstance().schedule(new Runnable() {
          public void run() {
            KejuTaskHandler.this.scheduled();
          }
        },  30000L);
  }
  
  public static SysHallQuestion getQuestionDs(Map<Integer, SysHallQuestion> questionMap) {
    Integer[] keys = (Integer[])questionMap.keySet().toArray((Object[])new Integer[0]);
    int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(keys.length - 1));
    Integer randomKey = keys[random];
    return questionMap.get(randomKey);
  }
  
  public static String getCourseTitle(byte type) {
    switch (type) {
      case 1:
        return "常识科";
      case 2:
        return "地理科";
      case 3:
        return "历史科";
      case 4:
        return "文学科";
      case 5:
        return "饮食科";
      case 6:
        return "游戏科";
    } 
    return null;
  }
  
  public void reqQuestionDs(Role role, Npc npc) {
    if (!isActivity())
      return; 
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Calendar cal = Calendar.getInstance();
    cal.set(11, 21);
    cal.set(12, 52);
    cal.set(13, 0);
    int enterTime = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    cal.set(11, 22);
    cal.set(12, 16);
    cal.set(13, 0);
    int endTime = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    if (current < enterTime) {
      this.npcService.sendNpcContent(role, npc, "", "科举殿试将于21:52开放进场，还有段时间，可以再温习下#<E:46>#");
    } else if (current >= enterTime && current < endTime) {
      this.npcService.sendNpcContent(role, npc, "进场殿试/enter_ds|我就看看，又来一车砖/离开", "能参加殿试者皆我大唐之栋梁，我房玄龄贵为当朝尚书，却不曾参加过殿试，想想真是好生羡慕你们啊");
    } else {
      this.npcService.sendNpcContent(role, npc, "查看金榜/kj_rank|再见/离开", "本届科举已经圆满结束了。");
    } 
  }
  
  public void enter(Role role, Npc npc) {
    if (!isActivity())
      return; 
    int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    Calendar cal = Calendar.getInstance();
    cal.set(11, 21);
    cal.set(12, 52);
    cal.set(13, 0);
    int enterTime = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    cal.set(11, 22);
    cal.set(12, 16);
    cal.set(13, 0);
    int endTime = (new Long(cal.getTimeInMillis() / 1000L)).intValue();
    if (current >= enterTime && current < endTime) {
      Integer hall = (Integer)this.redisService.get("hall:" + role.getRoleId(), Integer.class);
      if (hall == null || !role.getRoleId().equals(hall)) {
        this.npcService.sendNpcContent(role, npc, "", "殿试名单里没看到你啊，不要想冒名顶替#<E:46>#");
        return;
      } 
      Team team = this.teamService.getTeam(role.getRoleId().intValue());
      if (team != null) {
        this.npcService.sendNpcContent(role, npc, "", "科举无法组队哦！请先离队再来吧");
        return;
      } 
      ReqTelePort reqTelePort = new ReqTelePort();
      reqTelePort.setMapId(3007);
      reqTelePort.setX(60);
      reqTelePort.setY(25);
      SpringUtils.getMapService().changeMap(role, reqTelePort, null);
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespTaskIcon(2000, "kj", (byte)1));
      this.memberMap.put(role.getRoleId(), Boolean.valueOf(true));
    } 
  }
  
  public void leave(Role role, boolean flag) {
    ReqTelePort reqTelePort = new ReqTelePort();
    reqTelePort.setMapId(1000);
    reqTelePort.setX(60);
    reqTelePort.setY(25);
    SpringUtils.getMapService().changeMap(role, reqTelePort, null);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespTaskIcon(2000, "kj", (byte)0));
    Boolean aBoolean = this.memberMap.get(role.getRoleId());
    if (aBoolean != null)
      this.memberMap.put(role.getRoleId(), Boolean.valueOf(false)); 
    if (flag)
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("殿试结束，考生离场")); 
  }
  
  public void handleHeartBeat(Role role, long prevHeartTime) {
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    TaskDataPool.taskKjList.forEach(obj -> {
          if (role.getTask().isAccepted(obj.getSerial())) {
            DoingTask doingTask = (DoingTask)role.getTask().getDoingTask().get(Integer.valueOf(obj.getSerial()));
            if (doingTask != null && currTime >= doingTask.getExpire()) {
              TaskSet taskSet = this.taskService.getTaskSet(obj.getSerial());
              giveUpTask(role, taskSet);
            } 
          } 
        });
  }
  
  public boolean isInMap(Role role) {
    if (role == null)
      return false; 
    return DataGlobalFunc.isInMap(role, 3007);
  }
  
  public boolean isFoundJoinTeam(Role role) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前场景禁止组队"));
      return false;
    } 
    return true;
  }
  
  public boolean isRequestJoinTeam(Role role) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前场景禁止组队"));
      return false;
    } 
    return true;
  }
  
  public boolean isInviteJoinTeam(Role role, int inviteRoleId) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前地图无法邀请"));
      return false;
    } 
    Role invitRole = this.roleService.getPlayer(inviteRoleId);
    if (invitRole == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("对方所在地图无法接受邀请哦。"));
      return false;
    } 
    if (isInMap(invitRole)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前地图无法邀请"));
      return false;
    } 
    return true;
  }
  
  public boolean isAcceptJoinTeam(Role role, int joinRoleId) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("该地图无法操作申请队员"));
      return false;
    } 
    Role joinRole = SpringUtils.getRoleService().getOnlinePlayer(joinRoleId);
    if (isInMap(joinRole)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("该地图无法操作申请队员"));
      return false;
    } 
    return true;
  }
  
  public boolean isBackToTeam(Team team, Role memberRole) {
    Role leaderRole = SpringUtils.getRoleService().getOnlinePlayer(team.getLeaderUid());
    if (isInMap(leaderRole)) {
      MessagePusher.pushMessage(memberRole, (Message)new RespMsg("当前场景禁止组队"));
      return false;
    } 
    return true;
  }
  
  public boolean isCompetePk(Role role, Role pkRole) {
    if (isInMap(role)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    if (isInMap(pkRole)) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你现在不能与人切磋。"));
      return false;
    } 
    return true;
  }
  
  public void rank() {
    ArrayList<RankHallQuestion> objects = new ArrayList<>();
    for (Integer key : this.memberMap.keySet()) {
      Role player = this.roleService.getPlayer(key.intValue());
      if (player == null)
        continue; 
      int elapsed = 0;
      List<RecordHallQuestion> list = this.redisService.getContain("hall:" + player.getRoleId() + "_*", RecordHallQuestion.class);
      if (list == null)
        continue; 
      for (RecordHallQuestion hallQuestion : list) {
        if (hallQuestion.getT() <= 0 || hallQuestion.getR() < 0) {
          elapsed += 60;
          continue;
        } 
        if (hallQuestion.getR() == 1) {
          elapsed += hallQuestion.getT() - hallQuestion.getS();
          continue;
        } 
        if (hallQuestion.getR() == 0)
          elapsed += hallQuestion.getT() - hallQuestion.getS() + 30; 
      } 
      if (list.size() < 10) {
        elapsed += (10 - list.size()) * 60;
        this.logger.error("玩家:{}中途退出已答题：{}道", player.getName(), Integer.valueOf(list.size()));
      } 
      objects.add(new RankHallQuestion(player.getId().longValue(), player.getName(), elapsed));
    } 
    List<RankHallQuestion> collect = (List<RankHallQuestion>)objects.stream().sorted(Comparator.comparing(RankHallQuestion::getT)).limit(100L).collect(Collectors.toList());
    String content = "恭喜少侠在本次科举考试中，取得{0}的好成绩";
    long current = System.currentTimeMillis();
    List<RankHallQuestion> questions = new ArrayList<>();
    String contentEmail = MessageFormat.format(content, new Object[] { "进士" });
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < collect.size(); i++) {
      if (i < 3) {
        RankHallQuestion rankHallQuestion = collect.get(i);
        rankHallQuestionTop3(i, rankHallQuestion, current, content);
        questions.add(rankHallQuestion);
      } else {
        RankHallQuestion question = collect.get(i);
        if (stringBuilder.length() < 3) {
          if (stringBuilder.length() > 0)
            stringBuilder.append("、"); 
          stringBuilder.append(question.getN());
        } 
        Role player = this.roleService.getPlayer(((RankHallQuestion)collect.get(i)).getI());
        if (player != null) {
          EmailSystem emailSystem = new EmailSystem();
          emailSystem.setEmailTitle("系统邮件");
          emailSystem.setSender(1);
          emailSystem.setRecipient(player.getId().longValue());
          emailSystem.setContent(contentEmail);
          emailSystem.setExpirationTime(current + 86400000L);
          List<EmailSystemAccessory> accessory = new ArrayList<>();
          accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP.intValue(), String.valueOf(DataExpFunc.calculateKj(player.getLevel().intValue(), player.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 1))));
          accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP_PET.intValue(), String.valueOf(DataExpFunc.calculateKj(player.getLevel().intValue(), player.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 1))));
          accessory.add(new EmailSystemAccessory((byte)6, ((Integer)TITLE.get(TITLE.size() - 1)).intValue(), "0"));
          emailSystem.setAccessory(accessory);
          this.emailService.addEmailSystem(emailSystem);
        } 
      } 
    } 
    String rest = "";
    if (stringBuilder.length() > 0)
      rest = MessageFormat.format("#<LB>赐{1}#等{2}人#<PRB0>进士及第#。", new Object[] { stringBuilder.toString(), Integer.valueOf(collect.size() - 3) }); 
    String msg = "兹宣布本届科举考试殿试成绩，皇帝制曰，本届策试天下文举,#<PRB0>状元##<LB>{0}#，#<PRB0>榜眼##<LB>{1}#，#<PRB0>探花##<LB>{2}。{3}# 造示";
    SpringUtils.getChatService().sendSystem(MessageFormat.format(msg, new Object[] { ((RankHallQuestion)questions.get(0)).getN(), ((RankHallQuestion)questions.get(1)).getN(), ((RankHallQuestion)questions.get(2)).getN(), rest }), "系统");
    clearRedis();
    this.redisService.set("kjTop", JsonUtils.object2String(questions), 36000L);
  }
  
  public void rankHallQuestionTop3(int index, RankHallQuestion rankHallQuestion, long current, String content) {
    String msg = content;
    if (index == 0) {
      msg = MessageFormat.format(content, new Object[] { "状元" });
    } else if (index == 1) {
      msg = MessageFormat.format(content, new Object[] { "榜眼" });
    } else if (index == 2) {
      msg = MessageFormat.format(content, new Object[] { "探花" });
    } else {
      return;
    } 
    Role player = this.roleService.getPlayer(rankHallQuestion.getI());
    if (player == null)
      return; 
    EmailSystem emailSystem = new EmailSystem();
    emailSystem.setEmailTitle("系统邮件");
    emailSystem.setSender(1);
    emailSystem.setRecipient(player.getId().longValue());
    emailSystem.setContent(msg);
    emailSystem.setExpirationTime(current + 86400000L);
    List<EmailSystemAccessory> accessory = new ArrayList<>();
    accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP.intValue(), String.valueOf(DataExpFunc.calculateKj(player.getLevel().intValue(), player.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 10 - index))));
    accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP_PET.intValue(), String.valueOf(DataExpFunc.calculateKj(player.getLevel().intValue(), player.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 12 - index))));
    accessory.add(new EmailSystemAccessory((byte)1, getTopAward(index), "1"));
    accessory.add(new EmailSystemAccessory((byte)6, ((Integer)TITLE.get(index)).intValue(), "0"));
    emailSystem.setAccessory(accessory);
    this.emailService.addEmailSystem(emailSystem);
  }
  
  public void clearRedis() {
    this.redisService.delContain("country:*");
    this.redisService.delContain("province:*");
    this.redisService.delContain("hall:*");
  }
  
  public static int getTopAward(int top) {
    if (top == 0) {
      List<Integer> list1 = Arrays.asList(new Integer[] { Integer.valueOf(75011), Integer.valueOf(75012), Integer.valueOf(75013), Integer.valueOf(75014), Integer.valueOf(75015) });
      return ((Integer)list1.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list1.size() - 1)))).intValue();
    } 
    if (top == 1) {
      List<Integer> list1 = Arrays.asList(new Integer[] { Integer.valueOf(75006), Integer.valueOf(75007), Integer.valueOf(75008), Integer.valueOf(75009), Integer.valueOf(75010) });
      return ((Integer)list1.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list1.size() - 1)))).intValue();
    } 
    List<Integer> list = Arrays.asList(new Integer[] { Integer.valueOf(75001), Integer.valueOf(75002), Integer.valueOf(75003), Integer.valueOf(75004), Integer.valueOf(75005) });
    return ((Integer)list.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1)))).intValue();
  }
  
  public void reqKjRank(Role role, Npc npc) {
    if (!isActivity())
      return; 
    String string = (String)this.redisService.get("kjTop", String.class);
    if (string == null)
      return; 
    List<RankHallQuestion> list = (List<RankHallQuestion>)JsonUtils.string2Collection(string, List.class, RankHallQuestion.class);
    List<RankHallQuestion> collect = (List<RankHallQuestion>)list.stream().sorted(Comparator.comparing(RankHallQuestion::getT)).limit(3L).collect(Collectors.toList());
    RespQuestionHallRank questionHallRank = new RespQuestionHallRank();
    for (RankHallQuestion obj : collect)
      questionHallRank.getList().add(obj.getN()); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)questionHallRank);
  }
  
  public void award(Role role, SysCommonAward commonAward) {
    try {
      if (commonAward != null && commonAward.getAward() != null && commonAward.getAward().size() > 0) {
        List<Param> award = commonAward.getAward();
        List<Lottery> list = new ArrayList<>();
        for (int i = 0; i < award.size(); i++)
          list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf(((Param)award.get(i)).getR().intValue() / 1000.0D))); 
        List<Double> orignalRates = new ArrayList<>(list.size());
        for (Lottery lottery : list)
          orignalRates.add(lottery.getBili()); 
        int index = LotteryUtils.lottery(orignalRates);
        Param param = award.get(index);
        if (param.getK().intValue() <= 0)
          return; 
        ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, param.getK().intValue(), param.getV().intValue(), param.getBd(), true, true, "科举");
      } 
      if (commonAward != null && commonAward.getSpecial() != null && commonAward.getSpecial().size() > 0) {
        List<Param> special = commonAward.getSpecial();
        List<Lottery> list = new ArrayList<>();
        for (int i = 0; i < special.size(); i++)
          list.add(new Lottery(Integer.valueOf(i), "", Double.valueOf(((Param)special.get(i)).getR().intValue() / 1000.0D))); 
        List<Double> orignalRates = new ArrayList<>(list.size());
        for (Lottery lottery : list)
          orignalRates.add(lottery.getBili()); 
        int index = LotteryUtils.lottery(orignalRates);
        Param param = special.get(index);
        if (param.getK().intValue() <= 0)
          return; 
        ((BagService)SpringUtils.getBean(BagService.class)).addGoods(role, param.getK().intValue(), param.getV().intValue(), param.getBd(), true, true, "科举");
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
