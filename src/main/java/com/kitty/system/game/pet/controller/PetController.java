package com.kitty.system.game.pet.controller;

import com.kitty.common.utils.CheckCharUtils;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.message.RespDestroySucceed;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.general.message.ReqAssignCorrection;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.message.MetaPet;
import com.kitty.system.game.pet.message.ReqChangeColorPet;
import com.kitty.system.game.pet.message.ReqDisassembleYuanqidan;
import com.kitty.system.game.pet.message.ReqDropPet;
import com.kitty.system.game.pet.message.ReqFeedPet;
import com.kitty.system.game.pet.message.ReqPetCutAccColor;
import com.kitty.system.game.pet.message.ReqPetFly;
import com.kitty.system.game.pet.message.ReqPetFlyStarter;
import com.kitty.system.game.pet.message.ReqRequestUpgradeTaskPet;
import com.kitty.system.game.pet.message.ReqRestAssignCorrection;
import com.kitty.system.game.pet.message.ReqRestAttributePet;
import com.kitty.system.game.pet.message.ReqSelectCurrentPet;
import com.kitty.system.game.pet.message.ReqSetPetName;
import com.kitty.system.game.pet.message.ReqSetShapeTemp;
import com.kitty.system.game.pet.message.RespConcisePet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PetController {
  Logger logger = LoggerFactory.getLogger(PetController.class);
  
  @Autowired
  private PetService petService;
  
  @Autowired
  private BagService bagService;
  
  @RequestMapping
  public void exChangePet(IoSession session, MetaPet metaPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.petService.onC2SGetPet(metaPet.getType(), role); 
  }
  
  @RequestMapping
  public void feedPet(IoSession session, ReqFeedPet feedPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(feedPet.getPetId()));
      if (pet != null)
        this.petService.feedPet(role, pet, feedPet.getParam(), feedPet.getType(), feedPet.getNum(), new Object[] { Integer.valueOf(3) }); 
    } 
  }
  
  @RequestMapping
  public void refinePetBase(IoSession session, ReqRequestUpgradeTaskPet requestUpgradeTaskPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(requestUpgradeTaskPet.getPetId()));
      if (pet != null) {
        List<PetBase> list = (List<PetBase>)pet.getDarkBaseMap().get(Integer.valueOf(requestUpgradeTaskPet.getParam()));
        if (list == null)
          list = new ArrayList<>(); 
        MessagePusher.pushMessage(session, (Message)new RespConcisePet(pet.getPetId().intValue(), list));
      } 
    } 
  }
  
  @RequestMapping
  public void refineReplace(IoSession session, ReqSetShapeTemp reqSetShapeTemp) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.petService.refineReplace(role, reqSetShapeTemp); 
  }
  
  @RequestMapping
  public void combatPet(IoSession session, ReqSelectCurrentPet reqSelectCurrentPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(reqSelectCurrentPet.getPetId()));
      if (pet != null) {
        int petLevel = pet.getPetLv().intValue() + pet.getRebirth().intValue() * 1000;
        int roleLevle = role.getLevel().intValue() + role.getRelive().intValue() * 1000;
        if (petLevel - roleLevle > 15)
          return; 
        role.getExtendBox().setCombatPet(pet.getPetId());
        this.petService.petInfo(role, pet);
        role.save();
      } else {
        MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
      } 
    } 
  }
  
  @RequestMapping
  public void giveUpPet(IoSession session, ReqDropPet reqDropPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      if (!SafeUtil.isUnlock(role))
        return; 
      if (role.getExtendBox().getCombatPet().equals(Integer.valueOf(reqDropPet.getPetId()))) {
        MessagePusher.pushMessage(session, (Message)new RespMsg("出战中的宠物不可以放生"));
      } else {
        Map<Integer, Pet> map = this.petService.selectPet(role);
        Pet pet = map.get(Integer.valueOf(reqDropPet.getPetId()));
        if (pet != null) {
          List<PetEquip> petWearEquip = this.bagService.getPetWearEquip(role.getId().longValue(), pet.getId().longValue());
          if (petWearEquip != null && petWearEquip.size() > 0) {
            MessagePusher.pushMessage(session, (Message)new RespMsg(" 请卸下召唤兽佩饰后再试"));
            return;
          } 
          List<Domesticate> domesticateList = (List<Domesticate>)DataCache.DOMESTICATE_MAP.get(role.getId());
          if (domesticateList != null) {
            Domesticate domesticate = domesticateList.stream().filter(e -> pet.getId().equals(Long.valueOf(e.getRestsId()))).findFirst().orElse(null);
            if (domesticate != null) {
              MessagePusher.pushMessage(session, (Message)new RespMsg(String.format("#<O>%s#处于驯养中，不可放生#<E:51>#", new Object[] { pet.getPetName() })));
              return;
            } 
          } 
          map.remove(Integer.valueOf(reqDropPet.getPetId()));
          RoleUtils.updateAssistList(role, pet.getPetId(), false);
          MessagePusher.pushMessage(session, (Message)new RespDestroySucceed(Const.BUYBACK_TYPE_PET, String.valueOf(reqDropPet.getPetId())));
          pet.setDelete(true);
          pet.save();
          role.save();
          RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)2, (byte)2, "放生宠物" + JsonUtils.object2String(pet), role.getMarking());
          EventDispatcher.getInstance().fireEvent((BaseGameEvent)new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, petWearEquip));
        } else {
          MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
        } 
      } 
    } 
  }
  
  @RequestMapping
  public void changeName(IoSession session, ReqSetPetName reqSetPetName) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Map<Integer, Pet> petMap = this.petService.selectPet(role);
      Pet pet = petMap.get(reqSetPetName.getPetId());
      if (pet != null) {
        if (!SpringUtils.getRoleService().canCreateRole(session, reqSetPetName.getName(), LOGIC.LOGICTYPE_PET.getValue()))
          return; 
        String name = CheckCharUtils.replaceName(reqSetPetName.getName());
        pet.setPetName(name);
        petMap.put(pet.getPetId(), pet);
        this.petService.petInfo(role, pet);
        pet.save();
        role.save();
      } else {
        MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
      } 
    } 
  }
  
  @RequestMapping
  public void assignCorrection(IoSession session, ReqAssignCorrection reqAssignCorrection) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(reqAssignCorrection.getPetId());
      if (pet != null) {
        this.petService.assignCorrection(role, pet, reqAssignCorrection.getNutMap());
      } else {
        MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
      } 
    } 
  }
  
  @RequestMapping
  public void restAssignCorrection(IoSession session, ReqRestAssignCorrection restAssignCorrection) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(restAssignCorrection.getPetId());
      if (pet != null) {
        this.petService.restAssignCorrection(role, pet, restAssignCorrection.getType());
      } else {
        MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
      } 
    } 
  }
  
  @RequestMapping
  public void restAttribute(IoSession session, ReqRestAttributePet restAttributePet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      Pet pet = (Pet)this.petService.selectPet(role).get(restAttributePet.getPetId());
      if (pet != null) {
        this.petService.restAttribute(role, pet, restAttributePet.getType());
      } else {
        MessagePusher.pushMessage(session, (Message)new RespMsg("未找到该宠物信息"));
      } 
    } 
  }
  
  @RequestMapping
  public void petFly(IoSession session, ReqPetFly reqPetFly) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.petService.petFly(role, reqPetFly); 
  }
  
  @RequestMapping
  public void petFly(IoSession session, ReqPetFlyStarter reqPetFlyStarter) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.petService.petFlyStarter(role, reqPetFlyStarter); 
  }
  
  @RequestMapping
  public void changeColor(IoSession session, ReqChangeColorPet reqChangeColorPet) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    int petId = reqChangeColorPet.getPetId();
    byte colorSerial = reqChangeColorPet.getColorSerial();
    if (petId <= 0)
      return; 
    if (colorSerial < 0)
      return; 
    this.petService.changeColor(role, petId, colorSerial);
  }
  
  @RequestMapping
  public void cutAccColor(IoSession session, ReqPetCutAccColor reqPetCutAccColor) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.petService.cutAccColor(role, reqPetCutAccColor);
  }
  
  @RequestMapping
  public void disassembleYuanqidan(IoSession session, ReqDisassembleYuanqidan reqDisassembleYuanqidan) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Integer serial = reqDisassembleYuanqidan.getSerial();
    if (serial == null)
      return; 
    this.petService.disassembleYuanqidan(role, serial, reqDisassembleYuanqidan.getNum());
  }
}
