package com.kitty.system.game.bag.helper;

import java.util.Arrays;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.common.message.RespCherish;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.init.domain.SysOtherItem;
import java.util.stream.Collectors;
import com.kitty.system.init.pool.ItemDataPool;
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
    
    public SkillBookHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        if (!NumberUtil.isNumber(param)) {
            return null;
        }
        Integer type = Integer.valueOf(param);
        if ((int)type < 0 || (int)type > SkillBookHandler.consume.size() - 1 || (int)num <= 0) {
            return null;
        }
        Item item = (Item)SkillBookHandler.consume.get((int)type);
        int i = 0;
        while (i < (int)num) {
            int bagPos = this.bagService.getPos(role, false);
            if (bagPos < 0) {
                return "物品栏空间不足";
            }
            ++i;
        }
        if (item != null) {
            Integer goods = this.bagService.consumeBagGoods(role, (int)item.getItem(), (int)item.getNum() * (int)num, true, true, "技能书合成");
            if (goods == null) {
                List<Item> objects = new ArrayList<>();
                List<SysGiftItemOpen> collect = (List)ItemDataPool.data_gift_item_open.stream().filter(g -> g.getGiftSerial().equals(item.getOthers())).collect(Collectors.toList());
                Map<Integer, SysOtherItem> data_other_item = ItemDataPool.data_other_item;
                for (int j = 0; j < (int)num; ++j) {
                    Item gift = this.openGift(collect, item.getOthers());
                    objects.add(new Item(gift.getItem(), gift.getNum()));
                }
                int cherish = -1;
                int cherishNum = -1;
                String msg = "";
                for (Item book : objects) {
                    this.bagService.addGoods(role, (int)book.getItem(), (int)book.getNum(), 0, true, true, "技能书合成");
                    SysOtherItem otherItem = (SysOtherItem)data_other_item.get(book.getItem());
                    if (SkillBookHandler.filtration.contains(otherItem.getSerial())) {
                        msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", otherItem.getSerial(), Integer.valueOf(0), Integer.valueOf(0), otherItem.getSerial(), otherItem.getSerial(), otherItem.getName());
                    }
                    if ((int)type == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                        cherish = (int)otherItem.getSerial();
                        cherishNum = (int)book.getNum();
                    }
                    else {
                        continue;
                    }
                }
                if (msg.length() > 1 && (int)type == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你获得了" + msg, true));
                    this.broadcastThread.add2Queue(MessageFormat.format("#<P>书成风云变，一页动乾坤##<E:101>##<LB1>{0}#历尽千辛万苦集齐终极技能书残卷，终将#<O>终极#技能书{1}#<O>X1#修缮问世#<E:28>#", role.getName(), msg));
                }
                if (cherish > -1 && cherishNum > -1) {
                    MessagePusher.pushMessage(role, new RespCherish(cherish, (short)cherishNum));
                }
            }
            else {
                return MessageFormat.format("缺少{0}技能书残卷X{1}", SkillBookHandler.DESC[(int)type], goods);
            }
        }
        return null;
    }
    
    public Item openGift(List<SysGiftItemOpen> giftItemOpens, Integer openGiftType) {
        List<SysGiftItemOpen> collect = (List)giftItemOpens.stream().filter(item -> item.getGiftSerial().equals(openGiftType)).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return null;
        }
        List<Lottery> list = new ArrayList<>();
        collect.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", Double.valueOf(itemOpen.getRatio()))));
        List<Double> orignalRates = new ArrayList(list.size());
        for (Lottery lottery : list) {
            orignalRates.add(lottery.getBili());
        }
        int index = LotteryUtils.lottery(orignalRates);
        Integer serial = ((Lottery)list.get(index)).getSerial();
        SysGiftItemOpen giftItemOpen = (SysGiftItemOpen)collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
        if (giftItemOpen != null) {
            return new Item(giftItemOpen.getSerial(), giftItemOpen.getNum());
        }
        return null;
    }
    
    static {
        DESC = new String[] { "普通", "高级", "终极" };
        consume = Arrays.asList(new Item(Integer.valueOf(75016), Integer.valueOf(10), Integer.valueOf(30011)), new Item(Integer.valueOf(75017), Integer.valueOf(50), Integer.valueOf(30010)), new Item(Integer.valueOf(75018), Integer.valueOf(100), Integer.valueOf(30018)));
        filtration = Arrays.asList(Integer.valueOf(92203), Integer.valueOf(92204), Integer.valueOf(92215), Integer.valueOf(92208));
    }
}
