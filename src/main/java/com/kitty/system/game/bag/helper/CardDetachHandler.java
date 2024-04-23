package com.kitty.system.game.bag.helper;

import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataCard;
import java.text.MessageFormat;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component("CardDetach")
public class CardDetachHandler extends BagHandler {
  private static final int[] SERIAL = new int[] { 0, 0, 0, 90036, 90037, 90038 };
  
  private static final String[] NAMES = new String[] { "", "", "", "高级卡碎片", "特殊卡碎片", "神兽卡碎片" };
  
  private static final String SUCCEED = "消耗{0}X{1}得到了{2}";
  
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    int serial = Integer.valueOf(param).intValue();
    SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
    if (card == null)
      return null; 
    if (card.getRarity() == 2)
      return "普通变身卡无法分解"; 
    Map<Long, CardChange> cardMap = this.cardService.getCard(role);
    Integer sum = (Integer)cardMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.summingInt(e -> DataGlobalFunc.getCardChangeNum(e)));
    if (sum.intValue() < num.intValue())
      return "数量不足"; 
    int item = SERIAL[card.getRarity()];
    if (!this.bagService.checkPos(role, item, num.intValue()))
      return null; 
    boolean detach = this.cardService.cardDetach(role, card.getSerial(), num.intValue());
    if (!detach)
      return "数量不足哦"; 
    this.bagService.addGoods(role, item, num.intValue(), 0, true, true, "分解变身卡");
    String format = String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), card.getName() });
    String msg = MessageFormat.format("消耗{0}X{1}得到了{2}", new Object[] { format, num, String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { Integer.valueOf(item), Integer.valueOf(item), Integer.valueOf(item), NAMES[card.getRarity()] }) });
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, true));
    return null;
  }
}
