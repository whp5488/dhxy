package com.kitty.system.game.instruct.handler;

import com.kitty.cache.RedisService;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.Temporary;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.message.ReqMagicBoxInfo;
import com.kitty.system.game.welfare.message.ReqMagicBoxLotto;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.init.service.InitService;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.data.mongodb.core.MongoTemplate;

public class CsHandler implements NcFather {
  public void handle(Role role, String content) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    Integer serial = Integer.valueOf(-1);
    String[] split = null;
    if (content.contains(",")) {
      split = content.split(",");
      serial = Integer.valueOf(split[0]);
    } else {
      serial = Integer.valueOf(content);
    } 
    if (serial.equals(Integer.valueOf(2001))) {
      ((WelfareService)SpringUtils.getBean(WelfareService.class)).reqMagicBoxInfo(role, new ReqMagicBoxInfo());
      return;
    } 
    if (serial.equals(Integer.valueOf(2002))) {
      ((WelfareService)SpringUtils.getBean(WelfareService.class)).reqMagicBoxLotto(role, new ReqMagicBoxLotto());
      return;
    } 
    if (serial.equals(Integer.valueOf(1))) {
      ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).hallCertification();
      return;
    } 
    if (serial.equals(Integer.valueOf(2))) {
      ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).scheduled();
      return;
    } 
    if (serial.equals(Integer.valueOf(3))) {
      EmailSystem emailSystem = new EmailSystem();
      emailSystem.setEmailTitle("系统邮件");
      emailSystem.setSender(1);
      emailSystem.setRecipient(role.getId().longValue());
      emailSystem.setContent(MessageFormat.format("恭喜少侠在本次科举考试中，取得{0}的好成绩", new Object[] { "榜眼" }));
      emailSystem.setExpirationTime(System.currentTimeMillis() + 86400000L);
      List<EmailSystemAccessory> accessory = new ArrayList<>();
      accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP.intValue(), String.valueOf(DataExpFunc.calculateKj(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 10))));
      accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP_PET.intValue(), String.valueOf(DataExpFunc.calculateKj(role.getLevel().intValue(), role.getRelive().intValue(), ItemClassify.HERO_EXP.getId(), 12))));
      accessory.add(new EmailSystemAccessory((byte)1, KejuTaskHandler.getTopAward(0), "1"));
      accessory.add(new EmailSystemAccessory((byte)6, 310001, "0"));
      emailSystem.setAccessory(accessory);
      ((EmailService)SpringUtils.getBean(EmailService.class)).addEmailSystem(emailSystem);
      return;
    } 
    if (serial.equals(Integer.valueOf(4))) {
      ((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).reqKjRank(role, null);
      return;
    } 
    if (serial.equals(Integer.valueOf(5))) {
      SpringUtils.getPartyService().mergeParty();
      return;
    } 
    if (serial.equals(Integer.valueOf(106))) {
      SpringUtils.getRoleService().verificationCode(role, "15299876686");
      return;
    } 
    if (serial.equals(Integer.valueOf(107))) {
      SpringUtils.getRoleService().relationMobile(role, split[1]);
      return;
    } 
    if (serial.intValue() == 100) {
      StopWatch stopWatch = new StopWatch();
      stopWatch.start();
      int num = 0;
      Role captain = null;
      List<Role> roles = ((InitService)SpringUtils.getBean(InitService.class)).getRoles();
      for (Role payer : roles) {
        int le = payer.getRelive().intValue() * 1000 + payer.getLevel().intValue();
        if (le < 80)
          continue; 
        Role r = (Role)DataCache.ONLINE_ROLES.get(payer.getId());
        if (r == null) {
          ((EquipService)SpringUtils.getBean(EquipService.class)).selectWearEquip(payer);
          SpringUtils.getPetService().selectPet(payer);
          payer.doAfterInit();
          SpringUtils.getPlayerService().loadRoleRests(payer);
          DataCache.ONLINE_ROLES.put(payer.getId(), payer);
          ReqTelePort reqTelePort = new ReqTelePort();
          reqTelePort.setMapId(1011);
          reqTelePort.setX(132);
          reqTelePort.setY(67);
          SpringUtils.getMapService().changeMap(payer, reqTelePort, null);
          if (num <= 0) {
            captain = payer;
            ReqFoundJoin join = new ReqFoundJoin("", "", new int[0], 0, 0, 0, 0, 0);
            join.setRoleId(captain.getRoleId().intValue());
            join.setName(captain.getName());
            join.setType("creation_join");
            SpringUtils.getTeamService().createTeam(payer, join);
          } else {
            TeamService teamService = SpringUtils.getTeamService();
            ReqRequestJoin join = new ReqRequestJoin();
            join.setRoleId(captain.getRoleId().intValue());
            join.setName(captain.getName());
            join.setType("request_join");
            teamService.requestJoin(payer, join);
            ReqAccept reqAccept = new ReqAccept();
            reqAccept.setRoleId(payer.getRoleId().intValue());
            reqAccept.setName(payer.getName());
            reqAccept.setType("request_join");
            teamService.accept(captain, reqAccept);
          } 
          num++;
          if (num >= 3) {
            int teamCount = SpringUtils.getTeamService().getTeamCount(captain);
            if (teamCount >= 3)
              Temporary.ROLES.put(captain.getId(), captain); 
            num = 0;
            captain = null;
            if (Temporary.ROLES.size() > 9)
              break; 
          } 
        } 
      } 
      stopWatch.stop();
      System.out.println("耗時毫秒 = " + stopWatch.getTime());
      System.out.println("加载队伍数量 = " + Temporary.ROLES.size());
    } else if (serial.intValue() == 200) {
      for (Long id : Temporary.ROLES.keySet()) {
        Role p = (Role)Temporary.ROLES.get(id);
        ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).enterMap(p, null);
      } 
    } else if (serial.intValue() == 201) {
      for (Long id : Temporary.ROLES.keySet()) {
        Role p = (Role)Temporary.ROLES.get(id);
        ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).enterMap(p, null);
      } 
    } else if (serial.intValue() == 300) {
      ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).cs();
    } else if (serial.intValue() == 301) {
      ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).cs();
    } else if (serial.intValue() == 302) {
      ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).cs();
    } else if (serial.intValue() == 303) {
      ((CaHandler)SpringUtils.getBean(CaHandler.class)).cs();
    } else if (serial.intValue() == 400) {
      RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
      MongoTemplate mongoTemplate = SpringUtils.getMongoTemplate();
      Dao dao = SpringUtils.getDao();
      List<Role> roles = ((InitService)SpringUtils.getBean(InitService.class)).getRoles();
      for (Role player : roles) {
        Role mySqlRole = (Role)dao.fetch(Role.class, (Condition)Cnd.where("id", "=", player.getId()));
        Role redisRole = (Role)redisService.get(String.valueOf(player.getId()), Role.class);
        comparison(mySqlRole, redisRole);
      } 
      System.out.println("数据比对完成");
    } else if (serial.intValue() == 404) {
      Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
      int time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      setting.setPlugEndTime(time + 86400);
    } else if (serial.intValue() == 702) {
      if (role.getExtendBox().getCombatPet().intValue() <= 0)
        return; 
      Map<Integer, Pet> petMap = SpringUtils.getPetService().selectPet(role);
      Pet pet = petMap.get(role.getExtendBox().getCombatPet());
      if (pet != null) {
        List<Domesticate> list = Collections.synchronizedList(new ArrayList<>());
        int time = (new Long(TimeUtil.getTimeMills() / 1000L)).intValue();
        Domesticate domesticate = new Domesticate(Long.valueOf((new IdWorker()).nextId()), role.getId().longValue(), pet.getId().longValue(), (byte)LOGIC.LOGICTYPE_PET.getValue(), (byte)1, 0, time, time, 1);
        domesticate.save();
        list.add(domesticate);
        DataCache.DOMESTICATE_MAP.put(role.getId(), list);
      } 
    } else if (serial.intValue() == 703) {
      if (role.getExtendBox().getCombatPet().intValue() <= 0)
        return; 
      Map<Long, Horse> horse = SpringUtils.getHorseService().getHorse(role);
      Iterator<Long> iterator = horse.keySet().iterator();
      if (iterator.hasNext()) {
        Long aLong = iterator.next();
        List<Domesticate> list = Collections.synchronizedList(new ArrayList<>());
        int time = (new Long(TimeUtil.getTimeMills() / 1000L)).intValue();
        Domesticate domesticate = new Domesticate(Long.valueOf((new IdWorker()).nextId()), role.getId().longValue(), aLong.longValue(), (byte)LOGIC.HORSE.getValue(), (byte)1, 0, time, time, 1);
        domesticate.save();
        list.add(domesticate);
        DataCache.DOMESTICATE_MAP.put(role.getId(), list);
      } 
    } else if (serial.intValue() > 1) {
      TaskService service = (TaskService)SpringUtils.getBean(TaskService.class);
      TaskSet taskSet = service.getTaskSet(serial.intValue());
      if (taskSet != null) {
        TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
        taskHandler.acceptTask(role, taskSet, -1);
      } else {
        System.out.println("taskSet = " + taskSet);
      } 
      SpringUtils.getRoleService().doDailyFiveReset(role, true);
      SpringUtils.getActivityService().getRespLivenessInfo(role);
    } 
  }
  
  public void comparison(Role role, Role redisRole) {
    int noe = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    if (redisRole == null)
      return; 
    int two = redisRole.getRelive().intValue() * 1000 + redisRole.getLevel().intValue();
    if (noe != two)
      System.out.println("等级比对-唯一ID = " + role.getId() + "，MySql：" + noe + "，Redis：" + two); 
  }
}
