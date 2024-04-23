package com.kitty.system.game.train.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.service.InitService;

public class TrainDataPool {
  public static void load() {
    InitService initService = SpringUtils.getInitService();
  }
}
