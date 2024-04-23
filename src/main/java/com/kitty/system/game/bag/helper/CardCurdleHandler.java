package com.kitty.system.game.bag.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataCard;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("CardCurdle")
public class CardCurdleHandler extends BagHandler {
  private static final int CONSUME = 2;
  
  private static final int[] SERIAL = new int[] { 0, 0, 0, 90036, 90037, 90038 };
  
  private static final String[] NAMES = new String[] { "", "", "", "高级卡碎片", "特殊卡碎片", "神兽卡碎片" };
  
  private static final String LACK = "缺少{0}X{1}";
  
  private static final String SUCCEED = "#<B>{0}#历尽艰辛合{1}竟然合成了{2}#<r:239,g:110,b:41>X{3}##<E:1>#";
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    int type = Integer.valueOf(param).intValue();
    if (type < 3 || type > 5)
      return null; 
    int serial = SERIAL[type];
    int consume = num.intValue() * 2;
    Integer bagGoods = this.bagService.getBagGoods(role, serial, consume);
    if (bagGoods != null)
      return MessageFormat.format("缺少{0}X{1}", new Object[] { NAMES[type], bagGoods }); 
    this.bagService.consumeBagGoods(role, serial, consume, false, true, "变身卡合成");
    List<SysDataCard> collect = (List<SysDataCard>)CardDataPool.cardMap.values().stream().filter(e -> (e.getRarity() == type)).collect(Collectors.toList());
    Random random = new Random();
    Map<Integer, Integer> map = new HashMap<>(num.intValue());
    Map<Integer, String> nameMap = new HashMap<>(num.intValue());
    for (int i = 0; i < num.intValue(); i++) {
      SysDataCard card = collect.get(random.nextInt(collect.size()));
      Integer count = map.get(Integer.valueOf(card.getSerial()));
      if (map.get(Integer.valueOf(card.getSerial())) == null)
        count = Integer.valueOf(0); 
      map.put(Integer.valueOf(card.getSerial()), Integer.valueOf(count.intValue() + 1));
      nameMap.put(Integer.valueOf(card.getSerial()), card.getName());
    } 
    String msg = null;
    int j = 0;
    for (Integer key : map.keySet()) {
      Integer v = map.get(key);
      this.cardService.addCard(role, key, v, Integer.valueOf(0), true, false);
      if (j < 3) {
        String name = nameMap.get(key);
        if (name != null) {
          String format = String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { key, key, key, name });
          msg = MessageFormat.format("#<B>{0}#历尽艰辛合{1}竟然合成了{2}#<r:239,g:110,b:41>X{3}##<E:1>#", new Object[] { role.getName(), NAMES[type], format, v });
        } 
      } 
    } 
    if (msg != null);
    MessagePusher.pushMessage(role, (Message)new RespHint(21, (short)1));
    return null;
  }
}
