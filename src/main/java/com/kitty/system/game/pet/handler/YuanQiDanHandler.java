package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.PetUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("YuanQiDanHandler")
public class YuanQiDanHandler extends FeedPetHandler {
  private static final int SERIAL = 75100;
  
  private static final String ERROR = "背包没有元气丹";
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    Integer growthDan = pet.getGrowthDan();
    Map<Integer, SysPet> petMap = PetDataPool.petMap;
    SysPet sysPet = petMap.get(pet.getPetType());
    List<Byte> sysPetColor = sysPet.getColor();
    List<Byte> colors = new ArrayList<>();
    for (Byte b : sysPetColor) {
      if (b != null)
        colors.add(b); 
    } 
    if (growthDan.intValue() == 1) {
      if (sysPet == null)
        return "系统出错"; 
      if (pet.getColorList().size() >= sysPetColor.size())
        return "此宠物颜色已经达到峰值，无法再增加"; 
    } 
    Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
    SysDataStuff sysDataStuff = data_stuff.get(param);
    Integer applyObjectSerial = sysDataStuff.getApplyObjectSerial();
    if (param.intValue() != 75100) {
      if (applyObjectSerial == null)
        return "编号不属于元气丹范围"; 
      if (!applyObjectSerial.equals(pet.getPetType()))
        return "此元气丹不能给本宠物使用"; 
    } 
    Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, param.intValue(), 1, true, true, "元气丹");
    if (goods != null)
      return "背包没有元气丹"; 
    Byte theColor = null;
    int property = 0;
    if (growthDan.intValue() == 0) {
      pet.setGrowthDan(Integer.valueOf(1));
      this.petService.getArenaScore(role.getId().longValue(), pet);
      double percent = 0.1D;
      boolean bool = SkillAi.canAffectOnPro(percent);
      if (bool) {
        theColor = Byte.valueOf(PetUtils.addPetColor(pet, colors));
        property = 1;
      } 
    } else {
      for (Map<String, Byte> map : (Iterable<Map<String, Byte>>)pet.getColorList()) {
        Byte color = map.get("color");
        colors.remove(color);
      } 
      theColor = Byte.valueOf(PetUtils.addPetColor(pet, colors));
      property = 2;
    } 
    pet.save();
    String message = null;
    boolean flag = false;
    boolean isG = false;
    switch (property) {
      case 0:
        message = MessageFormat.format("召唤兽#<G>{0}#获得了高成长", new Object[] { pet.getPetName() });
        MessagePusher.pushMessage(role, (Message)new RespHint(15, (short)1));
        isG = true;
        break;
      case 1:
        message = MessageFormat.format("召唤兽#<G>{0}#获得了高成长及变色{1}", new Object[] { pet.getPetName(), theColor });
        MessagePusher.pushMessage(role, (Message)new RespHint(16, (short)1));
        MessagePusher.pushMessage(role, (Message)new RespHint(15, (short)1));
        isG = true;
        flag = true;
        break;
      case 2:
        message = MessageFormat.format("召唤兽#<G>{0}#获得了变色{1}", new Object[] { pet.getPetName(), theColor });
        MessagePusher.pushMessage(role, (Message)new RespHint(16, (short)1));
        flag = true;
        break;
    } 
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(pet.getPetId().intValue());
    List<FiedValue> list = new ArrayList<>();
    if (isG) {
      byte byteG = pet.getGrowthDan().byteValue();
      list.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(byteG)));
    } 
    if (flag) {
      short[] sendPetColor = this.petService.getSendPetColor(pet.getColorList());
      if (sendPetColor != null && sendPetColor.length > 1) {
        list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
        list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
      } 
    } 
    if (list.size() > 0) {
      respUpdate.setList(list);
      MessagePusher.pushMessage(role, (Message)respUpdate);
    } 
    return message;
  }
}
