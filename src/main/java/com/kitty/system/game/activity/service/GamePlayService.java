
package com.kitty.system.game.activity.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.activity.domain.WeekTreasure;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class GamePlayService
{
    @Autowired
    private Dao dao;
    
    public Map<Long, WeekTreasure> getWeekTreasure(final long id) {
        Map<Long, WeekTreasure> map = DataCache.WEEK_TREASURE.get(id);
        if (map == null) {
            final List<WeekTreasure> list = this.dao.query(WeekTreasure.class, Cnd.where("r_id", "=", id));
            map = list.stream().collect(Collectors.toMap(WeekTreasure::getId, WeekTreasure -> WeekTreasure));
            DataCache.WEEK_TREASURE.put(id, map);
            return map;
        }
        return map;
    }
}
