package com.kitty.system.game.instruct.handler;

import com.kitty.common.start.ServerService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.instruct.inter.NcFather;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.init.domain.SysPet;
import java.util.Map;

public class PetHandler implements NcFather {
  public void handle(Role role, String name) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1)
      return; 
    Map<Integer, Pet> map = SpringUtils.getPetService().selectPet(role);
    if (map.size() < 3) {
      SysPet sysPet = null;
      Map<Integer, SysPet> petMap = PetDataPool.petMap;
      for (Integer key : petMap.keySet()) {
        SysPet pet = petMap.get(key);
        if (name.equals(pet.getName())) {
          sysPet = pet;
          break;
        } 
      } 
      if (sysPet != null) {
        PetService bean = (PetService)SpringUtils.getBean(PetService.class);
        Pet pet = bean.addPet(role, sysPet, 0, "命令");
        bean.loadPet(role, pet);
      } else {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "未找到#<r:255,g:0,b:0>" + name + "#召唤兽信息", false));
      } 
    } 
  }
}
