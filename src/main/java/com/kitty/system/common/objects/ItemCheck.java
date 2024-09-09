package com.kitty.system.common.objects;

import org.nutz.lang.util.NutMap;
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
    public ItemCheck() {
    }
    
    public static CheckMapInt checkAllItemTypes(List<Item> list, Role role) {
        Map<Long, BagGoods> bagGoodsMap = SpringUtils.getBean(BagService.class).getBagGoods(role);
        boolean issucc = Boolean.TRUE;
        Map<Integer, Integer> lackitems = new HashMap<>();
        Map<Integer, Integer> hasitems = new HashMap<>();
        for (Item item : list) {
            Integer itemType = item.getItem();
            Integer num = item.getNum();
            Integer count = bagGoodsSum(bagGoodsMap, itemType);
            if (count < num) {
                issucc = Boolean.FALSE;
                hasitems.put(itemType, count);
                lackitems.put(itemType, num - count);
            }
            else {
                hasitems.put(itemType, num);
            }
        }
        CheckMapInt check = new CheckMapInt(issucc, lackitems, hasitems);
        return check;
    }
    
    public static Integer bagGoodsSum(Map<Long, BagGoods> bagGoodsMap, int type) {
        int sum = 0;
        List<BagGoods> collect = bagGoodsMap.values().stream().filter(goods -> goods.getSerial() == type).collect(Collectors.toList());
        Iterator<BagGoods> iterator = collect.iterator();
        while (iterator.hasNext()) {
        	BagGoods goods = iterator.next();
            sum += getBagGoodsNum(goods);
        }
        return sum;
    }
    
    public static int getBagGoodsNum(BagGoods bagGoods) {
        int num = 0;
        NutMap rests = bagGoods.getRests();
        for (String s : rests.keySet()) {
            num += rests.getInt(s);
        }
        return num;
    }
}
