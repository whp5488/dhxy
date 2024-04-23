package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.text.Convert;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.ItemEasyInfo;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("EquipGem")
public class EquipGemHandler extends UpgradeHandler {
  private static final String LACK = "缺少%sX%d";
  
  public static final String NOT_FIND_STUFF = "材料数量不足";
  
  public static final String SUCCESS_INLAY = "恭喜，镶嵌成功";
  
  public static final String FIND_GEM_MAX = "宝石镶嵌已达最大数量";
  
  public static final String FIND_GEM_MIN = "请先镶嵌一颗宝石吧";
  
  public static final List<Integer> gem_tips = Arrays.asList(new Integer[] { Integer.valueOf(16), Integer.valueOf(20) });
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    try {
      Long id = Long.valueOf(param);
      EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
      if (equipInfo.getTips() != null)
        return equipInfo.getTips(); 
      BagEquip equip = equipInfo.getEquip();
      int bn = 0;
      Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
      if (first.isPresent()) {
        FiedEquip fiedEquip = first.get();
        bn = fiedEquip.getValue().intValue();
      } 
      Map<Integer, Integer> map = new LinkedHashMap<>();
      EquipAlike equipData = DataGlobalFunc.getEquipData(Integer.valueOf(equip.getType()));
      if (type.intValue() == 0) {
        if (equipData != null) {
          if (bn >= equipData.getMaxEmbedGemCnt())
            return "宝石镶嵌已达最大数量"; 
          Integer serial = DataGlobalFunc.getEquipInlayGemSerial(equip.getType(), equip.getPosition(), bn);
          Integer goods = this.bagService.getBagGoodsNum(role, serial);
          boolean flag = false;
          if (goods.intValue() >= 1) {
            flag = true;
            map.put(serial, Integer.valueOf(1));
          } else {
            int temp = serial.intValue() % 10000;
            int level = temp % 100;
            if (level > 1) {
              int subType = temp / 100;
              int needNum = DataGlobalFunc.gem2lowGemNum(serial.intValue());
              int typeId = 70000 + subType * 100;
              int count = 0;
              for (int i = 1; i < level; i++) {
                int gemSerial = typeId + level - i;
                Integer gemNum = this.bagService.getBagGoodsNum(role, Integer.valueOf(gemSerial));
                if (gemNum.intValue() > 0) {
                  int num = DataGlobalFunc.gem2lowGemNum(gemSerial) * gemNum.intValue();
                  count += num;
                  if (num > needNum) {
                    flag = true;
                    int needed = needNum;
                    if (level - i > 1) {
                      needed = needNum / DataGlobalFunc.gem2lowGemNum(gemSerial);
                    } else {
                      needed = needNum;
                    } 
                    map.put(Integer.valueOf(gemSerial), Integer.valueOf(needed));
                    break;
                  } 
                  map.put(Integer.valueOf(gemSerial), gemNum);
                  if (count >= needNum) {
                    flag = true;
                    break;
                  } 
                  needNum -= count;
                } 
              } 
            } 
          } 
          if (flag) {
            for (Integer key : map.keySet()) {
              Integer bagGoods = this.bagService.consumeBagGoods(role, key.intValue(), ((Integer)map.get(key)).intValue(), false, true, "镶嵌宝石");
              if (bagGoods != null) {
                ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(key);
                if (easyInfo != null) {
                  MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), String.format("缺少%sX%d", new Object[] { easyInfo.getName(), bagGoods }), true));
                } else {
                  MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("扣除宝石缺少数量：{0}", new Object[] { bagGoods }), true));
                } 
                return null;
              } 
            } 
            int[] num = { 0 };
            equip.getField().forEach(fiedEquip -> {
                  if (fiedEquip.getType().equals(Integer.valueOf(6670))) {
                    fiedEquip.setValue(Integer.valueOf(fiedEquip.getValue().intValue() + 1));
                    num[0] = fiedEquip.getValue().intValue();
                  } 
                });
            equip = EquipUtils.gemChange(equipData, equip);
            refresh(role, equip, pos.intValue(), id, true);
            MessagePusher.pushMessage(role, (Message)new RespHint(0, (short)1));
            MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "恭喜，镶嵌成功", false));
            if (gem_tips.contains(Integer.valueOf(num[0])))
              MessagePusher.notify2Lb(role, 600, new Object[] { role.getName(), Convert.getChineseFromArabic(Integer.valueOf(num[0])), String.format("#<MP:0,M:1,MT:%d,CI:%d}>%s#", new Object[] { Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }) }); 
          } else {
            MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "材料数量不足", false));
          } 
        } 
      } else {
        if (bn < 1)
          return "请先镶嵌一颗宝石吧"; 
        int level = 1;
        for (int i = 1; i <= bn; i++) {
          int num = 1;
          int serial = 70000 + equip.getPosition() * 100 + level;
          Integer value = map.get(Integer.valueOf(serial));
          if (value != null)
            num += value.intValue(); 
          map.put(Integer.valueOf(serial), Integer.valueOf(num));
          int cost = i % 3;
          if (cost == 0) {
            level++;
            level = (level > 7) ? 7 : level;
          } 
        } 
        equip.getField().forEach(fiedEquip -> {
              if (fiedEquip.getType().equals(Integer.valueOf(6670))) {
                fiedEquip.setValue(Integer.valueOf(0));
                return;
              } 
            });
        for (Integer key : map.keySet())
          this.bagService.addGoods(role, key.intValue(), ((Integer)map.get(key)).intValue(), 0, true, false, "拆卸宝石"); 
        equip = EquipUtils.gemChange(equipData, equip);
        refresh(role, equip, pos.intValue(), id, true);
        MessagePusher.pushMessage(role, (Message)new RespHint(1, (short)1));
      } 
      return null;
    } catch (Exception e) {
      this.logger.error("镶嵌宝石出错了：");
      e.printStackTrace();
      return null;
    } 
  }
}
