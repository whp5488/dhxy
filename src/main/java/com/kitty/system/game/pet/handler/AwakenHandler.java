package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysPet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("AwakenHandler")
public class AwakenHandler extends FeedPetHandler {
  private static final String SUCCESS = "消耗玄元丹X{0}，{1}的觉醒度成功突破至#<G>{2}#";
  
  private static final String FAIL = "消耗玄元丹X{0}，突破失败，少侠再接再厉！";
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null)
      return MessageFormat.format("#<Y>{0}#宠物配置未找到，无法进行此操作。", new Object[] { pet.getPetName() }); 
    Integer[] consume = consume(sysPet);
    if (consume.length <= 0)
      return null; 
    boolean flag = true;
    short awaken = pet.getAwaken();
    int num = consume[awaken].intValue();
    if (param.intValue() > 1) {
      num /= 3;
      flag = false;
    } 
    Integer integer = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 75403, num, true, true, "觉醒突破：" + pet.getPetId());
    if (integer != null)
      return null; 
    if (flag) {
      success(role.getId().longValue(), pet, num);
    } else if (ratio()) {
      success(role.getId().longValue(), pet, num);
    } else {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(MessageFormat.format("消耗玄元丹X{0}，突破失败，少侠再接再厉！", new Object[] { Integer.valueOf(num) })));
    } 
    return null;
  }
  
  public void success(long id, Pet pet, int num) {
    pet.setAwaken((short)(pet.getAwaken() + 1));
    this.petService.calculateBase(id, pet);
    this.petService.getArenaScore(id, pet);
    pet.save();
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(pet.getPetId().intValue());
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("awaken", (byte)2, Short.valueOf(pet.getAwaken())));
    respUpdate.setList(list);
    MessagePusher.pushMessage(id, (Message)respUpdate);
    MessagePusher.pushMessage(id, (Message)new RespMsg(MessageFormat.format("消耗玄元丹X{0}，{1}的觉醒度成功突破至#<G>{2}#", new Object[] { Integer.valueOf(num), pet.getPetName(), Short.valueOf(pet.getAwaken()) })));
  }
  
  public Integer[] consume(SysPet dataPet) {
    Integer[] consume = new Integer[0];
    switch (dataPet.getLevelType().intValue()) {
      case 2:
        consume = new Integer[] { Integer.valueOf(3), Integer.valueOf(6), Integer.valueOf(12), Integer.valueOf(24), Integer.valueOf(48), Integer.valueOf(96) };
        break;
      case 3:
        consume = new Integer[] { Integer.valueOf(15), Integer.valueOf(30), Integer.valueOf(60), Integer.valueOf(120), Integer.valueOf(240), Integer.valueOf(480) };
        break;
      case 4:
        consume = new Integer[] { Integer.valueOf(90), Integer.valueOf(180), Integer.valueOf(360), Integer.valueOf(720), Integer.valueOf(1440), Integer.valueOf(2880) };
        break;
      case 5:
        consume = new Integer[] { Integer.valueOf(150), Integer.valueOf(300), Integer.valueOf(600), Integer.valueOf(1200), Integer.valueOf(2400), Integer.valueOf(4800) };
        break;
    } 
    return consume;
  }
  
  public boolean ratio() {
    double pow = Math.pow(0.35D, BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(RandomUtil.round(Integer.valueOf(4), Integer.valueOf(6)))));
    return SkillAi.canAffectOnPro(pow);
  }
}
