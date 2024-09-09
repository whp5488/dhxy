package com.kitty.system.game.pet.handler;

import java.util.Iterator;
import java.util.List;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import java.text.MessageFormat;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.PetUtils;
import java.util.Map;
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
    
    public YuanQiDanHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        Integer growthDan = pet.getGrowthDan();
        Map<Integer, SysPet> petMap = PetDataPool.petMap;
        SysPet sysPet = (SysPet)petMap.get(pet.getPetType());
        List<Byte> sysPetColor = sysPet.getColor();
        List<Byte> colors = new ArrayList<>();
        for (Byte b : sysPetColor) {
            if (b != null) {
                colors.add(b);
            }
            else {
                continue;
            }
        }
        if ((int)growthDan == 1) {
            if (sysPet == null) {
                return "系统出错";
            }
            if (pet.getColorList().size() >= sysPetColor.size()) {
                return "此宠物颜色已经达到峰值，无法再增加";
            }
        }
        Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
        SysDataStuff sysDataStuff = (SysDataStuff)data_stuff.get(param);
        Integer applyObjectSerial = sysDataStuff.getApplyObjectSerial();
        if ((int)param != 75100) {
            if (applyObjectSerial == null) {
                return "编号不属于元气丹范围";
            }
            if (!applyObjectSerial.equals(pet.getPetType())) {
                return "此元气丹不能给本宠物使用";
            }
        }
        Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)param, 1, true, true, "元气丹");
        if (goods != null) {
            return "背包没有元气丹";
        }
        Byte theColor = null;
        int property = 0;
        if ((int)growthDan == 0) {
            pet.setGrowthDan(Integer.valueOf(1));
            this.petService.getArenaScore((long)role.getId(), pet);
            double percent = 0.1;
            boolean flag = SkillAi.canAffectOnPro(percent);
            if (flag) {
                theColor = Byte.valueOf(PetUtils.addPetColor(pet, colors));
                property = 1;
            }
        }
        else {
            for (Map<String, Byte> map : pet.getColorList()) {
                Byte color = (Byte)map.get("color");
                colors.remove(color);
            }
            theColor = Byte.valueOf(PetUtils.addPetColor(pet, colors));
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
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)pet.getPetId());
        List<FiedValue> list = new ArrayList<>();
        if (isG) {
            byte byteG = pet.getGrowthDan().byteValue();
            list.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(byteG)));
        }
        if (flag2) {
            short[] sendPetColor = this.petService.getSendPetColor(pet.getColorList());
            if (sendPetColor != null && sendPetColor.length > 1) {
                list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
                list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
            }
        }
        if (list.size() > 0) {
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
            return message;
        }
        return message;
    }
}
