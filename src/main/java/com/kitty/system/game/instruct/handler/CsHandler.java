
package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.task.service.handler.TaskHandler;
import com.kitty.system.game.config.TaskSet;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Map;
import com.kitty.system.game.role.domain.Setting;
import org.nutz.dao.Dao;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.kitty.system.game.team.service.TeamService;
import java.util.Iterator;
import com.kitty.system.game.task.service.handler.TaskHelper;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.TimeUtil;
import java.util.List;
import java.util.Collections;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.service.SettingService;
import java.io.Serializable;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.cache.RedisService;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.mina.cache.Temporary;
import com.kitty.system.game.team.message.ReqAccept;
import com.kitty.system.game.team.message.ReqRequestJoin;
import com.kitty.system.game.team.message.ReqFoundJoin;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.init.service.InitService;
import org.apache.commons.lang3.time.StopWatch;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.ArrayList;
import java.text.MessageFormat;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.welfare.message.ReqMagicBoxLotto;
import com.kitty.system.game.welfare.message.ReqMagicBoxInfo;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class CsHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String content) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        Integer serial = -1;
        String[] split = null;
        if (content.contains(",")) {
            split = content.split(",");
            serial = Integer.valueOf(split[0]);
        }
        else {
            serial = Integer.valueOf(content);
        }
        if (serial.equals(2001)) {
            SpringUtils.getBean(WelfareService.class).reqMagicBoxInfo(role, new ReqMagicBoxInfo());
            return;
        }
        if (serial.equals(2002)) {
            SpringUtils.getBean(WelfareService.class).reqMagicBoxLotto(role, new ReqMagicBoxLotto());
            return;
        }
        if (serial.equals(1)) {
            SpringUtils.getBean(KejuTaskHandler.class).hallCertification();
            return;
        }
        if (serial.equals(2)) {
            SpringUtils.getBean(KejuTaskHandler.class).scheduled();
            return;
        }
        if (serial.equals(3)) {
            final EmailSystem emailSystem = new EmailSystem();
            emailSystem.setEmailTitle("系统邮件");
            emailSystem.setSender(1);
            emailSystem.setRecipient(role.getId());
            emailSystem.setContent(MessageFormat.format("恭喜少侠在本次科举考试中，取得{0}的好成绩", "榜眼"));
            emailSystem.setExpirationTime(System.currentTimeMillis() + 86400000L);
            final List<EmailSystemAccessory> accessory = new ArrayList<EmailSystemAccessory>();
            accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP, String.valueOf(DataExpFunc.calculateKj(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), 10))));
            accessory.add(new EmailSystemAccessory((byte)5, Const.RES_TYPE_EXP_PET, String.valueOf(DataExpFunc.calculateKj(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), 12))));
            accessory.add(new EmailSystemAccessory((byte)1, KejuTaskHandler.getTopAward(0), "1"));
            accessory.add(new EmailSystemAccessory((byte)6, 310001, "0"));
            emailSystem.setAccessory(accessory);
            SpringUtils.getBean(EmailService.class).addEmailSystem(emailSystem);
            return;
        }
        if (serial.equals(4)) {
            SpringUtils.getBean(KejuTaskHandler.class).reqKjRank(role, null);
            return;
        }
        if (serial.equals(5)) {
            SpringUtils.getPartyService().mergeParty();
            return;
        }
        if (serial.equals(106)) {
            SpringUtils.getRoleService().verificationCode(role, "15299876686");
            return;
        }
        if (serial.equals(107)) {
            SpringUtils.getRoleService().relationMobile(role, split[1]);
            return;
        }
        if (serial == 100) {
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int num = 0;
            Role captain = null;
            final List<Role> roles = SpringUtils.getBean(InitService.class).getRoles();
            for (final Role payer : roles) {
                final int le = payer.getRelive() * 1000 + payer.getLevel();
                if (le < 80) {
                    continue;
                }
                final Role r = DataCache.ONLINE_ROLES.get(payer.getId());
                if (r != null) {
                    continue;
                }
                SpringUtils.getBean(EquipService.class).selectWearEquip(payer);
                SpringUtils.getPetService().selectPet(payer);
                payer.doAfterInit();
                SpringUtils.getPlayerService().loadRoleRests(payer);
                DataCache.ONLINE_ROLES.put(payer.getId(), payer);
                final ReqTelePort reqTelePort = new ReqTelePort();
                reqTelePort.setMapId(1011);
                reqTelePort.setX(132);
                reqTelePort.setY(67);
                SpringUtils.getMapService().changeMap(payer, reqTelePort, null);
                if (num <= 0) {
                    captain = payer;
                    final ReqFoundJoin join = new ReqFoundJoin("", "", new int[0], 0, 0, 0, 0, 0);
                    join.setRoleId(captain.getRoleId());
                    join.setName(captain.getName());
                    join.setType("creation_join");
                    SpringUtils.getTeamService().createTeam(payer, join);
                }
                else {
                    final TeamService teamService = SpringUtils.getTeamService();
                    final ReqRequestJoin join2 = new ReqRequestJoin();
                    join2.setRoleId(captain.getRoleId());
                    join2.setName(captain.getName());
                    join2.setType("request_join");
                    teamService.requestJoin(payer, join2);
                    final ReqAccept reqAccept = new ReqAccept();
                    reqAccept.setRoleId(payer.getRoleId());
                    reqAccept.setName(payer.getName());
                    reqAccept.setType("request_join");
                    teamService.accept(captain, reqAccept);
                }
                if (++num < 3) {
                    continue;
                }
                final int teamCount = SpringUtils.getTeamService().getTeamCount(captain);
                if (teamCount >= 3) {
                    Temporary.ROLES.put(captain.getId(), captain);
                }
                num = 0;
                captain = null;
                if (Temporary.ROLES.size() > 9) {
                    break;
                }
            }
            stopWatch.stop();
            System.out.println("耗時毫秒 = " + stopWatch.getTime());
            System.out.println("加载队伍数量 = " + Temporary.ROLES.size());
        }
        else if (serial == 200) {
            for (final Long id : Temporary.ROLES.keySet()) {
                final Role p = Temporary.ROLES.get(id);
                SpringUtils.getBean(AtWillHandler.class).enterMap(p, null);
            }
        }
        else if (serial == 201) {
            for (final Long id : Temporary.ROLES.keySet()) {
                final Role p = Temporary.ROLES.get(id);
                SpringUtils.getBean(WaterHandler.class).enterMap(p, null);
            }
        }
        else if (serial == 300) {
            SpringUtils.getBean(AtWillHandler.class).cs();
        }
        else if (serial == 301) {
            SpringUtils.getBean(WaterHandler.class).cs();
        }
        else if (serial == 302) {
            SpringUtils.getBean(GangWarsHandler.class).cs();
        }
        else if (serial == 303) {
            SpringUtils.getBean(CaHandler.class).cs();
        }
        else if (serial == 400) {
            final RedisService redisService = SpringUtils.getBean(RedisService.class);
            final MongoTemplate mongoTemplate = SpringUtils.getMongoTemplate();
            final Dao dao = SpringUtils.getDao();
            final List<Role> roles = SpringUtils.getBean(InitService.class).getRoles();
            for (final Role player : roles) {
                final Role mySqlRole = (Role)dao.fetch(Role.class, Cnd.where("id", "=", player.getId()));
                final Role redisRole = redisService.get(String.valueOf(player.getId()), Role.class);
                this.comparison(mySqlRole, redisRole);
            }
            System.out.println("数据比对完成");
        }
        else if (serial == 404) {
            final Setting setting = SpringUtils.getBean(SettingService.class).getSetting(role);
            final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
            setting.setPlugEndTime(time + 86400);
        }
        else if (serial == 702) {
            if (role.getExtendBox().getCombatPet() <= 0) {
                return;
            }
            final Map<Integer, Pet> petMap = SpringUtils.getPetService().selectPet(role);
            final Pet pet = petMap.get(role.getExtendBox().getCombatPet());
            if (pet != null) {
                final List<Domesticate> list = Collections.synchronizedList(new ArrayList<Domesticate>());
                final int time2 = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
                final Domesticate domesticate = new Domesticate(new IdWorker().nextId(), role.getId(), pet.getId(), (byte)LOGIC.LOGICTYPE_PET.getValue(), (byte)1, 0, time2, time2, 1);
                domesticate.save();
                list.add(domesticate);
                DataCache.DOMESTICATE_MAP.put(role.getId(), list);
            }
        }
        else if (serial == 703) {
            if (role.getExtendBox().getCombatPet() <= 0) {
                return;
            }
            final Map<Long, Horse> horse = SpringUtils.getHorseService().getHorse(role);
            final Iterator<Long> iterator5 = horse.keySet().iterator();
            if (iterator5.hasNext()) {
                final Long aLong = iterator5.next();
                final List<Domesticate> list2 = Collections.synchronizedList(new ArrayList<Domesticate>());
                final int time3 = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
                final Domesticate domesticate2 = new Domesticate(new IdWorker().nextId(), role.getId(), aLong, (byte)LOGIC.HORSE.getValue(), (byte)1, 0, time3, time3, 1);
                domesticate2.save();
                list2.add(domesticate2);
                DataCache.DOMESTICATE_MAP.put(role.getId(), list2);
            }
        }
        else if (serial > 1) {
            final TaskService service = SpringUtils.getBean(TaskService.class);
            final TaskSet taskSet = service.getTaskSet(serial);
            if (taskSet != null) {
                final TaskHandler taskHandler = TaskHelper.INSTANCE.getTaskHandler(taskSet.getTaskType());
                taskHandler.acceptTask(role, taskSet, -1);
            }
            else {
                System.out.println("taskSet = " + taskSet);
            }
            SpringUtils.getRoleService().doDailyFiveReset(role, true);
            SpringUtils.getActivityService().getRespLivenessInfo(role);
        }
    }
    
    public void comparison(final Role role, final Role redisRole) {
        final int noe = role.getRelive() * 1000 + role.getLevel();
        if (redisRole == null) {
            return;
        }
        final int two = redisRole.getRelive() * 1000 + redisRole.getLevel();
        if (noe != two) {
            System.out.println("等级比对-唯一ID = " + role.getId() + "，MySql：" + noe + "，Redis：" + two);
        }
    }
}
