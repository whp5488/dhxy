package com.kitty.system.game.bag.helper;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.bag.message.RespZhenLuCasePet;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("ZhenLuCase")
public class ZhenLuCaseHandler extends BagHandler {
  public String acceptOperate(Role role, Integer pos, Integer num, String param) {
    int serial = Integer.valueOf(param).intValue();
    SysOtherItem otherItem = (SysOtherItem)ItemDataPool.data_other_item.get(pos);
    if (otherItem.getParam() == null || otherItem.getParam().isEmpty())
      return null; 
    List<Item> list = (List<Item>)JsonUtils.string2Collection(otherItem.getParam(), List.class, Item.class);
    Item item = list.stream().filter(e -> e.getItem().equals(Integer.valueOf(serial))).findFirst().orElse(null);
    if (item == null)
      return null; 
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(item.getItem());
    if (sysPet == null)
      return null; 
    PetService petService = SpringUtils.getPetService();
    boolean flag = petService.canGetPet(role, sysPet, false);
    if (!flag)
      return null; 
    Integer integer = this.bagService.consumeBagGoods(role, otherItem.getSerial().intValue(), item.getNum().intValue(), true, true, "珍露兑换");
    if (integer != null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(String.format("缺少#<II%d>##<%s>[%s]#X%d", new Object[] { otherItem.getSerial(), DataGlobalFunc.getColours(otherItem.getItemPJ().intValue(), true), otherItem.getName(), integer })));
      return null;
    } 
    Pet pet = petService.addPet(role, sysPet, 0, "珍露兑换");
    if (pet != null) {
      petService.loadPet(role, pet);
    } else {
      this.logger.error("珍露兑换宠物失败，道具编号 = {}，合成宠物编号 = {}", otherItem.getSerial(), sysPet.getSerial());
    } 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespZhenLuCasePet(pet));
    return null;
  }
}
