package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.pool.ItemDataPool;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("EquipDisconnect")
public class EquipDisconnectHandler extends UpgradeHandler {
  public static final int SERIAL = 75031;
  
  public static final int SERIAL_75048 = 75048;
  
  public static final String NOT_FIND = "需要拆卸宝石后才可操作";
  
  public static final String NOT_FIND_SIN = "未装备且无宝石镶嵌的一阶仙器才可以拆解哦";
  
  public static final String SUCCESS = "消耗{0}×1得到了{1}";
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    String msg = null;
    try {
      Long id = Long.valueOf(param);
      if (type.intValue() > 0) {
        Map<Long, BagGoods> goodsMap = this.bagService.getBagGoods(role);
        BagGoods goods = goodsMap.get(id);
        if (goods != null) {
          Integer classify = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(goods.getSerial()));
          if (pos.equals(Integer.valueOf(goods.getPos())) && classify.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY_EGG.getValue()))) {
            this.bagService.deleteRoleItem(role, pos.intValue(), "未鉴定配饰分解");
            this.bagService.addGoods(role, 75048, 1, 0, true, true, "未鉴定配饰分解");
            SysDataAcsryEgg egg = (SysDataAcsryEgg)ItemDataPool.acsryEggMap.get(Integer.valueOf(goods.getSerial()));
            if (egg != null)
              msg = MessageFormat.format("消耗{0}×1得到了{1}", new Object[] { String.format("#<II%d>##<r:36,g:210,b:224>[%s]#", new Object[] { Integer.valueOf(egg.getSerial()), egg.getName() }), String.format("#<II%d>##<r:36,g:210,b:224>[玉符]#", new Object[] { Integer.valueOf(75048) }) }); 
            return msg;
          } 
        } 
      } 
      EquipInfo equipInfo = getEquip(role, pos.intValue(), id);
      if (equipInfo.getTips() != null) {
        this.logger.error("装备分解拆解获取装备错误：获取物品所在位置为 = {}，获取物品ID = {}", pos, id);
        return equipInfo.getTips();
      } 
      msg = equip(role, equipInfo, msg);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
    return msg;
  }
  
  public String equip(Role role, EquipInfo equipInfo, String msg) {
    BagEquip equip = equipInfo.getEquip();
    int bn = 0;
    Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue().intValue();
    } 
    Integer large = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(equip.getType()));
    if (bn > 0) {
      if (large.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue())
        return "未装备且无宝石镶嵌的一阶仙器才可以拆解哦"; 
      return "需要拆卸宝石后才可操作";
    } 
    if (large.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
      SysNewBieEq bieEq = (SysNewBieEq)EquipDataPool.newBieEq.get(Integer.valueOf(equip.getType()));
      if (bieEq != null) {
        int price = (int)(bieEq.getPrice() * 0.9D);
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(price), "初级装备分解", true, false);
        this.bagService.deleteRoleEquip(role, equip, "初级装备分解");
        msg = MessageFormat.format("消耗{0}×1得到了{1}", new Object[] { String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(equip.getType()), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }), String.format("#<IR%d>#,%s", new Object[] { Const.RESTYPE_COIN, MoneyUtil.colours(Long.valueOf(price)) }) });
      } 
    } else if (large.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
      SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(equip.getType()));
      if (DataGlobalFunc.seniorIsGem(bieEq, equip)) {
        Map<Integer, Integer> consume = consume(bieEq.getNeedGrade());
        String materials = "";
        for (Integer key : consume.keySet()) {
          Integer amount = consume.get(key);
          if (key.equals(Const.RESTYPE_COIN)) {
            materials = materials + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(amount.intValue())) + ",";
            this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(amount.intValue()), "高级装备分解", true, false);
            continue;
          } 
          SysDataStuff stuff = (SysDataStuff)ItemDataPool.data_stuff.get(key);
          materials = materials + String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { stuff.getSerial(), stuff.getSerial(), stuff.getSerial(), stuff.getName() });
          if (amount.intValue() > 1) {
            materials = materials + "x" + amount + ",";
          } else {
            materials = materials + ",";
          } 
          this.bagService.consumeBagGoods(role, key.intValue(), amount.intValue(), true, true, "装备分解-高级装备");
        } 
        this.bagService.deleteRoleEquip(role, equip, "高级装备分解");
        msg = MessageFormat.format("消耗{0}×1得到了{1}", new Object[] { String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(equip.getType()), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }), materials.substring(0, materials.length() - 1) });
      } else {
        msg = "珍品装备不可以分解哟";
      } 
    } else if (large.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
      String tips = DataGlobalFunc.needGradeJudge(2, 130, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
      if (tips != null)
        return tips; 
      int pj = getPj(equip.getType());
      if (pj == 1) {
        boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 1000000L, "EquipDisconnect");
        if (flag) {
          this.bagService.deleteRoleEquip(role, equip, "仙器装备分解");
          this.bagService.addGoods(role, 75031, 20, equip.getBd(), true, true, "仙器装备分解");
          msg = MessageFormat.format("消耗{0}×1得到了{1}", new Object[] { String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(equip.getType()), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName() }), String.format("#<II%d>#×%d", new Object[] { Integer.valueOf(75031), Integer.valueOf(20) }) });
        } 
      } else {
        return "未装备且无宝石镶嵌的一阶仙器才可以拆解哦";
      } 
    } else if (large.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
      SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(equip.getType()));
      if (acsry == null)
        return null; 
      SysDataAcsrySuit acsrySuit = (SysDataAcsrySuit)EquipDataPool.acsrySuitMap.get(acsry.getSuiteType());
      if (acsrySuit == null)
        return null; 
      int num = decompose(equip.getProductOrder(), acsrySuit.getGrade());
      if (num > 0) {
        this.bagService.deleteRoleEquip(role, equip, "配饰分解");
        this.bagService.addGoods(role, 75048, num, 0, true, true, "配饰分解");
        msg = MessageFormat.format("消耗{0}×1得到了{1}", new Object[] { String.format("#<II%d>##<r:36,g:210,b:224>[%s]#", new Object[] { Integer.valueOf(acsry.getSerial()), acsry.getName() }), String.format("#<II%d>##<r:36,g:210,b:224>[玉符]#×%d", new Object[] { Integer.valueOf(75048), Integer.valueOf(num) }) });
      } 
    } 
    return msg;
  }
  
  public Map<Integer, Integer> consume(int grade) {
    Map<Integer, Integer> map = new LinkedHashMap<>();
    if (grade == 40) {
      map.put(Const.RESTYPE_COIN, Integer.valueOf(100000));
    } else if (grade == 60) {
      map.put(Const.RESTYPE_COIN, Integer.valueOf(200000));
    } else if (grade == 80) {
      map.put(Const.RESTYPE_COIN, Integer.valueOf(300000));
    } else if (grade == 100) {
      map.put(Integer.valueOf(75030), Integer.valueOf(1));
    } else if (grade == 120) {
      map.put(Integer.valueOf(75030), Integer.valueOf(2));
      map.put(Integer.valueOf(75047), Integer.valueOf(2));
    } else if (grade == 140) {
      map.put(Integer.valueOf(75030), Integer.valueOf(5));
      map.put(Integer.valueOf(75047), Integer.valueOf(5));
    } 
    return map;
  }
  
  public int decompose(int suit, int grade) {
    int num = 0;
    if (suit == ACC_GRADE.PLAY_WITH.getValue()) {
      if (grade == 1) {
        num = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(2));
      } else if (grade == 2) {
        num = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(3));
      } else if (grade == 3) {
        num = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(4));
      } 
    } else if (suit == ACC_GRADE.RESERVE.getValue()) {
      if (grade == 1) {
        num = RandomUtil.round(Integer.valueOf(2), Integer.valueOf(3));
      } else if (grade == 2) {
        num = RandomUtil.round(Integer.valueOf(2), Integer.valueOf(4));
      } else if (grade == 3) {
        num = RandomUtil.round(Integer.valueOf(2), Integer.valueOf(5));
      } 
    } else if (suit == ACC_GRADE.PRICELESS.getValue()) {
      if (grade == 1) {
        num = RandomUtil.round(Integer.valueOf(3), Integer.valueOf(4));
      } else if (grade == 2) {
        num = RandomUtil.round(Integer.valueOf(3), Integer.valueOf(5));
      } else if (grade == 3) {
        num = RandomUtil.round(Integer.valueOf(3), Integer.valueOf(6));
      } 
    } else if (suit == ACC_GRADE.TRENDS.getValue()) {
      if (grade == 1) {
        num = RandomUtil.round(Integer.valueOf(4), Integer.valueOf(5));
      } else if (grade == 2) {
        num = RandomUtil.round(Integer.valueOf(4), Integer.valueOf(6));
      } else if (grade == 3) {
        num = RandomUtil.round(Integer.valueOf(4), Integer.valueOf(7));
      } 
    } 
    return num;
  }
}
