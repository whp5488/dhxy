package com.kitty.system.game.horse.handler;

import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.role.domain.Role;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("HorseGrowUpHandler")
public class HorseGrowUpHandler extends FeedHorseHandler {
  private static final int SERIAL = 75046;
  
  private static final String LACK = "你的坐骑#<G>{0}#使用#<G>筋骨提气丸#已上限，请先提升坐骑等级";
  
  private static final String SUCCESS = "你的坐骑#<G>{0}#的成长率由#<G>{1}#提升到#<G>{2}#";
  
  public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
    byte growthDan = horse.getGrowthDan();
    byte limit = HorseDataPool.getGrowthDanLimit(horse.getLevel());
    if (growthDan >= limit)
      return MessageFormat.format("你的坐骑#<G>{0}#使用#<G>筋骨提气丸#已上限，请先提升坐骑等级", new Object[] { horse.getName() }); 
    if (this.bagService.getBagGoods(role, 75046, 1) != null)
      return null; 
    role.save();
    Integer integer = this.bagService.consumeBagGoods(role, 75046, 1, true, true, "使用筋骨提气丸");
    if (integer != null) {
      this.logger.error("扣除道具筋骨提气丸失败，玩家编号：{}", role.getRoleId());
      return null;
    } 
    double score = horse.getGrowUp() / 1000.0D + growthDan * 0.1D;
    double newScore = horse.getGrowUp() / 1000.0D + (growthDan + 1) * 0.1D;
    horse.setGrowthDan((byte)(growthDan + 1));
    horse.setScore(this.horseService.score(horse));
    horse.save();
    this.horseService.refresh(role, horse);
    this.horseService.calculateControlPet(role, horse.getId().longValue());
    return MessageFormat.format("你的坐骑#<G>{0}#的成长率由#<G>{1}#提升到#<G>{2}#", new Object[] { horse.getName(), Double.valueOf(score), Double.valueOf(newScore) });
  }
}
