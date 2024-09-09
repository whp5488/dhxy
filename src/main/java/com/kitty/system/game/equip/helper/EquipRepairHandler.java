package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.FiedEquip;
import java.util.Iterator;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import java.text.MessageFormat;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRepair")
public class EquipRepairHandler extends UpgradeHandler
{
    public static final String SUCCESS = "{0}，修复了#<R>{1}#";
    
    public EquipRepairHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        List<BagEquip> list = new ArrayList<>();
        Long id = Long.valueOf(param);
        if (pos <= 0 && id == 0L) {
            List<WearEquip> equips = this.equipService.selectWearEquip(role);
            for (WearEquip equip2 : equips) {
                EquipInfo equipInfo2 = this.getEquip(role, equip2.getPos(), equip2.getId());
                if (equipInfo2.getTips() != null) {
                    return equipInfo2.getTips();
                }
                list.add(equipInfo2.getEquip());
            }
        }
        else {
            EquipInfo equipInfo = this.getEquip(role, pos, id);
            if (equipInfo.getTips() != null) {
                return equipInfo.getTips();
            }
            BagEquip equip = equipInfo.getEquip();
            list.add(equip);
        }
        Iterator<BagEquip> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            BagEquip equip = iterator2.next();
            String msg = "";
            long consume = 50000L;
            if (role.getCoin() < consume) {
                if (role.getSilver() >= consume - role.getCoin()) {
                    long silver = consume - role.getCoin();
                    this.roleService.deductCurrency(role, Const.RESTYPE_COIN, role.getCoin(), "EquipRepair");
                    this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipRepair");
                    msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(consume - silver) + "，#<IR" + Const.RESTYPE_SILVER + ">#" + MoneyUtil.colours(silver);
                }
                else {
                    return "货币不足";
                }
            }
            else {
                this.roleService.deductCurrency(role, Const.RESTYPE_COIN, consume, "EquipRepair");
                msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(consume);
            }
            equip.setField(this.field(equip.getField(), DataGlobalFunc.getEquipData(equip.getType()).getMaxEndure()));
            MessagePusher.pushMessage(role, new RespMsg(MessageFormat.format("{0}，修复了#<R>{1}#", msg, equip.getName())));
            this.refresh(role, equip, pos, id, true);
            this.roleService.calculateBase(role);
            role.save();
            this.roleService.refreshRoleInfo(role, false);
        }
        return null;
    }
    
    public List<FiedEquip> field(List<FiedEquip> field, int dur) {
        field.forEach(e -> {
            if (e.getType().equals(6672)) {
                e.setValue(dur);
            }
        });
        return field;
    }
}
