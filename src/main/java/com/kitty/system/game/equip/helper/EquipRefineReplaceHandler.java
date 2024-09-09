package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.FiedEquip;
import java.util.ConcurrentModificationException;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRefineReplace")
public class EquipRefineReplaceHandler extends UpgradeHandler
{
    public EquipRefineReplaceHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("炼化属性替换：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        int index = type / 1000;
        int genre = type % 1000;
        if (genre == 1) {
            if (equip.getArtificeGeneralHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeGeneralHistory()));
            equip.setArtificeGeneralHistory(new ArrayList<>());
        }
        else if (genre == 2) {
            if (equip.getArtificeHighHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeHighHistory().get(index)));
            equip.setArtificeHighHistory(new ArrayList<>());
        }
        else if (genre == 3) {
            if (equip.getArtificeSuperHistory().size() <= 0) {
                return null;
            }
            equip.setArtifice(this.field(equip.getArtificeSuperHistory().get(index)));
            equip.setArtificeSuperHistory(new ArrayList<>());
        }
        EquipAlike alike = DataGlobalFunc.getEquipData(equip.getType());
        Integer score = MakeAttribute.calEquipScore(equip);
        equip = this.updateSore(equip, score + alike.getBaseScore());
        equip = EquipUtils.scoreUpdate(equip, 4, genre, alike.getBaseScore());
        this.refresh(role, equip, pos, id, true);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)3, "装备炼化替换" + JsonUtils.object2String(equip), role.getMarking());
        return null;
    }
    
    public BagEquip updateSore(BagEquip equip, int score) {
        try {
            equip.getField().forEach(e -> {
                if (e.getType().equals(6679)) {
                    e.setValue(score);
                }
            });
        }
        catch (ConcurrentModificationException e) {
            e.getMessage();
        }
        return equip;
    }
    
    public List<FiedEquip> field(List<FiedEquip> field) {
        try {
            field.forEach(e -> {
                if (e.getType().equals(6679)) {
                    field.remove(e);
                }
            });
        }
        catch (ConcurrentModificationException e) {
            e.getMessage();
        }
        return field;
    }
}
