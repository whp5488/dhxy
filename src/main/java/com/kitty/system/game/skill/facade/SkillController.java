package com.kitty.system.game.skill.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.message.ReqActivateSkillEffects;
import com.kitty.system.game.skill.message.ReqLearnSkill;
import com.kitty.system.game.skill.message.ReqSkillEffects;
import com.kitty.system.game.skill.message.ReqSuitSkillEffects;
import com.kitty.system.game.skill.service.SkillService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SkillController {
  Logger logger = LoggerFactory.getLogger(SkillController.class);
  
  @Autowired
  private SkillService skillService;
  
  @RequestMapping
  public void learnSkill(IoSession session, ReqLearnSkill reqLearnSkill) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.skillService.learnSkill(reqLearnSkill, role);
  }
  
  @RequestMapping
  public void skillEffects(IoSession session, ReqSkillEffects reqSkillEffects) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.skillService.skillEffects(role, reqSkillEffects.getSerial());
  }
  
  @RequestMapping
  public void activateSkillEffects(IoSession session, ReqActivateSkillEffects reqActivateSkillEffects) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.skillService.activateSkillEffects(role, reqActivateSkillEffects);
  }
  
  @RequestMapping
  public void suitSkillEffects(IoSession session, ReqSuitSkillEffects reqSuitSkillEffects) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.skillService.suitSkillEffects(role, reqSuitSkillEffects);
  }
}
