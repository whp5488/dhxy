package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.enums.pet.ConciseType;
import com.kitty.system.enums.pet.FeedType;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.PetUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.message.RespUpgradeTaskPet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component("RefineHandler")
public class RefineHandler extends FeedPetHandler {
  private static final String SUCCESS = "恭喜你，洗练成功，#<G>{0}#的属性已重新生成。";
  
  private static final String FAIL = "很抱歉，你的#<G>{0}#所有属性已满，无法进行洗练。";
  
  private static final String FAIL_LACK = "很抱歉，金柳露不足#<R>{0}#个，无法进行洗练。";
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    Integer goodSerial = Integer.valueOf(75052);
    boolean flag = false;
    if (FeedType.REFINE_HIGH.getType().equals(type)) {
      goodSerial = Integer.valueOf(75053);
      flag = true;
    } 
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null)
      return MessageFormat.format("#<Y>{0}#宠物配置未找到，无法进行此操作。", new Object[] { pet.getPetName() }); 
    if (sysPet.getStaticInitVarScore().intValue() >= 90)
      return null; 
    Integer num = consume(role, sysPet, Integer.valueOf(param.intValue() - 1), goodSerial);
    if (num == null || num.intValue() <= 0) {
      boolean isTrue = false;
      if (pet.getPetBase().getHp_base().intValue() < sysPet.getHpMax().intValue())
        isTrue = true; 
      if (pet.getPetBase().getMp_base().intValue() < sysPet.getMpMax().intValue())
        isTrue = true; 
      if (pet.getPetBase().getAp_base().intValue() < sysPet.getApMax().intValue())
        isTrue = true; 
      if (pet.getPetBase().getSp_base().intValue() < sysPet.getSpMax().intValue())
        isTrue = true; 
      int growUpMax = Arrays.stream(sysPet.getGrowthRate()).max().getAsInt();
      if (pet.getPetBase().getGrow_up().intValue() < growUpMax)
        isTrue = true; 
      if (!isTrue)
        return MessageFormat.format("很抱歉，你的#<G>{0}#所有属性已满，无法进行洗练。", new Object[] { pet.getPetName() }); 
      List<PetBase> list = new ArrayList<>();
      Integer value = ConciseType.getValue(param);
      for (int i = 0; i < value.intValue(); i++) {
        PetBase petBase = this.petService.createPetBase(sysPet, Integer.valueOf(2));
        list.add(petBase);
      } 
      if (flag)
        trigger(role, pet, value.intValue()); 
      pet.getDarkBaseMap().put(param, list);
      ((Pet)this.petService.selectPet(role).get(pet.getPetId())).setDarkBaseMap(pet.getDarkBaseMap());
      RespUpgradeTaskPet respUpgradeTaskPet = new RespUpgradeTaskPet(pet.getPetId(), list);
      pet.save();
      MessagePusher.pushMessage(role, (Message)respUpgradeTaskPet);
      return MessageFormat.format("恭喜你，洗练成功，#<G>{0}#的属性已重新生成。", new Object[] { pet.getPetName() });
    } 
    return MessageFormat.format("很抱歉，金柳露不足#<R>{0}#个，无法进行洗练。", new Object[] { num });
  }
  
  public Integer consume(Role role, SysPet dataPet, Integer type, Integer goodSerial) {
    Integer consume = Integer.valueOf(1);
    if (dataPet.getLevelType().intValue() == 1) {
      consume = Const.NORMAL_PET_REFINE_CONSUME[type.intValue()];
    } else if (dataPet.getLevelType().intValue() == 2) {
      consume = Const.PROTECT_PET_REFINE_CONSUME[type.intValue()];
    } else if (dataPet.getLevelType().intValue() == 3) {
      consume = Const.SPECIAL_PET_REFINE_CONSUME[type.intValue()];
    } else if (dataPet.getLevelType().intValue() == 4) {
      consume = Const.NIUBI_PET_REFINE_CONSUME[type.intValue()];
    } else if (dataPet.getLevelType().intValue() == 5) {
      consume = Const.TOP_PET_REFINE_CONSUME[type.intValue()];
    } 
    return ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, goodSerial.intValue(), consume.intValue(), true, true, "宠物洗炼");
  }
  
  public void trigger(Role role, Pet pet, int num) {
    Random random = new Random();
    boolean high_growth = true;
    boolean is_color = true;
    for (int i = 0; i < num; i++) {
      if (pet.getGrowthDan().intValue() < 1) {
        boolean is_success = (random.nextInt(1000) + 1 <= 20);
        if (is_success) {
          high_growth = false;
          pet.setGrowthDan(Integer.valueOf(1));
        } 
      } 
      if (pet.getGrowthDan().intValue() >= 1 && is_color) {
        boolean is_success = (random.nextInt(1000) + 1 <= 20);
        if (is_success)
          if (high_growth) {
            double percent = 0.1D;
            if (SkillAi.canAffectOnPro(percent))
              is_color = false; 
          } else {
            is_color = false;
          }  
      } 
      if (!high_growth || !is_color) {
        int hint = 15;
        RespUpdate respUpdate = new RespUpdate();
        List<FiedValue> listFied = new ArrayList<>();
        respUpdate.setId(pet.getPetId().intValue());
        if (!high_growth) {
          this.petService.getArenaScore(role.getId().longValue(), pet);
          listFied.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(pet.getGrowthDan().byteValue())));
        } 
        if (!is_color) {
          List<Byte> addPetColors = PetUtils.getAddPetColors(pet);
          if (addPetColors != null && addPetColors.size() > 0)
            PetUtils.addPetColor(pet, addPetColors); 
          short[] sendPetColor = this.petService.getSendPetColor(pet.getColorList());
          if (sendPetColor != null && sendPetColor.length > 1) {
            listFied.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
            listFied.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
          } 
          hint = 16;
        } 
        respUpdate.setList(listFied);
        MessagePusher.pushMessage(role, (Message)respUpdate);
        MessagePusher.pushMessage(role, (Message)new RespHint(hint, (short)1));
        break;
      } 
    } 
  }
}
