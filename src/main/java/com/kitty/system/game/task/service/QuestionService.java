package com.kitty.system.game.task.service;

import com.kitty.common.utils.RandomUtil;
import java.util.Map;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.game.task.domain.vo.RecordHallQuestion;
import com.kitty.system.game.task.message.ReqSubmitQuestionHall;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.game.task.message.ReqSubmitQuestionProvince;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import java.util.Calendar;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.task.message.ReqSubmitQuestionCountry;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.ActivityService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.task.message.ReqSubmitQuestion;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.task.message.RespQuestion;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.vo.Question;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.activity.model.product.ActivityLivenessSet;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.role.domain.Role;
import com.kitty.logs.LoggerFunction;
import com.kitty.cache.RedisService;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.chat.service.ChatService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class QuestionService
{
    protected Logger logger;
    private static final String DLS_DIST = "dls";
    private static final String DLS = "%s/get_dls|我还没有准备好/离开";
    @Autowired
    private ChatService chatService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PetService petService;
    @Autowired
    private NpcService npcService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisService redisService;
    
    public QuestionService() {
        this.logger = LoggerFunction.TASK.getLogger();
    }
    
    public void reqQuestionDls(Role role, Npc npc) {
        int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.QUESTION_DLS);
        int sum = (int)((float)activityLivenessSet.getMaxLiveness() / activityLivenessSet.getPerLiveness());
        if (num >= sum) {
            String msg = "你今天已经接受过我的考验了，明天再来吧#<E:17>#";
            this.npcService.sendNpcContent(role, npc, "", msg);
            return;
        }
        String content = String.format("%s/get_dls|我还没有准备好/离开", "我要参加有奖回答");
        if (num > 0) {
            content = String.format("%s/get_dls|我还没有准备好/离开", "大理寺答题（未完成）");
        }
        this.npcService.sendNpcContent(role, npc, content, null);
        return;
    }
    
    public void getQuestionDls(Role role, Npc npc) {
        int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        Question question = (Question)this.redisService.get(role.getId() + "dls", Question.class);
        if (question == null) {
            question = this.getDls(TaskDataPool.questionMap);
        }
        RespQuestion respQuestion = new RespQuestion(1, question.getSerial(), num + 1);
        MessagePusher.pushMessage(role, respQuestion);
        this.redisService.set(role.getId() + "dls", question, 180L);
    }
    
    public void submitQuestionDls(Role role, ReqSubmitQuestion submitQuestion) {
        int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        ActivityLivenessSet activityLivenessSet = (ActivityLivenessSet)ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.QUESTION_DLS);
        int sum = (int)((float)activityLivenessSet.getMaxLiveness() / activityLivenessSet.getPerLiveness());
        if (num > sum) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 1;
        Question question = (Question)TaskDataPool.questionMap.get(Integer.valueOf(submitQuestion.getSerial()));
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 2;
            msg = "恭喜您答对了#<E:93>#";
        }
        this.roleService.addExp(role, DataExpFunc.calculateDls((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "大理寺答题", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateDls((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        int step = 1;
        ActivityService activityService = (ActivityService)SpringUtils.getBean(ActivityService.class);
        activityService.addFinishCount(role, ActivityType.QUESTION_DLS, step);
        this.chatService.sendInfo(role.getId(), "你获得了#<G>" + step + "#点活跃度");
        MessagePusher.pushMessage(role, new RespMsg(msg));
        ++num;
        if (num < sum) {
            question = this.getDls(TaskDataPool.questionMap);
            RespQuestion respQuestion = new RespQuestion(1, question.getSerial(), num + 1);
            MessagePusher.pushMessage(role, respQuestion);
            this.redisService.set(role.getId() + "dls", question, 180L);
        }
        return;
    }
    
    public void submitQuestionXs(Role role, ReqSubmitQuestionCountry submitQuestion) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730001))), "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "乡试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("country:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null) {
            return;
        }
        if (recordQuestion.getQ() != submitQuestion.getSerial()) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 1;
        SysCountryQuestion question = (SysCountryQuestion)TaskDataPool.countryQuestion.get(Integer.valueOf(submitQuestion.getSerial()));
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 2;
            msg = "恭喜您答对了#<E:93>#";
            recordQuestion.getR().add(Byte.valueOf((byte)1));
        }
        else {
            recordQuestion.getR().add(Byte.valueOf((byte)0));
        }
        this.roleService.addExp(role, DataExpFunc.calculateKj((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "乡试答题", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateKj((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).award(role, (SysCommonAward)PartDataPool.commonAwardMap.get("country"));
        }
        MessagePusher.pushMessage(role, new RespMsg(msg));
        int num = recordQuestion.getR().size() + 1;
        if (num > 20) {
            int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
            int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
            recordQuestion.setE(current + mistakes * 30);
            int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
            this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
            if (minutes >= 10) {
                String talk = "你已完成乡试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。#<E:50>#";
                this.npcService.sendNpcContent(role, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730001))), "", talk);
            }
            else {
                String talk = "你已完成乡试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。#<E:50>#\n恭喜少侠获得省试资格，请于今日21:50前找国子监#<PRB0>礼部官员#，参加省试。";
                this.npcService.sendNpcContent(role, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730001))), "", talk);
                ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).acceptTask(role, expire, 1730002);
                RecordQuestion record = new RecordQuestion();
                record.setI((int)role.getRoleId());
                this.redisService.set("province:" + role.getId(), record, -1L);
            }
            this.redisService.set("country:" + role.getId(), recordQuestion, -1L);
        }
        else {
            ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).conductQuestionXs(role, recordQuestion, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730001))), true);
        }
        return;
    }
    
    public void submitQuestionSs(Role role, ReqSubmitQuestionProvince submitQuestion) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730002))), "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "省试"));
            return;
        }
        RecordQuestion recordQuestion = (RecordQuestion)this.redisService.get("province:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null) {
            return;
        }
        if (recordQuestion.getQ() != submitQuestion.getSerial()) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 2;
        SysProvinceQuestion question = (SysProvinceQuestion)TaskDataPool.provinceQuestion.get(Integer.valueOf(submitQuestion.getSerial()));
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 3;
            msg = "恭喜您答对了#<E:93>#";
            recordQuestion.getR().add(Byte.valueOf((byte)1));
        }
        else {
            recordQuestion.getR().add(Byte.valueOf((byte)0));
        }
        this.roleService.addExp(role, DataExpFunc.calculateKj((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "省试答题", true);
        Integer combatPet = role.getExtendBox().getCombatPet();
        if ((int)combatPet > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateKj((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).award(role, (SysCommonAward)PartDataPool.commonAwardMap.get("province"));
        }
        MessagePusher.pushMessage(role, new RespMsg(msg));
        int num = recordQuestion.getR().size() + 1;
        if (num > 20) {
            int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(Byte.valueOf((byte)0))).count();
            recordQuestion.setE(current + mistakes * 30);
            int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
            this.taskService.deleteTask(role, this.taskService.getTaskSet(1730002));
            String talk = "你已完成省试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。殿试名单将于21:51公布，请留意放榜通知。";
            this.npcService.sendNpcContent(role, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730002))), "", talk);
            this.redisService.set("province:" + role.getId(), recordQuestion, -1L);
        }
        else {
            ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).conductQuestionSs(role, recordQuestion, new Npc((SysNpcInfo)DataCache.PLACE_NPC.get(Integer.valueOf(1730002))), true);
        }
        return;
    }
    
    public void submitQuestionDs(Role role, ReqSubmitQuestionHall submitQuestionHall) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        String key = "hall:" + role.getRoleId() + "_" + submitQuestionHall.getSerial() + "_" + KejuTaskHandler.hallNum;
        RecordHallQuestion question = (RecordHallQuestion)this.redisService.get(key, RecordHallQuestion.class);
        if (question == null) {
            this.logger.error("玩家{}殿试提交答题找不到缓存题：{}，次数{}", new Object[] { role.getName(), Integer.valueOf(submitQuestionHall.getSerial()), Integer.valueOf(KejuTaskHandler.hallNum) });
            return;
        }
        if (current - question.getS() > 30) {
            this.logger.error("玩家{}殿试提交答题超时缓存题：{}，次数{}", new Object[] { role.getName(), Integer.valueOf(submitQuestionHall.getSerial()), Integer.valueOf(KejuTaskHandler.hallNum) });
            return;
        }
        if (question.getQ() != submitQuestionHall.getSerial()) {
            return;
        }
        question.setT(current);
        SysHallQuestion hallQuestion = (SysHallQuestion)TaskDataPool.hallQuestion.get(Integer.valueOf(submitQuestionHall.getSerial()));
        if (hallQuestion == null) {
            question.setR((byte)0);
        }
        else if (hallQuestion.getAnswer().equals(submitQuestionHall.getAnswer())) {
            question.setR((byte)1);
        }
        else {
            question.setR((byte)0);
        }
        this.redisService.set(key, question, -1L);
        return;
    }
    
    private Question getDls(Map<Integer, Question> questionMap) {
        Integer[] keys = (Integer[])questionMap.keySet().toArray(new Integer[0]);
        int random = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(keys.length - 1));
        Integer randomKey = keys[random];
        return (Question)questionMap.get(randomKey);
    }
}
