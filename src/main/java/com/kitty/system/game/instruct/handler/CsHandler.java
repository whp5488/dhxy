package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.team.service.TeamService;
import org.nutz.dao.Dao;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.kitty.system.game.role.domain.Setting;
import java.util.List;
import java.util.Iterator;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Map;
import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.welfare.message.ReqMagicBoxInfo;
import com.kitty.system.game.welfare.message.ReqMagicBoxLotto;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.equip.service.EquipService;
import org.apache.commons.lang3.time.StopWatch;
import com.kitty.mina.cache.Temporary;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import org.nutz.dao.Cnd;
import com.kitty.system.init.service.InitService;
import com.kitty.cache.RedisService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.TimeUtil;
import java.util.Collections;
import java.util.ArrayList;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class CsHandler implements NcFather
{
    public CsHandler() {
    }
    
    @Override
    public void handle(Role role, String content) {
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            return;
        }
        Integer serial = Integer.valueOf(-1);
        String[] split = null;
        if (content.contains(",")) {
            split = content.split(",");
            serial = Integer.valueOf(split[0]);
        }
        else {
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
            emailSystem.setRecipient((long)role.getId());
            emailSystem.setContent(MessageFormat.format("恭喜少侠在本次科举考试中，取得{0}的好成绩", "榜眼"));
            emailSystem.setExpirationTime(System.currentTimeMillis() + 86400000L);
            List<EmailSystemAccessory> accessory = new ArrayList<>();
            accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP, String.valueOf(DataExpFunc.calculateKj((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), 10))));
            accessory.add(new EmailSystemAccessory((byte)5, (int)Const.RES_TYPE_EXP_PET, String.valueOf(DataExpFunc.calculateKj((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), 12))));
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
        if ((int)serial == 100) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int num = 0;
            Role captain = null;
            List<Role> roles = ((InitService)SpringUtils.getBean(InitService.class)).getRoles();
            for (Role payer : roles) {
                int le = (int)payer.getRelive() * 1000 + (int)payer.getLevel();
                if (le < 80) {
                    continue;
                }
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
                        ReqFoundJoin join2 = new ReqFoundJoin("", "", new int[0], 0, 0, 0, 0, 0);
                        join2.setRoleId((int)captain.getRoleId());
                        join2.setName(captain.getName());
                        join2.setType("creation_join");
                        SpringUtils.getTeamService().createTeam(payer, join2);
                    }
                    else {
                        TeamService teamService = SpringUtils.getTeamService();
                        ReqRequestJoin join = new ReqRequestJoin();
                        join.setRoleId((int)captain.getRoleId());
                        join.setName(captain.getName());
                        join.setType("request_join");
                        teamService.requestJoin(payer, join);
                        ReqAccept reqAccept = new ReqAccept();
                        reqAccept.setRoleId((int)payer.getRoleId());
                        reqAccept.setName(payer.getName());
                        reqAccept.setType("request_join");
                        teamService.accept(captain, reqAccept);
                    }
                    if (++num >= 3) {
                        int teamCount = SpringUtils.getTeamService().getTeamCount(captain);
                        if (teamCount >= 3) {
                            Temporary.ROLES.put(captain.getId(), captain);
                        }
                        num = 0;
                        captain = null;
                        if (Temporary.ROLES.size() > 9) {
                            break;
                        }
                        continue;
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            stopWatch.stop();
            System.out.println("耗時毫秒 = " + stopWatch.getTime());
            System.out.println("加载队伍数量 = " + Temporary.ROLES.size());
        }
        else if ((int)serial == 200) {
            for (Long id : Temporary.ROLES.keySet()) {
                Role p = (Role)Temporary.ROLES.get(id);
                ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).enterMap(p, null);
            }
        }
        else if ((int)serial == 201) {
            for (Long id : Temporary.ROLES.keySet()) {
                Role p = (Role)Temporary.ROLES.get(id);
                ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).enterMap(p, null);
            }
        }
        else if ((int)serial == 300) {
            ((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).cs();
        }
        else if ((int)serial == 301) {
            ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).cs();
        }
        else if ((int)serial == 302) {
            ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).cs();
        }
        else if ((int)serial == 303) {
            ((CaHandler)SpringUtils.getBean(CaHandler.class)).cs();
        }
        else if ((int)serial == 400) {
            RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
            MongoTemplate mongoTemplate = SpringUtils.getMongoTemplate();
            Dao dao = SpringUtils.getDao();
            List<Role> roles = ((InitService)SpringUtils.getBean(InitService.class)).getRoles();
            for (Role player : roles) {
                Role mySqlRole = (Role)dao.fetch(Role.class, Cnd.where("id", "=", player.getId()));
                Role redisRole = (Role)redisService.get(String.valueOf(player.getId()), Role.class);
                this.comparison(mySqlRole, redisRole);
            }
            System.out.println("数据比对完成");
        }
        else if ((int)serial == 404) {
            Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
            int time3 = new Long(System.currentTimeMillis() / 1000L).intValue();
            setting.setPlugEndTime(time3 + 86400);
        }
        else if ((int)serial == 702) {
            if ((int)role.getExtendBox().getCombatPet() <= 0) {
                return;
            }
            Map<Integer, Pet> petMap = SpringUtils.getPetService().selectPet(role);
            Pet pet = (Pet)petMap.get(role.getExtendBox().getCombatPet());
            if (pet != null) {
                List<Domesticate> list2 = Collections.synchronizedList(new ArrayList<>());
                int time2 = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
                Domesticate domesticate2 = new Domesticate(Long.valueOf(new IdWorker().nextId()), (long)role.getId(), (long)pet.getId(), (byte)LOGIC.LOGICTYPE_PET.getValue(), (byte)1, 0, time2, time2, 1);
                domesticate2.save();
                list2.add(domesticate2);
                DataCache.DOMESTICATE_MAP.put(role.getId(), list2);
            }
        }
        else if ((int)serial == 703) {
            if ((int)role.getExtendBox().getCombatPet() <= 0) {
                return;
            }
            Map<Long, Horse> horse = SpringUtils.getHorseService().getHorse(role);
            Iterator<Long> iterator = horse.keySet().iterator();
            if (iterator.hasNext()) {
                Long aLong = (Long)iterator.next();
                List<Domesticate> list = Collections.synchronizedList(new ArrayList<>());
                int time = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
                Domesticate domesticate = new Domesticate(Long.valueOf(new IdWorker().nextId()), (long)role.getId(), (long)aLong, (byte)LOGIC.HORSE.getValue(), (byte)1, 0, time, time, 1);
                domesticate.save();
                list.add(domesticate);
                DataCache.DOMESTICATE_MAP.put(role.getId(), list);
            }
        }
        else if ((int)serial > 1) {
            TaskService service = (TaskService)SpringUtils.getBean(TaskService.class);
            TaskSet taskSet = service.getTaskSet((int)serial);
            if (taskSet != null) {
                TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                taskHandler.acceptTask(role, taskSet, -1);
            }
            else {
                System.out.println("taskSet = " + taskSet);
            }
            SpringUtils.getRoleService().doDailyFiveReset(role, true);
            SpringUtils.getActivityService().getRespLivenessInfo(role);
        }
        return;
    }
    
    public void comparison(Role role, Role redisRole) {
        int noe = (int)role.getRelive() * 1000 + (int)role.getLevel();
        if (redisRole == null) {
            return;
        }
        int two = (int)redisRole.getRelive() * 1000 + (int)redisRole.getLevel();
        if (noe != two) {
            System.out.println("等级比对-唯一ID = " + role.getId() + "，MySql：" + noe + "，Redis：" + two);
        }
        return;
    }
}
