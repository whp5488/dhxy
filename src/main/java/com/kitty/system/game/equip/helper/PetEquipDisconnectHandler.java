package com.kitty.system.game.equip.helper;

import com.kitty.common.utils.RandomUtil;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.game.vo.utils.Const;
import java.util.Iterator;
import java.util.Map;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.message.RespPetEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.common.utils.NumberUtil;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("PetEquipDisconnect")
public class PetEquipDisconnectHandler extends UpgradeHandler
{
    public PetEquipDisconnectHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Integer pos, Integer type, String param, String cause) {
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return null;
        }
        if (type.equals(1)) {
            if (!NumberUtil.isNumber(param)) {
                return null;
            }
            Long id = Long.valueOf(param);
            PetEquip equip2 = petEquipMap.get(id);
            if (equip2 == null) {
                return null;
            }
            if (equip2.getPId() > 0L) {
                return "请先卸下装备，再分解";
            }
            if (equip2.getLock() > 0) {
                return "该物品已被锁定，请解锁后再试";
            }
            SysPetAcsry sysPetAcsry2 = EquipDataPool.petAcsryMap.get(equip2.getSerial());
            if (sysPetAcsry2 == null) {
                return null;
            }
            Map<Integer, Long> decompose2 = this.decompose(sysPetAcsry2.getGrade(), equip2.getProductOrder());
            if (decompose2 == null && decompose2.size() > 0) {
                return null;
            }
            petEquipMap.remove(equip2.getId());
            equip2.setDelete(true);
            equip2.save();
            List<String> objects2 = new ArrayList<>(1);
            objects2.add(String.valueOf(equip2.getId()));
            MessagePusher.pushMessage(role.getId(), new RespPetEquip(objects2));
            this.send(role, decompose2);
        }
        else {
            String[] split = param.split("-");
            List<PetEquip> objects = new ArrayList<>();
            for (String str : split) {
                if (NumberUtil.isNumber(str)) {
                    byte order = Byte.parseByte(str);
                    List<PetEquip> collect = petEquipMap.values().stream().filter(e -> e.getProductOrder() == order && e.getPId() <= 0L && e.getLock() <= 0).collect(Collectors.toList());
                    if (collect.size() > 0) {
                        objects.addAll(collect);
                    }
                }
            }
            Map<Integer, Long> map = new HashMap<>();
            List<String> delList = new ArrayList<>(objects.size());
            for (PetEquip equip : objects) {
                SysPetAcsry sysPetAcsry = EquipDataPool.petAcsryMap.get(equip.getSerial());
                if (sysPetAcsry == null) {
                    continue;
                }
                Map<Integer, Long> decompose = this.decompose(sysPetAcsry.getGrade(), equip.getProductOrder());
                if (decompose == null && decompose.size() > 0) {
                    continue;
                }
                petEquipMap.remove(equip.getId());
                equip.setDelete(true);
                equip.save();
                for (Integer key : decompose.keySet()) {
                    Long value = map.get(key);
                    if (value != null) {
                        value = value + decompose.get(key);
                    }
                    else {
                        value = decompose.get(key);
                    }
                    map.put(key, value);
                }
                delList.add(String.valueOf(equip.getId()));
            }
            MessagePusher.pushMessage(role.getId(), new RespPetEquip(delList));
            this.send(role, map);
        }
        return null;
    }
    
    public void send(Role role, Map<Integer, Long> map) {
        for (Integer key : map.keySet()) {
            Long num = map.get(key);
            if (Const.RESTYPE_COIN.equals(key)) {
                this.roleService.addCurrency(role, Const.RESTYPE_COIN, num, "分解宠物配饰", true, false);
            }
            else {
                this.bagService.addGoods(role, key, num.intValue(), 0, true, true, "分解宠物配饰");
            }
        }
    }
    
    public Map<Integer, Long> decompose(int grade, byte suit) {
        Map<Integer, Long> map = new HashMap<>();
        if (suit == ACC_GRADE.PLAY_WITH.getValue()) {
            if (grade == 1) {
                map.put(Const.RESTYPE_COIN, 5000L);
            }
            else if (grade == 2) {
                map.put(Const.RESTYPE_COIN, 10000L);
                if (RandomUtil.round(0, 100) <= 33) {
                    map.put(75401, 1L);
                }
            }
            else if (grade == 3) {
                map.put(75401, 1L);
                map.put(Const.RESTYPE_COIN, 15000L);
            }
        }
        else if (suit == ACC_GRADE.RESERVE.getValue()) {
            if (grade == 1) {
                map.put(Const.RESTYPE_COIN, 15000L);
            }
            else if (grade == 2) {
                if (RandomUtil.round(0, 100) <= 33) {
                    map.put(75401, 1L);
                }
                map.put(Const.RESTYPE_COIN, 30000L);
            }
            else if (grade == 3) {
                map.put(75401, 1L);
                map.put(Const.RESTYPE_COIN, 45000L);
            }
        }
        else if (suit == ACC_GRADE.PRICELESS.getValue()) {
            if (grade == 1) {
                map.put(Const.RESTYPE_COIN, 50000L);
            }
            else if (grade == 2) {
                if (RandomUtil.round(0, 100) <= 33) {
                    map.put(75401, 1L);
                }
                map.put(Const.RESTYPE_COIN, 100000L);
            }
            else if (grade == 3) {
                map.put(75401, 1L);
                map.put(Const.RESTYPE_COIN, 150000L);
            }
        }
        return map;
    }
}
