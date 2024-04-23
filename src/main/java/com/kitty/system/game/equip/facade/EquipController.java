package com.kitty.system.game.equip.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.equip.message.ReqEffect;
import com.kitty.system.game.equip.message.ReqEquip;
import com.kitty.system.game.equip.message.ReqOperateEffect;
import com.kitty.system.game.equip.message.ReqPetAcsryLock;
import com.kitty.system.game.equip.message.ReqPetEquip;
import com.kitty.system.game.equip.message.ReqRepairPetAcsry;
import com.kitty.system.game.equip.message.ReqUnEquip;
import com.kitty.system.game.equip.message.ReqUnPetEquip;
import com.kitty.system.game.equip.message.ReqUpgradeEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EquipController {
  Logger logger = LoggerFactory.getLogger(EquipController.class);
  
  @Autowired
  public EquipService equipService;
  
  @RequestMapping
  public void getEffect(IoSession session, ReqEffect reqEffect) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.getEffect(role); 
  }
  
  @RequestMapping
  public void operateEffect(IoSession session, ReqOperateEffect reqOperateEffect) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      if (reqOperateEffect.getType() == 1) {
        this.equipService.wearEffect(role, reqOperateEffect.getSerial());
      } else if (reqOperateEffect.getType() == 0) {
        this.equipService.unEffect(role, reqOperateEffect.getSerial());
      }  
  }
  
  @RequestMapping
  public void equip(IoSession session, ReqEquip reqEquip) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.wearEquip(role, Long.valueOf(reqEquip.getId()), true); 
  }
  
  @RequestMapping
  public void unEquip(IoSession session, ReqUnEquip reqUnequip) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.unEquip(role, Long.valueOf(reqUnequip.getId()).longValue(), true); 
  }
  
  @RequestMapping
  public void equip(IoSession session, ReqPetEquip reqEquip) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.wearEquip(role, reqEquip); 
  }
  
  @RequestMapping
  public void unEquip(IoSession session, ReqUnPetEquip reqUnEquip) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.unEquip(role, reqUnEquip); 
  }
  
  @RequestMapping
  public void repairPetAcsry(IoSession session, ReqRepairPetAcsry repairPetAcsry) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.repairPetAcsry(role, repairPetAcsry); 
  }
  
  @RequestMapping
  public void petAcsryLock(IoSession session, ReqPetAcsryLock petAcsryLock) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      this.equipService.petAcsryLock(role, petAcsryLock); 
  }
  
  @RequestMapping
  public void upgradeEquip(IoSession session, ReqUpgradeEquip upgradeEquip) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.equipService.upgradeEquip(role, upgradeEquip.getPos(), upgradeEquip.getType(), upgradeEquip.getParam(), upgradeEquip.getOperationType(), true, "客户端-装备打造处理");
  }
}
