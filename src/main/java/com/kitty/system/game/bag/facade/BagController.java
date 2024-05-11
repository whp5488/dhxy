
package com.kitty.system.game.bag.facade;

import com.kitty.system.game.bag.message.ReqUseIncense;
import com.kitty.system.game.bag.message.ReqExchange;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.message.ReqDrugUse;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.bag.message.ReqItemUse;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.stereotype.Controller;

@Controller
public class BagController
{
    @Autowired
    private BagService bagService;
    
    @RequestMapping
    public void itemUse(final IoSession session, final ReqItemUse reqItemUse) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (reqItemUse.getNum() <= 0) {
            return;
        }
        final int pos = reqItemUse.getPos();
        this.bagService.useItem(role, pos, reqItemUse.getNum());
    }
    
    @RequestMapping
    public void drugUse(final IoSession session, final ReqDrugUse drugUse) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.useDrug(role, drugUse);
    }
    
    @RequestMapping
    public void itemOperate(final IoSession session, final ReqOperateItem operateItem) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.itemOperate(role, operateItem);
    }
    
    @RequestMapping
    public void exchange(final IoSession session, final ReqExchange reqExchange) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.exchange(role, reqExchange);
    }
    
    @RequestMapping
    public void useIncense(final IoSession session, final ReqUseIncense reqUseIncense) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Integer residueTimes = reqUseIncense.getResidueTimes();
        if (residueTimes == null) {
            residueTimes = 0;
        }
        Integer serial = reqUseIncense.getSerial();
        if (serial == null || serial <= 0) {
            serial = 90005;
        }
        this.bagService.useIncense(role, serial, residueTimes);
    }
}
