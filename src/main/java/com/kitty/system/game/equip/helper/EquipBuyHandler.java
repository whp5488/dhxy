package com.kitty.system.game.equip.helper;

import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.make.EquipManage;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipBuy")
public class EquipBuyHandler extends UpgradeHandler
{
    public EquipBuyHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer upgradePos, Integer type, String param, String cause) {
        int pos = this.getVacancy(role);
        if (pos <= 0) {
            return "很抱歉，你的包裹空位不足，请整理后再来吧。";
        }
        BagEquip equip = EquipManage.createEquip(Integer.valueOf(param));
        if (equip != null) {
            this.equipService.addEquip(role, equip, pos, 0, cause);
            return null;
        }
        return "操作的类型不正确";
    }
}
