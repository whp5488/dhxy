
package com.kitty.system.game.safe.facade;

import com.kitty.system.game.safe.message.ReqModifyPassword;
import com.kitty.system.game.safe.message.ReqUnlock;
import com.kitty.system.game.safe.message.ReqOpenSafeLockDlg;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.safe.message.ReqSetSafePassword;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.safe.service.SafeService;
import org.springframework.stereotype.Controller;

@Controller
public class SafeController
{
    @Autowired
    SafeService safeService;
    
    @RequestMapping
    public void setSafePassword(final IoSession session, final ReqSetSafePassword reqSetSafePassword) {
        this.safeService.setSafePassword(session, reqSetSafePassword);
    }
    
    @RequestMapping
    public void openSafeDlg(final IoSession session, final ReqOpenSafeLockDlg reqOpenSafeLockDlg) {
        this.safeService.openSafeDlg(session);
    }
    
    @RequestMapping
    public void unlock(final IoSession session, final ReqUnlock reqUnlock) {
        this.safeService.unlock(session, reqUnlock);
    }
    
    @RequestMapping
    public void modifyPassword(final IoSession session, final ReqModifyPassword reqModifyPassword) {
        this.safeService.modifyPassword(session, reqModifyPassword);
    }
}
