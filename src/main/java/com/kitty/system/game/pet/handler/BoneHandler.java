package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("BoneHandler")
public class BoneHandler extends FeedPetHandler {
  private static final Integer GOODS_SERIAL = Integer.valueOf(75025);
  
  private static final String SUCCESS = "召唤兽#<G>{0}#的成长率从{1}提高到{2}";
  
  private static final String FAIL = "#<G>{0}#{1}转{2}级可吃龙之骨数量已达上限";
  
  private static final String ERROR = "背包没有龙骨";
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(GOODS_SERIAL);
    if (item != null) {
      int level = pet.getPetLv().intValue();
      int bone = pet.getBone().intValue();
      int rebirth = pet.getRebirth().intValue();
      double grow_up = pet.getPetBase().getGrow_up().intValue() / 1000.0D;
      double gain = item.getValue().intValue() / 1000.0D;
      double current = grow_up + gain * bone;
      boolean check = check(level, rebirth, pet.getBone().intValue());
      if (!check)
        return MessageFormat.format("#<G>{0}#{1}转{2}级可吃龙之骨数量已达上限", new Object[] { pet.getPetName(), Integer.valueOf(rebirth), Integer.valueOf(level) }); 
      Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, GOODS_SERIAL.intValue(), 1, true, true, "使用龙骨");
      if (goods != null)
        return "背包没有龙骨"; 
      pet.setBone(Integer.valueOf(bone + 1));
      this.petService.calculateBase(role.getId().longValue(), pet);
      this.petService.getArenaScore(role.getId().longValue(), pet);
      this.petService.loadPet(role, pet);
      pet.save();
      return MessageFormat.format("召唤兽#<G>{0}#的成长率从{1}提高到{2}", new Object[] { pet.getPetName(), Double.valueOf(current), Double.valueOf(current + gain) });
    } 
    return null;
  }
  
  public boolean check(int lv, int rebirth, int num) {
    boolean flag = false;
    if (rebirth >= 3 && lv >= 160 && num < 18) {
      flag = true;
    } else if (rebirth >= 3 && lv >= 140 && num < 15) {
      flag = true;
    } else if (rebirth >= 3 && lv >= 120 && num < 12) {
      flag = true;
    } else if (rebirth >= 2 && num < 7) {
      flag = true;
    } else if (rebirth >= 1 && num < 4) {
      flag = true;
    } else if (rebirth >= 0 && num < 2) {
      flag = true;
    } 
    return flag;
  }
}
