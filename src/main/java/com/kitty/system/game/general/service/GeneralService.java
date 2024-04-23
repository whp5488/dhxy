package com.kitty.system.game.general.service;

import com.kitty.cache.RedisService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.confirm.ConfirmType;
import com.kitty.system.enums.task.TaskType;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.activity.handler.timing.hg.HgFightHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsPkHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.general.message.ReqDomesticate;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.system.game.general.message.RespAutoAttrib;
import com.kitty.system.game.general.message.RespDomesticateInfo;
import com.kitty.system.game.general.message.RespRectifyTime;
import com.kitty.system.game.general.message.vo.DomesticateInfo;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.house.service.HouseService;
import com.kitty.system.game.onlinemall.service.MallService;
import com.kitty.system.game.partner.service.PartnerService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.plug.service.PlugService;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.safe.service.SafeService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.suit.service.SuitService;
import com.kitty.system.game.task.message.RespLivenessInfo;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.task.service.handler.dungeon.DungeonMwkFront;
import com.kitty.system.game.task.service.handler.dungeon.DungeonXmHandler;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.welfare.service.WelfareService;
import com.kitty.system.init.domain.SysHorse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralService extends EchoHandler {
  Logger logger = LoggerFactory.getLogger(GeneralService.class);
  
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
  
  public void generalNotify(Role role, ReqGeneralNotify notify) {
    RespLivenessInfo respLivenessInfo;
    Team team;
    int generalType = notify.getType() & 0xFFFF;
    switch (generalType) {
      case 0:
        MessagePusher.pushMessage(role, (Message)new RespRectifyTime(notify.getStatus()));
        return;
      case 1:
        this.roleService.modifyName(role, notify.getString());
        return;
      case 2:
        ((TaskService)SpringUtils.getBean(TaskService.class)).requestGiveUpTask(role, Integer.valueOf(notify.getStatus()).intValue());
        return;
      case 3:
        this.roleService.updateDrill(role);
        return;
      case 4:
        if (NumberUtil.isNumber(notify.getStatus()))
          this.petService.firstClaimPet(role, Integer.valueOf(notify.getStatus())); 
        return;
      case 7:
        if (NumberUtil.isNumber(notify.getStatus())) {
          Integer integer = Integer.valueOf(notify.getStatus());
          if (integer.equals(Integer.valueOf(TaskType.MWK_FRONT.getId())))
            ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).leave(role); 
          if (integer.equals(Integer.valueOf(TaskType.XM.getId())))
            ((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).leave(role); 
          if (integer.equals(Integer.valueOf(3003)))
            ((WaterHandler)SpringUtils.getBean(WaterHandler.class)).leave(role); 
        } 
        return;
      case 6:
        if (NumberUtil.isNumber(notify.getStatus())) {
          Integer integer = Integer.valueOf(notify.getStatus());
          if (integer.equals(Integer.valueOf(TaskType.XM.getId())))
            ((DungeonXmHandler)SpringUtils.getBean(DungeonXmHandler.class)).viewDugeonAward(role); 
        } 
        return;
      case 9:
        if (NumberUtil.isNumber(notify.getStatus())) {
          Integer id = Integer.valueOf(notify.getStatus());
          ((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).initiator(role, id.intValue(), notify.getString());
        } 
        return;
      case 16:
        ((HouseService)SpringUtils.getBean(HouseService.class)).openFurnitureStore(role);
        return;
      case 18:
        if (NumberUtil.isNumber(notify.getStatus()))
          ((HouseService)SpringUtils.getBean(HouseService.class)).switchoverMotif(role, Long.valueOf(notify.getStatus()).longValue()); 
        return;
      case 19:
        ((HouseService)SpringUtils.getBean(HouseService.class)).house(role);
        return;
      case 8:
        getDomesticateInfo(role);
        return;
      case 20:
        ((PlugService)SpringUtils.getBean(PlugService.class)).getPlugEndTime(role);
        return;
      case 26:
        recommend_attrib(role, notify);
        return;
      case 24:
        ((PartnerService)SpringUtils.getBean(PartnerService.class)).relive(role, Integer.valueOf(notify.getString()).intValue());
        return;
      case 25:
        ((PetService)SpringUtils.getBean(PetService.class)).relive(role, Integer.valueOf(notify.getString()));
        return;
      case 31:
        this.roleService.reliveCorrection(role, notify.getStatus());
        return;
      case 32:
        respLivenessInfo = SpringUtils.getActivityService().getRespLivenessInfo(role);
        MessagePusher.pushMessage(role, (Message)respLivenessInfo);
        return;
      case 33:
        if (NumberUtil.isNumber(notify.getStatus()))
          SpringUtils.getActivityService().fetchLivenessReward(role, Integer.parseInt(notify.getStatus())); 
        return;
      case 34:
        if (NumberUtil.isNumber(notify.getStatus()))
          ((HgFightHandler)SpringUtils.getBean(HgFightHandler.class)).accept(role, Byte.valueOf(notify.getStatus()).byteValue()); 
        return;
      case 35:
        this.roleService.setElseFight(role);
        return;
      case 36:
        if (role.getExtendBox().getStar() >= 8)
          ((SuitService)SpringUtils.getBean(SuitService.class)).addSuit(role); 
        return;
      case 37:
        if (role.getExtendBox().getStar() >= 8 && NumberUtil.isNumber(notify.getStatus()))
          ((SuitService)SpringUtils.getBean(SuitService.class)).updateSuitName(role, Long.valueOf(notify.getStatus()).longValue(), notify.getString()); 
        return;
      case 38:
        this.cardService.reqFiveExercise(role, true);
        return;
      case 40:
        this.cardService.upgradeFiveGrade(role);
        return;
      case 41:
        SpringUtils.getPartyService().upgrade(role);
        return;
      case 42:
        ((MallService)SpringUtils.getBean(MallService.class)).openTreasure(role);
        return;
      case 43:
        ((MallService)SpringUtils.getBean(MallService.class)).buyTreasure(role);
        return;
      case 44:
        ((MallService)SpringUtils.getBean(MallService.class)).openTreasureAward(role);
        return;
      case 46:
        if (NumberUtil.isNumber(notify.getStatus()))
          ((MallService)SpringUtils.getBean(MallService.class)).openTreasure(role, Long.valueOf(notify.getStatus())); 
        return;
      case 45:
        if (NumberUtil.isNumber(notify.getStatus()))
          SpringUtils.getMarketService().reqPrice(role, Integer.valueOf(notify.getStatus()).intValue()); 
        return;
      case 10001:
        if (NumberUtil.isNumber(notify.getString()))
          this.bagService.itemOperate(role, new ReqOperateItem(Integer.valueOf(notify.getString()).intValue(), 10001, 1, notify.getStatus())); 
        return;
      case 10002:
        if (NumberUtil.isNumber(notify.getStatus()))
          ((SafeService)SpringUtils.getBean(SafeService.class)).reqReset(role, Byte.valueOf(notify.getStatus()).byteValue()); 
        return;
      case 10003:
        ((SafeService)SpringUtils.getBean(SafeService.class)).cancelReset(role);
        return;
      case 20025:
        this.bagService.getTemporary(role, notify.getStatus());
        return;
      case 30011:
        this.redisService.del(new Serializable[] { role.getRoleId() + "invite_join" });
        return;
      case 30012:
        this.redisService.del(new Serializable[] { role.getRoleId() + "request_join" });
        return;
      case 30015:
        this.welfareService.recharge(role, notify);
        return;
      case 30030:
        team = SpringUtils.getTeamService().getTeam(role.getRoleId().intValue());
        if (team != null) {
          Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
          if (leaderRole != null)
            if (leaderRole.getConfirm().getConfirmType() == ConfirmType.CREATE_DUNGEON_MWK_F)
              ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).createBurgeonBallotAgree(role);  
        } 
        return;
      case 30031:
        team = SpringUtils.getTeamService().getTeam(role.getRoleId().intValue());
        if (team != null) {
          Role leaderRole = SpringUtils.getRoleService().getPlayer(team.getLeaderUid());
          if (leaderRole != null)
            if (leaderRole.getConfirm().getConfirmType() == ConfirmType.CREATE_DUNGEON_MWK_F)
              ((DungeonMwkFront)SpringUtils.getBean(DungeonMwkFront.class)).createBurgeonBallotDisagree(role);  
        } 
        return;
      case 30043:
        ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).eventInfo(role);
        return;
      case 30044:
        if (NumberUtil.isNumber(notify.getStatus())) {
          Integer type = Integer.valueOf(notify.getStatus());
          ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).reqOperateEquipment(role, type.intValue(), notify.getString());
        } 
        return;
      case 30045:
        if (NumberUtil.isNumber(notify.getString()))
          ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).requestFight(role, Integer.valueOf(notify.getString()).intValue()); 
        return;
      case 30046:
        ((GangWarsPkHandler)SpringUtils.getBean(GangWarsPkHandler.class)).getNearby(role);
        return;
      case 30032:
        if (SafeUtil.isUnlock(role))
          this.bagService.removeItem(role, Integer.valueOf(notify.getString()).intValue(), Integer.valueOf(notify.getStatus()).intValue(), "客户端请求删除"); 
        return;
      case 40013:
        SpringUtils.getMarketService().focus(role, notify);
        return;
      case 40014:
        SpringUtils.getMarketService().columbia(role, notify);
        return;
      case 40015:
        SpringUtils.getMarketService().sendMarketList(role);
        return;
      case 40016:
        SpringUtils.getMarketService().removeGoods(role, notify, true);
        return;
      case 40017:
        SpringUtils.getMarketService().restartStall(role, notify);
        return;
      case 40018:
        SpringUtils.getMarketService().marketQuery(notify, role);
        return;
      case 40021:
        SpringUtils.getMarketService().takeCash(role, notify);
        return;
      case 40313:
        this.welfareService.openNewBitGift(role);
        return;
      case 40314:
        if (NumberUtil.isNumber(notify.getStatus()))
          this.welfareService.fetchNewBieGift(role, Integer.valueOf(notify.getStatus()).intValue()); 
        return;
      case 20018:
        this.welfareService.openRechargeGift(role);
        return;
      case 20019:
        if (NumberUtil.isNumber(notify.getStatus()))
          this.welfareService.fetchRechargeGift(role, Integer.valueOf(notify.getStatus()).intValue()); 
        return;
      case 20020:
        this.welfareService.openFirstRechargeGift(role);
        return;
      case 20021:
        this.welfareService.fetchFirstRechargeGift(role);
        return;
      case 20022:
        this.welfareService.openNewDistrictSign(role, 0);
        return;
      case 20023:
        this.welfareService.fetchNewDistrictSign(role);
        return;
      case 20024:
        this.welfareService.openDayRechargeGift(role);
        return;
      case 20026:
        if (NumberUtil.isNumber(notify.getStatus()))
          this.welfareService.fetchDayRechargeGift(role, Integer.valueOf(notify.getStatus()).intValue()); 
        return;
      case 20027:
        this.welfareService.fetchGiftBag(role, notify.getStatus());
        return;
      case 20017:
        this.welfareService.openPrizeDraw(role);
        return;
    } 
    this.logger.error("未处理NOTIFY功能==CODE:{}", Integer.valueOf(generalType));
  }
  
  public void recommend_attrib(Role role, ReqGeneralNotify notify) {
    RespAutoAttrib autoAttrib = new RespAutoAttrib();
    int id = Integer.valueOf(notify.getStatus()).intValue();
    Map<String, Integer> map = new HashMap<>();
    if (role.getRoleId().intValue() == id) {
      autoAttrib.setId(role.getRoleId().intValue());
      map = role.getExtendBox().getAutoAttrib();
    } else {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(id));
      if (pet != null) {
        autoAttrib.setId(pet.getPetId().intValue());
        map = pet.getAutoAttrib();
      } 
    } 
    if (map.size() > 0) {
      autoAttrib.setGg(((Integer)map.get("gg")).intValue());
      autoAttrib.setLx(((Integer)map.get("lx")).intValue());
      autoAttrib.setLl(((Integer)map.get("ll")).intValue());
      autoAttrib.setMj(((Integer)map.get("mj")).intValue());
      autoAttrib.setType(((Integer)map.get("type")).intValue());
    } 
    MessagePusher.pushMessage(role, (Message)autoAttrib);
  }
  
  public void getDomesticateInfo(Role role) {
    RespDomesticateInfo domesticateInfo = new RespDomesticateInfo();
    List<Domesticate> list = (List<Domesticate>)DataCache.DOMESTICATE_MAP.get(role.getId());
    if (list != null) {
      Map<Integer, Pet> petMap = this.petService.selectPet(role);
      Map<Long, Pet> collect = (Map<Long, Pet>)petMap.values().stream().collect(Collectors.toMap(Pet::getId, pet -> pet));
      for (Domesticate domesticate : list) {
        if (domesticate.getLogic() == LOGIC.HORSE.getValue()) {
          domesticateInfo.getList().add(new DomesticateInfo(String.valueOf(domesticate.getRestsId()), domesticate.getCreateTime(), domesticate.getCount(), (short)-1, domesticate.getLogic()));
          continue;
        } 
        Pet pet = collect.get(Long.valueOf(domesticate.getRestsId()));
        if (pet != null) {
          short domesticateCount = (short)(30 - pet.getDomesticate());
          domesticateInfo.getList().add(new DomesticateInfo(String.valueOf(pet.getPetId()), domesticate.getCreateTime(), domesticate.getCount(), (domesticateCount <= 0) ? 0 : domesticateCount, domesticate.getLogic()));
        } 
      } 
    } 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)domesticateInfo);
  }
  
  public void handleEcho(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role == null)
      return; 
    List<Domesticate> list = (List<Domesticate>)DataCache.DOMESTICATE_MAP.get(role.getId());
    if (list == null)
      return; 
    List<Long> clear = new ArrayList<>();
    int currTime = (new Long(TimeUtil.getTimeMills() / 1000L)).intValue();
    for (int i = 0; i < list.size(); i++) {
      Domesticate domesticate = list.get(i);
      if (!domesticate.isDelete()) {
        int domesticateTime = 60;
        if (domesticate.getLogic() == LOGIC.HORSE.getValue())
          domesticateTime = 15; 
        int domesticateEndTime = getDomesticateEndTime(domesticate);
        int remainingTime = domesticateEndTime - currTime;
        int sumCount = domesticateTime / 3 * domesticate.getCount();
        if (remainingTime < 0 && sumCount > 1) {
          int notCount = sumCount - domesticate.getAlready();
          domesticateEarnings(role, domesticate.getType(), domesticate.getLogic(), domesticate.getRestsId(), notCount, false);
          domesticate.setDelete(true);
          domesticate.save();
          clear.add(domesticate.getId());
        } else {
          double time = (currTime - domesticate.getPrevTime()) * 1.0D / 60.0D;
          if (time >= 3.0D) {
            int num = (int)time / 3;
            domesticateEarnings(role, domesticate.getType(), domesticate.getLogic(), domesticate.getRestsId(), num, true);
            domesticate.setAlready(domesticate.getAlready() + num);
            domesticate.setPrevTime(currTime - (int)(time % 3.0D * 60.0D));
            if (domesticate.getAlready() >= sumCount) {
              clear.add(domesticate.getId());
              domesticate.setDelete(true);
            } 
            domesticate.save();
          } 
        } 
      } 
    } 
    for (Long id : clear)
      list.removeIf(e -> e.getId().equals(id)); 
    if (list.size() <= 0)
      DataCache.DOMESTICATE_MAP.remove(role.getId()); 
  }
  
  public void domesticateEarnings(Role role, byte type, int logic, long id, int num, boolean isTips) {
    if (logic == LOGIC.LOGICTYPE_PET.getValue()) {
      if (type == 1) {
        int tips = 0;
        if (isTips)
          tips = 500; 
        this.petService.addPetClose(role, id, 500L * num, 702, tips);
      } 
    } else if (logic == LOGIC.HORSE.getValue()) {
      if (type == 1) {
        int tips = 0;
        if (isTips)
          tips = 3200; 
        this.horseService.domesticateEarnings(role, id, 3200L * num, 703, tips);
      } 
    } 
  }
  
  public int getDomesticateEndTime(Domesticate domesticate) {
    int time = 0;
    if (domesticate.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
      if (domesticate.getType() == 1)
        time = domesticate.getCreateTime() + 3600 * domesticate.getCount(); 
    } else if (domesticate.getLogic() == LOGIC.HORSE.getValue()) {
      if (domesticate.getType() == 1)
        time = domesticate.getCreateTime() + 900 * domesticate.getCount(); 
    } 
    return time;
  }
  
  public void domesticate(Role role, ReqDomesticate reqDomesticate) {
    if (reqDomesticate.getType() == LOGIC.LOGICTYPE_PET.getValue()) {
      Map<Integer, Pet> petMap = this.petService.selectPet(role);
      Pet pet = petMap.get(Integer.valueOf(reqDomesticate.getId()));
      if (pet == null)
        return; 
      short residue = (short)(30 - pet.getDomesticate());
      if (residue <= 0) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(String.format("本周%s驯养次数已上限", new Object[] { pet.getPetName() })));
        return;
      } 
      List<Domesticate> list = (List<Domesticate>)DataCache.DOMESTICATE_MAP.get(role.getId());
      if (list == null)
        list = new ArrayList<>(); 
      if (reqDomesticate.getNum() < 0) {
        residue = (short)(residue + Math.abs(reqDomesticate.getNum()));
      } else {
        if (reqDomesticate.getNum() > residue)
          reqDomesticate.setNum(residue); 
        residue = (short)(residue - reqDomesticate.getNum());
      } 
      boolean flag = domesticate(role, pet.getId().longValue(), String.valueOf(pet.getPetId()), list, reqDomesticate.getNum(), residue, reqDomesticate.getType(), 75020, 60);
      if (flag) {
        pet.setDomesticate(residue);
        pet.save();
      } 
    } else if (reqDomesticate.getType() == LOGIC.HORSE.getValue()) {
      Long id = Long.valueOf(reqDomesticate.getId());
      Map<Long, Horse> horseMap = SpringUtils.getHorseService().getHorse(role);
      Horse horse = horseMap.get(id);
      if (horse == null)
        return; 
      SysHorse sysHorse = (SysHorse)HorseDataPool.horseMap.get(Integer.valueOf(horse.getSerial()));
      if (sysHorse == null)
        return; 
      String msg = DataGlobalFunc.needGradeJudge(sysHorse.getNeedGrade() / 1000, sysHorse.getNeedGrade() % 1000, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
      if (msg != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
        return;
      } 
      if (horse.getLevel() >= HorseDataPool.horseLevelMax) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你的坐骑等级已满级哦!"));
        return;
      } 
      short limit = HorseDataPool.getGrowthDanLimit(role.getRelive().intValue() * 1000 + role.getLevel().intValue());
      if (horse.getLevel() >= limit && horse.getExp() >= 1500000000L) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("坐骑等级已上限，请先提升你的等级"));
        return;
      } 
      List<Domesticate> list = (List<Domesticate>)DataCache.DOMESTICATE_MAP.get(role.getId());
      if (list == null)
        list = new ArrayList<>(); 
      domesticate(role, horse.getId().longValue(), reqDomesticate.getId(), list, reqDomesticate.getNum(), (short)-1, reqDomesticate.getType(), 75043, 15);
    } 
  }
  
  private boolean domesticate(Role role, long id, String finalId, List<Domesticate> list, int num, short residue, byte type, int serial, int domesticateTime) {
    Domesticate domesticate = list.stream().filter(e -> (id == e.getRestsId())).findFirst().orElse(null);
    if (num > 0) {
      Integer consume = this.bagService.consumeBagGoods(role, serial, num, true, true, "帮派驯养");
      if (consume != null)
        return false; 
      if (domesticate == null) {
        int time = (new Long(TimeUtil.getTimeMills() / 1000L)).intValue();
        domesticate = new Domesticate(Long.valueOf(this.worker.nextId()), role.getId().longValue(), id, type, (byte)1, 0, time, time, num);
        list.add(domesticate);
        DataCache.DOMESTICATE_MAP.put(role.getId(), list);
      } else {
        domesticate.setCount(domesticate.getCount() + num);
      } 
      domesticate.save();
      RespDomesticateInfo domesticateInfo = new RespDomesticateInfo();
      domesticateInfo.getList().add(new DomesticateInfo(finalId, domesticate.getCreateTime(), domesticate.getCount(), residue, domesticate.getLogic()));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)domesticateInfo);
    } else {
      if (domesticate == null)
        return false; 
      if (domesticate.getCount() <= 1)
        return false; 
      int reduceCount = Math.abs(num);
      if (reduceCount > domesticate.getCount() - 1)
        return false; 
      int time = (new Long(TimeUtil.getTimeMills() / 1000L)).intValue();
      int endTime = getDomesticateEndTime(domesticate);
      int residueTime = endTime - time;
      int reduceTime = domesticateTime * reduceCount;
      if (residueTime <= domesticateTime || reduceTime > residueTime - domesticateTime)
        return false; 
      domesticate.setCount(domesticate.getCount() - reduceCount);
      domesticate.save();
      RespDomesticateInfo domesticateInfo = new RespDomesticateInfo();
      domesticateInfo.getList().add(new DomesticateInfo(finalId, domesticate.getCreateTime(), domesticate.getCount(), residue, domesticate.getLogic()));
      MessagePusher.pushMessage(role.getId().longValue(), (Message)domesticateInfo);
      this.bagService.addGoods(role, serial, reduceCount, 2, true, true, "帮派驯养");
    } 
    return true;
  }
  
  public void loadDomesticate(Role role) {
    List<Domesticate> list = this.dao.query(Domesticate.class, (Condition)Cnd.where("r_id", "=", role.getId()));
    if (list != null && list.size() > 0)
      DataCache.DOMESTICATE_MAP.put(role.getId(), list); 
  }
}
