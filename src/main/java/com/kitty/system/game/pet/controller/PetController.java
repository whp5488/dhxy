
package com.kitty.system.game.pet.controller;

import com.kitty.system.game.pet.message.ReqDisassembleYuanqidan;
import com.kitty.system.game.pet.message.ReqPetCutAccColor;
import com.kitty.system.game.pet.message.ReqChangeColorPet;
import com.kitty.system.game.pet.message.ReqPetFlyStarter;
import com.kitty.system.game.pet.message.ReqPetFly;
import com.kitty.system.game.pet.message.ReqRestAttributePet;
import com.kitty.system.game.pet.message.ReqRestAssignCorrection;
import com.kitty.system.game.general.message.ReqAssignCorrection;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.message.ReqSetPetName;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.Map;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.enter.message.RespDestroySucceed;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.pet.message.ReqDropPet;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.pet.message.ReqSelectCurrentPet;
import com.kitty.system.game.pet.message.ReqSetShapeTemp;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.pet.message.RespConcisePet;
import java.util.ArrayList;
import com.kitty.system.game.pet.domain.PetBase;
import java.util.List;
import com.kitty.system.game.pet.message.ReqRequestUpgradeTaskPet;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.message.ReqFeedPet;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.pet.message.MetaPet;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.pet.service.PetService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class PetController
{
    Logger logger;
    @Autowired
    private PetService petService;
    @Autowired
    private BagService bagService;
    
    public PetController() {
        this.logger = LoggerFactory.getLogger(PetController.class);
    }
    
    @RequestMapping
    public void exChangePet(final IoSession session, final MetaPet metaPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.petService.onC2SGetPet(metaPet.getType(), role);
        }
    }
    
    @RequestMapping
    public void feedPet(final IoSession session, final ReqFeedPet feedPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(feedPet.getPetId());
            if (pet != null) {
                this.petService.feedPet(role, pet, feedPet.getParam(), feedPet.getType(), feedPet.getNum(), 3);
            }
        }
    }
    
    @RequestMapping
    public void refinePetBase(final IoSession session, final ReqRequestUpgradeTaskPet requestUpgradeTaskPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(requestUpgradeTaskPet.getPetId());
            if (pet != null) {
                List<PetBase> list = pet.getDarkBaseMap().get(requestUpgradeTaskPet.getParam());
                if (list == null) {
                    list = new ArrayList<PetBase>();
                }
                MessagePusher.pushMessage(session, new RespConcisePet(pet.getPetId(), list));
            }
        }
    }
    
    @RequestMapping
    public void refineReplace(final IoSession session, final ReqSetShapeTemp reqSetShapeTemp) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.petService.refineReplace(role, reqSetShapeTemp);
        }
    }
    
    @RequestMapping
    public void combatPet(final IoSession session, final ReqSelectCurrentPet reqSelectCurrentPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(reqSelectCurrentPet.getPetId());
            if (pet != null) {
                final int petLevel = pet.getPetLv() + pet.getRebirth() * 1000;
                final int roleLevle = role.getLevel() + role.getRelive() * 1000;
                if (petLevel - roleLevle > 15) {
                    return;
                }
                role.getExtendBox().setCombatPet(pet.getPetId());
                this.petService.petInfo(role, pet);
                role.save();
            }
            else {
                MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
            }
        }
    }
    
    @RequestMapping
    public void giveUpPet(final IoSession session, final ReqDropPet reqDropPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if (!SafeUtil.isUnlock(role)) {
                return;
            }
            if (role.getExtendBox().getCombatPet().equals(reqDropPet.getPetId())) {
                MessagePusher.pushMessage(session, new RespMsg("出战中的宠物不可以放生"));
            }
            else {
                final Map<Integer, Pet> map = this.petService.selectPet(role);
                final Pet pet = map.get(reqDropPet.getPetId());
                if (pet != null) {
                    final List<PetEquip> petWearEquip = this.bagService.getPetWearEquip(role.getId(), pet.getId());
                    if (petWearEquip != null && petWearEquip.size() > 0) {
                        MessagePusher.pushMessage(session, new RespMsg(" 请卸下召唤兽佩饰后再试"));
                        return;
                    }
                    final List<Domesticate> domesticateList = DataCache.DOMESTICATE_MAP.get(role.getId());
                    if (domesticateList != null) {
                        final Domesticate domesticate = domesticateList.stream().filter(e -> pet.getId().equals(e.getRestsId())).findFirst().orElse(null);
                        if (domesticate != null) {
                            MessagePusher.pushMessage(session, new RespMsg(String.format("#<O>%s#处于驯养中，不可放生#<E:51>#", pet.getPetName())));
                            return;
                        }
                    }
                    map.remove(reqDropPet.getPetId());
                    RoleUtils.updateAssistList(role, pet.getPetId(), false);
                    MessagePusher.pushMessage(session, new RespDestroySucceed(Const.BUYBACK_TYPE_PET, String.valueOf(reqDropPet.getPetId())));
                    pet.setDelete(true);
                    pet.save();
                    role.save();
                    RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)2, (byte)2, "放生宠物" + JsonUtils.object2String(pet), role.getMarking());
                    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, petWearEquip));
                }
                else {
                    MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
                }
            }
        }
    }
    
    @RequestMapping
    public void changeName(final IoSession session, final ReqSetPetName reqSetPetName) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Map<Integer, Pet> petMap = this.petService.selectPet(role);
            final Pet pet = petMap.get(reqSetPetName.getPetId());
            if (pet != null) {
                if (!SpringUtils.getRoleService().canCreateRole(session, reqSetPetName.getName(), LOGIC.LOGICTYPE_PET.getValue())) {
                    return;
                }
                final String name = CheckCharUtils.replaceName(reqSetPetName.getName());
                pet.setPetName(name);
                petMap.put(pet.getPetId(), pet);
                this.petService.petInfo(role, pet);
                pet.save();
                role.save();
            }
            else {
                MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
            }
        }
    }
    
    @RequestMapping
    public void assignCorrection(final IoSession session, final ReqAssignCorrection reqAssignCorrection) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(reqAssignCorrection.getPetId());
            if (pet != null) {
                this.petService.assignCorrection(role, pet, reqAssignCorrection.getNutMap());
            }
            else {
                MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
            }
        }
    }
    
    @RequestMapping
    public void restAssignCorrection(final IoSession session, final ReqRestAssignCorrection restAssignCorrection) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(restAssignCorrection.getPetId());
            if (pet != null) {
                this.petService.restAssignCorrection(role, pet, restAssignCorrection.getType());
            }
            else {
                MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
            }
        }
    }
    
    @RequestMapping
    public void restAttribute(final IoSession session, final ReqRestAttributePet restAttributePet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final Pet pet = this.petService.selectPet(role).get(restAttributePet.getPetId());
            if (pet != null) {
                this.petService.restAttribute(role, pet, restAttributePet.getType());
            }
            else {
                MessagePusher.pushMessage(session, new RespMsg("未找到该宠物信息"));
            }
        }
    }
    
    @RequestMapping
    public void petFly(final IoSession session, final ReqPetFly reqPetFly) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.petService.petFly(role, reqPetFly);
        }
    }
    
    @RequestMapping
    public void petFly(final IoSession session, final ReqPetFlyStarter reqPetFlyStarter) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.petService.petFlyStarter(role, reqPetFlyStarter);
        }
    }
    
    @RequestMapping
    public void changeColor(final IoSession session, final ReqChangeColorPet reqChangeColorPet) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final int petId = reqChangeColorPet.getPetId();
        final byte colorSerial = reqChangeColorPet.getColorSerial();
        if (petId <= 0) {
            return;
        }
        if (colorSerial < 0) {
            return;
        }
        this.petService.changeColor(role, petId, colorSerial);
    }
    
    @RequestMapping
    public void cutAccColor(final IoSession session, final ReqPetCutAccColor reqPetCutAccColor) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.petService.cutAccColor(role, reqPetCutAccColor);
    }
    
    @RequestMapping
    public void disassembleYuanqidan(final IoSession session, final ReqDisassembleYuanqidan reqDisassembleYuanqidan) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final Integer serial = reqDisassembleYuanqidan.getSerial();
        if (serial == null) {
            return;
        }
        this.petService.disassembleYuanqidan(role, serial, reqDisassembleYuanqidan.getNum());
    }
}
