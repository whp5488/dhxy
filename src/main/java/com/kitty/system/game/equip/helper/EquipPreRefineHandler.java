package com.kitty.system.game.equip.helper;

import java.util.Iterator;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipPreRefine")
public class EquipPreRefineHandler extends UpgradeHandler
{
    public EquipPreRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        List<EquipNeo> list = new ArrayList<>();
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("装备炼化预览：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        if (type == 1) {
            List<FiedEquip> equips = equip.getArtificeGeneralHistory();
            list.add(new EquipNeo(equips));
        }
        else if (type == 2) {
            List<List<FiedEquip>> history = equip.getArtificeHighHistory();
            for (List<FiedEquip> fieldEquips : history) {
                list.add(new EquipNeo(fieldEquips));
            }
        }
        else if (type == 3) {
            List<List<FiedEquip>> superHistory = equip.getArtificeSuperHistory();
            for (List<FiedEquip> fieldEquips : superHistory) {
                list.add(new EquipNeo(fieldEquips));
            }
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)1, param, list, (byte)(int)type));
        return null;
    }
}
