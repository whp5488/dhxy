package com.kitty.system.game.bag.helper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.text.MessageFormat;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
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
    
    public FiveElementsHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        int bagServicePos = this.bagService.getPos(role, false);
        if (bagServicePos < 0) {
            return "物品栏空间不足";
        }
        try {
            NutMap nutMap = (NutMap)Json.fromJson(NutMap.class, param);
            int count = 0;
            Iterator<String> iterator = nutMap.keySet().iterator();
            while (iterator.hasNext()) {
            	String key = (String)iterator.next();
                count += nutMap.getInt(key);
            }
            if (count != 5) {
                return "材料数量不足！";
            }
            Map<Integer, List<Integer>> map = this.getFiveData();
            List<Integer> list = new ArrayList<>();
            Integer index = Integer.valueOf(0);
            for (String key2 : nutMap.keySet()) {
                int anInt = nutMap.getInt(key2);
                if (anInt < 1) {
                    return "材料数量不足！";
                }
                Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
                BagGoods goods = (BagGoods)bagGoodsMap.values().stream().filter(e -> Integer.valueOf(key2).equals(Integer.valueOf(e.getPos()))).findFirst().orElse(null);
                if (goods == null) {
                    return "材料数量不足！";
                }
                if ((int)index == 0) {
                    index = Integer.valueOf(this.getFiveLevel(map, Integer.valueOf(goods.getSerial())));
                    if ((int)index == -1) {
                        return "材料数量不足！";
                    }
                    list = (List)map.get(index);
                }
                if (!list.contains(Integer.valueOf(goods.getSerial()))) {
                    return "材料数量不足！";
                }
                Integer bagGoodsNum = this.bagService.getBagGoodsNum(role, Integer.valueOf(goods.getSerial()));
                if (bagGoodsNum == null || (int)bagGoodsNum < anInt) {
                    return "材料数量不足！";
                }
                continue;
            }
            for (String key2 : nutMap.keySet()) {
                Integer integer = this.bagService.consumeBagGoodsByPos(role, nutMap.getInt(key2), (int)Integer.valueOf(key2), true, "五行书熔炼");
                if (integer != null) {
                    return "非法操作";
                }
                continue;
            }
            List<Integer> integers = (List)map.get(Integer.valueOf((int)index + 1));
            Random random = new Random();
            Integer serial = (Integer)integers.get(random.nextInt(integers.size()));
            MessagePusher.pushMessage(role, new RespHint(12, (short)1));
            this.bagService.addGoods(role, (int)serial, 1, 0, true, true, "五行书熔炼");
            SysDataStuff stuff = (SysDataStuff)ItemDataPool.data_stuff.get(serial);
            return MessageFormat.format("灵力流转，你获得了1个{0}。", stuff.getName());
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public int getFiveLevel(Map<Integer, List<Integer>> map, Integer serial) {
        for (Integer key : map.keySet()) {
            if ((int)key > 2) {
                return -1;
            }
            List<Integer> list = (List)map.get(key);
            if (list.contains(serial)) {
                return (int)key;
            }
            continue;
        }
        return -1;
    }
    
    public Map<Integer, List<Integer>> getFiveData() {
        Map<Integer, List<Integer>> map = new HashMap();
        map.put(Integer.valueOf(1), Arrays.asList(Integer.valueOf(75001), Integer.valueOf(75002), Integer.valueOf(75003), Integer.valueOf(75004), Integer.valueOf(75005)));
        map.put(Integer.valueOf(2), Arrays.asList(Integer.valueOf(75006), Integer.valueOf(75007), Integer.valueOf(75008), Integer.valueOf(75009), Integer.valueOf(75010)));
        map.put(Integer.valueOf(3), Arrays.asList(Integer.valueOf(75011), Integer.valueOf(75012), Integer.valueOf(75013), Integer.valueOf(75014), Integer.valueOf(75015)));
        return map;
    }
}
