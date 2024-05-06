package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysSinBieEq;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component("EquipSinUpGrade")
public class EquipSinUpGradeHandler extends UpgradeHandler {
  public static final String NOT_FIND_STUFF = "请添加足够的仙器";
  
  public static final String SUCCESS = "升级成功，你得到了一件{0}级{1}";
  
  public static final String NOT_STUFF = "请放入同等品阶的仙器";
  
  public static final String NOT_LEVEL = "等级升到{0}级才能升级该装备";
  
  public static final int SERIAL = 75031;
  
  public static final int DEDUCT_NUM = 30;
  
  public static final int[] SILVER = new int[] { 10000000, 20000000, 30000000, 100000000, 1000000000 };
  
  public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
    String[] split = param.split("-");
    if (split.length < 3)
      return "请放入同等品阶的仙器"; 
    Long id = Long.valueOf(split[0]);
    Long aux_id = Long.valueOf(split[1]);
    Long aux_id_two = Long.valueOf(split[2]);
    EquipInfo equipInfo = getEquip(role, 1, id);
    if (equipInfo.getTips() != null) {
      this.logger.error("角色编号：{}，仙器升阶：获取物品所在位置为 = {}，获取物品ID = {}", role.getRoleId(), pos, id);
      return equipInfo.getTips();
    } 
    BagEquip equip = equipInfo.getEquip();
    Map<Integer, SysSinBieEq> sinBieEq = EquipDataPool.sinBieEq;
    SysSinBieEq sysSinBieEq = sinBieEq.get(equip.getType());
    if (sysSinBieEq == null)
      return "该装备不可以升级"; 
    int needGrade = sysSinBieEq.getNeedGrade();
    int needRei = sysSinBieEq.getNeedRei();
    String msg = needRei + "转";
    if (role.getRelive() < needRei)
      return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
    if (role.getLevel() < needGrade)
      return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
    if (sysSinBieEq.getNextSerial() <= 0)
      return equip.getName() + "已达最大等级"; 
    int pj = getPj(equip.getType());
    boolean deduct = deduct(role, pj, aux_id, aux_id_two);
    if (deduct) {
      succeed(role, sinBieEq.get(sysSinBieEq.getNextSerial()), equip);
    } else {
      LoggerUtils.info("玩家[{}]升阶仙器错误：参数 = {}，位置 = {}" + role.getRoleId(), param, pos);
    } 
    return null;
  }
  
  public void succeed(Role role, SysSinBieEq sinBieEq, BagEquip equip) {
    int bn = 0;
    Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue();
    } 
    BagEquip newEq = new BagEquip();
    BeanUtils.copyBeanProp(newEq, equip);
    List<FiedEquip> field = MakeAttribute.createBaseBieEq(sinBieEq.getBaseAttr(), sinBieEq.getMaxEndure(), sinBieEq.getSerial(), bn, 0, 0.0D);
    newEq = clearHistory(newEq);
    newEq.setField(field);
    newEq.setType(sinBieEq.getSerial());
    newEq.setName(sinBieEq.getName());
    newEq.setField(field);
    Integer score = MakeAttribute.calEquipScore(newEq);
    field.add(new FiedEquip(6679, score + sinBieEq.getBaseScore()));
    newEq.setField(field);
    refresh(role, newEq, equip.getPos(), equip.getId(), true);
    MessagePusher.pushMessage(role, (Message)new RespHint(2, (short)1));
    MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级成功，你得到了一件{0}级{1}", getPj(sinBieEq.getSerial()), sinBieEq.getName()), false));
    animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue());
    int pj = getPj(newEq.getType());
    if (pj > 2) {
      String msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", newEq.getType(), equip.getId(), role.getRoleId(), newEq.getType(), newEq.getType(), pj + "阶仙器");
      MessagePusher.notify2Lb(role, 800, role.getName(), "升阶", msg);
    } 
    RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)1, "升阶仙器获得装备 - " + JsonUtils.object2String(newEq), role.getMarking());
  }
  
  public boolean deduct(Role role, int grade, long aux_id, long aux_id_two) {
      int silver = SILVER[grade - 1];
      if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver))
          return false;
      if (grade == 4) {
          Integer goods = this.bagService.consumeBagGoods(role, 75031, 30, true, true, "4阶升5阶仙器");
          if (goods != null) {
              MessagePusher.pushMessage(role, new RespNotifyMiscEx("升级材料数量不足"));
              return false;
          }
          return true;
      } else if (grade == 5) {
          Integer goods = this.bagService.consumeBagGoods(role, 76001, 30, true, true, "5阶升6阶仙器");
          if (goods != null) {
              MessagePusher.pushMessage(role, new RespNotifyMiscEx("升级材料数量不足"));
              return false;
          }
          return true;
      }
      BagEquip auxEquip = getEquip(role, 1, aux_id).getEquip();
      if (auxEquip != null) {
          if (aux_id_two > 0L) {
              BagEquip auxEquipTwo = getEquip(role, 1, aux_id_two).getEquip();
              if (auxEquip == null || auxEquipTwo == null) {
                  MessagePusher.pushMessage(role, (Message) new RespNotifyMiscEx("请添加足够的仙器"));
                  return false;
              }
              if (!check(grade, getPj(auxEquip.getType()), getPj(auxEquipTwo.getType()))) {
                  MessagePusher.pushMessage(role, (Message) new RespNotifyMiscEx("请放入同等品阶的仙器"));
                  return false;
              }
              boolean bool1 = this.bagService.deleteRoleEquip(role, auxEquipTwo, "升阶扣除材料装备");
              if (!bool1) {
                  this.logger.error("删除材料二时，玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", role.getRoleId(), role.getName());
                  return false;
              }
          } else if (aux_id > 0L &&
                  !check(grade, getPj(auxEquip.getType()), 0)) {
              MessagePusher.pushMessage(role, (Message) new RespNotifyMiscEx("请放入同等品阶的仙器"));
              return false;
          }
          boolean bool = this.bagService.deleteRoleEquip(role, auxEquip, "升阶扣除材料装备-");
          if (!bool) {
              this.logger.error("删除材料一， 时玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", role.getRoleId(), role.getName());
              return false;
          }
      } else {
          return false;
      }
      boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipSinUpGrade");
      if (!b) {
          this.logger.error("扣除银币时，玩家编号 = {}，玩家名称：{}，升阶扣除道具失败，可能使用非法操作", role.getRoleId(), role.getName());
          return false;
      }
      return true;
  }
  
  public boolean check(int grade, int auxPj, int auxPjTwo) {
    if (auxPjTwo > 0) {
      if (auxPj != grade || auxPjTwo != grade)
        return false; 
    } else if (auxPj != grade) {
      return false;
    } 
    return true;
  }
}
