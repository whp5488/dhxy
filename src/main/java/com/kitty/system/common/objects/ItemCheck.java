
package com.kitty.system.common.objects;

import org.nutz.lang.util.NutMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Iterator;
import com.kitty.system.game.bag.domain.BagGoods;
import java.util.Map;
import java.util.HashMap;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.vo.CheckMapInt;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.init.Item;
import java.util.List;

public class ItemCheck
{
    public static CheckMapInt checkAllItemTypes(final List<Item> list, final Role role) {
        final Map<Long, BagGoods> bagGoodsMap = SpringUtils.getBean(BagService.class).getBagGoods(role);
        Boolean issucc = true;
        final Map<Integer, Integer> lackitems = new HashMap<Integer, Integer>();
        final Map<Integer, Integer> hasitems = new HashMap<Integer, Integer>();
        for (final Item item : list) {
            final Integer itemType = item.getItem();
            final Integer num = item.getNum();
            final Integer count = bagGoodsSum(bagGoodsMap, itemType);
            if (count < num) {
                issucc = false;
                hasitems.put(itemType, count);
                lackitems.put(itemType, num - count);
            }
            else {
                hasitems.put(itemType, num);
            }
        }
        final CheckMapInt check = new CheckMapInt(issucc, lackitems, hasitems);
        return check;
    }
    
    public static Integer bagGoodsSum(final Map<Long, BagGoods> bagGoodsMap, final int type) {
        int sum = 0;
        final List<BagGoods> collect = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == type).collect(Collectors.toList());
        final Iterator<BagGoods> iterator = collect.iterator();
        while (iterator.hasNext()) {
            BagGoods goods = iterator.next();
            sum += getBagGoodsNum(goods);
        }
        return sum;
    }
    
    public static int getBagGoodsNum(final BagGoods bagGoods) {
        int num = 0;
        final NutMap rests = bagGoods.getRests();
        for (final String s : rests.keySet()) {
            num += rests.getInt(s);
        }
        return num;
    }
}
