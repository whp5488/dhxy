package com.kitty.system.game.echo.pool;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.general.service.GeneralService;
import com.kitty.system.game.role.service.TitleService;
import java.util.ArrayList;
import java.util.List;

public class EchoDataPool {
  public static List<EchoHandler> echoHandlers = new ArrayList<>();
  
  static {
    echoHandlers.add(SpringUtils.getBean(CardService.class));
    echoHandlers.add(SpringUtils.getBean(GeneralService.class));
    echoHandlers.add(SpringUtils.getBean(TitleService.class));
  }
}
