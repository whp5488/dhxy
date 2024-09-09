package com.kitty.system.game.equip.helper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import com.kitty.system.game.vo.FiedEquip;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.ArrayList;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("AcsryRemold")
public class AcsryRemoldHandler extends UpgradeHandler
{
    public static final List<Integer> integers;
    public static final String NOT_FIND_STUFF = "所需材料不足";
    
    public AcsryRemoldHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Long id = Long.valueOf(param);
        EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("配饰重铸：获取物品所在位置为 = {}，获取物品ID = {}", pos, id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        SysDataAcsry acsry = EquipDataPool.acsryMap.get(equip.getType());
        if (acsry == null) {
            return null;
        }
        Map<Integer, Integer> consume = this.consume(type);
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                if (role.getSilver() < (long)(int)amount) {
                    return "所需银两不足";
                }
            }
            else {
                Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return "所需材料不足";
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
        int count = DataGlobalFunc.expend(type);
        List<List<FiedEquip>> redo = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            List<FiedEquip> field = MakeAttribute.createBaseAcsry(acsry, equip.getField(), AcsryRemoldHandler.integers);
            list.add(new EquipNeo(field));
            redo.add(field);
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)0, param, list, (byte)(int)type));
        equip = this.remoldSave(equip, redo, type);
        this.refresh(role, equip, (int)pos, id, false);
        return null;
    }
    
    public Map<Integer, Integer> consume(int type) {
        Map<Integer, Integer> map = new LinkedHashMap<>();
        if (type == 1) {
            map.put(75031, 1);
            map.put(75048, 5);
            map.put(Const.RESTYPE_SILVER, 300000);
        }
        else if (type == 2) {
            map.put(75031, 5);
            map.put(75048, 25);
            map.put(Const.RESTYPE_SILVER, 1425000);
        }
        else if (type == 3) {
            map.put(75031, 20);
            map.put(75048, 100);
            map.put(Const.RESTYPE_SILVER, 5400000);
        }
        return map;
    }
    
    static {
        integers = Arrays.asList(92, 7, 1);
    }
}
