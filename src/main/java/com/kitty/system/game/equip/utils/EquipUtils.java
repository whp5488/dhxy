
package com.kitty.system.game.equip.utils;

import com.kitty.system.enums.common.ADORN;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.vo.FiedEquip;
import java.util.List;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;

public class EquipUtils
{
    public static BagEquip gemChange(final EquipAlike equipData, BagEquip equip) {
        final Integer score = MakeAttribute.calEquipScore(equip);
        final List<FiedEquip> field = equip.getField();
        field.forEach(e -> {
            if (e.getType().equals(6679)) {
                e.setValue(score + equipData.getBaseScore());
                return;
            }
            else {
                return;
            }
        });
        equip.setField(field);
        equip = scoreUpdate(equip, 0, 0, equipData.getBaseScore());
        return equip;
    }
    
    public static BagEquip scoreUpdate(final BagEquip equip, final int type, final int operateType, final int score) {
        final BagEquip calculate = new BagEquip();
        BeanUtils.copyBeanProp(calculate, equip);
        final BagEquip redo = redo(calculate, type, operateType, score);
        equip.setRedoGeneralHistory(redo.getRedoGeneralHistory());
        equip.setRedoHighHistory(redo.getRedoHighHistory());
        equip.setRedoSuperHistory(redo.getRedoSuperHistory());
        final BagEquip artifice = artifice(calculate, type, operateType, score);
        equip.setArtificeGeneralHistory(artifice.getArtificeGeneralHistory());
        equip.setArtificeHighHistory(artifice.getArtificeHighHistory());
        equip.setArtificeSuperHistory(artifice.getArtificeSuperHistory());
        return equip;
    }
    
    public static BagEquip redo(final BagEquip calculate, final int type, final int operateType, final int score) {
        if ((type == 2 || type == 3) && calculate.getRedoGeneralHistory().size() > 0) {
            if (operateType != 1) {
                calculate.setRedoGeneralHistory(countCommonScore(calculate.getRedoGeneralHistory(), calculate, score, 0));
            }
        }
        else {
            calculate.setRedoGeneralHistory(countCommonScore(calculate.getRedoGeneralHistory(), calculate, score, 0));
        }
        if ((type == 2 || type == 3) && calculate.getRedoHighHistory().size() > 0) {
            if (operateType != 2) {
                calculate.setRedoHighHistory(countOtherScore(calculate.getRedoHighHistory(), calculate, score, 0));
            }
        }
        else {
            calculate.setRedoHighHistory(countOtherScore(calculate.getRedoHighHistory(), calculate, score, 0));
        }
        if ((type == 2 || type == 3) && calculate.getRedoSuperHistory().size() > 0) {
            if (operateType != 3) {
                calculate.setRedoSuperHistory(countOtherScore(calculate.getRedoSuperHistory(), calculate, score, 0));
            }
        }
        else {
            calculate.setRedoSuperHistory(countOtherScore(calculate.getRedoSuperHistory(), calculate, score, 0));
        }
        return calculate;
    }
    
    public static BagEquip artifice(final BagEquip calculate, final int type, final int operateType, final int score) {
        if (type == 4 && calculate.getArtificeGeneralHistory().size() > 0) {
            if (operateType != 1) {
                calculate.setArtificeGeneralHistory(countCommonScore(calculate.getArtificeGeneralHistory(), calculate, score, 1));
            }
        }
        else {
            calculate.setArtificeGeneralHistory(countCommonScore(calculate.getArtificeGeneralHistory(), calculate, score, 1));
        }
        if (type == 4 && calculate.getArtificeHighHistory().size() > 0) {
            if (operateType != 2) {
                calculate.setArtificeHighHistory(countOtherScore(calculate.getArtificeHighHistory(), calculate, score, 1));
            }
        }
        else {
            calculate.setArtificeHighHistory(countOtherScore(calculate.getArtificeHighHistory(), calculate, score, 1));
        }
        if (type == 4 && calculate.getArtificeSuperHistory().size() > 0) {
            if (operateType != 3) {
                calculate.setArtificeSuperHistory(countOtherScore(calculate.getArtificeSuperHistory(), calculate, score, 1));
            }
        }
        else {
            calculate.setArtificeSuperHistory(countOtherScore(calculate.getArtificeSuperHistory(), calculate, score, 1));
        }
        return calculate;
    }
    
    public static List<List<FiedEquip>> countOtherScore(final List<List<FiedEquip>> list, final BagEquip calculate, final int score, final int type) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, countCommonScore(list.get(i), calculate, score, type));
        }
        return list;
    }
    
    public static List<FiedEquip> countCommonScore(final List<FiedEquip> list, final BagEquip calculate, final int score, final int type) {
        if (calculate.getPosition() <= ADORN.SHOES.getValue()) {
            if (type == 0) {
                calculate.setField(list);
            }
            else {
                calculate.setArtifice(list);
            }
            final Integer scoreNew = MakeAttribute.calEquipScore(calculate) + score;
            list.forEach(e -> {
                if (e.getType().equals(6679)) {
                    e.setValue(scoreNew);
                    return;
                }
                else {
                    return;
                }
            });
        }
        return list;
    }
}
