package com.kitty.system.game.partner.controller;

import com.kitty.system.game.partner.message.ReqChangeDefaultSkill;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.mina.cache.DataCache;
import java.util.Map;
import com.kitty.system.game.partner.message.ReqFeedPartner;
import com.kitty.system.game.partner.message.ReqPartnerList;
import com.kitty.system.game.partner.message.ReqSelectCurrentPartner;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.partner.message.ReqPartnerRecruits;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.partner.service.PartnerService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class PartnerController
{
    Logger logger;
    @Autowired
    PartnerService partnerService;
    
    public PartnerController() {
        this.logger = LoggerFactory.getLogger(PartnerController.class);
    }
    
    @RequestMapping
    public void recruitsPartner(IoSession session, ReqPartnerRecruits reqPartnerRecruits) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null && reqPartnerRecruits.getPartnerId() > 0) {
            this.partnerService.addPartner(role, Integer.valueOf(reqPartnerRecruits.getPartnerId()));
        }
    }
    
    @RequestMapping
    public void combatPartner(IoSession session, ReqSelectCurrentPartner reqSelectCurrentPartner) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null && reqSelectCurrentPartner != null) {
            this.partnerService.combatPartner(role, reqSelectCurrentPartner);
        }
    }
    
    @RequestMapping
    public void selectPartnerList(IoSession session, ReqPartnerList reqPartnerList) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if (reqPartnerList.getIsFight() == 1) {
                this.partnerService.selectFightPartnerList(role);
            }
            else {
                this.partnerService.selectPartnerList(role);
            }
        }
    }
    
    @RequestMapping
    public void feedPartner(IoSession session, ReqFeedPartner reqFeedPartner) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            String type = reqFeedPartner.getType();
            if (type == null) {
                return;
            }
            Integer partnerId = Integer.valueOf(reqFeedPartner.getPartnerId());
            if (partnerId == null) {
                return;
            }
            Map<Integer, Partner> partnerMap = (Map)DataCache.PARTNER_MAP.get(role.getId());
            if (partnerMap == null || partnerMap.size() <= 0) {
                return;
            }
            Partner partner = (Partner)partnerMap.get(partnerId);
            if (partner == null) {
                return;
            }
            Integer param = Integer.valueOf(reqFeedPartner.getParam());
            if (param == null) {
                return;
            }
            this.partnerService.feedPartner(role, partner, type, (int)param);
        }
    }
    
    @RequestMapping
    public void changeDefaultSkill(IoSession session, ReqChangeDefaultSkill reqChangeDefaultSkill) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            int pId = reqChangeDefaultSkill.getPId();
            int skillId = reqChangeDefaultSkill.getSkillId();
            if (pId < 0 || skillId < 0) {
                return;
            }
            this.partnerService.changeDefaultSkill(role, pId, skillId);
        }
    }
}
