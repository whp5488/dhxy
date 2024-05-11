
package com.kitty.system.game.equip.helper;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
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
    
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final Long id = Long.valueOf(param);
        final EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("配饰重铸：获取物品所在位置为 = {}，获取物品ID = {}", (Object)pos, (Object)id);
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        final SysDataAcsry acsry = EquipDataPool.acsryMap.get(equip.getType());
        if (acsry == null) {
            return null;
        }
        final Map<Integer, Integer> consume = this.consume(type);
        for (final Integer key : consume.keySet()) {
            final Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                if (role.getSilver() < amount) {
                    return "所需银两不足";
                }
                continue;
            }
            else {
                final Integer num = this.bagService.getBagGoods(role, key, amount);
                if (num != null) {
                    return "所需材料不足";
                }
                continue;
            }
        }
        final boolean deduct = this.deduct(role, consume);
        if (!deduct) {
            return "道具或者货币扣除失败";
        }
        final List<EquipNeo> list = new ArrayList<EquipNeo>();
        final BagEquip calculate = new BagEquip();
        BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
        final int count = this.expend(type);
        final List<List<FiedEquip>> redo = new ArrayList<List<FiedEquip>>();
        for (int i = 0; i < count; ++i) {
            final List<FiedEquip> field = MakeAttribute.createBaseAcsry(acsry, equip.getField(), AcsryRemoldHandler.integers);
            list.add(new EquipNeo(field));
            redo.add(field);
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)0, param, list, (byte)(int)type));
        equip = this.remoldSave(equip, redo, type);
        this.refresh(role, equip, pos, id, false);
        return null;
    }
    
    public Map<Integer, Integer> consume(final int type) {
        final Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
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
