package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysMagicalBieEq;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("EquipMagicalUpGrade")
public class EquipMagicalUpGradeHandler extends UpgradeHandler {
  public static final String SUCCESS = "升级成功，你得到了一件{0}级{1}";
  
  public static final String FAIL = "升级失败，由于#<R>{0}#碎了，你获得了#<R>{1}快神兵碎片#";
  
  public static final String NOT_STUFF = "请先选择一种低级神兵作为材料";
  
  public static final int OFFSET = 75042;
  
  public static final int STUFF_ONE = 75047;
  
  public static final int STUFF_TWO = 75030;
  
  public static final int DEGREE_RAISING = 100000;
  
  public static final double[] ODDS = new double[] { 0.5D, 0.35D, 0.15D, 0.05D, 5.0E-4D };
  
  public static final int[] SILVER = new int[] { 2000000, 1500000, 1500000, 1000000, 1000000 };
  
  public static final String BUGLE = "#<B>{0}#紧张得闭上了眼，只觉得风雷奔涌，霸气横流。睁开眼，何大雷已经送上#<G>{1}级神兵#{2}!";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    String[] split = param.split("-");
    if (split.length < 2)
      return "请先选择一种低级神兵作为材料"; 
    Long id = Long.valueOf(split[0]);
    Long aux_id = Long.valueOf(split[1]);
    EquipInfo equipInfo = getEquip(role, 1, id);
    if (equipInfo.getTips() != null) {
      this.logger.error("神兵升阶：获取物品ID = {}", id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    Map<Integer, SysMagicalBieEq> magicalBieEq = EquipDataPool.magicalBieEq;
    SysMagicalBieEq bieEq = magicalBieEq.get(Integer.valueOf(equip.getType()));
    if (getPj(bieEq.getSerial()) <= 1) {
      boolean deduct = deduct(role, getPj(bieEq.getSerial()), getPj(bieEq.getSerial()), type.intValue());
      if (deduct) {
        boolean flag = rate(getPj(bieEq.getSerial()), type.intValue(), 0);
        if (flag) {
          succeed(role, magicalBieEq, equip);
        } else {
          MessagePusher.pushMessage(role, (Message)new RespHint(2, (short)0));
        } 
      } 
    } else {
      BagEquip auxEquip = getEquip(role, 1, aux_id).getEquip();
      if (auxEquip == null)
        return "请先选择一种低级神兵作为材料"; 
      if (equip != null && auxEquip != null) {
        SysMagicalBieEq auxBieEq = magicalBieEq.get(Integer.valueOf(auxEquip.getType()));
        boolean deduct = deduct(role, getPj(bieEq.getSerial()), getPj(auxBieEq.getSerial()), type.intValue());
        if (deduct) {
          boolean flag = rate(getPj(bieEq.getSerial()), type.intValue(), getPj(auxBieEq.getSerial()));
          if (flag) {
            this.bagService.deleteRoleEquip(role, auxEquip, "升阶成功删除材料装备");
            succeed(role, magicalBieEq, equip);
          } else {
            failure(role, auxEquip, getPj(auxBieEq.getSerial()));
          } 
        } 
      } 
    } 
    return null;
  }
  
  public void succeed(Role role, Map<Integer, SysMagicalBieEq> magicalBieEq, BagEquip equip) {
    int bn = 0;
    Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue().intValue();
    } 
    BagEquip newEq = new BagEquip();
    BeanUtils.copyBeanProp(newEq, equip);
    SysMagicalBieEq sysMagicalBieEq = magicalBieEq.get(Integer.valueOf(equip.getType() + 100000));
    List<FiedEquip> field = MakeAttribute.createBaseBieEq(sysMagicalBieEq.getBaseAttr(), sysMagicalBieEq.getMaxEndure(), sysMagicalBieEq.getSerial(), sysMagicalBieEq.getRndRange(), bn);
    newEq = clearHistory(newEq);
    newEq.setField(field);
    newEq.setType(sysMagicalBieEq.getSerial());
    newEq.setName(sysMagicalBieEq.getName());
    newEq.setField(field);
    Integer score = MakeAttribute.calEquipScore(newEq);
    field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + sysMagicalBieEq.getBaseScore())));
    newEq.setField(field);
    refresh(role, newEq, equip.getPos(), equip.getId(), true);
    MessagePusher.pushMessage(role, (Message)new RespHint(2, (short)1));
    animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue());
    int pj = getPj(sysMagicalBieEq.getSerial());
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级成功，你得到了一件{0}级{1}", new Object[] { Integer.valueOf(pj), sysMagicalBieEq.getName() }), false));
    if (pj >= 3)
      this.chatService.sendLaba(MessageFormat.format("#<B>{0}#紧张得闭上了眼，只觉得风雷奔涌，霸气横流。睁开眼，何大雷已经送上#<G>{1}级神兵#{2}!", new Object[] { role.getName(), Integer.valueOf(pj), String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(newEq.getType()), newEq.getId(), role.getRoleId(), Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), newEq.getName() }) })); 
    role.save();
  }
  
  public void failure(Role role, BagEquip equip, int grade) {
    MessagePusher.pushMessage(role, (Message)new RespHint(2, (short)0));
    this.bagService.deleteRoleEquip(role, equip, "升阶失败删除材料装备");
    int num = RandomUtil.round(Integer.valueOf(1), Integer.valueOf((grade > 3) ? 3 : grade));
    try {
      Thread.sleep(1500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } 
    this.bagService.addGoods(role, 75042, num, 0, true, true, "神兵升阶");
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级失败，由于#<R>{0}#碎了，你获得了#<R>{1}快神兵碎片#", new Object[] { equip.getName(), Integer.valueOf(num) }), false));
    role.save();
  }
  
  public boolean deduct(Role role, int grade, int pj, int type) {
    int silver = SILVER[grade - 1];
    if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver))
      return false; 
    if (type > 0) {
      int num = grade * pj;
      Integer numOne = this.bagService.getBagGoods(role, 75047, num);
      Integer numTwo = this.bagService.getBagGoods(role, 75030, num);
      if (numOne != null || numTwo != null) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "请先选择一种低级神兵作为材料", false));
        return false;
      } 
      Integer integer = this.bagService.consumeBagGoods(role, 75047, num, true, true, "神兵升阶");
      if (integer != null)
        return false; 
      Integer integer1 = this.bagService.consumeBagGoods(role, 75030, num, true, true, "神兵升阶");
      if (integer1 != null)
        return false; 
    } 
    this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipMagicalUpGrade");
    return true;
  }
  
  public static boolean rate(int grade, int prop, int helpLevel) {
    double rate = ODDS[grade - 1];
    if (prop > 0)
      rate += 0.01D; 
    rate += helpLevel * 0.01D;
    return SkillAi.canAffectOnPro(rate);
  }
}
