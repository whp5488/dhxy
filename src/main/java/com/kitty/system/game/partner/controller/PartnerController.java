
package com.kitty.system.game.partner.controller;

import com.kitty.system.game.partner.message.ReqChangeDefaultSkill;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.partner.domain.Partner;
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
    public void recruitsPartner(final IoSession session, final ReqPartnerRecruits reqPartnerRecruits) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null && reqPartnerRecruits.getPartnerId() > 0) {
            this.partnerService.addPartner(role, reqPartnerRecruits.getPartnerId());
        }
    }
    
    @RequestMapping
    public void combatPartner(final IoSession session, final ReqSelectCurrentPartner reqSelectCurrentPartner) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null && reqSelectCurrentPartner != null) {
            this.partnerService.combatPartner(role, reqSelectCurrentPartner);
        }
    }
    
    @RequestMapping
    public void selectPartnerList(final IoSession session, final ReqPartnerList reqPartnerList) {
        final Role role = SessionUtils.getRoleBySession(session);
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
    public void feedPartner(final IoSession session, final ReqFeedPartner reqFeedPartner) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final String type = reqFeedPartner.getType();
            if (type == null) {
                return;
            }
            final Integer partnerId = reqFeedPartner.getPartnerId();
            if (partnerId == null) {
                return;
            }
            final Map<Integer, Partner> partnerMap = DataCache.PARTNER_MAP.get(role.getId());
            if (partnerMap == null || partnerMap.size() <= 0) {
                return;
            }
            final Partner partner = partnerMap.get(partnerId);
            if (partner == null) {
                return;
            }
            final Integer param = reqFeedPartner.getParam();
            if (param == null) {
                return;
            }
            this.partnerService.feedPartner(role, partner, type, param);
        }
    }
    
    @RequestMapping
    public void changeDefaultSkill(final IoSession session, final ReqChangeDefaultSkill reqChangeDefaultSkill) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final int pId = reqChangeDefaultSkill.getPId();
            final int skillId = reqChangeDefaultSkill.getSkillId();
            if (pId < 0 || skillId < 0) {
                return;
            }
            this.partnerService.changeDefaultSkill(role, pId, skillId);
        }
    }
}
