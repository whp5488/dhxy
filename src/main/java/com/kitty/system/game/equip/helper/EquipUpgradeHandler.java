package com.kitty.system.game.equip.helper;

import java.util.LinkedHashMap;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.common.ADORN;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import java.text.MessageFormat;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipUpgrade")
public class EquipUpgradeHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "升级材料数量不足";
    public static final String SUCCESS = "消耗{0}合成{1}";
    public static final String NOT_LEVEL = "等级升到{0}级才能升级该装备";
    
    public EquipUpgradeHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        Integer order = DataGlobalFunc.GetItemTypeByItemTypeId(equip.getType());
        int nextSerial = 0;
        if (order == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
            SysNewBieEq bieEq2 = EquipDataPool.newBieEq.get(equip.getType());
            nextSerial = this.nextSerial(bieEq2.getNeedGrade(), bieEq2.getSerial(), bieEq2.getPos());
        }
        else if (order == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
            SysSeniorBieEq bieEq = EquipDataPool.seniorBieEq.get(equip.getType());
            nextSerial = this.nextSerial(bieEq.getNeedGrade(), bieEq.getSerial(), bieEq.getPos());
        }
        SysSeniorBieEq bieEq = EquipDataPool.seniorBieEq.get(nextSerial);
        if (nextSerial <= 0 || bieEq == null) {
            return "该装备不可以升级";
        }
        int needGrade = bieEq.getNeedGrade();
        int needRei = bieEq.getNeedRei();
        String msg = needRei + "转";
        if (role.getRelive() < needRei) {
            return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
        }
        if (role.getLevel() < needGrade) {
            return MessageFormat.format("等级升到{0}级才能升级该装备", ((needRei > 0) ? msg : "") + needGrade);
        }
        Map<Integer, Integer> consume = this.consume(needGrade);
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_COIN)) {
                if (role.getCoin() < (long)(int)amount) {
                    return "升级材料数量不足";
                }
            }
            else {
                Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return "升级材料数量不足";
                }
            }
        }
        String materials = "";
        for (Integer key2 : consume.keySet()) {
            Integer amount2 = consume.get(key2);
            if (key2.equals(Const.RESTYPE_COIN)) {
                materials = materials + "#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours((long) amount2) + ",";
                this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount2, "EquipUpgrade");
            }
            else {
                SysDataStuff stuff = ItemDataPool.data_stuff.get(key2);
                materials += String.format("#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", stuff.getSerial(), stuff.getSerial(), stuff.getSerial(), stuff.getName());
                if ((int)amount2 > 1) {
                    materials = materials + "x" + amount2 + ",";
                }
                else {
                    materials += ",";
                }
                Integer integer = this.bagService.consumeBagGoods(role, key2, amount2, true, true, "装备升级");
                if (integer != null) {
                    return null;
                }
            }
        }
        materials = materials.substring(0, materials.length() - 1);
        int bn = 0;
        Optional<FiedEquip> first = equip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        BagEquip newEq = new BagEquip();
        BeanUtils.copyBeanProp(newEq, equip);
        List<FiedEquip> field = MakeAttribute.createHighBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getAttrFactor(), bn);
        newEq = this.clearHistory(newEq);
        newEq.setField(field);
        newEq.setType(bieEq.getSerial());
        newEq.setName(bieEq.getName());
        newEq.setField(field);
        Integer score = MakeAttribute.calEquipScore(newEq);
        field.add(new FiedEquip(6679, Integer.valueOf(score + bieEq.getBaseScore())));
        newEq.setField(field);
        if ((int)pos < 0) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(String.format("穿上装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), Integer.valueOf(newEq.getType()), newEq.getName())));
        }
        this.refresh(role, newEq, pos, id, true);
        role.save();
        this.animation(role, equip, newEq, ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue());
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), MessageFormat.format("消耗{0}合成{1}", materials, newEq.getName()), false));
        return null;
    }
    
    public int nextSerial(int grade, int serial, int pos) {
        if (grade == 1) {
            serial += 1040000;
        }
        else if (grade < 100) {
            serial -= 10000;
        }
        else if (grade == 100) {
            serial += 40000;
            if (ADORN.SHOES.getValue() == pos) {
                int round = RandomUtil.round(1, 1000);
                if (round < 500) {
                    ++serial;
                }
            }
        }
        else {
            serial += 10000;
            if (ADORN.SHOES.getValue() == pos) {
                int round = RandomUtil.round(1, 1000);
                if (round < 500) {
                    ++serial;
                }
            }
        }
        return serial;
    }
    
    public Map<Integer, Integer> consume(int grade) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (grade == 40) {
            map.put(75030, 1);
            map.put(Const.RESTYPE_COIN, 50000);
        }
        else if (grade == 60) {
            map.put(75030, 1);
            map.put(Const.RESTYPE_COIN, 100000);
        }
        else if (grade == 80) {
            map.put(75030, 1);
            map.put(Const.RESTYPE_COIN, 200000);
        }
        else if (grade == 100) {
            map.put(75030, 1);
            map.put(Const.RESTYPE_COIN, 500000);
        }
        else if (grade == 120) {
            map.put(75030, 10);
            map.put(75047, 10);
            map.put(Const.RESTYPE_COIN, 800000);
        }
        else if (grade == 140) {
            map.put(75030, 50);
            map.put(75047, 50);
            map.put(Const.RESTYPE_COIN, 2000000);
        }
        return map;
    }
}
