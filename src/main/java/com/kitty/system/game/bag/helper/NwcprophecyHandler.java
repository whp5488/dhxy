package com.kitty.system.game.bag.helper;

import java.util.List;
import java.util.Iterator;
import com.kitty.common.model.Pos;
import java.util.Map;
import java.io.Serializable;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cache.RedisService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.message.RespPotholingNotice;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("Nwcprophecy")
public class NwcprophecyHandler extends BagHandler
{
    private static final String TIPS = "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#";
    
    public NwcprophecyHandler() {
    }
    
    @Override
    public String acceptOperate(Role role, Integer pos, Integer num, String param) {
        Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        BagGoods goods = (BagGoods)bagGoodsMap.values().stream().filter(item -> pos.equals(Integer.valueOf(item.getPos()))).findFirst().orElse(null);
        if (goods == null || !Const.treasureMap.contains(Integer.valueOf(goods.getSerial()))) {
            return null;
        }
        Pos rolePos = role.getPos();
        String msg = null;
        for (String key : goods.getSpecial().keySet()) {
            if (key.equals("map")) {
                int map = goods.getSpecial().getInt(key);
                if (!rolePos.getMapId().equals(Integer.valueOf(map))) {
                    msg = "当前位置没有宝物哦，请移动到指定位置挖宝";
                    break;
                }
            }
        }
        if (msg == null) {
            int bd = 0;
            int anInt = goods.getRests().getInt(String.valueOf(2), 0);
            if (anInt > 0) {
                bd = 2;
            }
            int type = (int)Integer.valueOf(param);
            if (type == 0 && goods.getSerial() == 90061) {
                ((RedisService)SpringUtils.getBean(RedisService.class)).del(role.getId() + "rare_potholing");
                Object object = role.getTempCache("rare_potholing", null);
                if (object != null) {
                    String[] split = String.valueOf(object).split("-");
                    if (split.length < 4) {
                        return null;
                    }
                    role.popTempCache("rare_potholing");
                    this.bagService.send(role, (int)Integer.valueOf(split[0]), (byte)Byte.valueOf(split[1]), (int)Integer.valueOf(split[2]), (int)pos, (byte)Byte.valueOf(split[3]), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
                }
            }
            else if (type == 1 && goods.getSerial() == 90033) {
                ((RedisService)SpringUtils.getBean(RedisService.class)).del(role.getId() + "potholing");
                Object object = role.getTempCache("potholing", null);
                if (object != null) {
                    String[] split = String.valueOf(object).split("-");
                    if (split.length < 4) {
                        return null;
                    }
                    role.popTempCache("potholing");
                    this.bagService.send(role, (int)Integer.valueOf(split[0]), (byte)Byte.valueOf(split[1]), (int)Integer.valueOf(split[2]), (int)pos, (byte)Byte.valueOf(split[3]), "#<BB>%s#跟随藏宝图的指引，挖到了强盗珍藏的%s#<O>X%d#，一时间喜笑颜开#<E:86>#", bd);
                }
            }
            else if (goods.getSerial() == 90032) {
                List<SysDataLottery> randoms = WelfareDataPool.lottery;
                List<Lottery> list = new ArrayList<>();
                randoms.forEach(itemOpen -> list.add(new Lottery(Integer.valueOf(itemOpen.getId()), "", Double.valueOf(itemOpen.getRate()))));
                List<Double> orignalRates = new ArrayList(list.size());
                for (Lottery lottery : list) {
                    orignalRates.add(lottery.getBili());
                }
                int index = LotteryUtils.lottery(orignalRates);
                Lottery lottery = (Lottery)list.get(index);
                SysDataLottery sysDataLottery = (SysDataLottery)randoms.stream().filter(e -> lottery.getSerial().equals(Integer.valueOf(e.getId()))).findFirst().orElse(null);
                if (sysDataLottery != null) {
                    this.bagService.send(role, sysDataLottery.getSerial(), (int)sysDataLottery.getType(), sysDataLottery.getNum(), (int)pos, "藏宝图", bd);
                    MessagePusher.pushMessage(role, new RespPotholingNotice(Integer.valueOf(sysDataLottery.getSerial()), Integer.valueOf(sysDataLottery.getNum())));
                }
            }
        }
        return msg;
    }
}
