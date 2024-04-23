package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysSinBieEq;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("EquipPurify")
public class EquipPurifyHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "需要{0}个八荒遗风";
  
  public static final int SERIAL = 75031;
  
  public static final int SILVER = 5000000;
  
  public static final int ITEM_NUM = 2;
  
  public static final double MIN = 0.005D;
  
  public static final double MAX = 0.05D;
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    Long id = Long.valueOf(param);
    EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
    if (equipInfo.getTips() != null) {
      this.logger.error("五阶仙器炼器：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    int pj = getPj(equip.getType());
    if (pj != 5)
      return null; 
    int count = expend(type.intValue());
    Integer goods = this.bagService.getBagGoods(role, 75031, count * 2);
    if (goods != null)
      return MessageFormat.format("需要{0}个八荒遗风", new Object[] { goods }); 
    boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, (5000000 * count));
    if (!flag)
      return null; 
    boolean equipPurify = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (5000000 * count), "EquipPurify");
    if (!equipPurify)
      return null; 
    Integer integer = this.bagService.consumeBagGoods(role, 75031, count * 2, true, true, "仙器炼器");
    if (integer != null)
      return null; 
    BagEquip calculate = new BagEquip();
    List<EquipNeo> list = new ArrayList<>();
    BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
    int bn = DataGlobalFunc.getGemNum(equip);
    SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(Integer.valueOf(equip.getType()));
    List<List<FiedEquip>> redo = new ArrayList<>();
    double refining = equip.getRefining();
    for (int i = 0; i < count; i++) {
      refining = getRefining(refining);
      double max = (0.005D * refining <= 0.0D) ? 0.005D : (0.005D * refining + 1.0D);
      double odds = RandomUtil.getRandomDouble(Double.valueOf(0.005D), Double.valueOf((max > 0.05D) ? 0.05D : max));
      List<FiedEquip> field = MakeAttribute.createBaseBieEq(refining, bieEq.getCoef(), bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, SkillAi.canAffectOnPro(odds));
      calculate.setField(field);
      Integer score = MakeAttribute.calEquipScore(calculate);
      field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
      list.add(new EquipNeo(field));
      redo.add(field);
    } 
    equip.setRefining(refining);
    MessagePusher.pushMessage(role, (Message)new RespPreUpgradeEquip((byte)2, param, list, (byte)type.intValue()));
    equip = remoldPurifySave(equip, redo, type.intValue());
    equip = updateRemold(equip, bn);
    refresh(role, equip, pos.intValue(), id, false);
    return null;
  }
  
  public double getRefining(double refining) {
    int round = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(100));
    if (round > 60) {
      refining++;
      boolean flag = SkillAi.canAffectOnPro(0.005D);
      if (flag) {
        double v = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(RandomUtil.getRandomDouble(Double.valueOf(1.5D), Double.valueOf(2.2D))));
        refining += v;
      } 
    } else if (round > 15 && round < 35) {
      refining--;
    } 
    refining = (refining < 0.0D) ? 0.0D : refining;
    refining = (refining > 100.0D) ? 100.0D : refining;
    return refining;
  }
  
  public BagEquip updateRemold(BagEquip equip, int bn) {
    if (equip.getRedoGeneralHistory().size() > 0) {
      int serial = fieldSerial(equip.getRedoGeneralHistory());
      equip.setRedoGeneralHistory(recastField(equip, serial, bn));
    } 
    if (equip.getRedoHighHistory().size() > 0) {
      List<List<FiedEquip>> redoHighHistory = new ArrayList<>();
      for (List<FiedEquip> fiedEquips : (Iterable<List<FiedEquip>>)equip.getRedoHighHistory()) {
        int serial = fieldSerial(fiedEquips);
        redoHighHistory.add(recastField(equip, serial, bn));
      } 
      equip.setRedoHighHistory(redoHighHistory);
    } 
    if (equip.getRedoSuperHistory().size() > 0) {
      List<List<FiedEquip>> redoSuperHistory = new ArrayList<>();
      for (List<FiedEquip> fiedEquips : (Iterable<List<FiedEquip>>)equip.getRedoSuperHistory()) {
        int serial = fieldSerial(fiedEquips);
        redoSuperHistory.add(recastField(equip, serial, bn));
      } 
      equip.setRedoSuperHistory(redoSuperHistory);
    } 
    return equip;
  }
  
  public List<FiedEquip> recastField(BagEquip equip, int serial, int bn) {
    SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(Integer.valueOf(serial));
    List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, bieEq.getCoef(), equip.getRefining());
    Integer score = MakeAttribute.calEquipScore(equip);
    field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(score.intValue() + bieEq.getBaseScore())));
    field.add(new FiedEquip(Integer.valueOf(6680), Integer.valueOf(bieEq.getSerial())));
    return field;
  }
}
