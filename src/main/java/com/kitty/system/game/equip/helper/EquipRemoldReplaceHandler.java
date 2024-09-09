package com.kitty.system.game.equip.helper;

import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.utils.EquipUtils;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipRemoldReplace")
public class EquipRemoldReplaceHandler extends UpgradeHandler
{
    public EquipRemoldReplaceHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("重铸属性替换：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        int dur = DataGlobalFunc.getDur(equip.getField());
        int bn = DataGlobalFunc.getGemNum(equip);
        int index = type / 1000;
        int genre = type % 1000;
        if (genre < 100) {
            if (genre == 1) {
                if (equip.getRedoGeneralHistory().size() <= 0) {
                    return null;
                }
                int serial = this.fieldSerial(equip.getRedoGeneralHistory());
                if (serial > 0) {
                    equip.setType(serial);
                }
                equip.setField(this.field(equip.getRedoGeneralHistory(), dur, bn));
                equip.setRedoGeneralHistory(new ArrayList<>());
                equip = this.recast(equip, bn);
            }
            else if (genre == 2) {
                if (equip.getRedoHighHistory().size() <= 0) {
                    return null;
                }
                int serial = this.fieldSerial(equip.getRedoHighHistory().get(index));
                if (serial > 0) {
                    equip.setType(serial);
                }
                equip.setField(this.field(equip.getRedoHighHistory().get(index), dur, bn));
                equip.setRedoHighHistory(new ArrayList<>());
                equip = this.recast(equip, bn);
            }
            else if (genre == 3) {
                if (equip.getRedoSuperHistory().size() <= 0) {
                    return null;
                }
                int serial = this.fieldSerial(equip.getRedoSuperHistory().get(index));
                if (serial > 0) {
                    equip.setType(serial);
                }
                equip.setField(this.field(equip.getRedoSuperHistory().get(index), dur, bn));
                equip.setRedoSuperHistory(new ArrayList<>());
                equip = this.recast(equip, bn);
            }
        }
        else if (genre == 101) {
            if (equip.getPurifyGeneralHistory().size() <= 0) {
                return null;
            }
            equip.setField(this.field(equip.getPurifyGeneralHistory(), dur, bn));
            equip.setPurifyGeneralHistory(new ArrayList<>());
        }
        else if (genre == 102) {
            if (equip.getPurifyHighHistory().size() <= 0) {
                return null;
            }
            equip.setField(this.field(equip.getPurifyHighHistory().get(index), dur, bn));
            equip.setPurifyHighHistory(new ArrayList<>());
        }
        else if (genre == 103) {
            if (equip.getPurifySuperHistory().size() <= 0) {
                return null;
            }
            equip.setField(this.field(equip.getPurifySuperHistory().get(index), dur, bn));
            equip.setPurifySuperHistory(new ArrayList<>());
        }
        Integer equipType = DataGlobalFunc.GetItemTypeByItemTypeId(equip.getType());
        EquipAlike alike = DataGlobalFunc.getEquipData(equip.getType());
        equip = EquipUtils.scoreUpdate(equip, 2, genre, alike.getBaseScore());
        equip.setName(alike.getName());
        BagEquip newEquip = new BagEquip();
        newEquip.setType(equip.getType());
        newEquip.setName(equip.getName());
        this.animation(role, equip, newEquip, equipType);
        if ((int)pos < 0) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(String.format("穿上装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", equip.getType(), equip.getType(), equip.getType(), newEquip.getName())));
        }
        this.refresh(role, equip, pos, id, true);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)4, "装备重铸替换" + JsonUtils.object2String(equip), role.getMarking());
        return null;
    }
    
    public List<FiedEquip> field(List<FiedEquip> field, int dur, int bn) {
        field.forEach(e -> {
            if (e.getType().equals(6672)) {
                e.setValue(dur);
            }
            else if (e.getType().equals(6670)) {
                e.setValue(bn);
            }
        });
        field.removeIf(e -> e.getType().equals(6680));
        return field;
    }
    
    public BagEquip recast(BagEquip equip, int bn) {
        int pj = this.getPj(equip.getType());
        if (pj >= 5) {
            SysSinBieEq bieEq = EquipDataPool.sinBieEq.get(equip.getType());
            if (bieEq == null) {
                equip.setPurifyGeneralHistory(new ArrayList<>());
                equip.setPurifyHighHistory(new ArrayList<>());
                equip.setPurifySuperHistory(new ArrayList<>());
            }
            else {
                if (equip.getPurifyGeneralHistory().size() > 0) {
                    equip.setPurifyGeneralHistory(this.purifyField(bieEq, equip.getPurifyGeneralHistory(), bn, pj));
                }
                if (equip.getPurifyHighHistory().size() > 0) {
                    List<List<FiedEquip>> purifyHighHistoryList = new ArrayList<>();
                    for (int i = 0; i < equip.getPurifyHighHistory().size(); ++i) {
                        List<FiedEquip> fiedEquips = equip.getPurifyHighHistory().get(i);
                        purifyHighHistoryList.add(this.purifyField(bieEq, fiedEquips, bn, pj));
                    }
                    equip.setPurifyHighHistory(purifyHighHistoryList);
                }
                if (equip.getPurifySuperHistory().size() > 0) {
                    List<List<FiedEquip>> purifySuperHistoryList = new ArrayList<>();
                    for (int i = 0; i < equip.getPurifySuperHistory().size(); ++i) {
                        List<FiedEquip> fiedEquips = equip.getPurifySuperHistory().get(i);
                        purifySuperHistoryList.add(this.purifyField(bieEq, fiedEquips, bn, pj));
                    }
                    equip.setPurifySuperHistory(purifySuperHistoryList);
                }
            }
        }
        return equip;
    }
    
    public List<FiedEquip> purifyField(SysSinBieEq bieEq, List<FiedEquip> fiedEquip, int bn, int pj) {
        List<FiedEquip> list = new ArrayList<>();
        FiedEquip field = fiedEquip.stream().filter(e -> e.getType().equals(6681)).findFirst().orElse(null);
        if (field != null) {
            FiedEquip purify = this.getPurify(fiedEquip, pj);
            double refining = (double)(int)field.getValue() / 1000.0;
            list = MakeAttribute.createBaseBieEq(refining, bieEq.getCoef(), bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, false);
            if (purify != null) {
                list.add(purify);
            }
        }
        return list;
    }
}
