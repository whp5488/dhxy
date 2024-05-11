
package com.kitty.system.game.bag.helper;

import java.util.LinkedHashMap;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.List;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("GemDetach")
public class GemDetachHandler extends BagHandler
{
    private static final String LACK = "该宝石需要大于3颗方可进行降解~";
    private static final String SUCCEED = "消耗#<II%d>##<%s>[%d级%s宝石]#X%d得到了#<II%d>##<LG1>[1级%s宝石]#X%d";
    private static final String[] GEM_TYPE;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(item.getPos())).findFirst().orElse(null);
        if (goods == null) {
            return null;
        }
        final List<Integer> gemList = gemList();
        if (!gemList.contains(goods.getSerial())) {
            return null;
        }
        final String str = String.valueOf(goods.getSerial());
        final int level = Integer.valueOf(str.substring(str.length() - 1));
        if (level < 2 || level > 6) {
            return null;
        }
        int count = DataGlobalFunc.unrestricted(goods.getRests());
        if (count <= 0) {
            return null;
        }
        final int position = goods.getSerial() % 70000 / 100;
        final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        final List<BagEquip> equips = bagEquipMap.values().stream().filter(e -> e.getPosition() == position).collect(Collectors.toList());
        for (final BagEquip equip : equips) {
            count += count(equip.getField(), position, level);
        }
        final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        final WearEquip wearEquip = wearEquips.stream().filter(e -> e.getPosition() == position).findFirst().orElse(null);
        if (wearEquip != null) {
            count += count(wearEquip.getField(), position, level);
        }
        if (count < 3) {
            return "该宝石需要大于3颗方可进行降解~";
        }
        final int lowGemNum = DataGlobalFunc.gem2lowGemNum(goods.getSerial());
        this.bagService.deleteRoleItem(role, pos, "宝石降解");
        final int goodsNum = num * lowGemNum;
        this.bagService.addGoods(role, 70000 + position * 100 + 1, goodsNum, 0, true, false, "宝石降解");
        final SysDataStuff stuff = ItemDataPool.data_stuff.get(goods.getSerial());
        return String.format("消耗#<II%d>##<%s>[%d级%s宝石]#X%d得到了#<II%d>##<LG1>[1级%s宝石]#X%d", goods.getSerial(), DataGlobalFunc.getColours(stuff.getItemPJ(), true), level, GemDetachHandler.GEM_TYPE[position - 1], num, 70000 + position * 100 + 1, GemDetachHandler.GEM_TYPE[position - 1], goodsNum);
    }
    
    public static List<Integer> gemList() {
        final List<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= 5; ++i) {
            for (int j = 1; j <= 7; ++j) {
                list.add(70000 + i * 100 + j);
            }
        }
        return list;
    }
    
    public static int count(final List<FiedEquip> field, final int pos, final int lv) {
        final FiedEquip equip = field.stream().filter(e -> e.getType().equals(6670)).findFirst().orElse(null);
        final Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        int level = 1;
        for (int i = 1; i <= equip.getValue(); ++i) {
            int num = 1;
            final int serial = 70000 + pos * 100 + level;
            final Integer value = map.get(serial);
            if (value != null) {
                num += value;
            }
            map.put(serial, num);
            final int cost = i % 3;
            if (cost == 0) {
                level = ((++level > 7) ? 7 : level);
            }
        }
        return (map.get(lv) == null) ? 0 : map.get(lv);
    }
    
    static {
        GEM_TYPE = new String[] { "红", "绿", "蓝", "紫", "橙" };
    }
}
