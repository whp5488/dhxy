package com.kitty.system.game.equip.helper;

import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.init.domain.SysSinBieEq;
import java.util.stream.Collectors;
import com.kitty.system.game.equip.pool.EquipDataPool;
import java.util.List;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.vo.utils.Const;
import java.util.Random;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipCompose")
public class EquipComposeHandler extends UpgradeHandler
{
    public static final int SERIAL_M = 75042;
    public static final int SERIAL_M_NUM = 50;
    public static final int SERIAL_S = 75031;
    public static final int SERIAL_S_NUM = 40;
    public static final long RESTYPE_SILVER = 10000000L;
    public static final String SUCCESS = "你获得了{0}";
    public static final String NOT_STUFF = "缺少神兵碎片×{0}";
    
    public EquipComposeHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer position, Integer type, String param, String cause) {
        int pos = this.getVacancy(role);
        if (pos <= 0) {
            return "很抱歉，你的包裹空位不足，请整理后再来吧。";
        }
        Random random = new Random();
        if (type.equals(1)) {
            Integer count = Integer.valueOf(param);
            int i = 0;
            while (i < (int)count) {
                pos = this.getVacancy(role);
                if (pos <= 0) {
                    return "很抱歉，你的包裹空位不足，请整理后再来吧。";
                }
                Integer num2 = this.bagService.consumeBagGoods(role, 75042, 50, true, true, "装备合成-神兵合成");
                if (num2 != null) {
                    return MessageFormat.format("缺少神兵碎片×{0}", num2);
                }
                List<SysMagicalBieEq> lowMagicalBieEqList = EquipDataPool.lowMagicalBieEq;
                int nextInt = random.nextInt(lowMagicalBieEqList.size());
                SysMagicalBieEq magicalBieEq = lowMagicalBieEqList.get(nextInt);
                BagEquip equip = EquipManage.createEquip(magicalBieEq.getSerial());
                if (equip != null) {
                    BagEquip eq = this.equipService.addEquip(role, equip, pos, 0, "合成神兵");
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(MessageFormat.format("你获得了{0}", String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", Integer.valueOf(equip.getType()), eq.getId(), role.getId(), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName()))));
                    role.save();
                    ++i;
                }
                else {
                    return "操作的类型不正确";
                }
            }
        }
        else if (type.equals(2)) {
            boolean b = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 10000000L);
            if (!b) {
                return null;
            }
            Integer num = this.bagService.consumeBagGoods(role, 75031, 40, true, true, "装备合成-仙器合成");
            if (num != null) {
                return "八荒遗风不足，不能合成仙器";
            }
            boolean flag = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 10000000L, "装备合成");
            if (!flag) {
                this.logger.error("仙器合成可能违规操作扣除银两失败，角色 = {}", role.getRoleId());
                return null;
            }
            if (position < ADORN.WEAPON.getValue() || position > ADORN.SHOES.getValue()) {
                return "请选择部位再合成仙器";
            }
            List<SysSinBieEq> list = EquipDataPool.lowSinBieEq.stream().filter(bieEq -> position == bieEq.getPos() && (bieEq.getSex() == 0 || bieEq.getSex() == role.getSex()) && (bieEq.getOwnerRole().contains(0) || bieEq.getOwnerRole().contains(role.getSerial()) || bieEq.getOwnerRole().contains(role.getRace())) && (bieEq.getRace() == 0 || (int)role.getRace() == bieEq.getRace())).collect(Collectors.toList());
            int nextInt = random.nextInt(list.size());
            SysSinBieEq bieEq = list.get(nextInt);
            BagEquip equip = EquipManage.createEquip(bieEq.getSerial());
            if (equip != null) {
                BagEquip eq = this.equipService.addEquip(role, equip, pos, 0, "合成仙器");
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(MessageFormat.format("你获得了{0}", String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", equip.getType(), eq.getId(), role.getId(), equip.getType(), equip.getType(), equip.getName()))));
            }
            else {
                return "操作的类型不正确";
            }
        }
        return null;
    }
}
