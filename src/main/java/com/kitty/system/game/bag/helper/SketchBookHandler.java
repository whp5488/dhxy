
package com.kitty.system.game.bag.helper;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.List;
import java.text.MessageFormat;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("SketchBook")
public class SketchBookHandler extends BagHandler
{
    private static final String LACK = "缺少见闻录{0}篇X{1}";
    private static final int SERIAL = 90033;
    private static final String[] DESC;
    private static final int[] NEED;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        if (!this.bagService.checkPos(role, 90033, num)) {
            return null;
        }
        for (int i = 0; i < SketchBookHandler.NEED.length; ++i) {
            final Integer bagGoods = this.bagService.getBagGoods(role, SketchBookHandler.NEED[i], num);
            if (bagGoods != null) {
                return MessageFormat.format("缺少见闻录{0}篇X{1}", SketchBookHandler.DESC[i], bagGoods);
            }
        }
        for (int i = 0; i < num; ++i) {
            final boolean b = this.bagService.checkPos(role, 90033, 1);
            if (!b) {
                break;
            }
            int bd = 0;
            final Map<Long, BagGoods> bagGoods2 = this.bagService.getBagGoods(role);
            for (int j = 0; j < SketchBookHandler.NEED.length; ++j) {
                final int finalJ = j;
                final List<BagGoods> collect = bagGoods2.values().stream().filter(e -> e.getSerial() == SketchBookHandler.NEED[finalJ]).collect(Collectors.toList());
                if (collect.get(0).getRests().getInt(String.valueOf(2), 0) > 0) {
                    bd = 2;
                    break;
                }
            }
            for (int j = 0; j < SketchBookHandler.NEED.length; ++j) {
                this.bagService.consumeBagGoods(role, SketchBookHandler.NEED[j], 1, true, true, "见闻录合成");
            }
            this.bagService.addGoods(role, 90033, 1, bd, true, false, "见闻录合成");
        }
        return null;
    }
    
    static {
        DESC = new String[] { "下", "上", "中" };
        NEED = new int[] { 75051, 75049, 75050 };
    }
}
