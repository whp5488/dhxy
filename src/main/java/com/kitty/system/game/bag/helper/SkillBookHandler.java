package com.kitty.system.game.bag.helper;

import com.kitty.common.bean.Lottery;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespCherish;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("SkillBook")
public class SkillBookHandler extends BagHandler {
  private static final String NOTICE = "#<P>书成风云变，一页动乾坤##<E:101>##<LB1>{0}#历尽千辛万苦集齐终极技能书残卷，终将#<O>终极#技能书{1}#<O>X1#修缮问世#<E:28>#";
  
  private static final String LACK = "缺少{0}技能书残卷X{1}";
  
  private static final String[] DESC = new String[] { "普通", "高级", "终极" };
  
  private static final List<Item> consume = Arrays.asList(new Item[] { new Item(Integer.valueOf(75016), Integer.valueOf(10), Integer.valueOf(30011)), new Item(Integer.valueOf(75017), Integer.valueOf(50), Integer.valueOf(30010)), new Item(Integer.valueOf(75018), Integer.valueOf(100), Integer.valueOf(30018)) });
  
  private static final List<Integer> filtration = Arrays.asList(new Integer[] { Integer.valueOf(92203), Integer.valueOf(92204), Integer.valueOf(92215), Integer.valueOf(92208) });
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    if (!NumberUtil.isNumber(param))
      return null; 
    Integer type = Integer.valueOf(param);
    if (type.intValue() < 0 || type.intValue() > consume.size() - 1 || num.intValue() <= 0)
      return null; 
    Item item = consume.get(type.intValue());
    for (int i = 0; i < num.intValue(); i++) {
      int bagPos = this.bagService.getPos(role, false);
      if (bagPos < 0)
        return "物品栏空间不足"; 
    } 
    if (item != null) {
      Integer goods = this.bagService.consumeBagGoods(role, item.getItem().intValue(), item.getNum().intValue() * num.intValue(), true, true, "技能书合成");
      if (goods == null) {
        List<Item> objects = new ArrayList<>();
        List<SysGiftItemOpen> collect = (List<SysGiftItemOpen>)ItemDataPool.data_gift_item_open.stream().filter(g -> g.getGiftSerial().equals(item.getOthers())).collect(Collectors.toList());
        Map<Integer, SysOtherItem> data_other_item = ItemDataPool.data_other_item;
        for (int j = 0; j < num.intValue(); j++) {
          Item gift = openGift(collect, item.getOthers());
          objects.add(new Item(gift.getItem(), gift.getNum()));
        } 
        int cherish = -1;
        int cherishNum = -1;
        String msg = "";
        for (Item book : objects) {
          this.bagService.addGoods(role, book.getItem().intValue(), book.getNum().intValue(), 0, true, true, "技能书合成");
          SysOtherItem otherItem = data_other_item.get(book.getItem());
          if (filtration.contains(otherItem.getSerial()))
            msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { otherItem.getSerial(), Integer.valueOf(0), Integer.valueOf(0), otherItem.getSerial(), otherItem.getSerial(), otherItem.getName() }); 
          if (type.intValue() == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
            cherish = otherItem.getSerial().intValue();
            cherishNum = book.getNum().intValue();
          } 
        } 
        if (msg.length() > 1 && type.intValue() == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你获得了" + msg, true));
          this.broadcastThread.add2Queue(MessageFormat.format("#<P>书成风云变，一页动乾坤##<E:101>##<LB1>{0}#历尽千辛万苦集齐终极技能书残卷，终将#<O>终极#技能书{1}#<O>X1#修缮问世#<E:28>#", new Object[] { role.getName(), msg }));
        } 
        if (cherish > -1 && cherishNum > -1)
          MessagePusher.pushMessage(role, (Message)new RespCherish(cherish, (short)cherishNum)); 
      } else {
        return MessageFormat.format("缺少{0}技能书残卷X{1}", new Object[] { DESC[type.intValue()], goods });
      } 
    } 
    return null;
  }
  
  public Item openGift(List<SysGiftItemOpen> giftItemOpens, Integer openGiftType) {
    List<SysGiftItemOpen> collect = (List<SysGiftItemOpen>)giftItemOpens.stream().filter(item -> item.getGiftSerial().equals(openGiftType)).collect(Collectors.toList());
    if (collect.size() <= 0)
      return null; 
    List<Lottery> list = new ArrayList<>();
    collect.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", Double.valueOf(itemOpen.getRatio()))));
    List<Double> orignalRates = new ArrayList<>(list.size());
    for (Lottery lottery : list)
      orignalRates.add(lottery.getBili()); 
    int index = LotteryUtils.lottery(orignalRates);
    Integer serial = ((Lottery)list.get(index)).getSerial();
    SysGiftItemOpen giftItemOpen = collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
    if (giftItemOpen != null)
      return new Item(giftItemOpen.getSerial(), giftItemOpen.getNum()); 
    return null;
  }
}
