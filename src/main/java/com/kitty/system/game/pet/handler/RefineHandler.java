package com.kitty.system.game.pet.handler;

import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.pet.PetUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.Random;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.PetBase;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.pet.message.RespUpgradeTaskPet;
import com.kitty.system.enums.pet.ConciseType;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.MessageFormat;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.enums.pet.FeedType;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.Pet;
import org.springframework.stereotype.Component;

@Component("RefineHandler")
public class RefineHandler extends FeedPetHandler
{
    private static final String SUCCESS = "恭喜你，洗练成功，#<G>{0}#的属性已重新生成。";
    private static final String FAIL = "很抱歉，你的#<G>{0}#所有属性已满，无法进行洗练。";
    private static final String FAIL_LACK = "很抱歉，金柳露不足#<R>{0}#个，无法进行洗练。";
    
    public RefineHandler() {
    }
    
    @Override
    public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
        Integer goodSerial = Integer.valueOf(75052);
        boolean flag = false;
        if (FeedType.REFINE_HIGH.getType().equals(type)) {
            goodSerial = Integer.valueOf(75053);
            flag = true;
        }
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null) {
            return MessageFormat.format("#<Y>{0}#宠物配置未找到，无法进行此操作。", pet.getPetName());
        }
        if ((int)sysPet.getStaticInitVarScore() >= 90) {
            return null;
        }
        Integer num = this.consume(role, sysPet, Integer.valueOf((int)param - 1), goodSerial);
        if (num == null || (int)num <= 0) {
            boolean isTrue = false;
            if ((int)pet.getPetBase().getHp_base() < (int)sysPet.getHpMax()) {
                isTrue = true;
            }
            if ((int)pet.getPetBase().getMp_base() < (int)sysPet.getMpMax()) {
                isTrue = true;
            }
            if ((int)pet.getPetBase().getAp_base() < (int)sysPet.getApMax()) {
                isTrue = true;
            }
            if ((int)pet.getPetBase().getSp_base() < (int)sysPet.getSpMax()) {
                isTrue = true;
            }
            int growUpMax = Arrays.stream(sysPet.getGrowthRate()).max().getAsInt();
            if ((int)pet.getPetBase().getGrow_up() < growUpMax) {
                isTrue = true;
            }
            if (!isTrue) {
                return MessageFormat.format("很抱歉，你的#<G>{0}#所有属性已满，无法进行洗练。", pet.getPetName());
            }
            List<PetBase> list = new ArrayList<>();
            Integer value = ConciseType.getValue(param);
            for (int i = 0; i < (int)value; ++i) {
                PetBase petBase = this.petService.createPetBase(sysPet, Integer.valueOf(2));
                list.add(petBase);
            }
            if (flag) {
                this.trigger(role, pet, (int)value);
            }
            pet.getDarkBaseMap().put(param, list);
            ((Pet)this.petService.selectPet(role).get(pet.getPetId())).setDarkBaseMap(pet.getDarkBaseMap());
            RespUpgradeTaskPet respUpgradeTaskPet = new RespUpgradeTaskPet(pet.getPetId(), list);
            pet.save();
            MessagePusher.pushMessage(role, respUpgradeTaskPet);
            return MessageFormat.format("恭喜你，洗练成功，#<G>{0}#的属性已重新生成。", pet.getPetName());
        }
        else {
            return MessageFormat.format("很抱歉，金柳露不足#<R>{0}#个，无法进行洗练。", num);
        }
    }
    
    public Integer consume(Role role, SysPet dataPet, Integer type, Integer goodSerial) {
        Integer consume = Integer.valueOf(1);
        if ((int)dataPet.getLevelType() == 1) {
            consume = Const.NORMAL_PET_REFINE_CONSUME[(int)type];
        }
        else if ((int)dataPet.getLevelType() == 2) {
            consume = Const.PROTECT_PET_REFINE_CONSUME[(int)type];
        }
        else if ((int)dataPet.getLevelType() == 3) {
            consume = Const.SPECIAL_PET_REFINE_CONSUME[(int)type];
        }
        else if ((int)dataPet.getLevelType() == 4) {
            consume = Const.NIUBI_PET_REFINE_CONSUME[(int)type];
        }
        else if ((int)dataPet.getLevelType() == 5) {
            consume = Const.TOP_PET_REFINE_CONSUME[(int)type];
        }
        return ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)goodSerial, (int)consume, true, true, "宠物洗炼");
    }
    
    public void trigger(Role role, Pet pet, int num) {
        Random random = new Random();
        boolean high_growth = true;
        boolean is_color = true;
        int i = 0;
        while (i < num) {
            if ((int)pet.getGrowthDan() < 1) {
                boolean is_success = random.nextInt(1000) + 1 <= 20;
                if (is_success) {
                    high_growth = false;
                    pet.setGrowthDan(Integer.valueOf(1));
                }
            }
            if ((int)pet.getGrowthDan() >= 1 && is_color) {
                boolean is_success = random.nextInt(1000) + 1 <= 20;
                if (is_success) {
                    if (high_growth) {
                        double percent = 0.1;
                        if (SkillAi.canAffectOnPro(percent)) {
                            is_color = false;
                        }
                    }
                    else {
                        is_color = false;
                    }
                }
            }
            if (!high_growth || !is_color) {
                int hint = 15;
                RespUpdate respUpdate = new RespUpdate();
                List<FiedValue> listFied = new ArrayList<>();
                respUpdate.setId((int)pet.getPetId());
                if (!high_growth) {
                    this.petService.getArenaScore((long)role.getId(), pet);
                    listFied.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(pet.getGrowthDan().byteValue())));
                }
                if (!is_color) {
                    List<Byte> addPetColors = PetUtils.getAddPetColors(pet);
                    if (addPetColors != null && addPetColors.size() > 0) {
                        PetUtils.addPetColor(pet, addPetColors);
                    }
                    short[] sendPetColor = this.petService.getSendPetColor(pet.getColorList());
                    if (sendPetColor != null && sendPetColor.length > 1) {
                        listFied.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
                        listFied.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
                    }
                    hint = 16;
                }
                respUpdate.setList(listFied);
                MessagePusher.pushMessage(role, respUpdate);
                MessagePusher.pushMessage(role, new RespHint(hint, (short)1));
                break;
            }
            else {
                ++i;
            }
        }
    }
}
