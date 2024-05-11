
package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import java.text.MessageFormat;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("BoneHandler")
public class BoneHandler extends FeedPetHandler
{
    private static final Integer GOODS_SERIAL;
    private static final String SUCCESS = "召唤兽#<G>{0}#的成长率从{1}提高到{2}";
    private static final String FAIL = "#<G>{0}#{1}转{2}级可吃龙之骨数量已达上限";
    private static final String ERROR = "背包没有龙骨";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        final SysDataStuff item = ItemDataPool.data_stuff.get(BoneHandler.GOODS_SERIAL);
        if (item == null) {
            return null;
        }
        final int level = pet.getPetLv();
        final int bone = pet.getBone();
        final int rebirth = pet.getRebirth();
        final double grow_up = pet.getPetBase().getGrow_up() / 1000.0;
        final double gain = item.getValue() / 1000.0;
        final double current = grow_up + gain * bone;
        final boolean check = this.check(level, rebirth, pet.getBone());
        if (!check) {
            return MessageFormat.format("#<G>{0}#{1}转{2}级可吃龙之骨数量已达上限", pet.getPetName(), rebirth, level);
        }
        final Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, BoneHandler.GOODS_SERIAL, 1, true, true, "使用龙骨");
        if (goods != null) {
            return "背包没有龙骨";
        }
        pet.setBone(bone + 1);
        this.petService.calculateBase(role.getId(), pet);
        this.petService.getArenaScore(role.getId(), pet);
        this.petService.loadPet(role, pet);
        pet.save();
        return MessageFormat.format("召唤兽#<G>{0}#的成长率从{1}提高到{2}", pet.getPetName(), current, current + gain);
    }
    
    public boolean check(final int lv, final int rebirth, final int num) {
        boolean flag = false;
        // 修改龙骨数量
        if (rebirth >= 4 && lv >= 200 && num < 24) {
            flag = true;
        } else if (rebirth >= 4 && lv >= 180 && num < 22) {
            flag = true;
        } else if (rebirth >= 4 && lv >= 140 && num < 20) {
            flag = true;
        } else if (rebirth >= 3 && lv >= 160 && num < 18) {
            flag = true;
        } else if (rebirth >= 3 && lv >= 140 && num < 15) {
            flag = true;
        } else if (rebirth >= 3 && lv >= 120 && num < 12) {
            flag = true;
        } else if (rebirth >= 2 && num < 7) {
            flag = true;
        } else if (rebirth >= 1 && num < 4) {
            flag = true;
        } else if (rebirth >= 0 && num < 2) {
            flag = true;
        }
        return flag;
    }
    
    static {
        GOODS_SERIAL = 75025;
    }
}
