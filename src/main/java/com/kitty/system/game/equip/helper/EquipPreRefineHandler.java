
package com.kitty.system.game.equip.helper;

import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.FiedEquip;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("EquipPreRefine")
public class EquipPreRefineHandler extends UpgradeHandler
{
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final List<EquipNeo> list = new ArrayList<EquipNeo>();
        final Long id = Long.valueOf(param);
        final EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("装备炼化预览：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        final BagEquip equip = equipInfo.getEquip();
        if (type == 1) {
            final List<FiedEquip> equips = equip.getArtificeGeneralHistory();
            list.add(new EquipNeo(equips));
        }
        else if (type == 2) {
            final List<List<FiedEquip>> history = equip.getArtificeHighHistory();
            for (final List<FiedEquip> fieldEquips : history) {
                list.add(new EquipNeo(fieldEquips));
            }
        }
        else if (type == 3) {
            final List<List<FiedEquip>> superHistory = equip.getArtificeSuperHistory();
            for (final List<FiedEquip> fieldEquips : superHistory) {
                list.add(new EquipNeo(fieldEquips));
            }
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)1, param, list, (byte)(int)type));
        return null;
    }
}
