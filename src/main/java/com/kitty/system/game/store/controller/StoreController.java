package com.kitty.system.game.store.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.store.message.ReqExcuteStorePara;
import com.kitty.system.game.store.service.StoreService;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StoreController {
  Logger logger = LoggerFactory.getLogger(StoreController.class);
  
  @Autowired
  private StoreService storeService;
  
  @RequestMapping
  public void excuteStorePara(IoSession session, ReqExcuteStorePara reqExcuteStorePara) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (reqExcuteStorePara.getType() < 0 || reqExcuteStorePara.getType() > 1)
      return; 
    try {
      this.storeService.excuteStorePara(role, Byte.valueOf(reqExcuteStorePara.getType()), Byte.valueOf(reqExcuteStorePara.getStoreType()), reqExcuteStorePara.getPos());
    } catch (Exception e) {
      this.logger.error("仓库物品存取操作异常：{}", e.getMessage());
      e.printStackTrace();
    } 
  }
}
