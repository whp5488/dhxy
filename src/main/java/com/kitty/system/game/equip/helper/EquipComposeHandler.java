package com.kitty.system.game.equip.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSinBieEq;
import java.text.MessageFormat;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("EquipCompose")
public class EquipComposeHandler extends UpgradeHandler {
  public static final int SERIAL_M = 75042;
  
  public static final int SERIAL_M_NUM = 50;
  
  public static final int SERIAL_S = 75031;
  
  public static final int SERIAL_S_NUM = 40;
  
  public static final long RESTYPE_SILVER = 10000000L;
  
  public static final String SUCCESS = "你获得了{0}";
  
  public static final String NOT_STUFF = "缺少神兵碎片×{0}";
  
  public String acceptUpgrade(Role role, Integer position, Integer type, String param, String cause) {
    int pos = getVacancy(role);
    if (pos <= 0)
      return "很抱歉，你的包裹空位不足，请整理后再来吧。"; 
    Random random = new Random();
    if (type.equals(Integer.valueOf(1))) {
      Integer count = Integer.valueOf(param);
      for (int i = 0; i < count.intValue(); i++) {
        pos = getVacancy(role);
        if (pos <= 0)
          return "很抱歉，你的包裹空位不足，请整理后再来吧。"; 
        Integer num = this.bagService.consumeBagGoods(role, 75042, 50, true, true, "装备合成-神兵合成");
        if (num != null)
          return MessageFormat.format("缺少神兵碎片×{0}", new Object[] { num }); 
        List<SysMagicalBieEq> lowMagicalBieEqList = EquipDataPool.lowMagicalBieEq;
        int nextInt = random.nextInt(lowMagicalBieEqList.size());
        SysMagicalBieEq magicalBieEq = lowMagicalBieEqList.get(nextInt);
        BagEquip equip = EquipManage.createEquip(Integer.valueOf(magicalBieEq.getSerial()));
        if (equip != null) {
          BagEquip eq = this.equipService.addEquip(role, equip, pos, 0, "合成神兵");
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(MessageFormat.format("你获得了{0}", new Object[] { String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(equip.getType()), eq.getId(), role.getId(), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }) })));
          role.save();
        } else {
          return "操作的类型不正确";
        } 
      } 
    } else if (type.equals(Integer.valueOf(2))) {
      boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 10000000L);
      if (!b)
        return null; 
      Integer num = this.bagService.consumeBagGoods(role, 75031, 40, true, true, "装备合成-仙器合成");
      if (num != null)
        return "八荒遗风不足，不能合成仙器"; 
      boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 10000000L, "装备合成");
      if (!flag) {
        this.logger.error("仙器合成可能违规操作扣除银两失败，角色 = {}", role.getRoleId());
        return null;
      } 
      if (position.intValue() < ADORN.WEAPON.getValue() || position.intValue() > ADORN.SHOES.getValue())
        return "请选择部位再合成仙器"; 
      List<SysSinBieEq> list = (List<SysSinBieEq>)EquipDataPool.lowSinBieEq.stream().filter(bieEq -> (position.intValue() == bieEq.getPos() && (bieEq.getSex() == 0 || bieEq.getSex() == role.getSex().intValue()) && (bieEq.getOwnerRole().contains(Integer.valueOf(0)) || bieEq.getOwnerRole().contains(role.getSerial()) || bieEq.getOwnerRole().contains(role.getRace())) && (bieEq.getRace() == 0 || role.getRace().intValue() == bieEq.getRace()))).collect(Collectors.toList());
      int nextInt = random.nextInt(list.size());
      SysSinBieEq bieEq = list.get(nextInt);
      BagEquip equip = EquipManage.createEquip(Integer.valueOf(bieEq.getSerial()));
      if (equip != null) {
        BagEquip eq = this.equipService.addEquip(role, equip, pos, 0, "合成仙器");
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(MessageFormat.format("你获得了{0}", new Object[] { String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(equip.getType()), eq.getId(), role.getId(), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }) })));
      } else {
        return "操作的类型不正确";
      } 
    } 
    return null;
  }
}
