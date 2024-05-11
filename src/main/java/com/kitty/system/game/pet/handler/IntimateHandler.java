
package com.kitty.system.game.pet.handler;

import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("IntimateHandler")
public class IntimateHandler extends FeedPetHandler
{
    private static final String SUCCESS = "消耗了[亲密丹]x{0}，你的召唤兽#<G>{1}#的亲密度增加了#<G>{2}#";
    private static final String ERROR = "背包没有亲密丹";
    private static final String WARN = "亲密度已达到上限";
    private static final int GOODS_SERIAL = 75019;
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, int count, final Object... args) {
        try {
            count = Math.max(1, count);
            final SysDataStuff item = ItemDataPool.data_stuff.get(75019);
            if (item == null) {
                return null;
            }
            final int value = item.getValue();
            final int limit = pet.getCloseLimitMap().get("close_dan");
            if (limit >= 100000) {
                return "该宠物亲密丹使用已到达上限";
            }
            if (pet.getPetClose() > 2000000000L) {
                return "亲密度已达到上限";
            }
            if (count + limit > 100000) {
                count = 100000 - limit;
            }
            final Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 75019, count, true, true, "宠物使用亲密丹");
            if (goods != null) {
                return "背包没有亲密丹";
            }
            final long close = pet.getPetClose() + value * count;
            pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
            this.petService.getArenaScore(role.getId(), pet);
            this.petService.loadPet(role, pet);
            pet.getCloseLimitMap().put("close_dan", limit + count);
            pet.save();
            return MessageFormat.format("消耗了[亲密丹]x{0}，你的召唤兽#<G>{1}#的亲密度增加了#<G>{2}#", count, pet.getPetName(), value * count);
        }
        catch (final Exception e) {
            return null;
        }
    }
}
