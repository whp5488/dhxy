
package com.kitty.system.game.bag.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.text.MessageFormat;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import java.util.Random;
import java.util.List;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.ArrayList;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("FiveElements")
public class FiveElementsHandler extends BagHandler
{
    private static final String LACK = "材料数量不足！";
    private static final String SUCCEED = "灵力流转，你获得了1个{0}。";
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final int bagServicePos = this.bagService.getPos(role, false);
        if (bagServicePos < 0) {
            return "物品栏空间不足";
        }
        try {
            final NutMap nutMap = (NutMap)Json.fromJson(NutMap.class, (CharSequence)param);
            int count = 0;
            final Iterator iterator = nutMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String)iterator.next();
                count += nutMap.getInt(key);
            }
            if (count != 5) {
                return "材料数量不足！";
            }
            final Map<Integer, List<Integer>> map = this.getFiveData();
            List<Integer> list = new ArrayList<Integer>();
            Integer index = 0;
            for (final String key2 : nutMap.keySet()) {
                final int anInt = nutMap.getInt(key2);
                if (anInt < 1) {
                    return "材料数量不足！";
                }
                final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
                final BagGoods goods = bagGoodsMap.values().stream().filter(e -> Integer.valueOf(key2).equals(e.getPos())).findFirst().orElse(null);
                if (goods == null) {
                    return "材料数量不足！";
                }
                if (index == 0) {
                    index = this.getFiveLevel(map, goods.getSerial());
                    if (index == -1) {
                        return "材料数量不足！";
                    }
                    list = map.get(index);
                }
                if (!list.contains(goods.getSerial())) {
                    return "材料数量不足！";
                }
                final Integer bagGoodsNum = this.bagService.getBagGoodsNum(role, goods.getSerial());
                if (bagGoodsNum == null || bagGoodsNum < anInt) {
                    return "材料数量不足！";
                }
            }
            for (final String key2 : nutMap.keySet()) {
                final Integer integer = this.bagService.consumeBagGoodsByPos(role, nutMap.getInt(key2), Integer.valueOf(key2), true, "五行书熔炼");
                if (integer != null) {
                    return "非法操作";
                }
            }
            final List<Integer> integers = map.get(index + 1);
            final Random random = new Random();
            final Integer serial = integers.get(random.nextInt(integers.size()));
            MessagePusher.pushMessage(role, new RespHint(12, (short)1));
            this.bagService.addGoods(role, serial, 1, 0, true, true, "五行书熔炼");
            final SysDataStuff stuff = ItemDataPool.data_stuff.get(serial);
            return MessageFormat.format("灵力流转，你获得了1个{0}。", stuff.getName());
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public int getFiveLevel(final Map<Integer, List<Integer>> map, final Integer serial) {
        for (final Integer key : map.keySet()) {
            if (key > 2) {
                return -1;
            }
            final List<Integer> list = map.get(key);
            if (list.contains(serial)) {
                return key;
            }
        }
        return -1;
    }
    
    public Map<Integer, List<Integer>> getFiveData() {
        final Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        map.put(1, Arrays.asList(75001, 75002, 75003, 75004, 75005));
        map.put(2, Arrays.asList(75006, 75007, 75008, 75009, 75010));
        map.put(3, Arrays.asList(75011, 75012, 75013, 75014, 75015));
        return map;
    }
}
