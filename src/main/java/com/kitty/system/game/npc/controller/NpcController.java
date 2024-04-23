package com.kitty.system.game.npc.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.npc.message.ReqCloseMenu;
import com.kitty.system.game.npc.message.ReqMotto;
import com.kitty.system.game.npc.message.ReqNpcTalk;
import com.kitty.system.game.npc.message.ReqOpenMenu;
import com.kitty.system.game.npc.message.ReqWorship;
import com.kitty.system.game.npc.message.RespNpcLeave;
import com.kitty.system.game.npc.service.NpcService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class NpcController {
  @Autowired
  private NpcService npcService;
  
  Logger logger = LoggerFactory.getLogger(getClass());
  
  @RequestMapping
  public void reqNpcContent(IoSession session, ReqOpenMenu reqOpenMenu) {
    Role role = SessionUtils.getRoleBySession(session);
    this.npcService.requestNpcContent(role, reqOpenMenu.getNpcId());
  }
  
  @RequestMapping
  public void reqClickNpcButton(IoSession session, ReqNpcTalk reqNpcTalk) {
    Role role = SessionUtils.getRoleBySession(session);
    this.npcService.clickNpcButton(role, reqNpcTalk.getNpcId(), reqNpcTalk.getMsg(), reqNpcTalk.getPara());
  }
  
  @RequestMapping
  public void closeMenu(IoSession session, ReqCloseMenu reqCloseMenu) {
    RespNpcLeave respNpcLeave = new RespNpcLeave();
    respNpcLeave.setNpcId(reqCloseMenu.getNpcId());
    MessagePusher.pushMessage(session, (Message)respNpcLeave);
  }
  
  @RequestMapping
  public void worship(IoSession session, ReqWorship reqWorship) {
    this.npcService.worship(session, reqWorship);
  }
  
  @RequestMapping
  public void motto(IoSession session, ReqMotto reqMotto) {
    this.npcService.motto(session, reqMotto.getMotto());
  }
}
