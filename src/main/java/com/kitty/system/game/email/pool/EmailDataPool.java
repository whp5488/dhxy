package com.kitty.system.game.email.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.init.service.InitService;
import java.util.concurrent.CopyOnWriteArrayList;

public class EmailDataPool {
  public static CopyOnWriteArrayList<EmailSystem> emailSystemList = new CopyOnWriteArrayList<>();
  
  public static void load() {
    InitService initService = SpringUtils.getInitService();
    emailSystemList = initService.initEmailSystem();
  }
}
