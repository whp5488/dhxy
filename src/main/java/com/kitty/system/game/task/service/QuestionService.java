
package com.kitty.system.game.task.service;

import com.kitty.common.utils.RandomUtil;
import java.util.Map;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.game.task.domain.vo.RecordHallQuestion;
import com.kitty.system.game.task.message.ReqSubmitQuestionHall;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.game.task.message.ReqSubmitQuestionProvince;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.game.task.domain.vo.RecordQuestion;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.SysNpcInfo;
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
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.task.message.RespQuestion;
import com.kitty.system.game.task.pool.TaskDataPool;
import java.io.Serializable;
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
    
    public void reqQuestionDls(final Role role, final Npc npc) {
        final int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.QUESTION_DLS);
        final int sum = (int)(activityLivenessSet.getMaxLiveness() / activityLivenessSet.getPerLiveness());
        if (num >= sum) {
            final String msg = "你今天已经接受过我的考验了，明天再来吧#<E:17>#";
            this.npcService.sendNpcContent(role, npc, "", msg);
            return;
        }
        String content = String.format("%s/get_dls|我还没有准备好/离开", "我要参加有奖回答");
        if (num > 0) {
            content = String.format("%s/get_dls|我还没有准备好/离开", "大理寺答题（未完成）");
        }
        this.npcService.sendNpcContent(role, npc, content, null);
    }
    
    public void getQuestionDls(final Role role, final Npc npc) {
        final int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        Question question = this.redisService.get(role.getId() + "dls", Question.class);
        if (question == null) {
            question = this.getDls(TaskDataPool.questionMap);
        }
        final RespQuestion respQuestion = new RespQuestion(1, question.getSerial(), num + 1);
        MessagePusher.pushMessage(role, respQuestion);
        this.redisService.set(role.getId() + "dls", question, 180L);
    }
    
    public void submitQuestionDls(final Role role, final ReqSubmitQuestion submitQuestion) {
        int num = role.getTaskProgress().getFinishCounts().getInt(ActivityType.QUESTION_DLS.getActivityId(), 0);
        final ActivityLivenessSet activityLivenessSet = ActivityDataPool.activityLivenessSetMapByType.get(ActivityType.QUESTION_DLS);
        final int sum = (int)(activityLivenessSet.getMaxLiveness() / activityLivenessSet.getPerLiveness());
        if (num > sum) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 1;
        Question question = TaskDataPool.questionMap.get(submitQuestion.getSerial());
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 2;
            msg = "恭喜您答对了#<E:93>#";
        }
        this.roleService.addExp(role, DataExpFunc.calculateDls(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "大理寺答题", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateDls(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        final int step = 1;
        final ActivityService activityService = SpringUtils.getBean(ActivityService.class);
        activityService.addFinishCount(role, ActivityType.QUESTION_DLS, step);
        this.chatService.sendInfo(role.getId(), "你获得了#<G>" + step + "#点活跃度");
        MessagePusher.pushMessage(role, new RespMsg(msg));
        ++num;
        if (num < sum) {
            question = this.getDls(TaskDataPool.questionMap);
            final RespQuestion respQuestion = new RespQuestion(1, question.getSerial(), num + 1);
            MessagePusher.pushMessage(role, respQuestion);
            this.redisService.set(role.getId() + "dls", question, 180L);
        }
    }
    
    public void submitQuestionXs(final Role role, final ReqSubmitQuestionCountry submitQuestion) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        final int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        final Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        final int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, new Npc(DataCache.PLACE_NPC.get(1730001)), "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "乡试"));
            return;
        }
        final RecordQuestion recordQuestion = this.redisService.get("country:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null) {
            return;
        }
        if (recordQuestion.getQ() != submitQuestion.getSerial()) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 1;
        final SysCountryQuestion question = TaskDataPool.countryQuestion.get(submitQuestion.getSerial());
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 2;
            msg = "恭喜您答对了#<E:93>#";
            recordQuestion.getR().add((byte)1);
        }
        else {
            recordQuestion.getR().add((byte)0);
        }
        this.roleService.addExp(role, DataExpFunc.calculateKj(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "乡试答题", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateKj(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            SpringUtils.getBean(KejuTaskHandler.class).award(role, PartDataPool.commonAwardMap.get("country"));
        }
        MessagePusher.pushMessage(role, new RespMsg(msg));
        final int num = recordQuestion.getR().size() + 1;
        if (num > 20) {
            final int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(0)).count();
            final int minutes = (current - recordQuestion.getS() + mistakes * 30) / 60 % 60;
            recordQuestion.setE(current + mistakes * 30);
            final int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
            this.taskService.deleteTask(role, this.taskService.getTaskSet(1730001));
            if (minutes >= 10) {
                final String talk = "你已完成乡试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。#<E:50>#";
                this.npcService.sendNpcContent(role, new Npc(DataCache.PLACE_NPC.get(1730001)), "", talk);
            }
            else {
                final String talk = "你已完成乡试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。#<E:50>#\n恭喜少侠获得省试资格，请于今日21:50前找国子监#<PRB0>礼部官员#，参加省试。";
                this.npcService.sendNpcContent(role, new Npc(DataCache.PLACE_NPC.get(1730001)), "", talk);
                SpringUtils.getBean(KejuTaskHandler.class).acceptTask(role, expire, 1730002);
                final RecordQuestion record = new RecordQuestion();
                record.setI(role.getRoleId());
                this.redisService.set("province:" + role.getId(), record, -1L);
            }
            this.redisService.set("country:" + role.getId(), recordQuestion, -1L);
        }
        else {
            SpringUtils.getBean(KejuTaskHandler.class).conductQuestionXs(role, recordQuestion, new Npc(DataCache.PLACE_NPC.get(1730001)), true);
        }
    }
    
    public void submitQuestionSs(final Role role, final ReqSubmitQuestionProvince submitQuestion) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        final int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        final Calendar cal = Calendar.getInstance();
        cal.set(11, 21);
        cal.set(12, 50);
        cal.set(13, 0);
        final int expire = new Long(cal.getTimeInMillis() / 1000L).intValue();
        if (current >= expire) {
            this.npcService.sendNpcContent(role, new Npc(DataCache.PLACE_NPC.get(1730002)), "", String.format("本届科举%s已圆满结束了，少侠下届再来吧", "省试"));
            return;
        }
        final RecordQuestion recordQuestion = this.redisService.get("province:" + role.getId(), RecordQuestion.class);
        if (recordQuestion == null) {
            return;
        }
        if (recordQuestion.getQ() != submitQuestion.getSerial()) {
            return;
        }
        String msg = "很可惜，你答错了#<E:17>#";
        int multiple = 2;
        final SysProvinceQuestion question = TaskDataPool.provinceQuestion.get(submitQuestion.getSerial());
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            multiple = 3;
            msg = "恭喜您答对了#<E:93>#";
            recordQuestion.getR().add((byte)1);
        }
        else {
            recordQuestion.getR().add((byte)0);
        }
        this.roleService.addExp(role, DataExpFunc.calculateKj(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "省试答题", true);
        final Integer combatPet = role.getExtendBox().getCombatPet();
        if (combatPet > 0) {
            final Pet pet = this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, DataExpFunc.calculateKj(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
        }
        if (question.getAnswer().equals(submitQuestion.getAnswer())) {
            SpringUtils.getBean(KejuTaskHandler.class).award(role, PartDataPool.commonAwardMap.get("province"));
        }
        MessagePusher.pushMessage(role, new RespMsg(msg));
        final int num = recordQuestion.getR().size() + 1;
        if (num > 20) {
            final int mistakes = (int)recordQuestion.getR().stream().filter(e -> e.equals(0)).count();
            recordQuestion.setE(current + mistakes * 30);
            final int differenceMillis = recordQuestion.getE() - recordQuestion.getS();
            this.taskService.deleteTask(role, this.taskService.getTaskSet(1730002));
            final String talk = "你已完成省试，考试用时" + differenceMillis / 60 % 60 + "分钟" + differenceMillis % 60 + "秒。殿试名单将于21:51公布，请留意放榜通知。";
            this.npcService.sendNpcContent(role, new Npc(DataCache.PLACE_NPC.get(1730002)), "", talk);
            this.redisService.set("province:" + role.getId(), recordQuestion, -1L);
        }
        else {
            SpringUtils.getBean(KejuTaskHandler.class).conductQuestionSs(role, recordQuestion, new Npc(DataCache.PLACE_NPC.get(1730002)), true);
        }
    }
    
    public void submitQuestionDs(final Role role, final ReqSubmitQuestionHall submitQuestionHall) {
        if (!KejuTaskHandler.isActivity()) {
            return;
        }
        final int current = new Long(System.currentTimeMillis() / 1000L).intValue();
        final String key = "hall:" + role.getRoleId() + "_" + submitQuestionHall.getSerial() + "_" + KejuTaskHandler.hallNum;
        final RecordHallQuestion question = this.redisService.get(key, RecordHallQuestion.class);
        if (question == null) {
            this.logger.error("玩家{}殿试提交答题找不到缓存题：{}，次数{}", role.getName(), submitQuestionHall.getSerial(), KejuTaskHandler.hallNum);
            return;
        }
        if (current - question.getS() > 30) {
            this.logger.error("玩家{}殿试提交答题超时缓存题：{}，次数{}", role.getName(), submitQuestionHall.getSerial(), KejuTaskHandler.hallNum);
            return;
        }
        if (question.getQ() != submitQuestionHall.getSerial()) {
            return;
        }
        question.setT(current);
        final SysHallQuestion hallQuestion = TaskDataPool.hallQuestion.get(submitQuestionHall.getSerial());
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
    }
    
    private Question getDls(final Map<Integer, Question> questionMap) {
        final Integer[] keys = questionMap.keySet().toArray(new Integer[0]);
        final int random = RandomUtil.round(0, keys.length - 1);
        final Integer randomKey = keys[random];
        return questionMap.get(randomKey);
    }
}
