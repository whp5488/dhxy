package com.kitty.system.game.bag.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.bag.message.ReqDrugUse;
import com.kitty.system.game.bag.message.ReqExchange;
import com.kitty.system.game.bag.message.ReqItemUse;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.message.ReqUseIncense;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BagController {
  @Autowired
  private BagService bagService;
  
  @RequestMapping
  public void itemUse(IoSession session, ReqItemUse reqItemUse) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (reqItemUse.getNum() <= 0)
      return; 
    int pos = reqItemUse.getPos();
    this.bagService.useItem(role, pos, reqItemUse.getNum());
  }
  
  @RequestMapping
  public void drugUse(IoSession session, ReqDrugUse drugUse) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.bagService.useDrug(role, drugUse);
  }
  
  @RequestMapping
  public void itemOperate(IoSession session, ReqOperateItem operateItem) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.bagService.itemOperate(role, operateItem);
  }
  
  @RequestMapping
  public void exchange(IoSession session, ReqExchange reqExchange) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.bagService.exchange(role, reqExchange);
  }
  
  @RequestMapping
  public void useIncense(IoSession session, ReqUseIncense reqUseIncense) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    Integer residueTimes = reqUseIncense.getResidueTimes();
    if (residueTimes == null)
      residueTimes = Integer.valueOf(0); 
    Integer serial = reqUseIncense.getSerial();
    if (serial == null || serial.intValue() <= 0)
      serial = Integer.valueOf(90005); 
    this.bagService.useIncense(role, serial.intValue(), residueTimes.intValue());
  }
}
