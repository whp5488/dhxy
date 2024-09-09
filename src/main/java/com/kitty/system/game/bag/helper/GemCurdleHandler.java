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
    
    public GemCurdleHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        int curve = (int)role.getRelive() * 1000 + (int)role.getLevel();
        if (curve < 1110) {
            return "请在对应系统开启后再使用此道具~";
        }
        if ((int)num <= 0) {
            return null;
        }
        int i = 0;
        while (i < GemCurdleHandler.GEM_SERIAL.length) {
            Integer bagGoods = this.bagService.getBagGoods(role, GemCurdleHandler.GEM_SERIAL[i], (int)num);
            if (bagGoods != null) {
                return String.format("缺少1级%s宝石X%d", GemCurdleHandler.GEM_TYPE[i], bagGoods);
            }
            ++i;
        }
        if (!this.bagService.checkPos(role, 75116, (int)num)) {
            return null;
        }
        i = 0;
        while (i < GemCurdleHandler.GEM_SERIAL.length) {
            Integer integer = this.bagService.consumeBagGoods(role, GemCurdleHandler.GEM_SERIAL[i], (int)num, false, true, "宝石凝合");
            if (integer != null) {
                return null;
            }
            ++i;
        }
        this.bagService.addGoods(role, 75116, (int)num, 0, false, false, "宝石凝合");
        return null;
    }
    
    static {
        GEM_SERIAL = new int[] { 70101, 70201, 70301, 70401, 70501 };
        GEM_TYPE = new String[] { "红", "绿", "蓝", "紫", "橙" };
    }
}
