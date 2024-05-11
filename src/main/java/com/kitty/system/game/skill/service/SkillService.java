
package com.kitty.system.game.skill.service;

import com.kitty.system.game.skill.message.ReqSuitSkillEffects;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.message.ReqActivateSkillEffects;
import java.util.Collection;
import com.kitty.system.game.vo.FieldByte;
import com.kitty.system.game.skill.message.RespSkillEffects;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.game.skill.message.ReqLearnSkill;
import java.util.Iterator;
import java.util.List;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.message.vo.Skill;
import java.util.ArrayList;
import com.kitty.system.game.skill.message.RespUpdateSkill;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LoginEvent;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.bag.service.BagService;
import org.springframework.stereotype.Service;

@Service
public class SkillService
{
    @Autowired
    BagService bagService;
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        this.loadRoleSkill(role);
    }
    
    public void loadRoleSkill(final Role role) {
        final RespUpdateSkill respUpdateSkill = new RespUpdateSkill();
        respUpdateSkill.setId(role.getRoleId());
        final List<Skill> list = new ArrayList<Skill>();
        for (final Integer key : role.getSkills().keySet()) {
            list.add(new Skill(role.getSkills().get(key)));
        }
        respUpdateSkill.setList(list);
        MessagePusher.pushMessage(role, respUpdateSkill);
    }
    
    public void learnSkill(final ReqLearnSkill reqLearnSkill, final Role role) {
        final SysRbAttr sysRbattr = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        if (sysRbattr != null) {
            final RespMsg respMsg = new RespMsg();
            final RoleSkill skill = role.getSkills().get(reqLearnSkill.getSerial());
            if (skill != null) {
                Integer hasExp = skill.getProficient() + Const.skillExp;
                if (hasExp > Const.maxSkillExp) {
                    respMsg.setMsg("技能熟练度到达当前等级限值");
                }
                else {
                    final Integer costMoney = FormulaHero.formulaSkillUpMoney(skill.getProficient(), skill.getSkillId());
                    if (costMoney > 0) {
                        if (role.getCoin() >= costMoney) {
                            final int currentMax = DataGlobalFunc.proficiency(role.getLevel());
                            if (skill.getProficient() >= currentMax || hasExp > sysRbattr.getMaxSkillExp()) {
                                respMsg.setMsg("技能熟练度到达当前等级限值，请先提升等级");
                            }
                            else {
                                role.setCoin(role.getCoin() - costMoney);
                                SpringUtils.getRoleService().refreshRoleCurrency(role);
                                if (skill.getProficient() < Const.skillExp) {
                                    hasExp = Const.skillExp;
                                }
                                skill.setProficient(hasExp);
                                role.getSkills().put(reqLearnSkill.getSerial(), skill);
                                final RespUpdateSkill respUpdateSkill = new RespUpdateSkill();
                                respUpdateSkill.setId(role.getRoleId());
                                final List<Skill> list = new ArrayList<Skill>();
                                list.add(new Skill(skill));
                                respUpdateSkill.setList(list);
                                MessagePusher.pushMessage(role, respUpdateSkill);
                                role.save();
                            }
                        }
                        else {
                            respMsg.setMsg("贡币不足#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours((long)costMoney));
                        }
                    }
                    else {
                        respMsg.setMsg("未找到该技能");
                    }
                }
            }
            else {
                respMsg.setMsg("未找到该技能");
            }
            MessagePusher.pushMessage(role, respMsg);
        }
    }
    
    public void skillEffects(final Role role, final int serial) {
        final RoleSkill skill = role.getSkills().get(serial);
        if (skill == null) {
            return;
        }
        final RespSkillEffects skillEffects = new RespSkillEffects();
        if (skill.getEffects().size() > 0) {
            final List<FieldByte> list = new ArrayList<FieldByte>();
            list.addAll(skill.getEffects());
            skillEffects.setList(list);
        }
        MessagePusher.pushMessage(role, skillEffects);
    }
    
    public void activateSkillEffects(final Role role, final ReqActivateSkillEffects reqActivateSkillEffects) {
        final RoleSkill skill = role.getSkills().get(reqActivateSkillEffects.getSerial());
        if (skill == null) {
            return;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(skill.getSkillId());
        final int serial = sysSkill.getIcon() * 10 + reqActivateSkillEffects.getType();
        final SysOtherItem item = ItemDataPool.data_other_item.values().stream().filter(e -> e.getValue().equals(serial)).findFirst().orElse(null);
        if (item == null) {
            MessagePusher.pushMessage(role, new RespMsg("未找该技能法术幻肤"));
            return;
        }
        final FieldByte fieldByte = skill.getEffects().stream().filter(e -> e.getK().equals(serial)).findFirst().orElse(null);
        if (fieldByte != null) {
            MessagePusher.pushMessage(role, new RespMsg("你已经激活该法术幻肤"));
            return;
        }
        final Integer goods = this.bagService.consumeBagGoods(role, item.getSerial(), 1, true, true, "激活法术幻肤：" + serial);
        if (goods != null) {
            MessagePusher.pushMessage(role, new RespMsg("缺少激活道具，请前往#<R>长安•杂货店老板#处进行购买"));
            return;
        }
        for (final FieldByte effect : skill.getEffects()) {
            effect.setV((byte)1);
        }
        skill.getEffects().add(new FieldByte(serial, (byte)2));
        this.skillEffects(role, reqActivateSkillEffects.getSerial());
        MessagePusher.pushMessage(role, new RespMsg("激活法术幻肤成功"));
        role.save();
    }
    
    public void suitSkillEffects(final Role role, final ReqSuitSkillEffects reqSuitSkillEffects) {
        final RoleSkill skill = role.getSkills().get(reqSuitSkillEffects.getSerial());
        if (skill == null) {
            return;
        }
        if (skill.getEffects().size() <= 0) {
            return;
        }
        if (reqSuitSkillEffects.getType() <= 0) {
            for (final FieldByte effect : skill.getEffects()) {
                effect.setV((byte)1);
            }
            MessagePusher.pushMessage(role, new RespMsg("切换法术幻肤成功"));
            this.skillEffects(role, reqSuitSkillEffects.getSerial());
            role.save();
            return;
        }
        final SysSkill sysSkill = SkillDataPool.skillMap.get(skill.getSkillId());
        final int serial = sysSkill.getIcon() * 10 + reqSuitSkillEffects.getType();
        final FieldByte fieldByte = skill.getEffects().stream().filter(e -> e.getK().equals(serial)).findFirst().orElse(null);
        if (fieldByte == null) {
            return;
        }
        for (final FieldByte effect2 : skill.getEffects()) {
            if (effect2.getK().equals(serial)) {
                effect2.setV((byte)2);
            }
            else {
                effect2.setV((byte)1);
            }
        }
        MessagePusher.pushMessage(role, new RespMsg("切换法术幻肤成功"));
        this.skillEffects(role, reqSuitSkillEffects.getSerial());
        role.save();
    }
}
