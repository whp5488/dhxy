
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
    public static void initRank(final Map<Long, Role> map) {
        final RedisService redisService = SpringUtils.getBean(RedisService.class);
        WearEquip equip = null;
        for (final WearEquip wearEquip : RankingList.wearEquips) {
            final Role role = map.get(wearEquip.getRId());
            equip = wearEquip;
            redisService.zAdd("rank:equip:" + wearEquip.getPosition(), DataGlobalFunc.getEquipScore(wearEquip), new RankEquip(wearEquip.getRId(), wearEquip.getType(), role.getName()));
        }
        redisService.zRem("rank:equip:" + equip.getPosition(), new RankEquip(equip.getRId(), equip.getType(), map.get(equip.getRId()).getName()));
        final LinkedHashMap<RankEquip, Double> rangeWithScore = redisService.zReverseRangeWithScore("rank:equip" + ADORN.WEAPON, RankEquip.class, 0, 100);
        for (final Map.Entry<RankEquip, Double> entry : rangeWithScore.entrySet()) {
            System.out.println(entry.getValue() + "---" + entry.getKey());
        }
    }
}
