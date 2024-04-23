package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipRepair")
public class EquipRepairHandler extends UpgradeHandler {
  public static final String SUCCESS = "{0}，修复了#<R>{1}#";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    List<BagEquip> list = new ArrayList<>();
    Long id = Long.valueOf(param);
    if (pos.intValue() <= 0 && id.longValue() == 0L) {
      List<WearEquip> equips = this.equipService.selectWearEquip(role);
      for (WearEquip equip : equips) {
        EquipInfo equipInfo = getEquip(role, equip.getPos(), equip.getId());
        if (equipInfo.getTips() != null)
          return equipInfo.getTips(); 
        list.add(equipInfo.getEquip());
      } 
    } else {
      EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
      if (equipInfo.getTips() != null)
        return equipInfo.getTips(); 
      BagEquip equip = equipInfo.getEquip();
      list.add(equip);
    } 
    for (BagEquip equip : list) {
      String msg = "";
      long consume = 50000L;
      if (role.getCoin().longValue() < consume) {
        if (role.getSilver().longValue() >= consume - role.getCoin().longValue()) {
          long silver = consume - role.getCoin().longValue();
          this.roleService.deductCurrency(role, Const.RESTYPE_COIN, role.getCoin().longValue(), "EquipRepair");
          this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipRepair");
          msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(consume - silver)) + "，#<IR" + Const.RESTYPE_SILVER + ">#" + MoneyUtil.colours(Long.valueOf(silver));
        } else {
          return "货币不足";
        } 
      } else {
        this.roleService.deductCurrency(role, Const.RESTYPE_COIN, consume, "EquipRepair");
        msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(consume));
      } 
      equip.setField(field(equip.getField(), DataGlobalFunc.getEquipData(Integer.valueOf(equip.getType())).getMaxEndure()));
      MessagePusher.pushMessage(role, (Message)new RespMsg(MessageFormat.format("{0}，修复了#<R>{1}#", new Object[] { msg, equip.getName() })));
      refresh(role, equip, pos.intValue(), id, true);
      this.roleService.calculateBase(role);
      role.save();
      this.roleService.refreshRoleInfo(role, false);
    } 
    return null;
  }
  
  public List<FiedEquip> field(List<FiedEquip> field, int dur) {
    field.forEach(e -> {
          if (e.getType().equals(Integer.valueOf(6672))) {
            e.setValue(Integer.valueOf(dur));
            return;
          } 
        });
    return field;
  }
}
