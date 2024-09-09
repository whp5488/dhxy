package com.kitty.system.game.horse.handler;

import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespPreUpgradeHorse;
import com.kitty.system.game.horse.message.vo.MountBase;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.IdentityHashMap;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.RandomUtil;
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
    
    public HorseRefineHandler() {
    }
    
    @Override
    public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
        if (operation > 3) {
            return null;
        }
        byte seat = horse.getSeat();
        int count = HorseRefineHandler.COUNT[operation - 1];
        Map<Integer, Integer> consume = this.consume((short)operation, seat, count);
        Integer silver = (Integer)consume.get(Const.RESTYPE_SILVER);
        Integer num = (Integer)consume.get(Integer.valueOf(75045));
        boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, (long)(int)silver);
        if (!b) {
            return null;
        }
        Integer integer = this.bagService.consumeBagGoods(role, 75045, (int)num, true, true, "坐骑洗练");
        if (integer != null) {
            return "净瓶玉露数量不足";
        }
        boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (long)(int)silver, "HorseRefine");
        if (!currency) {
            this.logger.error("坐骑洗练扣除货币失败，玩家编号 = {}", role.getRoleId());
            return null;
        }
        role.save();
        int[] horseMoveScope = HorseDataPool.horseMoveScope;
        List<Double> rates = new ArrayList(HorseDataPool.horseGrowList.size());
        for (Lottery lottery : HorseDataPool.horseGrowList) {
            rates.add(lottery.getBili());
        }
        List<HorseBase> list = new ArrayList<>();
        List<MountBase> list_ = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            int move = RandomUtil.round(Integer.valueOf(horseMoveScope[0]), Integer.valueOf(horseMoveScope[1]));
            Lottery lottery2 = (Lottery)HorseDataPool.horseGrowList.get(LotteryUtils.lottery(rates));
            IdentityHashMap<Integer, Long> skill = this.horseService.creationHorseSkill(seat, new IdentityHashMap(3));
            list.add(new HorseBase((int)lottery2.getSerial(), move, 0, skill));
            List<FiedValue> objects = new ArrayList<>();
            for (Integer key : skill.keySet()) {
                Long value = (Long)skill.get(key);
                if (value == null) {
                    value = Long.valueOf(0L);
                }
                objects.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(value)));
            }
            list_.add(new MountBase((int)lottery2.getSerial(), move, 0, objects));
        }
        horse.getDarkBaseMap().put(Byte.valueOf(operation), list);
        horse.save();
        RespPreUpgradeHorse upgradeHorse = new RespPreUpgradeHorse();
        upgradeHorse.setId(String.valueOf(horse.getId()));
        upgradeHorse.setAsk(operation);
        upgradeHorse.setList(list_);
        MessagePusher.pushMessage(role, upgradeHorse);
        return null;
    }
    
    public Map<Integer, Integer> consume(short type, byte seat, int count) {
        double rebate = HorseRefineHandler.REBATE[type - 1];
        int num = HorseRefineHandler.BASE_ITEM[seat - 1];
        int currency = HorseRefineHandler.BASE_CURRENCY[seat - 1];
        Map<Integer, Integer> map = new LinkedHashMap();
        map.put(Integer.valueOf(75045), Integer.valueOf((int)((double)(count * num) * rebate)));
        map.put(Const.RESTYPE_SILVER, Integer.valueOf((int)((double)(count * currency) * rebate)));
        return map;
    }
    
    static {
        COUNT = new int[] { 1, 5, 20 };
        BASE_ITEM = new int[] { 3, 4, 5, 6 };
        BASE_CURRENCY = new int[] { 100000, 120000, 160000, 200000 };
        REBATE = new double[] { 1.0, 0.95, 0.9 };
    }
}
