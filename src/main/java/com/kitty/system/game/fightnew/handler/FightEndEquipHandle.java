package com.kitty.system.game.fightnew.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.service.ActivityHandler;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.fightnew.messages.old.RespEqUpdateDur;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FightEndEquipHandle extends ActivityHandler {
  public void handleFightEnd(FightEndEvent fightEndEvent) {
    String msg = "";
    Role role = fightEndEvent.getRole();
    List<WearEquip> list = SpringUtils.getEquipService().selectWearEquip(role);
    int count = 0;
    boolean flag = false;
    for (WearEquip equip : list) {
      EquipAlike alike = DataGlobalFunc.getEquipData(Integer.valueOf(equip.getType()));
      int dur = DataGlobalFunc.getDur(equip.getField());
      if (alike != null && dur > 0) {
        dur = countDeductDur(role, dur, alike.getMaxEndure());
        List<FiedEquip> field = field(equip.getField(), dur);
        equip.setField(field);
        MessagePusher.pushMessage(role, (Message)new RespEqUpdateDur(String.valueOf(equip.getId()), dur));
        if (dur <= 0) {
          flag = true;
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "装备耐久度为0，" + String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }) + "失效了", true));
        } else if (dur <= alike.getMaxEndure() * 0.25D) {
          msg = msg + String.format("#<CI:%d>%s#、", new Object[] { Integer.valueOf(equip.getType()), equip.getName() });
        } 
        count++;
        if (count >= 5) {
          msg = "穿戴装备耐久度为0，已失效了!";
          break;
        } 
      } 
    } 
    if (flag) {
      RoleService roleService = SpringUtils.getRoleService();
      roleService.calculateBase(role);
      role.save();
      roleService.refreshRoleInfo(role, false);
    } 
    if (!msg.isEmpty() && count < 5) {
      msg = msg.substring(0, msg.length() - 1);
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg + "装备耐久偏低，即将失效。请尽快修理", true));
    } 
    if (role.getLastDead() == 0 && list.size() > 0)
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "由于你的死亡，你的装备损失了2%耐久度", true)); 
  }
  
  public int countDeductDur(Role role, int dur, int max) {
    if (role.getLastDead() == 0) {
      dur = (int)(dur - max * 0.02D);
    } else {
      dur--;
    } 
    return (dur <= 0) ? 0 : dur;
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
