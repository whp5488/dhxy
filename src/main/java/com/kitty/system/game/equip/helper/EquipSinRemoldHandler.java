
package com.kitty.system.game.equip.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import com.kitty.common.utils.MapObjectUtil;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPreUpgradeEquip;
import com.kitty.system.game.equip.make.MakeAttribute;
import java.util.Random;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.equip.helper.vo.EquipNeo;
import java.util.ArrayList;
import com.kitty.system.game.bag.domain.BagEquip;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSinBieEq;
import java.util.List;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("EquipSinRemold")
public class EquipSinRemoldHandler extends UpgradeHandler
{
    public static final String NOT_FIND_STUFF = "需要{0}个悔梦石";
    public static final int SERIAL = 75026;
    public static final int SILVER = 1000000;
    
    @Override
    public String acceptUpgrade(final Role role, final Integer pos, final Integer type, final String param, final String cause) {
        final Long id = Long.valueOf(param);
        final EquipInfo equipInfo = this.getEquip(role, pos, id);
        if (equipInfo.getTips() != null) {
            this.logger.error("角色编号：{}，仙器重铸：获取物品所在位置为 = {}，获取物品ID = {}", new Object[] { role.getRoleId(), pos, id });
            return equipInfo.getTips();
        }
        BagEquip equip = equipInfo.getEquip();
        final int position = equip.getPosition();
        final int pj = this.getPj(equip.getType());
        if (pj < 1 || pj > 6) {
            return null;
        }
        final int count = this.expend(type);
        final Integer goods = this.bagService.getBagGoods(role, 75026, count);
        if (goods != null) {
            return MessageFormat.format("需要{0}个悔梦石", goods);
        }
        final int consumeSilver = this.consumeSilver(pj, count);
        if (consumeSilver < 0) {
            return null;
        }
        final boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, consumeSilver);
        if (!flag) {
            return null;
        }
        this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, consumeSilver, "EquipSinRemold");
        final Integer integer = this.bagService.consumeBagGoods(role, 75026, count, true, true, "仙器重铸");
        if (integer != null) {
            return null;
        }
        final List<SysSinBieEq> bieEqs = EquipDataPool.sinBieEq.values().stream().collect(Collectors.toList());
        final List<SysSinBieEq> collect = bieEqs.stream().filter(eq -> eq.getPos() == position && (eq.getOwnerRole().contains(0) || eq.getOwnerRole().contains(role.getSerial()) || eq.getOwnerRole().contains(role.getRace())) && (eq.getSex() == 0 || role.getSex() == eq.getSex()) && (eq.getRace() == 0 || role.getRace() == eq.getRace()) && this.getPj(eq.getSerial()) == pj).collect(Collectors.toList());
        final List<SysSinBieEq> sysSinBieEqs = this.deWeight(collect, equip.getField());
        if (sysSinBieEqs.size() < 1) {
            this.logger.error("重铸仙器获取属性空，装备ID{}", id);
            return null;
        }
        final BagEquip calculate = new BagEquip();
        final List<EquipNeo> list = new ArrayList<EquipNeo>();
        BeanUtils.copyBeanProp(calculate, equipInfo.getEquip());
        final int bn = DataGlobalFunc.getGemNum(equip);
        final FiedEquip purify = this.getPurify(equip.getField(), pj);
        final List<List<FiedEquip>> redo = new ArrayList<List<FiedEquip>>();
        final Random random = new Random();
        for (int i = 0; i < count; ++i) {
            final SysSinBieEq bieEq = this.getBieEq(random, sysSinBieEqs);
            final List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), bn, bieEq.getCoef(), equip.getRefining());
            if (pj >= 5 && purify != null) {
                field.add(purify);
            }
            calculate.setField(field);
            final Integer score = MakeAttribute.calEquipScore(calculate);
            field.add(new FiedEquip(6679, score + bieEq.getBaseScore()));
            field.add(new FiedEquip(6680, bieEq.getSerial()));
            list.add(new EquipNeo(field));
            redo.add(field);
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeEquip((byte)0, param, list, (byte)(int)type));
        equip = this.remoldSave(equip, redo, type);
        this.refresh(role, equip, pos, id, false);
        return null;
    }
    
    public SysSinBieEq getBieEq(final Random random, final List<SysSinBieEq> sysSinBieEqs) {
        final SysSinBieEq bieEq = sysSinBieEqs.get(random.nextInt(sysSinBieEqs.size()));
        return bieEq;
    }
    
    public List<SysSinBieEq> deWeight(final List<SysSinBieEq> list, final List<FiedEquip> field) {
        final List<String> fieldList = new ArrayList<String>();
        for (final FiedEquip fiedEquip : field) {
            final String dist = CONVERT_DICT.getPro(fiedEquip.getType());
            if (dist != null) {
                fieldList.add(dist);
            }
        }
        list.forEach(e -> {
            final Map<String, Integer> baseMap = MapObjectUtil.getStringToMapStrInteger(e.getBaseAttr());
            final List<String> strings = new ArrayList<String>(baseMap.keySet());
            final List<String> diff = getDiffRent(strings, fieldList);
            if (diff.size() <= 0) {
                list.remove(e);
            }
            return;
        });
        return list;
    }
    
    public static List<String> getDiffRent(final List<String> list1, final List<String> list2) {
        final Map<String, Integer> map = new HashMap<String, Integer>(list1.size() + list2.size());
        final List<String> diff = new ArrayList<String>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (final String string : maxList) {
            map.put(string, 1);
        }
        for (final String string : minList) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
            }
            else {
                map.put(string, 1);
            }
        }
        for (final Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }
    
    public int consumeSilver(final int pj, final int count) {
        switch (pj) {
            case 1: {
                return 1000000 * count;
            }
            case 2: {
                return 2000000 * count;
            }
            case 3: {
                return 3000000 * count;
            }
            case 4: {
                return 4000000 * count;
            }
            case 5: {
                return 8000000 * count;
            }
            case 6:
                return 16000000 * count;
            default: {
                return -1;
            }
        }
    }
}
