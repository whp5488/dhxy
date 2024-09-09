package com.kitty.system.game.bag.helper;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("GemFit")
public class GemFitHandler extends BagHandler
{
    private static final String LACK = "当前宝石数量不足，无法合成";
    private static final int[] GEM_SERIAL;
    
    public GemFitHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        Integer value = Integer.valueOf(param);
        int type = (int)value / 1000;
        int level = (int)value % 1000;
        if (level < 2 || level > 7 || (int)num <= 0 || type < 1 || type > 5) {
            return null;
        }
        int gemNum = consume(level, (int)num);
        int gemSerial = consumeGemSerial(type, level);
        int serial = gemSerial + 1;
        if (this.bagService.getBagGoods(role, gemSerial, gemNum) != null) {
            return "当前宝石数量不足，无法合成";
        }
        if (!this.bagService.checkPos(role, serial, (int)num)) {
            return null;
        }
        Integer integer = this.bagService.consumeBagGoods(role, gemSerial, gemNum, false, true, "合成宝石");
        if (integer != null) {
            return "当前宝石数量不足，无法合成";
        }
        this.bagService.addGoods(role, serial, (int)num, 0, false, false, "合成宝石");
        MessagePusher.pushMessage(role, new RespHint(11, (short)1));
        return null;
    }
    
    public static int consume(int level, int num) {
        int need = 6;
        if (level == 3) {
            need = 5;
        }
        else if (level == 4) {
            need = 4;
        }
        else if (level == 5) {
            need = 3;
        }
        else if (level == 6 || level == 7) {
            need = 2;
        }
        return need * num;
    }
    
    public static int consumeGemSerial(int type, int level) {
        int serial = GemFitHandler.GEM_SERIAL[type - 1];
        if (level > 2) {
            serial += level - 2;
        }
        return serial;
    }
    
    static {
        GEM_SERIAL = new int[] { 70101, 70201, 70301, 70401, 70501 };
    }
}
