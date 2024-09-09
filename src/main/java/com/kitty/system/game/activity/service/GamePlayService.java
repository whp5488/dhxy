package com.kitty.system.game.activity.service;

import java.util.List;
import java.util.stream.Collectors;
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
    
    public GamePlayService() {
    }
    
    public Map<Long, WeekTreasure> getWeekTreasure(long id) {
        Map<Long, WeekTreasure> map = (Map)DataCache.WEEK_TREASURE.get(Long.valueOf(id));
        if (map == null) {
            List<WeekTreasure> list = this.dao.query(WeekTreasure.class, Cnd.where("r_id", "=", Long.valueOf(id)));
            map = (Map)list.stream().collect(Collectors.toMap(WeekTreasure::getId, WeekTreasure -> WeekTreasure));
            DataCache.WEEK_TREASURE.put(Long.valueOf(id), map);
            return map;
        }
        return map;
    }
}
