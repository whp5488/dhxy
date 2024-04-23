package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.queue.BroadcastThread;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class BagHandler {
  protected Logger logger = LoggerFunction.ITEM_PARTNER.getLogger();
  
  protected CardService cardService = (CardService)SpringUtils.getBean(CardService.class);
  
  protected BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
  
  protected RoleService roleService = SpringUtils.getRoleService();
  
  protected EquipService equipService = SpringUtils.getEquipService();
  
  protected BroadcastThread broadcastThread = SpringUtils.getBroadcastThread();
  
  static final String ROLE_VACANCY_NOT_ENOUGH = "很抱歉，你的包裹空位不足，请整理后再来吧。";
  
  static final String NOT_FIND_CONFIG = "很抱歉，没有找到该道具的配置。";
  
  public abstract String acceptOperate(Role paramRole, Integer paramInteger1, Integer paramInteger2, String paramString);
}
