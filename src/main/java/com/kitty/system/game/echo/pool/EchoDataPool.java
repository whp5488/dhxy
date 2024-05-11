
package com.kitty.system.game.echo.pool;

import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.general.service.GeneralService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.card.service.CardService;
import java.util.ArrayList;
import com.kitty.system.game.echo.handler.EchoHandler;
import java.util.List;

public class EchoDataPool
{
    public static List<EchoHandler> echoHandlers;
    
    static {
        (EchoDataPool.echoHandlers = new ArrayList<EchoHandler>()).add(SpringUtils.getBean(CardService.class));
        EchoDataPool.echoHandlers.add(SpringUtils.getBean(GeneralService.class));
        EchoDataPool.echoHandlers.add(SpringUtils.getBean(TitleService.class));
    }
}
