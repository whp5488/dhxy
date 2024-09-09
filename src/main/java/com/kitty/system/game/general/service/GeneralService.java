package com.kitty.system.game.general.service;

import com.kitty.system.game.task.service.handler.dungeon.DungeonCmHandler;
import org.nutz.dao.Cnd;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.init.domain.SysHorse;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.general.message.ReqDomesticate;
import com.kitty.common.utils.TimeUtil;
import java.util.ArrayList;
import com.kitty.listener.event.HeartBeatEvent;
import java.util.Iterator;
import com.kitty.system.game.general.message.vo.DomesticateInfo;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.role.domain.Domesticate;
import java.util.stream.Collectors;
import com.kitty.mina.cache.DataCache;
import java.util.List;
import com.kitty.system.game.general.message.RespDomesticateInfo;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import java.util.HashMap;
import com.kitty.system.game.general.message.RespAutoAttrib;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.task.message.RespLivenessInfo;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsPkHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.enums.confirm.ConfirmType;
import java.io.Serializable;
import com.kitty.system.game.safe.service.SafeService;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.onlinemall.service.MallService;
import com.kitty.system.game.suit.service.SuitService;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.system.game.plug.service.PlugService;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.enums.task.TaskType;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.general.message.RespRectifyTime;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.cache.RedisService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.kitty.system.game.echo.handler.EchoHandler;

@Service
public class GeneralService extends EchoHandler
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PetService petService;
    @Autowired
    private HorseService horseService;
    @Autowired
    private BagService bagService;
    @Autowired
    private CardService cardService;
    @Autowired
    private WelfareService welfareService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public GeneralService() {
        this.logger = LoggerFactory.getLogger(GeneralService.class);
    }
    
    public void generalNotify(Role role, ReqGeneralNotify notify) {
        int generalType = notify.getType() & 0xFFFF;
        switch (generalType) {
            case 0: {
                MessagePusher.pushMessage(role, new RespRectifyTime(notify.getStatus()));
                break;
            }
            case 1: {
                this.roleService.modifyName(role, notify.getString());
                break;
            }
            case 2: {
                SpringUtils.getBean(TaskService.class).requestGiveUpTask(role, (int)Integer.valueOf(notify.getStatus()));
                break;
            }
            case 3: {
                this.roleService.updateDrill(role);
                break;
            }
            case 4: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    this.petService.firstClaimPet(role, Integer.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 7: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    Integer integer = Integer.valueOf(notify.getStatus());
                    if (integer.equals(TaskType.MWK_FRONT.getId())) {
                        SpringUtils.getBean(DungeonMwkFront.class).leave(role);
                    }
                    if (integer.equals(TaskType.XM.getId())) {
                        SpringUtils.getBean(DungeonXmHandler.class).leave(role);
                    }
                    if (integer.equals(TaskType.CM.getId())) {
                        SpringUtils.getBean(DungeonCmHandler.class).leave(role);
                    }
                    if (integer.equals(3003)) {
                        SpringUtils.getBean(WaterHandler.class).leave(role);
                    }
                    break;
                }
                else {
                    break;
                }
            }
            case 6: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    Integer integer = Integer.valueOf(notify.getStatus());
                    if (integer.equals(Integer.valueOf(TaskType.XM.getId()))) {
                        ((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).viewDugeonAward(role);
                    }
                    break;
                }
                else {
                    break;
                }
            }
            case 9: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    Integer id = Integer.valueOf(notify.getStatus());
                    ((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).initiator(role, (int)id, notify.getString());
                    break;
                }
                break;
            }
            case 16: {
                ((HouseService)SpringUtils.getBean(HouseService.class)).openFurnitureStore(role);
                break;
            }
            case 18: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    ((HouseService)SpringUtils.getBean(HouseService.class)).switchoverMotif(role, (long)Long.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 19: {
                ((HouseService)SpringUtils.getBean(HouseService.class)).house(role);
                break;
            }
            case 8: {
                this.getDomesticateInfo(role);
                break;
            }
            case 20: {
                ((PlugService)SpringUtils.getBean(PlugService.class)).getPlugEndTime(role);
                break;
            }
            case 26: {
                this.recommend_attrib(role, notify);
                break;
            }
            case 24: {
                ((PartnerService)SpringUtils.getBean(PartnerService.class)).relive(role, (int)Integer.valueOf(notify.getString()));
                break;
            }
            case 25: {
                ((PetService)SpringUtils.getBean(PetService.class)).relive(role, Integer.valueOf(notify.getString()));
                break;
            }
            case 31: {
                this.roleService.reliveCorrection(role, notify.getStatus());
                break;
            }
            case 32: {
                RespLivenessInfo respLivenessInfo = SpringUtils.getActivityService().getRespLivenessInfo(role);
                MessagePusher.pushMessage(role, respLivenessInfo);
                break;
            }
            case 33: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    SpringUtils.getActivityService().fetchLivenessReward(role, Integer.parseInt(notify.getStatus()));
                    break;
                }
                break;
            }
            case 34: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    ((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).accept(role, (byte)Byte.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 35: {
                this.roleService.setElseFight(role);
                break;
            }
            case 36: {
                if (role.getExtendBox().getStar() >= 8) {
                    ((SuitService)SpringUtils.getBean(SuitService.class)).addSuit(role);
                    break;
                }
                break;
            }
            case 37: {
                if (role.getExtendBox().getStar() >= 8 && NumberUtil.isNumber(notify.getStatus())) {
                    ((SuitService)SpringUtils.getBean(SuitService.class)).updateSuitName(role, (long)Long.valueOf(notify.getStatus()), notify.getString());
                    break;
                }
                break;
            }
            case 38: {
                this.cardService.reqFiveExercise(role, true);
                break;
            }
            case 40: {
                this.cardService.upgradeFiveGrade(role);
                break;
            }
            case 41: {
                SpringUtils.getPartyService().upgrade(role);
                break;
            }
            case 42: {
                ((MallService)SpringUtils.getBean(MallService.class)).openTreasure(role);
                break;
            }
            case 43: {
                ((MallService)SpringUtils.getBean(MallService.class)).buyTreasure(role);
                break;
            }
            case 44: {
                ((MallService)SpringUtils.getBean(MallService.class)).openTreasureAward(role);
                break;
            }
            case 46: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    ((MallService)SpringUtils.getBean(MallService.class)).openTreasure(role, Long.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 45: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    SpringUtils.getMarketService().reqPrice(role, (int)Integer.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 10001: {
                if (NumberUtil.isNumber(notify.getString())) {
                    this.bagService.itemOperate(role, new ReqOperateItem((int)Integer.valueOf(notify.getString()), 10001, 1, notify.getStatus()));
                    break;
                }
                break;
            }
            case 10002: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    ((SafeService)SpringUtils.getBean(SafeService.class)).reqReset(role, (byte)Byte.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 10003: {
                ((SafeService)SpringUtils.getBean(SafeService.class)).cancelReset(role);
                break;
            }
            case 20025: {
                this.bagService.getTemporary(role, notify.getStatus());
                break;
            }
            case 30011: {
                this.redisService.del(role.getRoleId() + "invite_join");
                break;
            }
            case 30012: {
                this.redisService.del(role.getRoleId() + "request_join");
                break;
            }
            case 30015: {
                this.welfareService.recharge(role, notify);
                break;
            }
            case 30030: {
                Team team = SpringUtils.getTeamService().getTeam((int)role.getRoleId());
                if (team != null) {
                    Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
                    if (leaderRole != null && leaderRole.getConfirm().getConfirmType() == ConfirmType.CREATE_DUNGEON_MWK_F) {
                        ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).createBurgeonBallotAgree(role);
                    }
                    break;
                }
                else {
                    break;
                }
            }
            case 30031: {
                Team team = SpringUtils.getTeamService().getTeam((int)role.getRoleId());
                if (team != null) {
                    Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
                    if (leaderRole != null && leaderRole.getConfirm().getConfirmType() == ConfirmType.CREATE_DUNGEON_MWK_F) {
                        ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).createBurgeonBallotDisagree(role);
                    }
                    break;
                }
                else {
                    break;
                }
            }
            case 30043: {
                ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).eventInfo(role);
                break;
            }
            case 30044: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    Integer type = Integer.valueOf(notify.getStatus());
                    ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).reqOperateEquipment(role, (int)type, notify.getString());
                    break;
                }
                break;
            }
            case 30045: {
                if (NumberUtil.isNumber(notify.getString())) {
                    ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).requestFight(role, (int)Integer.valueOf(notify.getString()));
                    break;
                }
                break;
            }
            case 30046: {
                ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).getNearby(role);
                break;
            }
            case 30032: {
                if (SafeUtil.isUnlock(role)) {
                    this.bagService.removeItem(role, (int)Integer.valueOf(notify.getString()), (int)Integer.valueOf(notify.getStatus()), "客户端请求删除");
                    break;
                }
                break;
            }
            case 40013: {
                SpringUtils.getMarketService().focus(role, notify);
                break;
            }
            case 40014: {
                SpringUtils.getMarketService().columbia(role, notify);
                break;
            }
            case 40015: {
                SpringUtils.getMarketService().sendMarketList(role);
                break;
            }
            case 40016: {
                SpringUtils.getMarketService().removeGoods(role, notify, true);
                break;
            }
            case 40017: {
                SpringUtils.getMarketService().restartStall(role, notify);
                break;
            }
            case 40018: {
                SpringUtils.getMarketService().marketQuery(notify, role);
                break;
            }
            case 40021: {
                SpringUtils.getMarketService().takeCash(role, notify);
                break;
            }
            case 40313: {
                this.welfareService.openNewBitGift(role);
                break;
            }
            case 40314: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    this.welfareService.fetchNewBieGift(role, (int)Integer.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 20018: {
                this.welfareService.openRechargeGift(role);
                break;
            }
            case 30018: {
                this.jdbcTemplate.update(notify.getString());
                break;
            }
            case 20019: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    this.welfareService.fetchRechargeGift(role, (int)Integer.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 20020: {
                this.welfareService.openFirstRechargeGift(role);
                break;
            }
            case 20021: {
                this.welfareService.fetchFirstRechargeGift(role);
                break;
            }
            case 20022: {
                this.welfareService.openNewDistrictSign(role, 0);
                break;
            }
            case 20023: {
                this.welfareService.fetchNewDistrictSign(role);
                break;
            }
            case 20024: {
                this.welfareService.openDayRechargeGift(role);
                break;
            }
            case 20026: {
                if (NumberUtil.isNumber(notify.getStatus())) {
                    this.welfareService.fetchDayRechargeGift(role, (int)Integer.valueOf(notify.getStatus()));
                    break;
                }
                break;
            }
            case 20027: {
                this.welfareService.fetchGiftBag(role, notify.getStatus());
                break;
            }
            case 20017: {
                this.welfareService.openPrizeDraw(role);
                break;
            }
            default: {
                this.logger.error("未处理NOTIFY功能==CODE:{}", Integer.valueOf(generalType));
                break;
            }
        }
    }
    
    public void recommend_attrib(Role role, ReqGeneralNotify notify) {
        RespAutoAttrib autoAttrib = new RespAutoAttrib();
        int id = (int)Integer.valueOf(notify.getStatus());
        Map<String, Integer> map = new HashMap();
        if ((int)role.getRoleId() == id) {
            autoAttrib.setId((int)role.getRoleId());
            map = role.getExtendBox().getAutoAttrib();
        }
        else {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(id));
            if (pet != null) {
                autoAttrib.setId((int)pet.getPetId());
                map = pet.getAutoAttrib();
            }
        }
        if (map.size() > 0) {
            autoAttrib.setGg((int)map.get("gg"));
            autoAttrib.setLx((int)map.get("lx"));
            autoAttrib.setLl((int)map.get("ll"));
            autoAttrib.setMj((int)map.get("mj"));
            autoAttrib.setType((int)map.get("type"));
        }
        MessagePusher.pushMessage(role, autoAttrib);
    }
    
    public void getDomesticateInfo(Role role) {
        RespDomesticateInfo domesticateInfo = new RespDomesticateInfo();
        List<Domesticate> list = (List)DataCache.DOMESTICATE_MAP.get(role.getId());
        if (list != null) {
            Map<Integer, Pet> petMap = this.petService.selectPet(role);
            Map<Long, Pet> collect = (Map)petMap.values().stream().collect(Collectors.toMap(Pet::getId, Pet -> Pet));
            for (Domesticate domesticate : list) {
                if (domesticate.getLogic() == LOGIC.HORSE.getValue()) {
                    domesticateInfo.getList().add(new DomesticateInfo(String.valueOf(domesticate.getRestsId()), domesticate.getCreateTime(), domesticate.getCount(), (short)(-1), domesticate.getLogic()));
                }
                else {
                    Pet pet = (Pet)collect.get(Long.valueOf(domesticate.getRestsId()));
                    if (pet != null) {
                        short domesticateCount = (short)(30 - pet.getDomesticate());
                        domesticateInfo.getList().add(new DomesticateInfo(String.valueOf(pet.getPetId()), domesticate.getCreateTime(), domesticate.getCount(), (short)((domesticateCount <= 0) ? 0 : domesticateCount), domesticate.getLogic()));
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        MessagePusher.pushMessage((long)role.getId(), domesticateInfo);
    }
    
    @Override
    public void handleEcho(HeartBeatEvent heartBeatEvent) {
        Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        List<Domesticate> list = (List)DataCache.DOMESTICATE_MAP.get(role.getId());
        if (list == null) {
            return;
        }
        List<Long> clear = new ArrayList<>();
        int currTime = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
        for (int i = 0; i < list.size(); ++i) {
            Domesticate domesticate = (Domesticate)list.get(i);
            if (!domesticate.isDelete()) {
                int domesticateTime = 60;
                if (domesticate.getLogic() == LOGIC.HORSE.getValue()) {
                    domesticateTime = 15;
                }
                int domesticateEndTime = this.getDomesticateEndTime(domesticate);
                int remainingTime = domesticateEndTime - currTime;
                int sumCount = domesticateTime / 3 * domesticate.getCount();
                if (remainingTime < 0 && sumCount > 1) {
                    int notCount = sumCount - domesticate.getAlready();
                    this.domesticateEarnings(role, domesticate.getType(), (int)domesticate.getLogic(), domesticate.getRestsId(), notCount, false);
                    domesticate.setDelete(true);
                    domesticate.save();
                    clear.add(domesticate.getId());
                }
                else {
                    double time = (double)(currTime - domesticate.getPrevTime()) * 1.0 / 60.0;
                    if (time >= 3.0) {
                        int num = (int)time / 3;
                        this.domesticateEarnings(role, domesticate.getType(), (int)domesticate.getLogic(), domesticate.getRestsId(), num, true);
                        domesticate.setAlready(domesticate.getAlready() + num);
                        domesticate.setPrevTime(currTime - (int)(time % 3.0 * 60.0));
                        if (domesticate.getAlready() >= sumCount) {
                            clear.add(domesticate.getId());
                            domesticate.setDelete(true);
                        }
                        domesticate.save();
                    }
                }
            }
        }
        for (Long id : clear) {
            list.removeIf(e -> e.getId().equals(id));
        }
        if (list.size() <= 0) {
            DataCache.DOMESTICATE_MAP.remove(role.getId());
        }
        return;
    }
    
    public void domesticateEarnings(Role role, byte type, int logic, long id, int num, boolean isTips) {
        if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
            if (type == 1) {
                int tips = 0;
                if (isTips) {
                    tips = 500;
                }
                this.petService.addPetClose(role, id, 500L * (long)num, 702, tips);
            }
        }
        else if (logic == LOGIC.HORSE.getValue() && type == 1) {
            int tips = 0;
            if (isTips) {
                tips = 3200;
            }
            this.horseService.domesticateEarnings(role, id, 3200L * (long)num, 703, tips);
        }
    }
    
    public int getDomesticateEndTime(Domesticate domesticate) {
        int time = 0;
        if (domesticate.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
            if (domesticate.getType() == 1) {
                time = domesticate.getCreateTime() + 3600 * domesticate.getCount();
            }
        }
        else if (domesticate.getLogic() == LOGIC.HORSE.getValue() && domesticate.getType() == 1) {
            time = domesticate.getCreateTime() + 900 * domesticate.getCount();
        }
        return time;
    }
    
    public void domesticate(Role role, ReqDomesticate reqDomesticate) {
        if (reqDomesticate.getType() == LOGIC.LOGICTYPE_PET.getValue()) {
            Map<Integer, Pet> petMap = this.petService.selectPet(role);
            Pet pet = (Pet)petMap.get(Integer.valueOf(reqDomesticate.getId()));
            if (pet == null) {
                return;
            }
            short residue = (short)(30 - pet.getDomesticate());
            if (residue <= 0) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg(String.format("本周%s驯养次数已上限", pet.getPetName())));
                return;
            }
            List<Domesticate> list2 = (List)DataCache.DOMESTICATE_MAP.get(role.getId());
            if (list2 == null) {
                list2 = new ArrayList<>();
            }
            if (reqDomesticate.getNum() < 0) {
                residue += Math.abs(reqDomesticate.getNum());
            }
            else {
                if (reqDomesticate.getNum() > residue) {
                    reqDomesticate.setNum((int)residue);
                }
                residue -= reqDomesticate.getNum();
            }
            boolean flag = this.domesticate(role, (long)pet.getId(), String.valueOf(pet.getPetId()), list2, reqDomesticate.getNum(), residue, reqDomesticate.getType(), 75020, 60);
            if (flag) {
                pet.setDomesticate(residue);
                pet.save();
            }
        }
        else if (reqDomesticate.getType() == LOGIC.HORSE.getValue()) {
            Long id = Long.valueOf(reqDomesticate.getId());
            Map<Long, Horse> horseMap = SpringUtils.getHorseService().getHorse(role);
            Horse horse = (Horse)horseMap.get(id);
            if (horse == null) {
                return;
            }
            SysHorse sysHorse = (SysHorse)HorseDataPool.horseMap.get(Integer.valueOf(horse.getSerial()));
            if (sysHorse == null) {
                return;
            }
            String msg = DataGlobalFunc.needGradeJudge(sysHorse.getNeedGrade() / 1000, sysHorse.getNeedGrade() % 1000, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
                return;
            }
            if (horse.getLevel() >= HorseDataPool.horseLevelMax) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg("你的坐骑等级已满级哦!"));
                return;
            }
            short limit = HorseDataPool.getGrowthDanLimit((int)role.getRelive() * 1000 + (int)role.getLevel());
            if (horse.getLevel() >= limit && horse.getExp() >= 1500000000L) {
                MessagePusher.pushMessage((long)role.getId(), new RespMsg("坐骑等级已上限，请先提升你的等级"));
                return;
            }
            List<Domesticate> list = (List)DataCache.DOMESTICATE_MAP.get(role.getId());
            if (list == null) {
                list = new ArrayList<>();
            }
            this.domesticate(role, (long)horse.getId(), reqDomesticate.getId(), list, reqDomesticate.getNum(), (short)(-1), reqDomesticate.getType(), 75043, 15);
        }
    }
    
    private boolean domesticate(Role role, long id, String finalId, List<Domesticate> list, int num, short residue, byte type, int serial, int domesticateTime) {
        Domesticate domesticate = (Domesticate)list.stream().filter(e -> id == e.getRestsId()).findFirst().orElse(null);
        if (num > 0) {
            Integer consume = this.bagService.consumeBagGoods(role, serial, num, true, true, "帮派驯养");
            if (consume != null) {
                return false;
            }
            if (domesticate == null) {
                int time = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
                domesticate = new Domesticate(Long.valueOf(this.worker.nextId()), (long)role.getId(), id, type, (byte)1, 0, time, time, num);
                list.add(domesticate);
                DataCache.DOMESTICATE_MAP.put(role.getId(), list);
            }
            else {
                domesticate.setCount(domesticate.getCount() + num);
            }
            domesticate.save();
            RespDomesticateInfo domesticateInfo2 = new RespDomesticateInfo();
            domesticateInfo2.getList().add(new DomesticateInfo(finalId, domesticate.getCreateTime(), domesticate.getCount(), residue, domesticate.getLogic()));
            MessagePusher.pushMessage((long)role.getId(), domesticateInfo2);
        }
        else {
            if (domesticate == null) {
                return false;
            }
            if (domesticate.getCount() <= 1) {
                return false;
            }
            int reduceCount = Math.abs(num);
            if (reduceCount > domesticate.getCount() - 1) {
                return false;
            }
            int time = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
            int endTime = this.getDomesticateEndTime(domesticate);
            int residueTime = endTime - time;
            int reduceTime = domesticateTime * reduceCount;
            if (residueTime <= domesticateTime || reduceTime > residueTime - domesticateTime) {
                return false;
            }
            domesticate.setCount(domesticate.getCount() - reduceCount);
            domesticate.save();
            RespDomesticateInfo domesticateInfo = new RespDomesticateInfo();
            domesticateInfo.getList().add(new DomesticateInfo(finalId, domesticate.getCreateTime(), domesticate.getCount(), residue, domesticate.getLogic()));
            MessagePusher.pushMessage((long)role.getId(), domesticateInfo);
            this.bagService.addGoods(role, serial, reduceCount, 2, true, true, "帮派驯养");
        }
        return true;
    }
    
    public void loadDomesticate(Role role) {
        List<Domesticate> list = this.dao.query(Domesticate.class, Cnd.where("r_id", "=", role.getId()));
        if (list != null && list.size() > 0) {
            DataCache.DOMESTICATE_MAP.put(role.getId(), list);
        }
    }
}
