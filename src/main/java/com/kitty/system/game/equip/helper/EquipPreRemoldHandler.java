
package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.FieldProp;
import java.util.Iterator;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipPreRemold")
public class EquipPreRemoldHandler extends UpgradeHandler
{
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, Integer type, final String param, final String cause) {
        final List<EquipNeo> list = new ArrayList<EquipNeo>();
        final Long id = Long.valueOf(param);
        final EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("装备重铸预览：获取物品所在位置为 = {}，获取物品ID = {}", (Object)pos, (Object)id);
            return equipInfo.getTips();
        }
        final BagEquip equip = equipInfo.getEquip();
        final int classify = (type < 1000) ? 0 : (type / 1000);
        if (classify == 0) {
            if (type == 1) {
                final List<FiedEquip> equips = equip.getRedoGeneralHistory();
                list.add(this.field(equips));
            }
            else if (type == 2) {
                final List<List<FiedEquip>> history = equip.getRedoHighHistory();
                for (final List<FiedEquip> fieldEquips : history) {
                    list.add(this.field(fieldEquips));
                }
            }
            else if (type == 3) {
                final List<List<FiedEquip>> superHistory = equip.getRedoSuperHistory();
                for (final List<FiedEquip> fieldEquips : superHistory) {
                    list.add(this.field(fieldEquips));
                }
            }
        }
        else if (classify == 2) {
            type %= 1000;
            if (type == 1) {
                final List<FiedEquip> equips = equip.getPurifyGeneralHistory();
                list.add(this.field(equips));
            }
            else if (type == 2) {
                final List<List<FiedEquip>> history = equip.getPurifyHighHistory();
                for (final List<FiedEquip> fieldEquips : history) {
                    list.add(this.field(fieldEquips));
                }
            }
            else if (type == 3) {
                final List<List<FiedEquip>> superHistory = equip.getPurifySuperHistory();
                for (final List<FiedEquip> fieldEquips : superHistory) {
                    list.add(this.field(fieldEquips));
                }
            }
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)classify, param, list, type.byteValue()));
        return null;
    }
    
    public EquipNeo field(final List<FiedEquip> field) {
        final EquipNeo equipNeo = new EquipNeo();
        final List<FieldProp> list = new ArrayList<FieldProp>();
        for (int i = 0; i < field.size(); ++i) {
            final FiedEquip fiedEquip = field.get(i);
            if (fiedEquip.getType() != 6672 && fiedEquip.getType() != 6670 && fiedEquip.getType() != 6680) {
                list.add(new FieldProp(fiedEquip.getType(), fiedEquip.getValue()));
            }
        }
        equipNeo.setList(list);
        return equipNeo;
    }
}
