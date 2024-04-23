package com.kitty.system.game.horse.handler;

import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.ItemEasyInfo;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("HorseExpUpHandler")
public class HorseExpUpHandler extends FeedHorseHandler {
  private static final int SERIAL = 75115;
  
  private static final String SUCCESS = "你的坐骑#<LG1>{0}#获得了#<LG1>{1}#经验";
  
  public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
    if (this.bagService.getBagGoods(role, 75115, 1) != null)
      return null; 
    ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(75115));
    if (easyInfo == null)
      return "没要找到该物品配置信息"; 
    long exp = easyInfo.getValue().longValue();
    short limit = HorseDataPool.getGrowthDanLimit(role.getRelive().intValue() * 1000 + role.getLevel().intValue());
    if (horse.getLevel() >= limit)
      return "坐骑等级已上限，请先提升你的等级"; 
    Integer integer = this.bagService.consumeBagGoods(role, 75115, 1, true, true, "坐骑修炼丹");
    if (integer != null) {
      this.logger.error("扣除道具坐骑修炼丹失败，玩家编号：{}", role.getRoleId());
      return null;
    } 
    role.save();
    this.horseService.addExp(role, horse, exp);
    return MessageFormat.format("你的坐骑#<LG1>{0}#获得了#<LG1>{1}#经验", new Object[] { horse.getName(), Long.valueOf(exp) });
  }
}
