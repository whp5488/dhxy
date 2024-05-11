
package com.kitty.system.game.bag.helper;

import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("GemCurdle")
public class GemCurdleHandler extends BagHandler
{
    private static final int SERIAL = 75116;
    private static final String LACK = "缺少1级%s宝石X%d";
    private static final int LIMIT_LEVEL = 1110;
    private static final int[] GEM_SERIAL;
    private static final String[] GEM_TYPE;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final int curve = role.getRelive() * 1000 + role.getLevel();
        if (curve < 1110) {
            return "请在对应系统开启后再使用此道具~";
        }
        if (num <= 0) {
            return null;
        }
        for (int i = 0; i < GemCurdleHandler.GEM_SERIAL.length; ++i) {
            final Integer bagGoods = this.bagService.getBagGoods(role, GemCurdleHandler.GEM_SERIAL[i], num);
            if (bagGoods != null) {
                return String.format("缺少1级%s宝石X%d", GemCurdleHandler.GEM_TYPE[i], bagGoods);
            }
        }
        if (!this.bagService.checkPos(role, 75116, num)) {
            return null;
        }
        for (int i = 0; i < GemCurdleHandler.GEM_SERIAL.length; ++i) {
            final Integer integer = this.bagService.consumeBagGoods(role, GemCurdleHandler.GEM_SERIAL[i], num, false, true, "宝石凝合");
            if (integer != null) {
                return null;
            }
        }
        this.bagService.addGoods(role, 75116, num, 0, false, false, "宝石凝合");
        return null;
    }
    
    static {
        GEM_SERIAL = new int[] { 70101, 70201, 70301, 70401, 70501 };
        GEM_TYPE = new String[] { "红", "绿", "蓝", "紫", "橙" };
    }
}
