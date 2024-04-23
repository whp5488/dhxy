package com.kitty.system.game.skill.service;

import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.message.ReqActivateSkillEffects;
import com.kitty.system.game.skill.message.ReqLearnSkill;
import com.kitty.system.game.skill.message.ReqSuitSkillEffects;
import com.kitty.system.game.skill.message.RespSkillEffects;
import com.kitty.system.game.skill.message.RespUpdateSkill;
import com.kitty.system.game.skill.message.vo.Skill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.vo.FieldByte;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService {
  @Autowired
  BagService bagService;
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    loadRoleSkill(role);
  }
  
  public void loadRoleSkill(Role role) {
    RespUpdateSkill respUpdateSkill = new RespUpdateSkill();
    respUpdateSkill.setId(role.getRoleId().intValue());
    List<Skill> list = new ArrayList<>();
    for (Integer key : role.getSkills().keySet())
      list.add(new Skill((RoleSkill)role.getSkills().get(key))); 
    respUpdateSkill.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdateSkill);
  }
  
  public void learnSkill(ReqLearnSkill reqLearnSkill, Role role) {
    SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(role.getRelive().intValue() + 1));
    if (sysRbattr != null) {
      RespMsg respMsg = new RespMsg();
      RoleSkill skill = (RoleSkill)role.getSkills().get(Integer.valueOf(reqLearnSkill.getSerial()));
      if (skill != null) {
        Integer hasExp = Integer.valueOf(skill.getProficient().intValue() + Const.skillExp.intValue());
        if (hasExp.intValue() > Const.maxSkillExp.intValue()) {
          respMsg.setMsg("技能熟练度到达当前等级限值");
        } else {
          Integer costMoney = FormulaHero.formulaSkillUpMoney(skill.getProficient(), skill.getSkillId());
          if (costMoney.intValue() > 0) {
            if (role.getCoin().longValue() >= costMoney.intValue()) {
              int currentMax = DataGlobalFunc.proficiency(role.getLevel().intValue());
              if (skill.getProficient().intValue() >= currentMax || hasExp.intValue() > sysRbattr.getMaxSkillExp().intValue()) {
                respMsg.setMsg("技能熟练度到达当前等级限值，请先提升等级");
              } else {
                role.setCoin(Long.valueOf(role.getCoin().longValue() - costMoney.intValue()));
                SpringUtils.getRoleService().refreshRoleCurrency(role);
                if (skill.getProficient().intValue() < Const.skillExp.intValue())
                  hasExp = Const.skillExp; 
                skill.setProficient(hasExp);
                role.getSkills().put(Integer.valueOf(reqLearnSkill.getSerial()), skill);
                RespUpdateSkill respUpdateSkill = new RespUpdateSkill();
                respUpdateSkill.setId(role.getRoleId().intValue());
                List<Skill> list = new ArrayList<>();
                list.add(new Skill(skill));
                respUpdateSkill.setList(list);
                MessagePusher.pushMessage(role, (Message)respUpdateSkill);
                role.save();
              } 
            } else {
              respMsg.setMsg("贡币不足#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(costMoney.longValue())));
            } 
          } else {
            respMsg.setMsg("未找到该技能");
          } 
        } 
      } else {
        respMsg.setMsg("未找到该技能");
      } 
      MessagePusher.pushMessage(role, (Message)respMsg);
    } 
  }
  
  public void skillEffects(Role role, int serial) {
    RoleSkill skill = (RoleSkill)role.getSkills().get(Integer.valueOf(serial));
    if (skill == null)
      return; 
    RespSkillEffects skillEffects = new RespSkillEffects();
    if (skill.getEffects().size() > 0) {
      List<FieldByte> list = new ArrayList<>();
      list.addAll(skill.getEffects());
      skillEffects.setList(list);
    } 
    MessagePusher.pushMessage(role, (Message)skillEffects);
  }
  
  public void activateSkillEffects(Role role, ReqActivateSkillEffects reqActivateSkillEffects) {
    RoleSkill skill = (RoleSkill)role.getSkills().get(Integer.valueOf(reqActivateSkillEffects.getSerial()));
    if (skill == null)
      return; 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
    int serial = sysSkill.getIcon() * 10 + reqActivateSkillEffects.getType();
    SysOtherItem item = ItemDataPool.data_other_item.values().stream().filter(e -> e.getValue().equals(Integer.valueOf(serial))).findFirst().orElse(null);
    if (item == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("未找该技能法术幻肤"));
      return;
    } 
    FieldByte fieldByte = skill.getEffects().stream().filter(e -> e.getK().equals(Integer.valueOf(serial))).findFirst().orElse(null);
    if (fieldByte != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("你已经激活该法术幻肤"));
      return;
    } 
    Integer goods = this.bagService.consumeBagGoods(role, item.getSerial().intValue(), 1, true, true, "激活法术幻肤：" + serial);
    if (goods != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("缺少激活道具，请前往#<R>长安•杂货店老板#处进行购买"));
      return;
    } 
    for (FieldByte effect : skill.getEffects())
      effect.setV(Byte.valueOf((byte)1)); 
    skill.getEffects().add(new FieldByte(Integer.valueOf(serial), Byte.valueOf((byte)2)));
    skillEffects(role, reqActivateSkillEffects.getSerial());
    MessagePusher.pushMessage(role, (Message)new RespMsg("激活法术幻肤成功"));
    role.save();
  }
  
  public void suitSkillEffects(Role role, ReqSuitSkillEffects reqSuitSkillEffects) {
    RoleSkill skill = (RoleSkill)role.getSkills().get(Integer.valueOf(reqSuitSkillEffects.getSerial()));
    if (skill == null)
      return; 
    if (skill.getEffects().size() <= 0)
      return; 
    if (reqSuitSkillEffects.getType() <= 0) {
      for (FieldByte effect : skill.getEffects())
        effect.setV(Byte.valueOf((byte)1)); 
      MessagePusher.pushMessage(role, (Message)new RespMsg("切换法术幻肤成功"));
      skillEffects(role, reqSuitSkillEffects.getSerial());
      role.save();
      return;
    } 
    SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(skill.getSkillId());
    int serial = sysSkill.getIcon() * 10 + reqSuitSkillEffects.getType();
    FieldByte fieldByte = skill.getEffects().stream().filter(e -> e.getK().equals(Integer.valueOf(serial))).findFirst().orElse(null);
    if (fieldByte == null)
      return; 
    for (FieldByte effect : skill.getEffects()) {
      if (effect.getK().equals(Integer.valueOf(serial))) {
        effect.setV(Byte.valueOf((byte)2));
        continue;
      } 
      effect.setV(Byte.valueOf((byte)1));
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("切换法术幻肤成功"));
    skillEffects(role, reqSuitSkillEffects.getSerial());
    role.save();
  }
}
