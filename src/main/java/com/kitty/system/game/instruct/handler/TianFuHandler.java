
package com.kitty.system.game.instruct.handler;

import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.instruct.inter.NcFather;

public class TianFuHandler implements NcFather
{
    @Override
    public void handle(final Role role, final String info) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            return;
        }
        final String[] split = info.split(",");
        final Integer serial = Integer.valueOf(split[0]);
        Short num = Short.valueOf(split[1]);
        if (serial == null || num == null) {
            return;
        }
        if (9997 != serial) {
            num = 1;
        }
        final RoleTianFuService bean = SpringUtils.getBean(RoleTianFuService.class);
        bean.putTianFu(role, serial, num);
    }
}
