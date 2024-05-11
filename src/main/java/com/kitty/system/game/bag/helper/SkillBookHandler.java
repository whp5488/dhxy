
package com.kitty.system.game.bag.helper;

import java.util.Arrays;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.common.message.RespCherish;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.init.domain.SysOtherItem;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysGiftItemOpen;
import java.util.ArrayList;
import java.text.MessageFormat;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("SkillBook")
public class SkillBookHandler extends BagHandler
{
    private static final String NOTICE = "#<P>书成风云变，一页动乾坤##<E:101>##<LB1>{0}#历尽千辛万苦集齐终极技能书残卷，终将#<O>终极#技能书{1}#<O>X1#修缮问世#<E:28>#";
    private static final String LACK = "缺少{0}技能书残卷X{1}";
    private static final String[] DESC;
    private static final List<Item> consume;
    private static final List<Integer> filtration;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        if (!NumberUtil.isNumber(param)) {
            return null;
        }
        final Integer type = Integer.valueOf(param);
        if (type < 0 || type > SkillBookHandler.consume.size() - 1 || num <= 0) {
            return null;
        }
        final Item item = SkillBookHandler.consume.get(type);
        for (int i = 0; i < num; ++i) {
            final int bagPos = this.bagService.getPos(role, false);
            if (bagPos < 0) {
                return "物品栏空间不足";
            }
        }
        if (item != null) {
            final Integer goods = this.bagService.consumeBagGoods(role, item.getItem(), item.getNum() * num, true, true, "技能书合成");
            if (goods != null) {
                return MessageFormat.format("缺少{0}技能书残卷X{1}", SkillBookHandler.DESC[type], goods);
            }
            final List<Item> objects = new ArrayList<Item>();
            final List<SysGiftItemOpen> collect = ItemDataPool.data_gift_item_open.stream().filter(g -> g.getGiftSerial().equals(item.getOthers())).collect(Collectors.toList());
            final Map<Integer, SysOtherItem> data_other_item = ItemDataPool.data_other_item;
            for (int j = 0; j < num; ++j) {
                final Item gift = this.openGift(collect, item.getOthers());
                objects.add(new Item(gift.getItem(), gift.getNum()));
            }
            int cherish = -1;
            int cherishNum = -1;
            String msg = "";
            for (final Item book : objects) {
                this.bagService.addGoods(role, book.getItem(), book.getNum(), 0, true, true, "技能书合成");
                final SysOtherItem otherItem = data_other_item.get(book.getItem());
                if (SkillBookHandler.filtration.contains(otherItem.getSerial())) {
                    msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", otherItem.getSerial(), 0, 0, otherItem.getSerial(), otherItem.getSerial(), otherItem.getName());
                }
                if (type == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                    cherish = otherItem.getSerial();
                    cherishNum = book.getNum();
                }
            }
            if (msg.length() > 1 && type == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了" + msg, true));
                this.broadcastThread.add2Queue(MessageFormat.format("#<P>书成风云变，一页动乾坤##<E:101>##<LB1>{0}#历尽千辛万苦集齐终极技能书残卷，终将#<O>终极#技能书{1}#<O>X1#修缮问世#<E:28>#", role.getName(), msg));
            }
            if (cherish > -1 && cherishNum > -1) {
                MessagePusher.pushMessage(role, new RespCherish(cherish, (short)cherishNum));
            }
        }
        return null;
    }
    
    public Item openGift(final List<SysGiftItemOpen> giftItemOpens, final Integer openGiftType) {
        final List<SysGiftItemOpen> collect = giftItemOpens.stream().filter(item -> item.getGiftSerial().equals(openGiftType)).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return null;
        }
        final List<Lottery> list = new ArrayList<Lottery>();
        collect.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRatio())));
        final List<Double> orignalRates = new ArrayList<Double>(list.size());
        for (final Lottery lottery : list) {
            orignalRates.add(lottery.getBili());
        }
        final int index = LotteryUtils.lottery(orignalRates);
        final Integer serial = list.get(index).getSerial();
        final SysGiftItemOpen giftItemOpen = collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
        if (giftItemOpen != null) {
            return new Item(giftItemOpen.getSerial(), giftItemOpen.getNum());
        }
        return null;
    }
    
    static {
        DESC = new String[] { "普通", "高级", "终极" };
        consume = Arrays.asList(new Item(75016, 10, 30011), new Item(75017, 50, 30010), new Item(75018, 100, 30018));
        filtration = Arrays.asList(92203, 92204, 92215, 92208);
    }
}
