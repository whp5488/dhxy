
package com.kitty.system.game.bag.helper;

import java.util.Iterator;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import java.util.List;
import java.text.MessageFormat;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("CardCurdle")
public class CardCurdleHandler extends BagHandler
{
    private static final int CONSUME = 2;
    private static final int[] SERIAL;
    private static final String[] NAMES;
    private static final String LACK = "缺少{0}X{1}";
    private static final String SUCCEED = "#<B>{0}#历尽艰辛合{1}竟然合成了{2}#<r:239,g:110,b:41>X{3}##<E:1>#";
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final int type = Integer.valueOf(param);
        if (type < 3 || type > 5) {
            return null;
        }
        final int serial = CardCurdleHandler.SERIAL[type];
        final int consume = num * 2;
        final Integer bagGoods = this.bagService.getBagGoods(role, serial, consume);
        if (bagGoods != null) {
            return MessageFormat.format("缺少{0}X{1}", CardCurdleHandler.NAMES[type], bagGoods);
        }
        this.bagService.consumeBagGoods(role, serial, consume, false, true, "变身卡合成");
        final List<SysDataCard> collect = CardDataPool.cardMap.values().stream().filter(e -> e.getRarity() == type).collect(Collectors.toList());
        final Random random = new Random();
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>(num);
        final Map<Integer, String> nameMap = new HashMap<Integer, String>(num);
        for (int i = 0; i < num; ++i) {
            final SysDataCard card = collect.get(random.nextInt(collect.size()));
            Integer count = map.get(card.getSerial());
            if (map.get(card.getSerial()) == null) {
                count = 0;
            }
            map.put(card.getSerial(), count + 1);
            nameMap.put(card.getSerial(), card.getName());
        }
        String msg = null;
        final int j = 0;
        for (final Integer key : map.keySet()) {
            final Integer v = map.get(key);
            this.cardService.addCard(role, key, v, 0, true, false);
            if (j < 3) {
                final String name = nameMap.get(key);
                if (name == null) {
                    continue;
                }
                final String format = String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", key, key, key, name);
                msg = MessageFormat.format("#<B>{0}#历尽艰辛合{1}竟然合成了{2}#<r:239,g:110,b:41>X{3}##<E:1>#", role.getName(), CardCurdleHandler.NAMES[type], format, v);
            }
        }
        if (msg != null) {}
        MessagePusher.pushMessage(role, new RespHint(21, (short)1));
        return null;
    }
    
    static {
        SERIAL = new int[] { 0, 0, 0, 90036, 90037, 90038 };
        NAMES = new String[] { "", "", "", "高级卡碎片", "特殊卡碎片", "神兽卡碎片" };
    }
}
