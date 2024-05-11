
package com.kitty.system.game.pet.handler;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import java.text.MessageFormat;
import java.util.Map;
import com.kitty.system.game.pet.PetUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.ArrayList;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("YuanQiDanHandler")
public class YuanQiDanHandler extends FeedPetHandler
{
    private static final int SERIAL = 75100;
    private static final String ERROR = "背包没有元气丹";
    
    @Override
    public String acceptFeed(final Pet pet, final Role role, final Integer param, final String type, final int count, final Object... args) {
        final Integer growthDan = pet.getGrowthDan();
        final Map<Integer, SysPet> petMap = PetDataPool.petMap;
        final SysPet sysPet = petMap.get(pet.getPetType());
        final List<Byte> sysPetColor = sysPet.getColor();
        final List<Byte> colors = new ArrayList<Byte>();
        for (final Byte b : sysPetColor) {
            if (b != null) {
                colors.add(b);
            }
        }
        if (growthDan == 1) {
            if (sysPet == null) {
                return "系统出错";
            }
            if (pet.getColorList().size() >= sysPetColor.size()) {
                return "此宠物颜色已经达到峰值，无法再增加";
            }
        }
        final Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
        final SysDataStuff sysDataStuff = data_stuff.get(param);
        final Integer applyObjectSerial = sysDataStuff.getApplyObjectSerial();
        if (param != 75100) {
            if (applyObjectSerial == null) {
                return "编号不属于元气丹范围";
            }
            if (!applyObjectSerial.equals(pet.getPetType())) {
                return "此元气丹不能给本宠物使用";
            }
        }
        final Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, param, 1, true, true, "元气丹");
        if (goods != null) {
            return "背包没有元气丹";
        }
        Byte theColor = null;
        int property = 0;
        if (growthDan == 0) {
            pet.setGrowthDan(1);
            this.petService.getArenaScore(role.getId(), pet);
            final double percent = 0.1;
            final boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                theColor = PetUtils.addPetColor(pet, colors);
                property = 1;
            }
        }
        else {
            for (final Map<String, Byte> map : pet.getColorList()) {
                final Byte color = map.get("color");
                colors.remove(color);
            }
            theColor = PetUtils.addPetColor(pet, colors);
            property = 2;
        }
        pet.save();
        String message = null;
        boolean flag2 = false;
        boolean isG = false;
        switch (property) {
            case 0: {
                message = MessageFormat.format("召唤兽#<G>{0}#获得了高成长", pet.getPetName());
                MessagePusher.pushMessage(role, new RespHint(15, (short)1));
                isG = true;
                break;
            }
            case 1: {
                message = MessageFormat.format("召唤兽#<G>{0}#获得了高成长及变色{1}", pet.getPetName(), theColor);
                MessagePusher.pushMessage(role, new RespHint(16, (short)1));
                MessagePusher.pushMessage(role, new RespHint(15, (short)1));
                isG = true;
                flag2 = true;
                break;
            }
            case 2: {
                message = MessageFormat.format("召唤兽#<G>{0}#获得了变色{1}", pet.getPetName(), theColor);
                MessagePusher.pushMessage(role, new RespHint(16, (short)1));
                flag2 = true;
                break;
            }
        }
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        if (isG) {
            final byte byteG = pet.getGrowthDan().byteValue();
            list.add(new FiedValue("growth_dan", (byte)1, byteG));
        }
        if (flag2) {
            final short[] sendPetColor = this.petService.getSendPetColor(pet.getColorList());
            if (sendPetColor != null && sendPetColor.length > 1) {
                list.add(new FiedValue("currentColor", (byte)2, sendPetColor[0]));
                list.add(new FiedValue("colorList", (byte)2, sendPetColor[1]));
            }
        }
        if (list.size() > 0) {
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
        }
        return message;
    }
}
