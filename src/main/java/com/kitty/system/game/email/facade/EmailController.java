package com.kitty.system.game.email.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.email.message.ReqEmaiLPickUpGoods;
import com.kitty.system.game.email.message.ReqEmailDetail;
import com.kitty.system.game.email.message.ReqEmailList;
import com.kitty.system.game.email.service.EmailService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController {
  @Autowired
  private EmailService emailService;
  
  @RequestMapping
  public void getEmailList(IoSession session, ReqEmailList reqEmailList) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.emailService.getEmailList(role);
  }
  
  @RequestMapping
  public void getEmailDetail(IoSession session, ReqEmailDetail reqEmailDetail) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    String emailIdStr = reqEmailDetail.getEmailId();
    byte isSystem = reqEmailDetail.getIsSystem();
    if (emailIdStr != null)
      try {
        long emailId = Long.parseLong(emailIdStr);
        if (emailId > 0L)
          this.emailService.getEmailDetail(role, Long.valueOf(emailId), isSystem); 
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }  
  }
  
  @RequestMapping
  public void pickUpGoods(IoSession session, ReqEmaiLPickUpGoods reqEmaiLPickUpGoods) {
    String emailIdStr = reqEmaiLPickUpGoods.getEmailId();
    long emailId = -1L;
    try {
      emailId = Long.parseLong(emailIdStr);
    } catch (NumberFormatException e) {
      return;
    } 
    Role role = SessionUtils.getRoleBySession(session);
    if (emailId > 0L && role != null) {
      byte type = reqEmaiLPickUpGoods.getType();
      if (type == 0) {
        this.emailService.deleteEmail(role, Long.valueOf(emailId));
      } else if (type == 1) {
        this.emailService.pickUpGoods(role, Long.valueOf(emailId));
      } 
    } 
  }
}
