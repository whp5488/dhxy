
package com.kitty.system.game.horse.handler;

import java.util.Iterator;
import org.nutz.lang.util.NutMap;
import java.util.IdentityHashMap;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseSkillUpHandler")
public class HorseSkillUpHandler extends FeedHorseHandler
{
    private static final long EXP_VALUE = 500L;
    private static final int SERIAL_DSMQ = 75044;
    private static final int SERIAL_LGSM = 75195;
    private static final String LACK = "{0}数量不足";
    private static final String SUCCESS;
    
    @Override
    public String acceptFeed(final Horse horse, final Role role, final short type, final byte operation, final Object... args) {
        final int index = Integer.valueOf(String.valueOf(args[0])) - 1;
        if (index < 0 || index > 2) {
            return null;
        }
        final IdentityHashMap<Integer, Long> map = horse.getSkill();
        final Integer[] keys = map.keySet().toArray(new Integer[0]);
        final Integer skill = keys[index];
        long exp = getSkillExp(map, skill, index);
        if (exp < 0L) {
            return null;
        }
        final long limit = getProficiency(horse.getLevel());
        if (exp >= limit) {
            return "技能熟练达到当前等级限制";
        }
        int serial = 75044;
        int num = 2;
        String name = "丹书密卷";
        final NutMap consume = consume(exp);
        final long coin = consume.getLong("coin", 0L);
        if (coin <= 0L) {
            return null;
        }
        num = consume.getInt("ds", 0);
        if (operation == 2) {
            name = "流光碎溟";
            serial = 75195;
            num = consume.getInt("lg", 0);
        }
        if (num <= 0 || this.bagService.getBagGoods(role, serial, num) != null) {
            return MessageFormat.format("{0}数量不足", name);
        }
        final boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_COIN, coin);
        if (!b) {
            return null;
        }
        final Integer integer = this.bagService.consumeBagGoods(role, serial, num, true, true, "坐骑技能升级");
        if (integer != null) {
            return MessageFormat.format("{0}数量不足", name);
        }
        final boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, coin, "HorseSkillUpHandler");
        if (!currency) {
            this.logger.error("坐骑技能升级扣除货币失败，玩家编号 = {}", (Object)role.getRoleId());
            return null;
        }
        role.save();
        int i = 0;
        for (final Integer key : map.keySet()) {
            if (i == index && key.equals(skill)) {
                exp += 500L;
                map.put(skill, (exp > limit) ? limit : exp);
                break;
            }
            ++i;
        }
        horse.save();
        this.horseService.refresh(role, horse);
        return MessageFormat.format(HorseSkillUpHandler.SUCCESS, name, num, MoneyUtil.colours(coin), 500L);
    }
    
    public static long getSkillExp(final IdentityHashMap<Integer, Long> map, final Integer serial, final int index) {
        long exp = -1L;
        int i = 0;
        for (final Integer key : map.keySet()) {
            if (key.equals(serial) && i == index) {
                exp = map.get(key);
                break;
            }
            ++i;
        }
        return exp;
    }
    
    public static long getProficiency(final short level) {
        if (level <= 10) {
            return 10000L;
        }
        if (level > 10 && level <= 20) {
            return 20000L;
        }
        if (level > 20 && level <= 30) {
            return 30000L;
        }
        if (level > 30 && level <= 40) {
            return 40000L;
        }
        if (level > 40 && level <= 50) {
            return 50000L;
        }
        if (level > 50 && level <= 60) {
            return 60000L;
        }
        if (level > 60 && level <= 70) {
            return 70000L;
        }
        if (level > 70 && level <= 80) {
            return 80000L;
        }
        if (level > 80 && level <= 90) {
            return 90000L;
        }
        if (level > 90 && level <= 100) {
            return 100000L;
        }
        if (level > 100 && level <= 110) {
            return 110000L;
        }
        if (level > 110 && level <= 120) {
            return 120000L;
        }
        if (level > 120 && level <= 130) {
            return 130000L;
        }
        return -1L;
    }
    
    public static NutMap consume(final long range) {
        final NutMap nutMap = new NutMap();
        int count_dsmj = 0;
        int count_lgsm = 0;
        if (range >= 0L && range <= 9999L) {
            count_dsmj = 2;
            count_lgsm = 2;
        }
        else if (range >= 10000L && range <= 19999L) {
            count_dsmj = 4;
            count_lgsm = 4;
        }
        else if (range >= 20000L && range <= 29999L) {
            count_dsmj = 4;
            count_lgsm = 4;
        }
        else if (range >= 30000L && range <= 39999L) {
            count_dsmj = 6;
            count_lgsm = 6;
        }
        else if (range >= 40000L && range <= 49999L) {
            count_dsmj = 9;
            count_lgsm = 9;
        }
        else if (range >= 50000L && range <= 59999L) {
            count_dsmj = 12;
            count_lgsm = 12;
        }
        else if (range >= 60000L && range <= 69999L) {
            count_dsmj = 18;
            count_lgsm = 18;
        }
        else if (range >= 70000L && range <= 79999L) {
            count_dsmj = 24;
            count_lgsm = 24;
        }
        else if (range >= 70000L && range <= 79999L) {
            count_dsmj = 24;
            count_lgsm = 24;
        }
        else if (range >= 80000L && range <= 89999L) {
            count_dsmj = 28;
        }
        else if (range >= 90000L && range <= 99999L) {
            count_dsmj = 35;
        }
        else if (range >= 100000L && range <= 109999L) {
            count_dsmj = 42;
        }
        else if (range >= 110000L && range <= 119999L) {
            count_dsmj = 50;
        }
        else if (range >= 120000L && range <= 130000L) {
            count_dsmj = 65;
        }
        final int coin = (int)(7510.0 + 19.0 * Math.pow((double)(range / 500L), 2.0) + range / 500L * 1392L);
        nutMap.setv("coin", (Object)coin);
        nutMap.setv("ds", (Object)count_dsmj);
        nutMap.setv("lg", (Object)count_lgsm);
        return nutMap;
    }
    
    static {
        SUCCESS = "消耗{0}X{1}，#<IR" + Const.RESTYPE_COIN + ">#{2}，熟练度提升{3}";
    }
}
