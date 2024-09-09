package com.kitty.system.game.pet.handler;

import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
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
    
    public BoneHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(BoneHandler.GOODS_SERIAL);
        if (item != null) {
            int level = (int)pet.getPetLv();
            int bone = (int)pet.getBone();
            int rebirth = (int)pet.getRebirth();
            double grow_up = (double)(int)pet.getPetBase().getGrow_up() / 1000.0;
            double gain = (double)(int)item.getValue() / 1000.0;
            double current = grow_up + gain * (double)bone;
            boolean check = this.check(level, rebirth, (int)pet.getBone());
            if (!check) {
                return MessageFormat.format("#<G>{0}#{1}转{2}级可吃龙之骨数量已达上限", pet.getPetName(), Integer.valueOf(rebirth), Integer.valueOf(level));
            }
            Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)BoneHandler.GOODS_SERIAL, 1, true, true, "使用龙骨");
            if (goods != null) {
                return "背包没有龙骨";
            }
            pet.setBone(Integer.valueOf(bone + 1));
            this.petService.calculateBase((long)role.getId(), pet);
            this.petService.getArenaScore((long)role.getId(), pet);
            this.petService.loadPet(role, pet);
            pet.save();
            return MessageFormat.format("召唤兽#<G>{0}#的成长率从{1}提高到{2}", pet.getPetName(), Double.valueOf(current), Double.valueOf(current + gain));
        }
        else {
            return null;
        }
    }
    
    public boolean check(int lv, int rebirth, int num) {
        boolean flag = false;
        if (rebirth >= 3 && lv >= 160 && num < 18) {
            flag = true;
        }
        else if (rebirth >= 3 && lv >= 140 && num < 15) {
            flag = true;
        }
        else if (rebirth >= 3 && lv >= 120 && num < 12) {
            flag = true;
        }
        else if (rebirth >= 2 && num < 7) {
            flag = true;
        }
        else if (rebirth >= 1 && num < 4) {
            flag = true;
        }
        else if (rebirth >= 0 && num < 2) {
            flag = true;
        }
        return flag;
    }
    
    static {
        GOODS_SERIAL = Integer.valueOf(75025);
    }
}
