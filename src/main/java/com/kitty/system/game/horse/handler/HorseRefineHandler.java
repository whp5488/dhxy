
package com.kitty.system.game.horse.handler;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespPreUpgradeHorse;
import com.kitty.system.common.enter.FiedValue;
import java.util.IdentityHashMap;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.horse.message.vo.MountBase;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseRefine")
public class HorseRefineHandler extends FeedHorseHandler
{
    public static final String NOT_FIND_STUFF = "净瓶玉露数量不足";
    public static final int SERIAL = 75045;
    public static final int[] COUNT;
    public static final int[] BASE_ITEM;
    public static final int[] BASE_CURRENCY;
    public static final double[] REBATE;
    
    @Override
    public String acceptFeed(final Horse horse, final Role role, final short type, final byte operation, final Object... args) {
        if (operation > 3) {
            return null;
        }
        final byte seat = horse.getSeat();
        final int count = HorseRefineHandler.COUNT[operation - 1];
        final Map<Integer, Integer> consume = this.consume(operation, seat, count);
        final Integer silver = consume.get(Const.RESTYPE_SILVER);
        final Integer num = consume.get(75045);
        final boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver);
        if (!b) {
            return null;
        }
        final Integer integer = this.bagService.consumeBagGoods(role, 75045, num, true, true, "坐骑洗练");
        if (integer != null) {
            return "净瓶玉露数量不足";
        }
        final boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "HorseRefine");
        if (!currency) {
            this.logger.error("坐骑洗练扣除货币失败，玩家编号 = {}", (Object)role.getRoleId());
            return null;
        }
        role.save();
        final int[] horseMoveScope = HorseDataPool.horseMoveScope;
        final List<Double> rates = new ArrayList<Double>(HorseDataPool.horseGrowList.size());
        for (final Lottery lottery : HorseDataPool.horseGrowList) {
            rates.add(lottery.getBili());
        }
        final List<HorseBase> list = new ArrayList<HorseBase>();
        final List<MountBase> list_ = new ArrayList<MountBase>();
        for (int i = 0; i < count; ++i) {
            final int move = RandomUtil.round(horseMoveScope[0], horseMoveScope[1]);
            final Lottery lottery2 = HorseDataPool.horseGrowList.get(LotteryUtils.lottery(rates));
            final IdentityHashMap<Integer, Long> skill = this.horseService.creationHorseSkill(seat, new IdentityHashMap<Integer, Long>(3));
            list.add(new HorseBase(lottery2.getSerial(), move, 0, skill));
            final List<FiedValue> objects = new ArrayList<FiedValue>();
            for (final Integer key : skill.keySet()) {
                Long value = skill.get(key);
                if (value == null) {
                    value = 0L;
                }
                objects.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(value)));
            }
            list_.add(new MountBase(lottery2.getSerial(), move, 0, objects));
        }
        horse.getDarkBaseMap().put(operation, list);
        horse.save();
        final RespPreUpgradeHorse upgradeHorse = new RespPreUpgradeHorse();
        upgradeHorse.setId(String.valueOf(horse.getId()));
        upgradeHorse.setAsk(operation);
        upgradeHorse.setList(list_);
        MessagePusher.pushMessage(role, upgradeHorse);
        return null;
    }
    
    public Map<Integer, Integer> consume(final short type, final byte seat, final int count) {
        final double rebate = HorseRefineHandler.REBATE[type - 1];
        final int num = HorseRefineHandler.BASE_ITEM[seat - 1];
        final int currency = HorseRefineHandler.BASE_CURRENCY[seat - 1];
        final Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
        map.put(75045, (int)(count * num * rebate));
        map.put(Const.RESTYPE_SILVER, (int)(count * currency * rebate));
        return map;
    }
    
    static {
        COUNT = new int[] { 1, 5, 20 };
        BASE_ITEM = new int[] { 3, 4, 5, 6 };
        BASE_CURRENCY = new int[] { 100000, 120000, 160000, 200000 };
        REBATE = new double[] { 1.0, 0.95, 0.9 };
    }
}
