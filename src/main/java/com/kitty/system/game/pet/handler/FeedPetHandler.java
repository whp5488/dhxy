
package com.kitty.system.game.pet.handler;

import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.pet.service.PetService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class FeedPetHandler
{
    protected Logger logger;
    protected PetService petService;
    protected RoleService roleService;
    protected BagService bagService;
    static final String ERROR_PETSET = "#<Y>{0}#宠物配置未找到，无法进行此操作。";
    
    public FeedPetHandler() {
        this.logger = LoggerFunction.FEED_PET.getLogger();
        this.petService = SpringUtils.getPetService();
        this.roleService = SpringUtils.getRoleService();
        this.bagService = SpringUtils.getBean(BagService.class);
    }
    
    public abstract String acceptFeed(final Pet p0, final Role p1, final Integer p2, final String p3, final int p4, final Object... p5);
}
