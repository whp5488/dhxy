package com.kitty.system.game.activity.service;

import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.activity.domain.WeekTreasure;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GamePlayService {
  @Autowired
  private Dao dao;
  
  public Map<Long, WeekTreasure> getWeekTreasure(long id) {
    Map<Long, WeekTreasure> map = (Map<Long, WeekTreasure>)DataCache.WEEK_TREASURE.get(id);
    if (map == null) {
      List<WeekTreasure> list = this.dao.query(WeekTreasure.class, (Condition)Cnd.where("r_id", "=", Long.valueOf(id)));
      map = (Map<Long, WeekTreasure>)list.stream().collect(Collectors.toMap(WeekTreasure::getId, weekTreasure -> weekTreasure));
      DataCache.WEEK_TREASURE.put(Long.valueOf(id), map);
      return map;
    } 
    return map;
  }
}
