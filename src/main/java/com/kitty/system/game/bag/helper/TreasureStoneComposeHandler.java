package com.kitty.system.game.bag.helper;

import java.util.Map;
import java.util.Iterator;
import com.kitty.system.game.onlinemall.service.MallService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.init.domain.SysGiftItemOpen;
import java.util.stream.Collectors;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.List;
import com.kitty.system.game.onlinemall.message.RespTreasureStoneCompose;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.activity.service.GamePlayService;
import com.kitty.system.game.activity.domain.WeekTreasure;
import com.kitty.common.thread.IdWorker;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("TreasureStoneCompose")
public class TreasureStoneComposeHandler extends BagHandler
{
    private static final String LACK = "少侠未放入原石，原石可通过天天挖宝礼包获得";
    private static final int[] NEED;
    
    public TreasureStoneComposeHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        NutMap nutMap = null;
        try {
            nutMap = (NutMap)Json.fromJson(NutMap.class, param);
        }
        catch (Exception e) {
            return null;
        }
        boolean flag = true;
        int count = 0;
        for (String key : nutMap.keySet()) {
            Integer index = Integer.valueOf(key);
            if ((int)index < 0 || (int)index > 2) {
                return null;
            }
            count += nutMap.getInt(key);
            if (this.bagService.getBagGoods(role, TreasureStoneComposeHandler.NEED[(int)index], nutMap.getInt(key)) != null) {
                flag = false;
            }
            else {
                continue;
            }
        }
        if (count <= 0 || count > 3 || !flag) {
            return "少侠未放入原石，原石可通过天天挖宝礼包获得";
        }
        short share = 2;
        int serial = 30026;
        if (nutMap.size() == 1 && count >= 3) {
            share = 6;
            serial = 30028;
        }
        else if (nutMap.size() == 1 && count >= 2) {
            share = 4;
            serial = 30027;
        }
        for (String key2 : nutMap.keySet()) {
            if (this.bagService.consumeBagGoods(role, TreasureStoneComposeHandler.NEED[(int)Integer.valueOf(key2)], nutMap.getInt(key2), true, true, "合成秘宝") != null) {
                return null;
            }
            continue;
        }
        WeekTreasure weekTreasure = new WeekTreasure(Long.valueOf(new IdWorker().nextId()), (long)role.getId(), serial);
        weekTreasure.setShare(share);
        weekTreasure.save();
        Map<Long, WeekTreasure> map = ((GamePlayService)SpringUtils.getBean(GamePlayService.class)).getWeekTreasure((long)role.getId());
        map.put(weekTreasure.getId(), weekTreasure);
        RespTreasureStoneCompose treasureStoneCompose = new RespTreasureStoneCompose();
        treasureStoneCompose.setSerial(serial);
        treasureStoneCompose.setId(String.valueOf(weekTreasure.getId()));
        List<SysGiftItemOpen> supremeList = (List)ItemDataPool.data_gift_item_open.stream().filter(item -> item.getGiftSerial().equals(Integer.valueOf(weekTreasure.getSerial()))).collect(Collectors.toList());
        for (SysGiftItemOpen obj : supremeList) {
            treasureStoneCompose.getList().add(new FieldProp(obj.getSerial(), obj.getNum()));
        }
        MessagePusher.pushMessage((long)role.getId(), treasureStoneCompose);
        ((MallService)SpringUtils.getBean(MallService.class)).openTreasure(role);
        return null;
    }
    
    static {
        NEED = new int[] { 90058, 90059, 90060 };
    }
}
