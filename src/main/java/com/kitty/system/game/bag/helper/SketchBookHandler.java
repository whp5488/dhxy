package com.kitty.system.game.bag.helper;

import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("SketchBook")
public class SketchBookHandler extends BagHandler {
  private static final String LACK = "缺少见闻录{0}篇X{1}";
  
  private static final int SERIAL = 90033;
  
  private static final String[] DESC = new String[] { "下", "上", "中" };
  
  private static final int[] NEED = new int[] { 75051, 75049, 75050 };
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    if (!this.bagService.checkPos(role, 90033, num.intValue()))
      return null; 
    int i;
    for (i = 0; i < NEED.length; i++) {
      Integer bagGoods = this.bagService.getBagGoods(role, NEED[i], num.intValue());
      if (bagGoods != null)
        return MessageFormat.format("缺少见闻录{0}篇X{1}", new Object[] { DESC[i], bagGoods }); 
    } 
    for (i = 0; i < num.intValue(); ) {
      boolean b = this.bagService.checkPos(role, 90033, 1);
      if (b) {
        int bd = 0;
        Map<Long, BagGoods> bagGoods = this.bagService.getBagGoods(role);
        int j;
        for (j = 0; j < NEED.length; j++) {
          int finalJ = j;
          List<BagGoods> collect = (List<BagGoods>)bagGoods.values().stream().filter(e -> (e.getSerial() == NEED[finalJ])).collect(Collectors.toList());
          if (((BagGoods)collect.get(0)).getRests().getInt(String.valueOf(2), 0) > 0) {
            bd = 2;
            break;
          } 
        } 
        for (j = 0; j < NEED.length; j++)
          this.bagService.consumeBagGoods(role, NEED[j], 1, true, true, "见闻录合成"); 
        this.bagService.addGoods(role, 90033, 1, bd, true, false, "见闻录合成");
        i++;
      } 
    } 
    return null;
  }
}
