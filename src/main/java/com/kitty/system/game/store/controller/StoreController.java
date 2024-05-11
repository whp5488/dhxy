
package com.kitty.system.game.store.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.store.message.ReqExcuteStorePara;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.store.service.StoreService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class StoreController
{
    Logger logger;
    @Autowired
    private StoreService storeService;
    
    public StoreController() {
        this.logger = LoggerFactory.getLogger(StoreController.class);
    }
    
    @RequestMapping
    public void excuteStorePara(final IoSession session, final ReqExcuteStorePara reqExcuteStorePara) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (reqExcuteStorePara.getType() < 0 || reqExcuteStorePara.getType() > 1) {
            return;
        }
        try {
            this.storeService.excuteStorePara(role, reqExcuteStorePara.getType(), reqExcuteStorePara.getStoreType(), reqExcuteStorePara.getPos());
        }
        catch (final Exception e) {
            this.logger.error("仓库物品存取操作异常：{}", (Object)e.getMessage());
            e.printStackTrace();
        }
    }
}
