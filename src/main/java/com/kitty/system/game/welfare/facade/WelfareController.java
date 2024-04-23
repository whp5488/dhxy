package com.kitty.system.game.welfare.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.welfare.message.ReqDrawMoneyFiveGuess;
import com.kitty.system.game.welfare.message.ReqFiveGuess;
import com.kitty.system.game.welfare.message.ReqFiveGuessReply;
import com.kitty.system.game.welfare.message.ReqLuckDraw;
import com.kitty.system.game.welfare.message.ReqMagicBoxInfo;
import com.kitty.system.game.welfare.message.ReqMagicBoxLotto;
import com.kitty.system.game.welfare.service.WelfareService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WelfareController {
  Logger logger = LoggerFactory.getLogger(WelfareController.class);
  
  @Autowired
  private WelfareService welfareService;
  
  @RequestMapping
  public void luckDraw(IoSession session, ReqLuckDraw reqLuckDraw) {
    switch (reqLuckDraw.getClassify()) {
      case 1:
        this.welfareService.luckDraw(session, reqLuckDraw);
        return;
      case 2:
        this.welfareService.prizeDraw(session, reqLuckDraw);
        return;
    } 
    this.logger.error("未处理功能==CODE:{}", Byte.valueOf(reqLuckDraw.getClassify()));
  }
  
  @RequestMapping
  public void fiveGuess(IoSession session, ReqFiveGuess reqFiveGuess) {
    this.welfareService.fiveGuess(session, reqFiveGuess);
  }
  
  @RequestMapping
  public void fiveGuessReply(IoSession session, ReqFiveGuessReply reqFiveGuessReply) {
    this.welfareService.fiveGuessReply(session, reqFiveGuessReply);
  }
  
  @RequestMapping
  public void drawMoneyFiveGuess(IoSession session, ReqDrawMoneyFiveGuess drawMoneyFiveGuess) {
    this.welfareService.drawMoneyFiveGuess(session);
  }
  
  @RequestMapping
  public void reqMagicBoxInfo(IoSession session, ReqMagicBoxInfo reqMagicBoxInfo) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.welfareService.reqMagicBoxInfo(role, reqMagicBoxInfo);
  }
  
  @RequestMapping
  public void reqMagicBoxLotto(IoSession session, ReqMagicBoxLotto reqMagicBoxLotto) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.welfareService.reqMagicBoxLotto(role, reqMagicBoxLotto);
  }
}
