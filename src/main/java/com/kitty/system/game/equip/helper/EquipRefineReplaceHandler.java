
package com.kitty.system.game.equip.helper;

import java.util.ConcurrentModificationException;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.List;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRefineReplace")
public class EquipRefineReplaceHandler extends UpgradeHandler
{
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final Long id = Long.valueOf(param);
        final EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("炼化属性替换：获取物品所在位置为 = {}，获取物品ID = {}", (Object)role.getPos(), (Object)id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        final int index = type / 1000;
        final int genre = type % 1000;
        if (genre == 1) {
            if (equip.getArtificeGeneralHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeGeneralHistory()));
            equip.setArtificeGeneralHistory(new ArrayList<FiedEquip>());
        }
        else if (genre == 2) {
            if (equip.getArtificeHighHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeHighHistory().get(index)));
            equip.setArtificeHighHistory(new ArrayList<List<FiedEquip>>());
        }
        else if (genre == 3) {
            if (equip.getArtificeSuperHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeSuperHistory().get(index)));
            equip.setArtificeSuperHistory(new ArrayList<List<FiedEquip>>());
        }
        final EquipAlike alike = DataGlobalFunc.getEquipData(equip.getType());
        final Integer score = MakeAttribute.calEquipScore(equip);
        equip = this.updateSore(equip, score + alike.getBaseScore());
        equip = EquipUtils.scoreUpdate(equip, 4, genre, alike.getBaseScore());
        this.refresh(role, equip, pos, id, true);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)3, "装备炼化替换" + JsonUtils.object2String(equip), role.getMarking());
        return null;
    }

    public BagEquip updateSore(final BagEquip equip, final int score) {
        try {
            equip.getField().forEach(e -> {
                if (e.getType().equals(6679)) {
                    e.setValue(score);
                    return;
                }
                else {
                    return;
                }
            });
        }
        catch (final ConcurrentModificationException e) {
            e.getMessage();
        }
        return equip;
    }

    public List<FiedEquip> field(final List<FiedEquip> field) {
        try {
            field.forEach(e -> {
                if (e.getType().equals(6679)) {
                    field.remove(e);
                    return;
                }
                else {
                    return;
                }
            });
        }
        catch (final ConcurrentModificationException e) {
            e.getMessage();
        }
        return field;
    }
}
