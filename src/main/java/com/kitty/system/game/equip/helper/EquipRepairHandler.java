
package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import java.util.Iterator;
import java.util.List;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import java.text.MessageFormat;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRepair")
public class EquipRepairHandler extends UpgradeHandler
{
    public static final String SUCCESS = "{0}，修复了#<R>{1}#";
    
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final List<BagEquip> list = new ArrayList<BagEquip>();
        final Long id = Long.valueOf(param);
        if (pos <= 0 && id == 0L) {
            final List<WearEquip> equips = this.equipService.selectWearEquip(role);
            for (final WearEquip equip : equips) {
                final EquipInfo equipInfo = this.getEquip(role, equip.getPos(), equip.getId());
                if (equipInfo.getTips() != null) {
                    return equipInfo.getTips();
                }
                list.add(equipInfo.getEquip());
            }
        }
        else {
            final EquipInfo equipInfo2 = this.getEquip(role, pos, id);
            if (equipInfo2.getTips() != null) {
                return equipInfo2.getTips();
            }
            final BagEquip equip2 = equipInfo2.getEquip();
            list.add(equip2);
        }
        final Iterator<BagEquip> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            final BagEquip equip2 = iterator2.next();
            String msg = "";
            final long consume = 50000L;
            if (role.getCoin() < consume) {
                if (role.getSilver() < consume - role.getCoin()) {
                    return "货币不足";
                }
                final long silver = consume - role.getCoin();
                this.roleService.deductCurrency(role, Const.RESTYPE_COIN, role.getCoin(), "EquipRepair");
                this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, silver, "EquipRepair");
                msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(consume - silver) + "，#<IR" + Const.RESTYPE_SILVER + ">#" + MoneyUtil.colours(silver);
            }
            else {
                this.roleService.deductCurrency(role, Const.RESTYPE_COIN, consume, "EquipRepair");
                msg = msg + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(consume);
            }
            equip2.setField(this.field(equip2.getField(), DataGlobalFunc.getEquipData(equip2.getType()).getMaxEndure()));
            MessagePusher.pushMessage(role, new RespMsg(MessageFormat.format("{0}，修复了#<R>{1}#", msg, equip2.getName())));
            this.refresh(role, equip2, pos, id, true);
            this.roleService.calculateBase(role);
            role.save();
            this.roleService.refreshRoleInfo(role, false);
        }
        return null;
    }
    
    public List<FiedEquip> field(final List<FiedEquip> field, final int dur) {
        field.forEach(e -> {
            if (e.getType().equals(6672)) {
                e.setValue(dur);
                return;
            }
            else {
                return;
            }
        });
        return field;
    }
}
