
package com.kitty.system.game.equip.helper;

import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.RandomUtil;
import java.util.List;
import java.util.Optional;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.vo.FiedEquip;
import java.util.Map;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipMagicalUpGrade")
public class EquipMagicalUpGradeHandler extends UpgradeHandler
{
    public static final String SUCCESS = "升级成功，你得到了一件{0}级{1}";
    public static final String FAIL = "升级失败，由于#<R>{0}#碎了，你获得了#<R>{1}快神兵碎片#";
    public static final String NOT_STUFF = "请先选择一种低级神兵作为材料";
    public static final int OFFSET = 75042;
    public static final int STUFF_ONE = 75047;
    public static final int STUFF_TWO = 75030;
    public static final int DEGREE_RAISING = 100000;
    public static final double[] ODDS;
    public static final int[] SILVER;
    public static final String BUGLE = "#<B>{0}#紧张得闭上了眼，只觉得风雷奔涌，霸气横流。睁开眼，何大雷已经送上#<G>{1}级神兵#{2}!";
    
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final String[] split = param.split("-");
        if (split.length < 2) {
            return "请先选择一种低级神兵作为材料";
        }
        final Long id = Long.valueOf(split[0]);
        final Long aux_id = Long.valueOf(split[1]);
        final EquipInfo equipInfo = this.getEquip(role, 1, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("神兵升阶：获取物品ID = {}", (Object)id);
            return equipInfo.getTips();
        }
        final BagEquip equip = equipInfo.getEquip();
        final Map<Integer, SysMagicalBieEq> magicalBieEq = EquipDataPool.magicalBieEq;
        final SysMagicalBieEq bieEq = magicalBieEq.get(equip.getType());
        if (this.getPj(bieEq.getSerial()) <= 1) {
            final boolean deduct = this.deduct(role, this.getPj(bieEq.getSerial()), this.getPj(bieEq.getSerial()), type);
            if (deduct) {
                final boolean flag = rate(this.getPj(bieEq.getSerial()), type, 0);
                if (flag) {
                    this.succeed(role, magicalBieEq, equip);
                }
                else {
                    MessagePusher.pushMessage(role, new RespHint(2, (short)0));
                }
            }
        }
        else {
            final BagEquip auxEquip = this.getEquip(role, 1, aux_id).getEquip();
            if (auxEquip == null) {
                return "请先选择一种低级神兵作为材料";
            }
            if (equip != null && auxEquip != null) {
                final SysMagicalBieEq auxBieEq = magicalBieEq.get(auxEquip.getType());
                final boolean deduct2 = this.deduct(role, this.getPj(bieEq.getSerial()), this.getPj(auxBieEq.getSerial()), type);
                if (deduct2) {
                    final boolean flag2 = rate(this.getPj(bieEq.getSerial()), type, this.getPj(auxBieEq.getSerial()));
                    if (flag2) {
                        this.bagService.deleteRoleEquip(role, auxEquip, "升阶成功删除材料装备");
                        this.succeed(role, magicalBieEq, equip);
                    }
                    else {
                        this.failure(role, auxEquip, this.getPj(auxBieEq.getSerial()));
                    }
                }
            }
        }
        return null;
    }
    
    public void succeed(final Role role, final Map<Integer, SysMagicalBieEq> magicalBieEq, final BagEquip equip) {
        int bn = 0;
        final Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            final FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        BagEquip newEq = new BagEquip();
        BeanUtils.copyBeanProp(newEq, equip);
        final SysMagicalBieEq sysMagicalBieEq = magicalBieEq.get(equip.getType() + 100000);
        final List<FiedEquip> field = MakeAttribute.createBaseBieEq(sysMagicalBieEq.getBaseAttr(), sysMagicalBieEq.getMaxEndure(), sysMagicalBieEq.getSerial(), sysMagicalBieEq.getRndRange(), bn);
        newEq = this.clearHistory(newEq);
        newEq.setField(field);
        newEq.setType(sysMagicalBieEq.getSerial());
        newEq.setName(sysMagicalBieEq.getName());
        newEq.setField(field);
        final Integer score = MakeAttribute.calEquipScore(newEq);
        field.add(new FiedEquip(6679, score + sysMagicalBieEq.getBaseScore()));
        newEq.setField(field);
        this.refresh(role, newEq, equip.getPos(), equip.getId(), true);
        MessagePusher.pushMessage(role, new RespHint(2, (short)1));
        this.animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue());
        final int pj = this.getPj(sysMagicalBieEq.getSerial());
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级成功，你得到了一件{0}级{1}", pj, sysMagicalBieEq.getName()), false));
        if (pj >= 3) {
            this.chatService.sendLaba(MessageFormat.format("#<B>{0}#紧张得闭上了眼，只觉得风雷奔涌，霸气横流。睁开眼，何大雷已经送上#<G>{1}级神兵#{2}!", role.getName(), pj, String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", newEq.getType(), newEq.getId(), role.getRoleId(), newEq.getType(), newEq.getType(), newEq.getName())));
        }
        role.save();
    }
    
    public void failure(final Role role, final BagEquip equip, final int grade) {
        MessagePusher.pushMessage(role, new RespHint(2, (short)0));
        this.bagService.deleteRoleEquip(role, equip, "升阶失败删除材料装备");
        final int num = RandomUtil.round(1, (grade > 3) ? 3 : grade);
        try {
            Thread.sleep(1500L);
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        this.bagService.addGoods(role, 75042, num, 0, true, true, "神兵升阶");
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级失败，由于#<R>{0}#碎了，你获得了#<R>{1}快神兵碎片#", equip.getName(), num), false));
        role.save();
    }
    
    public boolean deduct(final Role role, final int grade, final int pj, final int type) {
        final int silver = EquipMagicalUpGradeHandler.SILVER[grade - 1];
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver)) {
            return false;
        }
        if (type > 0) {
            final int num = grade * pj;
            final Integer numOne = this.bagService.getBagGoods(role, 75047, num);
            final Integer numTwo = this.bagService.getBagGoods(role, 75030, num);
            if (numOne != null || numTwo != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "请先选择一种低级神兵作为材料", false));
                return false;
            }
            final Integer integer = this.bagService.consumeBagGoods(role, 75047, num, true, true, "神兵升阶");
            if (integer != null) {
                return false;
            }
            final Integer integer2 = this.bagService.consumeBagGoods(role, 75030, num, true, true, "神兵升阶");
            if (integer2 != null) {
                return false;
            }
        }
        this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipMagicalUpGrade");
        return true;
    }
    
    public static boolean rate(final int grade, final int prop, final int helpLevel) {
        double rate = EquipMagicalUpGradeHandler.ODDS[grade - 1];
        if (prop > 0) {
            rate += 0.01;
        }
        rate += helpLevel * 0.01;
        return SkillAi.canAffectOnPro(rate);
    }
    
    static {
        ODDS = new double[] { 0.5, 0.35, 0.15, 0.05, 5.0E-4 };
        SILVER = new int[] { 2000000, 1500000, 1500000, 1000000, 1000000 };
    }
}
