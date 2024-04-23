package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.RandomUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespAcsryEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataAcsryIdentify;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("AcsryAuth")
public class AcsryAuthHandler extends UpgradeHandler {
  public static final List<Integer> integers = Arrays.asList(new Integer[] { Integer.valueOf(92), Integer.valueOf(7), Integer.valueOf(1) });
  
  public static final String NOT_FIND_STUFF = "{0}数量不足";
  
  public String acceptUpgrade(Role role, Integer position, Integer type, String param, String cause) {
    int pos = this.bagService.getPos(role, false);
    if (pos <= 0)
      return "你的物品栏已满"; 
    Map<Long, BagGoods> bagGoods = this.bagService.getBagGoods(role);
    BagGoods goods = bagGoods.values().stream().filter(e -> position.equals(Integer.valueOf(e.getPos()))).findFirst().orElse(null);
    if (goods == null)
      return null; 
    SysDataAcsryEgg acsryEgg = (SysDataAcsryEgg)ItemDataPool.acsryEggMap.get(Integer.valueOf(goods.getSerial()));
    if (acsryEgg == null) {
      MessagePusher.pushMessage(role, (Message)new RespHint(22, (short)0));
      return "该道具无法鉴定";
    } 
    SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(acsryEgg.getHatchType()));
    if (acsry == null) {
      MessagePusher.pushMessage(role, (Message)new RespHint(22, (short)0));
      return "没有该配饰信息";
    } 
    SysDataAcsryIdentify identify = (SysDataAcsryIdentify)EquipDataPool.acsryAIdentifyMap.get(acsry.getSuiteType());
    if (identify == null) {
      MessagePusher.pushMessage(role, (Message)new RespHint(22, (short)0));
      return "道具扣除错误";
    } 
    if (identify.getSilver() > 0L && 
      !this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, identify.getSilver()))
      return null; 
    for (Item item : identify.getList()) {
      Integer num = this.bagService.getBagGoods(role, item.getItem().intValue(), item.getNum().intValue());
      if (num != null) {
        ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(item.getItem());
        if (info != null)
          return MessageFormat.format("{0}数量不足", new Object[] { info.getName() }); 
        return "鉴定道具数量不足";
      } 
    } 
    boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, identify.getSilver(), "配饰鉴定");
    if (!currency) {
      this.logger.error("配饰鉴定扣除货币异常：玩家编号 = {}，鉴定道具编号：{}", role.getRoleId(), Integer.valueOf(goods.getSerial()));
      return null;
    } 
    for (Item item : identify.getList()) {
      Integer integer = this.bagService.consumeBagGoods(role, item.getItem().intValue(), item.getNum().intValue(), true, true, "装备打造");
      if (integer != null) {
        this.logger.error("配饰鉴定扣除道具异常：玩家编号 = {}，鉴定道具编号：{}", role.getRoleId(), Integer.valueOf(goods.getSerial()));
        return null;
      } 
    } 
    BagEquip equip = MakeAttribute.createBaseAcsry(acsry, pos, integers);
    if (equip == null) {
      this.logger.error("鉴定配饰错误，配饰编号：{}", Integer.valueOf(goods.getSerial()));
      MessagePusher.pushMessage(role, (Message)new RespHint(22, (short)0));
    } 
    equip.setId(Long.valueOf(this.worker.nextId()));
    equip.setRId(role.getId().longValue());
    byte productOrder = getProductOrder();
    equip.setProductOrder(productOrder);
    this.bagService.deleteRoleItem(role, position.intValue(), "配饰鉴定");
    this.bagService.addEquip(role, equip, false, "配饰鉴定");
    MessagePusher.pushMessage(role, (Message)new RespAcsryEquip(equip));
    return null;
  }
  
  public static byte getProductOrder() {
    byte productOrder = ACC_GRADE.PLAY_WITH.getValue();
    int round = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(1000));
    if (round <= 50) {
      productOrder = ACC_GRADE.PRICELESS.getValue();
    } else if (round <= 150) {
      productOrder = ACC_GRADE.RESERVE.getValue();
    } 
    return productOrder;
  }
}
