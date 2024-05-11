
package com.kitty.system.game.pet.handler;

import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import java.util.List;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import java.text.MessageFormat;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("AwakenHandler")
public class AwakenHandler extends FeedPetHandler
{
    private static final String SUCCESS = "消耗玄元丹X{0}，{1}的觉醒度成功突破至#<G>{2}#";
    private static final String FAIL = "消耗玄元丹X{0}，突破失败，少侠再接再厉！";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null) {
            return MessageFormat.format("#<Y>{0}#宠物配置未找到，无法进行此操作。", pet.getPetName());
        }
        final Integer[] consume = this.consume(sysPet);
        if (consume.length <= 0) {
            return null;
        }
        boolean flag = true;
        final short awaken = pet.getAwaken();
        int num = consume[awaken];
        if (param > 1) {
            num /= 3;
            flag = false;
        }
        final Integer integer = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 75403, num, true, true, "觉醒突破：" + pet.getPetId());
        if (integer != null) {
            return null;
        }
        if (flag) {
            this.success(role.getId(), pet, num);
        }
        else if (this.ratio()) {
            this.success(role.getId(), pet, num);
        }
        else {
            MessagePusher.pushMessage(role.getId(), new RespMsg(MessageFormat.format("消耗玄元丹X{0}，突破失败，少侠再接再厉！", num)));
        }
        return null;
    }
    
    public void success(final long id, final Pet pet, final int num) {
        pet.setAwaken((short)(pet.getAwaken() + 1));
        this.petService.calculateBase(id, pet);
        this.petService.getArenaScore(id, pet);
        pet.save();
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("awaken", (byte)2, pet.getAwaken()));
        respUpdate.setList(list);
        MessagePusher.pushMessage(id, respUpdate);
        MessagePusher.pushMessage(id, new RespMsg(MessageFormat.format("消耗玄元丹X{0}，{1}的觉醒度成功突破至#<G>{2}#", num, pet.getPetName(), pet.getAwaken())));
    }
    
    public Integer[] consume(final SysPet dataPet) {
        Integer[] consume = new Integer[0];
        switch (dataPet.getLevelType()) {
            case 2: {
                consume = new Integer[] { 3, 6, 12, 24, 48, 96 };
                break;
            }
            case 3: {
                consume = new Integer[] { 15, 30, 60, 120, 240, 480 };
                break;
            }
            case 4: {
                consume = new Integer[] { 90, 180, 360, 720, 1440, 2880 };
                break;
            }
            case 5: {
                consume = new Integer[] { 150, 300, 600, 1200, 2400, 4800 };
                break;
            }
        }
        return consume;
    }
    
    public boolean ratio() {
        final double pow = Math.pow(0.35, BigDecimalUtil.decimalScaleObjectDown(3, RandomUtil.round(4, 6)));
        return SkillAi.canAffectOnPro(pow);
    }
}
