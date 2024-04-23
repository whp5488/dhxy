package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedPetHandler {
  protected Logger logger = LoggerFunction.FEED_PET.getLogger();
  
  protected PetService petService = SpringUtils.getPetService();
  
  protected RoleService roleService = SpringUtils.getRoleService();
  
  protected BagService bagService = (BagService)SpringUtils.getBean(BagService.class);
  
  static final String ERROR_PETSET = "#<Y>{0}#宠物配置未找到，无法进行此操作。";
  
  public abstract String acceptFeed(Pet paramPet, Role paramRole, Integer paramInteger, String paramString, int paramInt, Object... paramVarArgs);
}
