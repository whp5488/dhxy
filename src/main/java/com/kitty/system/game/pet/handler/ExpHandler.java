package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.text.MessageFormat;
import org.springframework.stereotype.Component;

@Component("ExpHandler")
public class ExpHandler extends FeedPetHandler {
  private static final String SUCCESS = "消耗了{0}x1，你的#<G>{1}#获得了#<IR12>##<G>{2}#";
  
  private static final String ERROR = "背包没有{0}";
  
  private static final String TIPS = "#<MP:0,M:1,MT:%d,CI:%d}>[%s]#";
  
  private static final int GOODS_SERIAL_L = 75021;
  
  private static final int GOODS_SERIAL_S = 75022;
  
  private static final int GOODS_SERIAL_D = 75023;
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    int roleGrade = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    int petGrade = pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue();
    SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(pet.getRebirth().intValue() + 1));
    if (sysRbattr != null) {
      Integer lvLimit = sysRbattr.getLvLimit();
      if (petGrade - roleGrade >= 10)
        return String.format("%s超过你的%d级%d级", new Object[] { pet.getPetName(), role.getLevel(), Integer.valueOf(10) }); 
      if (pet.getPetLv().intValue() >= lvLimit.intValue() && pet.getExp() >= 1500000000L)
        return String.format("%s召唤兽已到达%d转的上限%d级", new Object[] { pet.getPetName(), pet.getRebirth(), lvLimit }); 
    } else {
      return "经验值已上限";
    } 
    int serial = 0;
    if (param.intValue() == 1) {
      serial = 75021;
    } else if (param.intValue() == 2) {
      serial = 75022;
    } else if (param.intValue() == 3) {
      serial = 75023;
    } 
    SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(serial));
    if (serial > 0 && item != null) {
      Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, serial, 1, true, true, "宠物经验丹");
      if (goods != null)
        return MessageFormat.format("背包没有{0}", new Object[] { item.getName() }); 
      Integer exp = item.getValue();
      boolean flag = this.petService.addExp(role, pet, Long.valueOf(exp.intValue()).longValue(), false);
      if (flag) {
        String format = String.format("#<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { item.getSerial(), item.getSerial(), item.getName() });
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("消耗了{0}x1，你的#<G>{1}#获得了#<IR12>##<G>{2}#", new Object[] { format, pet.getPetName(), exp }), true));
      } 
      return null;
    } 
    return "未找到该道具";
  }
}
