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
    
    public EquipMagicalUpGradeHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        String[] split = param.split("-");
        if (split.length < 2) {
            return "请先选择一种低级神兵作为材料";
        }
        Long id = Long.valueOf(split[0]);
        Long aux_id = Long.valueOf(split[1]);
        EquipInfo equipInfo = this.getEquip(role, 1, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("神兵升阶：获取物品ID = {}", id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        Map<Integer, SysMagicalBieEq> magicalBieEq = EquipDataPool.magicalBieEq;
        SysMagicalBieEq bieEq = magicalBieEq.get(equip.getType());
        if (this.getPj(bieEq.getSerial()) <= 1) {
            boolean deduct2 = this.deduct(role, this.getPj(bieEq.getSerial()), this.getPj(bieEq.getSerial()), (int)type);
            if (deduct2) {
                boolean flag2 = rate(this.getPj(bieEq.getSerial()), (int)type, 0);
                if (flag2) {
                    this.succeed(role, magicalBieEq, equip);
                }
                else {
                    MessagePusher.pushMessage(role, new RespHint(2, (short)0));
                }
            }
        }
        else {
            BagEquip auxEquip = this.getEquip(role, 1, aux_id).getEquip();
            if (auxEquip == null) {
                return "请先选择一种低级神兵作为材料";
            }
            if (equip != null && auxEquip != null) {
                SysMagicalBieEq auxBieEq = magicalBieEq.get(auxEquip.getType());
                boolean deduct = this.deduct(role, this.getPj(bieEq.getSerial()), this.getPj(auxBieEq.getSerial()), type);
                if (deduct) {
                    boolean flag = rate(this.getPj(bieEq.getSerial()), type, this.getPj(auxBieEq.getSerial()));
                    if (flag) {
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
    
    public void succeed(Role role, Map<Integer, SysMagicalBieEq> magicalBieEq, BagEquip equip) {
        int bn = 0;
        Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        BagEquip newEq = new BagEquip();
        BeanUtils.copyBeanProp(newEq, equip);
        SysMagicalBieEq sysMagicalBieEq = magicalBieEq.get(equip.getType() + 100000);
        List<FiedEquip> field = MakeAttribute.createBaseBieEq(sysMagicalBieEq.getBaseAttr(), sysMagicalBieEq.getMaxEndure(), sysMagicalBieEq.getSerial(), sysMagicalBieEq.getRndRange(), bn);
        newEq = this.clearHistory(newEq);
        newEq.setField(field);
        newEq.setType(sysMagicalBieEq.getSerial());
        newEq.setName(sysMagicalBieEq.getName());
        newEq.setField(field);
        Integer score = MakeAttribute.calEquipScore(newEq);
        field.add(new FiedEquip(6679, score + sysMagicalBieEq.getBaseScore()));
        newEq.setField(field);
        this.refresh(role, newEq, equip.getPos(), equip.getId(), true);
        MessagePusher.pushMessage(role, new RespHint(2, (short)1));
        this.animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue());
        int pj = this.getPj(sysMagicalBieEq.getSerial());
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级成功，你得到了一件{0}级{1}", Integer.valueOf(pj), sysMagicalBieEq.getName()), false));
        if (pj >= 3) {
            this.chatService.sendLaba(MessageFormat.format("#<B>{0}#紧张得闭上了眼，只觉得风雷奔涌，霸气横流。睁开眼，何大雷已经送上#<G>{1}级神兵#{2}!", role.getName(), Integer.valueOf(pj), String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", Integer.valueOf(newEq.getType()), newEq.getId(), role.getRoleId(), Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), newEq.getName())));
        }
        role.save();
    }
    
    public void failure(Role role, BagEquip equip, int grade) {
        MessagePusher.pushMessage(role, new RespHint(2, (short)0));
        this.bagService.deleteRoleEquip(role, equip, "升阶失败删除材料装备");
        int num = RandomUtil.round(1, (grade > 3) ? 3 : grade);
        try {
            Thread.sleep(1500L);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.bagService.addGoods(role, 75042, num, 0, true, true, "神兵升阶");
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("升级失败，由于#<R>{0}#碎了，你获得了#<R>{1}快神兵碎片#", equip.getName(), num), false));
        role.save();
    }
    
    public boolean deduct(Role role, int grade, int pj, int type) {
        int silver = EquipMagicalUpGradeHandler.SILVER[grade - 1];
        if (!this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, silver)) {
            return false;
        }
        if (type > 0) {
            int num = grade * pj;
            Integer numOne = this.bagService.getBagGoods(role, 75047, num);
            Integer numTwo = this.bagService.getBagGoods(role, 75030, num);
            if (numOne != null || numTwo != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "请先选择一种低级神兵作为材料", false));
                return false;
            }
            Integer integer = this.bagService.consumeBagGoods(role, 75047, num, true, true, "神兵升阶");
            if (integer != null) {
                return false;
            }
            Integer integer2 = this.bagService.consumeBagGoods(role, 75030, num, true, true, "神兵升阶");
            if (integer2 != null) {
                return false;
            }
        }
        this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipMagicalUpGrade");
        return true;
    }
    
    public static boolean rate(int grade, int prop, int helpLevel) {
        double rate = EquipMagicalUpGradeHandler.ODDS[grade - 1];
        if (prop > 0) {
            rate += 0.01;
        }
        rate += (double)helpLevel * 0.01;
        return SkillAi.canAffectOnPro(rate);
    }
    
    static {
        ODDS = new double[] { 0.5, 0.35, 0.15, 0.05, 5.0E-4 };
        SILVER = new int[] { 2000000, 1500000, 1500000, 1000000, 1000000 };
    }
}
