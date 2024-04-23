package com.kitty.system.game.pet.handler;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.message.RespPetEffctInfo;
import com.kitty.system.game.role.domain.Role;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("GridHandler")
public class GridHandler extends FeedPetHandler {
  private static final Integer GOODS_SERIAL = Integer.valueOf(75024);
  
  private static final String SUCCESS = "消耗#<II" + GOODS_SERIAL + ">#{0}，你的#<G>{1}#成功开启#<G>第{2}技能格#";
  
  private static final String FAIL = "聚魄丹不足{0}颗";
  
  private static final String ERROR = "消耗#<II" + GOODS_SERIAL + ">#，开启失败，少侠再接再厉！";
  
  public String acceptFeed(Pet pet, Role role, Integer param, String type, int count, Object... args) {
    int need = 1;
    String msg = null;
    boolean flag = false;
    int pos = param.intValue() / 1000 - 1;
    int genre = param.intValue() % 1000;
    if (((PetSkill)pet.getSkills().get(pos)).getSerial() >= 0)
      return "该技能格子已经解封了"; 
    List<Integer> unset = new ArrayList<>();
    for (int i = 0; i < pet.getSkills().size(); i++) {
      PetSkill petSkill = pet.getSkills().get(i);
      if (petSkill.getSerial() >= 0 && petSkill.getType() != PETSKILLSUB.SUBTYPE_SPECIAL.getValue() && petSkill.getType() != PETSKILLSUB.SUB_TYPE_SOUL.getValue())
        unset.add(Integer.valueOf(i)); 
    } 
    if (need > 0) {
      if (genre == 1) {
        need = need(unset.size() + 1);
        msg = MessageFormat.format(SUCCESS, new Object[] { "#<P>[聚魄丹]#x" + need, pet.getPetName(), Integer.valueOf(unset.size() + 1) });
        flag = true;
      } else if (genre == 2) {
        flag = ratio();
        if (flag) {
          msg = MessageFormat.format(SUCCESS, new Object[] { "#<P>[聚魄丹]#", pet.getPetName(), Integer.valueOf(unset.size() + 1) });
          flag = true;
        } else {
          msg = ERROR;
        } 
      } 
      Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, GOODS_SERIAL.intValue(), need, true, true, "开启技能格子");
      if (goods != null) {
        msg = MessageFormat.format("聚魄丹不足{0}颗", new Object[] { Integer.valueOf(need) });
        flag = false;
      } 
    } else {
      msg = "召唤兽技能格子已全部开启";
    } 
    if (flag) {
      ((PetSkill)pet.getSkills().get(pos)).setSerial(0);
      pet.save();
      MessagePusher.pushMessage(role, (Message)new RespPetEffctInfo(role.getRoleId(), pet.getPetId(), pet.getSkills()));
    } 
    return msg;
  }
  
  public int need(int index) {
    int need = -1;
    if (index == 2)
      return 1; 
    if (index == 3)
      return 2; 
    if (index == 4)
      return 4; 
    if (index == 5)
      return 6; 
    if (index == 6)
      return 10; 
    if (index == 7)
      return 15; 
    if (index == 8)
      return 25; 
    if (index == 9)
      return 35; 
    if (index == 10)
      return 50; 
    if (index == 11)
      return 100; 
    if (index == 12)
      return 200; 
    return need;
  }
  
  public boolean ratio() {
    double pow = Math.pow(0.35D, BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(RandomUtil.round(Integer.valueOf(4), Integer.valueOf(6)))));
    return SkillAi.canAffectOnPro(pow);
  }
}
