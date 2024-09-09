package com.kitty.system.game.bag.helper;

import com.kitty.system.game.card.domain.CardChange;
import java.util.Map;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import java.util.stream.Collectors;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("CardDetach")
public class CardDetachHandler extends BagHandler
{
    private static final int[] SERIAL;
    private static final String[] NAMES;
    private static final String SUCCEED = "消耗{0}X{1}得到了{2}";
    
    public CardDetachHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        int serial = (int)Integer.valueOf(param);
        SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
        if (card == null) {
            return null;
        }
        if (card.getRarity() == 2) {
            return "普通变身卡无法分解";
        }
        Map<Long, CardChange> cardMap = this.cardService.getCard(role);
        Integer sum = (Integer)cardMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.summingInt(e -> DataGlobalFunc.getCardChangeNum(e)));
        if ((int)sum < (int)num) {
            return "数量不足";
        }
        int item = CardDetachHandler.SERIAL[card.getRarity()];
        if (!this.bagService.checkPos(role, item, (int)num)) {
            return null;
        }
        boolean detach = this.cardService.cardDetach(role, card.getSerial(), (int)num);
        if (!detach) {
            return "数量不足哦";
        }
        this.bagService.addGoods(role, item, (int)num, 0, true, true, "分解变身卡");
        String format = String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), card.getName());
        String msg = MessageFormat.format("消耗{0}X{1}得到了{2}", format, num, String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", Integer.valueOf(item), Integer.valueOf(item), Integer.valueOf(item), CardDetachHandler.NAMES[card.getRarity()]));
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, true));
        return null;
    }
    
    static {
        SERIAL = new int[] { 0, 0, 0, 90036, 90037, 90038 };
        NAMES = new String[] { "", "", "", "高级卡碎片", "特殊卡碎片", "神兽卡碎片" };
    }
}
