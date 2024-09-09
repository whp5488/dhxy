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
    
    public BagController() {
    }
    
    @RequestMapping
    public void itemUse(IoSession session, ReqItemUse reqItemUse) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (reqItemUse.getNum() <= 0) {
            return;
        }
        int pos = reqItemUse.getPos();
        this.bagService.useItem(role, pos, reqItemUse.getNum());
        return;
    }
    
    @RequestMapping
    public void drugUse(IoSession session, ReqDrugUse drugUse) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.useDrug(role, drugUse);
        return;
    }
    
    @RequestMapping
    public void itemOperate(IoSession session, ReqOperateItem operateItem) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.itemOperate(role, operateItem);
        return;
    }
    
    @RequestMapping
    public void exchange(IoSession session, ReqExchange reqExchange) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.bagService.exchange(role, reqExchange);
        return;
    }
    
    @RequestMapping
    public void useIncense(IoSession session, ReqUseIncense reqUseIncense) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        Integer residueTimes = reqUseIncense.getResidueTimes();
        if (residueTimes == null) {
            residueTimes = Integer.valueOf(0);
        }
        Integer serial = reqUseIncense.getSerial();
        if (serial == null || (int)serial <= 0) {
            serial = Integer.valueOf(90005);
        }
        this.bagService.useIncense(role, (int)serial, (int)residueTimes);
        return;
    }
}
