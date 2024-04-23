package com.kitty.system.game.safe.facade;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.safe.message.ReqModifyPassword;
import com.kitty.system.game.safe.message.ReqOpenSafeLockDlg;
import com.kitty.system.game.safe.message.ReqSetSafePassword;
import com.kitty.system.game.safe.message.ReqUnlock;
import com.kitty.system.game.safe.service.SafeService;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SafeController {
  @Autowired
  SafeService safeService;
  
  @RequestMapping
  public void setSafePassword(IoSession session, ReqSetSafePassword reqSetSafePassword) {
    this.safeService.setSafePassword(session, reqSetSafePassword);
  }
  
  @RequestMapping
  public void openSafeDlg(IoSession session, ReqOpenSafeLockDlg reqOpenSafeLockDlg) {
    this.safeService.openSafeDlg(session);
  }
  
  @RequestMapping
  public void unlock(IoSession session, ReqUnlock reqUnlock) {
    this.safeService.unlock(session, reqUnlock);
  }
  
  @RequestMapping
  public void modifyPassword(IoSession session, ReqModifyPassword reqModifyPassword) {
    this.safeService.modifyPassword(session, reqModifyPassword);
  }
}
