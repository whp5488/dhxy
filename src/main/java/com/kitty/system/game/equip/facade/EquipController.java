
package com.kitty.system.game.equip.facade;

import com.kitty.system.game.equip.message.ReqUpgradeEquip;
import com.kitty.system.game.equip.message.ReqPetAcsryLock;
import com.kitty.system.game.equip.message.ReqRepairPetAcsry;
import com.kitty.system.game.equip.message.ReqUnPetEquip;
import com.kitty.system.game.equip.message.ReqPetEquip;
import com.kitty.system.game.equip.message.ReqUnEquip;
import com.kitty.system.game.equip.message.ReqEquip;
import com.kitty.system.game.equip.message.ReqOperateEffect;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.equip.message.ReqEffect;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.equip.service.EquipService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class EquipController
{
    Logger logger;
    @Autowired
    public EquipService equipService;
    
    public EquipController() {
        this.logger = LoggerFactory.getLogger(EquipController.class);
    }
    
    @RequestMapping
    public void getEffect(final IoSession session, final ReqEffect reqEffect) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.getEffect(role);
        }
    }
    
    @RequestMapping
    public void operateEffect(final IoSession session, final ReqOperateEffect reqOperateEffect) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if (reqOperateEffect.getType() == 1) {
                this.equipService.wearEffect(role, reqOperateEffect.getSerial());
            }
            else if (reqOperateEffect.getType() == 0) {
                this.equipService.unEffect(role, reqOperateEffect.getSerial());
            }
        }
    }
    
    @RequestMapping
    public void equip(final IoSession session, final ReqEquip reqEquip) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.wearEquip(role, Long.valueOf(reqEquip.getId()), true);
        }
    }
    
    @RequestMapping
    public void unEquip(final IoSession session, final ReqUnEquip reqUnequip) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.unEquip(role, Long.valueOf(reqUnequip.getId()), true);
        }
    }
    
    @RequestMapping
    public void equip(final IoSession session, final ReqPetEquip reqEquip) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.wearEquip(role, reqEquip);
        }
    }
    
    @RequestMapping
    public void unEquip(final IoSession session, final ReqUnPetEquip reqUnEquip) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.unEquip(role, reqUnEquip);
        }
    }
    
    @RequestMapping
    public void repairPetAcsry(final IoSession session, final ReqRepairPetAcsry repairPetAcsry) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.repairPetAcsry(role, repairPetAcsry);
        }
    }
    
    @RequestMapping
    public void petAcsryLock(final IoSession session, final ReqPetAcsryLock petAcsryLock) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            this.equipService.petAcsryLock(role, petAcsryLock);
        }
    }
    
    @RequestMapping
    public void upgradeEquip(final IoSession session, final ReqUpgradeEquip upgradeEquip) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.equipService.upgradeEquip(role, upgradeEquip.getPos(), upgradeEquip.getType(), upgradeEquip.getParam(), upgradeEquip.getOperationType(), true, "客户端-装备打造处理");
    }
}
