package com.kitty.system.game.bag.helper;

import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("GemCurdle")
public class GemCurdleHandler extends BagHandler {
  private static final int SERIAL = 75116;
  
  private static final String LACK = "缺少1级%s宝石X%d";
  
  private static final int LIMIT_LEVEL = 1110;
  
  private static final int[] GEM_SERIAL = new int[] { 70101, 70201, 70301, 70401, 70501 };
  
  private static final String[] GEM_TYPE = new String[] { "红", "绿", "蓝", "紫", "橙" };
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    int curve = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    if (curve < 1110)
      return "请在对应系统开启后再使用此道具~"; 
    if (num.intValue() <= 0)
      return null; 
    int i;
    for (i = 0; i < GEM_SERIAL.length; i++) {
      Integer bagGoods = this.bagService.getBagGoods(role, GEM_SERIAL[i], num.intValue());
      if (bagGoods != null)
        return String.format("缺少1级%s宝石X%d", new Object[] { GEM_TYPE[i], bagGoods }); 
    } 
    if (!this.bagService.checkPos(role, 75116, num.intValue()))
      return null; 
    for (i = 0; i < GEM_SERIAL.length; i++) {
      Integer integer = this.bagService.consumeBagGoods(role, GEM_SERIAL[i], num.intValue(), false, true, "宝石凝合");
      if (integer != null)
        return null; 
    } 
    this.bagService.addGoods(role, 75116, num.intValue(), 0, false, false, "宝石凝合");
    return null;
  }
}
