package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("IntimateHandler")
public class IntimateHandler extends FeedPetHandler {
  private static final String SUCCESS = "消耗了[亲密丹]x{0}，你的召唤兽#<G>{1}#的亲密度增加了#<G>{2}#";
  
  private static final String ERROR = "背包没有亲密丹";
  
  private static final String WARN = "亲密度已达到上限";
  
  private static final int GOODS_SERIAL = 75019;
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    try {
      count = Math.max(1, count);
      SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(75019));
      if (item != null) {
        int value = item.getValue().intValue();
        int limit = ((Integer)pet.getCloseLimitMap().get("close_dan")).intValue();
        if (limit >= 100000)
          return "该宠物亲密丹使用已到达上限"; 
        if (pet.getPetClose() > 2000000000L)
          return "亲密度已达到上限"; 
        if (count + limit > 100000)
          count = 100000 - limit; 
        Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 75019, count, true, true, "宠物使用亲密丹");
        if (goods != null)
          return "背包没有亲密丹"; 
        long close = pet.getPetClose() + (value * count);
        pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
        this.petService.getArenaScore(role.getId().longValue(), pet);
        this.petService.loadPet(role, pet);
        pet.getCloseLimitMap().put("close_dan", Integer.valueOf(limit + count));
        pet.save();
        return MessageFormat.format("消耗了[亲密丹]x{0}，你的召唤兽#<G>{1}#的亲密度增加了#<G>{2}#", new Object[] { Integer.valueOf(count), pet.getPetName(), Integer.valueOf(value * count) });
      } 
      return null;
    } catch (Exception e) {
      return null;
    } 
  }
}
