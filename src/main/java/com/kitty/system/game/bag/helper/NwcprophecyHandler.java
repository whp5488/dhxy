
package com.kitty.system.game.bag.helper;

import java.util.List;
import java.util.Iterator;
import com.kitty.common.model.Pos;
import java.util.Map;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.message.RespPotholingNotice;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import java.io.Serializable;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cache.RedisService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("Nwcprophecy")
public class NwcprophecyHandler extends BagHandler
{
    private static final String TIPS = "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#";
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(item.getPos())).findFirst().orElse(null);
        if (goods == null || !Const.treasureMap.contains(goods.getSerial())) {
            return null;
        }
        final Pos rolePos = role.getPos();
        String msg = null;
        for (final String key : goods.getSpecial().keySet()) {
            if (key.equals("map")) {
                final int map = goods.getSpecial().getInt(key);
                if (!rolePos.getMapId().equals(map)) {
                    msg = "当前位置没有宝物哦，请移动到指定位置挖宝";
                    break;
                }
                continue;
            }
        }
        if (msg == null) {
            int bd = 0;
            final int anInt = goods.getRests().getInt(String.valueOf(2), 0);
            if (anInt > 0) {
                bd = 2;
            }
            final int type = Integer.valueOf(param);
            if (type == 0 && goods.getSerial() == 90061) {
                SpringUtils.getBean(RedisService.class).del(role.getId() + "rare_potholing");
                final Object object = role.getTempCache("rare_potholing", null);
                if (object != null) {
                    final String[] split = String.valueOf(object).split("-");
                    if (split.length < 4) {
                        return null;
                    }
                    role.popTempCache("rare_potholing");
                    this.bagService.send(role, Integer.valueOf(split[0]), Byte.valueOf(split[1]), Integer.valueOf(split[2]), pos, Byte.valueOf(split[3]), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
                }
            }
            else if (type == 1 && goods.getSerial() == 90033) {
                SpringUtils.getBean(RedisService.class).del(role.getId() + "potholing");
                final Object object = role.getTempCache("potholing", null);
                if (object != null) {
                    final String[] split = String.valueOf(object).split("-");
                    if (split.length < 4) {
                        return null;
                    }
                    role.popTempCache("potholing");
                    this.bagService.send(role, Integer.valueOf(split[0]), Byte.valueOf(split[1]), Integer.valueOf(split[2]), pos, Byte.valueOf(split[3]), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
                }
            }
            else if (goods.getSerial() == 90032) {
                final List<SysDataLottery> randoms = WelfareDataPool.lottery;
                final List<Lottery> list = new ArrayList<Lottery>();
                randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
                final List<Double> orignalRates = new ArrayList<Double>(list.size());
                for (final Lottery lottery : list) {
                    orignalRates.add(lottery.getBili());
                }
                final int index = LotteryUtils.lottery(orignalRates);
                Lottery lottery = list.get(index);
                final SysDataLottery sysDataLottery = randoms.stream().filter(e -> lottery.getSerial().equals(e.getId())).findFirst().orElse(null);
                if (sysDataLottery != null) {
                    this.bagService.send(role, sysDataLottery.getSerial(), sysDataLottery.getType(), sysDataLottery.getNum(), pos, "藏宝图", bd);
                    MessagePusher.pushMessage(role, new RespPotholingNotice(sysDataLottery.getSerial(), sysDataLottery.getNum()));
                }
            }
        }
        return msg;
    }
}
