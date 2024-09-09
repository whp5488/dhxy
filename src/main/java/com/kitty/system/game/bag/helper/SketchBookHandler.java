package com.kitty.system.game.bag.helper;

import java.util.Map;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.stream.Collectors;
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
    
    public SketchBookHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        if (!this.bagService.checkPos(role, 90033, (int)num)) {
            return null;
        }
        int i = 0;
        while (i < SketchBookHandler.NEED.length) {
            Integer bagGoods = this.bagService.getBagGoods(role, SketchBookHandler.NEED[i], (int)num);
            if (bagGoods != null) {
                return MessageFormat.format("缺少见闻录{0}篇X{1}", SketchBookHandler.DESC[i], bagGoods);
            }
            ++i;
        }
        i = 0;
        while (i < (int)num) {
            boolean b = this.bagService.checkPos(role, 90033, 1);
            if (b) {
                int bd = 0;
                Map<Long, BagGoods> bagGoods2 = this.bagService.getBagGoods(role);
                int j = 0;
                while (j < SketchBookHandler.NEED.length) {
                    int finalJ = j;
                    List<BagGoods> collect = (List)bagGoods2.values().stream().filter(e -> e.getSerial() == SketchBookHandler.NEED[finalJ]).collect(Collectors.toList());
                    if (((BagGoods)collect.get(0)).getRests().getInt(String.valueOf(2), 0) > 0) {
                        bd = 2;
                        break;
                    }
                    ++j;
                }
                for (j = 0; j < SketchBookHandler.NEED.length; ++j) {
                    this.bagService.consumeBagGoods(role, SketchBookHandler.NEED[j], 1, true, true, "见闻录合成");
                }
                this.bagService.addGoods(role, 90033, 1, bd, true, false, "见闻录合成");
                ++i;
            }
            else {
                break;
            }
        }
        return null;
    }
    
    static {
        DESC = new String[] { "下", "上", "中" };
        NEED = new int[] { 75051, 75049, 75050 };
    }
}
