package com.kitty.system.game.equip.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipSeniorRemold")
public class EquipSeniorRemoldHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "重铸材料数量不足";
    
    public EquipSeniorRemoldHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("高级装备重铸：获取物品所在位置为 = {}，获取物品ID = {}", role.getPos(), id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        SysSeniorBieEq bieEq = EquipDataPool.seniorBieEq.get(equip.getType());
        Map<Integer, Integer> consume = this.consume(bieEq.getNeedGrade(), type);
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                if (role.getSilver() < (long)(int)amount) {
                    return "重铸材料数量不足";
                }
            }
            else {
                Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return "重铸材料数量不足";
                }
            }
        }
        boolean deduct = this.deduct(role, consume);
        if (!deduct) {
            return "道具或者货币扣除失败";
        }
        List<EquipNeo> list = new ArrayList<>();
        BagEquip calculate = new BagEquip();
        BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
        int count = DataGlobalFunc.expend((int)type);
        int bn = DataGlobalFunc.getGemNum(equip);
        List<List<FiedEquip>> redo = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            List<FiedEquip> field = MakeAttribute.createHighBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bieEq.getAttrFactor(), bn);
            calculate.setField(field);
            Integer score = MakeAttribute.calEquipScore(calculate);
            field.add(new FiedEquip(6679, score + bieEq.getBaseScore()));
            list.add(new EquipNeo(field));
            redo.add(field);
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)0, param, list, (byte)(int)type));
        equip = this.remoldSave(equip, redo, type);
        this.refresh(role, equip, pos, id, false);
        return null;
    }
    
    public Map<Integer, Integer> consume(int grade, int type) {
        int num = DataGlobalFunc.expend(type);
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (grade == 40) {
            map.put(75030, 1 * num);
            map.put(Const.RESTYPE_SILVER, 20000 * num);
        }
        else if (grade == 60) {
            map.put(75030, 1 * num);
            map.put(Const.RESTYPE_SILVER, 40000 * num);
        }
        else if (grade == 80) {
            map.put(75030, 1 * num);
            map.put(Const.RESTYPE_SILVER, 100000 * num);
        }
        else if (grade == 100) {
            map.put(75030, 1 * num);
            map.put(Const.RESTYPE_SILVER, 200000 * num);
        }
        else if (grade == 120) {
            map.put(75030, 5 * num);
            map.put(75047, 2 * num);
            map.put(Const.RESTYPE_SILVER, 250000 * num);
        }
        else if (grade == 140) {
            map.put(75030, 10 * num);
            map.put(75047, 5 * num);
            map.put(Const.RESTYPE_SILVER, 500000 * num);
        }
        return map;
    }
}
