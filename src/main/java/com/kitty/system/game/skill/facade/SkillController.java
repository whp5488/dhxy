
package com.kitty.system.game.skill.facade;

import com.kitty.system.game.skill.message.ReqSuitSkillEffects;
import com.kitty.system.game.skill.message.ReqActivateSkillEffects;
import com.kitty.system.game.skill.message.ReqSkillEffects;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.skill.message.ReqLearnSkill;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.skill.service.SkillService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class SkillController
{
    Logger logger;
    @Autowired
    private SkillService skillService;
    
    public SkillController() {
        this.logger = LoggerFactory.getLogger(SkillController.class);
    }
    
    @RequestMapping
    public void learnSkill(final IoSession session, final ReqLearnSkill reqLearnSkill) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.skillService.learnSkill(reqLearnSkill, role);
    }
    
    @RequestMapping
    public void skillEffects(final IoSession session, final ReqSkillEffects reqSkillEffects) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.skillService.skillEffects(role, reqSkillEffects.getSerial());
    }
    
    @RequestMapping
    public void activateSkillEffects(final IoSession session, final ReqActivateSkillEffects reqActivateSkillEffects) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.skillService.activateSkillEffects(role, reqActivateSkillEffects);
    }
    
    @RequestMapping
    public void suitSkillEffects(final IoSession session, final ReqSuitSkillEffects reqSuitSkillEffects) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.skillService.suitSkillEffects(role, reqSuitSkillEffects);
    }
}
