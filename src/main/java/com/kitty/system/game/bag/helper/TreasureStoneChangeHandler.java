
package com.kitty.system.game.bag.helper;

import java.util.Arrays;
import java.util.Iterator;
import com.kitty.system.common.message.RespHint;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import java.util.Collection;
import com.kitty.common.utils.ArraysUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("TreasureStoneChange")
public class TreasureStoneChangeHandler extends BagHandler
{
    private static final String LACK = "少侠所拥有的宝石种类数量不足两款，无法熔炼";
    private static final List<Integer> NEED;
    private static final long SILVER = 100000L;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        NutMap nutMap = null;
        try {
            nutMap = (NutMap)Json.fromJson(NutMap.class, (CharSequence)param);
        }
        catch (final Exception e) {
            return null;
        }
        final ArrayList<Integer> list = new ArrayList<Integer>();
        boolean flag = true;
        int count = 0;
        for (final String key : nutMap.keySet()) {
            final Integer index = Integer.valueOf(key);
            if (index < 0 || index > 2) {
                return null;
            }
            list.add(TreasureStoneChangeHandler.NEED.get(index));
            count += nutMap.getInt(key);
            if (this.bagService.getBagGoods(role, TreasureStoneChangeHandler.NEED.get(index), nutMap.getInt(key)) == null) {
                continue;
            }
            flag = false;
        }
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 100000L)) {
            return null;
        }
        if (count < 2 || count > 2 || !flag) {
            return "少侠所拥有的宝石种类数量不足两款，无法熔炼";
        }
        if (this.bagService.getPos(role, false) < 0) {
            return "你的物品栏已满";
        }
        if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 100000L, "原石转换")) {
            return null;
        }
        for (final String key : nutMap.keySet()) {
            if (this.bagService.consumeBagGoods(role, TreasureStoneChangeHandler.NEED.get(Integer.valueOf(key)), nutMap.getInt(key), true, true, "原石转换") != null) {
                return null;
            }
        }
        final List<Integer> objects = new ArrayList<Integer>();
        if (nutMap.size() == 2) {
            final int index2 = RandomUtil.round(0, TreasureStoneChangeHandler.NEED.size() - 1);
            objects.add(TreasureStoneChangeHandler.NEED.get(index2));
            final List<Integer> notMatchList = new ArrayList<Integer>(ArraysUtil.getDifferListByMapPlus(TreasureStoneChangeHandler.NEED, list));
            if (notMatchList != null && notMatchList.size() > 0) {
                objects.add(notMatchList.get(0));
            }
            else {
                objects.add(TreasureStoneChangeHandler.NEED.get(RandomUtil.round(0, TreasureStoneChangeHandler.NEED.size() - 1)));
            }
        }
        else {
            objects.add(TreasureStoneChangeHandler.NEED.get(RandomUtil.round(0, TreasureStoneChangeHandler.NEED.size() - 1)));
            objects.add(TreasureStoneChangeHandler.NEED.get(RandomUtil.round(0, TreasureStoneChangeHandler.NEED.size() - 1)));
        }
        MessagePusher.pushMessage(role.getId(), new RespMsg("转换成功"));
        MessagePusher.pushMessage(role.getId(), new RespHint(30, (short)1));
        for (final Integer serial : objects) {
            String name = "红色原石";
            if (serial == 90059) {
                name = "蓝色原石";
            }
            else if (serial == 90060) {
                name = "黄色原石";
            }
            this.bagService.addGoods(role, serial, 1, 0, false, true, "原石转换");
            MessagePusher.pushMessage(role.getId(), new RespMsg("获得#<LB1>" + name + "#1个"));
        }
        return null;
    }
    
    static {
        NEED = Arrays.asList(90058, 90059, 90060);
    }
}
