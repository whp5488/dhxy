
package com.kitty.system.game.bag.helper;

import java.util.List;
import java.util.Iterator;
import com.kitty.common.model.Pos;
import java.util.Map;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.system.game.bag.message.vo.PotholingVo;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import java.io.Serializable;
import com.kitty.system.game.bag.message.RespPotholing;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.cache.RedisService;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("Potholing")
public class PotholingHandler extends BagHandler
{
    private static final int SERIAL = 90033;
    
    @Override
    public String acceptOperate(final Role role, final Integer pos, final Integer num, final String param) {
        String msg = DataGlobalFunc.needGradeJudge(0, 60, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            return msg;
        }
        final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
        final BagGoods goods = bagGoodsMap.values().stream().filter(item -> pos.equals(item.getPos())).findFirst().orElse(null);
        if (goods == null) {
            return null;
        }
        if (goods.getSerial() != 90033) {
            return null;
        }
        final Pos rolePos = role.getPos();
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
            final RedisService redisService = SpringUtils.getBean(RedisService.class);
            final RespPotholing respPotholing = redisService.get(role.getId() + "potholing", RespPotholing.class);
            if (respPotholing != null) {
                role.pushTempCache("potholing", redisService.get(role.getId() + "potholing_data", String.class));
                MessagePusher.pushMessage(role, respPotholing);
                return null;
            }
            final List<SysDataLotterySuper> randoms = WelfareDataPool.createRandoms(15);
            final List<Lottery> list = new ArrayList<Lottery>();
            randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
            final List<Double> orignalRates = new ArrayList<Double>(list.size());
            for (final Lottery lottery : list) {
                orignalRates.add(lottery.getBili());
            }
            final int index = LotteryUtils.lottery(orignalRates);
            Lottery lottery = list.get(index);
            int site = 0;
            final List<PotholingVo> objects = new ArrayList<PotholingVo>();
            for (int i = 0; i < randoms.size(); ++i) {
                final SysDataLotterySuper lotterySuper = randoms.get(i);
                final int serial = WelfareDataPool.getLotterySuperEquip(lotterySuper.getSerial(), role.getRace());
                if (lottery.getSerial().equals(lotterySuper.getId())) {
                    role.pushTempCache("potholing", serial + "-" + lotterySuper.getType() + "-" + lotterySuper.getNum() + "-" + lotterySuper.getIsNotice());
                    site = i + 1;
                }
                objects.add(new PotholingVo(serial, lotterySuper.getNum()));
            }
            final RespPotholing potholing = new RespPotholing(objects, site, String.valueOf(goods.getId()));
            MessagePusher.pushMessage(role, potholing);
            redisService.set(role.getId() + "potholing", potholing, 180L);
            redisService.set(role.getId() + "potholing_data", role.getTempCache("potholing", (Serializable)null), 180L);
        }
        return msg;
    }
}
