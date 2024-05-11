
package com.kitty.system.game.email.pool;

import com.kitty.system.init.service.InitService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.email.domain.EmailSystem;
import java.util.concurrent.CopyOnWriteArrayList;

public class EmailDataPool
{
    public static CopyOnWriteArrayList<EmailSystem> emailSystemList;
    
    public static void load() {
        final InitService initService = SpringUtils.getInitService();
        EmailDataPool.emailSystemList = initService.initEmailSystem();
    }
    
    static {
        EmailDataPool.emailSystemList = new CopyOnWriteArrayList<EmailSystem>();
    }
}
