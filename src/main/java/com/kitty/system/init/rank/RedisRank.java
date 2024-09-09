package com.kitty.system.init.rank;

import java.util.LinkedHashMap;
import java.util.Iterator;
import com.kitty.system.enums.common.ADORN;
import java.io.Serializable;
import com.kitty.system.game.ranking.domain.RankEquip;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cache.RedisService;
import com.kitty.system.game.role.domain.Role;
import java.util.Map;

public class RedisRank
{
    public RedisRank() {
    }
    
    public static void initRank(Map<Long, Role> map) {
        RedisService redisService = (RedisService)SpringUtils.getBean(RedisService.class);
        WearEquip equip = null;
        for (WearEquip wearEquip : RankingList.wearEquips) {
            Role role = (Role)map.get(Long.valueOf(wearEquip.getRId()));
            equip = wearEquip;
            redisService.zAdd("rank:equip:" + wearEquip.getPosition(), (double)DataGlobalFunc.getEquipScore(wearEquip), new RankEquip(wearEquip.getRId(), wearEquip.getType(), role.getName()));
        }
        redisService.zRem("rank:equip:" + equip.getPosition(), new RankEquip(equip.getRId(), equip.getType(), ((Role)map.get(Long.valueOf(equip.getRId()))).getName()));
        LinkedHashMap<RankEquip, Double> rangeWithScore = redisService.zReverseRangeWithScore("rank:equip" + ADORN.WEAPON, RankEquip.class, 0, 100);
        for (Map.Entry<RankEquip, Double> entry : rangeWithScore.entrySet()) {
            System.out.println(entry.getValue() + "---" + entry.getKey());
        }
    }
}
